
package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import com.android.volley.Request.Method;

public class HttpConnectDownloader extends HttpClientDownloader {
	public static final String TAG = "HttpUrlConnect";

	public HttpConnectDownloader(Context ctx) {
		super(ctx);
	}

	@Override
	public byte[] getByteArrayFromNetwork(URI imageUri, Options options) throws IOException {
		if(options.mCancel) return null;
		Log.i(TAG, String.format("time:%s", System.currentTimeMillis()));
		HttpResponse response = performRequest(imageUri.toURL().toString());
		Log.i(TAG, String.format("time:%s", System.currentTimeMillis()));
		HttpEntity entity = response.getEntity();
		Log.i(TAG, String.format("time:%s", System.currentTimeMillis()));
		byte[] bytes = Stream.toByteArray(entity, options);
		return bytes;
	}
	
	protected HttpURLConnection createConnection(URL url) throws IOException {
		return (HttpURLConnection)url.openConnection();
	}

	private HttpURLConnection openConnection(URL url) throws IOException {
		HttpURLConnection connection = createConnection(url);

		int timeoutMs = Http.CONNECT_TIMEOUT;
		connection.setConnectTimeout(timeoutMs);
		connection.setReadTimeout(timeoutMs);
		connection.setUseCaches(false);
		connection.setDoInput(true);

		return connection;
	}

	public HttpResponse performRequest(String url) throws IOException {
		URL parsedUrl = new URL(url);
		HttpURLConnection connection = openConnection(parsedUrl);
		setConnectionParametersForRequest(connection, Method.GET);
		ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
		int responseCode = connection.getResponseCode();
		if (responseCode == -1) {
			throw new IOException("Could not retrieve response code from HttpUrlConnection.");
		}
		StatusLine responseStatus = new BasicStatusLine(protocolVersion,
				connection.getResponseCode(), connection.getResponseMessage());
		BasicHttpResponse response = new BasicHttpResponse(responseStatus);
		response.setEntity(entityFromConnection(connection));
		for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
			if (header.getKey() != null) {
				Header h = new BasicHeader(header.getKey(), header.getValue().get(0));
				response.addHeader(h);
			}
		}
		return response;
	}
	
	private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException ioe) {
            inputStream = connection.getErrorStream();
        }
        
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

	static void setConnectionParametersForRequest(HttpURLConnection connection, int method)
			throws IOException {
		switch (method) {
			case Method.DEPRECATED_GET_OR_POST:
				break;
			case Method.GET:
				connection.setRequestMethod("GET");
				break;
			case Method.DELETE:
				connection.setRequestMethod("DELETE");
				break;
			case Method.POST:
				connection.setRequestMethod("POST");
				break;
			case Method.PUT:
				connection.setRequestMethod("PUT");
				break;
			default:
				throw new IllegalStateException("Unknown method type.");
		}
	}
}
