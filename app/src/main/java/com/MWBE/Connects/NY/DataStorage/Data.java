package com.MWBE.Connects.NY.DataStorage;

import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
import com.MWBE.Connects.NY.JavaBeen.ListData_RFP;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fazal on 6/16/2016.
 */
public class Data {

    public static ArrayList<ListData_RFP> listData_rfp = new ArrayList<>();
    public static boolean AllNYC;

    public static ArrayList<ListData_Agency> list_geographic;

    public static int count_geographic;

    public static boolean DateExpire;
    public static boolean istrial;

    public ArrayList<ListData_RFP> getListData_rfp() {
        return listData_rfp;
    }

    public static ArrayList<Integer> SettingTypeID_capab = new ArrayList<>();

    public static String search = "";

    public static boolean isCompleted;

    public static ArrayList<Integer> procid = new ArrayList<>();

    public static String agency;

    public static boolean isOpen;

    public static int selectioninput = 0;

    public static ArrayList<String> tagidlist = new ArrayList<>();

    public static ArrayList<String> tagidlist_db = new ArrayList<>();

    public static ArrayList<String> tagidlistcertification = new ArrayList<>();

    public static ArrayList<String> tagidlistcertification_db = new ArrayList<>();

    public static ArrayList<String> tagidlistcontractvalue = new ArrayList<>();

    public static ArrayList<String> tagidlistcontractvalue_db = new ArrayList<>();

    public static ArrayList<Boolean> list_check = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_adv = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_arc = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_cons = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_envoirnmental = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_solidstate = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_facility = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_safety = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_it = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_hservices = new ArrayList<>();

    public static ArrayList<Boolean> list_check_capabilities_others = new ArrayList<>();

    public static ArrayList<Boolean> list_check_target_contract = new ArrayList<>();

    public static ArrayList<Boolean> list_check_certification = new ArrayList<>();

    public static ArrayList<Integer> poss_array = new ArrayList<>();

    public static ArrayList<Integer> poss_array_target = new ArrayList<>();

    public static ArrayList<Integer> poss_array_certification = new ArrayList<>();

    public static void clearSearch(){
        search = "";
    }


    public static ArrayList<Integer> ActualID_list_advertising = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_construction = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_architectural = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_envoirnmental = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_facilities = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_solidwaste = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_safety = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_it = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_humanservice = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_others = new ArrayList<>();


    public static ArrayList<Integer> ActualID_list_advertising_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_construction_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_architectural_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_envoirnmental_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_facilities_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_solidwaste_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_safety_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_it_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_humanservice_db = new ArrayList<>();

    public static ArrayList<Integer> ActualID_list_others_db = new ArrayList<>();

    public static ArrayList<JSONObject> jsonObject = new ArrayList<>();

    public static ArrayList<Integer> SettingTypeID_capab_search = new ArrayList<>();

    public static int star = 0;

    public static int count_capab = 0;

    public static ArrayList<ListData_RFP> list = new ArrayList<>();

    public static ArrayList<ListData_RFP> Capabilities_list = new ArrayList<>();

    public static ArrayList<ListData_RFP> Contract_list = new ArrayList<>();

    public static ArrayList<ListData_RFP> Geographic_list = new ArrayList<>();

    public static void clearlist(int settingTypeID){
        /*Data.ActualID_list_advertising.clear();
        Data.ActualID_list_architectural.clear();
        Data.ActualID_list_construction.clear();
        Data.ActualID_list_envoirnmental.clear();
        Data.ActualID_list_solidwaste.clear();
        Data.ActualID_list_facilities.clear();
        Data.ActualID_list_safety.clear();
        Data.ActualID_list_it.clear();
        Data.ActualID_list_humanservice.clear();
        Data.ActualID_list_others.clear();*/

        switch (settingTypeID){
            case 100:{
                Data.ActualID_list_advertising.clear();
                break;
            }

            case 200:{
                Data.ActualID_list_architectural.clear();
                break;
            }

            case 300:{
                Data.ActualID_list_construction.clear();
                break;
            }

            case 400:{
                Data.ActualID_list_envoirnmental.clear();
                break;
            }

            case 500:{
                Data.ActualID_list_solidwaste.clear();
                break;
            }

            case 600:{
                Data.ActualID_list_facilities.clear();
                break;
            }

            case 700:{
                Data.ActualID_list_safety.clear();
                break;
            }

            case 800:{
                Data.ActualID_list_it.clear();
                break;
            }

            case 900:{
                Data.ActualID_list_humanservice.clear();
                break;
            }

            case 1000:{
                Data.ActualID_list_others.clear();
                break;
            }


        }
    }

    public static ArrayList<Integer> Actual_id_list_geographic = new ArrayList<>();

    public static ArrayList<Integer> Actual_id_list_target = new ArrayList<>();


    public static void populatelist(){
        for(int i=0;i<5;i++){
            Actual_id_list_geographic.add(i);
        }
    }


}
