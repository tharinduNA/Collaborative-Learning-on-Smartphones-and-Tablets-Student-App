package com.android.studentapp;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import com.android.utils.MyGlobalVariables;
import com.android.utils.WifiUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class MyClientTask extends AsyncTask<Void, Void, Void>{

	private String TAG = "MyClientTask";
	private int FILE_SIZE = 1024;
	private FileOutputStream fos = null;
	private BufferedOutputStream bos = null;
	ProgressDialog PD;
	private Context mContext;

	public MyClientTask (Context context){
        mContext = context;
   }
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
//		super.onPostExecute(result);
		PD.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		PD = new ProgressDialog(mContext);
		PD.setTitle("Please Wait..");
		PD.setMessage("Downloading...");
		PD.setCancelable(false);
		PD.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.i(TAG , "my client task ");
				
		String serverIp = WifiUtils.getServerIpFromArpCache();
		int port = MyGlobalVariables.getClientPort();
		int bytesRead;
		try {
			Socket socket = new Socket(serverIp, port); //after this request send to the server
			Log.i(TAG , "my client task ");
			
			int current = 0;
			
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			String in = dataInputStream.readUTF(); //reading server response
			Log.i(TAG , "my client task1 ");
			if(in.equalsIgnoreCase("connection_success"))
			{
				Log.i(TAG , "my client task2 ");
				dataOutputStream.writeUTF("TodayLessonRequest"); //sending request code
				String receivingFileName = dataInputStream.readUTF();
				Log.i(TAG , "my client task3 " + receivingFileName);
				byte [] mybytearray  = new byte [FILE_SIZE];
				InputStream is = socket.getInputStream();
				Log.i(TAG , "client file");
				
				//create file path and directories if parent directories are not existing
				File lessonDirectory = new File( Environment.getExternalStorageDirectory() + "/lessons/" + receivingFileName);
				lessonDirectory.mkdirs();
				
				fos = new FileOutputStream(lessonDirectory + "/" + receivingFileName + ".rar");
				bos = new BufferedOutputStream(fos);
				
				do {
					bytesRead = is.read(mybytearray, 0, (mybytearray.length));
					
					if(bytesRead >= 0) 
					{
						bos.write(mybytearray, 0 , bytesRead);
						current += bytesRead;
					}
				} while(bytesRead > -1);

				bos.flush();
				Log.i(TAG , "client file");
			}
		    dataOutputStream.close();
		    dataInputStream.close();
		    bos.close();
		    fos.close();
		    socket.close();
//		    progress.dismiss();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
