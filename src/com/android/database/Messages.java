package com.android.database;

public class Messages {
    
   //private variables
   int id;
   String mac_address;
   String message;
//   Date date;
    
   // Empty constructor
   public Messages(){
        
   }
   // constructor
   public Messages(int id, String macAddress, String message){
       this.id = id;
       this.mac_address = macAddress;
       this.message = message;
//       this.date = date;
   }
    
   // getting ID
   public int getID(){
       return this.id;
   }
    
   // setting id
   public void setID(int id){
       this.id = id;
   }
    
   // getting name
   public String getMacAddress(){
       return this.mac_address;
   }
    
   // setting name
   public void setMacAddress(String message){
       this.mac_address = message;
   }
   
   //getting message
   public String getMessage(){
       return this.message;
   }
    
   // setting message
   public void setMessage(String message){
       this.message = message;
   }
   
 //getting time
//   public Date getDate(){
//       return this.date;
//   }
//    
//   // setting time
//   public void setDate(String date){
//       this.message = date;
//   }
}
