package ru.max314.an21utools.util.tw;

import android.tw.john.TWUtil;

import java.lang.reflect.Type;

import ru.max314.an21utools.util.LogHelper;

/**
 * Created by max on 14.02.2015.
 * Декоратор над TWutil
 * поскольку я не понмаю нахуя они его делают статиком
 * будем делать по человечески
 */
public class TWUtilDecorator {
    protected static final int TWU_CODE_GET_ID = 65521;
    protected static final int TWU_CODE_SLEEP = 514;
    protected static final int TWU_CODE_REQUEST_SHUTDOWN = 65289;
    protected static final int TWU_CODE_REQUEST_SHUTDOWN1 = 40720;

    static LogHelper Log = new LogHelper(TWUtilDecorator.class);

    private TWUtil twUtil;

    protected TWUtil getTwUtil() {
        if (twUtil==null) throw new RuntimeException("Ошибка в использовании  класса twUtil==null");
        return twUtil;
    }

    public TWUtilDecorator(){
        this(null);
    }

    public TWUtilDecorator(short[] shorts){
        Log.d("TWUtilDecorator - enter");
        twUtil = new TWUtil();
        int result = twUtil.open(shorts);
        if (result!=0)
            throw new RuntimeException("Ошибка открытия TWUtil. статус код:"+result);
        twUtil.start();
        Log.d("TWUtilDecorator - out");
    }

    public void end(){
        Log.d("TWUtilDecorator.end() - enter");
        twUtil.stop();
        twUtil.close();
        twUtil = null;
        Log.d("TWUtilDecorator.end() - out");
    }

    /**
     * Доступен класс twutil
     * @return
     */
    public static boolean isAvailable(){
        try {
            Type type = TWUtil.class;
            String name = type.getClass().getName();
            return true;
        }
        catch (Throwable ex){
            Log.e("isAvailable()==false",ex);
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


