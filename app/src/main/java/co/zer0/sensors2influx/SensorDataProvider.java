package co.zer0.sensors2influx;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import java.util.HashMap;

import co.zer0.sensors2influx.persistence.CachedPersistenceProvider;


public class SensorDataProvider implements SensorEventListener {

    SensorManager sensorManager;
    CachedPersistenceProvider persistenceProvider;

    Sensor ambientTemperature;
    Sensor relativeHumidity;
    Sensor pressure;

    Handler handleFlush = new Handler();

    HashMap<String, Float> values = new HashMap<>();

    private Runnable flusher = new Runnable() {
        @Override
        public void run() {
            Log.d("Flusher", "Running Flusher");
            persistenceProvider.Persist("ambient", "phone", values, System.currentTimeMillis());
            handleFlush.postDelayed(this, 5000);
        }
    };

    SensorDataProvider(SensorManager manager, CachedPersistenceProvider provider) {
        this.sensorManager = manager;
        this.persistenceProvider = provider;

        ambientTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        relativeHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

    }

    public void registerSensors() {
        if (ambientTemperature != null){
            sensorManager.registerListener(this, ambientTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (relativeHumidity != null) {
            sensorManager.registerListener(this, relativeHumidity, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (pressure != null) {
            sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        }

        this.handleFlush.post(flusher);
    }

    public void unregisterSensors() {
        sensorManager.unregisterListener(this);
        this.handleFlush.removeCallbacks(flusher);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        StringBuilder valueName = new StringBuilder();

        if (event.sensor == this.ambientTemperature) {
            valueName.append("tempteraute");
        }else if (event.sensor == this.relativeHumidity) {
            valueName.append("relative_humidity");
        }else if (event.sensor == this.pressure) {
            valueName.append("pressure");
        }else {
            valueName.append("unknown");
        }

        this.values.put(valueName.toString(), event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
