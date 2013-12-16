package com.cbagroup.safaricom.sevens.data;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cbagroup.safaricom.sevens.CBAapplication;

public class JsonExportTask {

	public static JSONObject JSONexport() {

		JSONObject json = new JSONObject();
		JSONArray valuesarray = new JSONArray();
		List<String[]> values = null;

		values = CBAapplication.mDb.selectAll();

		for (String[] entries : values) {
			JSONObject valuesJson = new JSONObject();
			try {
				valuesJson.put("registrations_id", entries[0]); 
				valuesJson.put("registrations_fname", entries[1]);
				valuesJson.put("registrations_lname", entries[2]);
				valuesJson.put("registrations_age_group", entries[3]); 
				valuesJson.put("registrations_email", entries[4]);
				valuesJson.put("registrations_gender", entries[5]);
				valuesJson.put("registrations_phone", entries[6]);
				valuesJson.put("registrations_address", entries[7]);
				valuesJson.put("registrations_state", entries[8]);
				valuesJson.put("registrations_accountnumber_state", entries[9]);
				valuesJson.put("registrations_accountnumber", entries[10]);

				valuesarray.put(valuesJson);
			} catch (JSONException e) {
				Log.e("JsonExportTask", "Error parsing data " + e.toString());
			}
		}

		try {

			json.put("PAYLOAD", valuesarray);

		} catch (JSONException e) {
			Log.e("JsonExportTask", "Error parsing data " + e.toString());
		}

		return json;

	}
}
