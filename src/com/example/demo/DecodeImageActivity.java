
package com.example.demo;

import java.io.IOException;
import java.net.URI;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class DecodeImageActivity extends Activity {

	private ImageView mImage;
	private TextView message;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decode_image);
		mImage = (ImageView)findViewById(R.id.image);
		message = (TextView)findViewById(R.id.message);
		
		Button httpClient = (Button)findViewById(R.id.httpClient);
		httpClient.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setBitmap(0);
			}
		});
		Button urlConnection = (Button)findViewById(R.id.urlConnection);
		urlConnection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setBitmap(1);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void setBitmap(final int http){
		final URI uri = URI.create(String.format(VolleyActivity.URL_FORMAT, VolleyActivity.images[0]));
		new Thread(new Runnable() {
			@Override
			public void run() {
				final long begin = System.currentTimeMillis();
				HttpClientDownloader downloader = new HttpClientDownloader(DecodeImageActivity.this);
				if(http == 1){
					downloader = new HttpConnectDownloader(DecodeImageActivity.this);
				}
				Log.i("Load", String.format("uri:%s", uri.toString()));
				final Bitmap bit = decodeNetworkUri(uri, new ImageSize(300,300), new BitmapFactory.Options(), downloader);
				final long end = System.currentTimeMillis();
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mImage.setImageBitmap(bit);
						message.setText(String.format("time duration: %s ms", end  - begin));
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
