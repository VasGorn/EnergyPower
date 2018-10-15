package example.vasiliy.energypower.http;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    /*public String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }*/

    public String postLogin(String reqUrl, String username, String password){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "username=" + URLEncoder.encode(username,"UTF-8") +
                    "&password=" + URLEncoder.encode(password,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String postGetOrdersForMaster(String reqUrl, String employeeID, String numMonth){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "employeeID=" + URLEncoder.encode(employeeID,"UTF-8") +
                    "&numMonth=" + URLEncoder.encode(numMonth,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getOrdersForManager(String reqUrl, String employeeID, String numMonth){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "managerID=" + URLEncoder.encode(employeeID,"UTF-8") +
                    "&numMonth=" + URLEncoder.encode(numMonth,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getHoursOnOrder(String reqUrl, String hoursPerMonthID){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "hoursPerMonthID=" + URLEncoder.encode(hoursPerMonthID,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public String getTypeHoursOnOrder(String reqUrl, String hoursPerMonthID, String workTypeID){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "hoursPerMonthID=" + URLEncoder.encode(hoursPerMonthID,"UTF-8")+
                    "&workTypeID=" + URLEncoder.encode(workTypeID,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getHoursForPerformer(String reqUrl, String orderID, String employeeID, String numMonth){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "orderID=" + URLEncoder.encode(orderID,"UTF-8")+
                    "&employeeID=" + URLEncoder.encode(employeeID,"UTF-8")+
                    "&numMonth=" + URLEncoder.encode(numMonth,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getWorkToApprove(String reqUrl, String hoursPerMonthID){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "hoursPerMonthID=" + URLEncoder.encode(hoursPerMonthID,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getPerformersOnOrder(String reqUrl, String orderID){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "orderID=" + URLEncoder.encode(orderID,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getTeamForMaster(String reqUrl, String masterID){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "masterID=" + URLEncoder.encode(masterID,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getWorkTypeForOrder(String reqUrl, String hoursPerMonthID){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "hoursPerMonthID=" + URLEncoder.encode(hoursPerMonthID,"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getEmployeeByID(String reqUrl, int id){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "id=" + URLEncoder.encode(String.valueOf(id),"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String sendWorkTimeToInsert(String reqUrl, JSONObject jsonObject){
        String response = null;
        try {
            URL url = new URL(reqUrl);
            String param = "json_work_time=" + URLEncoder.encode(jsonObject.toString(),"UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    //---------------------------------------------------------------------------------------
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
