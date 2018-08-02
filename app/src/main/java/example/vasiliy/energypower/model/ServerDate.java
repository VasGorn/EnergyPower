package example.vasiliy.energypower.model;

import java.util.Calendar;
import java.util.Date;

public final class ServerDate {
    private static Calendar serverCalender = Calendar.getInstance();

    private ServerDate(){
        //
    }

    public static void setServerDate(Date date){
        //serverCalender = Calendar.getInstance();
        serverCalender.setTime(date);
    }

    public static String getNameOfMonth(){
        String nameMonth;

        int numOfMonth = serverCalender.get(Calendar.MONTH) + 1;

        switch (numOfMonth){
            case 1: nameMonth = "Январь"; break;
            case 2: nameMonth = "Февраль"; break;
            case 3: nameMonth = "Март"; break;
            case 4: nameMonth = "Апрель"; break;
            case 5: nameMonth = "Май"; break;
            case 6: nameMonth = "Июнь"; break;
            case 7: nameMonth = "Июль"; break;
            case 8: nameMonth = "Август"; break;
            case 9: nameMonth = "Сентябрь"; break;
            case 10: nameMonth = "Окрябрь"; break;
            case 11: nameMonth = "Ноябрь"; break;
            case 12: nameMonth = "Декабрь"; break;
            default: nameMonth = "invalid data";
        }

        return nameMonth;
    }

    public static String getNumberOfYear(){
        String nameYear;

        int numOfYear = serverCalender.get(Calendar.YEAR);

        nameYear = String.valueOf(numOfYear);

        return nameYear;
    }

    public static int getDayOfMonth(){
        return serverCalender.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getNumMonth(){
        return (serverCalender.get(Calendar.MONTH) + 1);
    }

    public static int getMaxDaysInMonth(){
        return serverCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
