package ru.homecatering;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

public class HomeFragment extends Fragment {

    private TextView textConsole;
    private TextView textTemperature;
    private TextView textHumidity;
    private SensorManager sensorManager;
    private List<Sensor> sensors;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;
    private Float temperature;

    SensorEventListener listenerTemperature = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showTemperatureSensors(event);
        }
    };

    SensorEventListener listenerHumidity = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showHumiditySensors(event);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        textConsole = view.findViewById(R.id.textConsole);
        textTemperature = view.findViewById(R.id.textTemperature);
        textHumidity = view.findViewById(R.id.textHumidity);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorManager.registerListener(listenerTemperature, sensorTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensorManager.registerListener(listenerHumidity, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL);
        showSensors();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerTemperature, sensorTemperature);
        sensorManager.unregisterListener(listenerHumidity, sensorHumidity);
    }


    private void showSensors() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Sensor sensor : sensors) {
            stringBuilder.append("name = ").append(sensor.getName())
                    .append(", type = ").append(sensor.getType())
                    .append("\n")
                    .append("vendor = ").append(sensor.getVendor())
                    .append(" ,version = ").append(sensor.getVersion())
                    .append("\n")
                    .append("max = ").append(sensor.getMaximumRange())
                    .append(", resolution = ").append(sensor.getResolution())
                    .append("\n").append("--------------------------------------").append("\n");
        }
        textConsole.setText(stringBuilder);
    }

    private void showTemperatureSensors(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(getActivity().getResources().getString(R.string.temperature_note), event.values[0]))
                .append("\n").append("=======================================").append("\n");
        temperature = event.values[0];
        textTemperature.setText(stringBuilder);
    }

    private void showHumiditySensors(SensorEvent event) {
        float relHum = event.values[0];
        String humidity;
        StringBuilder stringBuilder = new StringBuilder();
        if (temperature != null) {
            double absHum = 216.7f * (relHum / 100 * 6.112 * Math.exp((17.62f * temperature) / (243.12f + temperature))) / (273.15 + temperature);
            humidity = Float.toString(Math.round(absHum * 10f) / 10f);
            stringBuilder.append(String.format(getActivity().getResources().getString(R.string.humidity_abs_note), humidity));
            Log.i("INFO", "1");
        } else {
            humidity = Float.toString(relHum);
            stringBuilder.append(String.format(getActivity().getResources().getString(R.string.humidity_note), humidity));
            Log.i("INFO", "2");
        }
        stringBuilder.append("\n").append("=======================================").append("\n");
        textHumidity.setText(stringBuilder);
    }


}
