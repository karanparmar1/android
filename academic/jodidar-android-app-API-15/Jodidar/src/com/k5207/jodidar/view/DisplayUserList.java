package com.k5207.jodidar.view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.k5207.jodidar.R;
import com.k5207.jodidar.model.User;
import com.k5207.jodidar.service.PostData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayUserList extends Activity {

	ListView listView;
	List<User> users;
	customListAdapter customListAdapt;
	TextView headText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		headText=(TextView)findViewById(R.id.textView1);
		users = new ArrayList<User>();
		Bundle b = getIntent().getExtras();
		listView = (ListView) findViewById(R.id.listView1);

		String response = "";
		try {
			if (b != null) {
				response = b.getString("jsonResponse").trim();
				loadIntoListView(response);
			} 
			else {
				response =  new PostData(DisplayUserList.this).execute("filter3.php", new JSONObject().toString()).get();
				loadIntoListView(response);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "SOME JsonException!", Toast.LENGTH_SHORT).show();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} 

	}

	private void loadIntoListView(String json) throws JSONException {

		JSONObject result = new JSONObject(json);
		JSONArray jsonArray;
		if (!result.getBoolean("error")) {
			jsonArray = result.getJSONArray("user");
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					users.add(new User(Integer.parseInt(obj.getString("id")), obj.getString("firstname"),
							obj.getString("lastname"), obj.getString("email"), Date.valueOf((obj.getString("bday"))),
							obj.getString("gender"), obj.getString("religion"), obj.getString("caste"), obj.getString("location"),
							obj.getString("education"), obj.getString("profession"), obj.getString("mothertongue"),
							Double.parseDouble(obj.getString("height")), obj.getString("eating"), obj.getString("manglik"),
							obj.getString("lookingfor"), obj.getString("about"), obj.getString("image"),
							Integer.parseInt(obj.getString("online"))));

				}
			}
			Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
			Log.i("userList_madyu", result.getString("message"));
		} else {
			Log.e("userListError", result.getString("message"));
			headText.setText(result.getString("message"));
			Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
		}
		customListAdapt = new customListAdapter(this, R.layout.my_custom_list, users, result);
		listView.setAdapter(customListAdapt);

	}

}
