package example.vasiliy.energypower.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import example.vasiliy.energypower.model.Employee;
import example.vasiliy.energypower.model.EmployeeWithPosition;
import example.vasiliy.energypower.model.User;
import example.vasiliy.energypower.model.WR_Table;
import example.vasiliy.energypower.model.WorkType;


public class JsonParsing {
    public JsonParsing(){}

    public int[] getHoursOnForMaster(String jsonStr){
        int[] hoursArray = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int success = jsonObj.getInt("success");

            if(success == 1) {
                int hoursInQuot = jsonObj.getInt(Const.HOUR_PER_MONTH_HOURS);
                int sumWorkingHours = jsonObj.getInt("sumHours");
                hoursArray = new int[]{hoursInQuot, sumWorkingHours};
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }finally {
            return hoursArray;
        }

    }

    public int getTypeHoursOnForMaster(String jsonStr){
        int sumWorkingHours = 0;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int success = jsonObj.getInt("success");

            if(success == 1) {
                sumWorkingHours = jsonObj.getInt("sumTypeHours");
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }finally {
            return sumWorkingHours;
        }

    }

    public int[] getHoursOnOrder(String jsonStr){
        int[] hoursArray = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int success = jsonObj.getInt("success");

            if(success == 1) {
                int hoursPerMonthID = jsonObj.getInt(Const.HOUR_PER_MONTH_ID);
                int hoursInQuot = jsonObj.getInt(Const.HOUR_PER_MONTH_HOURS);
                int sumWorkingHours = jsonObj.getInt("sumHours");
                hoursArray = new int[]{hoursPerMonthID, hoursInQuot, sumWorkingHours};
            }
        } catch (final JSONException e) {
                e.printStackTrace();
        }finally {
            return hoursArray;
        }

    }


    public List<Employee> getTeamForMaster(String jsonStr){
        List<Employee> teamList;

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int success = jsonObj.getInt("success");

            if(success == 1) {
                JSONArray team = jsonObj.getJSONArray("team");

                teamList = new ArrayList<>();

                for (int i = 0; i < team.length(); i++) {
                    JSONObject o = team.getJSONObject(i);
                    String id = o.getString(Const.EMPLOYEE_ID);
                    String lastName = o.getString(Const.EMPLOYEE_LASTNAME);
                    String firstName = o.getString(Const.EMPLOYEE_FIRSTNAME);
                    String middleName = o.getString(Const.EMPLOYEE_MIDDLENAME);

                    Employee newWorker = new Employee(lastName, firstName, middleName, Integer.valueOf(id));

                    teamList.add(newWorker);
                }

                return teamList;
            }else{
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<WR_Table> getWorkToApprove(String jsonStr){
        ArrayList<WR_Table> workList;

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int success = jsonObj.getInt("success");

            if(success == 1) {
                JSONArray work = jsonObj.getJSONArray("work");

                workList = new ArrayList<>();

                for (int i = 0; i < work.length(); i++) {
                    JSONObject o = work.getJSONObject(i);
                    String id = o.getString(Const.EMPLOYEE_ID);
                    String lastName = o.getString(Const.EMPLOYEE_LASTNAME);
                    String firstName = o.getString(Const.EMPLOYEE_FIRSTNAME);
                    String middleName = o.getString(Const.EMPLOYEE_MIDDLENAME);

                    Employee worker = new Employee(lastName, firstName, middleName, Integer.valueOf(id));

                    int wtID = o.getInt(Const.WORK_TYPE_ID);
                    String wtName = o.getString(Const.WORK_TYPE_NAME);

                    WorkType workType = new WorkType(wtID, wtName, 0);

                    int numMonth = o.getInt(Const.HOUR_PER_MONTH_QUOT_NUM_MONTH);
                    int numDay = o.getInt(Const.WORK_TIME_NUM_DAY);
                    int workTime = o.getInt(Const.WORK_TIME_WORK_TIME);
                    int overWork = o.getInt(Const.WORK_TIME_OVER_TIME);

                    workList.add(new WR_Table(worker,workType,numMonth,numDay,workTime,overWork));
                }

                return workList;
            }else{
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EmployeeWithPosition> getPerformersList(String jsonStr){
        List<EmployeeWithPosition> list;

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int success = jsonObj.getInt("success");

            if(success == 1) {
                JSONArray performers = jsonObj.getJSONArray("performers");

                list = new ArrayList<>();

                for (int i = 0; i < performers.length(); i++) {
                    JSONObject o = performers.getJSONObject(i);
                    String id = o.getString(Const.EMPLOYEE_ID);
                    String lastName = o.getString(Const.EMPLOYEE_LASTNAME);
                    String firstName = o.getString(Const.EMPLOYEE_FIRSTNAME);
                    String middleName = o.getString(Const.EMPLOYEE_MIDDLENAME);
                    String position = o.getString(Const.POSITION_NAME);

                    Employee newWorker = new Employee(lastName, firstName, middleName, Integer.valueOf(id));
                    EmployeeWithPosition newObj = new EmployeeWithPosition(newWorker,position);
                    list.add(newObj);
                }

                return list;
            }else{
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<WorkType> getWorkTypeForOrder(String jsonStr){
        ArrayList<WorkType> workTypeList;

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int success = jsonObj.getInt("success");

            if(success == 1) {
                JSONArray work_type = jsonObj.getJSONArray("work_type");

                workTypeList = new ArrayList<>();

                for (int i = 0; i < work_type.length(); i++) {
                    JSONObject o = work_type.getJSONObject(i);
                    int id = o.getInt(Const.WORK_TYPE_ID);
                    String workTypeName = o.getString(Const.WORK_TYPE_NAME);
                    int hours = o.getInt(Const.ORDER_HAS_WORKTYPE_HOURS);

                    WorkType newWorkType = new WorkType(id, workTypeName, hours);

                    workTypeList.add(newWorkType);
                }

                return workTypeList;
            }else{
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee getCurrentEmployee(String jsonStr){
        Employee newEmployee = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            String lastName = jsonObj.getString(Const.EMPLOYEE_LASTNAME);
            String firstName = jsonObj.getString(Const.EMPLOYEE_FIRSTNAME);
            String middleName = jsonObj.getString(Const.EMPLOYEE_MIDDLENAME);
            newEmployee = new Employee(lastName, firstName, middleName, User.getId());
        } catch (final JSONException e) {
            e.printStackTrace();
        }finally {
            return newEmployee;
        }

    }

    public int getSuccess(String jsonStr){
        int success = 0;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            success = jsonObj.getInt("success");

        } catch (final JSONException e) {
            e.printStackTrace();
        }finally {
            return success;
        }

    }
}
