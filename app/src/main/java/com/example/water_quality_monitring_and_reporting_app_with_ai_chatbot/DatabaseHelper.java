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
    private static final String TABLE_USER_UBIDOTS_CREDENTIALS = "userUbidotsCredentials";

    private static final String TABLE_ORGANIZATION = "organization";
    private static final String TABLE_EMPLOYEE_ORGANIZATION = "employeeOrganization";
    private static final String TABLE_INVESTIGATION_TEAM = "investigationTeam";
    private static final String TABLE_INVESTIGATION_TEAM_MEMBER = "investigationTeamMember";

    private static final String TABLE_REPORT_FROM_USER = "reportFromUser";
    private static final String TABLE_REPORT_FROM_USER_IMAGE = "reportFromUserImage";
    private static final String TABLE_REPORT_LOCATION = "reportLocation";
    private static final String TABLE_REPORT_INVESTIGATION = "reportInvestigation";
    private static final String TABLE_REPORT_CLEANING_PROCESS = "reportCleaningProcess";


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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORGANIZATION + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE_ORGANIZATION + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVESTIGATION_TEAM + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVESTIGATION_TEAM_MEMBER + ";");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_FROM_USER + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_FROM_USER_IMAGE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_LOCATION + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_INVESTIGATION + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_CLEANING_PROCESS + ";");


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
                "ubidotsUserID TEXT NOT NULL, " +
                "FOREIGN KEY (ubidotsUserID) REFERENCES user (userID));");



        db.execSQL("CREATE TABLE " + TABLE_ORGANIZATION +"(" +
                "orgID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "orgName TEXT NOT NULL, " +
                "orgAddressLine TEXT NOT NULL, " +
                "orgPostCode TEXT NOT NULL, " +
                "orgCity TEXT NOT NULL, " +
                "orgState TEXT NOT NULL, " +
                "orgReady TEXT NOT NULL);");

        db.execSQL("CREATE TABLE " + TABLE_EMPLOYEE_ORGANIZATION +"(" +
                "employOrgID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "orgID TEXT NOT NULL, " +
                "userID TEXT NOT NULL, " +
                "FOREIGN KEY (orgID) REFERENCES " + TABLE_ORGANIZATION + " (orgID), " +
                "FOREIGN KEY (userID) REFERENCES " + TABLE_ORGANIZATION + " (userID));");

        db.execSQL("CREATE TABLE " + TABLE_INVESTIGATION_TEAM +"(" +
                "investigationTeamID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "investigationTeamName TEXT NOT NULL, " +
                "investigationTeamOrgID TEXT NOT NULL, " +
                "FOREIGN KEY (investigationTeamOrgID) REFERENCES "+ TABLE_ORGANIZATION +" (orgID));");

        db.execSQL("CREATE TABLE " + TABLE_INVESTIGATION_TEAM_MEMBER +"(" +
                "investigationTeamMemID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "investigationTeamUserID TEXT NOT NULL, " +
                "investigationTeamID TEXT NOT NULL, " +
                "FOREIGN KEY (investigationTeamUserID) REFERENCES " + TABLE_USER + " (userID), " +
                "FOREIGN KEY (investigationTeamID) REFERENCES " + TABLE_INVESTIGATION_TEAM + " (investigationTeamID));");

        //Report
        db.execSQL("CREATE TABLE " + TABLE_REPORT_FROM_USER +"(" +
                "reportID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "reportDesc TEXT NOT NULL, " +
                "reportDate DATE NOT NULL, " +
                "reportTime TEXT NOT NULL, " +
                "reportStatus TEXT NOT NULL, " +
                "reportPollutionCause TEXT, " +
                "reportEstimatedSolveDuration TEXT, " +
                "reportInvestigationTeam TEXT, " +
                "examiner TEXT NOT NULL, " +
                "reportHandler TEXT, " +
                "orgID TEXT, " +
                "userID TEXT NOT NULL, " +
                "FOREIGN KEY (reportInvestigationTeam) REFERENCES " + TABLE_INVESTIGATION_TEAM + " (investigationTeamID), " +
                "FOREIGN KEY (examiner) REFERENCES " + TABLE_USER + " (userID), " +
                "FOREIGN KEY (reportHandler) REFERENCES " + TABLE_USER + " (userID), "+
                "FOREIGN KEY (orgID) REFERENCES " + TABLE_ORGANIZATION + " (orgID), "+
                "FOREIGN KEY (userID) REFERENCES " + TABLE_USER + " (userID));");

        db.execSQL("CREATE TABLE " + TABLE_REPORT_FROM_USER_IMAGE +"(" +
                "reportImageID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "reportImageFilePath TEXT NOT NULL, " +
                "reportID TEXT NOT NULL, " +
                "FOREIGN KEY (reportID) REFERENCES "+ TABLE_REPORT_FROM_USER +" (reportID));");

        db.execSQL("CREATE TABLE " + TABLE_REPORT_LOCATION +"(" +
                "reportLocationID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "reportaddressLine TEXT NOT NULL, " +
                "reportPostcode TEXT NOT NULL, " +
                "reportCity TEXT NOT NULL, " +
                "reportState TEXT NOT NULL, " +
                "reportLatitude TEXT NOT NULL, " +
                "reportLongitude TEXT NOT NULL, " +
                "reportID TEXT NOT NULL, " +
                "FOREIGN KEY (reportID) REFERENCES "+ TABLE_REPORT_FROM_USER +" (reportID));");

        db.execSQL("CREATE TABLE " + TABLE_REPORT_INVESTIGATION +"(" +
                "investigationDocID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "firstInvestigationDocPath TEXT NOT NULL, " +
                "secondInvestigationDocPath TEXT NOT NULL, " +
                "reportID TEXT NOT NULL, " +
                "FOREIGN KEY (reportID) REFERENCES " + TABLE_REPORT_FROM_USER + " (reportID));");

        db.execSQL("CREATE TABLE " + TABLE_REPORT_CLEANING_PROCESS +"(" +
                "reportDealingID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "pollutionCleaningProcDocPath TEXT NOT NULL, " +
                "documentStatus TEXT NOT NULL, " +
                "remark TEXT NOT NULL, " +
                "reportID TEXT NOT NULL, " +
                "FOREIGN KEY (reportID) REFERENCES " + TABLE_REPORT_FROM_USER + " (reportID));");



        //(investigationDocID, firstInvestigationDoc, secondInvestigationDoc, reportID
//        private static final String TABLE_REPORT_FROM_USER = "reportFromUser";
//        private static final String TABLE_REPORT_FROM_USER_IMAGE = "reportFromUserImage";
//        private static final String TABLE_REPORT_LOCATION = "reportLocation";
//        private static final String TABLE_REPORT_INVESTIGATION = "reportInvestigation";
//        private static final String TABLE_REPORT_CLEANING_PROCESS = "reportCleaningProcess";

    }

    /**
     * Includes query to insert default data into the database
     *
     * @param db is the SQLiteDatabase sent from OnCreate
     */
    private void insertDefaultData(SQLiteDatabase db) {

        //Default user data is inserted into database
        db.execSQL("INSERT INTO " + TABLE_USER + "(userEmail, fName, lName, phoneNo, userType, password) VALUES " +
                "('i12@gmail.com','Jason', 'Ng','0111111111','NA','i12')," +
                "('i13@gmail.com','Jeremy', 'Teoh','0111111125','NA','1')," +
                "('i14@gmail.com','Jer', 'Teo','0111111121','NA','1')," +

                "('a1@gmail.com','Selina', 'Wong','0111111321','AD','1')," +
                "('a2@gmail.com','Rebecca', 'Cheng','0111011344','AD','1')," +

                "('i17@gmail.com','Rush', 'Lim','0111118344','IN','1')," +
                "('i18@gmail.com','Nelson', 'Tan','0111117344','IN','1')," +
                "('i19@gmail.com','Peter', 'Wong','0111117044','IN','1')," +
                "('i20@gmail.com','Robert', 'Goh','0112117044','IN','1')," +
                "('i21@gmail.com','Jessica', 'Chua','0112117054','IN','1')," +
                "('i22@gmail.com','Billy', 'Chun','0112117004','IN','1')," +
                "('i23@gmail.com','Pete', 'Tsai','0112117555','IN','1')," +
                "('i24@gmail.com','Charles', 'Soo','0112117556','IN','1'), " +

                "('a3@gmail.com','Mohammed', 'Ahmad','0111011345','AD','1')," +
                "('a4@gmail.com','Siti', 'Farah','0111011346','AD','1'), " +

                "('e1@gmail.com','Fancy', 'Yap','0111011347','EX','1'), " +
                "('e2@gmail.com','Bryan', 'Yip','0111011348','EX','1'), " +
                "('e3@gmail.com','Wei Ting', 'Mack','0111011349','EX','1'), " +
                "('e4@gmail.com','Ruth', 'Lee','0111011350','EX','1'), " +

                "('r1@gmail.com','Roth', 'Lee','0112011350','RH','1'), " +
                "('r2@gmail.com','Lara', 'Corft','0113011350','RH','1'), " +
                "('r3@gmail.com','Jess', 'Chong','0114011350','RH','1'), " +
                "('r4@gmail.com','Zhe Li', 'Tan','0115011350','RH','1'), " +

                "('sa1@gmail.com','System Admin', '1','0112011359','SAD','sad1'), " +
                "('sa2@gmail.com','System Admin', '2','0113011351','SAD','sad1')");

        //Default vaccine data is inserted into database
        db.execSQL("INSERT INTO " + TABLE_USER_ADDRESS + "(addressLine, postcode, city, state, addressUserID)" +
                "VALUES('5 Jln Pingirran 34 Medan Damai Ukay Hulu Ampang', '68000', 'Klang', 'Selangor', '1'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '68000', 'Klang', 'Selangor', '2'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '68000', 'Klang', 'Selangor', '3'), " +

                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '68000', 'Klang', 'Selangor', '4'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '68000', 'Klang', 'Selangor', '5'), " +

                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81000', 'Kulai', 'Johor', '6'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81000', 'Kulai', 'Johor', '7'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '80300', 'Johor Bahru', 'Johor', '8'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '80300', 'Johor Bahru', 'Johor', '9'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '80300', 'Johor Bahru', 'Johor', '10'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '80300', 'Johor Bahru', 'Johor', '11'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Pasir Gudang', 'Johor', '12'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Pasir Gudang', 'Johor', '13'), " +

                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Pasir Gudang', 'Johor', '14'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Pasir Gudang', 'Johor', '15'), " +

                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81000', 'Kulai', 'Johor', '16'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Petaling Jaya', 'Selangor', '17'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Kota Tinggi', 'Johor', '18'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Pasir Gudang', 'Johor', '19'), " +

                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81000', 'Kulai', 'Johor', '20'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Petaling Jaya', 'Selangor', '21'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Kota Tinggi', 'Johor', '22'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Kota Tinggi', 'Johor', '23'), " +

                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Kota Tinggi', 'Johor', '24'), " +
                "('6 Jln Smile 34 Medan Damai Ukay Hulu Ampang', '81700', 'Kota Tinggi', 'Johor', '25')");

        db.execSQL("INSERT INTO " + TABLE_ORGANIZATION + "(orgName, orgAddressLine, orgPostCode, orgCity, orgState, orgReady)" +
                "VALUES ('Department of Environment (DOE) Kulai', '3, Jln Berjaya, Kampung Berjaya', '81000', 'Kulai', 'Johor', '1'), " +
                "('Department of Environment (DOE) Kuala Lumpur', '3, Jln BBB, Kampung Sungai Kayu Ara', '47400', 'Petaling Jaya', 'Selangor', '1'), " +
                "('Water Saver Organization Kota Tinggi', '5, Jln Cerita, Kampung Cerita', '81900', 'Kota Tinggi', 'Johor', '1'), " +
                "('Water Saver Organization Pasir Gudang', '52, Jln Cerita Baru, Kampung Cerita Baru', '81700', 'Pasir Gudang', 'Johor', '1')");

        db.execSQL("INSERT INTO " + TABLE_EMPLOYEE_ORGANIZATION + "(orgID, userID)" +
                "VALUES " +
                "('1', '4'), " +
                "('2', '5'), " +

                "('1', '6'), " +
                "('1', '7'), " +
                "('3', '8'), " +
                "('3', '9'), " +
                "('4', '10'), " +
                "('4', '11'), " +
                "('4', '12'), " +
                "('4', '13'), " +

                "('3', '14'), " +
                "('4', '15'), " +

                "('1', '16'), " +
                "('2', '17'), " +
                "('3', '18'), " +
                "('4', '19'), " +

                "('1', '20'), " +
                "('2', '21'), " +
                "('3', '22'), " +
                "('4', '23')");

        db.execSQL("INSERT INTO " + TABLE_INVESTIGATION_TEAM + "(investigationTeamName, investigationTeamOrgID)" +
                "VALUES ('Investigation Team 1', '1'), " +
                "('Investigation Team 2', '3')," +
                "('Investigation Team 3', '4')," +
                "('Investigation Team 4', '4')");

        db.execSQL("INSERT INTO " + TABLE_INVESTIGATION_TEAM_MEMBER + "(investigationTeamUserID, investigationTeamID)" +
                "VALUES " +
                "('6', '1'), " +
                "('7', '1'), " +
                "('8', '2'), " +
                "('9', '2'), " +
                "('10', '3'), " +
                "('11', '3'), " +
                "('12', '4'), " +
                "('13', '4')");

//        "investigationTeamUserID TEXT NOT NULL, " +
//                "investigationTeamID TEXT NOT NULL, " +
//
//
//        private static final String TABLE_INVESTIGATION_TEAM = "investigationTeam";
//        private static final String TABLE_INVESTIGATION_TEAM_MEMBER = "investigationTeamMember";

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

        String userID = getUserID(userEmail);

        ContentValues conValUserAddress = new ContentValues();
        conValUserAddress.put("addressLine", addressLine);
        conValUserAddress.put("postcode", postcode);
        conValUserAddress.put("city", city);
        conValUserAddress.put("state", state);
        conValUserAddress.put("addressUserID", userID);

        return insertUserStatus && db.insert(TABLE_USER_ADDRESS, null, conValUserAddress) != -1;
    }

    public Boolean addAPIKey(String API, String userID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValUserUbidotsCredentials = new ContentValues();
        conValUserUbidotsCredentials.put("ubidotsAPI", API);
        conValUserUbidotsCredentials.put("ubidotsUserID", userID);

        return db.insert(TABLE_USER_UBIDOTS_CREDENTIALS, null, conValUserUbidotsCredentials) != -1;
    }

    public Boolean addOrg(String orgName, String orgAddressLine, String orgPostCode, String orgCity, String orgState){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValOrg = new ContentValues();
        conValOrg.put("orgName", orgName);
        conValOrg.put("orgAddressLine", orgAddressLine);
        conValOrg.put("orgPostCode", orgPostCode);
        conValOrg.put("orgCity", orgCity);
        conValOrg.put("orgState", orgState);
        conValOrg.put("orgReady", "0");

        return db.insert(TABLE_ORGANIZATION, null, conValOrg) != -1;
    }

    public Boolean addEmployeeOrg(String orgID, String userID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValEmployeeOrg = new ContentValues();
        conValEmployeeOrg.put("orgID", orgID);
        conValEmployeeOrg.put("userID", userID);

        return db.insert(TABLE_EMPLOYEE_ORGANIZATION, null, conValEmployeeOrg) != -1;
    }

    public Boolean addReport(String reportDesc, String reportDate, String reportTime, String reportStatus, String examiner, String orgID, String userID,
                             String reportImageFilePaths[],
                             String reportaddressLine, String reportPostcode, String reportCity, String reportState, String reportLatitude, String reportLongitude){

        Boolean insertedReportFromUser = false, insertedReportImage = false, insertedReportLocation = false;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValReportFromUser = new ContentValues();
        conValReportFromUser.put("reportDesc", reportDesc);
        conValReportFromUser.put("reportDate", reportDate);
        conValReportFromUser.put("reportTime", reportTime);
        conValReportFromUser.put("reportStatus", reportStatus);
        //conValReportFromUser.put("reportPollutionCause", reportPollutionCause);
        //conValReportFromUser.put("reportEstimatedSolveDuration", reportEstimatedSolveDuration);
        //conValReportFromUser.put("reportInvestigationTeam", reportInvestigationTeam);
        conValReportFromUser.put("examiner", examiner);
        //conValReportFromUser.put("reportHandler", reportHandler);
        conValReportFromUser.put("orgID", orgID);
        conValReportFromUser.put("userID", userID);

        insertedReportFromUser = db.insert(TABLE_REPORT_FROM_USER, null, conValReportFromUser) != -1;

        String reportID = getReportID(reportDate, reportTime, userID);

        if(insertedReportFromUser){
            try{
                // insert image
                for(int i = 0; i < reportImageFilePaths.length; i++){
                    ContentValues conValReportImage = new ContentValues();
                    conValReportImage.put("reportImageFilePath", reportImageFilePaths[i]);
                    conValReportImage.put("reportID", reportID);
                    db.insert(TABLE_REPORT_FROM_USER_IMAGE, null, conValReportImage);
                }

                insertedReportImage = true;

            }catch (Exception e){
                insertedReportImage = false;

            }

            // insert report location
            ContentValues conValReportLocation = new ContentValues();
            conValReportLocation.put("reportaddressLine", reportaddressLine);
            conValReportLocation.put("reportPostcode", reportPostcode);
            conValReportLocation.put("reportCity", reportCity);
            conValReportLocation.put("reportState", reportState);
            conValReportLocation.put("reportLatitude", reportLatitude);
            conValReportLocation.put("reportLongitude", reportLongitude);
            conValReportLocation.put("reportID", reportID);
            insertedReportLocation = db.insert(TABLE_REPORT_LOCATION, null, conValReportLocation) != -1;
        }

        return insertedReportFromUser && insertedReportImage && insertedReportLocation;
    }

    public boolean addInvestigationTeamMember(String userID, String investigationTeamID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValTeamMem = new ContentValues();
        conValTeamMem.put("investigationTeamUserID", userID);
        conValTeamMem.put("investigationTeamID", investigationTeamID);

        return db.insert(TABLE_INVESTIGATION_TEAM_MEMBER, null, conValTeamMem) != -1;
    }

    public boolean isEmail_Exist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE userEmail=?", new String[]{email});
        return (cursor.getCount() > 0); //true if exists
    }

    public boolean isPhone_Exist(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE phoneNo=?", new String[]{phone});
        return (cursor.getCount() > 0); //true if exists
    }

    public boolean isAPIKey_exist(String API){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_UBIDOTS_CREDENTIALS + " WHERE ubidotsAPI=?", new String[]{API});
        return (cursor.getCount() > 0);
    }

    public boolean isOrgExist(String orgName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORGANIZATION + " WHERE orgName=?", new String[]{orgName});
        return (cursor.getCount() > 0);
    }

    public String getUserID(String userEmail){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT userID FROM " + TABLE_USER + " WHERE userEmail=? ", new String[]{userEmail});;

        return (cursor.moveToFirst()) ? cursor.getString(cursor.getColumnIndex("userID")) : "";
    }

    public String getAPIKey(String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ubidotsAPI FROM " + TABLE_USER_UBIDOTS_CREDENTIALS + " WHERE ubidotsUserID=? ", new String[]{userID});

        return (cursor.moveToFirst()) ? cursor.getString(cursor.getColumnIndex("ubidotsAPI")) : "";
    }

    public String getorgID(String orgName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT orgID FROM " + TABLE_ORGANIZATION + " WHERE orgName=? ", new String[]{orgName});

        return (cursor.moveToFirst()) ? cursor.getString(cursor.getColumnIndex("orgID")) : "";
    }

    public Cursor getOrgInfoByUserID(String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT org.orgID, org.orgName FROM " + TABLE_ORGANIZATION + " org, "+ TABLE_EMPLOYEE_ORGANIZATION +" employOrg WHERE employOrg.userID=? AND org.orgID = employOrg.orgID", new String[]{userID});
    }

    public Cursor getAvailableOrgPostcodeByState(String reportState){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT DISTINCT orgPostCode FROM " + TABLE_ORGANIZATION + " WHERE orgState=? AND orgReady='1' ORDER BY orgPostCode ASC", new String[]{reportState});
    }

    public Cursor getAvailableOrgIDBySelectedPostcode(String selectedPostcode){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT orgID FROM " + TABLE_ORGANIZATION + " WHERE orgPostCode=? AND orgReady='1' ORDER BY orgPostCode ASC", new String[]{selectedPostcode});
    }

    public Cursor getOrgReportNumByOrgID(String orgID){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT org.orgID, COUNT(report.reportID) FROM " + TABLE_REPORT_FROM_USER +" report, "+ TABLE_ORGANIZATION +" org WHERE org.orgID=?  AND report.orgID = org.orgID GROUP BY org.orgID ORDER BY COUNT(report.reportID)", new String[]{orgID});
    }

    public Cursor getExaminerReportNumByExaminerID(String examinerID){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT user.userID, COUNT(report.reportID) FROM " + TABLE_REPORT_FROM_USER +" report, "+ TABLE_USER +" user WHERE user.userID=?  AND report.userID = user.userID GROUP BY user.userID ORDER BY COUNT(report.reportID)", new String[]{examinerID});
    }

    public String getOrgIDWithLeastReports(Cursor cursorAvailableOrgID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String WHERE_clause = "";
        for (int i = 0; i < cursorAvailableOrgID.getCount(); i++){
            if(i == 0){
                WHERE_clause += "orgID=" + cursorAvailableOrgID.getString(cursorAvailableOrgID.getColumnIndex("orgID")) + " ";
            }else{
                WHERE_clause += "OR orgID=" + cursorAvailableOrgID.getString(cursorAvailableOrgID.getColumnIndex("orgID")) + " ";
            }

            cursorAvailableOrgID.moveToNext();
        }

        String query = "SELECT orgID, COUNT(reportID) FROM reportFromUser WHERE " + WHERE_clause + " GROUP BY orgID ORDER BY COUNT(reportID)";

        System.out.println("QUERY CHECK: " + query);

        Cursor getOrgID = db.rawQuery(query, null);

        Cursor cursorOrgWithNoReport = null;

        System.out.println("TWO CURSOR: " + Boolean.toString(cursorAvailableOrgID.getCount() != getOrgID.getCount()) );

        if(cursorAvailableOrgID.getCount() != getOrgID.getCount()){
            System.out.println("move to first? : " + cursorAvailableOrgID.moveToFirst());

            for(int i = 0; i < cursorAvailableOrgID.getCount(); i++){
                cursorOrgWithNoReport = getOrgReportNumByOrgID(cursorAvailableOrgID.getString(cursorAvailableOrgID.getColumnIndex("orgID")));
                cursorOrgWithNoReport.moveToFirst();

                System.out.println("cursorOrgWithNoReport == null?: " + cursorOrgWithNoReport == null);

                if(cursorOrgWithNoReport.getCount() == 0){
                    return cursorAvailableOrgID.getString(cursorAvailableOrgID.getColumnIndex("orgID"));
                }
                cursorAvailableOrgID.moveToNext();
            }
        }

        return (getOrgID.moveToFirst()) ? getOrgID.getString(getOrgID.getColumnIndex("orgID")) : "";
    }

    public Cursor getInvestigatorTeamInfoByUserID(String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT inTeam.investigationTeamID, inTeam.investigationTeamName FROM " + TABLE_INVESTIGATION_TEAM + " inTeam, "+ TABLE_INVESTIGATION_TEAM_MEMBER +" inTeamMem WHERE inTeamMem.investigationTeamUserID=? AND inTeamMem.investigationTeamID = inTeam.investigationTeamID", new String[]{userID});
    }

    public Cursor getInvestigationTeamByOrgID(String orgID){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT investigationTeamID, investigationTeamName FROM " + TABLE_INVESTIGATION_TEAM + " WHERE investigationTeamOrgID=?", new String[]{orgID});
    }

    public Cursor getInvestigationTeamByTeamName(String investigationTeamName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_INVESTIGATION_TEAM + " WHERE investigationTeamName=?", new String[]{investigationTeamName});
    }

    public Cursor getTeamMember(String teamID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT user.fName, user.lName FROM " + TABLE_USER + " user ," + TABLE_INVESTIGATION_TEAM_MEMBER +" teamMem WHERE teamMem.investigationTeamID=? AND teamMem.investigationTeamUserID=user.userID", new String[]{teamID});
    }

    public Cursor getAvailableExaminerByOrgID(String selectedOrgID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT user.userID, employOrg.orgID FROM " + TABLE_USER + " user, " + TABLE_EMPLOYEE_ORGANIZATION + " employOrg WHERE user.userType='EX' AND employOrg.orgID=? AND employOrg.userID=user.userID", new String[]{selectedOrgID});
    }

    public String getExaminerIDWithLeastReports(Cursor cursorAvailableExaminerID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String WHERE_clause = "";

        for (int i = 0; i < cursorAvailableExaminerID.getCount(); i++){
//            try{
                if(i == 0){
                    WHERE_clause += "examiner=" + cursorAvailableExaminerID.getString(cursorAvailableExaminerID.getColumnIndex("userID")) + " ";
                }else{
                    WHERE_clause += "OR examiner=" + cursorAvailableExaminerID.getString(cursorAvailableExaminerID.getColumnIndex("userID")) + " ";
                }
//            }catch(Exception e){
//                System.out.println("ERROR in Where" + e.toString());
//            }


            cursorAvailableExaminerID.moveToNext();
        }

        String query = "SELECT examiner, COUNT(reportID) FROM reportFromUser WHERE " + WHERE_clause + " GROUP BY examiner ORDER BY COUNT(reportID)";

        System.out.println("QUERY CHECK: " + query);

        Cursor getExaminerID = db.rawQuery(query, null);

        Cursor cursorExaminerWithNoReport = null;

        System.out.println("TWO CURSOR: " + Boolean.toString(cursorAvailableExaminerID.getCount() != getExaminerID.getCount()) );

        if(cursorAvailableExaminerID.getCount() != getExaminerID.getCount()){
            System.out.println("move to first? : " + cursorAvailableExaminerID.moveToFirst());

            for(int i = 0; i < cursorAvailableExaminerID.getCount(); i++){
                cursorExaminerWithNoReport = getOrgReportNumByOrgID(cursorAvailableExaminerID.getString(cursorAvailableExaminerID.getColumnIndex("userID")));

                cursorExaminerWithNoReport = getExaminerReportNumByExaminerID(cursorAvailableExaminerID.getString(cursorAvailableExaminerID.getColumnIndex("userID")));
                cursorExaminerWithNoReport.moveToFirst();

                System.out.println("cursorExaminerWithNoReport == null?: " + cursorExaminerWithNoReport == null);

                if(cursorExaminerWithNoReport.getCount() == 0){
                    return cursorAvailableExaminerID.getString(cursorAvailableExaminerID.getColumnIndex("userID"));
                }
                cursorAvailableExaminerID.moveToNext();
            }
        }

        return (getExaminerID.moveToFirst()) ? getExaminerID.getString(getExaminerID.getColumnIndex("examiner")) : "";
    }

    public String getReportID(String reportDate, String reportTime, String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT reportID FROM " + TABLE_REPORT_FROM_USER + " WHERE reportDate=? AND reportTime=? AND userID=?", new String[]{reportDate, reportTime, userID});

        return (cursor.moveToFirst()) ? cursor.getString(cursor.getColumnIndex("reportID")) : "";
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

    public boolean deleteOrg(String orgID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_ORGANIZATION, "orgID=?", new String[]{orgID}) == 1;
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

    public int getTeamMemberNum(String teamID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INVESTIGATION_TEAM_MEMBER + " WHERE investigationTeamID=?", new String[]{String.valueOf(teamID)});
        return cursor.getCount();
    }

    public int getOrgNum() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT orgID FROM " + TABLE_ORGANIZATION, null);
        return cursor.getCount();
    }
}
