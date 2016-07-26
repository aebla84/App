package dms.deideas.zas.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Loading extends AppCompatActivity {

    private int idUser = 0;
    private long timelastLogin = 0;
    private long timeNow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        idUser = prefs.getInt("idUser", 0);

        // Keep current time in preferences
        timeNow = System.currentTimeMillis() / 1000;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("timeNow", timeNow);
        editor.commit();

        timelastLogin = prefs.getLong("timelastLogin",0);

        if (idUser == 0) {
            Intent intent = new Intent(Loading.this, Splashscreen.class);
            startActivity(intent);
        }

        // while the value of last login time is less than 8 hours, login is unnecessary
        if(timeNow-timelastLogin<28800){
            Intent intent = new Intent(Loading.this, MainActivity.class);
            startActivity(intent);
        }
        // in other way, login is mandatory
        else{
            Intent intent = new Intent(Loading.this, Splashscreen.class);
            startActivity(intent);
        }
    }
}
