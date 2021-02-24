import bpy


class PANEL_CLASS_NAME(bpy.types.Panel):
    bl_idname = "$ID_NAME$"
    bl_label = "$LABEL$"
    bl_category = "$CATEGORY$"
    bl_space_type = "$SPACE_TYPE$"
    bl_region_type = "$REGION_TYPE$"

    def draw(self, context):
        pass
