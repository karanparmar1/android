package com.k5207.jodidar.service;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.k5207.jodidar.R;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GetData extends AsyncTask<String, Object, String> {
	Context ctx = null;
	ProgressBar progressBar=null;

	public GetData() {
	}

	public GetData(Context applicationContext) {
		this.ctx = applicationContext;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (ctx != null) {
			progressBar = (ProgressBar) ((Activity) ctx).findViewById(R.id.progressBar);
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
		if (ctx != null && progressBar!=null)
			progressBar.setVisibility(View.GONE);

	}

	@Override
	protected String doInBackground(String... params) {
		String url = Session.APIURL + params[0];
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		try {
			HttpResponse response = client.execute(request);

			Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("ERROR", "Something went wrong! ClientProtocolException", e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("ERROR", "Something went wrong! IOException", e);
		}

		return "";
	}

	public void showToast(String msg) {
		if (ctx != null)
			Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}
}
