
package com.example.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.demo.TransparentActivity.onActivityListener;

/**
 * 这个广播接收器是为了测试在广播中是否能够显示toast和dialog toast是可以的，dialog是不可以的
 * 原因是toast的显示不依赖activity 而dialog的显示必须依赖activity
 * 
 * @author johnnyxyz
 * @mail johnnyxyzw@gmail.com
 */
public class AlertInBoardcastReceiver extends BroadcastReceiver {
	public static final String ACTION_SHOW_DIALOG_IN_BOARDCAST = "ACTION_SHOW_DIALOG_IN_BOARDCAST";
	public static final String ACTION_SHOW_TOAST_IN_BOARDCAST = "ACTION_SHOW_TOAST_IN_BOARDCAST";
	public static final String ACTION_SHOW_DIALOG_WHIT_TRANSPRARANT_ACTIVITY_IN_BOARDCAST = "ACTION_SHOW_DIALOG_WHIT_TRANSPRARANT_ACTIVITY_IN_BOARDCAST";

	public static final String TAG = "AlertInBoardcastReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {
		final String action = intent.getAction();
		Log.i(TAG, String.format("on Receive %s", action));
		if (action.equals(ACTION_SHOW_DIALOG_IN_BOARDCAST)) {
			try {
				confirmAction(context, "Dialog", "I Can show Dialog in boardcast with system alert", null, true);
			} catch (Exception ex) {
				ex.printStackTrace();
				String message = " Can't show dialog in boardcast.";
				Log.i(TAG, message);
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		} else if (action.equals(ACTION_SHOW_TOAST_IN_BOARDCAST)) {
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
							Toast.makeText(context, String.format("Finish %s", action),
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}).start();
		} else if (action.equals(ACTION_SHOW_DIALOG_WHIT_TRANSPRARANT_ACTIVITY_IN_BOARDCAST)) {
			confirmAction(context, "Dialog", "I Can show Dialog in boardcast with transparent activity.", new Runnable() {
				@Override
				public void run() {
					
				}
			}, false);
		}
	}

	/**
	 * 使用透明activity来弹出dialog
	 * @param context
	 * @param title
	 * @param message
	 * @param action
	 */
	private static void confirmWithTransparentActivity(Context context, final String title,
			final String message, final Runnable action) {
		TransparentActivity.onActivityListener = new onActivityListener() {
			@Override
			public void onCreate(final Activity act) {
				confirmAction(act, title, message, action, false, act);
			}
		};
		Intent dialog = new Intent(TransparentActivity.ACTION_SHOW_DIALOG_WITH_TRANSPARENT_ACTIVITY);
		dialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(dialog);
	}

	public static void confirmAction(Context context, String title, String message,
			final Runnable action, boolean isSystemAlert) {
		if (isSystemAlert)
			confirmAction(context, title, message, action, true, null);
		else
			confirmWithTransparentActivity(context, title, message, action);
	}

	/**
	 * 弹dialog 2种方式：一种使用系统dialog 另一种是使用透明activity来弹
	 * @param context
	 * @param title
	 * @param message
	 * @param action
	 * @param isSystemAlert
	 * @param act
	 */
	private static void confirmAction(Context context, String title, String message,
			final Runnable action, boolean isSystemAlert, final Activity act) {
		OnClickListener listener = new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						if (action != null)
							action.run();
				}
				if (act != null)
					act.finish();// 如果是透明activity的弹出方式，需要关闭activity
			}
		};
		AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(context,
				R.style.AppTheme)).setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
				.setMessage(message).setPositiveButton(android.R.string.ok, listener)
				.setNegativeButton(android.R.string.cancel, listener).create();
		// 配置为系统dialog.如果不这样做，在非activity环境中弹dialog会报WindowsBadtokenException
		// 前提是申请一条<uses-permission
		// android:name="android.permission.SYSTEM_ALERT_WINDOW"></uses-permission>权限，在权限提示框中会提示该应用会在其它应用上面显示内容
		if (isSystemAlert)
			dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}
}
