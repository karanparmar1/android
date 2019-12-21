package com.k5207.jodidar.service;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.k5207.jodidar.model.User;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LoginService {
	User user;
	Context activity = null;

	public LoginService() {
	}

	public LoginService(Context ctx) {
		this.activity = ctx;
	}

	public String toJsonString(String email, String password) {

		JSONObject object = new JSONObject();
		try {
			object.put("email", email);
			object.put("password", password);
		} catch (JSONException ex) {
			ex.printStackTrace();
			Log.e("ERROR", "Something went wrong!", ex);
		}

		return object.toString();

	}

	public User login(String email, String password) {

		User user = null;

		// String loginJsonString = toJsonString(email, password);
		try {
			String response = new PostData().execute("login.php", toJsonString(email, password)).get();
			Log.i("JsonFromApi", response);
			try {
				JSONObject obj = new JSONObject(response);

				if (!obj.getBoolean("error")) {
					JSONObject userJson = obj.getJSONObject("user");
					user = new User(Integer.parseInt(userJson.getString("id")), userJson.getString("email"),
							userJson.getString("firstname"));
					user.setOnline(userJson.getInt("online"));
					showToast(obj.getString("message"));

					Log.i("LOGIN_Success", obj.getString("message"));

				} else {
					showToast(obj.getString("message"));
					Log.e("LOGIN_Fail", obj.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}

	public void logout(String email) {
		try {
			JSONObject object = new JSONObject();
			try {
				object.put("email", email);
			} catch (JSONException ex) {
				ex.printStackTrace();
				Log.e("ERROR", "Something went wrong!", ex);
			}

			String response = new PostData().execute("logout.php", object.toString()).get();
			Log.i("JsonFromApi", response);
			try {
				JSONObject obj = new JSONObject(response);
				if (!obj.getBoolean("error")) {

					showToast(obj.getString("message"));
					Log.i("LOGOUT_Success", obj.getString("message"));

				} else {
					showToast(obj.getString("message"));
					Log.e("LOGOUT_Fail", obj.getString("message"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showToast(String msg) {
		if (activity != null)
			Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}

}
