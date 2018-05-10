package com.MWBE.Connects.NY.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.MWBE.Connects.NY.Database.DatabaseBeen.TrackingData;
import com.MWBE.Connects.NY.JavaBeen.CapabilitiesMaster;
import com.MWBE.Connects.NY.JavaBeen.TaggedRFP;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DatabaseBeen.ProcMaster;
import com.MWBE.Connects.NY.Database.DatabaseBeen.SettingsModel;
import com.MWBE.Connects.NY.JavaBeen.ContentMasterUpdatedModel;
import com.MWBE.Connects.NY.JavaBeen.ListData_RFP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anas on 3/8/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.MWBE.Connect.NY/databases/";
    // Data Base Name.
    public static final String DATABASE_NAME = "CapalinoDataBase.sqlite";
    // Data Base Version.
    private static final int DATABASE_VERSION = 3;
    // Table Names of Data Base.

    public Context context;
    public static SQLiteDatabase sqliteDataBase;
    private String query;
    private ProgressDialog pb;

    ArrayList<ListData_RFP> list_geog =new ArrayList<>();

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     * Parameters of super() are    1. Context
     *                              2. Data Base Name.
     *                              3. Cursor Factory.
     *                              4. Data Base Version.
     */
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null ,DATABASE_VERSION);
        this.context = context;
        this.DB_PATH = this.context.getDatabasePath(DATABASE_NAME).getAbsolutePath();


    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * By calling this method and empty database will be created into the default system path
     * of your application so we are gonna be able to overwrite that database with our database.
     * */
    public void createDataBase() throws IOException{
        //check if the database exists
        boolean databaseExist = checkDataBase();

        if(databaseExist){
            // Do Nothing.
        }else{
            this.getWritableDatabase();
            copyDataBase();
        }// end if else dbExist
    } // end createDataBase().

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){
        File databaseFile = new File(DB_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring byte stream.
     * */
    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * This method opens the data base connection.
     * First it create the path up till data base of the device.
     * Then create connection with data base.
     */
    public void openDataBase() throws SQLException{
        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        sqliteDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * This Method is used to close the data base connection.
     */
    @Override
    public synchronized void close() {
        if(sqliteDataBase != null)
            sqliteDataBase.close();
        super.close();
    }

    /**
     * Apply your methods and class to fetch data using raw or queries on data base using
     * following demo example code as:
     */
    public Cursor getDataFromDB(String colname,String colvariable,String tablename,boolean iscon){
        if(iscon==true){
            query = "select * From "+tablename+" where "+colname+" = '"+colvariable+"'";
        }else {
            query = "select * From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getDataFromQuery(String query){
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getDataFromDB(String colname,int colvariable,String tablename,boolean iscon){
        if(iscon==true){
            query = "select * From "+tablename+" where "+colname+" = '"+colvariable+"'";
        }else {
            query = "select * From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getDataFromDBAgency(boolean isorderby){
        if(isorderby){
            query = "SELECT Distinct(ProcurementAgencyID) From ProcurementMaster Order by ProcurementAgencyID";
        }

        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }


    public Cursor getDataFromDB(String colname,String colvariable,String tablename,boolean iscon,boolean isorderby){
        if(iscon==true){
            query = "select * From "+tablename+" where "+colname+" = '"+colvariable+"'";
        }else {
            query = "select * From " + tablename;
        }

        if(isorderby){
            query = "select * From " + tablename+" order by TagTitle ASC";
        }

        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getDataFromDB(String colname,String colvariable,String tablename,boolean iscon,boolean isorderby, String orderbywith){
        if(iscon==true){
            query = "select * From "+tablename+" where "+colname+" = '"+colvariable+"'";
        }else {
            query = "select * From " + tablename;
        }

        if(isorderby){
            //query = "select * From " + tablename+" order by strftime(ContentPostedDate) DESC";
            query = "select * From " + tablename;
        }

        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getTrackedData(String colname,String trackid,String tablename,boolean iscon){
        if(iscon==true){
            query = "select * From "+tablename+" where "+colname+" = '"+trackid+"'";
        }else {
            query = "select * From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getTrackedData(String userid,boolean iscon){
        if(iscon==true){
            query = "select PM.ProcurementTitle,PM.ProcurementAgencyID,TM.TrackDate,TM.Rating,PM.ProcurementProposalDeadline," +
                    "TM.UserID,TM.ProcurementID from ProcurementMaster PM , TrackListing TM where TM.UserID='"+userid+"' " +
                    "AND PM.ProcurementID=TM.ProcurementID";
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getDataFromDB1(String colname,String colvariable,String tablename,boolean iscon){
        if(iscon==true){
            query = "select * From "+tablename+" where "+colname+" LIKE  %"+colvariable+"%";
        }else {
            query = "select * From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor DBRecord(String query){
        Cursor cursor = sqliteDataBase.rawQuery(query, null);
        return cursor;
    }

    public Cursor getEmailFromDB(String colname,String colvariable,String tablename,boolean iscon){
        if(iscon==true){
            query = "select DISTINCT Email From "+tablename+" where "+colname+" = '"+colvariable+"'";
        }else {
            query = "select DISTINCT Email From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getCertificationCity(String tablename,boolean iscon){
        if(iscon==true){
            query = "select * From "+tablename+" where ID < 10";
        }else {
            query = "select * From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getCertificationState(String tablename,boolean iscon){
        if(iscon==true){
            query = "select * From "+tablename+" where ID >= 10 ";
        }else {
            query = "select * From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public boolean UpdateContentMasterLastUpdateDate(String date,int contentid){
       try{
           query = "update ContentMaster set LastUpdate = '"+date+"' where ContentID = "+contentid;
           Cursor cursor = sqliteDataBase.rawQuery(query,null);
           cursor.moveToFirst();
           cursor.close();
           return true;
           }catch (Exception e){
               e.printStackTrace();
           return false;
           }
    }

    public Cursor getDataFromProcurementMaster(String contractvalue,String agency,String settingTypeID,String keyword){
        /*if(keyword.length()>0) {
            query = "select * From ProcurementMaster join ContractValueTags on ProcurementMaster.Maxvalue <= ContractValueTags.MaxValue AND " +
                    "ProcurementMaster.MinValue >=  ContractValueTags.Minvalue where ContractValueTags.TagTitle='" + tagtitle + "' " +
                    "OR ProcurementMaster.ProcurementAgencyID='" + agency + "' OR ProcurementMaster.ProcurementTitle LIKE '%" + keyword + "%' OR " +
                    "ProcurementMaster.ProcurementShortDescription LIKE '%" + keyword + "%' OR ProcurementMaster.ProcurementLongDescription LIKE '%" + keyword + "%'";
        }else {
            query = "select * From ProcurementMaster join ContractValueTags on ProcurementMaster.Maxvalue <= ContractValueTags.MaxValue AND " +
                    "ProcurementMaster.MinValue >=  ContractValueTags.Minvalue where ContractValueTags.TagTitle='" + tagtitle + "' " +
                    "OR ProcurementMaster.ProcurementAgencyID='" + agency + "'";
        }*/




        /*query = "select distinct ProcurementMaster.ProcurementID,ProcurementMaster.ProcurementAgencyID, " +
                "ProcurementMaster.ProcurementTitle,ProcurementMaster.ProcurementProposalDeadline,ProcurementMaster.ProcurementAddedDate, " +
                "ProcurementMaster.ProcurementLongDescription from ProcurementMaster JOIN ProcurementRFPPreferences where " +
                "ProcurementMaster.ProcurementTitle LIKE '%"+keyword+"%' OR ProcurementMaster.ProcurementAgencyID ='"+agency+"' OR " +
                "ProcurementMaster.ProcurementShortDescription LIKE '%"+keyword+"%' OR ProcurementMaster.ProcurementLongDescription LIKE '%"+keyword+"%' " +
                "OR ProcurementRFPPreferences.ProcurementID=ProcurementMaster.ProcurementID AND ProcurementRFPPreferences.SettingTypeID='2' " +
                "AND ProcurementRFPPreferences.ActualTagID='"+contractvalue+"' OR ProcurementRFPPreferences.SettingTypeID='"+settingTypeID+"'";*/

     /*   query = "select Distinct(ProcurementMaster.ProcurementID),ProcurementMaster.ProcurementAgencyID," +
                "ProcurementMaster.ProcurementTitle,ProcurementMaster.ProcurementProposalDeadline,ProcurementMaster.ProcurementAddedDate," +
                "ProcurementMaster.ProcurementLongDescription,ProcurementMaster.PdfPath from ProcurementMaster JOIN " +
                "ProcurementRFPPreferences ON ProcurementMaster.ProcurementID=ProcurementRFPPreferences.ProcurementID where " +
                "ProcurementMaster.ProcurementTitle LIKE '%"+keyword+"%' OR ProcurementMaster.ProcurementAgencyID='"+agency+"' OR " +
                "ProcurementMaster.ProcurementShortDescription LIKE '%"+keyword+"%' OR ProcurementMaster.ProcurementLongDescription LIKE '%"+keyword+"%' " +
                "OR ProcurementRFPPreferences.SettingTypeID='"+settingTypeID+"' OR ProcurementRFPPreferences.SettingTypeID='2' AND " +
                "ProcurementRFPPreferences.ActualTagID='"+contractvalue+"'";
*/

        if(Data.search.length()>0)
        {
            Data.search = "%"+Data.search+"%";
        }

            query="select Distinct(ProcurementMaster.ProcurementID),ProcurementMaster.ProcurementAgencyID,ProcurementMaster.ProcurementTitle,ProcurementMaster.ProcurementProposalDeadline,ProcurementMaster.ProcurementAddedDate,ProcurementMaster.ProcurementLongDescription,ProcurementMaster.PdfPath from ProcurementMaster  JOIN ProcurementRFPPreferences ON ProcurementMaster.ProcurementID=ProcurementRFPPreferences.ProcurementID where ProcurementMaster.ProcurementTitle LIKE '"+Data.search+"' OR ProcurementMaster.ProcurementAgencyID='"+agency+"' OR ProcurementMaster.ProcurementShortDescription LIKE '"+Data.search+"' OR ProcurementMaster.ProcurementLongDescription LIKE '"+Data.search+"' OR  ProcurementRFPPreferences.SettingTypeID='"+settingTypeID+"' OR ProcurementRFPPreferences.SettingTypeID='2' AND ProcurementRFPPreferences.ActualTagID='"+contractvalue+"' ORDER BY ProcurementMaster.ProcurementID DESC";
            Log.d("query",query);
            Data.clearSearch();
            Cursor cursor = sqliteDataBase.rawQuery(query, null);
            return cursor;
    }

    void showPB(final String message) {

        Activity activity = (Activity) context;

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(context);
                pb.setMessage(message);
                pb.setCancelable(false);
                pb.show();
            }
        });

    }

    void hidePB() {
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pb != null && pb.isShowing())
                    pb.dismiss();
            }
        });

    }

    public Cursor getDataFromProcurementMaster(int settingtypeid){
        Data.star = 0;
        try {
            if(!DataBaseHelper.sqliteDataBase.isOpen()) {
                openDataBase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
           /* query = "select Distinct PM.ProcurementTitle,PM.ProcurementAgencyID,PM.ProcurementAddedDate,PM.ProcurementProposalDeadline,PM.ProcurementID from ProcurementMaster PM JOIN ProcurementRFPPreferences RFP ON " +
                    "PM.ProcurementID=RFP.ProcurementID where RFP.SettingTypeID="+settingtypeid;*/

            query = "select Distinct PM.ProcurementTitle,PM.ProcurementAgencyID,PM.ProcurementProposalDeadline," +
                    "PM.ProcurementAddedDate,PM.ProcurementID,PM.ProcurementLongDescription,PM.PDFPath," +
                    "PM.ProcurementAgencyURL from ProcurementMaster PM JOIN ProcurementRFPPreferences RFP " +
                    "ON PM.ProcurementID=RFP.ProcurementID where RFP.SettingTypeID=" + settingtypeid;

        Cursor cursor = sqliteDataBase.rawQuery(query, null);
        try {
            if (cursor.getCount() > 0) {
                Data.star = 1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //cursor.close();
        return cursor;
    }

    public Cursor getDataFromProcurementMasterByProcurementID(int ProcurementID){
        Data.star = 0;
        try {
            if(!DataBaseHelper.sqliteDataBase.isOpen()) {
                openDataBase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
           /* query = "select Distinct PM.ProcurementTitle,PM.ProcurementAgencyID,PM.ProcurementAddedDate,PM.ProcurementProposalDeadline,PM.ProcurementID from ProcurementMaster PM JOIN ProcurementRFPPreferences RFP ON " +
                    "PM.ProcurementID=RFP.ProcurementID where RFP.SettingTypeID="+settingtypeid;*/

            query = "select Distinct PM.ProcurementTitle,PM.ProcurementAgencyID,PM.ProcurementProposalDeadline," +
                    "PM.ProcurementAddedDate,PM.ProcurementID,PM.ProcurementLongDescription,PM.PDFPath," +
                    "PM.ProcurementAgencyURL from ProcurementMaster PM JOIN ProcurementRFPPreferences RFP " +
                    "ON PM.ProcurementID=RFP.ProcurementID where RFP.ProcurementID = " +
                    ProcurementID;


        Cursor cursor = sqliteDataBase.rawQuery(query, null);
        try {

        }catch (Exception e){
            e.printStackTrace();
        }

        //cursor.close();
        return cursor;
    }


    public Cursor getDataFromProcurementMasterStar2(final int Actualid, int settingTypeID) {
        final ArrayList<ListData_RFP> Geographic_list = new ArrayList<>();
        final ArrayList<ListData_RFP> geographiclist = new ArrayList<ListData_RFP>();// unique
        /*try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        query = "select Distinct PM.ProcurementTitle,PM.ProcurementAgencyID,PM.ProcurementProposalDeadline," +
                "PM.ProcurementAddedDate,PM.ProcurementID,PM.ProcurementLongDescription,PM.PDFPath," +
                "PM.ProcurementAgencyURL from ProcurementMaster PM JOIN ProcurementRFPPreferences RFP " +
                "ON PM.ProcurementID=RFP.ProcurementID where RFP.SettingTypeID=" + settingTypeID +
                " AND RFP.ActualTagID=" + Actualid;


        Cursor cursorstar2 = sqliteDataBase.rawQuery(query, null);
        try {
            if (cursorstar2.getCount() > 0) {
                return cursorstar2;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //Log.d("2starquery","Success size"+ cursorstar2.getCount());
        return null;

    }

    public Cursor getDataFromProcurementMasterStar2ByProcurementID(final int Actualid, int settingTypeID) {
        final ArrayList<ListData_RFP> Geographic_list = new ArrayList<>();
        final ArrayList<ListData_RFP> geographiclist = new ArrayList<ListData_RFP>();// unique
        /*try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        query = "select Distinct PM.ProcurementTitle,PM.ProcurementAgencyID,PM.ProcurementProposalDeadline," +
                "PM.ProcurementAddedDate,PM.ProcurementID,PM.ProcurementLongDescription,PM.PDFPath," +
                "PM.ProcurementAgencyURL from ProcurementMaster PM JOIN ProcurementRFPPreferences RFP " +
                "ON PM.ProcurementID=RFP.ProcurementID where RFP.SettingTypeID=" + settingTypeID +
                " AND RFP.ActualTagID=" + Actualid;


        Cursor cursorstar2 = sqliteDataBase.rawQuery(query, null);
        try {
            if (cursorstar2.getCount() > 0) {
                return cursorstar2;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //Log.d("2starquery","Success size"+ cursorstar2.getCount());
        return null;

    }

    public Cursor getDataFromProcurementMasterStar3(final int Actualid, int settingTypeID) {
        final ArrayList<ListData_RFP> Geographic_list = new ArrayList<>();
        final ArrayList<ListData_RFP> geographiclist = new ArrayList<ListData_RFP>();// unique
        /*try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        query = "select Distinct PM.ProcurementTitle,PM.ProcurementAgencyID,PM.ProcurementProposalDeadline," +
                "PM.ProcurementAddedDate,PM.ProcurementID,PM.ProcurementLongDescription,PM.PDFPath," +
                "PM.ProcurementAgencyURL from ProcurementMaster PM JOIN ProcurementRFPPreferences RFP " +
                "ON PM.ProcurementID=RFP.ProcurementID where RFP.SettingTypeID=" + settingTypeID +
                " AND RFP.ActualTagID=" + Actualid;


        Cursor cursorstar2 = sqliteDataBase.rawQuery(query, null);
        try {
            if (cursorstar2.getCount() > 0) {
                return cursorstar2;

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public Cursor getDataFromProcurementMasterStar2_(final int Actualid, int ProcurementID) {
        final ArrayList<ListData_RFP> Geographic_list = new ArrayList<>();
        final ArrayList<ListData_RFP> geographiclist = new ArrayList<ListData_RFP>();// unique
        /*try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        query = "select Distinct(PM.ProcurementTitle),PM.ProcurementAgencyID,PM.ProcurementProposalDeadline,PM.ProcurementAddedDate,PM.ProcurementID from ProcurementMaster PM " +
                "JOIN ProcurementRFPPreferences RFP ON PM.ProcurementID=RFP.ProcurementID where " +
                "RFP.SettingTypeID=1 AND RFP.ActualTagID="+ Actualid +" AND PM.ProcurementID='"+ProcurementID+"'";


        Cursor cursorstar2_ = sqliteDataBase.rawQuery(query, null);
        Log.d("cursor_", cursorstar2_.getCount()+"");
        try {
            if (cursorstar2_.getCount() > 0) {
                return cursorstar2_;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //sqliteDataBase.close();
        //Log.d("2starquery_","Success size"+ cursorstar2_.getCount());
        return null;

    }



    //insert Data
    public boolean InsertUserProcurmentTracking(TrackingData been){
        try {
            query = "insert into TrackListing (ProcurementTitle,AgencyTitle,TrackDate,ProposalDeadLine,UserID,Rating,ProcurementID) " +
                    "Values('"+been.getProcurementTitle()+"','"+been.getAgencyTitle()+"','"+been.getTrackDate()+"'," +
                    "'"+been.getProposalDeadLine()+"','"+been.getUserID()+"','"+been.getRating()+"','"+been.getProcID()+"')";
            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean InsertProcurementMaster(ProcMaster been){
        try {
            query = "insert into ProcurementMaster (ProcurementID,ProcurementEPIN,ProcurementSource,ProcurementAgencyID,ProcurementTypeID,ProcurementTitle," +
                    "ProcurementShortDescription, ProcurementLongDescription,ProcurementProposalDeadline, ProcurementPreConferenceDate, " +
                    "ProcurementQuestionDeadline,ProcurementAgencyURL, ProcurementDocument1URL, ProcurementDocument2URL, ProcurementDocument3URL, " +
                    "ProcurementDocument4URL, ProcurementDocument5URL,ProcurementAddedDate, ProcurementContractValueID, PDFPath, RecordUpdatedDate, Action) " +
                    "Values('"+been.getProcurementID()+"','"+been.getProcurementEPIN()+"','"+been.getProcurementSource()+"'," +
                    "'"+been.getProcurementAgencyID()+"','"+been.getProcurementTypeIDP()+"','"+been.getProcurementTitle()+"','"+been.getProcurementShortDescription()+"'," +
                    "'"+been.getProcurementLongDescription()+"','"+been.getProcurementProposalDeadline()+"','"+been.getProcurementPreConferenceDate()+"'," +
                    "'"+been.getProcurementQuestionDeadline()+"','"+been.getProcurementAgencyURL()+"','"+been.getProcurementDocument1URL()+"','"+been.getProcurementDocument2URL()+"'," +
                    "'"+been.getProcurementDocument3URL()+"','"+been.getProcurementDocument4URL()+"','"+been.getProcurementDocument5URL()+"','"+been.getProcurementAddedDate()+"'," +
                    "'"+been.getProcurementContractValueID()+"','"+been.getPDFPath()+"','"+been.getRecordUpdatedDate()+"','"+been.getAction()+"')";
            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean UpdateProcurementMaster(ProcMaster been){
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put("ProcurementID",been.getProcurementID());
            contentValues.put("ProcurementEPIN",been.getProcurementEPIN());
            contentValues.put("ProcurementSource",been.getProcurementSource());
            contentValues.put("ProcurementAgencyID",been.getProcurementAgencyID());
            contentValues.put("ProcurementTypeID",been.getProcurementTypeIDP());
            contentValues.put("ProcurementTitle",been.getProcurementTitle());
            contentValues.put("ProcurementShortDescription",been.getProcurementShortDescription());
            contentValues.put("ProcurementLongDescription",been.getProcurementLongDescription());
            contentValues.put("ProcurementProposalDeadline", been.getProcurementProposalDeadline());
            contentValues.put("ProcurementPreConferenceDate",been.getProcurementPreConferenceDate());

            contentValues.put("ProcurementQuestionDeadline",been.getProcurementQuestionDeadline());
            contentValues.put("ProcurementAgencyURL",been.getProcurementAgencyURL());
            contentValues.put("ProcurementDocument1URL",been.getProcurementDocument1URL());
            contentValues.put("ProcurementDocument2URL",been.getProcurementDocument2URL());
            contentValues.put("ProcurementDocument3URL",been.getProcurementDocument3URL());
            contentValues.put("ProcurementDocument4URL",been.getProcurementDocument4URL());
            contentValues.put("ProcurementDocument5URL",been.getProcurementDocument5URL());
            contentValues.put("ProcurementAddedDate",been.getProcurementAddedDate());
            contentValues.put("ProcurementContractValueID", been.getProcurementContractValueID());
            contentValues.put("PDFPath",been.getPDFPath());
            contentValues.put("RecordUpdatedDate",been.getRecordUpdatedDate());
            contentValues.put("Action",been.getAction());

            String where = "ProcurementID=?";
            String[] whereArgs = new String[] {String.valueOf(been.getProcurementID())};

            sqliteDataBase.update("ProcurementMaster", contentValues, where, whereArgs);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("err",e.getMessage());
            return false;
        }
    }

    public boolean InsertSettingsMaster(SettingsModel been){
        try {
            query = "Insert into SettingsMaster (GeographicCoverage,ContractValue,Certification,Capabilities) " +
                    "Values('"+been.getGeographicCoverage()+"','"+been.getContractValue()+"'," +
                    "'"+been.getCertification()+"','"+been.getCapabilities()+"')";
            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean InsertEmailList(String email){
        try {
            query = "Insert into AutoCompleteEmailList (Email) " +
                    "Values('"+email+"')";
            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean InsertContentMaster(ContentMasterUpdatedModel been){
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put("ContentID",been.getContentID());
            contentValues.put("ContentTitle",been.getContentTitle());
            contentValues.put("ContentDescription",been.getContentDescription());
            contentValues.put("EventStartDateTime",been.getEventStartDateTime());
            contentValues.put("EventEndDateTime",been.getEventEndDateTime());
            contentValues.put("EventLocation",been.getEventLocation());
            contentValues.put("EventCost",been.getEventCost());
            contentValues.put("ReferenceURL",been.getReferenceURL());
            contentValues.put("ContentPostedDate",been.getContentPostedDate());
            contentValues.put("LASTUPDATE",been.getLASTUPDATE());
            contentValues.put("ContentType", been.getContentType());
            contentValues.put("ContentStatus",been.getContentStatus());
            contentValues.put("Action",been.getAction());

            sqliteDataBase.insert("ContentMasterUpdated", null, contentValues);


            Log.d("Title",been.getContentTitle());

            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("err",e.getMessage());
            return false;
        }
    }

    public boolean UpdateContentMaster(ContentMasterUpdatedModel been){
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put("ContentTitle",been.getContentTitle());
            contentValues.put("ContentDescription",been.getContentDescription());
            contentValues.put("EventStartDateTime",been.getEventStartDateTime());
            contentValues.put("EventEndDateTime",been.getEventEndDateTime());
            contentValues.put("EventCost",been.getEventCost());
            contentValues.put("ReferenceURL",been.getReferenceURL());
            contentValues.put("ContentPostedDate",been.getContentPostedDate());
            contentValues.put("LASTUPDATE",been.getLASTUPDATE());
            contentValues.put("ContentType", been.getContentType());
            contentValues.put("ContentStatus",been.getContentStatus());
            contentValues.put("Action",been.getAction());

            String where = "ContentID=?";
            String[] whereArgs = new String[] {String.valueOf(been.getContentID())};

            sqliteDataBase.update("ContentMasterUpdated", contentValues, where, whereArgs);

            Log.d("update",been.getContentTitle());

            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("err",e.getMessage());
            return false;
        }
    }

    public boolean InsertinRFPPrefence(TaggedRFP been){
        try {
            query = "insert or ignore into ProcurementRFPPreferences (PreferenceID,ProcurementID,SettingTypeID,ActualTagID," +
                    "AddedDateTime,LastUpdateDate, Status)" +
                    "Values('"+been.getPreferenceID()+"','"+been.getProcurementID()+"','"+been.getSettingTypeID()+"'," +
                    "'"+been.getActualTagID()+"','"+been.getAddedDateTime()+"'," +
                    "'"+been.getLastupdatedate()+"','"+been.getStatus()+"')";

            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean InsertinCapabilitiesMaster(CapabilitiesMaster been){
        try {
            query = "INSERT INTO CapabilitiesMaster (TagID,CapTagID,TagValueID,TagValueTitle) "+
                    "Values('"+been.getTagID()+"','"+been.getCapTagID()+"','"+been.getTagValueID()+"'," +
                    "'"+been.getTagValueTitle()+"')";

            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int deleteContentMasterRecord(int ContentID){
        String where = "ContentID=?";
        String[] whereArgs = new String[] {String.valueOf(ContentID)};
        int deleted = sqliteDataBase.delete("ContentMasterUpdated", where, whereArgs);
        return deleted;

    }

    public void deleteRFPPrefrence(int prefernceID){
        String where = "PreferenceID=?";
        String[] whereArgs = new String[] {String.valueOf(prefernceID)};

        sqliteDataBase.delete("ProcurementRFPPreferences", where, whereArgs);

        //been.setLoaded(true);
    }

    public void deleteRecord(String title){
        sqliteDataBase.execSQL("delete from TrackListing where ProcurementTitle= "+title);
        sqliteDataBase.close();
    }

    public void delete(String tablename){
        sqliteDataBase.execSQL("delete from "+tablename);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to write the create table query.
        // As we are using Pre built data base.
        // Which is ReadOnly.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No need to write the update table query.
        // As we are using Pre built data base.
        // Which is ReadOnly.
        // We should not update it as requirements of application.
        if(newVersion>oldVersion){
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<ListData_RFP> getList_geog() {
        return list_geog;
    }
}
