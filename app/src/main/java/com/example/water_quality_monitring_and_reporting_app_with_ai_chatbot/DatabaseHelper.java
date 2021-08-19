package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = ".db";
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
                COLUMN_IC + " TEXT PRIMARY KEY , " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_AGE + " TEXT NOT NULL, " +
                COLUMN_PHONE + " TEXT NOT NULL, " +
                COLUMN_ADDRESS + " TEXT NOT NULL, " +
                COLUMN_NOTES + " TEXT NOT NULL, " +
                COLUMN_VACCINE_STATUS + " TEXT NOT NULL, " +
                COLUMN_isADMIN + " TEXT NOT NULL, " +
                COLUMN_VACCINE_ID + " TEXT NOT NULL," +
                "CONSTRAINT fk_vaccine FOREIGN KEY (" + COLUMN_VACCINE_ID + ") " +
                "REFERENCES " + TABLE_USER_ADDRESS + "(" + COLUMN_VACCINE_ID + "));");

        //create SECOND table in the database
        db.execSQL("CREATE TABLE " + TABLE_USER_ADDRESS + " (" +
                COLUMN_VACCINE_ID + " TEXT PRIMARY KEY , " +
                COLUMN_VACCINE_NAME + " TEXT NOT NULL );");
    }

    /**
     * Includes query to insert default data into the database
     *
     * @param db is the SQLiteDatabase sent from OnCreate
     */
    private void insertDefaultData(SQLiteDatabase db) {

        //Default user data is inserted into database
        db.execSQL("INSERT INTO " + TABLE_USER + "(" + COLUMN_IC + "," + COLUMN_NAME + "," +
                COLUMN_PASSWORD + "," + COLUMN_AGE + "," + COLUMN_PHONE + "," +
                COLUMN_ADDRESS + "," + COLUMN_NOTES + "," + COLUMN_VACCINE_STATUS + "," +
                COLUMN_isADMIN + "," + COLUMN_VACCINE_ID + ") VALUES " +
                "('000101142345','JASON NG','000101142345','21','01294864903','5 JLN PINGGIRAN 34 MEDAN DAMAI UKAY HULU AMPANG,68000 KLANG SELANGOR','N/A','PENDING','0','1')," +
                "('000427019999','CHEW PEI TING','000427019999','21','01294864903','5 JLN PINGGIRAN 1 MEDAN DAMAI UKAY HULU AMPANG,68000 KLANG SELANGOR','N/A','PENDING','0','1')," +
                "('991105012980','LIM SIAO SIAO','991105012980','22','0198475735','BLOK B 49 RUMAH PANJANG 1 JLN BATU 13  47100 PUCHONG SELANGOR','N/A','PENDING','0','1')," +
                "('670218145667','MOHD ALI BIN ABUDULLAH','670218145667','54','0164573656','NO. 138 4TH FLOOR JLN TUN H S LEE, 50000 KUALA LUMPUR WILAYAH PERSEKUTUAN','N/A','FIRST DOSE:29TH AUGUST 2021','0','2')," +
                "('880911143556','JANICE HO YI XIAN','880911143556','33','01948758473',' LORONG SERI TERUNTUM 1, MEDAN WARISAN 25100 KUANTAN PAHANG','N/A','2 DOSES OF VACCINATION COMPLETED','0','3')," +
                "('991023042345','PRICILLA TAN WEI XUAN','991023042345','22','0118347546','NO 52A-F BROOKE DRIVE SIBU 96000 SIBU SARAWAK','N/A','PENDING','0','2')," +
                "('940217101011','TOH XIN YU','940217101011','27','0109948584','33 JLN 20/16 SEKSYEN 20 PETALING JAYA 46300 PETALING JAYA','FIRST DOSE DONE','SECOND DOSE:30TH JULY 2021','0','2')," +
                "('881207100882','SAMUEL NG','881207100882','33','01298847684','62 JALAN 10/34A KEPONG ENTERPRENEURS PARK 52100 KUALA LUMPUR, WILAYAH PERSEKUTUAN','N/A','PENDING','0','1')," +
                "('770703019983','DWEE WEN XIN','770703019983','44','0127787342','BLOK H 25 4 JLN PJU 1/37 PRIMA SQUARE PETALING JAYA 46500 PETALING JAYA','N/A','FIRST DOSE:27TH AUGUST 2021','0','3')," +
                "('780113012456','LONG LI SUAN','780113012456','43','0176475638','JALAN DAGANG 1/1A, TAMAN DAGANG, 68000 AMPANG, SELANGOR','N/A','FIRST DOSE:29TH AUGUST 2021','0','1')," +
                "('admin01','TEOH BO WEI','admin01','21','0116835683','C-6-06 RITZE PERDANA JALAN PJU 8/2 47820 PETALING JAYA, SELANGOR','N/A','-','1','1')," +
                "('admin02','CHEW PEI TING','admin02','21','012576773','B5-6 HAPPY MANSION, JALAN 17/3, 46400 PETALING JAYA, SELANGOR','N/A','-','1','1')," +
                "('admin03','BOO TING','admin03','21','0109948573','2A, JALAN DAMAR LAUT 2B TELOK GONG 42000 KLANG, SELANGOR','N/A','-','1','1')");

        //Default vaccine data is inserted into database
        db.execSQL("INSERT INTO " + TABLE_USER_ADDRESS + "(" +
                COLUMN_VACCINE_ID + "," + COLUMN_VACCINE_NAME + ")" +
                "VALUES('1','PFIZER'),('2','SINOVAC'),('3','AZ');");

    }


    public Boolean addUser(String IC, String name, String password, String age, String phone, String address, String notes, String vaccineID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conVal = new ContentValues();
        conVal.put(COLUMN_IC, IC); //0
        conVal.put(COLUMN_NAME, name);//1
        conVal.put(COLUMN_PASSWORD, password);//2
        conVal.put(COLUMN_AGE, age);//3
        conVal.put(COLUMN_PHONE, phone);//4
        conVal.put(COLUMN_ADDRESS, address);//5
        conVal.put(COLUMN_VACCINE_STATUS, "PENDING");//default value is pending//6
        conVal.put(COLUMN_NOTES, notes);//7
        conVal.put(COLUMN_isADMIN, "0"); //default user as not admin//8
        conVal.put(COLUMN_VACCINE_ID, vaccineID); // before come to here need to speficy the id//9

        return db.insert(TABLE_USER, null, conVal) != -1;
    }

    public boolean isIC_Exist(String ic) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_IC + "=?", new String[]{ic});
        return (cursor.getCount() > 0); //true if exists
    }

    // Registered user info
    public Cursor readInfo(String ic) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_IC + "=? ", new String[]{ic});
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
