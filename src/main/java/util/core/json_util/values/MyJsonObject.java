package util.core.json_util.values;

import java.util.ArrayList;

public abstract class MyJsonObject<T> {

    public T Value;

    public T getValue(){
        return Value;
    }

    public abstract String getJsonString();

    public abstract MyJsonObject<?> get(String key);

    public abstract String[] getKeysAsSet();

    public abstract String getStringValue(boolean ignoreType);

    public abstract int getIntValue();

    public abstract void remove(String key);

    public abstract MyJsonNode addKeyJsonArray(String key, ArrayList<MyJsonObject<?>> addValue);

    public abstract <TArray> MyJsonNode addKeyJsonArray(String key, Iterable<TArray> array, ForEach<TArray> each);

    public abstract MyJsonNode addKeyJsonString(String key, boolean addValue);

    public abstract MyJsonNode addKeyJsonString(String key, int addValue);

    public abstract MyJsonNode addKeyJsonString(String key, float addValue);

    public abstract MyJsonNode addKeyJsonString(String key, String addValue);

    public abstract MyJsonNode addKey(String key, MyJsonObject<?> innerObject);

    public abstract MyJsonArray addValue(MyJsonObject<?> newValue);

    public abstract boolean deleteKeyIf(MyJsonNode.IfKey ifKey, boolean recursive, boolean inString);

    public String getStringValue() {
        return getStringValue(false);
    }

    public interface ForEach<T> {
        MyJsonObject<?> forEach(T anObject);
    }
}
