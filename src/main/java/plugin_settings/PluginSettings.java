package plugin_settings;

public class PluginSettings {
    public static final boolean isCommunity = false;

    public static String getStripeVersion() {
        return isCommunity ? "Community Edition" : "Professional Edition";
    }
}
