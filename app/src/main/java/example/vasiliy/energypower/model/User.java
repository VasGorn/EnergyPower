package example.vasiliy.energypower.model;

import java.util.ArrayList;

public final class User {
    private static int id;
    private static String userName;
    private static String password;

    private static int employeeID;

    private static String nameEmployee;
    private static Employee employee;
    private static ArrayList<String> position;
    private static ArrayList<Employee> listOfWorkers;
    private static ArrayList<Order> listOrdersMaster;
    private static ArrayList<Order> listOrdersManager;



    private static boolean engineer;
    private static boolean master;
    private static boolean manager;
    private static boolean admin;

    public static boolean isEngineer() {
        return engineer;
    }

    public static void setEngineer(boolean engineer) {
        User.engineer = engineer;
    }

    public static boolean isMaster() {
        return master;
    }

    public static void setMaster(boolean master) {
        User.master = master;
    }

    public static boolean isManager() {
        return manager;
    }

    public static void setManager(boolean manager) {
        User.manager = manager;
    }

    public static boolean isAdmin() {
        return admin;
    }

    public static void setAdmin(boolean admin) {
        User.admin = admin;
    }

    public static Employee getEmployee() {
        return employee;
    }

    public static void setEmployee(Employee employee) {
        User.employee = employee;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int idIn) {
        id = idIn;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userNameIn) {
        userName = userNameIn;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String passwordIn) {
        password = passwordIn;
    }

    public static int getEmployeeID() {
        return employeeID;
    }

    public static void setEmployeeID(int newEmployeeID) {
        employeeID = newEmployeeID;
    }

    public static String getNameEmployee(){
        return nameEmployee;
    }

    public static void setNameEmployee(String lastName, String firstName, String middleName){
        nameEmployee = lastName + " " + firstName + " " + middleName;
    }

    public static ArrayList<String> getPosition(){
        return position;
    }

    public static void setPosition(ArrayList<String> newPosition){
        position = newPosition;
    }

    public static ArrayList<Employee> getWorkersList(){
        return  listOfWorkers;
    }

    public static void setWorkers(ArrayList<Employee> newWorkers){
        listOfWorkers = newWorkers;
    }

    public static void setListOrdersMaster(ArrayList<Order> newListOrders){ listOrdersMaster = newListOrders;}

    public static ArrayList<Order> getListOrdersMaster(){ return listOrdersMaster; }

    public static ArrayList<Order> getListOrdersManager() {
        return listOrdersManager;
    }

    public static void setListOrdersManager(ArrayList<Order> listOrdersManager) {
        User.listOrdersManager = listOrdersManager;
    }
}
