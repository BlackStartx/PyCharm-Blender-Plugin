package util.core.json_util;

import util.core.json_util.values.MyJsonNode;
import util.core.json_util.values.MyJsonObject;

public class MyJsonDocument {

    private final MyJsonObject<?> root;

    public MyJsonDocument() {
        this.root = new MyJsonNode();
    }

    public MyJsonDocument(MyJsonObject<?> finalNode) {
        this.root = finalNode;
    }

    public MyJsonObject<?> getRoot() {
        return this.root;
    }
}
