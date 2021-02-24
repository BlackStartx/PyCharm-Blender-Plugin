package data;

public class CommunicationData {

    public static final String REQUEST = "request";
    public static final int REQUEST_PLUGIN_FOLDER = 0;
    public static final String REQUEST_PLUGIN_FOLDER_PROJECT_FOLDER = "project";
    public static final String REQUEST_PLUGIN_FOLDER_ADDON_NAMES = "addon_names";

    public static final int REQUEST_PLUGIN_REFRESH = 1;
    public static final String REQUEST_PLUGIN_REFRESH_NAME_LIST = "name_list";

    public static final String RESPONSE = "response";
    public static final int RESPONSE_PLUGIN_FOLDER = REQUEST_PLUGIN_FOLDER;
    public static final String RESPONSE_PLUGIN_FOLDER_PLUGIN_PATH = "plugin_path";
    public static final int RESPONSE_PLUGIN_REFRESH = REQUEST_PLUGIN_REFRESH;
    public static final String RESPONSE_PLUGIN_REFRESH_STATUS = "status";
    public static final String RESPONSE_PLUGIN_REFRESH_NAME_LIST = "name_list";

}
