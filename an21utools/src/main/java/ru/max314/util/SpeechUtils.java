package ru.max314.util;

import android.speech.tts.TextToSpeech;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


import ru.max314.an21utools.App;
import ru.max314.util.threads.TimerHelper;

/**
 * Created by max on 10.01.2015.
 */
public class SpeechUtils {
    static LogHelper Log = new LogHelper(SpeechUtils.class);
    private static Object locker = new Object();
    private static TextToSpeech textToSpeech;
    private static TimerHelper freeHolder;
    private static String predString = "Хозяин";

    /**
     * Сказать чтонибудь
     * @param text
     */
    public synchronized static void speech(final String text, final boolean speechPredString){
        Log.d("Говорилко хочет говорить:"+text);
        if (true)
         return;
        synchronized (locker){
            if (textToSpeech!=null){
                Log.d("Говорилко попали в кеш:"+text);
                speechInternal(text,speechPredString);
                return;
            }
            textToSpeech = new TextToSpeech(App.getInstance(), new TextToSpeech.OnInitListener() {
                @Override
                public synchronized void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS){
                        Log.d("Говорилко инициализации движка и протзношение:"+text);
                        textToSpeech.setLanguage(new Locale("ru","RU"));
                        textToSpeech.setSpeechRate(0.8f);
                        speechInternal(text,speechPredString);
                    }
                    else {
                        new DisplayToast(App.getInstance(),"Error init test to speech engine: "+status,false).run();
                        Log.e("Говорилко Ошибка инициализации движка:" + status);
                    }
                }
            });

        }
    }
    private synchronized static void speechInternal(final String text, boolean speechPredString){
        Log.d("Говорилко speechInternal: вход -> "+text);
        if (textToSpeech!=null){
            if (speechPredString)
            {
                textToSpeech.speak("Хозяин.",TextToSpeech.QUEUE_ADD, null);
                textToSpeech.playSilence(750, TextToSpeech.QUEUE_ADD, null);
            }
            String[] parts = text.split("\\.");
            boolean firstPart = true;
            for(String part : parts){
                if (firstPart){
                    firstPart = false;
                }else {
                    textToSpeech.playSilence(500, TextToSpeech.QUEUE_ADD, null);
                }
                textToSpeech.speak(part,TextToSpeech.QUEUE_ADD, null);
            }
            textToSpeech.playSilence(500, TextToSpeech.QUEUE_ADD, null);

            Log.d("Говорилко speechInternal: в очереди -> "+text);
            if (freeHolder!=null){
                freeHolder.stop();
                freeHolder=null;
            }
//            freeHolder = new TimerHelper("10 минут на освобождение говорилки",
//                    TimeUnit.MILLISECONDS.convert(10,TimeUnit.MINUTES), // Начинаем через 10 минуту
//                    TimeUnit.MILLISECONDS.convert(30,TimeUnit.MINUTES), // каждые полминуты
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            freeSpeechEngine();
//                        }
//                    });
//            freeHolder.start();
        }
        else {
            Log.e("Говорилко speechInternal: чейта намудрили");
        }

    }

    /**
     * освободить говорилку
     */
    private synchronized static void freeSpeechEngine() {
        Log.d("Говорилко освобождение движка: Вход.");
        synchronized (locker) {
            if (textToSpeech!=null){
                textToSpeech.shutdown();
            }
            textToSpeech = null;
            if (freeHolder!=null)
                freeHolder.stop();
            freeHolder = null;
        }
        Log.d("Говорилко освобождение движка: Успешно.");
    }
}
