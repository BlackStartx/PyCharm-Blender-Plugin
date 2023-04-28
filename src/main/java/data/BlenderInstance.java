package data;

import javax.xml.bind.annotation.XmlElement;

public class BlenderInstance {
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "addonPath")
    public String addonPath;

    @SuppressWarnings("unused")
    public BlenderInstance() {

    }

    public BlenderInstance(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getAddonPath() {
        return addonPath == null ? null : addonPath.toLowerCase();
    }

    @Override
    public String toString() {
        return name;
    }
}
