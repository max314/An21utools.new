package ru.max314.util.tw;

import android.tw.john.TWUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ru.max314.util.LogHelper;

/**
 * Created by max on 14.02.2015.
 * Декоратор над TWutil
 * поскольку я не понмаю нахуя они его делают статиком
 * будем делать по человечески
 */
public class TWUtilDecorator {
    private static final int TWU_CODE_GET_ID = 65521;

    static LogHelper Log = new LogHelper(TWUtilDecorator.class);

    private TWUtil twUtil;

    protected TWUtil getTwUtil() {
        if (twUtil==null) throw new RuntimeException("Ошибка в использовании  класса twUtil==null");
        return twUtil;
    }

    public void TWUtilDecorator(){
        int result = twUtil.open(null);
        if (result!=0)
            throw new RuntimeException("Ошибка открытия TWUtil. статус код:"+result);
        twUtil.start();
    }

    public void end(){
        twUtil.stop();
        twUtil.close();
    }

    /**
     * Доступен класс twutil
     * @return
     */
    public static boolean isAvailable(){
        try {
            TWUtil test = new TWUtil();
            test = null;
            return true;
        }
        catch (Throwable ex){
            Log.e("isAvaible()==false",ex);
            return false;
        }
    }

    /**
     * Получить идентификатор производителя ГУ
     * @return
     */
    public static int getCarDeviceID(){
        TWUtilDecorator decorator = new TWUtilDecorator();
        try {
            int result = decorator.getTwUtil().write(TWU_CODE_GET_ID);
            Log.d("getCarDeviceID() == "+result);
            return result;
        }
        finally {
            decorator.end();
        }
    }

    /**
     * Строковое представление производителя
     * @param id
     * @return
     */
    public static String getCarDeviceString(int id){
        IdToNamePair[] list = new IdToNamePair[]{
                        new TWUtilDecorator().new IdToNamePair(7,"МоЁ"),
                        new TWUtilDecorator().new IdToNamePair(17,"RedPower")};

        for (IdToNamePair item : list){
            if (item.ID == id)
                return item.Name;
        }
        return "Не известно";
    }


    private class IdToNamePair{
        private IdToNamePair(int ID, String name) {
            this.ID = ID;
            Name = name;
        }

        public int ID;
        public String Name;
    }


}


