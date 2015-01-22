package com.alphaking.ijumprope;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.alphaking.ijumprope.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Start extends Activity implements SensorEventListener {

TextView jumps,calories;
ToggleButton tog;
Button finish;
Sensor accelerometer;
SensorManager sm;
int sensitivity,jumpCount,beep1;
SoundPool sp;
double weight;
boolean inAir,freeFall,rebound,up,paused;
Chronometer chronometer;
long timeWhenStopped = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		sm=(SensorManager)getSystemService(SENSOR_SERVICE);
		accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
		jumps=(TextView) findViewById(R.id.textView1);
		calories=(TextView) findViewById(R.id.textView6);
		tog=(ToggleButton) findViewById(R.id.toggleButton1);
		finish=(Button) findViewById(R.id.button1);
		finish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					File myFile = new File(Environment.getExternalStorageDirectory() + "/iJumpRopeLog.txt");
		            if(!myFile.exists()){
		            	myFile.createNewFile();
		            	}
		            
		            	 FileOutputStream fOut = new FileOutputStream(myFile,true);
		                 OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
		                 Time today = new Time(Time.getCurrentTimezone());
		                 today.setToNow();
		                 String date = today.month + "/" + today.monthDay + "/" + today.year;
		                 myOutWriter.append(" "+date +"    "+chronometer.getText()+"            "+jumpCount+"                  "+calories.getText()+"\n");
		                 myOutWriter.close();
		                 fOut.close();
		                 Toast.makeText(getBaseContext(),
		                         "Log has been saved.",
		                         Toast.LENGTH_SHORT).show();
		            
		            }catch (Exception e) {
		            	Toast.makeText(getBaseContext(), e.getMessage(),
		                Toast.LENGTH_SHORT).show();
		            }					
			finish();	
			}
		});

		sp=new SoundPool(5,AudioManager.STREAM_MUSIC, 0);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		beep1=sp.load(this,R.raw.tick, 1);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		chronometer.setBase(SystemClock.elapsedRealtime());
	    chronometer.setText("00:00:00");
	    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

	        @Override
	        public void onChronometerTick(Chronometer chronometer) {
	            CharSequence text = chronometer.getText();
	            if (text.length()  == 5) {
	                chronometer.setText("00:"+text);
	            } else if (text.length() == 7) {
	                chronometer.setText("0"+text);
	            }
	        }
	    });
		LoadPrefs();
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
if(tog.isChecked()){
	if(paused){
		chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
	}
    chronometer.start();			
			if(up){
							
			if(event.values[1]>(15-(sensitivity-50)/10)){
				inAir=true;
			}
			if(inAir&&event.values[1]<(5+(sensitivity-50)/10)){
				freeFall=true;
			}
			if(freeFall&&event.values[1]>(13-(sensitivity-50)/10)){
				rebound=true;
			}
			if(rebound&&event.values[1]<(10+(sensitivity-50)/10)&&event.values[1]>(1-(sensitivity-50)/10))
			{
				sp.play(beep1,1,1,0,0,1);
				jumpCount++;
				inAir=false;
				freeFall=false;
				rebound=false;
				double minutes= (Double.valueOf((String) chronometer.getText().subSequence(6, 8))/60 + Double.valueOf((String) chronometer.getText().subSequence(0, 2))*60 + Double.valueOf((String)chronometer.getText().subSequence(3, 5)));
				Double x = weight*0.074*minutes;
				calories.setText((String)String.format("%.1f", x));
			}	
			
			jumps.setText(Integer.toString(jumpCount));
			
			}
			//phone is set to be upside down
			else{
				if(event.values[1]<-(15-(sensitivity-50)/10)){
					inAir=true;
				}
				if(inAir&&event.values[1]<-(5+(sensitivity-50)/10)){
					freeFall=true;
				}
				if(freeFall&&event.values[1]<-(13-(sensitivity-50)/10)){
					rebound=true;
				}
				if(rebound&&event.values[1]>-(10+(sensitivity-50)/10)&&event.values[1]<-(1-(sensitivity-50)/10))
				{
					sp.play(beep1,1,1,0,0,1);
					jumpCount++;
					inAir=false;
					freeFall=false;
					rebound=false;
					double minutes= (Double.valueOf((String) chronometer.getText().subSequence(6, 8))/60 + Double.valueOf((String) chronometer.getText().subSequence(0, 2))*60 + Double.valueOf((String)chronometer.getText().subSequence(3, 5)));
					Double x = weight*0.074*minutes;
					calories.setText((String)String.format("%.1f", x));
				}
				jumps.setText(Integer.toString(jumpCount));
			}
			
			paused=false;
		}
	else{
		if(!paused){
		timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
		}
		chronometer.stop();
		paused=true;
	}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	private void LoadPrefs(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if(sp.getString("ORIENT", "UP")=="UP"){
			up=true;
		}
		if(sp.getString("ORIENT", "UP")=="DOWN"){
			up=false;
		}
		try{
		weight=Integer.parseInt((sp.getString("WEIGHT","0")));
		}catch(NumberFormatException e){
			calories.setText("Invalid Weight");
		}
		sensitivity=Integer.parseInt(sp.getString("TRACK", "50"));
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sm.unregisterListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sm.unregisterListener(this);
	}
	
}
