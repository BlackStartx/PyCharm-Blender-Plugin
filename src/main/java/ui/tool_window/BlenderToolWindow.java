package ui.tool_window;

import com.intellij.execution.*;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.PathMappingSettings;
import com.intellij.util.messages.MessageBusConnection;
import com.jetbrains.python.console.PythonDebugLanguageConsoleView;
import com.intellij.python.pro.debugger.remote.PyRemoteDebugConfiguration;
import com.intellij.python.pro.debugger.remote.PyRemoteDebugConfigurationFactory;
import com.intellij.python.pro.debugger.remote.PyRemoteDebugConfigurationType;
import data.*;
import icons.BlendCharmIcons;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import plugin_settings.PluginSettings;
import settings.BlenderSettings;
import ui.dialogs.add_blender_instance.AddBlenderInstanceWrapper;
import ui.dialogs.debug_popup.DebugPopupWrapper;
import ui.dialogs.remove_blender_instance.RemoveBlenderInstanceWrapper;
import util.MyInputStreamHelper;
import util.MyProjectHolder;
import util.MySwingUtil;
import util.core.MyFileUtils;
import util.core.socket.MySocketConnection;
import util.core.socket.server.MyServerSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BlenderToolWindow {

    private static final String blenderRunFileName = "pycharm_connector.py";
    private static final File runningFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "BlendCharm" + File.separator + blenderRunFileName);
    private static final File egg = BlenderToolWindowUtils.getEggFile();
    private static final int debugPort = 8132;
    private static final int socketPort = 8525;
    private static DefaultListModel<RunningBlenderProcess> runningInstancesAdapter;
    private final BlenderSettings blenderSettings;
    private final MyProjectHolder project;
    private final MyServerSocket currentSocket = new MyServerSocket("localhost", socketPort);
    private JPanel myToolWindowContent;
    private JComboBox<BlenderInstance> blenderInstances;
    private JBLabel button_add;
    private JBLabel button_remove;
    private JBLabel start;
    private JBLabel debug;
    private JBList<RunningBlenderProcess> runningInstances;
    private JPanel consolePanel;
    private JPanel nullPanel;
    private JBLabel button_settings;
    private JBLabel blenderLogo;

    BlenderToolWindow(@NotNull Project project) {
        this.project = new MyProjectHolder(project);
        this.blenderSettings = BlenderSettings.getBlenderSettings(this.project);
        initIcons();
        init();

        for (BlenderInstance savedBlenderInstance : blenderSettings.getBlenderInstances())
            blenderInstances.addItem(savedBlenderInstance);

        blenderInstances.addItemListener(this::onBlenderInstanceChange);
        MySwingUtil.setLabelOnClickListener(button_add, this::onAddClick);
        MySwingUtil.setLabelOnClickListener(button_remove, this::onRemoveClick);
        MySwingUtil.setLabelOnClickListener(button_settings, this::onSettingsClick);

        MySwingUtil.setLabelOnClickListener(start, this::onStartClick);
        MySwingUtil.setLabelOnClickListener(debug, this::onDebugClick);

        validateFile(true);
        updateButtons();

        runningInstancesAdapter = new DefaultListModel<>();
        runningInstances.setModel(runningInstancesAdapter);
        runningInstances.setCellRenderer(new RunningBlenderProcessRenderer());
        runningInstances.addListSelectionListener(e -> setConsoleView(getSelectedRunningProcess()));
    }

    private void initIcons() {
        button_settings.setIcon(AllIcons.General.Gear);
        start.setIcon(AllIcons.Actions.Execute);
        button_add.setIcon(AllIcons.General.Add);
        debug.setIcon(AllIcons.Actions.StartDebugger);
        button_remove.setIcon(AllIcons.General.Remove);
        blenderLogo.setIcon(BlendCharmIcons.BLENDER_LOGO_BIG);
    }

    private void init() {
        MessageBusConnection connection = project.getProject().getMessageBus().connect();
        connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
            @Override
            public void after(@NotNull List<? extends VFileEvent> events) {
                for (VFileEvent event : events) if (event.isFromSave()) onSave(event.getFile());
            }
        });
    }

    private RunningBlenderProcess getSelectedRunningProcess() {
        return runningInstances.getSelectedIndex() == -1 ? null : runningInstancesAdapter.get(runningInstances.getSelectedIndex());
    }

    private boolean isSelectedInstanceValid() {
        return blenderInstances.getModel().getSize() != 0;
    }

    private void onSave(VirtualFile virtualFile) {
        for (int i = 0; i < runningInstancesAdapter.size(); i++)
            intellijConsoleInfoPrintLn(runningInstancesAdapter.get(i).getConsole(), "[On Save: VFS_CHANGE]");
        String ofAddon = new VirtualBlenderFile(project, virtualFile).getRelativeAddonName();
        if (ofAddon != null) reloadAddons(new String[]{ofAddon});
    }

    private void reloadAddons(String[] strings) {
        for (int i = 0; i < runningInstancesAdapter.size(); i++) {
            RunningBlenderProcess instance = runningInstancesAdapter.get(i);
            MySocketConnection socket = instance.getSocket();
            if (socket == null) {
                intellijConsoleInfoPrintLn(instance.getConsole(), "[On Save: ERROR - Socket connection lost]");
                continue;
            }
            socket.sendJsonData(new JSONObject()
                    .put(CommunicationData.REQUEST, CommunicationData.REQUEST_PLUGIN_REFRESH)
                    .put(CommunicationData.REQUEST_PLUGIN_REFRESH_NAME_LIST, new JSONArray().putAll(strings))
            );
            intellijConsoleInfoPrintLn(instance.getConsole(), "[On Save: Plugin reload request sent for:");
            for (String s : strings) intellijConsoleInfoPrintLn(instance.getConsole(), " - " + s);
            intellijConsoleInfoPrintLn(instance.getConsole(), "]");
        }
    }

    private BlenderInstance getSelectedBlenderInstance() {
        return (BlenderInstance) blenderInstances.getSelectedItem();
    }

    private void updateButtons() {
        start.setEnabled(isSelectedInstanceValid());
        debug.setEnabled(isSelectedInstanceValid() && !PluginSettings.isCommunity);

        button_remove.setEnabled(getSelectedBlenderInstance() != null);
        button_settings.setEnabled(getSelectedBlenderInstance() != null);
    }

    private void addConfiguration(BlenderInstance configuration) {
        blenderSettings.addBlenderInstance(configuration);
        blenderInstances.addItem(configuration);
    }

    private void onBlenderInstanceChange(ItemEvent itemEvent) {
        updateButtons();
    }

    JPanel getContent() {
        return myToolWindowContent;
    }

    /**
     * Validate that the runningFile variable could be used.
     *
     * @return true if the file exist or has been successfully created.
     */
    private boolean validateFile(boolean checkContent) {
        if (runningFile.exists() && !checkContent) return true;

        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("Python/pycharm_connector.py");
        if (resourceAsStream == null) return false;
        String realContent = MyInputStreamHelper.readString(resourceAsStream);

        if (runningFile.exists()) {
            if (realContent.equals(MyFileUtils.readString(runningFile.toPath()))) return true;
            if (!runningFile.delete()) return false;
        }
        File dirs = runningFile.getParentFile();

        if (dirs.exists() || dirs.mkdirs()) MyFileUtils.write(runningFile.toPath(), realContent);
        return runningFile.exists();
    }

    /*
     *  Socket methods
     */

    private void onInstanceConnectionStart(MySocketConnection socket, RunningBlenderProcess runningBlenderProcess) {
        runningBlenderProcess.assignSocket(socket);
        blenderSettings.removeDeletedAddon(project);
        socket.sendJsonData(new JSONObject()
                .put(CommunicationData.REQUEST, CommunicationData.REQUEST_PLUGIN_FOLDER)
                .put(CommunicationData.REQUEST_PLUGIN_FOLDER_PROJECT_FOLDER, project.getBasePath())
                .put(CommunicationData.REQUEST_PLUGIN_FOLDER_ADDON_NAMES, new JSONArray().putAll(blenderSettings.getBlenderAddons()))
        );
    }

    private void onInstanceMessage(MySocketConnection.Data message, RunningBlenderProcess runningBlenderProcess) {
        JSONObject root = new JSONObject(new JSONTokener(message.getStringData()));
        switch (root.getInt(CommunicationData.RESPONSE)) {
            case CommunicationData.RESPONSE_PLUGIN_FOLDER -> {
                String addonPath = root.getString(CommunicationData.RESPONSE_PLUGIN_FOLDER_PLUGIN_PATH);
                String currentPath = runningBlenderProcess.getInstance().addonPath;
                if (currentPath == null || !currentPath.equals(addonPath)) {
                    runningBlenderProcess.getInstance().addonPath = addonPath;
                    if (runningBlenderProcess.isDebug()) intentionalDebugRestart(runningBlenderProcess);
                }
            }
            case CommunicationData.RESPONSE_PLUGIN_REFRESH -> root.getString(CommunicationData.RESPONSE_PLUGIN_REFRESH_STATUS);
        }
    }

    private void intentionalDebugRestart(RunningBlenderProcess runningBlenderProcess) {
        runningBlenderProcess.getProcess().destroyProcess();
        ApplicationManager.getApplication().invokeLater(this::onNewDebugInformationPopup);
    }

    private void onNewDebugInformationPopup() {
        new DebugPopupWrapper().show();
    }

    private void onInstanceConnectionEnd(RunningBlenderProcess runningBlenderProcess) {
        runningBlenderProcess.getProcess().destroyProcess();
        ApplicationManager.getApplication().invokeLater(() -> runningInstancesAdapter.removeElement(runningBlenderProcess));
    }

    /*
     *  Process Starting
     */

    @NotNull
    private RunningBlenderProcess startBlenderProcess() throws ExecutionException {
        return startBlenderProcess(false, () -> {
        });
    }

    @NotNull
    private RunningBlenderProcess startBlenderProcess(boolean debugMode, Runnable onEnd) throws ExecutionException {
        if (currentSocket.open()) {
            BlenderInstance instance = getSelectedBlenderInstance();
            OSProcessHandler processHandler = createBlenderProcessHandler(instance, debugMode, blenderSettings.data().showVerbose);
            RunningBlenderProcess runningBlenderProcess = new RunningBlenderProcess(instance, processHandler, debugMode);

            currentSocket.asyncWaitClient(new MySocketConnection.MySocketConnectionInterface() {
                @Override
                public void onConnectionStart(MySocketConnection socket) {
                    onInstanceConnectionStart(socket, runningBlenderProcess);
                }

                @Override
                public void onMessage(MySocketConnection socket, MySocketConnection.Data message) {
                    onInstanceMessage(message, runningBlenderProcess);
                }

                @Override
                public void onEnd(MySocketConnection socket) {
                    onInstanceConnectionEnd(runningBlenderProcess);
                    onEnd.run();
                }
            });

            runningInstancesAdapter.addElement(runningBlenderProcess);
            runningInstances.setSelectedIndex(runningInstancesAdapter.size() - 1);
            return runningBlenderProcess;
        }
        throw new ExecutionException("Socket not open!");
    }

    private OSProcessHandler createBlenderProcessHandler(BlenderInstance instance, boolean debugMode, boolean print) throws ExecutionException {
        ArrayList<String> command = new ArrayList<>();

        command.add(instance.path);
        command.add("--python");
        command.add(runningFile.getPath());
        command.add("--");
        if (print) {
            command.add("print_on");
            command.add(".");
        }
        if (debugMode && egg != null && egg.exists()) {
            command.add("debug_mode");
            command.add(".");
            command.add("debug_port");
            command.add(String.valueOf(debugPort));
            command.add("debug_egg");
            command.add(egg.getPath());
        }

        GeneralCommandLine generalCommandLine = new GeneralCommandLine(command);
        generalCommandLine.withEnvironment(instance.environment);
        generalCommandLine.setCharset(StandardCharsets.UTF_8);
        generalCommandLine.setWorkDirectory(project.getBasePath());
        return new OSProcessHandler(generalCommandLine);
    }

    /*
     *  Label Listener
     */

    private void onAddClick() {
        AddBlenderInstanceWrapper dialog = new AddBlenderInstanceWrapper(project.getProject(), null);
        if (dialog.showAndGet()) addConfiguration(dialog.form.getNewConfiguration());
    }

    private void onRemoveClick() {
        if (getSelectedBlenderInstance() == null) return;

        RemoveBlenderInstanceWrapper dialog = new RemoveBlenderInstanceWrapper();
        if (dialog.showAndGet()) {
            blenderSettings.removeBlenderInstances(getSelectedBlenderInstance());
            blenderInstances.removeItem(getSelectedBlenderInstance());
            updateButtons();
        }
    }

    private void onSettingsClick() {
        BlenderInstance blenderInstance = getSelectedBlenderInstance();
        if (blenderInstance == null) return;

        AddBlenderInstanceWrapper dialog = new AddBlenderInstanceWrapper(project.getProject(), blenderInstance);
        if (dialog.showAndGet()) dialog.form.updateConfiguration(blenderInstance);
    }

    private void setConsoleView(RunningBlenderProcess runningBlenderProcess) {
        consolePanel.removeAll();
        consolePanel.revalidate();
        consolePanel.repaint();
        consolePanel.add(runningBlenderProcess != null ? runningBlenderProcess.getComponent() : nullPanel, BorderLayout.CENTER);
    }

    private void destroyDebugInstance(RunContentDescriptor runContentDescriptor) {
        if (runContentDescriptor.getProcessHandler() == null) return;
        runContentDescriptor.getProcessHandler().destroyProcess();
    }

    private void intellijConsoleInfoPrintLn(ConsoleView console, String message) {
        if (!blenderSettings.data().showVerbose) return;
        console.print(message + "\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
    }

    private void onStartClick() {
        if (validateFile(false)) {
            try {
                ConsoleView console = TextConsoleBuilderFactory.getInstance().createBuilder(project.getProject()).getConsole();

                RunningBlenderProcess runningBlenderProcess = startBlenderProcess();
                runningBlenderProcess.setComponent(console.getComponent());

                setConsoleView(runningBlenderProcess);

                runningBlenderProcess.setConsole(console);
                console.attachToProcess(runningBlenderProcess.getProcess());
                runningBlenderProcess.getProcess().startNotify();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void onDebugClick() {
        if (PluginSettings.isCommunity) return;

        if (validateFile(false)) {
            try {
                PyRemoteDebugConfigurationType remoteConfigurationType = PyRemoteDebugConfigurationType.getInstance();

                PyRemoteDebugConfigurationFactory factory = (PyRemoteDebugConfigurationFactory) remoteConfigurationType.getConfigurationFactories()[0];

                RunnerAndConfigurationSettings runSettings = RunManager.getInstance(project.getProject()).createConfiguration("Blender Debug", factory);
                PyRemoteDebugConfiguration configuration = (PyRemoteDebugConfiguration) runSettings.getConfiguration();

                configuration.setHost("localhost");
                configuration.setPort(debugPort);
                configuration.setSuspendAfterConnect(false);

                configuration.setMappingSettings(new PathMappingSettings() {{
                    add(new PathMapping(project.addonContainerPath().toLowerCase(), getSelectedBlenderInstance().getAddonPath()));
                }});

                Executor debugExecutorInstance = DefaultDebugExecutor.getDebugExecutorInstance();
                ExecutionEnvironment executionEnvironment = ExecutionEnvironmentBuilder.create(project.getProject(), debugExecutorInstance, configuration).build();
                ProgramRunnerUtil.executeConfigurationAsync(executionEnvironment, false, false, runContentDescriptor -> {
                    try {
                        RunningBlenderProcess runningBlenderProcess = startBlenderProcess(true, () -> destroyDebugInstance(runContentDescriptor));
                        PythonDebugLanguageConsoleView console = ((PythonDebugLanguageConsoleView) runContentDescriptor.getExecutionConsole());
                        runningBlenderProcess.setConsole(console);
                        console.attachToProcess(runningBlenderProcess.getProcess());
                        runningBlenderProcess.getProcess().startNotify();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
