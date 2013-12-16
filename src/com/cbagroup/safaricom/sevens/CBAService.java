
package com.cbagroup.safaricom.sevens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.cbagroup.safaricom.sevens.data.JsonExportTask;
import com.cbagroup.safaricom.sevens.util.Pref;
import com.cbagroup.safaricom.sevens.util.Util;

public class CBAService extends Service{
	private TimerTask mDoTask;
	private Timer mT = new Timer();
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	int Qsearch, size;String res="";String data;
	private PowerManager.WakeLock wl;
	private static final String TAG = "CBA Group";
	int z;
	private static final int SYNC_OPERATION=0;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); 
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		Pref.loadSettings(CBAService.this);
		RegistrationService();		
	}
	
	@Override
	public void onDestroy(){
        super.onDestroy();
        if(wl.isHeld())
		   {
            wl.release(); 
       }
    }
	
	private void RegistrationService() {
		long delay =  100000;
		long period = 400000; 
		mDoTask = new TimerTask() {

			@Override
			public void run() {
				
				
				if(Util.isConnected(CBAService.this)){
					
					if(CBAapplication.mDb.chkDB()){
						wl.acquire();
						JSONObject json = JsonExportTask.JSONexport();
						data =json.toString();
						Log.v("JsonExport", data);
					    xHandler.sendMessage(Message.obtain(xHandler, SYNC_OPERATION));
					}
				}
				
			}

		};
		mT.scheduleAtFixedRate(mDoTask, delay, period);       
		 
	}
	
	static class xInnerHandler extends Handler{
	   	 
	   	 WeakReference<CBAService> mService;
	   	 xInnerHandler (CBAService aService) 
	   	 {
	   	 mService = new WeakReference<CBAService>(aService);
	   	 }
	   	 @Override
	     	public void handleMessage(Message message){
	   		CBAService xService = mService.get();
	     		switch (message.what){
	     		case SYNC_OPERATION:
	     			
	     			if(Util.isConnected(xService)){
	     				xService.new ExportJson ().execute();
	     			}
	     			
	     			break;
	     		}
	     	}
	    }
	   xInnerHandler xHandler = new xInnerHandler (this);
	   
	   
		public class ExportJson extends AsyncTask<Integer, Integer, Integer> {
			protected void onPreExecute() {
				Log.i("CBA Export Method Call", data);
			}

			@Override
			protected Integer doInBackground(Integer... arg0)throws NullPointerException {
				String URL =getResources().getString(R.string.url);
				try
				{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(URL);
					HttpParams params = new BasicHttpParams();
				    HttpConnectionParams.setConnectionTimeout(params, 50000);
				    HttpConnectionParams.setSoTimeout(params, 50000);
					//adding our data
					namevaluepairs = new ArrayList<NameValuePair>(2);
					namevaluepairs.add (new BasicNameValuePair("do","jsonexport"));
					namevaluepairs.add (new BasicNameValuePair("json",data));
					httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
					
					//Execute post request
					HttpResponse response = httpclient.execute(httpPost);
					HttpEntity entity = response.getEntity();
					inputstream=entity.getContent();
				}
				catch (ConnectTimeoutException e) {
					return null;
				} catch (SocketTimeoutException ste) {
					return null;
				} catch (NullPointerException nall) {
					return null;
				} catch (MalformedURLException e) {
					return null;
				} catch (IOException e) {
					return null;
				} catch (Exception e) {
					return null;
				}
				
					try
					{
						
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream,"iso-8859-1"),8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
		            inputstream.close();
		             res = sb.toString().trim();
		             
		             
				}
				
				catch(Exception e){
		            Log.e(TAG, "Error converting result "+e.toString());
		    }
				if (isCancelled())
				{
					return null;
				}
				
				return null;

			}

			@Override
			protected void onProgressUpdate(final Integer... progress) {

				if (ExportJson.this.isCancelled()) {
					if(wl.isHeld()){
		                wl.release(); 
		            }
					
				}
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
				if(wl.isHeld()){
	                wl.release(); 
	            }
				
			}

			@Override
			protected void onPostExecute(Integer result) {
				
				if(wl.isHeld()){
	                wl.release(); 
	            }
				
				if(res.contentEquals("1")){
					CBAapplication.mDb.deleteDB();
				}
				Log.v("postExecute", "completed");
				
			}

		} 
	   

}
