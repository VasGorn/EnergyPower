package example.vasiliy.energypower;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import example.vasiliy.energypower.http.Const;
import example.vasiliy.energypower.http.HttpHandler;
import example.vasiliy.energypower.model.ServerDate;
import example.vasiliy.energypower.model.User;


public class Login extends AppCompatActivity {
    private String TAG = Login.class.getSimpleName();

    EditText etLogin;
    EditText etPassword;
    Button btnLogin;
    CheckBox cbRemember;
    ProgressBar progressBar;

    private String user;
    private String password;

    private final String URL_LOGIN = Const.URL_SERVER + "/login1.php";
    private final String PREF_NAME = "MyPref";
    private final String KEY_USERNAME = "username";
    private final String KEY_PASSWORD = "password";
    private final String KYT_ISLOGIN = "isLogIn";
    private final int PRIVATE_MODE = 0;

    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = (EditText)findViewById(R.id.etLogin);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        boolean isLogIn = pref.getBoolean(KYT_ISLOGIN,false);

        if(isLogIn){
            String userSaved = pref.getString(KEY_USERNAME,null);
            String passwordSaved = pref.getString(KEY_PASSWORD,null);
            etLogin.setText(userSaved);
            etPassword.setText(passwordSaved);
            cbRemember.setChecked(true);
        }

        cbRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cbRemember.isChecked()){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(KYT_ISLOGIN, false);
                    editor.apply();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = etLogin.getText().toString().trim();
                password = etPassword.getText().toString();

                if(user.equals("") || password.equals("")){
                    Toast toast = Toast.makeText(Login.this, "Ошибка: некоторые поля пустые", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM,0,100);
                    toast.show();
                    return;
                }

                //request authentication with remote server
                AsyncDataClass asyncRequestObject = new AsyncDataClass();
                asyncRequestObject.execute();

            }
        });
    }

    private class AsyncDataClass extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();
            //String jsonStr = sh.makeServiceCall(URL_ORDERS_FOR_MASTER);
            String jsonStr = sh.postLogin(URL_LOGIN, user, password);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    success = jsonObj.getInt("success");

                    if(success == 1) {
                        JSONArray orders = jsonObj.getJSONArray("user");
                        //JSONArray orders = new JSONArray(jsonStr);
                        User.setManager(false);
                        User.setEngineer(false);
                        User.setMaster(false);

                        for (int i = 0; i < orders.length(); i++) {

                            JSONObject o = orders.getJSONObject(i);
                            String id = o.getString(Const.USER_EMPLOYEE_ID);
                            String date = o.getString("date");
                            String position = o.getString(Const.EMPLOYEE_HAS_POSITION_POSITION_ID);

                            Date servDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);

                            ServerDate.setServerDate(servDate);

                            User.setId(Integer.valueOf(id));

                            int pos = Integer.valueOf(position);
                            switch (pos) {
                                case 1:
                                    User.setManager(true);
                                    break;
                                case 2:
                                    User.setEngineer(true);
                                    break;
                                case 3:
                                    User.setMaster(true);
                                    break;
                            }
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Неверный логин или пароль!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (final JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Ошибка формата даты!",
                            Toast.LENGTH_LONG).show();
                }
            }else{
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Ошибка соединения с сервером!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            progressBar.setVisibility(View.INVISIBLE);

            if(success == 1){
                boolean isRemember = cbRemember.isChecked();

                if(isRemember){
                    saveLoginAndPassword();
                }

                Intent intent = new Intent(Login.this, ListOrders.class);
                startActivity(intent);

            }

        }

    }

    private void saveLoginAndPassword(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(KYT_ISLOGIN, true);
        editor.putString(KEY_USERNAME, user);
        editor.putString(KEY_PASSWORD, password);

        editor.apply();

    }
}
