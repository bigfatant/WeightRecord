package com.ssw.weightrecord;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

public class RecordActivity extends Activity {
	private TextView et_value_weight;
	// ÈÕÆÚ
	private TextView tv_datetime;
	private Calendar c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		init();
	}

	private void init() {
		et_value_weight = (TextView) findViewById(R.id.et_value_weight);
		tv_datetime = (TextView) findViewById(R.id.tv_datetime);

		c = Calendar.getInstance();
		tv_datetime.setText(c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH)
				+ "-" + c.get(Calendar.DATE) + "-  "
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND));
		tv_datetime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				OnDateSetListener callBack = new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						((TextView) v).setText(year + "-" + monthOfYear
								+ dayOfMonth);
					}

				};
				new DatePickerDialog(getApplicationContext(), callBack, c
						.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DATE)).show();
				// new TimePickerDialog(getApplicationContext(), null,
				// c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);

			}

		});
	}
}
