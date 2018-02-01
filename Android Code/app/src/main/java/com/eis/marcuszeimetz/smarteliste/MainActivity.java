package com.eis.marcuszeimetz.smarteliste;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private BeaconManager beaconManager;
    public static final String PREF_NAME = "AppPref";
    SharedPreferences mPrefs;
    private Request request;
    private ArrayList<HashMap<String, String>> list;
    ListView mDrawerList;
    JSONObject arr [] = new JSONObject[30];
    String gmid, cookie, user_id;
    public static final String FIRST_COLUMN_Route="route";
    public static final String SECOND_COLUMN_Date="date";
    public static final String THIRD_COLUMN_Trip_Id="trip_id";
    public static final String FOUR_COLUMN_ROLE="role";

    private int i = 0;

    TextView labwork,time,datum,lesson,room,txtPogress;
    public int index = 0;
    private ProgressBar progressBar;
    private int progressStatus = 0;












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menue, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logout) {
            logout();
            return true;

        } else if( id == R.id.profil) {
            Intent profactivity = new Intent(MainActivity.this,profil.class);
            startActivity(profactivity);
        } else if( id == R.id.ueberblick) {
            Intent profactivity = new Intent(MainActivity.this,Ueberblick.class);
            startActivity(profactivity);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {




            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



          mPrefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
          gmid = mPrefs.getString("gmid","");
          cookie = mPrefs.getString("cookie","");
          user_id = mPrefs.getString("user_id","");

            SystemRequirementsChecker.checkWithDefaultDialogs(this);
            createelements();
            setActionBar();


       // getTodayReportCards(user_id,cookie);


        setBeaconManager();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setBeaconManager();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);






    }





    public void createelements() {


    mDrawerList = (ListView)findViewById(R.id.navList);
        datum = (TextView)findViewById(R.id.date);
        time = (TextView)findViewById(R.id.time);
        labwork = (TextView)findViewById(R.id.labwork);
        lesson = (TextView)findViewById(R.id.lesson);
        room = (TextView)findViewById(R.id.room);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtPogress = (TextView) findViewById(R.id.txtPogress);


    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Today");
        //actionBar.setLogo(R.mipmap.thkoelnlogo);

        actionBar.setDisplayShowHomeEnabled(true);
       // actionBar.setLogo(R.mipmap.thkoelnlogo);
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setTitle(gmid.toUpperCase());
        //actionBar.hide();s
        //actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purpel)));
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, LoginClass.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{notifyIntent}, PendingIntent.FLAG_IMMUTABLE);



        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();







        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m, notification);


    }




    public void setBeaconManager(){

        beaconManager = new BeaconManager(getApplicationContext());

        // Set the monitoring settings.
        beaconManager.setBackgroundScanPeriod(1000, 0);

        // Set the exit expiration to it's min (between 1000 and 60000 in ms).
        //beaconManager.setRegionExitExpiration(1000);






        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {



                i = 1;

                int Major = beacons.get(0).getMajor();
                int Minor = beacons.get(0).getMinor();


                //Beacon Symbolisiert den CGA Praktikumsraum
                if (Major == 51635 &&  Minor == 14196) {
                    checkTodayReportCards(user_id,cookie,"CGA");




                //Beacon Symbolisiert das KTDS-Labor
                } else if (Major == 46887 &&  Minor == 11165) {
                    checkTodayReportCards(user_id,cookie,"KTN-MI");

                }

            }


            @Override
            public void onExitedRegion(BeaconRegion region) {

                 i = 0;

                time.setText("...");
                datum.setText("...");
                labwork.setText("...");
                lesson.setText("...");
                room.setText("...");


/*
                HashMap < String, String> temp = new HashMap<String,String>();
                list = new ArrayList<HashMap<String, String>>();

                temp.put(FIRST_COLUMN_Route, "Keine Treffer!" );
                list.add(temp);
                ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list, 0);
                mDrawerList.setAdapter(adapter);


               //showNotification("Auf wi",  "");



*/

                publishToLocalView(0);




            }



        });



        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {

               beaconManager.startMonitoring(new BeaconRegion(
                        "region",
                        UUID.fromString("D1DFE79D-1AC9-40E5-9628-09061104BBE2"),
                        null, null));




            }






        });



    }


    public void checkTodayReportCards(String user_id, final String cookie, final String Labwork){


        request = new Request();



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
                Log.d("TestCallback","Error");
            }

            @Override
            public void onSucces(JSONArray result) {



                HashMap < String, String> temp = new HashMap<String,String>();
                list = new ArrayList<HashMap<String, String>>();
                int nomatches  = 0;




                JSONArray jbo = result;


                for (int i = 0; i<jbo.length(); i++) {



                    try {



                        String dt = jbo.getJSONObject(i).getString("date");
                       // String dt2 = "2018-01-16";
                        String dt2 = "2017-11-13";

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(dt);
                        Date today = sdf.parse(dt2);

                        //
                        // Date today = new Date();

                        String st = jbo.getJSONObject(i).getString("start");
                        String et = jbo.getJSONObject(i).getString("end");



                        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm");



                       Time StartTime = new java.sql.Time(sdft.parse(st).getTime());
                       Time EndTime = new java.sql.Time(sdft.parse(et).getTime());

                       String Label = jbo.getJSONObject(i).getJSONObject("labwork").getString("label");


                        if (date.compareTo(today) == 0 && Label.equals(Labwork) ) {
                            arr[i] = jbo.getJSONObject(i);


                            time.setText(StartTime +" - " + EndTime + " Uhr");
                            datum.setText(dt);
                            labwork.setText(arr[i].getJSONObject("labwork").getString("label"));
                            lesson.setText(arr[i].getString("label"));
                            room.setText(arr[i].getJSONObject("room").getString("label") + " " + arr[i].getJSONObject("room").getString("description") );

                            /*
                            temp.put(FIRST_COLUMN_Route, arr[i].getJSONObject("labwork").getString("label"));
                            temp.put(SECOND_COLUMN_Date, arr[i].getString("label"));
                            temp.put(FOUR_COLUMN_ROLE, StartTime +" - " + EndTime + " Uhr");
                            list.add(temp);
                            ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list, 1);
                            mDrawerList.setAdapter(adapter);

                            */
                            index = i;

                        } else {
                            nomatches++;
                        }










                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }
                }




                if (nomatches == jbo.length() ) {

                    temp.put(FIRST_COLUMN_Route, "Keine Treffer!" );
                    list.add(temp);
                    ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list, 0);
                    mDrawerList.setAdapter(adapter);

                } else {
                    try {








                      //  showNotification("Willkommen im " + arr[index].getJSONObject("labwork").getString("label") + " Praktikum", arr[index].getString("label"));

                      final String CourseId = arr[index].getJSONObject("labwork").getString("course");
                      final String  EntryTypId = arr[index].getJSONArray("entryTypes").getJSONObject(1).getString("id");



                       // 1 Minute warten um sicherzustellen, dass die Person als Anwesend sichtbar wird!
                          new CountDownTimer(50000, 1000) {

                            public void onTick(long millisUntilFinished) {

                                progressStatus++;
                                progressBar.setProgress(progressStatus);
                                txtPogress.setText(progressStatus+"/"+progressBar.getMax());
                                if (i == 0) {
                                    this.cancel();
                                }

                                //Log.d("Ticker",(""+millisUntilFinished/1000));
                            }

                            public void onFinish() {
                                publishToLocalView(1);


                                // Nach weiteren 4 Minuten ist sichergestellt, dass die Person auch über das ADV Tool als "Anwesend" ist!
                                new CountDownTimer(50000, 1000) {

                                    public void onTick(long millisUntilFinished) {

                                        progressStatus++;
                                        progressBar.setProgress(progressStatus);
                                        txtPogress.setText(progressStatus+"/"+progressBar.getMax());

                                        if (i == 0) {
                                            this.cancel();
                                        }

                                        //Log.d("Ticker",(""+millisUntilFinished/1000));
                                    }

                                    public void onFinish() {
                                        ReportCardEntryType(cookie,CourseId,EntryTypId);
                                        try {
                                            showNotification("Erfolreich im " + arr[index].getJSONObject("labwork").getString("label") + " angemeldet", arr[index].getString("label"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }

                                }.start();




                            }

                        }.start();









                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }




            }

        });



    }


    public void ReportCardEntryType(String cookie,String CourseId,String EntryTypId){


        request = new Request();

        JSONObject obj = new JSONObject();

        obj = createjson(EntryTypId);


        Map<String, String> params = new HashMap<>();
        params.put("Cookie",cookie );
        params.put("Content-Type","application/vnd.fhk.reportCardEntryType.V1+json");



        request.put(getApplicationContext(), obj ,params, "/courses/"+ CourseId +"/reportCardEntryTypes/"+EntryTypId, new Request.VolleyCallback() {



            @Override
            public void onSuccess(JSONObject result) throws JSONException {

            }


            @Override
            public void onError(JSONObject result) {

            }

            @Override
            public void onSucces(JSONArray result) {




            }

        });

    }

    public JSONObject createjson(String EntryTypId) {




        JSONObject obj = new JSONObject();



        try {


            obj.put("entryType","Anwesenheitspflichtig");
            obj.put("bool",true);
            obj.put("int",0);
            obj.put("id", EntryTypId);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return obj;

    }


    public void logout() {



        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logout...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        beaconManager.disconnect();
                        mPrefs.edit().clear().apply();
                        Intent profactivity = new Intent(MainActivity.this,LoginClass.class);
                        startActivity(profactivity);
                        finish();

                        progressDialog.dismiss();
                    }
                }, 3000);






    }


    public void publishToLocalView(final int type){
        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.shiftr.io",
                        clientId);



        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("96a90124");
        options.setPassword("588f7b4dd604effe".toCharArray());



        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("mqtt", "onSuccess");


                    if (type == 0) {
                        String topic = "TH-Koeln/Gummersbach/Praktikum/Student";
                        String payload = "exit";
                        byte[] encodedPayload = new byte[0];
                        try {
                            encodedPayload = payload.getBytes("UTF-8");
                            MqttMessage message = new MqttMessage(encodedPayload);
                            client.publish(topic, message);
                        } catch (UnsupportedEncodingException | MqttException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String topic = "TH-Koeln/Gummersbach/Praktikum/Student";
                        String payload = "enter";
                        byte[] encodedPayload = new byte[0];
                        try {
                            encodedPayload = payload.getBytes("UTF-8");
                            MqttMessage message = new MqttMessage(encodedPayload);
                            client.publish(topic, message);
                        } catch (UnsupportedEncodingException | MqttException e) {
                            e.printStackTrace();
                        }
                    }




                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("mqtt", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }






}
