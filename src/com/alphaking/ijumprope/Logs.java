package com.alphaking.ijumprope;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.alphaking.ijumprope.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Logs extends Activity {
Button bClear;
TextView logz;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		logz = (TextView) findViewById(R.id.textView3);
		logz.setText(addText());
		bClear=(Button) findViewById(R.id.button1c);
		bClear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file = new File(Environment.getExternalStorageDirectory() + "/iJumpRopeLog.txt"); 
				boolean deleted = file.delete();
				logz.setText(addText());
			}});
	}
	
	private String addText() {
		String line;
		File file = new File(Environment.getExternalStorageDirectory() + "/iJumpRopeLog.txt");
	    if (!file.exists()){
	        line = "No logs found.";
	        return line;
	    }
	    else{
	    	line="";
	    	try {
	    		Scanner scanner = new Scanner(file);
	            while (scanner.hasNextLine()) {
	                line+=scanner.nextLine();
	                line+="\n";
	            }
	            scanner.close();
	            } catch (FileNotFoundException e) {
	        }
	    	
	    }
	    return line;
	}
	

	
}
