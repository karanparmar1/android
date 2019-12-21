package com.k5207.jodidar;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.k5207.jodidar.service.PostData;
import com.k5207.jodidar.service.Session;
import com.k5207.jodidar.view.DisplayUserList;
import com.k5207.jodidar.view.FilterActivity;
import com.k5207.jodidar.view.LoginActivity;
import com.k5207.jodidar.view.ProfileActivity;
import com.k5207.jodidar.view.SignupActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button btnLogin, btnSignup, btnFilter, btnUserlist, btnProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignup = (Button) findViewById(R.id.btnSignup);
		btnFilter = (Button) findViewById(R.id.btnFilter);
		btnUserlist = (Button) findViewById(R.id.btnUserList);
		btnProfile = (Button) findViewById(R.id.btnProfile);

		btnUserlist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String response;
				try {
					response = new PostData(MainActivity.this, DisplayUserList.class)
							.execute("filter3.php", new JSONObject().toString()).get();

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}
		});

		btnSignup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, SignupActivity.class);
				startActivity(i);

			}
		});

		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(i);
			}
		});

		btnFilter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, FilterActivity.class);
				startActivity(i);
			}
		});

		btnProfile.setOnClickListener(new View.OnClickListener() {
			String response;

			@Override
			public void onClick(View v) {
				JSONObject obj = new JSONObject();
				int current = new Session(getApplicationContext()).getCurrentUser().getId();
				Log.i("currentuserId", current + "");
				if (current > 1) {
					try {
						obj.put("id", current);
						response = new PostData(MainActivity.this, ProfileActivity.class).execute("getUserById.php", obj.toString()).get();
						/*Intent i = new Intent(MainActivity.this, ProfileActivity.class);
						i.putExtra("jsonResponse", response);
						i.putExtra("profileType", "self");
						startActivity(i);*/
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(getApplicationContext(), "Login First !", Toast.LENGTH_LONG).show();
					startActivity(new Intent(MainActivity.this, LoginActivity.class));
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
