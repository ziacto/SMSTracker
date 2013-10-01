package com.example.smstracker;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneListener extends PhoneStateListener {

	private static final String TAG = MainActivity.SUPER_TAG + "PhoneListener";
	private Context context;

	public PhoneListener(Context context) {
		this.context = context;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		if (state == TelephonyManager.CALL_STATE_RINGING) {
			if (incomingNumber.equals(MainActivity.AUTHORIZED_NUM)) {
				Log.i(TAG, "call from authorized number, starting service");
				Intent service = new Intent(context, LocationService.class);
				context.startService(service);
			} else {
				Log.i(TAG, "call from unauthorized number: " + incomingNumber);
			}
		}
	}

}
