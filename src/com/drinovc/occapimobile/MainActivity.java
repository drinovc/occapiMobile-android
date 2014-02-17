package com.drinovc.occapimobile;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

	private int SPLASH_TIMEOUT = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        		intent.putExtra(LoginActivity.EXTRA_EMAIL, "occapi.adm@hotmail.com");
        		intent.putExtra(LoginActivity.EXTRA_PASS, "pass1");
        		startActivity(intent);
        		finish();
            }
        }, SPLASH_TIMEOUT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
