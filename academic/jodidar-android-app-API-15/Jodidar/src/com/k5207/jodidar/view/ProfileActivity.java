package com.k5207.jodidar.view;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.k5207.jodidar.MainActivity;
import com.k5207.jodidar.R;
import com.k5207.jodidar.model.User;
import com.k5207.jodidar.service.LoadImage;
import com.k5207.jodidar.service.LoginService;
import com.k5207.jodidar.service.PostData;
import com.k5207.jodidar.service.Session;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	User user;
	String profileType = "other", reqStatus;
	TextView txtEditBtn;
	EditText txtEmail, txtFname, txtLname, txtBday, txtLocation, txtReligion, txtManglik, txtAbout;
	List<EditText> txtboxList;
	Calendar cal;
	Date bDate, tempDate;
	Button btnSaveChanges, btnLogout, btnSendReq, btnCancelReq, btnAcceptReq, btnRejectReq, btnUnfriend;
	LinearLayout friendsBtns;
	ImageView imageView, onlineIcon;
	Resources res;
	Drawable pencilIcon;
	Session session;
	JSONObject result, obj, friendAction;
	int currentId, whoSent;
	String response;
	JSONObject editedObj;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_profile);
		findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		imageView = (ImageView) findViewById(R.id.profileImage);
		txtEditBtn = (TextView) findViewById(R.id.txtEditBtn);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtFname = (EditText) findViewById(R.id.txtFname);
		txtLname = (EditText) findViewById(R.id.txtLname);
		txtBday = (EditText) findViewById(R.id.txtBday);
		txtLocation = (EditText) findViewById(R.id.txtLocation);
		txtReligion = (EditText) findViewById(R.id.txtReligion);
		txtManglik = (EditText) findViewById(R.id.txtManglik);
		btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);
		btnLogout = (Button) findViewById(R.id.btnLogout);

		friendsBtns = (LinearLayout) findViewById(R.id.friendButtons);
		btnSendReq = (Button) findViewById(R.id.btnSendReq);
		btnCancelReq = (Button) findViewById(R.id.btnCancelReq);
		btnAcceptReq = (Button) findViewById(R.id.btnAcceptReq);
		btnRejectReq = (Button) findViewById(R.id.btnRejectReq);
		btnUnfriend = (Button) findViewById(R.id.btnUnfriend);

		cal = Calendar.getInstance();
		res = getApplicationContext().getResources();
		session = new Session(getApplicationContext());
		currentId = session.getCurrentUser().getId();
		if (currentId < 1) {
			finish();
			Log.e("NotLoggedin", "ProfileActivty:LogIn first!");
			Toast.makeText(getApplicationContext(), "Login First !", Toast.LENGTH_LONG).show();
			startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
		} else { // IsLoggedIn
			b = getIntent().getExtras();
			response = (b.getString("jsonResponse"));
			Log.i("CurrentId", "inProfileActivity : " + currentId);
			Log.i("jsonResponse", "profileActivity:JSONresponse\n" + response);
			try {
				result = new JSONObject(response);
				obj = result.getJSONObject("user");
				user = new User(Integer.parseInt(obj.getString("id")), obj.getString("firstname"), obj.getString("lastname"),
						obj.getString("email"), Date.valueOf((obj.getString("bday"))), obj.getString("gender"),
						obj.getString("religion"), obj.getString("caste"), obj.getString("location"), obj.getString("education"),
						obj.getString("profession"), obj.getString("mothertongue"), Double.parseDouble(obj.getString("height")),
						obj.getString("eating"), obj.getString("manglik"), obj.getString("lookingfor"), obj.getString("about"),
						obj.getString("image"), Integer.parseInt(obj.getString("online")));
				whoSent = result.getInt("whoSent");
				reqStatus = result.getString("status");
				imageView.setImageBitmap(new LoadImage().execute(user.getImage()).get());
				bDate = user.getBday();
				cal.setTime(bDate);

			} catch (JSONException e) {
				Log.e("userJsonError", "InProfileActvity:" + e);
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			profileType = b.containsKey("profileType") ? b.getString("profileType")
					: (user.getId() == currentId) ? "self" : "other";
			Log.i("ProfileType", profileType);

			pencilIcon = res.getDrawable(R.drawable.edit_pencil_16);
			txtboxList = Arrays.asList(txtEmail, txtFname, txtLname, txtBday, txtLocation, txtReligion, txtManglik);
			onlineIcon = (ImageView) findViewById(R.id.onlineIcon);

			hide(btnLogout);
			session = new Session(this);

			txtEmail.setText(user.getEmail());
			txtFname.setText(user.getFirstname());
			txtLname.setText(user.getLastname());
			txtLocation.setText(user.getLocation());
			txtReligion.setText(user.getReligion());
			txtManglik.setText(user.getManglik());
			// txtBday.setText(user.getBday().toString());
			txtBday.setText(
					"BirthDate : " + cal.get(Calendar.DATE) + " / " + (cal.get(Calendar.MONTH)+1) + " / " + cal.get(Calendar.YEAR));
			findViewById(R.id.progressBar).setVisibility(View.GONE);

			btnSendReq.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String response = "";
					try {
						friendAction = new JSONObject();
						friendAction.put("action", "send");
						friendAction.put("senderid", currentId);
						friendAction.put("receiverid", user.getId());
						response = new PostData(ProfileActivity.this, btnSendReq, btnCancelReq)
								.execute("friends.php", friendAction.toString()).get();
						JSONObject result = new JSONObject(response);
						if (result.getBoolean("error")) {
							Log.e("SendReq_Err", result.getString("message"));
							showToast(result.getString("message"));
						} else {
							Log.i("SendReq_Success", result.getString("message"));
							showToast(result.getString("message"));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			btnCancelReq.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String response = "";
					try {
						friendAction = new JSONObject();
						friendAction.put("action", "cancel");
						friendAction.put("senderid", currentId);
						friendAction.put("receiverid", user.getId());
						response = new PostData(ProfileActivity.this, btnCancelReq, btnSendReq)
								.execute("friends.php", friendAction.toString()).get();
						JSONObject result = new JSONObject(response);
						if (result.getBoolean("error")) {
							Log.e("SendReq_Err", result.getString("message"));
							showToast(result.getString("message"));
						} else {
							Log.i("SendReq_Success", result.getString("message"));
							showToast(result.getString("message"));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			btnAcceptReq.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					hide(btnRejectReq);
					String response = "";
					try {
						friendAction = new JSONObject();
						friendAction.put("action", "accept");
						friendAction.put("senderid", user.getId());
						friendAction.put("receiverid", currentId);
						response = new PostData(ProfileActivity.this, btnAcceptReq, btnUnfriend)
								.execute("friends.php", friendAction.toString()).get();
						JSONObject result = new JSONObject(response);
						if (result.getBoolean("error")) {
							Log.e("SendReq_Err", result.getString("message"));
							showToast(result.getString("message"));
						} else {
							Log.i("SendReq_Success", result.getString("message"));
							showToast(result.getString("message"));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			btnRejectReq.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					btnAcceptReq.setVisibility(View.INVISIBLE);
					String response = "";
					try {
						friendAction = new JSONObject();
						friendAction.put("action", "reject");
						friendAction.put("senderid", user.getId());
						friendAction.put("receiverid", currentId);
						response = new PostData(ProfileActivity.this, btnRejectReq, btnSendReq)
								.execute("friends.php", friendAction.toString()).get();
						JSONObject result = new JSONObject(response);
						if (result.getBoolean("error")) {
							Log.e("SendReq_Err", result.getString("message"));
							showToast(result.getString("message"));
						} else {
							Log.i("SendReq_Success", result.getString("message"));
							showToast(result.getString("message"));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			btnUnfriend.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String response = "";
					try {
						friendAction = new JSONObject();
						friendAction.put("action", "unfriend");
						friendAction.put("senderid", user.getId());
						friendAction.put("receiverid", currentId);
						response = new PostData(ProfileActivity.this, btnUnfriend, btnSendReq)
								.execute("friends.php", friendAction.toString()).get();
						JSONObject result = new JSONObject(response);
						if (result.getBoolean("error")) {
							Log.e("SendReq_Err", result.getString("message"));
							showToast(result.getString("message"));
						} else {
							Log.i("SendReq_Success", result.getString("message"));
							showToast(result.getString("message"));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			});

			if (profileType.equals("other")) {
				txtEditBtn.setVisibility(View.GONE);

				if (reqStatus.equalsIgnoreCase("none")) { /* Req Not Sent/received */
					show(btnSendReq);
				}

				if (reqStatus.equalsIgnoreCase("pending")) {
					if (whoSent == currentId) {
						show(btnCancelReq);
					} else {
						show(btnAcceptReq);
						show(btnRejectReq);
					}
				}

				if (reqStatus.equalsIgnoreCase("accepted")) {
					show(btnUnfriend);
				}

			} else { /* If Visiting self Profile */
				friendsBtns.setVisibility(View.GONE);
				editedObj = new JSONObject();
				txtEditBtn.setVisibility(View.VISIBLE);
				show(btnLogout);
				if (session.isLoggedIn()) {

					if (user.getOnline() == 1) {
						onlineIcon.setImageDrawable(res.getDrawable(R.drawable.online_green));
						Log.i("profileOnline", "user" + user.getEmail() + ",online:" + user.isOnline());
					}

				}
				txtEditBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onlineIcon.setVisibility(View.GONE);
						hide(btnLogout);
						txtFname.requestFocus();
						for (EditText editText : txtboxList) {
							editText.setGravity(Gravity.LEFT);
							editText.setPadding(5, 5, 5, 5);
							editText.setBackgroundColor(Color.rgb(41, 186, 181));
							editText.setTextColor(Color.rgb(255, 255, 255));
							editText.setEnabled(true);
							editText.setClickable(true);
							if (editText == txtFname || editText == txtLname || editText == txtAbout) {
								editText.setFocusable(true);
								editText.setCursorVisible(true);
								editText.setFocusableInTouchMode(true);
								editText.setInputType(InputType.TYPE_CLASS_TEXT);
							}
							editText.setCompoundDrawablesWithIntrinsicBounds(null, null, pencilIcon, null);
							editText.setTypeface(null, Typeface.NORMAL);

						}

						displayDialogAndSetJson(txtLocation, "location", getResources().getStringArray(R.array.locations),
								txtLocation.getText().toString());
						displayDialogAndSetJson(txtReligion, "religion", getResources().getStringArray(R.array.religions),
								txtReligion.getText().toString());
						displayDialogAndSetJson(txtManglik, "manglik", getResources().getStringArray(R.array.manglik),
								txtManglik.getText().toString());
						txtBday.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,
										new DatePickerDialog.OnDateSetListener() {
											public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
												bDate = Date.valueOf(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
												txtBday.setText("BirthDate : " + dayOfMonth + " / " + (monthOfYear+1) + " / " + year);
											}
										}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
								datePickerDialog.show();

							}
						}); /* BDAY TXTBOX CLicked */

						show(btnSaveChanges);

						btnSaveChanges.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								for (EditText editText : txtboxList) {
									editText.setGravity((editText == txtFname) ? Gravity.RIGHT : Gravity.LEFT);
									editText.setPadding(0, 0, 0, 0);
									editText.setBackgroundColor(Color.TRANSPARENT);
									editText.setTextColor(Color.BLACK);
									editText.setEnabled(false);
									editText.setClickable(false);
									if (editText != txtBday) {
										editText.setFocusable(false);
										editText.setCursorVisible(false);
										editText.setFocusableInTouchMode(false);
										editText.setInputType(InputType.TYPE_CLASS_TEXT);
									}
									editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
									editText.setTypeface(null,
											(editText == txtFname || editText == txtLname) ? Typeface.BOLD : Typeface.NORMAL);

									hide(btnSaveChanges);
									onlineIcon.setVisibility(View.VISIBLE);
									show(btnLogout);
								}
							}
						});
					}
				}); /* EDITButton CLICKED */

				btnLogout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						LoginService loginService = new LoginService(getApplicationContext());
						loginService.logout(session.getCurrentUser().getEmail());
						session.clearUserSession();
						finish();
						Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
						startActivity(intent);

					}
				});
			} /* Else Closed(For If Current User visits selfProfile) */
		} /* Else Closed(For If user has Logged In) */
	} /* OnCreate CLosed */

	int selectedPosition = -1;
	String selectedItem = "";

	public void displayDialogAndSetJson(final EditText passedView, final String objName, final String[] itemArray,
			final String defItem) {
		itemArray[0] = "other";
		passedView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < itemArray.length; i++) {
					if (itemArray[i].equalsIgnoreCase(defItem)) {
						selectedPosition = i;
					}
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
				builder.setTitle("Set " + objName);
				builder.setSingleChoiceItems(itemArray, selectedPosition, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						selectedItem = Arrays.asList(itemArray).get(i);
					}
				});
				builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						shortToast("selected " + objName + ":" + selectedItem);
						passedView.setText(selectedItem);
						try {
							editedObj.put(objName, selectedItem);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.profile, menu);

		if (session.isLoggedIn()) {
			menu.add("LOG OUT").setIcon(R.drawable.logout).setIntent(new Intent(this, MainActivity.class));
		} else {
			menu.add("LOG IN").setIcon(R.drawable.arrow_right_color).setIntent(new Intent(this, LoginActivity.class));
		}

		menu.add("Find Users").setIcon(R.drawable.list_check_apporve).setIntent(new Intent(this, FilterActivity.class));

		menu.add("Display Users").setIcon(R.drawable.user_list).setIntent(new Intent(this, DisplayUserList.class));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	public void shortToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	public void show(Button btn) {
		btn.setVisibility(View.VISIBLE);
	}

	public void hide(Button btn) {
		btn.setVisibility(View.GONE);
	}

}
