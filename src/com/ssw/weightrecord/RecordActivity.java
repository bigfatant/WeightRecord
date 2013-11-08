package com.ssw.weightrecord;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class RecordActivity extends Activity {
	private TextView et_value_weight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		init();
	}

	private void init() {
		et_value_weight = (TextView) findViewById(R.id.et_value_weight);

	}

}
