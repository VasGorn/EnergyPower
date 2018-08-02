package example.vasiliy.energypower.model;

public class QuotTable {
    private Employee employee;

    private QuotTable.columnMonth   january,    february,
            march,      april,      may,
            june,       july,       august,
            september,  october,    november,
            december;

    public QuotTable(Employee fName){
        this.employee = fName;


        january     = new QuotTable.columnMonth();  february    = new QuotTable.columnMonth();
        march       = new QuotTable.columnMonth();  april       = new QuotTable.columnMonth();
        may         = new QuotTable.columnMonth();  june        = new QuotTable.columnMonth();
        july        = new QuotTable.columnMonth();  august      = new QuotTable.columnMonth();
        september   = new QuotTable.columnMonth();  october     = new QuotTable.columnMonth();
        november    = new QuotTable.columnMonth();  december    = new QuotTable.columnMonth();

    }

    public void setEmployee(Employee employee){
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }


    public void setHoursOnMonth(int monthNumber, int hours){
        switch (monthNumber){
            case 1:     january.setHoursOnMonth(hours); break;
            case 2:     february.setHoursOnMonth(hours); break;
            case 3:     march.setHoursOnMonth(hours); break;
            case 4:     april.setHoursOnMonth(hours); break;
            case 5:     may.setHoursOnMonth(hours); break;
            case 6:     june.setHoursOnMonth(hours); break;
            case 7:     july.setHoursOnMonth(hours); break;
            case 8:     august.setHoursOnMonth(hours); break;
            case 9:     september.setHoursOnMonth(hours); break;
            case 10:    october.setHoursOnMonth(hours); break;
            case 11:    november.setHoursOnMonth(hours); break;
            case 12:    december.setHoursOnMonth(hours); break;
        }
    }

    public int getHoursOnMonth(int numMonth){
        int hours = 0;
        switch (numMonth){
            case 1:     hours = january.getHoursOnMonth(); break;
            case 2:     hours = february.getHoursOnMonth(); break;
            case 3:     hours = march.getHoursOnMonth(); break;
            case 4:     hours = april.getHoursOnMonth(); break;
            case 5:     hours = may.getHoursOnMonth(); break;
            case 6:     hours = june.getHoursOnMonth(); break;
            case 7:     hours = july.getHoursOnMonth(); break;
            case 8:     hours = august.getHoursOnMonth(); break;
            case 9:     hours = september.getHoursOnMonth(); break;
            case 10:    hours = october.getHoursOnMonth(); break;
            case 11:    hours = november.getHoursOnMonth(); break;
            case 12:    hours = december.getHoursOnMonth(); break;
        }
        return  hours;
    }

    public columnMonth getJanuary() {
        return january;
    }

    public columnMonth getFebruary() {
        return february;
    }

    public columnMonth getMarch() {
        return march;
    }

    public columnMonth getApril() {
        return april;
    }

    public columnMonth getMay() {
        return may;
    }

    public columnMonth getJune() {
        return june;
    }

    public columnMonth getJuly() {
        return july;
    }

    public columnMonth getAugust() {
        return august;
    }

    public columnMonth getSeptember() {
        return september;
    }

    public columnMonth getOctober() {
        return october;
    }

    public columnMonth getNovember() {
        return november;
    }

    public columnMonth getDecember() {
        return december;
    }

    public class columnMonth{
        private int hoursOnMonth;

        public columnMonth(){
            this.hoursOnMonth = 0;
        }

        public void setHoursOnMonth(int hoursOnMonth) {
            this.hoursOnMonth = hoursOnMonth;
        }

        public int getHoursOnMonth(){ return hoursOnMonth; }

        @Override
        public String toString() {
            if (hoursOnMonth < 1) {
                return "";
            } else {
                return String.valueOf(hoursOnMonth);
            }
        }
    }
}
