package com.vitalsigntracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProviderLogOut extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.providerlogout);
	}

	public void logOutOnClick(View v) {
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
}
