package com.android.studentapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.android.studentapp.R;
import com.android.utils.MyGlobalVariables;
import com.android.utils.WifiUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LessonViewActivity extends ActionBarActivity{

	LinearLayout lLayout;
	TextView tvSubjectLabel,tvLessonLabel,tvSubject,tvLesson;
	String tag = "LessonViewActivity";
	String lessonName;
	ArrayList<String[]> tagsData;
	ArrayList<String[]> mcqQuestions;
	private String TAG = "LessonViewActivity";
	
	@TargetApi(Build.VERSION_CODES.FROYO)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_lesson_view);
		
		lLayout = (LinearLayout) findViewById(R.id.lLayoutLessonView);
		tvSubjectLabel = (TextView) findViewById(R.id.tvSubjectLabel);
		tvSubject = (TextView) findViewById(R.id.tvSubject);
		tvLessonLabel = (TextView) findViewById(R.id.tvLessonLabel);
		tvLesson = (TextView) findViewById(R.id.tvLesson);
		Intent intent = getIntent();

		try {
			lessonName = intent.getStringExtra("lessonName");
			String fileName = lessonName + ".xml";
//			Log.i(tag, fileName);
			File fXmlFile = new File(Environment.getExternalStorageDirectory()+ "/lessons/" + lessonName,fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			//Document xmlDocument = dBuilder.parse(new ByteArrayInputStream(fXmlFile.getBytes()));
			XPath xPath =  XPathFactory.newInstance().newXPath();
			doc.getDocumentElement().normalize(); 
			
			String expression = "/lesson_plan/lessonID";
			Node node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
			tagsData = new ArrayList<String[]>();
			if(null != node) {
			    NodeList nodeList = node.getChildNodes();
			    
			    for (int i = 0;null!=nodeList && i < nodeList.getLength(); i++) {
			    	
			        Node nod = nodeList.item(i);
			        
			        if(nod.getNodeType() == Node.ELEMENT_NODE){
			        	Element eElement = (Element) nod;
			        	String attribute = eElement.getAttribute("type");
			        	if(attribute.equalsIgnoreCase("mcq")){
				        	String element = nodeList.item(i).getNodeName();
			        		String[] singleTagData = {attribute,element};
			        		tagsData.add(singleTagData);
			        		NodeList mcqNodeList = nodeList.item(i).getChildNodes();
			        		mcqQuestions = new ArrayList<String[]>();
			        		for(int j=0;mcqNodeList!=null && j < mcqNodeList.getLength();j++){
			        			Node mcqNode = mcqNodeList.item(j);
			        			if(mcqNode.getNodeType() == Node.ELEMENT_NODE){
			        				Element mcqElement = (Element) mcqNode;
						        	String question= mcqElement.getAttribute("question");
						        	String ans1 = mcqElement.getAttribute("ans1");
						        	String ans2 = mcqElement.getAttribute("ans2");
						        	String ans3 = mcqElement.getAttribute("ans3");
						        	String ans4 = mcqElement.getAttribute("ans4");
						        	String correctAnswer = mcqElement.getAttribute("correctAnswer");
						        	String[] singleQuestion = {question,ans1,ans2,ans3,ans4,correctAnswer};
						        	mcqQuestions.add(singleQuestion);
						        	Log.i(tag, question + " " + ans1 + " " + ans2 + " " + ans3 + " " + ans4 + " " + correctAnswer);
			        			}
			        		}
			        	}else{
			        		if(!attribute.equalsIgnoreCase("feedback"))
			        		{
				        		String element = nodeList.item(i).getNodeName();
					        	Log.i(tag, element);
					        	String value = nod.getFirstChild().getNodeValue();
					        	String[] singleTagData = {attribute,element,value};
					        	tagsData.add(singleTagData);
			        		}
			        	}
			        }
			    }
			}
	 	}catch (SAXException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ParserConfigurationException e) {
	        e.printStackTrace();
	    } catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		
		
		
//		making UI---------------------------------------
		tvSubject.setText(Util.firstLetterCapitalize(tagsData.get(0)[2]));
		tvLesson.setText(Util.firstLetterCapitalize(tagsData.get(1)[2]));
		
		final String serverIp = WifiUtils.getServerIpFromArpCache();
		final int port = MyGlobalVariables.getClientPort();
		//final String clientMac = WifiUtils.getClientMacFromArpCache();
		
		//here mac can only pull if wifi is on
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		final String clientMac = info.getMacAddress();
		Log.i(TAG, "mymac " + clientMac);
		
		for(int i=2;i<tagsData.size();i++){
			if(tagsData.get(i)[0].equalsIgnoreCase("image")){
				Log.i(tag, tagsData.get(i)[2]);
				ImageView iv = new ImageView(this);
				final File file = new File(tagsData.get(i)[2]);
				iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				iv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
				Log.i(tag, file.getAbsolutePath());
				iv.setAdjustViewBounds(true);
				iv.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_VIEW);
						Uri data = Uri.fromFile(file);
						intent.setDataAndType(data, "image/*");
						startActivity(intent);
					}
				});
				lLayout.addView(iv);
				addHorizontalDivider(lLayout);
			}else{
				if(tagsData.get(i)[0].equalsIgnoreCase("video")){
					Log.i(tag, tagsData.get(i)[2]);
					Button button = new Button(this);
					LayoutParams params = new LayoutParams(
					        LayoutParams.MATCH_PARENT,      
					        LayoutParams.WRAP_CONTENT
					);
					params.setMargins(0, 10, 0, 10);
					button.setLayoutParams(params);
					button.setBackgroundResource(R.drawable.btn_green_selector);
					button.setText("Play now");
					button.setGravity(Gravity.CENTER);
					button.setTextAppearance(this, android.R.style.TextAppearance_Medium);
					button.setTextColor(getResources().getColor(R.color.white));
					final File file = new File(tagsData.get(i)[2]);
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(Intent.ACTION_VIEW);
							Uri data = Uri.fromFile(file);
							intent.setDataAndType(data, "video/*");
							startActivity(intent);
							
//							sending feed back
							try {
								Socket socket = new Socket(serverIp, port); //after this request send to the server
								Log.i(TAG , "lesson feed back video");
								
								DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
								DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
								String in = dataInputStream.readUTF(); //reading server response
								if(in.equalsIgnoreCase("connection_success"))
								{
									Log.i(TAG , "lesson feed back video 1");
									dataOutputStream.writeUTF("StudentFeedback " + lessonName + " video " + clientMac); //sending request code
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
//							----------
						}
					});
					lLayout.addView(button);
					addHorizontalDivider(lLayout);
				}else{
					if(tagsData.get(i)[0].equalsIgnoreCase("mcq")){
						Button button = new Button(this);
						LayoutParams params = new LayoutParams(
						        LayoutParams.MATCH_PARENT,      
						        LayoutParams.WRAP_CONTENT
						);
						params.setMargins(0, 10, 0, 10);
						button.setLayoutParams(params);
						button.setBackgroundResource(R.drawable.btn_green_selector);
						button.setText("Quiz");
						button.setGravity(Gravity.CENTER);
						button.setTextAppearance(this, android.R.style.TextAppearance_Medium);
						button.setTextColor(getResources().getColor(R.color.white));
						button.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent i = new Intent(getApplicationContext(), McqViewActivity.class);
								i.putExtra("mcq", mcqQuestions);
								startActivity(i);
								
//								sending feed back
								try {
									Socket socket = new Socket(serverIp, port); //after this request send to the server
									Log.i(TAG , "lesson feed back mcq");
									
									DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
									DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
									String in = dataInputStream.readUTF(); //reading server response
									if(in.equalsIgnoreCase("connection_success"))
									{
										Log.i(TAG , "lesson feed back video 1");
										dataOutputStream.writeUTF("StudentFeedback " + lessonName + " mcq " + clientMac); //sending request code
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
//								----------
							}
						});
						lLayout.addView(button);
						addHorizontalDivider(lLayout);
					}else{
						if(tagsData.get(i)[0].equalsIgnoreCase("text")){
							TextView tv = new TextView(this);
							tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							tv.setText(tagsData.get(i)[2]);
							tv.setTextSize(18);
							
							lLayout.addView(tv);
							addHorizontalDivider(lLayout);
						}else{
							if(tagsData.get(i)[0].equalsIgnoreCase("pdf")){
								Button button = new Button(this);
								LayoutParams params = new LayoutParams(
								        LayoutParams.MATCH_PARENT,      
								        LayoutParams.WRAP_CONTENT
								);
								params.setMargins(0, 10, 0, 10);
								button.setLayoutParams(params);
								button.setBackgroundResource(R.drawable.btn_green_selector);
								button.setText("Open document");
								button.setGravity(Gravity.CENTER);
								button.setTextAppearance(this, android.R.style.TextAppearance_Medium);
								button.setTextColor(getResources().getColor(R.color.white));
								final File file = new File(tagsData.get(i)[2]);
								button.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(Intent.ACTION_VIEW);
										Uri data = Uri.fromFile(file);
										intent.setDataAndType(data, "text/plain");
										startActivity(intent);
										
//										sending feed back
										try {
											Socket socket = new Socket(serverIp, port); //after this request send to the server
											Log.i(TAG , "lesson feed back pdf");
											
											DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
											DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
											String in = dataInputStream.readUTF(); //reading server response
											if(in.equalsIgnoreCase("connection_success"))
											{
												Log.i(TAG , "lesson feed back video 1");
												dataOutputStream.writeUTF("StudentFeedback " + lessonName + " pdf " + clientMac); //sending request code
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
//										----------
									}
								});
								lLayout.addView(button);
								addHorizontalDivider(lLayout);
							}
						}
					}
				}
			}
		}
	}
	@SuppressLint("NewApi")
	private void addHorizontalDivider(LinearLayout lLayout2) {
		// TODO Auto-generated method stub
		TextView tv1 = new TextView(this);
		tv1.setTextAppearance(this, android.R.style.TextAppearance_Small);
		tv1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10));
		lLayout2.addView(tv1);
		
		View v = new View(this);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		v.setBackgroundColor(getResources().getColor(R.color.gray_divider));
		lLayout2.addView(v);
		
		TextView tv2 = new TextView(this);
		tv2.setTextAppearance(this, android.R.style.TextAppearance_Small);
		tv2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10));
		lLayout2.addView(tv2);
	}
	
}