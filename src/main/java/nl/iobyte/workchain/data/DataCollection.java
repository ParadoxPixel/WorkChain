package nl.iobyte.workchain.data;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class DataCollection extends HashMap<String, Object> {

    public String getString(String key) {
        return (String) get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    public int getInt(String key) {
        return (int) get(key);
    }

    public double getDouble(String key) {
        return (double) get(key);
    }

    public float getFloat(String key) {
        return (float) get(key);
    }

    public <T> T getCustom(String key) {
        return (T) get(key);
    }

}
