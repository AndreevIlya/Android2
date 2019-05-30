package ru.homecatering;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class ServiceBound extends Service {
    IBinder binder = new CustomBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class CustomBinder extends Binder {
        ServiceBound getBoundService() {
            return ServiceBound.this;
        }
    }

    public void doWork() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    Log.e("ERROR", "Service", e);
                }
            }
        }).start();
    }
}
