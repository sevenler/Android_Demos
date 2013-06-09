
package com.example.demo;

import android.app.WallpaperManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayManager {
	private static DisplayManager instance = null;
	private int displayWidth = 0;
	private int displayHeight = 0;

	private int desiredWidth = 0;
	private int desiredHeight = 0;
	
	private int densityDpi = 0;
	private float density = 0;

	private DisplayMetrics dm = null;

	public static DisplayManager instance() {
		if (instance == null) {
			synchronized (DisplayManager.class) {
				if (instance == null) {
					instance = new DisplayManager();
				}
			}
		}
		return instance;
	}

	private DisplayManager() {
	}

	@Override
	public String toString() {
		return "displayWidth=" + displayWidth + ", displayHeight=" + displayHeight
				+ ", desiredWidth=" + desiredWidth + ", desiredHeight=" + desiredHeight
				+ ", densityDpi=" + densityDpi + ", density=" + density
				+ ", displayWidth=" + displayWidth / density + "dp, displayHeight=" + displayHeight  / density + "dp";
	}

	public DisplayManager initialize(Context context) {
		if (dm != null) return this;
		try {
			dm = new DisplayMetrics();
			WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(dm);
			displayWidth = dm.widthPixels;
			displayHeight = dm.heightPixels;
			densityDpi = dm.densityDpi;
			density = dm.density;
		} catch (Exception e) {
		}
		try {
			desiredWidth = WallpaperManager.getInstance(context).getDesiredMinimumWidth();
			desiredHeight = WallpaperManager.getInstance(context).getDesiredMinimumHeight();
		} catch (Exception e) {
		}
		return this;
	}

	private void checkInitialized() {
		if (dm == null) throw new IllegalArgumentException("displaymanager not being initialized");
	}

	public int getDisplayWidth() {
		checkInitialized();
		return displayWidth;
	}

	public int getDisplayHeight() {
		checkInitialized();
		return displayHeight;
	}

	public int getDesiredWidth() {
		checkInitialized();
		return desiredWidth;
	}

	public int getDesiredHeight() {
		checkInitialized();
		return desiredHeight;
	}

	public DisplayMetrics getDisplayMetrics() {
		checkInitialized();
		return dm;
	}

	public int getDensityDpi() {
		checkInitialized();
		return densityDpi;
	}
}
