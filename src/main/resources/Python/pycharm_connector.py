import json
import os
import queue
import socket
import struct
import subprocess
import sys
import threading
import traceback
import pathlib

# noinspection PyUnresolvedReferences
import bpy

my_args = {}
if "--" in sys.argv:
    args_list = sys.argv[sys.argv.index("--") + 1:]
    my_args = {args_list[i - 1]: args_list[i] for i in range(1, len(args_list), 2)}

# # # # # # # # #
#  Main Thread  #
# # # # # # # # #

main_thread_queue = queue.Queue()


def run_in_main_thread(func, params):
    main_thread_queue.put((func, params))


# noinspection PyBroadException
def run_thread_queue():
    while not main_thread_queue.empty():
        (func, data) = main_thread_queue.get()
        try:
            func(data)
        except Exception:
            traceback.print_exc()
    return 0.1


bpy.app.timers.register(run_thread_queue, persistent=True)

# # # # # # # # #
# Communication #
# # # # # # # # #

request = "request"
request_plugin_folder = 0
request_plugin_folder_project_folder = "project"
request_plugin_folder_addon_names = "addon_names"

request_plugin_refresh = 1
request_plugin_refresh_name_list = "name_list"

response = "response"
response_plugin_folder = request_plugin_folder
response_plugin_folder_plugin_path = "plugin_path"
response_plugin_folder_failed_addons = "failed_addons"

response_plugin_refresh = request_plugin_refresh
response_plugin_refresh_status = "status"
response_plugin_refresh_name_list = request_plugin_refresh_name_list

# # # # # # # #
#   Params:   #
# # # # # # # #

print_on = False
debug_mode = None
debug_port = None
debug_egg = None

# # # # # # # #
# Cmd Params: #
# # # # # # # #

if "debug_mode" in my_args:
    debug_mode = True
if "debug_port" in my_args:
    debug_port = int(my_args["debug_port"])
if "debug_egg" in my_args:
    debug_egg = my_args["debug_egg"]
if "print_on" in my_args:
    print_on = True


def send_json_string(client, string):
    client.send(struct.pack('>b', 4) + struct.pack('>I', len(string)) + str.encode(string))


def my_print(text):
    if print_on:
        run_in_main_thread(print, text)


# noinspection PyUnresolvedReferences,PyBroadException
def on_data(client: socket, data: bytes):
    my_print("[My Socket] -- Data Received --")
    json_data = json.loads(data)
    request_id = json_data[request]
    if request_id == request_plugin_folder:
        my_print("[My Socket] ---- Request Plugin Folder ----")
        project = json_data[request_plugin_folder_project_folder]
        project_name = os.path.split(project)[1]

        script_folder = bpy.utils.user_resource('SCRIPTS', "addons")
        for addon in json_data[request_plugin_folder_addon_names]:
            if addon == ".":
                src = project
                addon = project_name
            else:
                src = os.path.join(project, addon)

            dst = os.path.join(script_folder, addon)
            if not os.path.exists(dst):
                sym_link(src, dst)
            try:
                bpy.ops.preferences.addon_enable(module=addon)
                my_print("[My Socket] ------ Enabled Addon: " + addon + " ------")
            except Exception:
                traceback.print_exc()
                my_print("[My Socket] ------ Failed Enabling Addon: " + addon + " ------")

        send_json_string(client, json.dumps({
            response: response_plugin_folder,
            response_plugin_folder_plugin_path: script_folder
        }))
    if request_id == request_plugin_refresh:
        my_print("[My Socket] ---- Data: Refresh ----")
        run_in_main_thread(reload_addons, json_data[request_plugin_refresh_name_list])


def sym_link(src, dst):
    path = pathlib.Path(dst)
    parent = path.parent
    if not parent.exists():
        os.makedirs(parent)
    if sys.platform == "win32":
        subprocess.check_call('mklink /J "' + dst + '" "' + src + '"', shell=True)
    else:
        os.symlink(str(src), str(dst), target_is_directory=True)


def start_client():
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect(('localhost', 8525))
            while True:
                my_print("[My Socket] Waiting For Data...")
                data_id_value = struct.unpack('>b', s.recv(1))[0]
                if data_id_value == 0:
                    my_print("[My Socket] -- Closed Connection --")
                    break
                on_data(s, s.recv(struct.unpack('>i', s.recv(4))[0]))
    except socket.error:
        pass
    bpy.ops.wm.quit_blender()


def connect_to_pycharm_debugger(egg_path: str, server_port: int):
    if egg_path not in sys.path:
        sys.path.append(egg_path)
    # noinspection PyUnresolvedReferences
    import pydevd_pycharm
    pydevd_pycharm.settrace('localhost', port=server_port, stdoutToServer=True, stderrToServer=True, suspend=False)


def reload_addons(module_list):
    for module in module_list:
        my_print("[My Socket] ------ Reloading: " + module + " ------")
        reload_add_on(module)


# noinspection PyBroadException
def reload_add_on(module_name):
    try:
        bpy.ops.preferences.addon_disable(module=module_name)
    except Exception:
        return {'CANCELLED'}

    for name in list(sys.modules.keys()):
        if name.startswith(module_name):
            del sys.modules[name]

    try:
        bpy.ops.preferences.addon_enable(module=module_name)
    except Exception:
        return {'CANCELLED'}

    for window in bpy.context.window_manager.windows:
        for area in window.screen.areas:
            area.tag_redraw()

    return {'FINISHED'}


x = threading.Thread(target=start_client, args=())
x.daemon = True
x.start()
if debug_mode:
    connect_to_pycharm_debugger(debug_egg, debug_port)
