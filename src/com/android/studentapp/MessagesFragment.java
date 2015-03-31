package com.android.studentapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import com.android.studentapp.MainActivity;
import com.android.studentapp.R;
import com.android.database.DatabaseHandler;
import com.android.database.Messages;
import com.android.utils.MyGlobalVariables;
import com.android.utils.WifiUtils;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MessagesFragment extends Fragment {

	private LinearLayout lLayoutMessagesView;
	private EditText etMessages;
	private Button btnSend;
	private String TAG="MessagesFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		View rootView = inflater.inflate(R.layout.fragment_messages, container,false);
		lLayoutMessagesView = (LinearLayout) rootView.findViewById(R.id.lLayout_fragment_messages_view);
		etMessages = (EditText) rootView.findViewById(R.id.etMessages);
		btnSend = (Button) rootView.findViewById(R.id.btnSend);
		btnSend.setBackgroundResource(R.drawable.btn_green_selector);
		final String serverIp = WifiUtils.getServerIpFromArpCache();
		final int port = MyGlobalVariables.getClientPort();
		final String myMac = MyGlobalVariables.getMyMac();
		
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!etMessages.getText().toString().equalsIgnoreCase(""))
				{
					DatabaseHandler db = new DatabaseHandler(MainActivity.appContext);
					Messages messages = new Messages();
					messages.setMessage(etMessages.getText().toString());
					messages.setMacAddress(MyGlobalVariables.getMyMac());
					db.addMessage(messages);
					//sending message
					try {
						Socket socket = new Socket(serverIp, port); //after this request send to the server
						Log.i(TAG , "messages 0");
						
						DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
						DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
						String in = dataInputStream.readUTF(); //reading server response
						if(in.equalsIgnoreCase("connection_success"))
						{
							Log.i(TAG , "messages 1");
							dataOutputStream.writeUTF("Message" + " " + myMac + " " +etMessages.getText().toString()); //sending request code
						}
					    dataOutputStream.close();
					    dataInputStream.close();
					    socket.close();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//----------
				}
				etMessages.setText("");
			}
		});
		
		DatabaseHandler db = new DatabaseHandler(MainActivity.appContext);
		List<Messages> messageList = db.getAllMessages();
		for(Messages m: messageList)
		{
			TextView tv0 = new TextView(getActivity());
			LayoutParams params0 = new LayoutParams(
			        LayoutParams.WRAP_CONTENT,      
			        50
			);
			tv0.setLayoutParams(params0);
			lLayoutMessagesView.addView(tv0);
			
			TextView tv = new TextView(getActivity());
			LayoutParams params = new LayoutParams(
			        LayoutParams.MATCH_PARENT,      
			        LayoutParams.WRAP_CONTENT
			);
			tv.setLayoutParams(params);
			tv.setTextSize(18);
						
			if(m.getMacAddress().equalsIgnoreCase(MyGlobalVariables.getMyMac()))
			{
				tv.setGravity(Gravity.LEFT);
				tv.setTextColor(getResources().getColor(R.color.chat_my));
			}else
			{
				tv.setGravity(Gravity.RIGHT);
				tv.setTextColor(getResources().getColor(R.color.chat_other));
			}
			
			tv.setText(m.getMessage());
			lLayoutMessagesView.addView(tv);
			Log.i(TAG , "studentfeedback1 message view");
			
		}
		
		return rootView;
	}

	private void addview(LinearLayout lLayout) {
		// TODO Auto-generated method stub
		Button b =new Button(getActivity());
		LayoutParams params = new LayoutParams(
		        LayoutParams.MATCH_PARENT,      
		        120
		);
		params.setMargins(5, 10, 5, 0);
		b.setLayoutParams(params);
		b.setText("fff");
		lLayout.addView(b);
	}

	
}
