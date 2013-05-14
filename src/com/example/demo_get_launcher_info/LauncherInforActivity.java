package com.example.demo_get_launcher_info;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class LauncherInforActivity extends Activity {
	TextView message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher_info);

		message = (TextView) findViewById(R.id.message);
		message.setText(getLauncherInfor());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private String getLauncherInfor() {
		final PackageManager pm = getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_HOME);
		//查找launcher类型的主Activity
		List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
		Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
		
		//读取默认launcher
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		final ResolveInfo defaultLauncher = pm.resolveActivity(i, 0);
		Log.v("my logs", "defaultLauncher package and activity name = "
				+ defaultLauncher.activityInfo.packageName + "    "
				+ defaultLauncher.activityInfo.name);
		
		StringBuilder value = new StringBuilder();
		for (ResolveInfo temp : appList) {
			Log.v("my logs", "package and activity name = "
					+ temp.activityInfo.packageName + "    "
					+ temp.activityInfo.name);
			
			value.append(String.format("{name:%s,isDefault:%s,isSystem:%s},", temp.activityInfo.name, 
					(defaultLauncher == null) ? false : temp.activityInfo.name.equals(defaultLauncher.activityInfo.name), checkIsSystemApp(temp, pm)));
		}
		return value.toString();
	}
	
	/**
	 * 判断应用是否为系统内置app
	 * @param app
	 * @param pm
	 * @return
	 */
	private boolean checkIsSystemApp(ResolveInfo app,PackageManager pm){
		if(app == null) return false;
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(app.activityInfo.packageName, PackageManager.GET_ACTIVITIES);
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

}
