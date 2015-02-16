package ru.max314.an21utools.util;

import java.io.File;
import java.io.FileFilter;

import ru.max314.an21utools.App;


/**
 * Created by max on 15.01.2015.
 */
public class AppUtils {

    /**
     * Очистить лог файлы в заданном каталоге
     */
    private static void clearLogFiles(String dir){

        File[] list = new File(dir).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".log");
            }
        });
        for (File file:list){
            file.delete();
        }
    }
    /**
     * Очистить лог файлы
     */
    public static void clearLogFiles(){
        clearLogFiles(App.getInstance().getFilesDir().getPath());
        clearLogFiles(App.getInstance().getFilesDir().getPath()+"/log/");
    }

}
