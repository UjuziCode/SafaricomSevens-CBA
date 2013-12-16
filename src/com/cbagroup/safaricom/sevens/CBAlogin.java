package com.cbagroup.safaricom.sevens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.WazaBe.HoloEverywhere.app.AlertDialog;
import com.WazaBe.HoloEverywhere.app.ProgressDialog;
import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.WazaBe.HoloEverywhere.widget.EditText;
import com.WazaBe.HoloEverywhere.widget.TextView;
import com.WazaBe.HoloEverywhere.widget.Toast;
import com.cbagroup.safaricom.sevens.util.Pref;
import com.cbagroup.safaricom.sevens.util.Util;

public class CBAlogin extends SActivity {
   
	EditText mUsername,mPassword;
	Button mLogin;
	TextView mForgot;
	private String mErrorMessage = "";
	private boolean mError = false;
	String sUsername,sPassword,res="";
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	InputStream inputstream;
	SharedPreferences app_preferences;
	ArrayList<NameValuePair> namevaluepairs;
	private static final String TAG = "CBA Group";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setForceThemeApply(true);
		setContentView(R.layout.layout_cbalogin);
		getSupportActionBar().hide();
		
		mUsername = (EditText)findViewById(R.id.username);
        mPassword =(EditText)findViewById(R.id.password);
        
        mLogin = (Button)findViewById(R.id.login);
        
        mForgot =(TextView)findViewById(R.id.forgot);
        
        mLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				if(Util.isConnected(CBAlogin.this)){
				sUsername=mUsername.getText().toString();
			    sPassword=mPassword.getText().toString();
			    
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
            	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            	mError = false;
            	
            	
            	if (TextUtils.isEmpty(mUsername.getText())) {
            		mUsername.setError(getString(R.string.empty_username));
            		mErrorMessage = getString(R.string.empty_username)+"\n";
                    mError = true;
                }
                
                
                if (TextUtils.isEmpty(mPassword.getText())) {
                	mPassword.setError(getString(R.string.empty_password));
                	mErrorMessage += getString(R.string.empty_password)+"\n";
                    mError = true;
                }
                if (!mError) {
                	
                if (Util.isConnected(CBAlogin.this)) {
                	sUsername=mUsername.getText().toString();
    			    sPassword=mPassword.getText().toString();
                	new login().execute();
    			    
                }
                else {
                	Toast.makeText(CBAlogin.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                	
                     }
                }
                else{
            		final Toast t = Toast.makeText(CBAlogin.this, "Login Error!\n" + mErrorMessage,
                            Toast.LENGTH_LONG);
                    t.show();
                    mErrorMessage = "";
            	}
                
                	        	
			}
			
			else{
				Toast.makeText(CBAlogin.this, R.string.no_internet, Toast.LENGTH_LONG).show(); 
            }
		}
     });
		
		
	}
	
	public class login extends AsyncTask<Void, Integer, Void> {
    	private ProgressDialog progressDialog = new ProgressDialog(CBAlogin.this);
    	
    	protected void onPreExecute() {
			
        	 progressDialog.setMessage("Authenticating Credentials....");
        	 progressDialog.setIndeterminate(true);
	         progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_dialog_icon_drawable_animation));
	         progressDialog.show();
    		         }
		@Override
		protected Void doInBackground(Void... arg0) {
			String URL =getResources().getString(R.string.url);
			try
			{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(URL);
				HttpParams params = new BasicHttpParams();
			    HttpConnectionParams.setConnectionTimeout(params, 30000);
			    HttpConnectionParams.setSoTimeout(params, 30000);
				//adding our data
				namevaluepairs = new ArrayList<NameValuePair>(3);
				namevaluepairs.add (new BasicNameValuePair("do","mobilelogin"));
				namevaluepairs.add (new BasicNameValuePair("uname",sUsername));
				namevaluepairs.add (new BasicNameValuePair("pword",sPassword));
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
			if(login.this.isCancelled())
			{
				this.progressDialog.dismiss();
				Toast.makeText(CBAlogin.this, R.string.no_internet, Toast.LENGTH_LONG).show();	
			}
		}
		@Override
	    protected void onCancelled(){
	        super.onCancelled();
	        this.progressDialog.dismiss();
	        Toast.makeText(CBAlogin.this, R.string.no_internet, Toast.LENGTH_LONG).show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			 
	       
	        if(res.equals("0")){
	        	this.progressDialog.dismiss();
	        	new AlertDialog.Builder(CBAlogin.this)
		        .setTitle(R.string.notification_prompt_login)
		        .setMessage(Html.fromHtml("<font color=\"#ffffff\">Sorry the UserName and Password combination provided  are incorrect </font>" ))
		        .setNeutralButton(R.string.ok,
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    
		                    }
		                }).show();
	        	return;
	        }
	        if(res.equals("1")){
	        	Intent intent = new Intent(CBAlogin.this, CBAmain.class);
	        	startActivity(intent);
	        	Pref.loadSettings(CBAlogin.this);
	        	Pref.user =sUsername;
	        	Pref.saveSettings(CBAlogin.this);
	        	
	        	
	        	CBAlogin.this.finish();
	        	this.progressDialog.dismiss();
	        	return;
	        }
	        
	        else{
	        	this.progressDialog.dismiss();
	        	Log.i(TAG+"Login Error Responce", res);
	        	return;
	        }
	        
		}
	}

	

}
