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
  private final String TABLE_RECORDS = "RECORD_TABLE";
  private final String TABLE_RECORDS_COLUMN_ID = "ID";
  //the id that links to the activity in activity table
  private final String TABLE_RECORDS_COLUMN_ACTIVITY_ID = "ACTIVITY_ID";
  //the integer that stores the start time in milliseconds
  private final String TABLE_RECORDS_COLUMN_START_TIME = "START_TIME";
  //the integer that stores the end time in milliseconds
  private final String TABLE_RECORDS_COLUMN_END_TIME = "END_TIME";

  //TABLE FOR THE ACTIVITY (THE NAMES)
  private final String TABLE_ACTIVITY = "ACTIVITY_TABLE";
  private final String TABLE_ACTIVITY_COLUMN_ID = "ACTIVITY_ID";
  private final String TABLE_ACTIVITY_COLUMN_NAME = "ACTIVITY_NAME";
  //the integer that stores the length of a activity
  private final String TABLE_ACITVITY_COLUMN_GOAL_DAILY = "GOAL_DAILY";
  //the integer that indicate whether or not to pass the goal. e.g "0 = not to reach goal" "1 = need to reach goal"
  private final String TABLE_ACTIVITY_COLUMN_GOAL_MODE = "GOAL_MODE";

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
    String query = "DELETE FROM " + TABLE_ACTIVITY + " WHERE " + TABLE_ACTIVITY_COLUMN_NAME + " = \"" + activityName + "\"";
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL(query);
    db.close();
  }

  /**
   * Method that updates an exciting activity,
   * e.g. the goal settings...
   * possibly rename the activity
   * @param activity
   */
  public void updateActivity(MyActivity activity){
    //lmao delete that one and add this one
    deleteActivity(activity.getName());
    addActiviy(activity);
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

  public String recordToString(){
    String dbString = "";
    TimeConverter TC = new TimeConverter();
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "SELECT * FROM " + TABLE_RECORDS + " ";
    //cursor points to a location in your result;
    Cursor c =  db.rawQuery(query, null);
    //move to first row

    while(c.moveToNext()){
      long stimeMills = c.getLong(c.getColumnIndex(TABLE_RECORDS_COLUMN_START_TIME));
      long etimeMills = c.getLong(c.getColumnIndex(TABLE_RECORDS_COLUMN_END_TIME));
      dbString += c.getString(c.getColumnIndex(TABLE_RECORDS_COLUMN_ACTIVITY_ID)) + "start: " + TC.getTimeString(stimeMills, true)  + " -- end: " + TC.getTimeString(etimeMills, true);
      dbString += "\n";
    }
    c.close();
    db.close();
    return dbString;
  }

  public String activityToString(){
    String dbString = "";
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "SELECT * FROM " + TABLE_ACTIVITY + " ";
    //cursor points to a location in your result;
    Cursor c =  db.rawQuery(query, null);
    //move to first row

    while(c.moveToNext()){
      dbString += c.getString(c.getColumnIndex(TABLE_ACTIVITY_COLUMN_NAME));
      dbString += "\n";
    }
    c.close();
    db.close();
    return dbString;
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
}
