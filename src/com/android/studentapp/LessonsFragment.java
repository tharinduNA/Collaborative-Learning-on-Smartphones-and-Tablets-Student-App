package com.android.studentapp;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import com.android.studentapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class LessonsFragment extends Fragment {

	LinearLayout lLayout;
	View rootView;
	ArrayList<String> lessonList;
	private String TAG = "fragment";
	Socket socket;

	private void addLessons() {
		// TODO Auto-generated method stub
		//get file names and removing extentions from file names
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		lessonList = new ArrayList<String>();
		lessonList = getLessonList();
		Log.i(TAG , "lesson fragment");
		for(final String s:lessonList){
			Button bLesson = new Button(getActivity());
			LayoutParams params = new LayoutParams(
			        LayoutParams.MATCH_PARENT,      
			        120
			);
			params.setMargins(5, 10, 5, 0);
			bLesson.setLayoutParams(params);
			bLesson.setBackgroundResource(R.drawable.btn_green_selector);
			bLesson.setText(s);
			bLesson.setGravity(Gravity.CENTER);
			bLesson.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
			bLesson.setTextColor(getResources().getColor(R.color.white));
			bLesson.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), LessonViewActivity.class);
					intent.putExtra("lessonName", s);
					startActivity(intent);
				}
			});
			lLayout.addView(bLesson);
		}
	}


	private ArrayList<String> getLessonList() {
		// TODO Auto-generated method stub
		ArrayList<String> lessonList;
		File file = new File(Environment.getExternalStorageDirectory() + "/lessons");
		String[] xmlListWithExtensions = file.list();
		lessonList = new ArrayList<String>();
		String[] xmlTemp;
		for(String s:xmlListWithExtensions){
			xmlTemp = s.split("\\.");
			lessonList.add(xmlTemp[0]);
		}
		Collections.sort(lessonList);
		return lessonList;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_lessons, container,false);
		lLayout = (LinearLayout) rootView.findViewById(R.id.lLayout_fragment_lesson);
		addLessons();
		return rootView;
	}


}
