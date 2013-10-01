package com.example.smstracker;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationNeeder {

	private static final String TAG = MainActivity.SUPER_TAG
			+ "LocationService";
	private Tracker tracker;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		acquireLocation();
		return super.onStartCommand(intent, flags, startId);
	}

	private void acquireLocation() {
		tracker = new Tracker(this, this);
		tracker.acquireLocation();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		tracker.cancelUpdates();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void locationChanged(Location location) {
		Log.i(TAG, "Location acquired. LAT: " + location.getLatitude()
				+ " LONG: " + location.getLongitude());
		stopSelf();
	}

}
