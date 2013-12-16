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

import android.content.ContentValues;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.WazaBe.HoloEverywhere.app.AlertDialog;
import com.WazaBe.HoloEverywhere.app.ProgressDialog;
import com.WazaBe.HoloEverywhere.sherlock.SActivity;
import com.WazaBe.HoloEverywhere.widget.EditText;
import com.WazaBe.HoloEverywhere.widget.Toast;
import com.cbagroup.safaricom.sevens.data.CBAdbUtils;
import com.cbagroup.safaricom.sevens.util.Util;
import com.google.android.gcm.GCMRegistrar;

public class CBAmain extends SActivity {
	
	public static  String SENDER_ID = "562954440161";
	EditText mFname,mLname,mEmail,mPhone,mAddress,mAccountnumber,mGender;
	Button msubmit;TextView maccountnumbertv;
	RadioGroup mradioaccount,mradiostate,mradioagegroup, mradioagegender;
	RadioButton mradioButton_state1,mradioButton_state2,mradioaccount_yes,mradioaccount_no, mradioButton_age_ffirst,
	mradioButton_age_first,mradioButton_age_second,mradioButton_age_third,mradioButton_male,mradioButton_female,mradioButton_age_fourth;
	
	private String mErrorMessage = "";
	private boolean mError = false;
	String sFname,sLname,sAge="18-24",sEmail,sPhone,sAddress="",sState="1",sAccountnumber="",sAccountnumberState="0",res="",sGender="male",regId;
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
		setContentView(R.layout.layout_cbamain);
		 getSupportActionBar().show();
		 getSupportActionBar().setTitle("Commercial Bank of Africa");
		 getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bottom_solid_example));
		 getSupportActionBar().setIcon(R.drawable.icon);
	     getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		
		GCMRegistrar.checkDevice(CBAmain.this);
		regId = GCMRegistrar.getRegistrationId(CBAmain.this);
		if (regId.equals("")) {
			GCMRegistrar.register(CBAmain.this, SENDER_ID);
		}
		 Intent startSMSServiceIntent = new Intent(CBAmain.this, CBAService.class);
		 CBAmain.this.startService(startSMSServiceIntent);
	     
		 mFname = (EditText)findViewById(R.id.fname);
		 mLname =(EditText)findViewById(R.id.lname);
		 mEmail =(EditText)findViewById(R.id.email);
		 mPhone = (EditText)findViewById(R.id.phone);
		 mAddress =(EditText)findViewById(R.id.address);
		 mAccountnumber = (EditText)findViewById(R.id.accountnumber);
		 
		 maccountnumbertv = (TextView)findViewById(R.id.accountnumbertv);
		 
		 mradioaccount = (RadioGroup)findViewById(R.id.radioaccount);
		 mradiostate = (RadioGroup)findViewById(R.id.radiostate);
		 mradioagegroup = (RadioGroup)findViewById(R.id.radioagegroup);
		 mradioagegender = (RadioGroup)findViewById(R.id.radioagegender); 
		 
		 mradioButton_state1 = (RadioButton)findViewById(R.id.radioButton_state1);
		 mradioButton_state2 = (RadioButton)findViewById(R.id.radioButton_state2);
		 mradioaccount_yes = (RadioButton)findViewById(R.id.radioaccount_yes);
		 mradioaccount_no = (RadioButton)findViewById(R.id.radioaccount_no);
		 
		 mradioButton_age_ffirst = (RadioButton)findViewById(R.id.radioButton_age_ffirst);
		 mradioButton_age_first = (RadioButton)findViewById(R.id.radioButton_age_first); 
		 mradioButton_age_second = (RadioButton)findViewById(R.id.radioButton_age_second);
		 mradioButton_age_third = (RadioButton)findViewById(R.id.radioButton_age_third);
		 mradioButton_age_fourth = (RadioButton)findViewById(R.id.radioButton_age_fourth);
		 
		 mradioButton_male = (RadioButton)findViewById(R.id.radioButton_male);
		 mradioButton_female = (RadioButton)findViewById(R.id.radioButton_female);
		 
		 
		 mradioaccount.setOnCheckedChangeListener(new OnCheckedChangeListener()
         {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId)
             {
                 switch(checkedId)
                 {
                 case R.id.radioaccount_yes:
                	 mAccountnumber.setVisibility(View.VISIBLE);
                	 maccountnumbertv.setVisibility(View.VISIBLE);
                	 sAccountnumberState="1";
                     break;
                 case R.id.radioaccount_no:
                	 mAccountnumber.setVisibility(View.GONE);
                	 maccountnumbertv.setVisibility(View.GONE);
                	 sAccountnumberState="0";
                     break;
                 
                 }
             }
         });
		 
		 mradioagegroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
         {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId)
             {
                 switch(checkedId)
                 {
                 case R.id.radioButton_age_ffirst: 
                	 sAge="Below 18";
                     break;
                 case R.id.radioButton_age_first: 
                	 sAge="18-24";
                     break;
                 case R.id.radioButton_age_second:
                	 sAge="25-34";
                     break;               
                  case R.id.radioButton_age_third:
                	  sAge="35-44";
                     break;
                  case R.id.radioButton_age_fourth:
                	  sAge="Above 45";
                     break;
                 
                 }
             }
         });
		 
		 mradioagegender.setOnCheckedChangeListener(new OnCheckedChangeListener()
         {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId)
             {
                 switch(checkedId)
                 {
                 case R.id.radioButton_male: 
                	 sGender="male";
                     break;
                 case R.id.radioButton_female:
                	 sGender="female";
                     break;
                     
                 
                 }
             }
         });
		 mradiostate.setOnCheckedChangeListener(new OnCheckedChangeListener()
         {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId)
             {
                 switch(checkedId)
                 {
                 case R.id.radioButton_state1:
                	 sState="1";
                	 break;
                 case R.id.radioButton_state2:
                	 sState="0";
                     break;
                 
                 }
             }
         });
		 
		 
		 msubmit = (Button)findViewById(R.id.submit);
		 
		 msubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(android.view.View v) {

						sFname=mFname.getText().toString();
						sLname=mLname.getText().toString();
						//age Radio Button
						//Gender Radio Button
						sEmail=mEmail.getText().toString();
						sPhone= mPhone.getText().toString();
						sAddress=mAddress.getText().toString();
						//State (Employed or Student Radio Button) (1,0)
						//Account Holder(1,0)
						sAccountnumber=mAccountnumber.getText().toString();
						
				    
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
	            	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	            	mError = false;
	            	
	            	
	            	if (TextUtils.isEmpty(mFname.getText())) {
	            		mFname.setError(getString(R.string.empty_fname));
	            		mErrorMessage = getString(R.string.empty_fname)+"\n";
	                    mError = true;
	                }
	                	                
	                if (TextUtils.isEmpty(mLname.getText())) {
	                	mLname.setError(getString(R.string.empty_lname));
	                	mErrorMessage += getString(R.string.empty_lname)+"\n";
	                    mError = true;
	                }
	                
	                if (TextUtils.isEmpty(mEmail.getText())) {
	                	mEmail.setError(getString(R.string.empty_email));
	                	mErrorMessage += getString(R.string.empty_email)+"\n";
	                    mError = true;
	                }
	                
	                if (TextUtils.isEmpty(mPhone.getText())) {
	                	mPhone.setError(getString(R.string.empty_phone));
	                	mErrorMessage += getString(R.string.empty_phone)+"\n";
	                    mError = true;
	                }
	                
	                if (!Util.validateEmail(sEmail)) {
	                	mEmail.setError(getString(R.string.invalid_email));
	                	mErrorMessage += getString(R.string.invalid_email)+"\n";
	                    mError = true;
	                    
	                }
	                if (!Util.validatePhoneNumber(sPhone)) {
	                	mPhone.setError(getString(R.string.invalid_phone_number));
	                	mErrorMessage += getString(R.string.invalid_phone_number)+"\n";
	                    mError = true;
	                    
	                }	
	                
	                if (!mError) {
	                	
	                if (Util.isConnected(CBAmain.this)) {
	                sPhone= Util.CorrectPhoneNumber(mPhone.getText().toString());
	                new submit().execute();
	                }
	                else 
	                {
	                sPhone= Util.CorrectPhoneNumber(mPhone.getText().toString());
	                new submitLocal().execute();	                	
	                 }
	                }
	                else{
	            		final Toast t = Toast.makeText(CBAmain.this, "Submitting Error!\n" + mErrorMessage,
	                            Toast.LENGTH_LONG);
	                    t.show();
	                    mErrorMessage = "";
	            	}
	                
	                	        	
				
			}
	     });
			
		 
		 
		 
      
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_cbamain, menu);
		return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_log_out:
            	Intent intent = new Intent(CBAmain.this, CBAlogin.class);
            	startActivity(intent);
                this.finish();
                return true;
                
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	
	public class submit extends AsyncTask<Void, Integer, Void> {
    	private ProgressDialog progressDialog = new ProgressDialog(CBAmain.this);
    	
    	protected void onPreExecute() {
			
        	 progressDialog.setMessage("Submitting Details......");
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
				namevaluepairs.add (new BasicNameValuePair("do","adddetails"));
				namevaluepairs.add (new BasicNameValuePair("fname",sFname));
				namevaluepairs.add (new BasicNameValuePair("lname",sLname)); 
				namevaluepairs.add (new BasicNameValuePair("age_group",sAge));
				namevaluepairs.add (new BasicNameValuePair("email",sEmail));
				namevaluepairs.add (new BasicNameValuePair("gender",sGender));
				namevaluepairs.add (new BasicNameValuePair("phone",sPhone));
				namevaluepairs.add (new BasicNameValuePair("address",sAddress));
				namevaluepairs.add (new BasicNameValuePair("state",sState));
				namevaluepairs.add (new BasicNameValuePair("accountnumberstate",sAccountnumberState));
				namevaluepairs.add (new BasicNameValuePair("accountnumber",sAccountnumber));
             	
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
			if(submit.this.isCancelled())
			{
				this.progressDialog.dismiss();
				Toast.makeText(CBAmain.this, R.string.no_internet, Toast.LENGTH_LONG).show();	
			}
		}
		@Override
	    protected void onCancelled(){
	        super.onCancelled();
	        this.progressDialog.dismiss();
	        Toast.makeText(CBAmain.this, R.string.no_internet, Toast.LENGTH_LONG).show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (res.equals("0")) {
				this.progressDialog.dismiss();
				new AlertDialog.Builder(CBAmain.this)
		        .setTitle(R.string.notification_already_registered)
		        .setMessage(Html.fromHtml("<font color=\"#ffffff\">Sorry it seems you have Registered your Details Before!</font>" ))
		        .setNeutralButton(R.string.ok,
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    
		                    }
		                }).show();
			}
			if(res.equals("1")){
				CBAmain.this.finish();
				Intent intent = new Intent(CBAmain.this, CBAmain.class);
				startActivity(intent);
				Toast.makeText(CBAmain.this, "Details Sucessfully Submitted", Toast.LENGTH_LONG).show();
				this.progressDialog.dismiss();	
			}
			
			
			
	        
		}
	}
	public class submitLocal extends AsyncTask<Void, Integer, Void> {
    	private ProgressDialog progressDialog = new ProgressDialog(CBAmain.this);
    	
    	protected void onPreExecute() {
			
        	 progressDialog.setMessage("Submitting Details Locally");
        	 progressDialog.setIndeterminate(true);
	         progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_dialog_icon_drawable_animation));
	         progressDialog.show();
    		         }
		@Override
		protected Void doInBackground(Void... arg0) {
			
		                ContentValues values = new ContentValues();
	                	values.put(CBAdbUtils.REGISTRATION_FNAME, sFname);
	                	values.put(CBAdbUtils.REGISTRATION_LNAME, sLname);
	                	values.put(CBAdbUtils.REGISTRATION_AGE_GROUP, sAge);
	                	values.put(CBAdbUtils.REGISTRATION_EMAIL, sEmail);
	                	values.put(CBAdbUtils.REGISTRATION_GENDER, sGender);
	                	values.put(CBAdbUtils.REGISTRATION_PHONE, sPhone);
	                	values.put(CBAdbUtils.REGISTRATION_ADDRESS,sAddress);
	                	values.put(CBAdbUtils.REGISTRATION_STATE, sState);
	                	values.put(CBAdbUtils.REGISTRATION_ACCOUNTNUMBER_STATE, sAccountnumberState);
	                	values.put(CBAdbUtils.REGISTRATION_ACCOUNTNUMBER, sAccountnumber);
	                	CBAapplication.mDb.insertDB(values);
	                	return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			CBAmain.this.finish();
			Intent intent = new Intent(CBAmain.this, CBAmain.class);
			startActivity(intent);
			this.progressDialog.dismiss();
			Toast.makeText(CBAmain.this,"Details sucessfully Added to Local Database",Toast.LENGTH_LONG).show();
					
					
		}
	
	}
}
