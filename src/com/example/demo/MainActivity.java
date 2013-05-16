package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		}
		if(intent != null) startActivity(intent);
	}

}
