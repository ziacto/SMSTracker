package com.example.smstracker;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements LocationNeeder {

	private static final String LOCATION_NOT_ACQUIRED_YET = "L·ska, je to pomalÈ, strpenie prosÌm.\nInak vieö, ûe ùa milujem :)?";
	public static final String SUPER_TAG = "SMSTracker.";
	private static final String TAG = SUPER_TAG + "MainActivity";
	private static final CharSequence NO_GPS_FIX = "Eöte nechytil satelity";
	private ProgressBar progressBar;
	private Tracker tracker;
	private Location location;
	private ImageView signIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViewFields();
		tracker = new Tracker(this, this);
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "acquire location");
		progressBar.setVisibility(View.VISIBLE);
		signIcon.setImageResource(R.drawable.ic_warn);
		location = null;
		tracker.acquireLocation();
	}

	protected void onPause() {
		super.onPause();
		tracker.cancelUpdates();
	}

	private void initViewFields() {
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		signIcon = (ImageView) findViewById(R.id.signIcon);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendCoords(View view) {
		Log.d(TAG, "send sms");
		if (location != null) {
			send();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(NO_GPS_FIX);
			builder.setMessage(LOCATION_NOT_ACQUIRED_YET);
			builder.setPositiveButton(android.R.string.ok, null);
			builder.show();
		}
	}

	@Override
	public void locationChanged(Location location) {
		Log.d(TAG, "locationChanged");
		progressBar.setVisibility(View.INVISIBLE);
		signIcon.setImageResource(R.drawable.ic_tick);
		this.location = location;
	}

	private void send() {
		String number = "+421907351787";
		String text = getSMSText();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",
				number, null));
		intent.putExtra("sms_body", text);
		startActivity(intent);
	}

	private String getSMSText() {
		return "test";//"lat: " + location.getLatitude() + " long: " + location.getLongitude();
	}

}
