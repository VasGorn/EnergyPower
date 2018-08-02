package example.vasiliy.energypower.model;

public class WR_Table {
    private Employee employee;
    private WorkType workType;
    private int numMonth;
    private int numDay;
    private int workHours;
    private int overWorkHours;
    private boolean isSelected;

    public WR_Table(Employee employee, WorkType workType, int numMonth, int numDay, int workHours, int overWorkHours){
        this.employee = employee;
        this.workType = workType;
        this.numMonth = numMonth;

        this.numDay = numDay;
        this.workHours = workHours;
        this.overWorkHours = overWorkHours;
        this.isSelected = false;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public void setNumMonth(int numMonth) {
        this.numMonth = numMonth;
    }

    public void setNumDay(int numDay) {
        this.numDay = numDay;
    }

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }

    public void setOverWorkHours(int overWorkHours) {
        this.overWorkHours = overWorkHours;
    }


    public Employee getEmployee() {
        return employee;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public int getNumMonth() {
        return numMonth;
    }

    public int getNumDay() {
        return numDay;
    }

    public int getWorkHours() {
        return workHours;
    }

    public int getOverWorkHours() {
        return overWorkHours;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o){
        if( o == null){
            return false;
        }else if (o instanceof WR_Table){
            return (this.employee.equals(((WR_Table) o).getEmployee()) && (this.getNumDay() == ((WR_Table) o).getNumDay())&& (this.getNumMonth() == ((WR_Table) o).getNumMonth()));
        }

        return false;

    }
}
