package example.vasiliy.energypower;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import example.vasiliy.energypower.http.Const;
import example.vasiliy.energypower.http.HttpHandler;
import example.vasiliy.energypower.http.JsonParsing;
import example.vasiliy.energypower.model.Employee;
import example.vasiliy.energypower.model.ServerDate;
import example.vasiliy.energypower.model.User;
import example.vasiliy.energypower.model.WorkType;

public class WorkMaster extends AppCompatActivity {
    private TextView txNameOrder;
    private TextView txtLeftHours;
    private TextView txtHoursMonth;

    private Spinner sprNumDay;
    private Spinner sprWorker;
    private Spinner sprWorkType;

    private TextView txtWork;
    private TextView txtNumWork;
    private TextView txtOverWork;
    private TextView txtNumOverWork;

    private Button btnAdd;
    private Button btnWrite;

    private ListView lvWork;

    ArrayList<HashMap<String, Object>> listWork;
    ListAdapter adapter;

    private final String KEY_EMPLOYEE = "employee";
    private final String KEY_WORK_TYPE = "work_type";
    private final String KEY_NUM_DAY = "num_day";
    private final String KEY_WORK_TIME = "work_time";
    private final String KEY_OVER_WORK = "over_work";

    private final int HOURS_IN_QUOT = 0;
    private final int SUM_WORKING_HOURS = 1;

    private final String URL_HOUR_PER_MONTH = Const.URL_SERVER + "/master/get_hours_on_order_for_master.php";
    private final String URL_TEAM_FOR_MASTER = Const.URL_SERVER + "/master/get_workers_for_master.php";
    private final String URL_WORK_TYPE_FOR_ORDER = Const.URL_SERVER +"/master/get_work_type_on_order.php";
    private final String URL_SEND_WORK_TIME= Const.URL_SERVER +"/master/set_work_time.php";
    private final String URL_EMPLOYEE_ID= Const.URL_SERVER +"/get_employee_by_id.php";

    private final String TAG = WorkMaster.class.getSimpleName();

    private String orderID;
    private String hoursPerMonthID;
    private int[] hoursArray;
    int leftHours;
    private List<Employee> teamList;
    private List<WorkType> workTypeList;

    private int hoursFromDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_master);

        txNameOrder = findViewById(R.id.txtOrderName);
        txtLeftHours = findViewById(R.id.txtLeftHours);
        txtHoursMonth = findViewById(R.id.txtHoursMonth);

        sprNumDay = findViewById(R.id.sprNumDay);
        sprWorker = findViewById(R.id.sprWorker);
        sprWorkType = findViewById(R.id.sprWorkType);

        txtWork = findViewById(R.id.txtWork);
        txtNumWork = findViewById(R.id.txtNumWork);
        txtOverWork = findViewById(R.id.txtOverWork);
        txtNumOverWork = findViewById(R.id.txtNumOverWork);

        btnAdd = findViewById(R.id.btnAdd);
        btnWrite = findViewById(R.id.btnWrite);

        lvWork = findViewById(R.id.lvWork);

        registerForContextMenu(lvWork);

        listWork = new ArrayList<>();

        Intent intent = getIntent();
        txNameOrder.setText(intent.getStringExtra(Const.ORDER_NAME));
        orderID = intent.getStringExtra(Const.ORDER_ID);
        hoursPerMonthID = intent.getStringExtra("hours_per_month_id");
        //----------------------------------------------------------------------------------------------------------------

        new GetHoursForMaster().execute();

        //----------------------------------------------------------------------------------------------------------------

        List<Integer> numDays = new ArrayList<>();
        for (int i = ServerDate.getDayOfMonth() - 3; i <= ServerDate.getDayOfMonth(); i++) {
            if(i > 0) numDays.add(i);
        }

        ArrayAdapter<Integer> sprAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, numDays);
        sprAdapter.setDropDownViewResource(R.layout.spinner_item);
        sprNumDay.setAdapter(sprAdapter);
        sprNumDay.setSelection(sprAdapter.getCount() - 1);

        //----------------------------------------------------------------------------------------------------------------
        if(User.isMaster()) {
            new GetWorkersForMaster().execute();
        }else{
            new GetWorkerForEngineer().execute();
        }

        //----------------------------------------------------------------------------------------------------------------

        new GetWorkTypeForOrder().execute();

        //----------------------------------------------------------------------------------------------------------------

        txtNumWork.setText("8");
        txtWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPickDialog(8,0);

            }
        });

        txtNumOverWork.setText("0");
        txtOverWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPickDialog(12,1);
            }
        });

        //----------------------------------------------------------------------------------------------------------------

        adapter = new SimpleAdapter(WorkMaster.this, listWork,
                R.layout.work_time_item, new String[]{KEY_EMPLOYEE, KEY_WORK_TYPE, KEY_NUM_DAY, KEY_WORK_TIME, KEY_OVER_WORK},
                new int[]{R.id.txtItemEmployee, R.id.txtItemWorkType, R.id.txtItemNumDay, R.id.txtItemWorkTime, R.id.txtItemOverWork});
        lvWork.setAdapter(adapter);

        //----------------------------------------------------------------------------------------------------------------
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Employee employee = (Employee) sprWorker.getSelectedItem();
                WorkType workType = (WorkType) sprWorkType.getSelectedItem();

                if(employee != null && workType != null) {

                    int day = (int) sprNumDay.getSelectedItem();
                    int workTime = Integer.valueOf(txtNumWork.getText().toString());
                    int overWork = Integer.valueOf(txtNumOverWork.getText().toString());

                    for (HashMap<String, Object> hm : listWork) {
                        Employee employeeElement = ((Employee) hm.get(KEY_EMPLOYEE));
                        int numDay = (int) hm.get(KEY_NUM_DAY);

                        if (employeeElement.equals(employee) && numDay == day) {
                            leftHours += ((int) hm.get(KEY_WORK_TIME) + (int) hm.get(KEY_OVER_WORK));
                            leftHours -= (workTime + overWork);

                            if (leftHours < 0) {
                                Toast.makeText(WorkMaster.this, "Превышен лимит по часам!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            hm.put(KEY_WORK_TYPE, workType);
                            hm.put(KEY_WORK_TIME, workTime);
                            hm.put(KEY_OVER_WORK, overWork);
                            txtLeftHours.setText(String.valueOf(leftHours));
                            ((BaseAdapter) adapter).notifyDataSetChanged();
                            Toast.makeText(WorkMaster.this, "запись обновлена", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    leftHours -= (workTime + overWork);
                    if (leftHours < 0) {
                        Toast.makeText(WorkMaster.this, "Превышен лимит по часам!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    HashMap<String, Object> hashMapWork = new HashMap<>();

                    hashMapWork.put(KEY_EMPLOYEE, employee);
                    hashMapWork.put(KEY_WORK_TYPE, workType);
                    hashMapWork.put(KEY_NUM_DAY, day);
                    hashMapWork.put(KEY_WORK_TIME, workTime);
                    hashMapWork.put(KEY_OVER_WORK, overWork);

                    listWork.add(hashMapWork);
                    txtLeftHours.setText(String.valueOf(leftHours));
                    ((BaseAdapter) adapter).notifyDataSetChanged();

                    Toast.makeText(WorkMaster.this, "запись добавлена", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(WorkMaster.this, "Поля пустые!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendWorkData().execute();
            }
        });

    }

    private void numPickDialog(final int numMax, final int isWorkOrOverWork){
        final NumberPicker numPick = new NumberPicker(this);
        numPick.setMaxValue(numMax);
        numPick.setMinValue(0);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setView(numPick);
        dialog.setTitle("Выберите время работы").setIcon(R.drawable.ic_dialog);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hoursFromDialog = numPick.getValue();
                if(isWorkOrOverWork == 0) {
                    txtNumWork.setText(String.valueOf(hoursFromDialog));
                }else{
                    txtNumOverWork.setText(String.valueOf(hoursFromDialog));
                }
            }
        });
        dialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete_id:
                HashMap<String, Object> hm = listWork.get(info.position);
                leftHours += ((int)hm.get(KEY_WORK_TIME) + (int)hm.get(KEY_OVER_WORK));
                txtLeftHours.setText(String.valueOf(leftHours));

                listWork.remove(info.position);
                ((BaseAdapter)adapter).notifyDataSetChanged();
                return true;

                default: return super.onContextItemSelected(item);
        }
    }

    private class GetHoursForMaster extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkMaster.this,
                    "Получение часов по заказу...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getHoursOnOrder(URL_HOUR_PER_MONTH, hoursPerMonthID);

            Log.e(TAG, "Response from url: " + jsonStr);

            JsonParsing jsonPars = new JsonParsing();

            if (jsonStr != null) {
                hoursArray = jsonPars.getHoursOnForMaster(jsonStr);

                if(hoursArray == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Ошибка преобразования данных!",
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
            //0 - hours
            if(hoursArray != null) {
                leftHours = hoursArray[HOURS_IN_QUOT] - hoursArray[SUM_WORKING_HOURS];
                txtLeftHours.setText(String.valueOf(leftHours));
                txtHoursMonth.setText(String.valueOf(hoursArray[HOURS_IN_QUOT]));
            }
        }
    }


    private class GetWorkersForMaster extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkMaster.this,
                    "Получение списка рабочих...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getTeamForMaster(URL_TEAM_FOR_MASTER, String.valueOf(User.getId()));

            Log.e(TAG, "Response from url: " + jsonStr);

            JsonParsing jsonPars = new JsonParsing();

            if (jsonStr != null) {
                teamList = jsonPars.getTeamForMaster(jsonStr);

                if (teamList == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Нет бригады для мастера!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(teamList != null) {
                ArrayAdapter<Employee> employeeArrayAdapter = new ArrayAdapter<>(WorkMaster.this, R.layout.spinner_item, teamList);
                employeeArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                sprWorker.setAdapter(employeeArrayAdapter);
            }
        }
    }

        private class GetWorkerForEngineer extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(WorkMaster.this,
                        "Установка данных...", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                HttpHandler sh = new HttpHandler();

                String jsonStr = sh.getEmployeeByID(URL_EMPLOYEE_ID, User.getId());

                Log.e(TAG, "Response from url: " + jsonStr);

                JsonParsing jsonPars = new JsonParsing();

                if (jsonStr != null) {
                    Employee employee = jsonPars.getCurrentEmployee(jsonStr);
                    teamList = new ArrayList<>();
                    teamList.add(employee);

                    if(teamList == null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Не найден сотрудник!",
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
            if(teamList != null) {
                ArrayAdapter<Employee> employeeArrayAdapter = new ArrayAdapter<>(WorkMaster.this, R.layout.spinner_item, teamList);
                employeeArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                sprWorker.setAdapter(employeeArrayAdapter);
            }
        }

    }


    private class GetWorkTypeForOrder extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkMaster.this,
                    "Получение списка видов работ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getWorkTypeForOrder(URL_WORK_TYPE_FOR_ORDER, orderID);

            Log.e(TAG, "Response from url: " + jsonStr);

            JsonParsing jsonPars = new JsonParsing();

            if (jsonStr != null) {
                workTypeList = jsonPars.getWorkTypeForOrder(jsonStr);

                if(workTypeList == null){
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
            if(workTypeList != null) {
                ArrayAdapter<WorkType> typeWorkAdapter = new ArrayAdapter<>(WorkMaster.this,R.layout.spinner_item,workTypeList);
                typeWorkAdapter.setDropDownViewResource(R.layout.spinner_item);
                sprWorkType.setAdapter(typeWorkAdapter);
            }
        }

    }

    private class SendWorkData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkMaster.this,
                    "Отправка данных...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject arrayToSend = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            int listSize = listWork.size();
            if (listSize > 0) {
                for (int i = 0; i < listSize; i++) {

                    JSONObject jsonObject = new JSONObject();
                    HashMap<String, Object> element = listWork.get(i);

                    try {
                        jsonObject.put(Const.WORK_TIME_HOURS_MONTH_ID, Integer.valueOf(hoursPerMonthID));
                        jsonObject.put(Const.WORK_TIME_EMPLOYEE_ID, ((Employee) element.get(KEY_EMPLOYEE)).getID());
                        jsonObject.put(Const.WORK_TIME_WORK_TYPE_ID, ((WorkType) element.get(KEY_WORK_TYPE)).getId());
                        jsonObject.put(Const.WORK_TIME_NUM_DAY, element.get(KEY_NUM_DAY));
                        jsonObject.put(Const.WORK_TIME_WORK_TIME, element.get(KEY_WORK_TIME));
                        jsonObject.put(Const.WORK_TIME_OVER_TIME, element.get(KEY_OVER_WORK));
                        jsonArray.put(jsonObject);

                        arrayToSend.put("work", jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                HttpHandler httpHandler = new HttpHandler();

                String strAnswer = httpHandler.sendWorkTimeToInsert(URL_SEND_WORK_TIME, arrayToSend);

                Log.e(TAG, "Json created: " + arrayToSend.toString());

                JsonParsing jsonPars = new JsonParsing();

                if (strAnswer != null) {
                    int success = jsonPars.getSuccess(strAnswer);

                    if (success == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Ошибка записи на сервере!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

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
                }


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Нет данных для записи!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            Toast.makeText(WorkMaster.this,
                    "данные отправлены", Toast.LENGTH_SHORT).show();
            listWork.clear();
            ((BaseAdapter)adapter).notifyDataSetChanged();
            new GetHoursForMaster().execute();
        }

    }

}
