package ru.homecatering;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    private PendingIntent pendingIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        pendingIntent = intent.getParcelableExtra("home");
        exec.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    Log.e("ERROR", "Exception in HomeFragment", e);
                }
                Log.i("INFO", "started");
                Intent intentResult = new Intent();
                intentResult.putExtra("answer", "Service produced a result!");
                try {
                    pendingIntent.send(MyService.this, 0, intentResult);
                    Log.i("INFO", "sent");
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", "stopped");
                stopSelf();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
