/*
 * Copyright (c) 2017 BlueCats. All rights reserved.
 * http://www.bluecats.com
 */

package com.bluecats.services;

import java.util.*;

import com.bluecats.sdk.*;
import com.bluecats.services.interfaces.BlueCatsSDKInterfaceService;
import com.bluecats.services.interfaces.IBlueCatsSDKInterfaceServiceCallback;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private TextView mTxtMessage;

	private ApplicationPermissions mPermissions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTxtMessage = (TextView)findViewById(R.id.txt_message);

		mPermissions = new ApplicationPermissions(MainActivity.this);
		mPermissions.verifyPermissions();

		MainApplication.runSDK(this.getApplicationContext());

		BlueCatsSDKInterfaceService.registerBlueCatsSDKServiceCallback(MainActivity.this.getClass().getName(), mBlueCatsSDKInterfaceServiceCallback);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// tell sdk to enter foreground scanning mode
		BlueCatsSDKInterfaceService.didEnterForeground();
	}
	
	@Override
	protected void onPause() {
		super.onPause();

		// tell sdk to enter background scanning mode
		BlueCatsSDKInterfaceService.didEnterBackground();
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (mPermissions != null) {
				mPermissions.verifyPermissions();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (mPermissions != null) {
			mPermissions.onRequestPermissionResult(requestCode, permissions, grantResults);
		}
	}
	
	private IBlueCatsSDKInterfaceServiceCallback mBlueCatsSDKInterfaceServiceCallback = new IBlueCatsSDKInterfaceServiceCallback() {
		@Override
		public void didEnterSite(final BCSite site) {
			Log.d(TAG, "onDidEnterSite");

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mTxtMessage.getText() != null) {
						mTxtMessage.setText(String.format("onDidEnterSite %s\n", site.getName()) + mTxtMessage.getText().toString());
					}
				}
			});
		}

		@Override
		public void didExitSite(final BCSite site) {
			Log.d(TAG, "onDidExitSite");

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mTxtMessage.getText() != null) {
						mTxtMessage.setText(String.format("onDidExitSite %s\n", site.getName()) + mTxtMessage.getText().toString());
					}
				}
			});
		}

		@Override
		public void didEnterBeacons(final List<BCBeacon> beacons) {
			Log.d(TAG, "didEnterBeacons");
		}

		@Override
		public void didExitBeacons(final List<BCBeacon> beacons) {
			Log.d(TAG, "didExitBeacons");

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					for (BCBeacon beacon: beacons) {
						if (mTxtMessage.getText() != null) {
							// assumes iBeacon. change properties for any format your beacon might be broadcasting, eg eddystone
							String beaconId = beacon.getProximityUUIDString();
							if (beacon.getMajor() != null) {
								beaconId += ":" + beacon.getMajor();
							}
							if (beacon.getMinor() != null) {
								beaconId += ":" + beacon.getMinor();
							}
							mTxtMessage.setText(String.format("didExitBeacon %s\n", beaconId) + mTxtMessage.getText().toString());
						}
					}
				}
			});
		}

		@Override
		public void didRangeBeacons(final List<BCBeacon> beacons) {
			Log.d(TAG, "didRangeBeacons");

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					for (BCBeacon beacon: beacons) {
						// assumes iBeacon. change properties for any format your beacon might be broadcasting, eg eddystone
						if (mTxtMessage.getText() != null) {
							String beaconId = beacon.getProximityUUIDString();
							if (beacon.getMajor() != null) {
								beaconId += ":" + beacon.getMajor();
							}
							if (beacon.getMinor() != null) {
								beaconId += ":" + beacon.getMinor();
							}
							mTxtMessage.setText(String.format("didRangeBeacon %s\n", beaconId) + mTxtMessage.getText().toString());
						}
					}
				}
			});
		}
	};
}
