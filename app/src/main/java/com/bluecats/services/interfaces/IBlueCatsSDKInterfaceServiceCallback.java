/*
 * Copyright (c) 2017 BlueCats. All rights reserved.
 * http://www.bluecats.com
 */

package com.bluecats.services.interfaces;

import java.util.List;

import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BCSite;

/*
 * add any other callbacks your activities might be interested in here.
 */
public interface IBlueCatsSDKInterfaceServiceCallback {
	void didEnterSite(BCSite site);
	void didExitSite(BCSite site);
	void didEnterBeacons(List<BCBeacon> beacons);
	void didExitBeacons(List<BCBeacon> beacons);
	void didRangeBeacons(List<BCBeacon> beacons);
}