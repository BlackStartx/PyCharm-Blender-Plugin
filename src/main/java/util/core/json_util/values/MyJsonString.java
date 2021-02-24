package util.core.json_util.values;

import util.core.json_util.exceptions.MyJsonIllegalNodeAlteration;
import util.core.json_util.exceptions.MyJsonStringInvalidExtraction;
import util.core.json_util.MyJsonHelper;
import util.core.MyStringUtils;

import java.util.ArrayList;

public class MyJsonString extends MyJsonObject<String> {
    
    private final String outputValue;
    private final Object simpleObject;

    public MyJsonString(){
        this(null);
    }

    public MyJsonString(String value)
    {
        Value = value;
        simpleObject = value;
        outputValue = value == null ? "null" : MyStringUtils.surrounded(value.replace("\\", "\\\\").replace("\"", "\\\""), "\"");
    }

    public MyJsonString(int value)
    {
        Value = String.valueOf(value);
        outputValue = Value;
        simpleObject = value;
    }

    public MyJsonString(float value)
    {
        Value = String.valueOf(value);
        outputValue = Value;
        simpleObject = value;
    }

    public MyJsonString(long value)
    {
        Value = String.valueOf(value);
        outputValue = Value;
        simpleObject = value;
    }

    public MyJsonString(boolean value)
    {
        Value = value ? MyJsonHelper.True : MyJsonHelper.False;
        outputValue = Value;
        simpleObject = value;
    }

    @Override
    public String getStringValue(boolean ignoreType)
    {
        if(ignoreType) return Value;
        if(simpleObject instanceof String) return (String) simpleObject;
        throw new MyJsonStringInvalidExtraction();
    }

    @Override
    public int getIntValue()
    {
        if (simpleObject instanceof Integer) return (int) simpleObject;
        throw new MyJsonStringInvalidExtraction();
    }

    @Override
    public String getJsonString()
    {
        return outputValue;
    }

    @Override
    public boolean deleteKeyIf(MyJsonNode.IfKey ifKey, boolean recursive, boolean inString)
    {
        return inString && ifKey.ifKey(Value);
    }

    /*
        EXCEPTIONS
        EXCEPTIONS
        EXCEPTIONS
     */

    @Override
    public MyJsonObject<?> get(String key)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to get an Object from a key in a JSONString.");
    }

    @Override
    public String[] getKeysAsSet()
    {
        throw new MyJsonIllegalNodeAlteration("Trying to get the keys in a JSONString.");
    }

    @Override
    public void remove(String key)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to remove a key on a JSONString.");
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, boolean addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONString.");
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, int addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONString.");
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, float addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONString.");
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, String addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONString.");
    }

    @Override
    public MyJsonNode addKey(String key, MyJsonObject<?> innerObject)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONString.");
    }

    @Override
    public <T> MyJsonNode addKeyJsonArray(String key, Iterable<T> array, ForEach<T> each)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a json array key on a JSONArray.");
    }

    @Override
    public MyJsonNode addKeyJsonArray(String key, ArrayList<MyJsonObject<?>> addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a json array key on a JSONString.");
    }

    @Override
    public MyJsonArray addValue(MyJsonObject<?> newValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a value on a JSONString.");
    }
}
