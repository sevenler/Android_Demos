
package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TransparentActivity extends Activity {
	public static final String TAG = "TransparentActivity";
	public static onActivityListener onActivityListener;
	
	public static final String ACTION_SHOW_DIALOG_WITH_TRANSPARENT_ACTIVITY = "ACTION_SHOW_DIALOG_WITH_TRANSPARENT_ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, String.format(" this is %s ", TAG));
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(onActivityListener != null) onActivityListener.onCreate(this);
	}

	public interface onActivityListener {
		public void onCreate(Activity act);
	}
}
