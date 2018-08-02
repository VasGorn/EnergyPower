package example.vasiliy.energypower.model;

import java.io.Serializable;

public class WorkType implements Serializable{
    private int id;
    private String typeName;

    public WorkType(int id, String typeName){
        this.id = id;
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getId(){ return id; }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
