
package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BoardcastActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boardcast);

		Button showToast = (Button)findViewById(R.id.show_toast);
		showToast.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OneKeyWallpaper.ACTION_SHOW_TOAST_IN_BOARDCAST);
				sendBroadcast(intent);
			}
		});
		
		Button showDialog = (Button)findViewById(R.id.show_dialog);
		showDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OneKeyWallpaper.ACTION_SHOW_DIALOG_IN_BOARDCAST);
				sendBroadcast(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
