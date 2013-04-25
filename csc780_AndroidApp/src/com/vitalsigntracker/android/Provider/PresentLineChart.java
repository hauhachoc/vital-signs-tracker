/* PresentLineChart displays the line chart of
 * patient's vital signs. 
 * The line chart uses the open source line chart
 * library. (Please check package under:
 * com.vitalsignstracker.android.Graph).
 */
package com.vitalsigntracker.android.Provider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.vitalsigntracker.android.R;
import com.vitalsigntracker.android.Graph.*;

public class PresentLineChart extends Activity {

	private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;
	private String pName;
	private String graphData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentlinechart);

		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
		pName = mySharedPreferences.getString("pName", "");
		graphData = mySharedPreferences.getString("displayGraphJson", "");
		initGraph();
	}

	/* Call the initGraph method to feed patient's systole and diastole
	 * data, then display the line graph. 
	 */
	public void initGraph() {
		JSONObject obj = null;
		int size = 0;
		try {
			obj = new JSONObject(graphData);
			size = obj.getInt("size");
		} catch (Exception e) {
			e.printStackTrace();
		}

		LineGraphView graphView = new LineGraphView(this, pName);

		JSONArray listDate = null;
		JSONArray listSystole = null;
		JSONArray listDiastole = null;
		try {
			listDate = obj.getJSONArray("listDate");
			listSystole = obj.getJSONArray("listSystole");
			listDiastole = obj.getJSONArray("listDiastole");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GraphView.GraphViewData[] arr1 = new GraphView.GraphViewData[size];
		GraphView.GraphViewData[] arr2 = new GraphView.GraphViewData[size];
		GraphView.GraphViewData g = null;
		GraphView.GraphViewData h = null;
		for (int i = 0; i < size; i++) {
			try {
				g = new GraphView.GraphViewData(Double.parseDouble(listDate
						.get(i).toString()), Double.parseDouble(listSystole
						.get(i).toString()));
				h = new GraphView.GraphViewData(Double.parseDouble(listDate
						.get(i).toString()), Double.parseDouble(listDiastole
						.get(i).toString()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			arr1[i] = g;
			arr2[i] = h;
		}
		graphView.addSeries(new GraphView.GraphViewSeries(arr1));
		graphView.addSeries(new GraphView.GraphViewSeries(arr2));
		setContentView(graphView);
	}

	/*
	 * overrides the back button to go to the correct class.
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(this, ProviderMainLobby.class);
		startActivity(i);
	}
}
