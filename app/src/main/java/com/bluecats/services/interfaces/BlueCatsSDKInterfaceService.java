/*
 * Copyright (c) 2017 BlueCats. All rights reserved.
 * http://www.bluecats.com
 */

package com.bluecats.services.interfaces;

import java.util.*;
import java.util.Map.*;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.bluecats.sdk.*;

public class BlueCatsSDKInterfaceService extends Service {
	protected static final String TAG = "SDKInterfaceService";

	private static WeakHashMap<String, IBlueCatsSDKInterfaceServiceCallback> mBlueCatsSDKServiceCallbacks;
	private static WeakHashMap<String, IBlueCatsSDKInterfaceServiceCallback> getBlueCatsSDKServiceCallbacks() {
		if (mBlueCatsSDKServiceCallbacks == null) {
			mBlueCatsSDKServiceCallbacks = new WeakHashMap<String, IBlueCatsSDKInterfaceServiceCallback>();
		}
		synchronized(mBlueCatsSDKServiceCallbacks) {
			return mBlueCatsSDKServiceCallbacks;
		}
	}

	private static Context mServiceContext;
	private static Context getServiceContext() {
		synchronized(mServiceContext) {
			return mServiceContext;
		}
	}

	private BCBeaconManager mBeaconManager;

	@Override
	public void onCreate() {
		super.onCreate();

		mServiceContext = BlueCatsSDKInterfaceService.this;

		mBeaconManager = new BCBeaconManager();
		mBeaconManager.registerCallback(mBeaconManagerCallback);

		Log.d(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		Log.d(TAG, "onStartCommand");

		super.onStartCommand(intent, flags, startId);

		String appToken = "";
		if (intent != null && intent.getStringExtra(BlueCatsSDK.EXTRA_APP_TOKEN) != null) {
			appToken = intent.getStringExtra(BlueCatsSDK.EXTRA_APP_TOKEN);
		}

		// add any options here
		Map<String, String> options = new HashMap<String, String>();
		//add any of your own options

		BlueCatsSDK.setOptions(options);
		BlueCatsSDK.startPurringWithAppToken(getApplicationContext(), appToken);
		Log.d(TAG, "startPurringWithAppToken " + appToken);

		// start the service and keep running even if activity is destroyed

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");

		super.onDestroy();
	}

	/**
	 * Register a callback for your activity to receive updates from any SDK events you have defined
	 */
	public static void registerBlueCatsSDKServiceCallback(String className, IBlueCatsSDKInterfaceServiceCallback callback) {
		Log.d(TAG, "registerBlueCatsSDKServiceCallback");

		getBlueCatsSDKServiceCallbacks().put(className, callback);
	}

	/**
	 * Unregister your activity's callback when the activity is closed or destroyed
	 */
	public static void unregisterBlueCatsSDKServiceCallback(String className) {
		Log.d(TAG, "unregisterBlueCatsSDKServiceCallback");

		getBlueCatsSDKServiceCallbacks().remove(className);
	}

	/**
	 * Let the SDK know when the app has entered the foreground to increase Beacon scanning rate
	 */
	public static void didEnterForeground() {
		Log.d(TAG, "didEnterForeground");

		BlueCatsSDK.didEnterForeground();
	}

	/**
	 * Let the SDK know when the app has entered the foreground to decrease Beacon scanning rate
	 */
	public static void didEnterBackground() {
		Log.d(TAG, "didEnterBackground");

		BlueCatsSDK.didEnterBackground();
	}

	private BCBeaconManagerCallback mBeaconManagerCallback = new BCBeaconManagerCallback() {
		@Override
		public void didEnterSite(BCSite site) {
			Log.d(TAG, "didEnterSite");

			Iterator<Entry<String, IBlueCatsSDKInterfaceServiceCallback>> iterator = getBlueCatsSDKServiceCallbacks().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, IBlueCatsSDKInterfaceServiceCallback> entry = iterator.next();

				IBlueCatsSDKInterfaceServiceCallback callback = entry.getValue();
				if (callback != null) {
					callback.didEnterSite(site);
				}
			}

			super.didEnterSite(site);
		}

		@Override
		public void didExitSite(BCSite site) {
			Log.d(TAG, "didExitSite");

			Iterator<Entry<String, IBlueCatsSDKInterfaceServiceCallback>> iterator = getBlueCatsSDKServiceCallbacks().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, IBlueCatsSDKInterfaceServiceCallback> entry = iterator.next();

				IBlueCatsSDKInterfaceServiceCallback callback = entry.getValue();
				if (callback != null) {
					callback.didExitSite(site);
				}
			}

			super.didExitSite(site);
		}

		@Override
		public void didEnterBeacons(List<BCBeacon> beacons) {
			Log.d(TAG, "didEnterBeacons");

			Iterator<Entry<String, IBlueCatsSDKInterfaceServiceCallback>> iterator = getBlueCatsSDKServiceCallbacks().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, IBlueCatsSDKInterfaceServiceCallback> entry = iterator.next();

				IBlueCatsSDKInterfaceServiceCallback callback = entry.getValue();
				if (callback != null) {
					callback.didEnterBeacons(beacons);
				}
			}

			super.didEnterBeacons(beacons);
		}

		@Override
		public void didExitBeacons(List<BCBeacon> beacons) {
			Log.d(TAG, "didExitBeacons");

			Iterator<Entry<String, IBlueCatsSDKInterfaceServiceCallback>> iterator = getBlueCatsSDKServiceCallbacks().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, IBlueCatsSDKInterfaceServiceCallback> entry = iterator.next();

				IBlueCatsSDKInterfaceServiceCallback callback = entry.getValue();
				if (callback != null) {
					callback.didExitBeacons(beacons);
				}
			}

			super.didExitBeacons(beacons);
		}

		@Override
		public void didRangeBeacons(List<BCBeacon> beacons) {
			Log.d(TAG, "didRangeBeacons");

			Iterator<Entry<String, IBlueCatsSDKInterfaceServiceCallback>> iterator = getBlueCatsSDKServiceCallbacks().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, IBlueCatsSDKInterfaceServiceCallback> entry = iterator.next();

				IBlueCatsSDKInterfaceServiceCallback callback = entry.getValue();
				if (callback != null) {
					callback.didRangeBeacons(beacons);
				}
			}

			super.didRangeBeacons(beacons);
		}
	};

	public class LocalBinder extends Binder {
		public BlueCatsSDKInterfaceService getService() {
			return BlueCatsSDKInterfaceService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBlueCatsSDKServiceBinder;
	}

	private final IBinder mBlueCatsSDKServiceBinder = new LocalBinder();
}
