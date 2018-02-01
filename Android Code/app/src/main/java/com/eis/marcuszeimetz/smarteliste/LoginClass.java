package com.eis.marcuszeimetz.smarteliste;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marcuszeimetz on 10.01.18.
 */

public class LoginClass extends AppCompatActivity {



    private Button btnlogin, btnregistration;
    private EditText txtgmid, txtpassword;
    private TextView txterror;
    public static final String PREF_NAME = "AppPref";
    private Request request;
    private String cookie;


    SharedPreferences mPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);




        hideactionbar();
        createelements();


        mPrefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);





       btnlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login();
            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checklogin()) {
            Intent profactivity = new Intent(LoginClass.this,MainActivity.class);
            startActivity(profactivity);
            finish();
        } else {
            //txterror.setText("Keine Aktive Sitzung. Bitte melden Sie sich an.");
        }
    }


    public boolean checklogin(){
        cookie = mPrefs.getString("cookie","");

        if (cookie.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }


    public void createelements() {

        //btnregistration = (Button) findViewById(R.id.btnregistration);
        btnlogin = (Button) findViewById(R.id.btnlogin);

        txtgmid = (EditText) findViewById(R.id.EditMail);
        txtpassword = (EditText) findViewById(R.id.EditPassword);
        //txtfehlerlogin = (TextView) findViewById(R.id.textviewfehlerlogin);


        txterror = (TextView) findViewById(R.id.txterror);



        //User_id 1
        txtgmid.setText("mi1758");
        txtpassword.setText("password");

        //User_id 2
        //txtgmid.setText("ai1805");
        //txtpassword.setText("password");





    }

    public void hideactionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }




    public void login() {

        final ProgressDialog progressDialog = new ProgressDialog(LoginClass.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authentifizieren...");
        progressDialog.show();

        request = new Request();

        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/vnd.fhk.login.V1+json ");

        JSONObject jsonBody = new JSONObject();



        try {
            jsonBody.put("password", txtpassword.getText());

            jsonBody.put("username", txtgmid.getText());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        request.post(getApplicationContext(), jsonBody, params, "/sessions", new Request.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                final JSONObject jbo = result;

                SharedPreferences.Editor prefsEditor = mPrefs.edit();


                    //prefsEditor.putString("gmid", txtgmid.getText().toString());
                    //prefsEditor.putString("cookie", result.getJSONObject("headers").getString("Set-Cookie"));




                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    try {
                                        getStudydata(txtgmid.getText().toString(),jbo.getJSONObject("headers").getString("Set-Cookie"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // onLoginFailed();
                                    progressDialog.dismiss();
                                }
                            }, 3000);










            }

            @Override
            public void onError(JSONObject result) {

                progressDialog.dismiss();

                try {
                    if (result.getString("status").equals("KO")) {
                        txterror.setText("Die Kombination aus Benutzername und Passwort ist nicht korrekt!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSucces(JSONArray result) {

            }


        });
    }

    //Innerhalb des Login erfolgt, der Aufruf von getStudydata

    public void getStudydata(String gmid, final String cookie) {

        request = new Request();


        Map<String, String> params = new HashMap<>();
        params.put("Cookie",cookie );

        request.get(getApplicationContext(), params, "/atomic/students?systemId="+ gmid , new Request.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                SharedPreferences.Editor prefsEditor = mPrefs.edit();


                try {
                    prefsEditor.putString("cookie", cookie);
                    prefsEditor.putString("FirstName", result.getString("firstname"));
                    prefsEditor.putString("LastName", result.getString("lastname"));
                    prefsEditor.putString("Email", result.getString("email"));
                    prefsEditor.putString("gmid", result.getString("systemId"));
                    prefsEditor.putString("label", result.getJSONObject("enrollment").getString("label"));
                    prefsEditor.putString("abbreviation", result.getJSONObject("enrollment").getString("abbreviation"));
                    prefsEditor.putString("user_id", result.getString("id"));

                    prefsEditor.commit();
                    Intent MainScreen = new Intent(LoginClass.this,MainActivity.class);
                    startActivity(MainScreen);
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }


            @Override
            public void onError(JSONObject result) {
                JSONObject test = result;
            }

            @Override
            public void onSucces(JSONArray result) {

            }

        });
    }

}
