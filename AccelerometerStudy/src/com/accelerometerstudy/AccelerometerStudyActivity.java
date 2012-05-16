package com.accelerometerstudy;



import android.app.Activity;
import android.os.Bundle;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.accelerometerstudy.R;

public class AccelerometerStudyActivity extends Activity {

    /////////////////////////////////////////////////////
    // Control stuff
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(mStarter);
    }

    private final View.OnClickListener mStopper = new View.OnClickListener() {
        public void onClick(View v) {
            finishTest();
        }
    };

    private final View.OnClickListener mStarter = new View.OnClickListener() {
        public void onClick(View v) {
            Button button = (Button) v;
            button.setText(R.string.stop_test);
            button.setOnClickListener(mStopper);
            (new TestStarter()).execute(AccelerometerStudyActivity.this, null, null);
        }
    };

    /**
     * Writes out the file header and launches the test 
     */
    private class TestStarter extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... params) {
            try {
                PrintStream p = getFile(false);
                Account[] accounts = AccountManager.get(params[0]).getAccountsByType("com.google");
                p.println("Reported by: " + accounts[0].name +
                        " System: " + android.os.Build.MODEL +
                        " (" + android.os.Build.DEVICE + "/" + android.os.Build.PRODUCT + ")" +
                        ",,,,,,,,");
                p.println(",,,,,,,,");
                p.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            runTest();
        }
    }

    /////////////////////////////////////////////////////
    // Sensor stuff
    private long mStartedAt = 0;
    private SensorManager mManager = null;
    private final Listener mAccelListener = new Listener();
    private final Listener mRVListener = new Listener();
    private final ArrayList<Datum> mAccelCollector = new ArrayList<Datum>();
    private final Flipper mRVFlipper = new Flipper();

    private void runTest() {
        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mManager.registerListener(mAccelListener, accelSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void finishTest() {
        mManager.unregisterListener(mAccelListener);
        (new TestResultProcessor()).execute();
    }

    /**
     * This is the only part that needs to be efficient
     */
    private class Listener implements SensorEventListener {
        public void onSensorChanged(SensorEvent sensorEvent) {

            final int sensorType = sensorEvent.sensor.getType();
            switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelCollector.add(new Datum(sensorEvent));
                break;
            }
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }

    /**
     * Writes the saved-up data out in CSV format and sends it off with an Intent
     */
    private class TestResultProcessor extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                PrintStream p = getFile(true);
                p.println("Accelerometer,,,,, Angle Change,,,");
                p.println("t (msec),Accel x, Accel y, Accel z,, t (msec),Angle x, Angle y, Angle z");
                int accelSize = mAccelCollector.size();
                

                                
                p.close();
                sendOffData();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        private void sendOffData() {
            Intent i = new Intent(Intent.ACTION_SEND);
            String address = AccountManager.get(AccelerometerStudyActivity.this).getAccountsByType("com.google")[0].name;

            i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { address });
            i.putExtra(Intent.EXTRA_SUBJECT, "Sensplore: Test results");
            i.putExtra(Intent.EXTRA_TEXT, "(Attached as CSV)");
            i.putExtra(Intent.EXTRA_STREAM, fileURI());
            i.setType("text/plain");
            startActivity(Intent.createChooser(i, "Send mail"));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            finish();
        }
    }

    private class Datum {
        public final long mWhen;       // in nanoseconds since mStartedAt
        public final float[] mValues;

        // should be called AccelerometerDatum - values are x/y/z
        public Datum(SensorEvent event) {
            mWhen = deltaT(event.timestamp);
            mValues = event.values.clone();
        }

        // should be called AngularDatum - values are azimuth/pitch/roll or z/x/y
        public Datum(long when, float[] values) {
            mWhen = deltaT(when);
            final float x = values[1], y = values[2], z = values[0];
            mValues = values;
            mValues[0] = x; mValues[1] = y; mValues[2] = z;
        }

        private long deltaT(long incoming) {
            if (mStartedAt == 0)
                mStartedAt = incoming;
            return incoming - mStartedAt;
        }

        public String toString() {
            long microseconds = (mWhen + 500) / 1000;
            float milliseconds = microseconds / 1000.0f;
            StringBuilder s = new StringBuilder(String.format("%1$.2f", milliseconds));
            for (float value : mValues) {
                s.append(", ").append(String.format("%1$.2f", value));
            }
            return new String(s);
        }
    }

    /////////////////////////////////////////////////////
    /// File stuff
    private File mOutputFile = null;

    public static File filename() {
        File ext = Environment.getExternalStorageDirectory();
        ext = new File(ext, "sensplore");
        if (!ext.exists()) {
            ext.mkdir();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return new File(ext, "kinetics-" + df.format(new Date()) + ".csv");
    }

    public PrintStream getFile(boolean append) {
        PrintStream p = null;
        try {
            if (append) {
                p = new PrintStream(new FileOutputStream(mOutputFile, true));
            } else {
                mOutputFile = filename();
                // clean out all previous files
                for (File file : mOutputFile.getParentFile().listFiles()) {
                    file.delete();
                }

                p = new PrintStream(new FileOutputStream(mOutputFile));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    public Uri fileURI() {
        return Uri.fromFile(mOutputFile);
    }

    /**
     * There are a few places in sensor-land where you get the delta of two 9x9 float[] matrices. This pre-allocates two and 
     *  flips them back and forth between "next" and "last", so you don’t have to keep allocating new ones.
     */
    private class Flipper {
        private final float[] mR1 = new float[9], mR2 = new float[9];
        private final float[][] mR = { mR1,  mR2 };
        private int mLast = -1;

        public float[] last() {
            if (mLast == -1) {
                mLast = 0;
                return null;
            } else {
                return mR[mLast];
            }
        }

        public float[] next() {
            return mR[mLast ^ 1];
        }

        public void flip() {
            mLast ^= 1;
        }
    }
}
