package com.example.demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	
	private static SingleThreadWorker worker = new SingleThreadWorker();
	
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
		worker.setNewWork(new Runnable() {
			@Override
			public void run() {
				try {
					setWallpaper(context, wallpaper);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				final int sleep = 1000 * 20;
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void setWallpaper(Context ctx, String file) throws IOException{
		byte[] bytes = getBytesFromFile(file);
		InputStream bis = new ByteArrayInputStream(bytes);
		final WallpaperManager wm = WallpaperManager.getInstance(ctx);
		try{
			wm.setStream(bis);
		}catch(IOException ex){
			throw ex;
		}finally{
			bis.close();
		}
	}
	
	private static byte[] getBytesFromFile(String path) throws IOException {
		File file = new File(path);
		int size = (int)file.length();
		byte[] bytes = new byte[size];
		BufferedInputStream buf = null;
		try {
			buf = new BufferedInputStream(new FileInputStream(file));
			buf.read(bytes, 0, bytes.length);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}finally{
			if(buf != null) buf.close();
		}
		return bytes;
	}
}