package example.vasiliy.energypower;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import example.vasiliy.energypower.custom_adapters.AdapterWorkToApprove;
import example.vasiliy.energypower.custom_adapters.AdapterWorkTypeProgress;
import example.vasiliy.energypower.http.Const;
import example.vasiliy.energypower.http.HttpHandler;
import example.vasiliy.energypower.http.JsonParsing;
import example.vasiliy.energypower.model.WR_Table;
import example.vasiliy.energypower.model.WorkType;

public class Progress extends AppCompatActivity {
    private ProgressBar pbAllProgress;
    private TextView txtAllProgress;
    private ListView lvWorkTypeProgress;

    private AdapterWorkTypeProgress adapter;

    private ArrayList<WorkType> workTypes;
    private String hoursPerMonthID;

    private final String TAG = WorkManager.class.getSimpleName();

    private final String URL_WORK_TYPE_FOR_ORDER = Const.URL_SERVER +"/manager/get_work_type_on_order.php";
    private final String URL_TYPE_HOUR_PER_MONTH = Const.URL_SERVER + "/manager/get_type_hours_on_order_for_master.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_for_manager);

        pbAllProgress = findViewById(R.id.pbAllProgress);
        txtAllProgress = findViewById(R.id.txtAllProgress);
        lvWorkTypeProgress = findViewById(R.id.lvWorkTypeProgress);

        Intent intent = getIntent();
        int hoursInMonth = intent.getIntExtra("hours_in_month",1);
        int sumWorkHours = intent.getIntExtra("work_hours",0);
        hoursPerMonthID = intent.getStringExtra("hoursOnMonthID");

        double percAll = ((double)sumWorkHours) / hoursInMonth * 100.0;
        int progressInt = (int)Math.round(percAll);

        pbAllProgress.setProgress(progressInt);
        txtAllProgress.setText(String.valueOf(progressInt) + "%");

        new GetWorkTypeForOrder().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backMenu: finish();
                return true;

            default:return super.onOptionsItemSelected(item);
        }

    }

    private class GetWorkTypeForOrder extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Progress.this,
                    "Получение списка видов работ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workTypes = new ArrayList<>();

            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getWorkTypeForOrder(URL_WORK_TYPE_FOR_ORDER, hoursPerMonthID);

            Log.e(TAG, "Response from url: " + jsonStr);

            JsonParsing jsonPars = new JsonParsing();

            if (jsonStr != null) {
                workTypes = jsonPars.getWorkTypeForOrder(jsonStr);

                if(workTypes == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Нет видов работ для заказа!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }else{
                Log.e(TAG, "Ошибка соединения с сервером!");
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
            if(workTypes != null) {
                new GetSumTypeHours().execute();
            }
        }

    }

    private class GetSumTypeHours extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Progress.this,
                    "Получение часов по заказу...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... str) {
            HttpHandler sh = new HttpHandler();

                for(WorkType wt: workTypes) {
                    String jsonStr = sh.getTypeHoursOnOrder(URL_TYPE_HOUR_PER_MONTH,
                            hoursPerMonthID, String.valueOf(wt.getId()));

                    Log.e(TAG, "Response from url: " + jsonStr);

                    JsonParsing jsonPars = new JsonParsing();

                    if (jsonStr != null) {
                        int sumTypeHours = jsonPars.getTypeHoursOnForMaster(jsonStr);
                        wt.setSumHours(sumTypeHours);

                    } else {
                        Log.e(TAG, "Ошибка соединения с сервером!");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Ошибка соединения с сервером!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        return false;
                    }
                }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);
            //0 - hours
            if(result){
                adapter = new AdapterWorkTypeProgress(workTypes,Progress.this);
                lvWorkTypeProgress.setAdapter(adapter);
            }
        }
    }


}
