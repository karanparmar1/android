package com.k5207.jodidar.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.k5207.jodidar.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PostData extends AsyncTask<String, Object, String> {
	Context ctx = null;
	ProgressBar progressBar;
	Class classname = null;
	Button btnClicked = null, btnToShow = null;

	public PostData() {
	}

	public PostData(Context applicationContext) {
		this.ctx = applicationContext;
	}

	public PostData(Context applicationContext, Button btnClicked, Button btnToShow) {
		this.ctx = applicationContext;
		this.btnClicked = btnClicked;
		this.btnToShow = btnToShow;
	}

	public PostData(Context applicationContext, Class destination) {
		this.ctx = applicationContext;
		this.classname = destination;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (ctx != null) {
			progressBar = (ProgressBar) ((Activity) ctx).findViewById(R.id.progressBar);
			progressBar.setVisibility(View.VISIBLE);
			if (btnClicked != null && btnToShow != null) {
				btnClicked.setEnabled(false);
			}
		}
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
		Log.i("PostExecute", "response:" + s);
		if (ctx != null && progressBar != null) {
			progressBar.setVisibility(View.GONE);
			if (classname != null) {
				Intent intent = new Intent(ctx, classname);
				intent.putExtra("jsonResponse", s);
				ctx.startActivity(intent);
			}
			if (btnClicked != null && btnToShow != null) {
				btnClicked.setVisibility(View.GONE);
				btnClicked.setEnabled(true);
				btnToShow.setVisibility(View.VISIBLE);
			}

		}
	}

	@Override
	protected String doInBackground(String... params) {
		String url = Session.APIURL + params[0];
		// String key = "data"; // "data" is key For posting data as Json in php:
		// $data=json_decode($_POST["data"]);

		String values = params[1];

		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("data", values));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse response = client.execute(request);
			Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("ERROR", "Something went wrong! ,Exception:ClientProtocolException", e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("ERROR", "Something went wrong ! Exception: IOEXception", e);
		}

		return "";
	}

	public void showToast(String msg) {
		if (ctx != null)
			Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}

}
