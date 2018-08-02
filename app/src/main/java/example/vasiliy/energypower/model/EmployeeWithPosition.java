package example.vasiliy.energypower.model;

public class EmployeeWithPosition {
    private Employee employee;
    private String positionName;

    public EmployeeWithPosition(Employee employee, String position){
        this.employee = employee;
        this.positionName = position;
    }
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
