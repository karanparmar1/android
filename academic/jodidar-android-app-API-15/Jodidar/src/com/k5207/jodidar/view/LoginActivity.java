package com.k5207.jodidar.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.k5207.jodidar.R;
import com.k5207.jodidar.model.User;
import com.k5207.jodidar.service.LoginService;
import com.k5207.jodidar.service.PostData;
import com.k5207.jodidar.service.Session;

public class LoginActivity extends Activity {
	Button btnLogin;
	EditText txtUsername, txtPassword;
	String username, password, response;
	User user;
	Session session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		btnLogin = (Button) findViewById(R.id.btn_submit);
		txtUsername = (EditText) findViewById(R.id.txt_username);
		txtPassword = (EditText) findViewById(R.id.txt_password);
		session = new Session(this);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				username = txtUsername.getText().toString().trim();
				password = txtPassword.getText().toString().trim();
				if (validate()) {
					LoginService loginService = new LoginService(getApplicationContext());
					user = loginService.login(username, password);
					if (user != null) {

						session.setUserSession(user);
						Log.i("Current", session.getCurrentUser().toString());
						finish();
						try {
							response = new PostData(LoginActivity.this, ProfileActivity.class)
									.execute("getUserById.php", new JSONObject().put("id", user.getId()).toString()).get();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					}

				} else {
					Toast.makeText(getApplicationContext(), "Empty Field(s)", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	boolean validate() {
		boolean isValid = true;
		if (username.equals("")) {
			txtUsername.setError("Enter Username");
			isValid = false;
		} else
			txtUsername.setError(null);

		if (password.equals("")) {
			txtPassword.setError("Enter Pass");
			isValid = false;
		} else
			txtPassword.setError(null);

		return isValid;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
