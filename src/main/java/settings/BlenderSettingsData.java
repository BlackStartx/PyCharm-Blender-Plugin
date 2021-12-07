package settings;

import data.BlenderInstance;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

public class BlenderSettingsData {
    @XmlElement(name = "showVerbose")
    public boolean showVerbose;

    @XmlElement(name = "blenderPath")
    public ArrayList<BlenderInstance> blenderPaths;

    @XmlElement(name = "blenderAddonNames")
    public ArrayList<String> blenderAddOnNames;
}
