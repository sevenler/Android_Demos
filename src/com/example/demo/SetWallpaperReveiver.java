
package com.example.demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SetWallpaperReveiver extends BroadcastReceiver {
	public static final String TAG = "SetWallpaperReveiver";

	public static final String ACTION_SET_WALLPAPER = "ACTION_TO_SET_WALLPAPER";

	public static final String ACTION_RANDOM_SET_WALLPAPER = "ACTION_RANDOM_SET_WALLPAPER";

	public static final String DATA_WALLPAPER = "DATA_WALLPAPER";

	/**
	 * 单线程工作，溢出时，删除老的线程
	 */
	private static ThreadPoolExecutor worker = new ThreadPoolExecutor(1, 2, 10,
			TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setPriority(Thread.NORM_PRIORITY - 1);
					return t;
				}
			}, new ThreadPoolExecutor.DiscardOldestPolicy());

	@Override
	public void onReceive(final Context context, Intent intent) {
		final String action = intent.getAction();
		String wallpaper = intent.getExtras().getString(DATA_WALLPAPER);

		Log.i(TAG, String.format("on Receive action:%s wallpaper:%s", action, wallpaper));
		if (ACTION_SET_WALLPAPER.equals(action)) {
			setWallpaperWithNewThead(context, wallpaper);
		} else if (ACTION_RANDOM_SET_WALLPAPER.equals(action)) {
			setWallpaperWithNewThead(context, randomWallpaper(wallpaper));
		}
	}

	private String randomWallpaper(String dir) {
		Random random = new Random();
		File file = new File(dir);
		if (!file.exists()) return null;

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".jpg");
			}
		};
		String[] list = file.list(filter);

		if (list.length == 0) return null;
		int next = random.nextInt(list.length);
		String value = dir + "/" + list[next];
		return value;
	}

	private void setWallpaperWithNewThead(final Context context, final String wallpaper) {
		if (wallpaper == null) {
			Log.i(TAG, String.format(" seting wallpaper is null ", wallpaper));
			return;
		}

		worker.submit(new Runnable() {
			@Override
			public void run() {
				try {
					setWallpaper(context, wallpaper);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void setWallpaper(Context ctx, String file) throws IOException {
		byte[] bytes = getBytesFromFile(file);

		InputStream bis = new ByteArrayInputStream(bytes);
		final WallpaperManager wm = WallpaperManager.getInstance(ctx);
		try {
			wm.setStream(bis);
		} catch (IOException ex) {
			throw ex;
		} finally {
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
		} finally {
			if (buf != null) buf.close();
		}
		return bytes;
	}
}
