
package com.example.demo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		Log.i("OneKeyBoardCastReceiver", String.format("MyWidgetProvider on onUpdate "));
		
		Intent toast = new Intent(OneKeyWallpaper.ACTION_SHOW_TOAST_IN_BOARDCAST);
		PendingIntent toastPenging = PendingIntent.getBroadcast(context, 0, toast, 0);
		remoteViews.setOnClickPendingIntent(R.id.show_toast, toastPenging);
		
		Intent dialog = new Intent(OneKeyWallpaper.ACTION_SHOW_DIALOG_IN_BOARDCAST);
		PendingIntent dialogPending = PendingIntent.getBroadcast(context, 0, dialog, 0);
		remoteViews.setOnClickPendingIntent(R.id.show_dialog, dialogPending);
		
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	}
}
