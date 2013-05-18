
package com.example.demo;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MediaActivity extends Activity {
	public static final String TAG = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);

		final MediaListGetter getter = new MediaListGetter(getContentResolver());

		Button internalImages = (Button)findViewById(R.id.internal_images);
		internalImages.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> hash = getter.getCameraImages();
				Log.i(TAG, String.format("get CameraImages size: %s ", hash.size()));
				Iterator<String> it = hash.keySet().iterator();
				while(it.hasNext()){
					Log.i(TAG, String.format(" %s ", hash.get(it.next())));
				}
			}
		});

		Button internalVidios = (Button)findViewById(R.id.internal_vidios);
		internalVidios.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> hash = getter.getCameraVidios();
				Log.i(TAG, String.format("get getCameraVidios size: %s ", hash.size()));
				
				Iterator<String> it = hash.keySet().iterator();
				while(it.hasNext()){
					Log.i(TAG, String.format(" %s ", hash.get(it.next())));
				}
			}
		});

		Button internalMedias = (Button)findViewById(R.id.internal_medias);
		internalMedias.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		Button allImages = (Button)findViewById(R.id.all_images);
		allImages.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> hash = getter.getAllImages();
				Log.i(TAG, String.format("get getAllImages size: %s ", hash.size()));
			}
		});

		Button allVidios = (Button)findViewById(R.id.all_vidios);
		allVidios.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> hash = getter.getAllVidios();
				Log.i(TAG, String.format("get getAllVidios size: %s ", hash.size()));
			}
		});

		Button externalFolder = (Button)findViewById(R.id.external_folder);
		externalFolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> hash = getter.getExternalFolders();
				Log.i(TAG, String.format("get getExternalFolders size: %s ", hash.size()));
				Iterator<String> it = hash.keySet().iterator();
				while(it.hasNext()){
					Log.i(TAG, String.format(" %s ", hash.get(it.next())));
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
