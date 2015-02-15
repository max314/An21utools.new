package ru.max314.util;

/**
 * Created by max on 16.11.2014.
 */
public class StringUtils {


    /***
     * Строка пустая или нулл
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if (str == null) return true;
        if (str.isEmpty()) return true;
        return false;
    }
}
