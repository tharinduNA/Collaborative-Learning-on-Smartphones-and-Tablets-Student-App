package com.android.studentapp;

import com.android.utils.WifiUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainFragment extends Fragment {
	LinearLayout lLayout;
	Button todayLesson;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_main, container,false);
		lLayout = (LinearLayout) rootView.findViewById(R.id.llayout_fragment_main);
		todayLesson = (Button) rootView.findViewById(R.id.btnTodayLesson);
		todayLesson.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String serverIp = WifiUtils.getServerIpFromArpCache();
				if(serverIp != null)
				{
					MyClientTask myClientTask = new MyClientTask(getActivity());
					myClientTask.execute();
				}else
				{
					Toast.makeText(getActivity(), "Connection failed..", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		return rootView;
	}


}
