package com.k5207.jodidar.view;

import java.sql.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.k5207.jodidar.R;
import com.k5207.jodidar.service.PostData;

public class SignupActivity extends Activity {
	Button btnSignup;
	EditText txtFname, txtLname, txtEmail, txtPassword, txtBday;
	String fname, lname, email, password, gender, txtDate;
	Date bDate;
	RadioGroup rg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		txtFname = (EditText) findViewById(R.id.txtFname);
		txtLname = (EditText) findViewById(R.id.txtLname);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtBday = (EditText) findViewById(R.id.txtBday);
		btnSignup = (Button) findViewById(R.id.btnSignupSubmit);
		rg = (RadioGroup) findViewById(R.id.rdoGender);
		txtBday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								bDate = Date.valueOf(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);

								txtBday.setText("BirthDate : " + dayOfMonth + " / " + (monthOfYear+1) + " / " + year);
							}
						}, 1990, 01, 01);
				datePickerDialog.show();

			}
		});
		btnSignup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				fname = txtFname.getText().toString().trim();
				lname = txtLname.getText().toString().trim();
				email = txtEmail.getText().toString().trim();
				password = txtPassword.getText().toString().trim();
				gender = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();

				if (validate()) {
					// Async Task Call here
					Toast.makeText(getApplicationContext(), "USER : " + " Name:" + fname + lname + " ,Email:" + email
							+ " ,Password:" + password + " Gender:" + gender + " ,Bday:" + bDate, Toast.LENGTH_LONG).show();
					JSONObject object = new JSONObject();
					try {
						object.put("firstname", fname);
						object.put("lastname", lname);
						object.put("email", email);
						object.put("password", password);
						object.put("gender", gender);
						object.put("bday", bDate);
						object.put("image", (gender.equalsIgnoreCase("male")) ? "user_male.png" : "user_female.png");

					} catch (JSONException ex) {
						ex.printStackTrace();
						Log.e("ERROR", "Something went wrong!", ex);
					}
					String response = "";
					try {
						response = new PostData().execute("signup.php", object.toString()).get();
						try {
							JSONObject obj = new JSONObject(response);
							if (!obj.getBoolean("error")) {
								showToast(obj.getString("message"));

								Log.i("Signup_Success", obj.getString("message"));
								Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
								startActivity(intent);

							} else {
								showToast(obj.getString("message"));
								Log.e("Signup_Fail", obj.getString("message"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(getApplicationContext(), "Empty Field(s)", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	boolean validate() {
		boolean isValid = true;
		if (fname.equals("")) {
			txtFname.setError("Fill FirstName");
			isValid = false;
		} else
			txtFname.setError(null);

		if (lname.equals("")) {
			txtLname.setError("Fill LastName");
			isValid = false;
		} else
			txtLname.setError(null);

		if (email.equals("")) {
			txtEmail.setError("Fill Email");
			isValid = false;
		} else
			txtEmail.setError(null);

		if (password.equals("")) {
			txtPassword.setError("Fill Pass");
			isValid = false;
		} else
			txtPassword.setError(null);

		if (bDate == null) {
			txtBday.setError("Set BirthDate");
			isValid = false;
		} else
			txtBday.setError(null);

		return isValid;

	}

	public void showToast(String msg) {

		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}

}
