package com.alphaking.ijumprope;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity {

	MediaPlayer ourSong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		//for the start of the app
		ourSong = MediaPlayer.create(Splash.this, R.raw.sound); 
		ourSong.start();
		
		//making a thread (timer for splash)
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(5000); //3 seconds (3000mili)
				} catch(InterruptedException e){
					e.printStackTrace(); //debugging <
				}finally{
					//startActivity method needs intent, so make it using the name in manifest.
					Intent openMainActivity = new Intent("android.intent.action.STARTINGPOINTSZ");
					startActivity(openMainActivity);
				}
			}
		};//end of thread
		timer.start();
	}
	
	//This will kill the splash screen b/c this splash goes on pause after it switches, therefore method is launched and finished.
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSong.release();
		finish();
	}
}