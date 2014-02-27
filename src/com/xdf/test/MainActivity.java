package com.xdf.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button locationovely_test;
	private Button geocoder_test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		locationovely_test = (Button) findViewById(R.id.locationovely_test);
		locationovely_test.setOnClickListener(this);
		geocoder_test = (Button) findViewById(R.id.geocoder_test);
		geocoder_test.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.locationovely_test:
			startActivity(new Intent(MainActivity.this, LocationOvelyTest.class));
			break;
		case R.id.geocoder_test:
			startActivity(new Intent(MainActivity.this, GeoCoderTest.class));
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
