package example.vasiliy.energypower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import example.vasiliy.energypower.custom_adapters.AdapterWorkToApprove;
import example.vasiliy.energypower.http.Const;
import example.vasiliy.energypower.http.HttpHandler;
import example.vasiliy.energypower.http.JsonParsing;
import example.vasiliy.energypower.model.Employee;
import example.vasiliy.energypower.model.EmployeeWithPosition;
import example.vasiliy.energypower.model.ServerDate;
import example.vasiliy.energypower.model.User;
import example.vasiliy.energypower.model.WR_Table;
import example.vasiliy.energypower.model.WorkType;

public class WorkList extends AppCompatActivity {

    private TextView txNameOrder;
    private TextView txtLeftHours;
    private TextView txtHoursMonth;
    private Spinner sprPerformers;
    private ListView lvWorkToApprove;
    private Button btnApproveAll;
    private Button btnApproveSel;

    private String orderID;
    private String hoursOnMonthID;
    private List<EmployeeWithPosition> performersList;
    private int leftHours;
    private int[] hoursArray;


    private ArrayList<WR_Table> workList;
    private AdapterWorkToApprove adapter;
    private int itemPosition;

    private final String TAG = WorkList.class.getSimpleName();

    private final int HOURS_ON_MONTH_ID = 0;
    private final int HOURS_IN_QUOT = 1;
    private final int SUM_WORKING_HOURS = 2;


    private final String URL_GET_PERFORMERS = Const.URL_SERVER +"/manager/get_performers_on_order.php";
    private final String URL_GET_HOURS_FOR_PERFORMER = Const.URL_SERVER +"/manager/get_hours_on_order_for_performers.php";
    private final String URL_GET_WORK_TO_APPROVE = Const.URL_SERVER +"/manager/get_work_to_approve.php";
    private final String URL_UPDATE_WORK = Const.URL_SERVER +"/manager/update_work_time.php";

    private final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_list);

        txNameOrder = findViewById(R.id.txtOrderName);
        txtLeftHours = findViewById(R.id.txtLeftHours);
        txtHoursMonth = findViewById(R.id.txtHoursMonth);
        sprPerformers = findViewById(R.id.sprPerformers);
        lvWorkToApprove = findViewById(R.id.lvWorkToApprove);
        btnApproveAll = findViewById(R.id.btnApproveAll);
        btnApproveSel = findViewById(R.id.btnApproveSel);

        registerForContextMenu(lvWorkToApprove);

        Intent intent = getIntent();
        orderID = intent.getStringExtra(Const.ORDER_ID);
        txNameOrder.setText(intent.getStringExtra(Const.ORDER_NAME));
        //-----------------------------------------------------------------------
        new GetPerformersForManager().execute();
        //-----------------------------------------------------------------------
        sprPerformers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EmployeeWithPosition select = (EmployeeWithPosition) adapterView.getItemAtPosition(i);
                String strID = String.valueOf(select.getEmployee().getID());
                new GetHoursForPerformer().execute(strID,String.valueOf(ServerDate.getNumMonth()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //-----------------------------------------------------------------------
        btnApproveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateWorkData().execute(true);
            }
        });
        //-----------------------------------------------------------------------
        btnApproveSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateWorkData().execute(false);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.edit_id:
                WR_Table wr = workList.get(info.position);
                Employee employee = wr.getEmployee();
                WorkType workType = wr.getWorkType();
                int numDay = wr.getNumDay();
                int workTime = wr.getWorkHours();
                int overWork = wr.getOverWorkHours();

                leftHours = leftHours + (workTime + overWork);

                itemPosition = info.position;
                Intent intent = new Intent(WorkList.this, WorkEdit.class);
                intent.putExtra("orderID", orderID);
                intent.putExtra("itemPosition", info.position);
                intent.putExtra("employee", employee);
                intent.putExtra("workType", workType);
                intent.putExtra("numDay", String.valueOf(numDay));
                intent.putExtra("workTime", String.valueOf(workTime));
                intent.putExtra("overWork", String.valueOf(overWork));

                startActivityForResult(intent,REQUEST_CODE);
                return true;

            default: return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                WorkType workTypeEdit = (WorkType) data.getSerializableExtra("workType");
                int workTimeEdit = data.getIntExtra("workTime",0);
                int overWorkEdit = data.getIntExtra("overWork",0);
                WR_Table wr = workList.get(itemPosition);

                leftHours = leftHours - (workTimeEdit + overWorkEdit);

                wr.setWorkType(workTypeEdit);
                wr.setWorkHours(workTimeEdit);
                wr.setOverWorkHours(overWorkEdit);

                txtLeftHours.setText(String.valueOf(leftHours));
                workList.set(itemPosition, wr);

                adapter.notifyDataSetChanged();
            }
        }
    }

    private class GetPerformersForManager extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkList.this,
                    "Получение исполнителей...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getPerformersOnOrder(URL_GET_PERFORMERS, orderID);

            Log.e(TAG, "Response from url: " + jsonStr);

            JsonParsing jsonPars = new JsonParsing();

            if (jsonStr != null) {
                performersList = jsonPars.getPerformersList(jsonStr);

                if(performersList == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Данные отсутствуют",
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
            if(performersList != null) {
                MySpinnerAdapter adapter = new MySpinnerAdapter(WorkList.this,
                        R.layout.spinner_performers_drop_down,
                        performersList);
                sprPerformers.setAdapter(adapter);
            }
        }
    }

    private class GetHoursForPerformer extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkList.this,
                    "Получение часов по заказу...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... str) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getHoursForPerformer(URL_GET_HOURS_FOR_PERFORMER, orderID, str[0],str[1]);

            Log.e(TAG, "Response from url: " + jsonStr);

            JsonParsing jsonPars = new JsonParsing();

            if (jsonStr != null) {
                hoursArray = jsonPars.getHoursOnOrder(jsonStr);

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
                hoursOnMonthID = String.valueOf(hoursArray[HOURS_ON_MONTH_ID]);

                txtLeftHours.setText(String.valueOf(leftHours));
                txtHoursMonth.setText(String.valueOf(hoursArray[HOURS_IN_QUOT]));

                Log.e(TAG, "ID часов на месец: " + String.valueOf(hoursArray[HOURS_ON_MONTH_ID]));
                new GetWorkToApprove().execute(hoursOnMonthID);
            }

        }
    }

    private class GetWorkToApprove extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkList.this,
                    "Получение списка для утверждения...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... str) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getWorkToApprove(URL_GET_WORK_TO_APPROVE, str[0]);

            Log.e(TAG, "Response from url: " + jsonStr);

            JsonParsing jsonPars = new JsonParsing();

            if (jsonStr != null) {

                ArrayList<WR_Table> list = jsonPars.getWorkToApprove(jsonStr);

                if(list == null){

                    if(workList != null) workList.clear();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Нет данных!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    workList = list;
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
            if(workList != null) {
                adapter = new AdapterWorkToApprove(workList,WorkList.this);
                lvWorkToApprove.setAdapter(adapter);
            }


        }
    }

    private class UpdateWorkData extends AsyncTask<Boolean, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkList.this,
                    "Подготовка отправки данных...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Boolean... isAll) {

            if(workList.size()==0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Нет данных для записи!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            JSONObject arrayToSend = new JSONObject();
            JSONArray jsonArray = new JSONArray();

                Iterator<WR_Table> it = workList.iterator();
                while(it.hasNext()) {

                    WR_Table element = it.next();

                    if (!isAll[0] && !element.isSelected()) {
                        continue;
                    }

                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put(Const.WORK_TIME_HOURS_MONTH_ID, Integer.valueOf(hoursOnMonthID));
                        jsonObject.put(Const.WORK_TIME_EMPLOYEE_ID, element.getEmployee().getID());
                        jsonObject.put(Const.WORK_TIME_WORK_TYPE_ID, element.getWorkType().getId());
                        jsonObject.put(Const.WORK_TIME_NUM_DAY, element.getNumDay());
                        jsonObject.put(Const.WORK_TIME_WORK_TIME, element.getWorkHours());
                        jsonObject.put(Const.WORK_TIME_OVER_TIME, element.getOverWorkHours());
                        jsonArray.put(jsonObject);

                        arrayToSend.put("work", jsonArray);

                        //delete item from list
                        it.remove();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                HttpHandler httpHandler = new HttpHandler();

                    //send this json to server
                String strAnswer = httpHandler.sendWorkTimeToInsert(URL_UPDATE_WORK, arrayToSend);

                Log.e(TAG, "Json created: " + arrayToSend.toString());

                JsonParsing jsonPars = new JsonParsing();

                //if(isCancelled()) break;

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
                        return  false;
                    }

                    //clear list if write to ALL
                    //if(isAll[0]) workList.clear();

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

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);
            if(result) {
                Toast.makeText(WorkList.this,
                        "данные отправлены", Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
            }
        }

    }

    private class MySpinnerAdapter extends ArrayAdapter<EmployeeWithPosition>{
        private List<EmployeeWithPosition> items;

        private MySpinnerAdapter(Context context, int textViewResourceId,
                                List<EmployeeWithPosition> items){
            super(context, textViewResourceId, items);
            this.items = items;
        }

        public int getCount(){ return items.size(); }

        public EmployeeWithPosition getItem(int pos){ return items.get(pos); }

        public long getItemId(int position){ return position; }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View spView = inflater.inflate(R.layout.spinner_performers_drop_down,parent,false);

            TextView txtEmployee = spView.findViewById(R.id.txtEmployee);
            txtEmployee.setText(items.get(position).getEmployee().toString());

            TextView txtPosition = spView.findViewById(R.id.txtPosition);
            txtPosition.setText(items.get(position).getPositionName());

            //return super.getView(position, convertView, parent);
            return spView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View dropDownView = inflater.inflate(R.layout.spinner_performers_drop_down,parent,false);

            TextView txtEmployee = dropDownView.findViewById(R.id.txtEmployee);
            txtEmployee.setText(items.get(position).getEmployee().toString());

            TextView txtPosition = dropDownView.findViewById(R.id.txtPosition);
            txtPosition.setText(items.get(position).getPositionName());

            //return super.getDropDownView(position, convertView, parent);
            return dropDownView;
        }
    }
}
