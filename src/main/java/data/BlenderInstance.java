package data;

import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

public class BlenderInstance {
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "addonPath")
    public String addonPath;
    @XmlElement(name = "environment")
    public Map<String, String> environment;

    @SuppressWarnings("unused")
    public BlenderInstance() {
    }

    public BlenderInstance(String path, String name, Map<String, String> environment) {
        this.path = path;
        this.name = name;
        this.environment = environment;
    }

    public String getAddonPath() {
        return addonPath == null ? null : addonPath.toLowerCase();
    }

    @Override
    public String toString() {
        return name;
    }
}
