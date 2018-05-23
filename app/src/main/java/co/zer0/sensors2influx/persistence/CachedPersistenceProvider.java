package co.zer0.sensors2influx.persistence;

import java.util.HashMap;

public interface CachedPersistenceProvider {
    public void Persist(String measurement, String sourceName, HashMap<String,Float> values, long timestamp);
}
