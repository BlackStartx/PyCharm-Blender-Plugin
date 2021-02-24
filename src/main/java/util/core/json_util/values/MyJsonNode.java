package util.core.json_util.values;

import util.core.json_util.exceptions.MyJsonIllegalNodeAlteration;
import util.core.MyOneTimeFunction;
import util.core.MyStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyJsonNode extends MyJsonObject<HashMap<String, MyJsonObject<?>>> {

    public MyJsonNode() {
        Value = new HashMap<>();
    }

    /*
     * Class
     */

    private static String asFileStringValueChild(Map.Entry<String, MyJsonObject<?>> jsonValue) {
        return MyStringUtils.surrounded(jsonValue.getKey().replace("\\", "\\\\").replace("\"", "\\\""), "\"") + ":" + jsonValue.getValue().getJsonString();
    }

    /*
     * Generic Add
     */

    @Override
    public MyJsonNode addKey(String key, MyJsonObject<?> innerObject) {
        Value.put(key, innerObject);
        return this;
    }

    /*
     * JsonString Add
     */

    @Override
    public MyJsonNode addKeyJsonString(String key, int addValue) {
        Value.put(key, new MyJsonString(addValue));
        return this;
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, float addValue) {
        Value.put(key, new MyJsonString(addValue));
        return this;
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, String addValue) {
        Value.put(key, new MyJsonString(addValue));
        return this;
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, boolean addValue) {
        Value.put(key, new MyJsonString(addValue));
        return this;
    }

    /*
     * JsonArray Add
     */

    @Override
    public <T> MyJsonNode addKeyJsonArray(String key, Iterable<T> array, ForEach<T> each) {
        ArrayList<MyJsonObject<?>> values = new ArrayList<>();
        array.forEach(t -> values.add(each.forEach(t)));
        return addKeyJsonArray(key, values);
    }

    @Override
    public MyJsonNode addKeyJsonArray(String key, ArrayList<MyJsonObject<?>> addValue) {
        Value.put(key, new MyJsonArray(addValue));
        return this;
    }

    /*
     * JsonNode Add
     */

    @Override
    public String getJsonString() {
        MyOneTimeFunction<String, Map.Entry<String, MyJsonObject<?>>> oneTimeFunction = new MyOneTimeFunction<>(MyJsonNode::asFileStringValueChild, set -> "," + asFileStringValueChild(set));
        StringBuilder builder = new StringBuilder("{");
        for (Map.Entry<String, MyJsonObject<?>> set : Value.entrySet()) builder.append(oneTimeFunction.run(set));
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void remove(String key) {
        Value.remove(key);
    }

    @Override
    public boolean deleteKeyIf(IfKey ifKey, boolean recursive, boolean inString) {
        ArrayList<String> toDelete = new ArrayList<>();
        for (String key : getKeysAsSet()) {
            if (ifKey.ifKey(key)) toDelete.add(key);
            else if (recursive)
                if (get(key).deleteKeyIf(ifKey, true, inString))
                    toDelete.add(key);
        }

        for (String delete : toDelete) remove(delete);

        return false;
    }

    @Override
    public String[] getKeysAsSet() {
        return Value.keySet().toArray(new String[0]);
    }

    @Override
    public MyJsonObject<?> get(String key) {
        return Value.getOrDefault(key, null);
    }

    /*
     *  EXCEPTIONS
     *  EXCEPTIONS
     *  EXCEPTIONS
     */

    @Override
    public MyJsonArray addValue(MyJsonObject<?> newValue) {
        throw new MyJsonIllegalNodeAlteration("Trying to add a value to a JSONNode without specifying a key.");
    }

    @Override
    public String getStringValue(boolean ignoreType) {
        throw new MyJsonIllegalNodeAlteration("Trying to get a value from a JSONNode.");
    }

    @Override
    public int getIntValue() {
        throw new MyJsonIllegalNodeAlteration("Trying to get a int from a JSONNode.");
    }

    public interface IfKey {
        boolean ifKey(String key);
    }
}