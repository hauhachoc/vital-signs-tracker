package com.vitalsigntracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ProviderMainLobby extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.providermainlobby);		
		
		/*setContentView(R.layout.tabactivity);
		
		TabHost host = getTabHost();
		
		host.addTab(host.newTabSpec("Table")
			.setIndicator("Table")
			.setContent(new Intent(this, DisplayTable.class).putExtra("TAB", "Table")));
		host.addTab(host.newTabSpec("Chart")
				.setIndicator("Chart")
				.setContent(new Intent(this, DisplayChart.class).putExtra("TAB", "Chart")));
		host.addTab(host.newTabSpec("Subscribe")
				.setIndicator("Subscribe")
				.setContent(new Intent(this, SubscribePatient.class).putExtra("TAB", "Subscribe")));
		host.addTab(host.newTabSpec("Sign Out")
				.setIndicator("Sign Out")
				.setContent(new Intent(this, ProviderLogOut.class).putExtra("TAB", "Sign Out")));*/		
	}	
	
	public void subscribePatientOnClick (View v) {
		Intent i = new Intent(this, SubscribePatient.class);
		startActivity(i);
	}
	
	public void providerInboxOnClick (View v) {
		Toast.makeText(this, "inbox on click", Toast.LENGTH_SHORT).show();
	}
	
	public void displayTableOnClick (View v) {
		Intent i = new Intent(this, DisplayTable.class);
		startActivity(i);
	}
	
	public void displayChartOnClick (View v) {
		Intent i = new Intent(this, DisplayChart.class);
		startActivity(i);		
	}
	
	public void signOutOnClick (View v) {
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
}
