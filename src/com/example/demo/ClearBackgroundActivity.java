package com.example.demo;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ClearBackgroundActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		TextView text = (TextView) findViewById(R.id.display);
		text.setText("Kill background process");
		text.setBackgroundColor(Color.parseColor("#e2e2e2"));
		text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				killAll(ClearBackgroundActivity.this);
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public static void killAll(Context context) {
		// 获取一个ActivityManager 对象
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		// 获取系统中所有正在运行的进程
		List<RunningAppProcessInfo> appProcessInfos = activityManager
				.getRunningAppProcesses();
		
		// 获取当前activity所在的进程
		String currentProcess = context.getApplicationInfo().processName;

		// 对系统中所有正在运行的进程进行迭代，如果进程名不是当前进程，则Kill掉
		for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {
			System.out.println(String.format("appProcessInfo:%s %s %s", appProcessInfo.pid, appProcessInfo.processName, appProcessInfo.lru));
			String processName = appProcessInfo.processName;
			//if (!processName.equals(currentProcess)) {
				activityManager.killBackgroundProcesses(processName);
				//}

		}

	}

}
