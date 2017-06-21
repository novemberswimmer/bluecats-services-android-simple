/*
 * Copyright (c) 2017 BlueCats. All rights reserved.
 * http://www.bluecats.com
 */

package com.bluecats.services;

import com.bluecats.sdk.BlueCatsSDK;
import com.bluecats.services.interfaces.BlueCatsSDKInterfaceService;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class MainApplication extends Application {
	protected static final String TAG = "MainApplication";
	
	public static final String BLUECATS_APP_TOKEN = "ef6a7c29-3702-4ff7-96f5-8eccc17e5e74";

	@Override
	public void onCreate() {
		super.onCreate();

		if (TextUtils.isEmpty(BLUECATS_APP_TOKEN)) {
			throw new RuntimeException("BLUECATS_APP_TOKEN is invalid in MainApplication.java.");
		}
	}

	public static void runSDK(Context context) {
		// start the BlueCatsSDKInterfaceService
		// this service will be responsible for handling SDK events while the app is closed
		Intent intent = new Intent(context, BlueCatsSDKInterfaceService.class);
		Bundle extras = new Bundle();
		extras.putString(BlueCatsSDK.EXTRA_APP_TOKEN, BLUECATS_APP_TOKEN);
		intent.putExtras(extras);
		context.startService(intent);
	}
}
