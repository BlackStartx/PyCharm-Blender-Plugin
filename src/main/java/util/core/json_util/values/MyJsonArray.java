package util.core.json_util.values;

import util.core.MyIterator;
import util.core.json_util.exceptions.MyJsonIllegalNodeAlteration;
import util.core.MyOneTimeFunction;

import java.util.ArrayList;

public class MyJsonArray extends MyJsonObject<ArrayList<MyJsonObject<?>>> {


    private static String AsFileStringValueChild(MyJsonObject<?> obj)
    {
        return obj.getJsonString();
    }

    /*
     * Class
     */

    public MyJsonArray()
    {
        this(new ArrayList<>());
    }

    public MyJsonArray(ArrayList<MyJsonObject<?>> values)
    {
        Value = values;
    }

    @Override
    public MyJsonArray addValue(MyJsonObject<?> newValue)
    {
        Value.add(newValue);
        return this;
    }

    @Override
    public String getJsonString()
    {
        MyOneTimeFunction<String, MyJsonObject<?>> oneTimeFunction = new MyOneTimeFunction<>(MyJsonArray::AsFileStringValueChild, obj -> "," + AsFileStringValueChild(obj));
        StringBuilder builder = new StringBuilder("[");
        for (MyJsonObject<?> currentValue : Value) builder.append(oneTimeFunction.run(currentValue));
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean deleteKeyIf(MyJsonNode.IfKey ifKey, boolean recursive, boolean inString)
    {
        ArrayList<MyJsonObject<?>> toDelete = new ArrayList<>();
        if (recursive) toDelete.addAll(new MyIterator<>(Value).where((innerObject, index) -> innerObject.deleteKeyIf(ifKey, true, inString)).toArrayList());
        for (MyJsonObject<?> delete : toDelete) Value.remove(delete);
        return false;
    }

    /*
        EXCEPTION
        EXCEPTION
        EXCEPTION
     */

    @Override
    public MyJsonObject<?> get(String key)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to get an object from a key on a JSONArray.");
    }

    @Override
    public String[] getKeysAsSet()
    {
        throw new MyJsonIllegalNodeAlteration("Trying to get keys from a JSONArray.");
    }

    @Override
    public String getStringValue(boolean ignore)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to get a value from a JSONArray.");
    }

    @Override
    public int getIntValue()
    {
        throw new MyJsonIllegalNodeAlteration("Trying to get a int from a JSONArray.");
    }

    @Override
    public void remove(String key)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to remove a key on a JSONArray.");
    }

    @Override
    public <T> MyJsonNode addKeyJsonArray(String key, Iterable<T> array, ForEach<T> each)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a json array key on a JSONArray.");
    }

    @Override
    public MyJsonNode addKeyJsonArray(String key, ArrayList<MyJsonObject<?>> addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a json array key on a JSONArray.");
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, boolean addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONArray.");
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, int addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONArray.");
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, float addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONArray.");
    }

    @Override
    public MyJsonNode addKeyJsonString(String key, String addValue)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONArray.");
    }

    @Override
    public MyJsonNode addKey(String key, MyJsonObject<?> innerObject)
    {
        throw new MyJsonIllegalNodeAlteration("Trying to add a key on a JSONArray.");
    }
}
