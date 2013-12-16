package com.cbagroup.safaricom.sevens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Broadcastreceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent startSMSServiceIntent = new Intent(context, CBAService.class);
		context.startService(startSMSServiceIntent);
		
	}
}
