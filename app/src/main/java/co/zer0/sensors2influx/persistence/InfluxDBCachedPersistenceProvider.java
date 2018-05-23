package co.zer0.sensors2influx.persistence;

import android.net.Uri;
import android.util.Log;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InfluxDBCachedPersistenceProvider implements CachedPersistenceProvider {

    private URL url;
    private String username;
    private String password;
    private String database;


    public InfluxDBCachedPersistenceProvider(URL url, String username, String password, String database) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public void Persist(String measurement, String sourceName, HashMap<String,Float> values, long timestamp) {
        Log.d("KARLTEST", getLineProtocol(measurement, sourceName, values, timestamp));
    }

    private String getLineProtocol(String measurement, String sourceName, HashMap<String,Float> values, long timestamp) {
        StringBuilder line = new StringBuilder();
        line.append(measurement + ",");
        line.append("source=" + sourceName);
        line.append(" ");

        Iterator it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            line.append(pair.getKey() + "=" + pair.getValue());
            if (it.hasNext()) {
                line.append(",");
            }
        }
        line.append(" ");
        line.append(new Long(timestamp).toString());

        return line.toString();
    }
}
