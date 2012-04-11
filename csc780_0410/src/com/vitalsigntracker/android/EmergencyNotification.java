package com.vitalsigntracker.android;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class EmergencyNotification extends MapActivity {

	private TextView textName, textAdd1, textAdd2, textMessage;	
	private MapView map_view;
	private MapController controller;
	private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergencynotification);			
		
		textName = (TextView) findViewById(R.id.textName);
		textAdd1 = (TextView) findViewById(R.id.textAdd1);
		textAdd2 = (TextView) findViewById(R.id.textAdd2);
		textMessage = (TextView) findViewById(R.id.textMessage);
		
		map_view = (MapView) findViewById(R.id.map_view);
		map_view.setBuiltInZoomControls(true);
		map_view.setFocusable(true);
		
		List<Overlay> mapOverlays = map_view.getOverlays();
		Drawable drawable = this.getResources().getDrawable(android.R.drawable.ic_menu_myplaces);
		MapViewOverlay itemOverlay = new MapViewOverlay(drawable, this);
		
		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);		
		float latitude = mySharedPreferences.getFloat("emergencylatitude", 0);
		float longitude = mySharedPreferences.getFloat("emergencylongitude", 0);
		String patientname = mySharedPreferences.getString("emergencypatientname", "");
		String message = mySharedPreferences.getString("emergencymessage", "Emergency Request");
		String streetName = mySharedPreferences.getString("emergencystreetname", "");
		String city = mySharedPreferences.getString("emergencycity", "");
		
		int latPoint = (int) (latitude * 1E6);
		int longPoint = (int) (longitude * 1E6);
		
		GeoPoint point = new GeoPoint(latPoint, longPoint);
		OverlayItem overlayitem = new OverlayItem(point, "My Address :", streetName);
		itemOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemOverlay);
		
		controller = map_view.getController();
		controller.setCenter(point);
		controller.setZoom(14);
		
		textName.setText("Name\t\t\t:" + patientname);
		textAdd1.setText("Add.\t\t\t:" + streetName);
		textAdd2.setText("City\t\t\t:" + city);
		textMessage.setText("Message\t\t:" + message);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
