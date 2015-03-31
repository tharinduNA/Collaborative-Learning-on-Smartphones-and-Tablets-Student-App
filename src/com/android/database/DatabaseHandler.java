package com.android.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "testDatabase";
 
    // ClientDetails table name
    private static final String TABLE_MESSAGES = "messages";
    // ClientDetails Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MAC_ADDRESS = "mac_address";
    
    // Feedback Table Columns names

    
    //messages table column names
    private static final String KEY_MASSAGE = "message";
//    private static final String KEY_DATE = "date";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES  + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MASSAGE + " TEXT," + KEY_MAC_ADDRESS + " TEXT" + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations for CLIENTDETAILS table
     */
 
    //adding message
    public void addMessage(Messages messages) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_MAC_ADDRESS, messages.getMacAddress()); // Contact Name
    	values.put(KEY_MASSAGE, messages.getMessage());
    	
    	//Inserting Row
    	db.insert(TABLE_MESSAGES, null, values);
    	db.close(); // Closing database connection
    }
    
    // Getting All messages
    public List<Messages> getAllMessages() {
    	List<Messages> messageList = new ArrayList<Messages>();
    	// Select All Query
    	String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES;
	
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);

    	// looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
    		do {
    			Messages messages = new Messages();
    			messages.setID(Integer.parseInt(cursor.getString(0)));
    			messages.setMessage(cursor.getString(1));
    			messages.setMacAddress(cursor.getString(2));
    			
    			// Adding contact to list
    			messageList.add(messages);
    		} while (cursor.moveToNext());
    	}
	
  		// return client detail list
    	return messageList;
    }
}
