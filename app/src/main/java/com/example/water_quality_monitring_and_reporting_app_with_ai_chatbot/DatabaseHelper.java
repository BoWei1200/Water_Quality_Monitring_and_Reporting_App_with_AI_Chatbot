package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;


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

    private static final String TABLE_NEWS = "news";
    private static final String TABLE_NEWS_IMAGE = "newsImage";

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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS_IMAGE + ";");

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
                "orgReady TEXT NOT NULL, " +
                "reportIsTaken TEXT NOT NULL);");

        db.execSQL("CREATE TABLE " + TABLE_EMPLOYEE_ORGANIZATION +"(" +
                "employOrgID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "orgID TEXT NOT NULL, " +
                "userID TEXT NOT NULL, " +
                "reportIsTaken TEXT NOT NULL, " +
                "FOREIGN KEY (orgID) REFERENCES " + TABLE_ORGANIZATION + " (orgID), " +
                "FOREIGN KEY (userID) REFERENCES " + TABLE_ORGANIZATION + " (userID));");

        db.execSQL("CREATE TABLE " + TABLE_INVESTIGATION_TEAM +"(" +
                "investigationTeamID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "investigationTeamName TEXT NOT NULL, " +
                "investigationTeamOrgID TEXT NOT NULL, " +
                "reportIsTaken TEXT NOT NULL, " +
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
                "reportBadWQI TEXT, " +
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
                "firstInvestigationDocPath TEXT, " +
                "secondInvestigationDocPath TEXT, " +
                "reportID TEXT NOT NULL, " +
                "FOREIGN KEY (reportID) REFERENCES " + TABLE_REPORT_FROM_USER + " (reportID));");

        db.execSQL("CREATE TABLE " + TABLE_REPORT_CLEANING_PROCESS +"(" +
                "reportDealingID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "pollutionCleaningProcDocPath TEXT, " +
                "documentStatus TEXT, " +
                "remark TEXT, " +
                "reportID TEXT NOT NULL, " +
                "FOREIGN KEY (reportID) REFERENCES " + TABLE_REPORT_FROM_USER + " (reportID));");

        db.execSQL("CREATE TABLE " + TABLE_NEWS +"(" +
                "newsID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "newsTitle TEXT NOT NULL, " +
                "newsDesc TEXT NOT NULL, " +
                "newsDate DATE NOT NULL, " +
                "newsTime TEXT NOT NULL, " +
                "userID TEXT NOT NULL, " +
                "FOREIGN KEY (userID) REFERENCES " + TABLE_REPORT_FROM_USER + " (userID));");

        db.execSQL("CREATE TABLE " + TABLE_NEWS_IMAGE +"(" +
                "newsImageID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "newsImageName TEXT NOT NULL, " +
                "newsID TEXT NOT NULL, " +
                "FOREIGN KEY (newsID) REFERENCES " + TABLE_NEWS + " (newsID));");


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

        db.execSQL("INSERT INTO " + TABLE_ORGANIZATION + "(orgName, orgAddressLine, orgPostCode, orgCity, orgState, orgReady, reportIsTaken)" +
                "VALUES ('Department of Environment (DOE) Kulai', '3, Jln Berjaya, Kampung Berjaya', '81000', 'Kulai', 'Johor', '1', '0'), " +
                "('Department of Environment (DOE) Kuala Lumpur', '3, Jln BBB, Kampung Sungai Kayu Ara', '47400', 'Petaling Jaya', 'Selangor', '1', '0'), " +
                "('Water Saver Organization Kota Tinggi', '5, Jln Cerita, Kampung Cerita', '81900', 'Kota Tinggi', 'Johor', '1', '0'), " +
                "('Water Saver Organization Pasir Gudang', '52, Jln Cerita Baru, Kampung Cerita Baru', '81700', 'Pasir Gudang', 'Johor', '1', '0')");

        db.execSQL("INSERT INTO " + TABLE_EMPLOYEE_ORGANIZATION + "(orgID, userID, reportIsTaken)" +
                "VALUES " +
                "('1', '4', '0'), " +
                "('2', '5', '0'), " +

                "('1', '6', '0'), " +
                "('1', '7', '0'), " +
                "('3', '8', '0'), " +
                "('3', '9', '0'), " +
                "('4', '10', '0'), " +
                "('4', '11', '0'), " +
                "('2', '12', '0'), " +
                "('2', '13', '0'), " +

                "('3', '14', '0'), " +
                "('4', '15', '0'), " +

                "('1', '16', '0'), " +
                "('2', '17', '0'), " +
                "('3', '18', '0'), " +
                "('4', '19', '0'), " +

                "('1', '20', '0'), " +
                "('2', '21', '0'), " +
                "('3', '22', '0'), " +
                "('4', '23', '0')");

        db.execSQL("INSERT INTO " + TABLE_INVESTIGATION_TEAM + "(investigationTeamName, investigationTeamOrgID, reportIsTaken)" +
                "VALUES ('Investigation Team 1', '1', '0'), " +
                "('Investigation Team 2', '3', '0')," +
                "('Investigation Team 3', '4', '0')," +
                "('Investigation Team 4', '4', '0')");

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
        conValOrg.put("reportIsTaken", "0");

        return db.insert(TABLE_ORGANIZATION, null, conValOrg) != -1;
    }

    public Boolean addEmployeeOrg(String orgID, String userID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValEmployeeOrg = new ContentValues();
        conValEmployeeOrg.put("orgID", orgID);
        conValEmployeeOrg.put("userID", userID);
        conValEmployeeOrg.put("reportIsTaken", "0");

        return db.insert(TABLE_EMPLOYEE_ORGANIZATION, null, conValEmployeeOrg) != -1;
    }

    public Boolean addReport(String reportDesc, String reportDate, String reportTime, String reportStatus, String examiner, String orgID, String userID,
                             String reportImageFilePaths[],
                             String reportaddressLine, String reportPostcode, String reportCity, String reportState, String reportLatitude, String reportLongitude,
                             Boolean reportBadWQI, Double currentWQI){

        Boolean insertedReportFromUser = false, insertedReportImage = false, insertedReportLocation = false,
                insertedReportInvestigation = false, insertedReportCleaningProcess = false ;

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

        if(reportBadWQI)
            conValReportFromUser.put("reportBadWQI", currentWQI);

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

            ContentValues conValReportInvestigation = new ContentValues();
            conValReportInvestigation.put("reportID", reportID);
            insertedReportInvestigation = db.insert(TABLE_REPORT_INVESTIGATION, null, conValReportInvestigation) != -1;

            ContentValues conValReportCleaning = new ContentValues();
            conValReportCleaning.put("reportID", reportID);
            insertedReportCleaningProcess = db.insert(TABLE_REPORT_CLEANING_PROCESS, null, conValReportCleaning) != -1;
        }

        return insertedReportFromUser && insertedReportImage && insertedReportLocation && insertedReportInvestigation && insertedReportCleaningProcess;
    }

    public boolean addInvestigationTeam(String investigationTeamName, String orgID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValTeam = new ContentValues();
        conValTeam.put("investigationTeamName", investigationTeamName);
        conValTeam.put("investigationTeamOrgID", orgID);
        conValTeam.put("reportIsTaken", "0");

        return db.insert(TABLE_INVESTIGATION_TEAM, null, conValTeam) != -1;
    }

    public boolean addInvestigationTeamMember(String userID, String investigationTeamID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues conValTeamMem = new ContentValues();
        conValTeamMem.put("investigationTeamUserID", userID);
        conValTeamMem.put("investigationTeamID", investigationTeamID);

        return db.insert(TABLE_INVESTIGATION_TEAM_MEMBER, null, conValTeamMem) != -1;
    }

    public boolean addNews(String title, String desc, String newsDate, String newsTime, String userID, ArrayList<String> imgNameSelected) {
        SQLiteDatabase db = this.getWritableDatabase();

        Boolean insertedNews = false, insertedNewsImage = false;

        ContentValues conValAddNews = new ContentValues();
        conValAddNews.put("newsTitle", title);
        conValAddNews.put("newsDesc", desc);
        conValAddNews.put("newsDate", newsDate);
        conValAddNews.put("newsTime", newsTime);
        conValAddNews.put("userID", userID);

        insertedNews = db.insert(TABLE_NEWS, null, conValAddNews) != -1;

        if(insertedNews){
            String newsID = getNewsID(newsDate, newsTime, userID);

            for(int i = 0; i < imgNameSelected.size(); i++){
                ContentValues conValAddNewsImg = new ContentValues();
                conValAddNewsImg.put("newsImageName", imgNameSelected.get(i));
                conValAddNewsImg.put("newsID", newsID);

                insertedNewsImage = db.insert(TABLE_NEWS_IMAGE, null, conValAddNewsImg) != -1;
            }

        }

        return insertedNews && insertedNewsImage;
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

    public boolean isInvestigationTemNameExist(String investigationTeamName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INVESTIGATION_TEAM + " WHERE investigationTeamName=?", new String[]{investigationTeamName});
        return (cursor.getCount() > 0);
    }

    public String getUserID(String userEmail){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT userID FROM " + TABLE_USER + " WHERE userEmail=? ", new String[]{userEmail});

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

    public Cursor getOrgInfoByOrgID(String orgID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORGANIZATION + " WHERE orgID=?", new String[]{orgID});
        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getOrgInfoByUserID(String userID){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT org.* FROM " + TABLE_ORGANIZATION + " org, "+ TABLE_EMPLOYEE_ORGANIZATION +" employOrg WHERE employOrg.userID=? AND org.orgID = employOrg.orgID", new String[]{userID});
        return cursor.moveToFirst() ? cursor : null;
    }

    public Cursor getOrgEmployeeByOrgIDAndUserType(String orgID, String userType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user.* FROM " + TABLE_USER + " user, " + TABLE_EMPLOYEE_ORGANIZATION + " em WHERE em.orgID=? AND user.userID=em.userID AND user.userType=?", new String[]{orgID, userType});
        return cursor;
    }

    public Cursor getAvailableOrgPostcodeByState(String reportState){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT DISTINCT orgPostCode FROM " + TABLE_ORGANIZATION + " WHERE orgState=? AND orgReady='1' ORDER BY orgPostCode ASC", new String[]{reportState});
    }

    public Cursor getAvailableOrgBySelectedPostcode(String selectedPostcode){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ORGANIZATION + " WHERE orgPostCode=? AND orgReady='1' ORDER BY orgPostCode ASC", new String[]{selectedPostcode});
    }

    public Boolean resetAllAvailableOrgReportIsTakenByPostcode(String orgPostCode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("reportIsTaken", "0");

        return db.update(TABLE_ORGANIZATION, conVal, "orgPostCode=?", new String[]{orgPostCode}) == 1;
    }

    public Boolean resetSelectedOrgReportIsTaken(String selectedOrgID){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("reportIsTaken", "1");

        return db.update(TABLE_ORGANIZATION, conVal, "orgID=?", new String[]{selectedOrgID}) == 1;
    }

    public Cursor getOrgReportNumByOrgID(String orgID){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT org.orgID, COUNT(report.reportID) FROM " + TABLE_REPORT_FROM_USER +" report, "+ TABLE_ORGANIZATION +" org WHERE org.orgID=?  AND report.orgID = org.orgID GROUP BY org.orgID ORDER BY COUNT(report.reportID)", new String[]{orgID});
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
        return db.rawQuery("SELECT employOrg.* FROM " + TABLE_USER + " user, " + TABLE_EMPLOYEE_ORGANIZATION + " employOrg WHERE user.userType='EX' AND employOrg.orgID=? AND employOrg.userID=user.userID", new String[]{selectedOrgID});
    }

    public Boolean resetAllAvailableEmployeeReportIsTakenByOrgIDAndUsertype(String selectedOrgID, String userType) {
        SQLiteDatabase db = getWritableDatabase();

        try{
            db.execSQL("UPDATE employeeOrganization SET reportIsTaken = '0' " +
                    "WHERE userID = ( " +
                    "SELECT employeeOrganization.userID FROM user " +
                    "WHERE userID=employeeOrganization.userID AND userType=? AND employeeOrganization.orgID=? " +
                    ")", new String[]{userType, selectedOrgID});

            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public Boolean resetAllAvailableINTeamReportIsTakenByOrgID(String reportOrgID) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues conVal = new ContentValues();
        conVal.put("reportIsTaken", "0");

        return db.update(TABLE_INVESTIGATION_TEAM, conVal, "investigationTeamOrgID=?", new String[]{reportOrgID}) == 1;
    }

    public boolean resetSelectedExaminerReportIsTaken(String selectedExaminerID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        System.out.println("SELECTED ID IN DB: " + selectedExaminerID);

        conVal.put("reportIsTaken", "1");

        return db.update(TABLE_EMPLOYEE_ORGANIZATION, conVal, "userID=?", new String[]{selectedExaminerID}) == 1;
    }

    public boolean resetSelectedINTeamReportIsTaken(String selectedINTeamID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("reportIsTaken", "1");

        return db.update(TABLE_INVESTIGATION_TEAM, conVal, "investigationTeamID=?", new String[]{selectedINTeamID}) == 1;
    }

    public Boolean resetSelectedReportHandlerReportIsTaken(String selectedReportHandlerID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("reportIsTaken", "1");

        return db.update(TABLE_EMPLOYEE_ORGANIZATION, conVal, "userID=?", new String[]{selectedReportHandlerID}) == 1;
    }

    public String getReportID(String reportDate, String reportTime, String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT reportID FROM " + TABLE_REPORT_FROM_USER + " WHERE reportDate=? AND reportTime=? AND userID=?", new String[]{reportDate, reportTime, userID});

        return (cursor.moveToFirst()) ? cursor.getString(cursor.getColumnIndex("reportID")) : "";
    }

    public Cursor getMyReport(String userID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REPORT_FROM_USER + " WHERE userID=?", new String[]{String.valueOf(userID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getReportByOrgID(String orgID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REPORT_FROM_USER + " WHERE orgID=?", new String[]{String.valueOf(orgID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getPostableReportByOrgID(String orgID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REPORT_FROM_USER + " WHERE orgID=? AND (reportStatus='Resolving' OR reportStatus='Resolved')", new String[]{String.valueOf(orgID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getReportByExaminerID(String examinerUserID, String pendingOrCompleted, String filter, String searchReportID) {
        SQLiteDatabase db = getReadableDatabase();

        String WHERE_CLAUSE = "";
        if(pendingOrCompleted.equals("Pending")){
            if(filter.equals("All")){
                WHERE_CLAUSE += "re.reportStatus = 'Pending' OR (re.reportStatus = 'Investigating1' AND re.reportID=inv.reportID AND inv.firstInvestigationDocPath IS NOT null) OR (re.reportStatus='Examining')";
            }

            if(filter.equals("Validity of Report")){
                WHERE_CLAUSE += "re.reportStatus = 'Pending' ";
            }

            if(filter.equals("1st Investigation")){
                WHERE_CLAUSE += "(re.reportStatus = 'Investigating1' AND re.reportID=inv.reportID AND inv.firstInvestigationDocPath IS NOT null) ";
            }

            if(filter.equals("2nd Investigation & Report on Cleaning Process")){
                WHERE_CLAUSE += "(re.reportStatus='Examining')";
            }
        }else{
            if(filter.equals("All")){
                WHERE_CLAUSE += "(re.reportStatus = 'Investigating1' AND re.reportID=inv.reportID AND inv.firstInvestigationDocPath IS null) OR (re.reportStatus = 'Resolving' ) OR (re.reportStatus='Resolved') OR (re.reportStatus='Rejected')";
            }

            if(filter.equals("Validity of Report")){
                WHERE_CLAUSE += "(re.reportStatus = 'Investigating1' AND re.reportID=inv.reportID AND inv.firstInvestigationDocPath IS null) ";
            }

            if(filter.equals("1st Investigation")){
                WHERE_CLAUSE += "(re.reportStatus = 'Resolving' ) ";
            }

            if(filter.equals("2nd Investigation & Report on Cleaning Process")){
                WHERE_CLAUSE += "(re.reportStatus='Resolved')";
            }
        }

        String query = "SELECT DISTINCT re.*  FROM reportFromUser re, reportInvestigation inv " +
                "WHERE re.examiner=?  AND re.reportID LIKE '%"+ searchReportID +"%' AND" +
                "( "+ WHERE_CLAUSE  +" )" +
                "ORDER BY re.reportDate, re.reportTime";

        System.out.println("query examiner check: " + query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(examinerUserID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getAllProcessingReportByExaminerID(String examinerID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM reportFromUser WHERE examiner=? AND reportStatus != 'Resolved' AND reportStatus != 'Rejected'", new String[]{examinerID});
        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getAvailableInvestigationTeamByOrgID(String orgID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT inTeam.* FROM " + TABLE_INVESTIGATION_TEAM + " inTeam, " + TABLE_INVESTIGATION_TEAM_MEMBER + " inTeamMem WHERE inTeam.investigationTeamOrgID=? AND inTeam.investigationTeamID = inTeamMem.investigationTeamID", new String[]{orgID});
        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getReportByInvestigationTeam(String investigationTeamID, String pendingOrCompleted, String searchReportID) {
        SQLiteDatabase db = getReadableDatabase();

        String WHERE_CLAUSE = "";
        if(pendingOrCompleted.equals("Pending")){
            WHERE_CLAUSE += "(re.reportStatus = 'Investigating1' AND re.reportID=inv.reportID AND inv.firstInvestigationDocPath IS null) OR (re.reportStatus = 'Investigating2' AND re.reportID=inv.reportID AND inv.secondInvestigationDocPath IS null)";
        }else{
            WHERE_CLAUSE += "(re.reportStatus = 'Investigating1' AND re.reportID=inv.reportID AND inv.firstInvestigationDocPath IS NOT null) OR (re.reportStatus = 'Examining' )";
        }

        String query = "SELECT DISTINCT re.*  FROM reportFromUser re, reportInvestigation inv " +
                "WHERE re.reportInvestigationTeam=?  AND re.reportID LIKE '%"+ searchReportID +"%' AND" +
                "( "+ WHERE_CLAUSE  +" )" +
                "ORDER BY re.reportDate, re.reportTime";

        System.out.println("query examiner check: " + query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(investigationTeamID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getReportByReportHandler(String reportHandler, String currentTab, String searchKey) {
        SQLiteDatabase db = getReadableDatabase();

        String WHERE_CLAUSE = "";
        if(currentTab.equals("Pending")){
            //WHERE_CLAUSE += "(re.reportStatus = 'Investigating1' AND re.reportID=inv.reportID AND inv.firstInvestigationDocPath IS NOT null) OR (re.reportStatus = 'Examining' )";
            WHERE_CLAUSE += "(reportStatus='Resolving')";
        }
        else if(currentTab.equals("Examining")){
            WHERE_CLAUSE += "(reportStatus='Examining') OR (reportStatus='Investigating2')";
        }
        else{
            WHERE_CLAUSE += "(reportStatus='Resolved')";
        }

        String query = "SELECT *  FROM reportFromUser " +
                "WHERE reportHandler=?  AND reportID LIKE '%"+ searchKey +"%' AND " +
                "( "+ WHERE_CLAUSE  +" )" +
                "ORDER BY reportDate, reportTime";

        System.out.println("query reportHandler check: " + query);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(reportHandler)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getReportInfoByReportID(String reportID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REPORT_FROM_USER + " WHERE reportID=?", new String[]{String.valueOf(reportID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getReportLocationByReportID(String reportID){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REPORT_LOCATION + " WHERE reportID=?", new String[]{String.valueOf(reportID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getImageByReportID(String reportID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT reportImageFilePath FROM " + TABLE_REPORT_FROM_USER_IMAGE + " WHERE reportID=?", new String[]{String.valueOf(reportID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getInvestigationDocByReportID(String reportID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT inv.* FROM " + TABLE_REPORT_FROM_USER + " re, "+ TABLE_REPORT_INVESTIGATION +" inv WHERE re.reportID=? AND re.reportID=inv.reportID", new String[]{String.valueOf(reportID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getAvailableReportHandlerByOrgID(String orgID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user.userID, employOrg.* FROM " + TABLE_USER + " user, " + TABLE_EMPLOYEE_ORGANIZATION + " employOrg WHERE user.userType='RH' AND employOrg.orgID=? AND employOrg.userID=user.userID", new String[]{orgID});
        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getPollutionResolvingDocByReportID(String reportID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT clean.* FROM " + TABLE_REPORT_FROM_USER + " re, "+ TABLE_REPORT_CLEANING_PROCESS +" clean WHERE re.reportID=? AND re.reportID=clean.reportID", new String[]{String.valueOf(reportID)});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public String getNewsID(String newsDate, String newsTime, String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT newsID FROM " + TABLE_NEWS + " WHERE newsDate=? AND newsTime=? AND userID=?", new String[]{newsDate, newsTime, userID});

        return (cursor.moveToFirst()) ? cursor.getString(cursor.getColumnIndex("newsID")) : "";
    }

    public Cursor getAllNews() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NEWS + " ORDER BY newsDate DESC, newsTime DESC", new String[]{});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getNewsByUserID(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NEWS +" WHERE userID=? ORDER BY newsDate DESC, newsTime DESC", new String[]{userID});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getImageByNewsID(String newsID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NEWS_IMAGE +" WHERE newsID=?", new String[]{newsID});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getNewInfoByNewsID(String newsID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NEWS +" WHERE newsID=?", new String[]{newsID});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getAllUser(String searchUserKey, String filter) {
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = "";
        if(filter.equals("Home User")){
            whereClause = " userType='NA'";
        }
        else if(filter.equals("System Admin")){
            whereClause = " userType='SAD'";
        }
        else{
            whereClause = " userType='NA' OR userType='SAD'";
        }

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " " +
                "WHERE (" + whereClause + ") AND (userID LIKE '%"+ searchUserKey +"%' OR fName LIKE '%"+ searchUserKey + "%' OR lName LIKE '%"+ searchUserKey + "%')", null);

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getEmployeesByOrgID(String orgID, String searchUserKey, String filter) {
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = "";
        if(filter.equals("Organization Admin")){
            whereClause = " userType='AD'";
        }
        else if(filter.equals("Examiner")){
            whereClause = " userType='EX'";
        }
        else if(filter.equals("Investigator")){
            whereClause = " userType='IN'";
        }
        else if(filter.equals("Report Handler")){
            whereClause = " userType='RH'";
        }
        else{
            whereClause = " userType='AD' OR userType='EX' OR userType='IN' OR userType='RH'";
        }

        Cursor cursor = db.rawQuery("SELECT user.*, em.orgID FROM " + TABLE_USER + " user, " + TABLE_EMPLOYEE_ORGANIZATION + " em " +
                "WHERE em.userID=user.userID " +
                "AND em.orgID=? " +
                "AND (em.userID LIKE '%" + searchUserKey + "%' OR user.fName LIKE '%" + searchUserKey + "%' OR user.lName LIKE '%" + searchUserKey + "%') " +
                "AND (" + whereClause + ")", new String[]{orgID});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getUserAddress(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_ADDRESS +" WHERE addressUserID=?", new String[]{userID});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getEmployeesByOrgIDAndUserType(String orgID, String userType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT em.* FROM " + TABLE_USER + " user, "+ TABLE_EMPLOYEE_ORGANIZATION + " em WHERE user.userID=em.userID AND em.orgID=? AND user.userType=?", new String[]{orgID, userType});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getInvestigatorTeamInfoByOrgID(String orgID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INVESTIGATION_TEAM + " WHERE investigationTeamOrgID=? ", new String[]{orgID});

        return (cursor.moveToFirst()) ? cursor : null;
    }

    public Cursor getAllOrg(String searchOrgKey) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORGANIZATION + " WHERE orgID LIKE '%" + searchOrgKey + "%' OR orgName LIKE '%" + searchOrgKey + "%'", null);

        return (cursor.moveToFirst()) ? cursor : null;
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

    public boolean deleteUser(String userID) {
        SQLiteDatabase db = getWritableDatabase();

        try{
            db.delete(TABLE_USER_ADDRESS,  "addressUserID=?", new String[]{userID});
            db.delete(TABLE_USER_UBIDOTS_CREDENTIALS,  "ubidotsUserID=?", new String[]{userID});

            Cursor cursorUserReport = getMyReport(userID);
            if (cursorUserReport != null){
                ArrayList<String> userReport = new ArrayList<>();
                for (int i = 0; i < cursorUserReport.getCount(); i++){
                    userReport.add(cursorUserReport.getString(cursorUserReport.getColumnIndex("reportID")));
                    cursorUserReport.moveToNext();
                }
                deleteReport(userReport);
            }

            db.delete(TABLE_USER,  "userID=?", new String[]{userID});

            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean deleteEmployee(String userID){
        SQLiteDatabase db = getWritableDatabase();

        try{
            db.delete(TABLE_USER_ADDRESS,  "addressUserID=?", new String[]{userID});
            db.delete(TABLE_EMPLOYEE_ORGANIZATION, "userID=?", new String[]{userID});
            db.delete(TABLE_USER,  "userID=?", new String[]{userID});

            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean deleteOrg(String orgID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_ORGANIZATION, "orgID=?", new String[]{orgID}) == 1;
    }

    public boolean deleteReport(ArrayList reportIDSelected) {
        SQLiteDatabase db = getWritableDatabase();

        try{
            for(int i = 0 ; i < reportIDSelected.size();i++)
            {
                db.delete(TABLE_REPORT_CLEANING_PROCESS,"reportID=?",new String[]{reportIDSelected.get(i).toString()});
                db.delete(TABLE_REPORT_FROM_USER_IMAGE,"reportID=?",new String[]{reportIDSelected.get(i).toString()});
                db.delete(TABLE_REPORT_INVESTIGATION,"reportID=?",new String[]{reportIDSelected.get(i).toString()});
                db.delete(TABLE_REPORT_LOCATION,"reportID=?",new String[]{reportIDSelected.get(i).toString()});
                db.delete(TABLE_REPORT_FROM_USER,"reportID=?",new String[]{reportIDSelected.get(i).toString()});
            }
            return true;
        }catch(Exception e){
            System.out.println("ERROR IN DELETING REPORT: " + e.toString());
            return false;
        }
    }

    public boolean deleteNews(String newsID) {
        SQLiteDatabase db = getWritableDatabase();
        Boolean deleted = false;

        try{
            db.delete(TABLE_NEWS, "newsID=?", new String[]{newsID});
            db.delete(TABLE_NEWS_IMAGE, "newsID=?", new String[]{newsID});
            deleted = true;
        }catch(Exception e){

        }

        return deleted;
    }

    public boolean updateOrgInfo(String orgID, String orgName, String orgAddressLine, String orgPostcode, String orgCity, String orgState, String orgReady) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("orgName", orgName);
        conVal.put("orgAddressLine", orgAddressLine);
        conVal.put("orgPostCode", orgPostcode);
        conVal.put("orgCity", orgCity);
        conVal.put("orgState", orgState);
        conVal.put("orgReady", orgReady);

        return db.update(TABLE_ORGANIZATION, conVal, "orgID=?", new String[]{orgID}) == 1;
    }

    public boolean updateOrgReady(String orgID, String orgReady){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("orgReady", orgReady);

        return db.update(TABLE_ORGANIZATION, conVal, "orgID=?", new String[]{orgID}) == 1;
    }

    public boolean updateUserInfo(String fName, String lName, String userEmail, String phoneNo, String addressLine, String postcode, String city, String state, String userID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("userEmail", userEmail);
        conVal.put("fName", fName);
        conVal.put("lName", lName);
        conVal.put("phoneNo", phoneNo);

        ContentValues conValAddress = new ContentValues();

        conValAddress.put("addressLine", addressLine);
        conValAddress.put("postcode", postcode);
        conValAddress.put("city", city);
        conValAddress.put("state", state);

        try{
            db.update(TABLE_USER, conVal, "userID=?", new String[]{userID});
            db.update(TABLE_USER_ADDRESS, conValAddress, "addressUserID=?", new String[]{userID});
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean updateReportStatusByReportID(String reportID, String updatedStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("reportStatus", updatedStatus);

        return db.update(TABLE_REPORT_FROM_USER, conVal, "reportID=?", new String[]{reportID}) == 1;
    }

    public boolean updateReportExaminerByReportID(String reportID, String selectedExaminerID){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("examiner", selectedExaminerID);

        return db.update(TABLE_REPORT_FROM_USER, conVal, "reportID=?", new String[]{reportID}) == 1;
    }

    public boolean updateReportInvestigationTeamByReportID(String reportID, String selectedInvestigationTeamID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("reportInvestigationTeam", selectedInvestigationTeamID);

        return db.update(TABLE_REPORT_FROM_USER, conVal, "reportID=?", new String[]{reportID}) == 1;
    }

    public boolean updateReportHandlerByReportID(String reportID, String selectedReportHandler) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("reportHandler", selectedReportHandler);

        return db.update(TABLE_REPORT_FROM_USER, conVal, "reportID=?", new String[]{reportID}) == 1;
    }

    public boolean updateFirstInvestigationDoc(String fileName, String reportID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("firstInvestigationDocPath", fileName);

        return db.update(TABLE_REPORT_INVESTIGATION, conVal, "reportID=?", new String[]{reportID}) == 1;
    }

    public boolean updateSecondInvestigationDoc(String fileName, String reportID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("secondInvestigationDocPath", fileName);

        return db.update(TABLE_REPORT_INVESTIGATION, conVal, "reportID=?", new String[]{reportID}) == 1;
    }

    public boolean updatePollutionResolvingDoc(String fileName, String reportID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("pollutionCleaningProcDocPath", fileName);

        return db.update(TABLE_REPORT_CLEANING_PROCESS, conVal, "reportID=?", new String[]{reportID}) == 1;
    }

    public boolean updateReportDurationAndCauseByReportID(String reportEstimatedSolveDuration, String reportPollutionCause, String reportID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues conVal = new ContentValues();

        conVal.put("reportEstimatedSolveDuration", reportEstimatedSolveDuration);
        conVal.put("reportPollutionCause", reportPollutionCause);

        return db.update(TABLE_REPORT_FROM_USER, conVal, "reportID=?", new String[]{reportID}) == 1;
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
