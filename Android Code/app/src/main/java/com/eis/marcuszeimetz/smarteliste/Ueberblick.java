package com.eis.marcuszeimetz.smarteliste;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ueberblick extends AppCompatActivity {



    public static final String PREF_NAME = "AppPref";
    SharedPreferences mPrefs;
    private Request request;
    private ArrayList<HashMap<String, String>> list;
    private ArrayList list2;
    ListView mDrawerList;
    JSONObject arr [] = new JSONObject[30];
    String gmid, cookie, user_id;
    public static final String FIRST_COLUMN_Route="route";
    public static final String SECOND_COLUMN_Date="date";
    public static final String THIRD_COLUMN_Trip_Id="trip_id";
    public static final String FOUR_COLUMN_ROLE="role";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ueberblick_menue, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id==android.R.id.home){
            Intent profactivity = new Intent(Ueberblick.this,MainActivity.class);
            startActivity(profactivity);
        } else if (id == R.id.today) {
            Intent profactivity = new Intent(Ueberblick.this,MainActivity.class);
            startActivity(profactivity);

        } else if( id == R.id.profil) {
            Intent profactivity = new Intent(Ueberblick.this,profil.class);
            startActivity(profactivity);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ueberblick);

        mPrefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         cookie = mPrefs.getString("cookie","");
         user_id = mPrefs.getString("user_id","");

            SystemRequirementsChecker.checkWithDefaultDialogs(this);
            createelements();
            setActionBar();


            getReportCards(user_id,cookie);



    }

    @Override
    protected void onResume() {
        super.onResume();
        //setBeaconManager();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        Log.d("TestCallback","Wieder zurück in der Main!");

        //Implementierung der Auflistung der heutigen Praktikas

//        Log.d("TestCallback","Aufruf der ReportCard Funktion");
  //      getTodayReportCards(user_id,cookie);
    //    Log.d("TestCallback","Ende der Funktion");





    }





    public void createelements() {


    mDrawerList = (ListView)findViewById(R.id.navList);


    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Überblick");
        //actionBar.setLogo(R.mipmap.thkoelnlogo);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
       // actionBar.setLogo(R.mipmap.thkoelnlogo);
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setTitle(gmid.toUpperCase());
        //actionBar.hide();s
    }

    public void getReportCards(String user_id,String cookie){
        request = new Request();

        Log.d("TestCallback","In der Funktion");



        Map<String, String> params = new HashMap<>();
        params.put("Cookie",cookie );
        //params.put("Content-Type", "application/vnd.fhk.reportCardEntry.V1+json");



        request.getJsonArray(getApplicationContext(), params, "/atomic/reportCardEntries/student/"+user_id, new Request.VolleyCallback() {



            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                Log.d("TestCallback","Success");
            }


            @Override
            public void onError(JSONObject result) {

            }

            @Override
            public void onSucces(JSONArray result) {





                list = new ArrayList<HashMap<String, String>>();


                JSONArray jbo = result;





                for (int i = 0; i<jbo.length(); i++) {



                    HashMap < String, String> temp = new HashMap<String,String>();

                    try {

                        String st = jbo.getJSONObject(i).getString("start");
                        String et = jbo.getJSONObject(i).getString("end");



                        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm");


                        Time StartTime = new Time(sdft.parse(st).getTime());
                        Time EndTime = new java.sql.Time(sdft.parse(et).getTime());


                        arr[i] = jbo.getJSONObject(i);
                            temp.put(FIRST_COLUMN_Route, arr[i].getJSONObject("labwork").getString("label"));
                            temp.put(SECOND_COLUMN_Date, arr[i].getString("label"));
                            temp.put(FOUR_COLUMN_ROLE, StartTime +" - " + EndTime + " Uhr");
                            list.add(temp);






                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                //SortedSet<String> values = new TreeSet<String>((Comparator<? super String>) list);

                //Collections.sort (list, new MyMapComporator ());
                ListViewAdapter adapter = new ListViewAdapter(Ueberblick.this, list, 1);
                mDrawerList.setAdapter(adapter);



            }

        });
    }
















}
