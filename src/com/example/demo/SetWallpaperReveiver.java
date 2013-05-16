package com.example.demo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SetWallpaperReveiver extends BroadcastReceiver{
	public static final String TAG = "SetWallpaperReveiver";
	
	public static final String ACTION_SET_WALLPAPER = "ACTION_TO_SET_WALLPAPER";
	
	public static final String ACTION_RANDOM_SET_WALLPAPER = "ACTION_RANDOM_SET_WALLPAPER";
	
	public static final String DATA_WALLPAPER = "DATA_WALLPAPER";
	
	private SingleThreadWorker worker = new SingleThreadWorker(new GetterHandler());
	private SingleWorkerCallback callback = new SingleWorkerCallback() {
		@Override
		public void load(Thread thread) {
		}
		
		@Override
		public void completed() {
		}
		
		@Override
		public void cancel(Thread thread) {
		}
	};
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		final String action = intent.getAction();
	   String wallpaper = intent.getExtras().getString(DATA_WALLPAPER);
		
		Log.i(TAG, String.format("on Receive action:%s wallpaper:%s", action, wallpaper));
		if (ACTION_SET_WALLPAPER.equals(action)) {
			setWallpaperWithNewThead(context, wallpaper);
		}else if(ACTION_RANDOM_SET_WALLPAPER.equals(action)){
			Random random = new Random();
			File file = new File(wallpaper);
			String[] list = file.list();
			int next = random.nextInt(list.length);
			
			setWallpaperWithNewThead(context, wallpaper + "/" + list[next]);
		}
	}
	
	private void setWallpaperWithNewThead(final Context context, final String wallpaper){
		worker.setNewWork(callback, new Runnable() {
			@Override
			public void run() {
				try {
					Log.i(TAG, String.format(" To set wallpaper %s ", wallpaper));
					setWallpaper(context, wallpaper);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setWallpaper(Context ctx, String file) throws IOException{
		InputStream is = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(is);
		final WallpaperManager wm = WallpaperManager.getInstance(ctx);
		try{
			wm.setStream(bis);
		}catch(IOException ex){
			throw ex;
		}finally{
			bis.close();
			is.close();
		}
	}
}