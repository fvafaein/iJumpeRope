package com.alphaking.ijumprope;

import com.alphaking.ijumprope.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Settings extends Activity implements SensorEventListener {
SeekBar seeker;
int sensitivity;
ToggleButton tog;
TextView test;
EditText weight;
String pounds;
RadioGroup orient;
Sensor accelerometer;
SensorManager sm;
int jumpCount, beep1;
SoundPool sp;
boolean inAir,freeFall, rebound;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);
		
		sm=(SensorManager)getSystemService(SENSOR_SERVICE);
		accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
		jumpCount=0;
		test=(TextView) findViewById(R.id.textView1);
		orient = (RadioGroup) findViewById(R.id.radioGroup1);
		seeker=(SeekBar) findViewById(R.id.seekBar1);
		weight=(EditText) findViewById(R.id.editText1);
		sp=new SoundPool(5,AudioManager.STREAM_MUSIC, 0);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		beep1=sp.load(this,R.raw.tick, 1);
		LoadPrefs();
		
		weight.addTextChangedListener(new TextWatcher(){

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			SavePrefs("WEIGHT",s.toString());
		}});
		
		//orientation check
		orient.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.radio0){
					SavePrefs("ORIENT","UP");
				}		
				
				if(checkedId==R.id.radio1){
					SavePrefs("ORIENT","DOWN");
				}	
			}});
		
		
		//for sens change
		seeker.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				sensitivity=progress;
				test.setText("Sensitivity is " + sensitivity);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				SavePrefs("TRACK",String.valueOf(sensitivity));
				test.setText("Settings");
				
			}
		});
		
		//toggle for running test
		tog=(ToggleButton) findViewById(R.id.toggleButton1);
		tog.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(tog.isChecked()){
				}
				else{
					jumpCount=0;
					test.setText("Settings");
										
				}
				
			}
		});
		
	
		
}
	
	
	private void LoadPrefs(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if(sp.getString("ORIENT", "UP")=="UP"){
			orient.check(R.id.radio0);
		}
		if(sp.getString("ORIENT", "UP")=="DOWN"){
			orient.check(R.id.radio1);
		}
		weight.setText(sp.getString("WEIGHT",null));
		seeker.setProgress(Integer.parseInt(sp.getString("TRACK", "50")));
		sensitivity=Integer.parseInt(sp.getString("TRACK", "50"));
		
	}
	
	private void SavePrefs(String key, String value){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();	
	}
	
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		
		//start the test if checked
		if(tog.isChecked()){
			
			if(orient.getCheckedRadioButtonId()==R.id.radio0){
							
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
			}	
			
			test.setText(Integer.toString(jumpCount));
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
				}
				test.setText(Integer.toString(jumpCount));
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		sm.unregisterListener(this);
		jumpCount=0;
		finish();
	}

	protected void onPause() {
		  super.onPause();
		  sm.unregisterListener(this);
		}
	protected void onResume() {
		  super.onResume();
		  sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
		}
	
	
	
}
