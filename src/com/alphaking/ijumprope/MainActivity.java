package com.alphaking.ijumprope;

import com.alphaking.ijumprope.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity{

	Button setting,start,logs,about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
						
		setting=(Button) findViewById(R.id.button2);
		setting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					Class ourClass = Class.forName("com.alphaking.ijumprope.Settings");
					Intent ourIntent = new Intent(MainActivity.this, ourClass);
					startActivity(ourIntent);
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}
			}
		});
		
		start=(Button) findViewById(R.id.button1);
		start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					Class ourClass = Class.forName("com.alphaking.ijumprope.Start");
					Intent ourIntent = new Intent(MainActivity.this, ourClass);
					startActivity(ourIntent);
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}
			}
		});
		
		logs=(Button) findViewById(R.id.button3);
		logs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					Class ourClass = Class.forName("com.alphaking.ijumprope.Logs");
					Intent ourIntent = new Intent(MainActivity.this, ourClass);
					startActivity(ourIntent);
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}
			}
		});
		
		about=(Button) findViewById(R.id.button4);
		about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					Class ourClass = Class.forName("com.alphaking.ijumprope.About");
					Intent ourIntent = new Intent(MainActivity.this, ourClass);
					startActivity(ourIntent);
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}
			}
		});
		
		
	}
	public void onBackPressed() {
	}


}
