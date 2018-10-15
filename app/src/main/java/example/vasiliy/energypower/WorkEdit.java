package example.vasiliy.energypower;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import example.vasiliy.energypower.http.Const;
import example.vasiliy.energypower.http.HttpHandler;
import example.vasiliy.energypower.http.JsonParsing;
import example.vasiliy.energypower.model.Employee;
import example.vasiliy.energypower.model.WorkType;

public class WorkEdit extends AppCompatActivity {
    private TextView txtNumDay;
    private TextView txtWorker;
    private Spinner spWorkType;
    private TextView txtNumWorkTime;
    private TextView txtWorkTime;
    private TextView txtOverWork;
    private TextView txtNumOverWork;
    private Button btnWrite;
    private Button btnEditBack;

    private List<WorkType> workTypeList;
    private WorkType workTypeOld;
    private String orderID;
    private int numPosition;

    private int hoursPerMonthID;

    private final String URL_WORK_TYPE_FOR_ORDER = Const.URL_SERVER +"/master/get_work_type_on_order.php";

    private final String TAG = WorkEdit.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_edit);

        txtNumDay = findViewById(R.id.txtEditNumDay);
        txtWorker = findViewById(R.id.txtEditWorker);
        spWorkType = findViewById(R.id.sprEditWorkType);
        txtNumWorkTime = findViewById(R.id.txtEditNumWork);
        txtNumOverWork = findViewById(R.id.txtEditNumOverWork);
        txtWorkTime = findViewById(R.id.txtEditWork);
        txtOverWork = findViewById(R.id.txtEditOverWork);
        btnWrite = findViewById(R.id.btnEditWrite);
        btnEditBack = findViewById(R.id.btnEditBack);

        Intent intent = getIntent();
        numPosition = intent.getIntExtra("itemPosition",0);

        orderID = intent.getStringExtra("orderID");
        hoursPerMonthID = intent.getIntExtra("hoursPerMonthID",0);

        String numDay = intent.getStringExtra("numDay");
        Employee employee = (Employee) intent.getSerializableExtra("employee");
        workTypeOld = (WorkType) intent.getSerializableExtra("workType") ;
        String workTime = intent.getStringExtra("workTime");
        String overWork = intent.getStringExtra("overWork");

        txtNumDay.setText(numDay);
        txtWorker.setText(employee.toString());
        txtNumWorkTime.setText(workTime);
        txtNumOverWork.setText(overWork);

        txtWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPickDialog(8,0);

            }
        });

        txtOverWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPickDialog(12,1);
            }
        });

        new GetWorkTypeForOrder().execute();

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                WorkType selectedWT = (WorkType) spWorkType.getSelectedItem();
                int workTime = Integer.valueOf(txtNumWorkTime.getText().toString());
                int overWork = Integer.valueOf(txtNumOverWork.getText().toString());

                returnIntent.putExtra("workType",selectedWT);
                returnIntent.putExtra("workTime",workTime);
                returnIntent.putExtra("overWork",overWork);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        btnEditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // arguments isWorkOrOverWork: 0 - for usual work time, 1 - for extra work time
    private void numPickDialog(final int numMax, final int isWorkOrOverWork){
        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.NumberPickerText);
        final NumberPicker numPick = new NumberPicker(cw);
        //final NumberPicker numPick = new NumberPicker(this);
        numPick.setMaxValue(numMax);
        numPick.setMinValue(0);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setView(numPick);
        dialog.setTitle("Выберите время работы").setIcon(R.drawable.ic_dialog);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int hoursFromDialog = numPick.getValue();
                if(isWorkOrOverWork == 0) {
                    txtNumWorkTime.setText(String.valueOf(hoursFromDialog));
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

    private class GetWorkTypeForOrder extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WorkEdit.this,
                    "Получение списка видов работ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getWorkTypeForOrder(URL_WORK_TYPE_FOR_ORDER,
                    String.valueOf(hoursPerMonthID));

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
                MySpinnerAdapter adapter = new MySpinnerAdapter(WorkEdit.this,
                        R.layout.spinner_performers_drop_down,
                        workTypeList);
                spWorkType.setAdapter(adapter);
                //set old value of work type
                for(int i = 0; i < workTypeList.size(); ++i){
                    if(workTypeList.get(i).equals(workTypeOld)){
                        spWorkType.setSelection(i);
                    }
                }

            }
        }

    }

    private class MySpinnerAdapter extends ArrayAdapter<WorkType>{
        private List<WorkType> items;

        public MySpinnerAdapter(Context context, int textViewResourceId,
                                List<WorkType> items){
            super(context, textViewResourceId, items);
            this.items = items;
        }

        public int getCount(){ return items.size(); }

        public WorkType getItem(int pos){ return items.get(pos); }

        public long getItemId(int position){ return position; }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View spView = inflater.inflate(R.layout.spinner_item_for_work_edit,parent,false);

            TextView txtWorkType = spView.findViewById(R.id.txtSprWorkType);
            txtWorkType.setText(items.get(position).toString());

            //return super.getView(position, convertView, parent);
            return spView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View spDropDown = inflater.inflate(R.layout.spinner_item_for_work_edit,parent,false);

            TextView txtWorkType = spDropDown.findViewById(R.id.txtSprWorkType);
            txtWorkType.setText(items.get(position).toString());

            //return super.getView(position, convertView, parent);
            return spDropDown;
        }
    }
}
