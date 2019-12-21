package com.k5207.jodidar.service;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.k5207.jodidar.model.User;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class UserService {
	User user;
	Context activity = null;

	public UserService(Context ctx) {
		this.activity = ctx;
	}

	public User getUserById(int id) {

		try {
			String response = new PostData().execute("filter2.php", new JSONObject().put("id", id).toString()).get();
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}

	public void showToast(String msg) {
		if (activity != null)
			Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}

}
