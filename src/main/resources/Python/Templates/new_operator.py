import bpy


class OPERATOR_CLASS_NAME(bpy.types.Operator):
    bl_idname = "$ID_NAME$"
    bl_label = "$LABEL$"

    def execute(self, context):
        return {'FINISHED'}
