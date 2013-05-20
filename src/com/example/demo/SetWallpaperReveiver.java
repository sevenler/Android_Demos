
package com.example.demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

public class SetWallpaperReveiver extends BroadcastReceiver {
	public static final String TAG = "SetWallpaperReveiver";

	public static final String ACTION_SET_WALLPAPER = "ACTION_TO_SET_WALLPAPER";
	public static final String ACTION_RANDOM_SET_WALLPAPER = "ACTION_RANDOM_SET_WALLPAPER";
	public static final String DATA_WALLPAPER = "DATA_WALLPAPER";

	public static final String SAVED_NEXT_WALLPAPER = "SAVED_NEXT_WALLPAPER";

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
			setWallpaperWithNewThead(context, wallpaper, null);
		} else if (ACTION_RANDOM_SET_WALLPAPER.equals(action)) {
			String[] dirs = new String[1];
			dirs[0] = wallpaper;

			// 实践证明Media数据库操作时间是FileFilter操作的时间的一半
			// long time = System.currentTimeMillis();
			// Util.randomWallpaperFilterMediaStore(context, dirs);
			// long time1 = System.currentTimeMillis();
			// Log.i(TAG, String.format(" filter Media Store duration:%s ",
			// time1 - time));
			// time = System.currentTimeMillis();
			// Util.randomWallpaperFilterFile(dirs);
			// time1 = System.currentTimeMillis();
			// Log.i(TAG, String.format(" filter File duration:%s ", time1 -
			// time));

			setWallpaperWithNewThead(context, generateOrLoadWallpaper(context, dirs), dirs);
		}
	}

	private void setWallpaperWithNewThead(final Context context, final String wallpaper,
			final String[] dir) {
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

				if (dir != null) generateNextWallpaperAndSave(context, dir);
			}
		});
	}

	private String generateOrLoadWallpaper(Context ctx, String[] dirs) {
		PrefManager pref = PrefManager.getInstance();
		String saved = pref.getStringFromPrefs(ctx, SAVED_NEXT_WALLPAPER, null);
		if (saved == null) saved = Util.randomWallpaperFilterMediaStore(ctx, dirs);
		return saved;
	}

	private void generateNextWallpaperAndSave(Context ctx, String[] dirs) {
		String next = Util.randomWallpaperFilterMediaStore(ctx, dirs);
		PrefManager pref = PrefManager.getInstance();
		pref.setStringToPrefs(ctx, SAVED_NEXT_WALLPAPER, next);
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

class Util {
	private static String[] getImagesByDir(Context context, String dir) {
		try {
			String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
					MediaStore.Images.Media.ORIENTATION };
			String where = MediaStore.Images.Media.BUCKET_ID
					+ String.format("= '%s'", dir.toLowerCase(Locale.getDefault()).hashCode());
			Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, where,
					MediaStore.Images.Media.DATE_ADDED + " desc");
			String[] results = new String[cursor.getCount()];
			for (int i = 0; cursor.moveToNext(); i++) {
				results[i] = cursor.getString(1);
			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String randomWallpaperFilterFile(String dir) {
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

	public static String randomWallpaperFilterFile(String[] dirs) {
		Random random = new Random();
		int size = dirs.length;
		if (size == 0) return null;
		int index = random.nextInt(size);
		return randomWallpaperFilterFile(dirs[index]);
	}

	public static String randomWallpaperFilterMediaStore(Context ctx, String dir) {
		Random random = new Random();
		String[] files = getImagesByDir(ctx, dir);
		int next = random.nextInt(files.length);
		String value = files[next];
		return value;
	}

	public static String randomWallpaperFilterMediaStore(Context ctx, String[] dirs) {
		Random random = new Random();
		int size = dirs.length;
		if (size == 0) return null;
		int index = random.nextInt(size);
		return randomWallpaperFilterMediaStore(ctx, dirs[index]);
	}
}
