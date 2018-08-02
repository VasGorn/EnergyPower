package example.vasiliy.energypower;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import example.vasiliy.energypower.http.Const;
import example.vasiliy.energypower.http.HttpHandler;
import example.vasiliy.energypower.model.ServerDate;
import example.vasiliy.energypower.model.User;

public class ListOrders extends AppCompatActivity {
    private String TAG = ListOrders.class.getSimpleName();
    private ListView lv;

    private final String URL_ORDERS_FOR_MASTER = Const.URL_SERVER + "/master/get_orders_for_master.php";
    private final String URL_ORDERS_FOR_MANAGER = Const.URL_SERVER + "/manager/get_active_orders_for_manager.php";

    ArrayList<HashMap<String, String>> orderList;

    private boolean isManager = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_orders_for_master);

        orderList = new ArrayList<HashMap<String, String>>();
        lv = (ListView) findViewById(R.id.orderList);

        if(User.isMaster()) {
            new GetOrders().execute();
        }else if(User.isManager()){
            new GetOrdersForManager().execute();
            isManager = true;
        }else if(User.isEngineer()){
            new GetOrders().execute();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(!isManager) {
                    // getting values from selected ListItem
                    HashMap<String, String> map = orderList.get(position);

                    Intent intent = new Intent(ListOrders.this, WorkMaster.class);
                    intent.putExtra(Const.ORDER_ID, map.get(Const.ORDER_ID));
                    intent.putExtra(Const.ORDER_NAME, map.get(Const.ORDER_NAME));
                    intent.putExtra("hours_per_month_id", map.get("hours_per_month_id"));
                    startActivity(intent);
                }else{
                    HashMap<String, String> map = orderList.get(position);

                    Intent intent = new Intent(ListOrders.this, WorkList.class);
                    intent.putExtra(Const.ORDER_ID, map.get(Const.ORDER_ID));
                    intent.putExtra(Const.ORDER_NAME, map.get(Const.ORDER_NAME));
                    startActivity(intent);
                }

            }
        });
    }

    private class GetOrders extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListOrders.this,
                    "Идет загрузка данных...", Toast.LENGTH_SHORT).show();
            /*pDialog = new ProgressDialog(ListOrders.this);
            pDialog.setMessage("Загрузка списка заказа...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            //String jsonStr = sh.makeServiceCall(URL_ORDERS_FOR_MASTER);

            Log.e(TAG, "month:" + ServerDate.getNumMonth());

            String jsonStr = sh.postGetOrdersForMaster(URL_ORDERS_FOR_MASTER, String.valueOf(User.getId()),String.valueOf(ServerDate.getNumMonth()));

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    int success = jsonObj.getInt("success");

                    if(success == 1) {
                        JSONArray orders = jsonObj.getJSONArray("orders");
                        //JSONArray orders = new JSONArray(jsonStr);

                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject o = orders.getJSONObject(i);
                            String id = o.getString(Const.ORDER_ID);
                            String name = o.getString(Const.ORDER_NAME);
                            String adress = o.getString(Const.ORDER_ADRESS);
                            String descr = o.getString(Const.ORDER_DERSCRIPTION);
                            String managerID = o.getString(Const.ORDER_MANAGER_ID);
                            String maxHours = o.getString(Const.ORDER_MAX_HOURS);
                            String mgrLastName = o.getString(Const.EMPLOYEE_LASTNAME);
                            String mgrFirstName = o.getString(Const.EMPLOYEE_FIRSTNAME);
                            String mgrMiddleName = o.getString(Const.EMPLOYEE_MIDDLENAME);
                            String hoursPerMonth = o.getString("hours_per_month_id");

                            HashMap<String, String> order = new HashMap<>();

                            order.put(Const.ORDER_ID, id);
                            order.put(Const.ORDER_NAME, name);
                            order.put(Const.ORDER_ADRESS, adress);
                            order.put(Const.ORDER_DERSCRIPTION, descr);
                            order.put(Const.ORDER_MANAGER_ID, managerID);
                            order.put(Const.ORDER_MAX_HOURS, maxHours);

                            String fullName = mgrLastName + " " + mgrFirstName + " " + mgrMiddleName;
                            order.put("fullName", fullName);
                            order.put("hours_per_month_id", hoursPerMonth);

                            orderList.add(order);
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Нет активных заказов!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (final JSONException e) {
                    e.printStackTrace();
                   Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
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
            //pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(ListOrders.this, orderList,
                    R.layout.order_item, new String[]{Const.ORDER_NAME, Const.ORDER_ADRESS, Const.ORDER_DERSCRIPTION, "fullName"},
                    new int[]{R.id.orderName, R.id.orderAdress, R.id.orderDescription, R.id.orderManager});
            lv.setAdapter(adapter);
        }

    }

    private class GetOrdersForManager extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListOrders.this,
                    "Идет загрузка данных...", Toast.LENGTH_SHORT).show();
            /*pDialog = new ProgressDialog(ListOrders.this);
            pDialog.setMessage("Загрузка списка заказа...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            //String jsonStr = sh.makeServiceCall(URL_ORDERS_FOR_MASTER);

            Log.e(TAG, "month:" + ServerDate.getNumMonth());

            String jsonStr = sh.getOrdersForManager(URL_ORDERS_FOR_MANAGER, String.valueOf(User.getId()),String.valueOf(ServerDate.getNumMonth()));

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    int success = jsonObj.getInt("success");

                    if(success == 1) {
                        JSONArray orders = jsonObj.getJSONArray("orders");
                        //JSONArray orders = new JSONArray(jsonStr);

                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject o = orders.getJSONObject(i);
                            String id = o.getString(Const.ORDER_ID);
                            String name = o.getString(Const.ORDER_NAME);
                            String adress = o.getString(Const.ORDER_ADRESS);
                            String descr = o.getString(Const.ORDER_DERSCRIPTION);
                            String maxHours = o.getString(Const.ORDER_MAX_HOURS);

                            HashMap<String, String> order = new HashMap<>();

                            order.put(Const.ORDER_ID, id);
                            order.put(Const.ORDER_NAME, name);
                            order.put(Const.ORDER_ADRESS, adress);
                            order.put(Const.ORDER_DERSCRIPTION, descr);
                            order.put(Const.ORDER_MAX_HOURS, maxHours);

                            orderList.add(order);
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Нет активных заказов!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (final JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
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
            //pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(ListOrders.this, orderList,
                    R.layout.order_manager_item_list, new String[]{Const.ORDER_NAME, Const.ORDER_ADRESS, Const.ORDER_DERSCRIPTION},
                    new int[]{R.id.orderName, R.id.orderAdress, R.id.orderDescription});
            lv.setAdapter(adapter);
        }

    }
}
