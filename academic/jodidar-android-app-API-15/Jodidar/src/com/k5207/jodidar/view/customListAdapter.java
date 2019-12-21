package com.k5207.jodidar.view;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.k5207.jodidar.R;
import com.k5207.jodidar.model.User;
import com.k5207.jodidar.service.LoadImage;
import com.k5207.jodidar.service.PostData;
import com.k5207.jodidar.service.Session;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class customListAdapter extends ArrayAdapter<User> {

	List<User> userList;
	Context context;
	int resource;
	Bitmap b;
	User user;
	JSONObject result;
	ProgressBar progressBar;
	String response;
	Session session = new Session();

	public customListAdapter(Context context, int resource, List<User> userList, JSONObject result) {
		super(context, resource, userList);
		this.userList = userList;
		this.context = context;
		this.resource = resource;
		this.result = result;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// we need to get the view of the xml for our list item
		// And for this we need a layoutinflater
		LayoutInflater layoutInflater = LayoutInflater.from(context);

		// getting the view
		View view = layoutInflater.inflate(resource, null, false);

		// getting the view elements of the list from the view
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
		TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
		TextView textViewSecond = (TextView) view.findViewById(R.id.textViewSecond);
		Button buttonDelete = (Button) view.findViewById(R.id.btnDelete);
		Button buttonDisplay = (Button) view.findViewById(R.id.btnDisplay);
		ImageView onlineIcon = (ImageView) view.findViewById(R.id.onlineIcon);
		// ScrollView sc = (ScrollView) view.findViewById(R.id.scroller1);
		// ListView listview = (List)
		// getting the User of the specified position
		user = userList.get(position);
		Log.i("image", user.getImage());
		// imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.sun));
		try {
			imageView.setImageBitmap(new LoadImage().execute(user.getImage()).get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		textViewName.setText(
			user.getFirstname() + " " + user.getLastname() + "\n" + user.getAge(user.getBday()) + "yrs , " + user.getGender());
		
		textViewSecond.setText("from " + user.getLocation() + "\nProfession:" + user.getProfession());
		if (user.isOnline()) {
			onlineIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.online_green));
		}

		buttonDisplay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				user = userList.get(position);
				Log.i("userToString", user.toString());
				JSONObject obj = new JSONObject();
				try {
					obj.put("id", user.getId());
					obj.put("receiverid", new Session(context).getCurrentUser().getId());
					response = new PostData(context, ProfileActivity.class).execute("getUserById.php", obj.toString()).get();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}
		});
		buttonDelete.setVisibility(View.GONE);
		buttonDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// removeUser(position);
			}
		});
		return view;
	}

	void removeUser(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Do You want to Hide this user ?");

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				userList.remove(position);
				notifyDataSetChanged();

			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

}
