package com.example.smstracker;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
	public static final String AUTHORIZED_NUM = "+421944626924";
	private ProgressBar progressBar;
	private Tracker tracker;
	private Location location;
	private ImageView signIcon;
	private PhoneListener phoneListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViewFields();
		initFields();
		startListeningForCalls();
	}

	private void startListeningForCalls() {
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		manager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private void initFields() {
		tracker = new Tracker(this, this);
		phoneListener = new PhoneListener(this);
	}

	protected void onResume() {
		super.onResume();
		progressBar.setVisibility(View.INVISIBLE);
		signIcon.setImageResource(R.drawable.ic_warn);
		location = null;
//		tracker.acquireLocation();
	}

	protected void onPause() {
		super.onPause();
		tracker.cancelUpdates();
		if (isFinishing()) {
			Log.d(TAG, "finishing, stopping listener");
			stopListeningForCalls();
			stopLocationService();
		}
	}

	private void stopLocationService() {
		Intent intent = new Intent(this, LocationService.class);
		if (stopService(intent)) {
			Log.d(TAG, "location service stopped");
		}
	}

	private void stopListeningForCalls() {
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		manager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
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

	public void acquireCoords(View view) {
		progressBar.setVisibility(View.VISIBLE);
		tracker.acquireLocation();
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
		return "lat: " + location.getLatitude() + " long: " + location.getLongitude();
	}

}
