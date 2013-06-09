
package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AlloyPhotoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alloy_photo);

		WebView webview = (WebView)findViewById(R.id.webView);
		webview.getSettings().setJavaScriptEnabled(true);
		
		String alloyPhotoUrl = "http://alloyteam.github.io/AlloyPhoto/alloyphoto.html";
		webview.loadUrl(alloyPhotoUrl);
	}
}
