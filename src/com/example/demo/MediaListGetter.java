
package com.example.demo;

import java.util.HashMap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;

/**
 * 获取系统Midia数据库数据，Midia数据库要这么用
 * 
 * @author johnnyxyz
 * @mail johnnyxyzw@gmail.com
 */
public class MediaListGetter {
	public static final Uri EXTERNAL_STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
	public static final Uri INTERNAL_STORAGE_URI = Images.Media.INTERNAL_CONTENT_URI;
	public static final Uri VIDEO_STORAGE_URI = Uri.parse("content://media/external/video/media");

	private static final String[] ACCEPTABLE_IMAGE_TYPES = new String[] { "image/jpeg",
			"image/png", "image/gif" };

	private static final String WHERE_CLAUSE = "(" + Media.MIME_TYPE + " in (?, ?, ?))";
	private static final String WHERE_CLAUSE_WITH_BUCKET_ID = WHERE_CLAUSE + " AND "
			+ Media.BUCKET_ID + " = ?";

	protected String whereClause(String bucketId) {
		return bucketId == null ? WHERE_CLAUSE : WHERE_CLAUSE_WITH_BUCKET_ID;
	}

	private String[] whereClauseArgs(String bucketId) {
		if (bucketId != null) {
			int count = ACCEPTABLE_IMAGE_TYPES.length;
			String[] result = new String[count + 1];
			System.arraycopy(ACCEPTABLE_IMAGE_TYPES, 0, result, 0, count);
			result[count] = bucketId;
			return result;
		}
		return ACCEPTABLE_IMAGE_TYPES;
	}

	public static final String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory()
			.toString() + "/DCIM/Camera";
	public static final String CAMERA_IMAGE_BUCKET_ID = String.valueOf(CAMERA_IMAGE_BUCKET_NAME
			.toLowerCase().hashCode());

	public MediaListGetter(ContentResolver mContentResolver) {
		super();
		this.mContentResolver = mContentResolver;
	}

	private final ContentResolver mContentResolver;

	/**
	 *  获取所有Camera图片文件夾列表
	 * @return
	 */
	public HashMap<String, String> getCameraImages() {
		// 内置存储的图片
		Uri baseUri = INTERNAL_STORAGE_URI;
		Uri uri = baseUri.buildUpon().appendQueryParameter("distinct", "true").build();
		Cursor internal = Media.query(mContentResolver, uri, new String[] {
				Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID }, whereClause(CAMERA_IMAGE_BUCKET_ID),
				whereClauseArgs(CAMERA_IMAGE_BUCKET_ID), null);

		// 外置存储的图片
		baseUri = EXTERNAL_STORAGE_URI;
		uri = baseUri.buildUpon().appendQueryParameter("distinct", "true").build();
		Cursor external = Media.query(mContentResolver, uri, new String[] {
				Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID }, whereClause(CAMERA_IMAGE_BUCKET_ID),
				whereClauseArgs(CAMERA_IMAGE_BUCKET_ID), null);

		try {
			HashMap<String, String> hash = new HashMap<String, String>();
			while (internal.moveToNext()) {
				hash.put(internal.getString(1), internal.getString(0));
			}
			while (external.moveToNext()) {
				hash.put(external.getString(1), external.getString(0));
			}
			return hash;
		} finally {
			internal.close();
			external.close();
		}
	}

	/**
	 *  获取所有Camera视频文件夾列表
	 * @return
	 */
	public HashMap<String, String> getCameraVidios() {
		Uri baseUri = VIDEO_STORAGE_URI;
		Uri uri = baseUri.buildUpon().appendQueryParameter("distinct", "true").build();
		Cursor cursor = Media.query(mContentResolver, uri, new String[] {
				Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID },
				(CAMERA_IMAGE_BUCKET_ID != null ? Images.Media.BUCKET_ID + " = '"
						+ CAMERA_IMAGE_BUCKET_ID + "'" : null), null, null);

		try {
			HashMap<String, String> hash = new HashMap<String, String>();
			while (cursor.moveToNext()) {
				hash.put(cursor.getString(1), cursor.getString(0));
			}
			return hash;
		} finally {
			cursor.close();
		}
	};

	/**
	 *  获取所有Camera媒体文件夾列表
	 * @return
	 */
	public void getCameraMedias() {
		// getCameraVidios + getCameraImages
	};

	/**
	 *  获取所有圖片的文件夾列表
	 * @return
	 */
	public HashMap<String, String> getAllImages() {
		Uri baseUri = EXTERNAL_STORAGE_URI;
		Uri uri = baseUri.buildUpon().appendQueryParameter("distinct", "true").build();
		// 外置存储camera图片
		Cursor camera = Media.query(mContentResolver, uri, new String[] {
				Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID }, whereClause(CAMERA_IMAGE_BUCKET_ID),
				whereClauseArgs(CAMERA_IMAGE_BUCKET_ID), null);
		// 外置存储非camera图片
		Cursor external = Media.query(mContentResolver, uri, new String[] {
				Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID }, whereClause(null),
				whereClauseArgs(null), null);
		// 内置图片
		baseUri = INTERNAL_STORAGE_URI;
		uri = baseUri.buildUpon().appendQueryParameter("distinct", "true").build();
		Cursor internal = Media.query(mContentResolver, uri, new String[] {
				Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID }, whereClause(null),
				whereClauseArgs(null), null);

		try {
			HashMap<String, String> hash = new HashMap<String, String>();
			while (camera.moveToNext()) {
				hash.put(camera.getString(1), camera.getString(0));
			}
			while (external.moveToNext()) {
				hash.put(external.getString(1), external.getString(0));
			}
			while (internal.moveToNext()) {
				hash.put(internal.getString(1), internal.getString(0));
			}
			return hash;
		} finally {
			camera.close();
			external.close();
			internal.close();
		}
	};

	/**
	 *  获取所有視頻的文件夾列表
	 * @return
	 */
	public HashMap<String, String> getAllVidios() {
		Uri baseUri = VIDEO_STORAGE_URI;
		Uri uri = baseUri.buildUpon().appendQueryParameter("distinct", "true").build();
		Cursor cursor = Media.query(mContentResolver, uri, new String[] {
				Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID }, null, null, null);
		try {
			HashMap<String, String> hash = new HashMap<String, String>();
			while (cursor.moveToNext()) {
				hash.put(cursor.getString(1), cursor.getString(0));
			}
			return hash;
		} finally {
			cursor.close();
		}
	};

	/**
	 *  获取外置存储的所有文件夹
	 * @return
	 */
	public HashMap<String, String> getExternalFolders() {
		Uri baseUri = EXTERNAL_STORAGE_URI;
		Uri uri = baseUri.buildUpon().appendQueryParameter("distinct", "true").build();
		Cursor cursor = Media.query(mContentResolver, uri, new String[] {
				Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID }, null, null, null);
		try {
			HashMap<String, String> hash = new HashMap<String, String>();
			while (cursor.moveToNext()) {
				if (CAMERA_IMAGE_BUCKET_ID.equals(cursor.getString(1)))
					continue;
				hash.put(cursor.getString(1), cursor.getString(0));
			}
			return hash;
		} finally {
			cursor.close();
		}
	}
}
