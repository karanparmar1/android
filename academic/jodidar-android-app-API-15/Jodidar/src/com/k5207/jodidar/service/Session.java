package com.k5207.jodidar.service;

import com.k5207.jodidar.model.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class Session {

	SharedPreferences spf;
	Context activity;
	public static String APIURL = "http://192.168.43.15:81/jodidar/";

	public Session() {
	}

	public Session(Context c) {
		this.activity = c;
	}

	public void setUserSession(User user) {
		spf = activity.getSharedPreferences("session", Context.MODE_PRIVATE);
		final Editor edt_spf = spf.edit();
		edt_spf.putInt("userId", user.getId());
		edt_spf.putString("userImage", user.getImage());
		edt_spf.putString("userEmail", user.getEmail());
		edt_spf.putString("userFirstname", user.getFirstname());
		edt_spf.putString("userLastname", user.getLastname());
		edt_spf.putInt("online", user.getOnline());
		// edt_spf.putString("userBday", user.getBday().toString());
		edt_spf.commit();
	}

	public User getCurrentUser() {

		User u = new User();
		try {
			spf = activity.getSharedPreferences("session", Context.MODE_PRIVATE);
			u.setId(spf.getInt("userId", -1));
			u.setEmail(spf.getString("userEmail", ""));
			u.setFirstname(spf.getString("userFirstname", null));
			u.setImage(spf.getString("userImage", null));
			u.setOnline(spf.getInt("online", 0));
		} catch (NullPointerException e) {
			Log.e("NoUser","Login Firsts");
			showToast("You need to Log in First");
		}
		return u;
	}

	public boolean isLoggedIn() {
		spf = activity.getSharedPreferences("session", Context.MODE_PRIVATE);
		return (spf.getString("userEmail", null) != null);
	}

	public void clearUserSession() {
		spf = activity.getSharedPreferences("session", Context.MODE_PRIVATE);
		final Editor edt_spf = spf.edit();
		edt_spf.clear().commit();
	}

	
	public void showToast(String msg) {
		if (activity != null)
			Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}

}
