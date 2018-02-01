package com.eis.marcuszeimetz.smarteliste;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

/**
 * Created by marcuszeimetz on 22.01.18.
 */

public class profil extends AppCompatActivity {

    private TextView txtFirstName, txtEmail,txtGmid, txtLastName, txtStudyFach, txtStudyabk,txtid;
    public static final String PREF_NAME = "AppPref";
   String firstname,lastname,email,gmid,label,abbreviation, id;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        setActionBar();
        createelements();


        mPrefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
         firstname = mPrefs.getString("FirstName","");
         lastname = mPrefs.getString("LastName","");
         email = mPrefs.getString("Email","");
         gmid = mPrefs.getString("gmid","");
         label = mPrefs.getString("label","");
         abbreviation = mPrefs.getString("abbreviation","");
         id = mPrefs.getString("user_id","");


        setDataToElements();
    }

    public void setDataToElements() {
        txtFirstName.setText(firstname);
        txtLastName.setText(lastname);
        txtEmail.setText(email);
        txtGmid.setText(gmid);
        txtStudyFach.setText(label);
        txtStudyabk.setText(abbreviation);
        txtid.setText(id);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profil_menue, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id==android.R.id.home){
            Intent profactivity = new Intent(profil.this,MainActivity.class);
            startActivity(profactivity);
        } else if( id == R.id.today) {
            Intent profactivity = new Intent(profil.this,MainActivity.class);
            startActivity(profactivity);
        } else if( id == R.id.ueberblick) {
            Intent profactivity = new Intent(profil.this, Ueberblick.class);
            startActivity(profactivity);

        }
        return super.onOptionsItemSelected(item);
    }


    public void createelements() {


        txtFirstName = (TextView) findViewById(R.id.txtFirstName);
        txtLastName = (TextView) findViewById(R.id.txtLastName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtGmid = (TextView) findViewById(R.id.txtGmid);
        txtStudyFach = (TextView) findViewById(R.id.txtStudyFach);
        txtStudyabk = (TextView) findViewById(R.id.txtStudyabk);
        txtid = (TextView) findViewById(R.id.txtid);

        //txtFirstName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profil");
        //actionBar.setLogo(R.mipmap.thkoelnlogo);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setDisplayShowHomeEnabled(true);
        // actionBar.setLogo(R.mipmap.thkoelnlogo);
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setTitle(gmid.toUpperCase());
        //actionBar.hide();s


    }


}
