package com.android.studentapp;

import java.util.ArrayList;

import com.android.studentapp.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class McqViewActivity extends ActionBarActivity{
	
	String tag;
	LinearLayout lLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mcq_view);
		Log.i(tag,"mcq_view");
		Intent intent = getIntent();
		lLayout = (LinearLayout) findViewById(R.id.lLayoutMcqView);
		String[] qNo = {"A","B","C","D"};
		@SuppressWarnings("unchecked")
		ArrayList<String[]> singleQuestion = (ArrayList<String[]>) intent.getSerializableExtra("mcq");
		
		//Generating UI dynamically
		for (int i = 0; i<singleQuestion.size(); i++){
			
			TextView question = new TextView(this);
			question.setText((i+1)+"). "+singleQuestion.get(i)[0]);
			question.setTextSize(16);
			Log.i(tag, singleQuestion.get(i)[0]);
			question.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			lLayout.addView(question);
			
			RadioButton rButton[] = new RadioButton[4];
			RadioGroup buttonGroup = new RadioGroup(this);
			buttonGroup.setOrientation(RadioGroup.VERTICAL);
			for(int j=0; j<4; j++){
				rButton[j] = new RadioButton(this);
				buttonGroup.addView(rButton[j]);
				rButton[j].setText(qNo[j] + "." + singleQuestion.get(i)[j+1]);
				rButton[j].setTextSize(16);
			}
			lLayout.addView(buttonGroup);
			addHorizontalDivider(lLayout);
		}
		Button btnSave = new Button(this);
		btnSave.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		btnSave.setBackgroundResource(R.drawable.btn_green_selector);
		btnSave.setText("SAVE");
		lLayout.addView(btnSave);
	}
	
	private void addHorizontalDivider(LinearLayout lLayout2) {
		// TODO Auto-generated method stub
		View v = new View(this);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		v.setBackgroundColor(Color.LTGRAY );
		lLayout2.addView(v);
	}
}


