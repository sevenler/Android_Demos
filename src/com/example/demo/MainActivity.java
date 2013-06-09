package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button directToGetLauncherInfo = (Button)findViewById(R.id.direct_to_get_launcher_info);
		directToGetLauncherInfo.setOnClickListener(this);
		Button boardcast = (Button)findViewById(R.id.boardcast);
		boardcast.setOnClickListener(this);
		Button media = (Button)findViewById(R.id.media);
		media.setOnClickListener(this);
		Button volley = (Button)findViewById(R.id.volley);
		volley.setOnClickListener(this);
		Button decode = (Button)findViewById(R.id.decode);
		decode.setOnClickListener(this);
		Button display = (Button)findViewById(R.id.display);
		display.setOnClickListener(this);
		Button alloyPhoto = (Button)findViewById(R.id.alloy_photo);
		alloyPhoto.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.direct_to_get_launcher_info:
			intent = new Intent(this, LauncherInforActivity.class);
			break;
		case R.id.boardcast:
			intent = new Intent(this, BoardcastActivity.class);
			break;
		case R.id.media:
			intent = new Intent(this, MediaActivity.class);
			break;
		case R.id.volley:
			intent = new Intent(this, VolleyActivity.class);
			break;
		case R.id.decode:
			intent = new Intent(this, DecodeImageActivity.class);
			break;
		case R.id.display:
			intent = new Intent(this, DisplayActivity.class);
			break;
		case R.id.alloy_photo:
			intent = new Intent(this, AlloyPhotoActivity.class);
			break;
		}
		if(intent != null) startActivity(intent);
	}

}
