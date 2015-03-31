package com.android.studentapp;

import java.util.ArrayList;

import com.android.studentapp.LessonsFragment;
import com.android.utils.MyGlobalVariables;
import com.android.navdrawer.NavDrawerItem;
import com.android.navdrawer.NavDrawerListAdapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "MainActivity";
	private String[] mDrawerTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private MainFragment mainFragment;
	public static Context appContext;
	private TypedArray mDrawerIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		appContext = getApplicationContext();
		
		mainFragment = new MainFragment();
		Log.i(TAG, "ss");
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, mainFragment).commit();
		Log.i(TAG, "ss");

		mDrawerTitles = getResources().getStringArray(R.array.drawer_list_array);
		mDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mListView = (ListView) findViewById(R.id.drawer_list);
		
		
		//here mac can only pull if wifi is on
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		String myMac = info.getMacAddress();
		MyGlobalVariables.setMyMac(myMac);
		Log.i(TAG, "mymac " + myMac);
		
		//initializing navigation drawer
 
        navDrawerItems = new ArrayList<NavDrawerItem>();
 
        // adding nav drawer items to array
        
        // Home
        navDrawerItems.add(new NavDrawerItem(mDrawerTitles[0], mDrawerIcons.getResourceId(0, -1)));
        // lesson
        navDrawerItems.add(new NavDrawerItem(mDrawerTitles[1], mDrawerIcons.getResourceId(1, -1)));
        //messages
        navDrawerItems.add(new NavDrawerItem(mDrawerTitles[2], mDrawerIcons.getResourceId(2, -1)));
         
 
        // Recycle the typed array
        mDrawerIcons.recycle();
 
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				displayView(position);
			}
		});
		Log.i(TAG, "ss");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void displayView(int position){
		Fragment fragment = null;
		switch(position){
		case 0:
			fragment = new MainFragment();
			break;
		case 1:
			fragment = new LessonsFragment();	
			break;
		case 2:
			fragment = new MessagesFragment();
			break;
		}
		if(fragment != null){
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			setTitle(mDrawerTitles[position]);
			mDrawerLayout.closeDrawer(mListView);
			
		}
	}

}
