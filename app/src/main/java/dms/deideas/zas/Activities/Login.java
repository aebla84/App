package dms.deideas.zas.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dms.deideas.zas.Push.RegistrationService;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.MotodriverGet;
import dms.deideas.zas.Services.MotodriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends Activity implements View.OnClickListener {

    private EditText user, password;
    private Button enterApp;

    private int iduser;
    private String name;
    private String email;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        enterApp = (Button) findViewById(R.id.enter);

        enterApp.setOnClickListener(this);

        //Register Push notification
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);

    }

    @Override
    public void onClick(View v) {
        Retrofit retrofit;
        MotodriverService motodriverService;

        if (v == enterApp) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://zascomidaentuboca.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            motodriverService = retrofit.create(MotodriverService.class);


            motodriverService.String(user.getText().toString(), password.getText().toString()).enqueue(new Callback<MotodriverGet>() {
                @Override
                public void onResponse(Call<MotodriverGet> call, Response<MotodriverGet> response) {
                    if (response.body() != null) {

                        iduser = response.body().getID();
                        name = response.body().getData().getUser_nicename();
                        email = response.body().getData().getUser_email();

                        long timelastLogin = System.currentTimeMillis() / 1000;
                        SharedPreferences prefs =
                                getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("idUser", iduser);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putLong("timelastLogin", timelastLogin);
                        editor.commit();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<MotodriverGet> call, Throwable t) {
                    Toast.makeText(Login.this, "ERROR:" + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
