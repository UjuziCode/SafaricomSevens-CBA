package com.cbagroup.safaricom.sevens;

import static com.cbagroup.safaricom.sevens.CBAmain.SENDER_ID;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cbagroup.safaricom.sevens.util.Pref;
import com.google.android.gcm.GCMBaseIntentService;
public class GCMIntentService extends GCMBaseIntentService {
	
	private static final String TAG = "CBA Group GCMIntentService";
    InputStream inputstream;
	ArrayList<NameValuePair> namevaluepairs;
	String res;
	
	public GCMIntentService() {
        super(SENDER_ID);
    
	}
	
	 @Override
	    protected void onRegistered(Context context, String registrationId) {
	    	 String  username= Pref.user;
	    	 new RegisterGCM().execute(registrationId,username);
	    	 String msg ="Welcome to CBA Group";
	    	 generateNotification(context, msg);
	    }

	    
	    @Override
	    protected void onUnregistered(Context context, String registrationId) {
	    	
	    }
	    
	    @Override
	    protected void onMessage(Context context, Intent intent) {
	    	
	    	
	    }
	    
	    @Override
	    protected void onDeletedMessages(Context context, int total) {
	        
	    }

	    
	    @Override
	    public void onError(Context context, String errorId) {
	    	 Log.i(TAG, "Received error: " + errorId);
	    }

	    @Override
	    protected boolean onRecoverableError(Context context, String errorId) {
	    	Log.i(TAG, "Received recoverable error: " + errorId);
	    	 return super.onRecoverableError(context, errorId);
	       
	    }

	    /**
	     * Issues a welcome notification to inform the user of CBA.
	     */
	    private  void generateNotification(Context context, String message ) {

	        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.stud);
	        int smalIcon =R.drawable.stud;
	        long when = System.currentTimeMillis();
	        String title="CBA Group";
	        String ticker ="Commercial Bank of Africa";
	        
	        
	        Intent intent =new Intent(context, CBAlogin.class);  
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
			NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)	
					.setContentTitle(title)
					.setContentText(message)
					.setTicker(ticker)
					.setWhen(when)
					.setSmallIcon(smalIcon)
					.setLargeIcon(icon)	
					.setAutoCancel(true)
					.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)
					.setContentIntent(pendingIntent);
			
			Notification notification=notificationBuilder.build();
			notificationManager.notify((int) when, notification);
			
	       
	        

	    }
	    
	    public class RegisterGCM extends AsyncTask<String, Integer, Void> {
	 	   @Override
	 		protected Void doInBackground(String ... params) {
	 		   String URL =getResources().getString(R.string.url);
	 			try{
	 			HttpClient httpclient = new DefaultHttpClient();
	 			HttpPost httpPost = new HttpPost(URL);
	 			namevaluepairs = new ArrayList<NameValuePair>();
	 			namevaluepairs.add (new BasicNameValuePair("do","gcm"));
	 			namevaluepairs.add (new BasicNameValuePair("gcmid",params[0]));
	 			namevaluepairs.add (new BasicNameValuePair("username",params[1]));
	 			httpPost.setEntity (new UrlEncodedFormEntity(namevaluepairs));
	 			HttpResponse response = httpclient.execute(httpPost);
	 			HttpEntity entity = response.getEntity();
	 			inputstream=entity.getContent();
	 		       }
	 	        catch(Exception e){
	 	            Log.e(TAG, "Error kwa http connection "+e.toString());
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
	 			
	 			return null;
	 		}
	 		
	 		@Override
	 		protected void onPostExecute(final Void result) {
	 			
	 		}

	 		
	 		
	 	}

	    

}
