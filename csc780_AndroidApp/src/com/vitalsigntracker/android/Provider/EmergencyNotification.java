/* EmergencyNotification displays the emergency request from
 * patient and also google map (location of the patient).
 * The top half displays the map, and the bottom half displays
 * the message (name, address, and emergency message).
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012) 
 */
package com.vitalsigntracker.android.Provider;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.vitalsigntracker.android.*;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class EmergencyNotification extends MapActivity {

	private TextView textName, textAdd1, textAdd2, textMessage;
	private TextView textNameField, textAdd1Field, textAdd2Field, textMessageField;
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
		textNameField = (TextView) findViewById(R.id.textNameField);
		textAdd1Field = (TextView) findViewById(R.id.textAdd1Field);
		textAdd2Field = (TextView) findViewById(R.id.textAdd2Field);
		textMessageField = (TextView) findViewById(R.id.textMessageField);

		map_view = (MapView) findViewById(R.id.map_view);
		map_view.setBuiltInZoomControls(true);
		map_view.setFocusable(true);

		List<Overlay> mapOverlays = map_view.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				android.R.drawable.ic_menu_myplaces);
		MapViewOverlay itemOverlay = new MapViewOverlay(drawable, this);

		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
		float latitude = mySharedPreferences.getFloat("emergencylatitude", 0);
		float longitude = mySharedPreferences.getFloat("emergencylongitude", 0);
		String patientname = mySharedPreferences.getString(
				"emergencypatientname", "");
		String message = mySharedPreferences.getString("emergencymessage",
				"Emergency Request");
		String streetName = mySharedPreferences.getString(
				"emergencystreetname", "");
		String city = mySharedPreferences.getString("emergencycity", "");

		int latPoint = (int) (latitude * 1E6);
		int longPoint = (int) (longitude * 1E6);

		//the top half displays the google map.
		//the latitude and longitude on google map.
		GeoPoint point = new GeoPoint(latPoint, longPoint);
		OverlayItem overlayitem = new OverlayItem(point, "My Address :",
				streetName);
		itemOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemOverlay);

		controller = map_view.getController();
		controller.setCenter(point);
		controller.setZoom(14);

		//the bottom half displays the info of the patient.
		textName.setText("Name");
		textNameField.setText(patientname);
		textAdd1.setText("Add.");
		textAdd1Field.setText(streetName);
		textAdd2.setText("City");
		textAdd2Field.setText(city);
		textMessage.setText("Message");
		textMessageField.setText(message);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
