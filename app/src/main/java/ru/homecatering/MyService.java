package ru.homecatering;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        final Intent intentF = intent;
        exec.submit(new Runnable() {
            @Override
            public void run() {
                Log.i("INFO", "started");
                PendingIntent pendingIntent = intentF.getParcelableExtra("home");
                Intent intentResult = new Intent();
                intentResult.putExtra("answer", "Service produced a result!");
                try {
                    pendingIntent.send(MyService.this, 0, intentResult);
                    Log.i("INFO", "sent");
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
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
