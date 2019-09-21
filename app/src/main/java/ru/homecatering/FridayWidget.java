package ru.homecatering;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class FridayWidget extends AppWidgetProvider {
    private RemoteViews widgetView;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i("WIDGET", "updated");
        widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        widgetView.setTextViewText(R.id.widget_time, getTimeToFriday(context));
        initOpenActivityButton(context);
        for (int i : appWidgetIds) {
            appWidgetManager.updateAppWidget(i, widgetView);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.i("WIDGET", "enabled");
        widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        widgetView.setTextViewText(R.id.widget_time, getTimeToFriday(context));

        initOpenActivityButton(context);
    }

    private void initOpenActivityButton(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        widgetView.setOnClickPendingIntent(R.id.widget_button, pendingIntent);
    }

    private String getTimeToFriday(Context context) {
        Calendar dateToday = Calendar.getInstance();
        int dayToday = dateToday.get(Calendar.DAY_OF_WEEK);
        int hourToFriday = 24 - dateToday.get(Calendar.HOUR_OF_DAY);
        String time;
        if (dayToday == Calendar.FRIDAY) {
            time = context.getString(R.string.friday);
        } else {
            for (int i = 1; ; i++) {
                dateToday.add(Calendar.DAY_OF_WEEK, 1);
                if (dateToday.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    if (i == 1) {
                        time = String.format(context.getString(R.string.until_friday_hours), hourToFriday);
                    } else {
                        time = String.format(context.getString(R.string.until_friday), i - 1, hourToFriday);
                    }
                    break;
                }
            }
        }
        return time;
    }
}
