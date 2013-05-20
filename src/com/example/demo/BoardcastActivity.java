
package com.example.demo;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
				Intent intent = new Intent(AlertInBoardcastReceiver.ACTION_SHOW_TOAST_IN_BOARDCAST);
				sendBroadcast(intent);
			}
		});
		
		Button showDialog = (Button)findViewById(R.id.show_dialog);
		showDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AlertInBoardcastReceiver.ACTION_SHOW_DIALOG_IN_BOARDCAST);
				sendBroadcast(intent);
			}
		});
		
		Button setWallpaper = (Button)findViewById(R.id.set_wallpaper);
		setWallpaper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int repeat = 1;
				String dir = "//sdcard//androidesk//onekeywallpapers";
				File file = new File(dir);
				String[] list = file.list();
				for (int i = 0; i < repeat; i++) {
					Intent intent = new Intent(SetWallpaperReveiver.ACTION_SET_WALLPAPER);
					intent.putExtra(SetWallpaperReveiver.DATA_WALLPAPER,  dir + "/" +list[i]);
					sendBroadcast(intent);
				}
			}
		});

	}
}
