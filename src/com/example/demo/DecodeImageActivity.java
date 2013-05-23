
package com.example.demo;

import java.io.IOException;
import java.net.URI;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class DecodeImageActivity extends Activity {

	private ImageView mImage;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decode_image);
		mImage = (ImageView)findViewById(R.id.image);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		final URI uri = URI.create(String.format(VolleyActivity.URL_FORMAT, VolleyActivity.images[0]));
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Bitmap bit = decodeNetworkUri(uri, new ImageSize(300,300), new BitmapFactory.Options(), new HttpClientDownloader(DecodeImageActivity.this));
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mImage.setImageBitmap(bit);
					}
				});
			}
		}).start();
	}
	
	public Bitmap decodeNetworkUri(URI uri, ImageSize imageSize, BitmapFactory.Options options, HttpClientDownloader imageDownloader) {
		if(options.mCancel) return null;
		Bitmap result = null;
		ImageDecoder decoder = new ImageDecoder(uri, imageDownloader);
		try {
			result = decoder.decode(imageSize, options, ImageScaleType.IN_SAMPLE_POWER_OF_2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
