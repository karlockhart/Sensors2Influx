package co.zer0.sensors2influx;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.net.MalformedURLException;
import java.net.URL;

import co.zer0.sensors2influx.persistence.CachedPersistenceProvider;
import co.zer0.sensors2influx.persistence.InfluxDBCachedPersistenceProvider;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorDataProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        try {
            URL url =  new URL("https", "data.karlsho.us", 8080, "");
            InfluxDBCachedPersistenceProvider persistenceProvider = new InfluxDBCachedPersistenceProvider(url, null, null, "weather");
            this.provider = new SensorDataProvider(sensorManager, persistenceProvider);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.provider.registerSensors();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
