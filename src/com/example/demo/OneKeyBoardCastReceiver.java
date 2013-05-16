package com.example.demo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * 这个广播接收器是为了测试在广播中是否能够显示toast和dialog
 * toast是可以的，dialog是不可以的
 *  原因是toast的显示不依赖activity 而dialog的显示必须依赖activity
 * @author johnnyxyz
 * @mail johnnyxyzw@gmail.com
 *
 */
public class OneKeyBoardCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		final String action = intent.getAction();
		Log.i("OneKeyBoardCastReceiver", String.format("on Receive %s", action));
		if (action.equals(OneKeyWallpaper.ACTION_SHOW_DIALOG_IN_BOARDCAST)) {
			try{
				confirmAction(context, "Dialog", "Can show Dialog in boardcast.", null);
			}catch(Exception ex){
				ex.printStackTrace();
				String message = " Can't show dialog in boardcast.";
				Log.i("OneKeyBoardCastReceiver", message);
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		} else if (action.equals(OneKeyWallpaper.ACTION_SHOW_TOAST_IN_BOARDCAST)) {
			Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
			
			final Handler handler = new Handler();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000 * 5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(context, String.format("Finish %s", action), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}).start();
		}
	}
	
	public static void confirmAction(Context context, String title,
			String message, final Runnable action) {
		OnClickListener listener = new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (action != null)
						action.run();
				}
			}
		};
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, listener)
				.setNegativeButton(android.R.string.cancel, listener).create()
				.show();
	}
}

class OneKeyWallpaper {
	public static final String ACTION_SHOW_DIALOG_IN_BOARDCAST = "ACTION_SHOW_DIALOG_IN_BOARDCAST";
	public static final String ACTION_SHOW_TOAST_IN_BOARDCAST = "ACTION_SHOW_TOAST_IN_BOARDCAST";
}