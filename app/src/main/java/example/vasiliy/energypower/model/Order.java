package example.vasiliy.energypower.model;

import java.util.ArrayList;

public class Order {
    private int id;
    private String nameOrder;
    private String description;
    private String address;
    private String managerName;
    private int hoursMax;
    private int sumWorkHours;
    private ArrayList<WorkType> workTypeList;
    private ArrayList<int[]> listMonthQout;

    private ArrayList<WR_Table> listForWorkTable;

    public Order(int id, String nameOrder, String address , String description, String managerName, int max_hours){
        this.id = id;
        this.nameOrder = nameOrder;
        this.description = description;
        this.address = address;
        this.managerName = managerName;
        this.hoursMax = max_hours;
    }

    public int getId() { return id; }

    public String getNameOrder(){ return this.nameOrder; }

    public String getAddress() { return  this.address; }

    public String getDescription() { return this.description; }

    public String getManagerName() { return  this.managerName; }

    public String getStringMaxHours() { return String.valueOf(this.hoursMax); }

    public int getSumWorkHours(){ return this.sumWorkHours;}

    public int getHoursMax() { return hoursMax; }

    public ArrayList<WorkType> getWorkTypeList() { return workTypeList; }

    public ArrayList<int[]> getListMonthQout() { return listMonthQout; }

    public int getHoursByMonth(int numMonth) {
        for(int[] i: listMonthQout){
            if (i[0] == numMonth)
                return i[1];
        }
        return 0;
    }

    public ArrayList<WR_Table> getListForWorkTable() {
        return listForWorkTable;
    }

    public void setId(int id) { this.id = id; }

    public void setNameOrder(String nameOrder) { this.nameOrder = nameOrder; }

    public void setDescription(String description) { this.description = description; }

    public void setAddress(String address) { this.address = address; }

    public void setHoursMax(int hoursMax) { this.hoursMax = hoursMax; }

    public void setWorkTypeList(ArrayList<WorkType> newList) { workTypeList = newList; }

    public void setListMonthQout(ArrayList<int[]> newList){ listMonthQout = newList; }

    public void setListOfWorkTable(ArrayList<WR_Table> newList){ listForWorkTable = newList; }

    public void setSumWorkHours(int sumWorkHours){ this.sumWorkHours = sumWorkHours; }

    public void addToSumWorkHours(int toAdd){ this.sumWorkHours = this.sumWorkHours + toAdd; }

    @Override
    public String toString() { return (this.nameOrder + " " + description);}

    @Override
    public boolean equals(Object o){
        if( o == null){
            return false;
        }else if (o instanceof Order){
            return this.nameOrder.equals(((Order) o).getNameOrder());
        }

        return false;
    }
}
