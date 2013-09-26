package com.example.smstracker;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class Tracker implements LocationListener {

	private static final String TAG = MainActivity.SUPER_TAG + "Tracker";
	private LocationNeeder parent;
	private Context context;
	private LocationManager locationManager;

	public Tracker(LocationNeeder parent, Context context) {
		this.parent = parent;
		this.context = context;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean acquireLocation() {
		if (locationManager != null) {
			if (!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Log.e(TAG, "no GPS");
				return false;
			}
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			Log.i(TAG, "update requested");
			return true;
		}
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i(TAG, "onLocationChanged");
		parent.locationChanged(location);
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	public void cancelUpdates() {
		Log.d(TAG, "cancelling updates");
		locationManager.removeUpdates(this);
	}

}
