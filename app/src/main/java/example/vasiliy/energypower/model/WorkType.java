package example.vasiliy.energypower.model;

import java.io.Serializable;

public class WorkType implements Serializable{
    private int id;
    private String typeName;
    private int hours;
    private int sumHours;

    public WorkType(int id, String typeName, int hours){
        this.id = id;
        this.typeName = typeName;
        this.hours = hours;
        this.sumHours = 0;
    }

    public WorkType(int id, String typeName, int hours, int sumHours){
        this.id = id;
        this.typeName = typeName;
        this.hours = hours;
        this.sumHours = sumHours;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getId(){ return id; }

    public int getHours() { return hours; }

    public void setHours(int hours) { this.hours = hours;}

    public int getSumHours() { return hours; }

    public void setSumHours(int sumHours) { this.sumHours = sumHours;}

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getProgress(){
        double beta = sumHours/((double)hours) * 100.0;
        int progress = (int)Math.round(beta);
        return progress;
    }

    @Override
    public String toString(){ return typeName; }

    @Override
    public boolean equals(Object o){
        if( o == null){
            return false;
        }else if (o instanceof WorkType){
            return this.typeName.equals(((WorkType) o).getTypeName());
        }

        return false;
    }
}
