package com.example.jiehe.atracker;

/**
 * Created by jiehe on 18/03/2018.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * SEXIEST CLASS OF ALL....
 *  YOU ASK WHAT TO GET THIS WILL GET THAT FROM DATABASE FOR YOU
 */
public class FileManager extends SQLiteOpenHelper {

  //VERSION OF THE DATABASE... DUNNO IF WE NEED TO USE IT
  private static final int DATABASE_VERSION = 1;

  //MAIN DATABASE FILE
  private static final String DATABASE_NAME = "ATracker.db";

  //TABLE FOR THE RECORDS
  private static final String TABLE_RECORDS = "RECORD_TABLE";
  private static final String TABLE_RECORDS_COLUMN_ID = "ID";
  //the id that links to the activity in activity table
  private static final String TABLE_RECORDS_COLUMN_ACTIVITY_ID = "ACTIVITY_ID";
  //the integer that stores the start time in milliseconds
  private static final String TABLE_RECORDS_COLUMN_START_TIME = "START_TIME";
  //the integer that stores the end time in milliseconds
  private static final String TABLE_RECORDS_COLUMN_END_TIME = "END_TIME";

  //TABLE FOR THE ACTIVITY (THE NAMES)
  private static final String TABLE_ACTIVITY = "ACTIVITY_TABLE";
  private static final String TABLE_ACTIVITY_COLUMN_ID = "ACTIVITY_ID";
  private static final String TABLE_ACTIVITY_COLUMN_NAME = "ACTIVITY_NAME";
  //the integer that stores the length of a activity
  private static final String TABLE_ACITVITY_COLUMN_GOAL_DAILY = "GOAL_DAILY";
  //the integer that indicate whether or not to pass the goal. e.g "0 = not to reach goal" "1 = need to reach goal"
  private static final String TABLE_ACTIVITY_COLUMN_GOAL_MODE = "GOAL_MODE";

  public FileManager(Context context, SQLiteDatabase.CursorFactory factory) {
    super(context, DATABASE_NAME, factory, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    String query_record = "CREATE TABLE " + TABLE_RECORDS + "(" +
            TABLE_RECORDS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TABLE_RECORDS_COLUMN_ACTIVITY_ID + " INTEGER, " +
            TABLE_RECORDS_COLUMN_START_TIME + " INTEGER, " +
            TABLE_RECORDS_COLUMN_END_TIME + " INTEGER " +
            ")";

    sqLiteDatabase.execSQL(query_record);

    String query_activity = "CREATE TABLE " + TABLE_ACTIVITY + "(" +
            TABLE_ACTIVITY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TABLE_ACTIVITY_COLUMN_NAME + " TEXT, " +
            TABLE_ACITVITY_COLUMN_GOAL_DAILY + " INTEGER, " +
            TABLE_ACTIVITY_COLUMN_GOAL_MODE + " INTEGER" +
            ")";

    sqLiteDatabase.execSQL(query_activity);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS); //delete table...
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
    onCreate(sqLiteDatabase);
  }

  /**
   * Method to add an activity to the database (not a recording)
   * @param newActivity
   *  Activity to add.
   */
  public void addActiviy(MyActivity newActivity){
    //check if the activity already exists.
    if(!existsActivity(newActivity.getName()) && newActivity.getName().length() > 0){
      ContentValues value = new ContentValues();
      value.put(TABLE_ACTIVITY_COLUMN_NAME, newActivity.getName());
      value.put(TABLE_ACITVITY_COLUMN_GOAL_DAILY, newActivity.getActualGoal());
      value.put(TABLE_ACTIVITY_COLUMN_GOAL_MODE, newActivity.getGoalMode());
      SQLiteDatabase db = getWritableDatabase();
      db.insert(TABLE_ACTIVITY, null, value);
      db.close();
    }
  }

  public int getActivityID(String activityName){
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + TABLE_ACTIVITY_COLUMN_NAME + " = '" + activityName + "'";
    Cursor c = db.rawQuery(query, null);
    c.moveToFirst();
    int myid = c.getInt(c.getColumnIndex(TABLE_ACTIVITY_COLUMN_ID));
    c.close();
    return myid;
  }

  public String getActivityName(int id){
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + TABLE_ACTIVITY_COLUMN_ID + " = '" + id + "'";
    Cursor c = db.rawQuery(query, null);
    if(c.getCount() > 0) {
      c.moveToFirst();
      String myName = c.getString(c.getColumnIndex(TABLE_ACTIVITY_COLUMN_NAME));
      c.close();
      return myName;
    }else{
      return "◉_◉ : NULL ";
    }

  }
  /**
   * this method checks if an Activity Name is already in the database
   * @param activityName
   *  the name of the activity
   * @return
   *  if this name exists already
   *
   */
  public boolean existsActivity(String activityName){
    //try to find it in the database/@
    boolean result;
    SQLiteDatabase db = getWritableDatabase();
    String query = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + TABLE_ACTIVITY_COLUMN_NAME + " = \"" + activityName.toUpperCase() + "\"";
    Cursor c = db.rawQuery(query, null);
    if(c.getCount() > 0){
      result =  true;
      Log.d("Database", "Found Activity!");
    }else{
      result =  false;
      Log.d("Database", "404 Activity!");
    }
    c.close();
    db.close();
    return result;

  }

  /**
   * Method to remove an activity (not a recording)
   * @param activityName
   *  use this name to delete the one in the database
   */
  public void deleteActivity(String activityName){
    //delete all the linked records in table records
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "DELETE FROM " + TABLE_RECORDS + " WHERE " + TABLE_RECORDS_COLUMN_ACTIVITY_ID + " = \"" +  getActivityID(activityName) + "\"";
    db.execSQL(query);
    //delete the activity
    query = "DELETE FROM " + TABLE_ACTIVITY + " WHERE " + TABLE_ACTIVITY_COLUMN_NAME + " = \"" + activityName + "\"";
    db.execSQL(query);

    db.close();
  }

  /**
   * Method that updates an exciting activity,
   * e.g. the goal settings...
   * possibly rename the activity
   * @param oldActivity - to be updated
   * @param newActivity - the new one
   */
  public void updateActivity(MyActivity oldActivity, MyActivity newActivity){

    //change the id of all the records.
    ArrayList<SingleActivityRecord> sar = getRecords(0, Long.MAX_VALUE, oldActivity.getName());

    //lmao delete that one and add this one
    deleteActivity(oldActivity.getName());
    addActiviy(newActivity);

    int newID = getActivityID(newActivity.getName());
    for(SingleActivityRecord s : sar){
      s.setActivity_id(newID);
      addRecord(s);
    }

  }

  /**
   * Method to add a new item to the database.
   * @param newRecord
   *  what to be added
   */
  public void addRecord(SingleActivityRecord newRecord){
    ContentValues value = new ContentValues();
   // value.put(COLUMN_PRODUCTNAME, newProdut.get_productname());
    value.put(TABLE_RECORDS_COLUMN_ACTIVITY_ID, newRecord.getActivity_id());
    value.put(TABLE_RECORDS_COLUMN_START_TIME, newRecord.getStartTime());
    value.put(TABLE_RECORDS_COLUMN_END_TIME, newRecord.getEndTime());
    SQLiteDatabase db = getWritableDatabase();
    db.insert(TABLE_RECORDS, null, value);
    db.close();
  }

  /**
   * Method that removes a product from database
   * @param removeRecord
   *  what to delete
   */
  public void deleteRecord(SingleActivityRecord removeRecord){
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("DELETE FROM " + TABLE_RECORDS + " WHERE " +
            TABLE_RECORDS_COLUMN_ACTIVITY_ID + " = " + removeRecord.getActivity_id() + " AND " +
                    TABLE_RECORDS_COLUMN_START_TIME + " = " + removeRecord.getStartTime()
            );
    db.close();
  }

  /**
   * method to update a record in db
   * @param sarOld
   *  the old record
   * @param sarNew
   *  the new record
   */

  public void updateRecord(SingleActivityRecord sarOld, SingleActivityRecord sarNew){
    deleteRecord(sarOld);
    addRecord(sarNew);
  }

  public ArrayList<MyActivity> getActivity(){
    ArrayList<MyActivity> aActivity = new ArrayList<>();
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "SELECT * FROM " + TABLE_ACTIVITY + " ";
    //cursor points to a location in your result;
    Cursor c =  db.rawQuery(query, null);
    //move to first row
    MyActivity newAct;
    Goal newGoal;
    while(c.moveToNext()){

      String name = c.getString(c.getColumnIndex(TABLE_ACTIVITY_COLUMN_NAME));
      int aob = c.getInt(c.getColumnIndex(TABLE_ACTIVITY_COLUMN_GOAL_MODE));
      int goal = c.getInt(c.getColumnIndex(TABLE_ACITVITY_COLUMN_GOAL_DAILY));

      newGoal = new Goal();

      if(aob ==1){

        newGoal.setGoal(goal, true);
      }else{
        newGoal.setGoal(goal, false);
      }

      newAct = new MyActivity(name, newGoal);
      aActivity.add(newAct);
    }
    c.close();
    db.close();
    return aActivity;
  }

  /**
   * gets a list of records
   * @param minDate
   *  the range of date
   * @param maxDate
   *  the max range of date
   * @param activityName
   *  use "" to select all, or select a particular activity.
   * @return
   * list of records
   */
  public ArrayList<SingleActivityRecord> getRecords(long minDate, long maxDate, String activityName){
    ArrayList<SingleActivityRecord> sSAR = new ArrayList<>();
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "";
    if(activityName.length() > 0){
      query = "SELECT * FROM " + TABLE_RECORDS + " WHERE " +  TABLE_RECORDS_COLUMN_ACTIVITY_ID + "='"+getActivityID(activityName)+"' AND " + TABLE_RECORDS_COLUMN_START_TIME + " >= '" + minDate + "' AND " + TABLE_RECORDS_COLUMN_END_TIME + " <= '"  + maxDate + "'";
    }else{
      query = "SELECT * FROM " + TABLE_RECORDS + " WHERE " + TABLE_RECORDS_COLUMN_START_TIME + " >= '" + minDate + "' AND " + TABLE_RECORDS_COLUMN_END_TIME + " <= '"  + maxDate + "'";
    }

    //cursor points to a location in your result;
    Cursor c =  db.rawQuery(query, null);
    //move to first row
    SingleActivityRecord sar;
    while(c.moveToNext()){
      int activityID = c.getInt(c.getColumnIndex(TABLE_RECORDS_COLUMN_ACTIVITY_ID));
      long startTime = c.getLong(c.getColumnIndex(TABLE_RECORDS_COLUMN_START_TIME));
      long endTime = c.getLong(c.getColumnIndex(TABLE_RECORDS_COLUMN_END_TIME));

      sar = new SingleActivityRecord(activityID, startTime, endTime);
      sSAR.add(sar);
    }
    c.close();
    db.close();
    return sSAR;
  }

  /***
   * just for testing
   */
  public void clearDatabase(){
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("DELETE FROM " + TABLE_RECORDS + " WHERE 1;");
    db.execSQL("DELETE FROM " + TABLE_ACTIVITY + " WHERE 1;");
    db.close();
  }

  //keep
  public String[] getDBRecord(String sql_Query){
    String dbRecord[] = {};
    String dbContent = "";

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor c = db.rawQuery(sql_Query, null);
    c.moveToFirst();
    if(c.getCount() !=0){
      for(int i = 0; i < c.getColumnCount(); i++){
        dbContent = dbContent + c.getString(i) + ",";
      }
      if(dbContent.length() > 0){
        dbContent = dbContent.substring(0, dbContent.length() - 1);
      }
      dbRecord = dbContent.split(",");
      return dbRecord;
    }
    return null;
  }
}
