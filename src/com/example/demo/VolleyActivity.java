
package com.example.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class VolleyActivity extends Activity {

	ImageLoader mImageLoader;
	private final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(20);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final long begin = System.currentTimeMillis();
		RequestQueue queue = Volley.newRequestQueue(VolleyActivity.this);
		ImageCache imageCache = new ImageCache() {
			@Override
			public void putBitmap(String key, Bitmap value) {
				mImageCache.put(key, value);
				long end = System.currentTimeMillis();
				System.out.println(String.format("time duration of Volley %s", (end - begin)));
			}

			@Override
			public Bitmap getBitmap(String key) {
				return mImageCache.get(key);
			}
		};
		mImageLoader = new ImageLoader(queue, imageCache);
		NetworkImageView item = (NetworkImageView)getLayoutInflater().inflate(R.layout.item_volley, null,false);
		item.setImageUrl(String.format(URL_FORMAT, images[0]), mImageLoader);
		setContentView(item);
		
		final long beg = System.currentTimeMillis();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(VolleyActivity.this)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.enableLogging() // Not necessary in common
			.build();
		
		com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
		imageLoader.init(config);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher)
			.cacheInMemory()
			.cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
		imageLoader.loadImage(String.format(URL_FORMAT, images[0]), options, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				long end = System.currentTimeMillis();
				System.out.println(String.format("time duration of universalimageloader %s", (end - beg)));
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});

	}

	public static final String URL_FORMAT = "http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f1000_1000&quality=60&sec=1355561804&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=%s";
	public static final String[] images = { "http://t2.baidu.com/it/u=306470869,1694571770&fm=17",
			"http://i3.baidu.com/it/u=3742918946,1731708307&fm=17",
			"http://i2.baidu.com/it/u=3792093916,1109408528&fm=17",
			"http://i3.baidu.com/it/u=2437358330,2525142469&fm=17",
			"http://i1.baidu.com/it/u=3280233078,1858685780&fm=17",
			"http://i2.baidu.com/it/u=2958158662,3585248609&fm=17",
			"http://i3.baidu.com/it/u=1386469160,4092193982&fm=17",
			"http://i1.baidu.com/it/u=2485889229,3871036636&fm=17",
			"http://i1.baidu.com/it/u=3831257100,3736290743&fm=17",
			"http://i2.baidu.com/it/u=226114291,552105118&fm=17",
			
			"http://i1.baidu.com/it/u=122350239,2038214310&fm=17",
			"http://i2.baidu.com/it/u=2772356371,4193573070&fm=17",
			"http://i1.baidu.com/it/u=303469386,3215218496&fm=17",
			"http://i1.baidu.com/it/u=3773650182,4220220977&fm=17",
			"http://i2.baidu.com/it/u=415161433,3025962525&fm=17",
			
			"http://i1.baidu.com/it/u=122350239,2038214310&fm=17",
			"http://i1.baidu.com/it/u=3773650182,4220220977&fm=17",
			"http://i3.baidu.com/it/u=3302558666,3621977906&fm=17",
			"http://i3.baidu.com/it/u=1631334071,292420538&fm=17",
			"http://i1.baidu.com/it/u=2182688544,2249461559&fm=17" };
}
