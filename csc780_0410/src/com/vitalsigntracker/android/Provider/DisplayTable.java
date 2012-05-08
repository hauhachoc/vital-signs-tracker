package com.vitalsigntracker.android.Provider;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import metadata.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitalsigntracker.android.ConnectionManager;
import com.vitalsigntracker.android.R;
import com.vitalsigntracker.android.R.id;
import com.vitalsigntracker.android.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DisplayTable extends Activity {

	private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;

	String[] names = null;
	String[] period = { "Select Period", "1 week", "2 week", "1 month",
			"3 month", "1 year" };
	int[] periodInInt = { 0, 7, 14, 30, 90, 365 };

	String patientNameSelected = null;
	int periodSelected = 0;

	private InputStream inpS;
	private OutputStream outS;

	Spinner patientNames, periodOption;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaytable);

		mySharedPreferences = this.getSharedPreferences("MY_PREFS",
				MODE_PRIVATE);

		String allPatientNames = mySharedPreferences.getString(
				"allPatientName", "");

		JSONObject obj = null;
		try {
			obj = new JSONObject(allPatientNames);
			int size = obj.getInt("length");
			names = new String[size];
			for (int i = 0; i < size; i++) {
				if (i == 0) {
					names[i] = "Select Patient";
				} else {
					names[i] = obj.getString(Integer.toString(i));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		patientNames = (Spinner) findViewById(R.id.patientNameSpinner);

		ArrayAdapter<String> namesAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, names);

		namesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		patientNames.setAdapter(namesAdapter);

		periodOption = (Spinner) findViewById(R.id.tablePeriodSpinner);

		ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, period);

		periodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		periodOption.setAdapter(periodAdapter);

		patientNames
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						patientNameSelected = names[position];
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		periodOption
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						periodSelected = periodInInt[position];
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
	}

	public void submitDisplayTableOnClick(View v) {

		/*
		 * Check if the form is filled out completely.
		 */
		if (patientNameSelected.equals("Select Patient") || periodSelected == 0) {

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Uncomplete Information");
			alertDialog
					.setMessage("Please enter patient's name and period of time");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {

						}
					});
			alertDialog.show();

			// Form is filled out completely. Ready to submit to server.

		} else {

			String str = null;
			try {
				JSONObject object = new JSONObject();
				object.put("code", Constants.PROVIDER_DISPLAY_TABLE);
				object.put("patientName", patientNameSelected);
				object.put("period", periodSelected);
				str = object.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}

			try {							
				SocketAddress socketAddress = new InetSocketAddress(
						Constants.IP_ADD, Constants.PORT);
				Socket sock = new Socket();
				sock.connect(socketAddress, 10 * 1000);
				inpS = sock.getInputStream();
				outS = sock.getOutputStream();

				Scanner in = new Scanner(inpS);
				PrintWriter out = new PrintWriter(outS, true);

				out.println(str);

				String response = in.nextLine();

				inpS.close();
				out.close();
				outS.close();
				sock.close();

				// put the "response" (json string) in SharedPreferences and
				// call the PresentTable class
				// to display the table.
				mySharedPreferences = this.getSharedPreferences(MY_PREFS,
						MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putString("displayTableJson", response);
				editor.putString("pName", patientNameSelected);
				editor.commit();

				Intent i = new Intent(this, PresentTable.class);
				startActivity(i);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
