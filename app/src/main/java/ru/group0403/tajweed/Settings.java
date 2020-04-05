package ru.group0403.tajweed.quran;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import ru.group0403.tajweed.R;


public class Settings extends Activity {


	TextView appearanceTextView;
	ScrollView outerScrollView;

	// TextView arabicFont;
	TextView arabicFontSize;
	CheckBox keepScreenOn;
	TextView KeepScreenOnTextView;
	int prevSetting;


	public void myClickListener(View view) {
		switch (view.getId()) {


		case R.id.settings_arabic_font_size:
			MyDialogBuilder.buildArbicFontSizeDialog(this);
			break;


		case R.id.settings_keep_screen_on:
			MyPreferenceHandler.setKeepScreenOn(getApplicationContext(),
					((CheckBox) view).isChecked());
			break;

		case R.id.settings_keep_screen_on_textView:
			keepScreenOn.toggle();
			MyPreferenceHandler.setKeepScreenOn(getApplicationContext(),
					keepScreenOn.isChecked());

			break;

		}
	}

	private void initialize() {


		// arabicFont = (TextView) findViewById(R.id.settings_arabic_fo);
		arabicFontSize = (TextView) findViewById(R.id.settings_arabic_font_size);
		keepScreenOn = (CheckBox) findViewById(R.id.settings_keep_screen_on);
		appearanceTextView = (TextView) findViewById(R.id.settings_appearance_text_view);
		outerScrollView = (ScrollView) findViewById(R.id.settings_outer_layout);
		KeepScreenOnTextView = (TextView) findViewById(R.id.settings_keep_screen_on_textView);


		this.setTitle("Settings");
		keepScreenOn.setChecked(MyPreferenceHandler
				.isKeepScreenOn(getApplicationContext()));



	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);
		prevSetting = -1;
		initialize();
	}

}
