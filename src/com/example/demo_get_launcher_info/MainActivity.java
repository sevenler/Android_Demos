package com.example.demo_get_launcher_info;

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
		
		Button direct_to_get_launcher_info = (Button)findViewById(R.id.direct_to_get_launcher_info);
		direct_to_get_launcher_info.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.direct_to_get_launcher_info:
			Intent intent = new Intent(this, LauncherInforActivity.class);
			startActivity(intent);
			break;
		}
	}

}
