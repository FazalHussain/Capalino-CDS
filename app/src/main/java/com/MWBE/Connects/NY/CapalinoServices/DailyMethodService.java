package com.MWBE.Connects.NY.CapalinoServices;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.Database.DatabaseBeen.ProcMaster;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fazal on 5/3/2017.
 */

public class DailyMethodService extends IntentService {
    private Context context = this;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DailyMethodService() {
        super("DailyMethodService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            //Activity activity = (Activity)context;
            Log.d("st", String.valueOf(System.currentTimeMillis()));
            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            HttpClient httpclient = new DefaultHttpClient();
            //utils.getdata("Userid");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            Date date = new Date();
            String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getProcurementDaily.php?currentDate="+simpleDateFormat.format(date);
            link = link.replace(" ","%20");
            HttpPost httppost = new HttpPost(link);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            final String response = httpclient.execute(httppost,
                    responseHandler);


            Log.i("Response", "Response : " + response);
            if (!dataBaseHelper.sqliteDataBase.isOpen())
                dataBaseHelper.openDataBase();
            dataBaseHelper.delete("ProcurementMaster");
            JSONArray jsonarray = new JSONArray(response);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobj = jsonarray.getJSONObject(i);

                String ProcurementID = jsonobj.getString("ProcurementID");
                String ProcurementEPIN = jsonobj.getString("ProcurementEPIN");
                String ProcurementSource = jsonobj.getString("ProcurementSource");
                String ProcurementAgencyID = jsonobj.getString("ProcurementAgencyID");

                        /*ProcurementAgencyID = ProcurementAgencyID.replace(")","");
                        ProcurementAgencyID = ProcurementAgencyID.replace("(","");*/
                ProcurementAgencyID = ProcurementAgencyID.replace("'","\\u0027");

                String ProcurementTypeIDP = jsonobj.getString("ProcurementTypeIDP");
                String ProcurementTitle = jsonobj.getString("ProcurementTitle");
                //Log.d("ProcurementTitle",ProcurementTitle);
/*
                        ProcurementTitle = ProcurementTitle.replace(")","");
                        ProcurementTitle = ProcurementTitle.replace("(","");*/
                ProcurementTitle = ProcurementTitle.replace("'","''");

                String ProcurementShortDescription = jsonobj.getString("ProcurementShortDescription");

                        /*ProcurementShortDescription = ProcurementShortDescription.replace(")","");
                        ProcurementShortDescription = ProcurementShortDescription.replace("(","");*/
                ProcurementShortDescription = ProcurementShortDescription.replace("'","''");


                String ProcurementLongDescription = jsonobj.getString("ProcurementLongDescription");

                        /*ProcurementLongDescription = ProcurementLongDescription.replace(")","");
                        ProcurementLongDescription = ProcurementLongDescription.replace("(","");*/
                ProcurementLongDescription = ProcurementLongDescription.replace("'","''");

                String ProcurementProposalDeadline = jsonobj.getString("ProcurementProposalDeadline");
                String ProcurementPreConferenceDate = jsonobj.getString("ProcurementPreConferenceDate");
                String ProcurementQuestionDeadline = jsonobj.getString("ProcurementQuestionDeadline");
                String ProcurementAgencyURL = jsonobj.getString("ProcurementAgencyURL");

                String ProcurementDocument1URL = jsonobj.getString("ProcurementDocument1URL");
                String ProcurementDocument2URL = jsonobj.getString("ProcurementDocument2URL");
                String ProcurementDocument3URL = jsonobj.getString("ProcurementDocument3URL");
                String ProcurementDocument4URL = jsonobj.getString("ProcurementDocument4URL");
                String ProcurementDocument5URL = jsonobj.getString("ProcurementDocument5URL");
                String ProcurementAddedDate = jsonobj.getString("ProcurementAddedDate");

                String ProcurementContractValueID = jsonobj.getString("ProcurementContractValueID");
                String Status = jsonobj.getString("Status");
                String LASTEDITEDUSERNAME = jsonobj.getString("LASTEDITEDUSERNAME");
                String PDFPath = jsonobj.getString("PDFPath");

                boolean isInserted = dataBaseHelper.InsertProcurementMaster(new ProcMaster(Integer.valueOf(ProcurementID), ProcurementEPIN, ProcurementSource,
                        ProcurementAgencyID, ProcurementTypeIDP, ProcurementTitle,ProcurementShortDescription,ProcurementLongDescription,ProcurementProposalDeadline,
                        ProcurementPreConferenceDate,ProcurementQuestionDeadline,ProcurementAgencyURL,ProcurementDocument1URL,ProcurementDocument2URL,ProcurementDocument3URL,
                        ProcurementDocument4URL,ProcurementDocument5URL,ProcurementAddedDate,ProcurementContractValueID,Status,LASTEDITEDUSERNAME,PDFPath));
                //Log.d("InsertProcurementMaster", "Inserted");

                //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));

                //isinserted = dataBaseHelper.InsertUserProcurmentTracking(been);
            }

            Log.d("et", String.valueOf(System.currentTimeMillis()));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
