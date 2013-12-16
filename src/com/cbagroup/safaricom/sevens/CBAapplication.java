
package com.cbagroup.safaricom.sevens;

import com.WazaBe.HoloEverywhere.app.Application;
import com.cbagroup.safaricom.sevens.data.CBAdbUtils;

public class CBAapplication extends Application {
	
	public static CBAdbUtils mDb;
	public static final String TAG = "Authoured by @akajaymo";
	 
	@Override
   public void onCreate() {
       super.onCreate();

       mDb = new CBAdbUtils(this);
       mDb.open();


   }
	@Override
   public void onTerminate() {
       mDb.close();

       super.onTerminate();
   }

}
