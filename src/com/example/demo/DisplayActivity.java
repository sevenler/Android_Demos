
package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		
		TextView text = (TextView)findViewById(R.id.display);
		DisplayManager dm = DisplayManager.instance().initialize(this);
		text.setText(dm.toString());
	}
}
