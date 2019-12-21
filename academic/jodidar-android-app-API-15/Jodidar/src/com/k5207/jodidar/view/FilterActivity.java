package com.k5207.jodidar.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.k5207.jodidar.R;
import com.k5207.jodidar.model.User;
import com.k5207.jodidar.service.PostData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FilterActivity extends Activity {

	Button btnSubmit, btnReligion, btnGender, btnAge, btnSaveAge, btnMothertongue, btnLocation, btnEducation, btnProfession,
			btnManglik;
	TextView tv;
	EditText txtMinAge, txtMaxAge;
	List<User> users;
	customListAdapter customListAdapt;
	String[] religionArray, locationArray, mothertongueArray, educationArray, professionArray, genderArray, manglikArray;
	boolean isValid = true;
	List<String> selectedItemsList;
	JSONObject obj = new JSONObject();
	JSONArray itemsArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		users = new ArrayList<User>();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		religionArray = getResources().getStringArray(R.array.religions);
		locationArray = getResources().getStringArray(R.array.locations);
		mothertongueArray = getResources().getStringArray(R.array.languages);
		educationArray = getResources().getStringArray(R.array.educations);
		professionArray = getResources().getStringArray(R.array.professions);
		genderArray = getResources().getStringArray(R.array.genders);
		manglikArray = getResources().getStringArray(R.array.manglik);
		selectedItemsList = new ArrayList<String>();
		selectedItemsList.add("Any");
		btnReligion = (Button) findViewById(R.id.btnReligion);
		btnGender = (Button) findViewById(R.id.btnGender);
		btnMothertongue = (Button) findViewById(R.id.btnLanguage);
		btnLocation = (Button) findViewById(R.id.btnLocation);
		btnEducation = (Button) findViewById(R.id.btnEducation);
		btnProfession = (Button) findViewById(R.id.btnProfession);
		btnManglik = (Button) findViewById(R.id.btnManglik);
		btnAge = (Button) findViewById(R.id.btnAge);
		txtMinAge = (EditText) findViewById(R.id.minAge);
		txtMinAge.setText("18");
		txtMaxAge = (EditText) findViewById(R.id.maxAge);
		txtMaxAge.setText("100");
		btnSaveAge = (Button) findViewById(R.id.btnSaveAge);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		tv = (TextView) findViewById(R.id.txtReligion);
		// checkedItemsArray = new boolean[religionArray.length]; //passed new Array
		// instead
		displayDialogAndSetJson(btnReligion, "religion", religionArray, new boolean[religionArray.length],
				getApplicationContext());
		displayDialogAndSetJson(btnGender, "gender", genderArray, new boolean[genderArray.length], getApplicationContext());
		displayDialogAndSetJson(btnLocation, "location", locationArray, new boolean[locationArray.length],
				getApplicationContext());
		displayDialogAndSetJson(btnMothertongue, "mothertongue", mothertongueArray, new boolean[mothertongueArray.length],
				getApplicationContext());
		displayDialogAndSetJson(btnEducation, "education", educationArray, new boolean[educationArray.length],
				getApplicationContext());
		displayDialogAndSetJson(btnProfession, "profession", professionArray, new boolean[professionArray.length],
				getApplicationContext());
		displayDialogAndSetJson(btnManglik, "manglik", manglikArray, new boolean[manglikArray.length], getApplicationContext());

		btnAge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				txtMaxAge.setVisibility(View.VISIBLE);
				txtMinAge.setVisibility(View.VISIBLE);
				btnSaveAge.setVisibility(View.VISIBLE);
				findViewById(R.id.tvDash).setVisibility(View.VISIBLE);
				btnAge.setVisibility(View.GONE);
			}
		});

		btnSaveAge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String txtMin = txtMinAge.getText().toString().trim();
				String txtMax = txtMaxAge.getText().toString().trim();
				boolean isValid = true;
				if (txtMin.equals("") || txtMin.equals(" ") || txtMin.equals(null) || Integer.parseInt(txtMin) < 18) {
					txtMinAge.setError("Enter MinAge");
					isValid = false;
				} else
					txtMinAge.setError(null);

				if (txtMax.equals("") || txtMin.equals(" ") || txtMax.equals(null) || Integer.parseInt(txtMax) > 100) {
					txtMaxAge.setError("Enter MaxAge");
					isValid = false;
				} else
					txtMaxAge.setError(null);
				if (isValid) {
					btnAge.setVisibility(View.VISIBLE);
					txtMaxAge.setVisibility(View.GONE);
					txtMinAge.setVisibility(View.GONE);
					findViewById(R.id.tvDash).setVisibility(View.GONE);
					btnSaveAge.setVisibility(View.GONE);

					try {
						Toast.makeText(getApplicationContext(),
								"MinAge:" + txtMinAge.getText() + " | MaxAge:" + txtMaxAge.getText(), Toast.LENGTH_LONG).show();
						obj.put("minAge", txtMinAge.getText());
						obj.put("maxAge", txtMaxAge.getText());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValid) {

					Toast.makeText(getApplicationContext(), "\nRequested:\n" + obj.toString(), Toast.LENGTH_SHORT).show();
					try {
						// obj.put("searchname", "par");
						String response = new PostData(FilterActivity.this, DisplayUserList.class)
								.execute("filter3.php", obj.toString()).get();
						// Intent intent = new Intent(FilterActivity.this, DisplayUserList.class);
						// intent.putExtra("jsonResponseFromFilter", response);
						// startActivity(intent);
						// Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
						// loadIntoListView(response);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}

				else
					Toast.makeText(getApplicationContext(), "Invalid Form,Empty Fields", Toast.LENGTH_SHORT).show();
			}
		});

	}

	public void displayDialogAndSetJson(final Button passedView, final String objName, final String[] itemArray,
			final boolean[] checkedItemsArray, Context c) {
		// itemArray[0] = "Any";
		checkedItemsArray[0] = true;
		passedView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(FilterActivity.this);

				itemsArray = new JSONArray();
				final List<String> itemsList = Arrays.asList(itemArray);
				// itemsList.set(0, "Any");
				builder.setMultiChoiceItems(itemArray, checkedItemsArray, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						checkedItemsArray[which] = isChecked; // Update the current focused item's checked status
						// String currentItem = itemsList.get(which); // Get the current focused item
						// Toast.makeText(getApplicationContext(), currentItem + " " + (isChecked ?
						// "selected" : "removed"),Toast.LENGTH_SHORT).show();
					}
				});
				builder.setTitle("Your preferred " + objName + "s ?");

				builder.setCancelable(false);

				builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						selectedItemsList.clear();
						passedView.setText(objName.toUpperCase() + "S : ");
						int coma = 0;
						for (int i = 0; i < checkedItemsArray.length; i++) {
							boolean checked = checkedItemsArray[i];

							if (checked) {
								passedView.setText(passedView.getText() + ((coma == 0) ? " " : " , ") + itemsList.get(i));
								++coma;
								selectedItemsList.add(itemsList.get(i));
							}
						}
						Log.i("SelectedItemsList.toString", selectedItemsList.toString());
						if (selectedItemsList.isEmpty()) {
							passedView.setError("Select Atleast one " + objName);
							isValid = false;
							passedView.setText("select Any " + objName);
							Toast.makeText(getApplicationContext(), "Select Atleast one " + objName, Toast.LENGTH_LONG).show();

						} else {
							passedView.setError(null);
							if (!selectedItemsList.isEmpty()) {
								isValid = true;
								for (String item : selectedItemsList) {
									itemsArray.put(item);
								}
								try {
									obj.put(objName, itemsArray);
									Log.i("JSON" + objName, obj.toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						}
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show(); // to display the alert dialog
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
