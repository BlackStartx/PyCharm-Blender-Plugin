package util.core.json_util;

import util.core.json_util.exceptions.MyJsonInvalidEscapeChar;
import util.core.json_util.exceptions.MyJsonInvalidFormat;
import util.core.json_util.values.MyJsonArray;
import util.core.json_util.values.MyJsonNode;
import util.core.json_util.values.MyJsonObject;
import util.core.json_util.values.MyJsonString;

import java.util.ArrayList;

public class MyJsonParser {
    private final ArrayList<String> keyPath = new ArrayList<>();
    private final ArrayList<MyJsonObject<?>> objectPath = new ArrayList<>();

    private MyJsonObject<?> innerObject;
    private MyJsonObject<?> outerObject;

    private StringBuilder builder = new StringBuilder();

    private boolean inText;
    private boolean waitingKey;
    private boolean waitingEscape;

    private MyJsonObject<?> source;

    public MyJsonDocument parse(String json) {
        if (json == null)
            return null;

        for (char character : json.toCharArray()) {
            switch (character) {
                case '{':
                    if (waitingEscape) throw new MyJsonInvalidEscapeChar();

                    if (inText) {
                        builder.append(character);
                    } else {
                        enterNode();
                        waitingKey = true;
                    }

                    break;
                case '}':
                    if (waitingEscape) throw new MyJsonInvalidEscapeChar();

                    if (inText) builder.append(character);
                    else evaluateStringBuilder(this::finishedNode);

                    break;
                case '[':
                    if (waitingEscape) throw new MyJsonInvalidEscapeChar();

                    if (inText) {
                        builder.append(character);
                    } else {
                        enterArray();
                        waitingKey = false;
                    }

                    break;
                case ']':
                    if (waitingEscape) throw new MyJsonInvalidEscapeChar();

                    if (inText) builder.append(character);
                    else evaluateStringBuilder(this::finishedArray);

                    break;
                case ',':
                    if (waitingEscape) throw new MyJsonInvalidEscapeChar();

                    if (inText) builder.append(character);
                    else evaluateStringBuilder(() -> {
                    });

                    break;
                case ':':
                    if (waitingEscape) throw new MyJsonInvalidEscapeChar();

                    if (inText) builder.append(character);
                    else waitingKey = false;

                    break;
                case '\\':
                    if (inText) {
                        if (waitingEscape) builder.append('\\');

                        waitingEscape = !waitingEscape;
                    }

                    break;
                case '"':
                    if (inText) {
                        if (waitingEscape) builder.append('"');
                        else {
                            inText = false;
                            if (waitingKey) enterKey(builder.toString());
                            else onStringValue(builder.toString(), true);
                            builder = new StringBuilder();
                        }
                    } else {
                        if (waitingEscape) throw new MyJsonInvalidEscapeChar();

                        builder = new StringBuilder();
                        inText = true;
                    }

                    waitingEscape = false;
                    break;
                default:
                    if (waitingEscape) {
                        builder.append('\\');
                        waitingEscape = false;
                    }

                    builder.append(character);
                    break;
            }
        }

        if (keyPath.size() != 0 || objectPath.size() != 0) throw new MyJsonInvalidFormat();
        return new MyJsonDocument(source);
    }

    private void evaluateStringBuilder(Runnable action) {
        String oldBuilder = builder.toString().trim();
        if (!oldBuilder.equals("")) onStringValue(oldBuilder, false);
        builder = new StringBuilder();
        action.run();
        waitingKey = innerObject instanceof MyJsonNode;
    }

    /**
     * Called whenever we enter in an array '['.
     */
    private void enterArray() {
        MyJsonArray newNode = new MyJsonArray();
        if (source == null) source = newNode;
        addToObjectArray(newNode);
    }

    /**
     * Called whenever we enter in a JSONNode '{'.
     */
    private void enterNode() {
        MyJsonNode newNode = new MyJsonNode();
        if (source == null) source = newNode;
        addToObjectArray(newNode);
    }

    /**
     * Called whenever we exit from an array ']'.
     */
    private void finishedArray() {
        if (outerObject instanceof MyJsonArray) outerObject.addValue(innerObject);
        else if (outerObject instanceof MyJsonNode) outerObject.addKey(getKey(), innerObject);
        removeObjectArray();
    }

    /**
     * Called whenever we exits from a JSONNode '}'.
     */
    private void finishedNode() {
        if (outerObject instanceof MyJsonArray) outerObject.addValue(innerObject);
        else if (outerObject instanceof MyJsonNode) outerObject.addKey(getKey(), innerObject);
        removeObjectArray();
    }

    /**
     * Called whenever we found a key.
     */
    private void enterKey(String key) {
        addKeyToPath(key);
    }

    /**
     * Called whenever the last key has a JSONString as value.
     *
     * @param value the value of the key.
     */
    private void onStringValue(String value, boolean isString) {
        if (isString) {
            if (innerObject instanceof MyJsonArray) innerObject.addValue(new MyJsonString(value));
            else innerObject.addKeyJsonString(getKey(), value);
            return;
        }
        if (value.equals(MyJsonHelper.Null)) {
            if (innerObject instanceof MyJsonArray) innerObject.addValue(new MyJsonString());
            else innerObject.addKeyJsonString(getKey(), null);
            return;
        }
        if (value.equals(MyJsonHelper.False) || value.equals(MyJsonHelper.True)) {
            if (innerObject instanceof MyJsonArray) innerObject.addValue(new MyJsonString(value.equals(MyJsonHelper.True)));
            else innerObject.addKeyJsonString(getKey(), value.equals(MyJsonHelper.True));
            return;
        }

        try {
            int intValue = Integer.parseInt(value);
            if (innerObject instanceof MyJsonArray) innerObject.addValue(new MyJsonString(intValue));
            else innerObject.addKeyJsonString(getKey(), intValue);
        } catch (NumberFormatException eInt) {
            try {
                float floatValue = Float.parseFloat(value);
                if (innerObject instanceof MyJsonArray) innerObject.addValue(new MyJsonString(floatValue));
                else innerObject.addKeyJsonString(getKey(), floatValue);
            } catch (NumberFormatException eFloat) {
                throw new MyJsonInvalidFormat();
            }
        }
    }

    private void addToObjectArray(MyJsonObject<?> value) {
        outerObject = innerObject;
        objectPath.add(value);
        innerObject = value;
    }

    private void removeObjectArray() {
        objectPath.remove(objectPath.size() - 1);
        if (objectPath.size() != 0) {
            innerObject = outerObject;
            outerObject = objectPath.size() == 1 ? null : objectPath.get(objectPath.size() - 2);
        }
    }

    /*
     *  KeyPath...
     */

    private void addKeyToPath(String key) {
        keyPath.add(key);
    }

    private String getKey() {
        String key = keyPath.get(keyPath.size() - 1);
        keyPath.remove(keyPath.size() - 1);
        return key;
    }
}
