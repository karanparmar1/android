package com.k5207.jodidar.service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadImage extends  AsyncTask<String, Void, Bitmap> {
	ImageView iv;
	
	protected Bitmap doInBackground(String... args) {
		String img = args[0];
		Bitmap b = null;
		try {
			URL url = new URL(Session.APIURL+"icon/"+img);
			InputStream is = new BufferedInputStream(url.openStream());
			b = BitmapFactory.decodeStream(is);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
