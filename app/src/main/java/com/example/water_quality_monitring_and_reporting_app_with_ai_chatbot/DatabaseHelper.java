package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hydromy.db";
    //user table
    private static final String TABLE_USER = "user";
    private static final String TABLE_USER_ADDRESS = "userAddress";
    private static final String TABLE_USER_UBIDOTS_CREDENTIALS = "userUbidotsCredentials";// (credentialID, ubidotsAPI, varDO, varBOD, varVOD, varNH3N, varSS, varPH, userID)

    private static final String COLUMN_IC = "ic";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_VACCINE_STATUS = "status";
    private static final String COLUMN_isADMIN = "isAdmin";
    //vaccine table
    private static final String COLUMN_VACCINE_ID = "vaccineID";
    private static final String COLUMN_VACCINE_NAME = "vaccineName";

    //Constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table(s) for the database
        createTable(db);
        //insert default data(s) into the table(s)
        insertDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ADDRESS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_UBIDOTS_CREDENTIALS + ";");
        onCreate(db);
    }

    /**
     * Includes query to create table for the database
     *
     * @param db is the SQLiteDatabase sent from OnCreate
     */
    private void createTable(SQLiteDatabase db) {

        //COLUMN_IC is the primary key for TABLE_USER
        //constraint foreign key COLUMN_VACCINE_ID to the PRIMARY KEY OF TABLE_VACCINE
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                "userID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "userEmail TEXT NOT NULL, " +
                "fName TEXT NOT NULL, " +
                "lName TEXT NOT NULL, " +
                "phoneNo TEXT NOT NULL, " +
                "userType TEXT NOT NULL, " +
                "password  TEXT NOT NULL);");

        //create SECOND table in the database
        db.execSQL("CREATE TABLE " + TABLE_USER_ADDRESS + " ( " +
                "addressID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "addressLine TEXT NOT NULL, " +
                "postcode TEXT NOT NULL, " +
                "city TEXT NOT NULL, " +
                "state TEXT NOT NULL, " +
                "addressUserID TEXT NOT NULL, " +
                "FOREIGN KEY (addressUserID) REFERENCES user (userID));");

        db.execSQL("CREATE TABLE " + TABLE_USER_UBIDOTS_CREDENTIALS +"(" +
                "ubidotsAPI TEXT PRIMARY KEY NOT NULL, " +
                "varID_DO TEXT NOT NULL, " +
                "varID_BOD TEXT NOT NULL, " +
                "varID_COD TEXT NOT NULL, " +
                "varID_NH3N TEXT NOT NULL, " +
                "varID_SS TEXT NOT NULL, " +
                "varID_PH TEXT NOT NULL, " +
                "ubidotsUserID TEXT NOT NULL, " +
                "FOREIGN KEY (ubidotsUserID) REFERENCES user (userID));");
    }

    /**
     * Includes query to insert default data into the database
     *
     * @param db is the SQLiteDatabase sent from OnCreate
     */
    private void insertDefaultData(SQLiteDatabase db) {

        //Default user data is inserted into database
        db.execSQL("INSERT INTO " + TABLE_USER + "(userEmail, fName, lName, phoneNo, userType, password) VALUES " +
                "('i12@gmail.com','JASON', 'NG','0111111111','NA','i12')");

        String email = "i12@gmail.com";

        String userID = "";
        Cursor cursor = db.rawQuery("SELECT userID FROM " + TABLE_USER + " WHERE userEmail=? ", new String[]{email});;

        if (cursor.moveToFirst()) {
            userID = cursor.getString(cursor.getColumnIndex("userID"));
        }

        //Default vaccine data is inserted into database
        db.execSQL("INSERT INTO " + TABLE_USER_ADDRESS + "(addressLine, postcode, city, state, addressUserID)" +
                "VALUES('5 JLN PINGGIRAN 34 MEDAN DAMAI UKAY HULU AMPANG', '68000', 'Klang', 'Selangor', '" + userID + "')");
    }


    public Boolean addUser(String userEmail, String fName, String lName, String phoneNo, String userType, String password,
                           String addressLine, String postcode, String city, String state) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValUser = new ContentValues();
        conValUser.put("userEmail", userEmail); //0
        conValUser.put("fName", fName);//1
        conValUser.put("lName", lName);//2
        conValUser.put("phoneNo", phoneNo);//3
        conValUser.put("userType", userType);//4
        conValUser.put("password", password);//5

        Boolean insertUserStatus = db.insert(TABLE_USER, null, conValUser) != -1;

        String userID = "";
        Cursor cursor = db.rawQuery("SELECT userID FROM " + TABLE_USER + " WHERE userEmail=? ", new String[]{userEmail});;

        if (cursor.moveToFirst()) {
            userID = cursor.getString(cursor.getColumnIndex("userID"));
        }

        ContentValues conValUserAddress = new ContentValues();
        conValUserAddress.put("addressLine", addressLine);
        conValUserAddress.put("postcode", postcode);
        conValUserAddress.put("city", city);
        conValUserAddress.put("state", state);
        conValUserAddress.put("addressUserID", userID);

        return insertUserStatus && db.insert(TABLE_USER_ADDRESS, null, conValUserAddress) != -1;
    }

    public boolean isEmail_Exist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE userEmail=?", new String[]{email});
        return (cursor.getCount() > 0); //true if exists
    }

    // Registered user info
    public Cursor readInfo(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE userEmail=? ", new String[]{userEmail});
    }

    public boolean isBlank() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);
        return (cursor.getCount() < 1);
    }

    public boolean deleteUser(String ic) {
        //delete user by using NRIC (primary key)
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_USER, COLUMN_IC + "=?", new String[]{ic}) == 1;
    }

    public Cursor getAllUser() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_isADMIN + " ='0' ORDER BY "+COLUMN_IC+" ASC", null);
    }

    public boolean updateUser(String IC, String name, String age, String phone, String address, String status, String notes, String vaccineID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();
        conVal.put(COLUMN_IC, IC); //0
        conVal.put(COLUMN_NAME, name);//1
        conVal.put(COLUMN_AGE, age);//3
        conVal.put(COLUMN_PHONE, phone);//4
        conVal.put(COLUMN_ADDRESS, address);//5
        conVal.put(COLUMN_VACCINE_STATUS, status);//default value is pending//6
        conVal.put(COLUMN_NOTES, notes);//7
        conVal.put(COLUMN_VACCINE_ID, vaccineID); // before come to here need to speficy the id//9

        return db.update(TABLE_USER, conVal, COLUMN_IC + "=?", new String[]{String.valueOf(IC)}) == 1;
    }

    public Cursor searchUserBy(String clause) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_isADMIN + " ='0' AND "
                + COLUMN_IC + " LIKE '%" + clause + "%'", null); //%NRIC%
    }

    public int getUserNum() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " +
                COLUMN_isADMIN + " ='0'", null);
        return cursor.getCount();
    }

}
