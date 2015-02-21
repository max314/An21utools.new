package ru.max314.an21utools.util.tw;



import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.tw.john.TWUtil;

import ru.max314.an21utools.App;
import ru.max314.an21utools.util.LogHelper;
import ru.max314.an21utools.util.SysUtils;

/**
 * Created by max on 15.02.2015.
 */
public class TWSleeper extends TWUtilDecorator {
    final static LogHelper Log = new LogHelper(TWSleeper.class);
    private static final String HANDLERR_TAG = "sleep_listener";
    public static final String BRD_TAG_SLEEP = "ru.max314.an21utools.sleep";
    public static final String BRD_TAG_WAKEUP = "ru.max314.an21utools.wakeup";
    public static final String BRD_TAG_SHUTDOWN = "ru.max314.an21utools.shutdown";
    Handler handler;

    public TWSleeper() {
        super(new short[]{
                514,
                -24816 // Это шутдоун 40720
        });
        Log.d("TWSleeper.ctor");

        // не нужно так делать

        handler = new Handler(){
            // Для того чтобы не слать два раза бродкаст - мы проснулись
            // заводим переменую
            // Улучшим алгоритм мы проснулись приходит только если перед этим был фак ухода в сон
            // Игнрорировать следующий приход 514 - 3.0
            private boolean inSleepMode = false;

            @Override
            public void handleMessage(Message msg) {
                Log.d("Handle message from TWutil: "+ dumpMessage(msg));
                switch (msg.what){
                    case TWU_CODE_SLEEP:
                        if (msg.arg1 == 3){
                            switch (msg.arg2){
                                case 1: // Уход в слип
                                    SysUtils.createAndSayBrodcastIntent(BRD_TAG_SLEEP);
                                    inSleepMode = true; // просыпания без сна не бывает)
                                    break;
                                case 0:
                                    if (inSleepMode){
                                        SysUtils.createAndSayBrodcastIntent(BRD_TAG_WAKEUP);
                                        inSleepMode = false;
                                        break;
                                    }
                                    inSleepMode = false; // В принцип это безполезноый код ибо уход в сон сделает тоже самое
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    // not work
//                    case TWU_CODE_REQUEST_SHUTDOWN:
//                        SysUtils.createAndSayBrodcastIntent(BRD_TAG_SHUTDOWN);
//                        break;
                    case TWU_CODE_REQUEST_SHUTDOWN1:
                        SysUtils.createAndSayBrodcastIntent(BRD_TAG_SHUTDOWN);
                        break;
                    default:
                        break;
                }

            }

            private String dumpMessage(Message mes){
                StringBuilder   b = new StringBuilder();

                b.append("{ what=");
                b.append(mes.what);

                    b.append(" arg1=");
                    b.append(mes.arg1);


                    b.append(" arg2=");
                    b.append(mes.arg2);
                b.append(" }");
                return b.toString();
            }
        };
        getTwUtil().addHandler(HANDLERR_TAG, handler);
        Log.d("TWSleeper.Listen handler");
        Log.d("Получим HD ID  для проверки");
        int id = TWUtilDecorator.getCarDeviceID();
    }


    @Override
    public void end() {
        Log.d("TWSleeper end()");
        getTwUtil().removeHandler(HANDLERR_TAG);
        super.end();
    }
}
/*
                case 514:
                    switch (message.arg1) {
                        case 1:
                            this.a.k.write(40720, 0, 2000);
                            this.a.i(this.a.n);
                            this.a.k.write(40730, 1);
                        case 3:
                            if (this.a.s != message.arg2) {
                                this.a.s = message.arg2;
                                switch (this.a.s) {
                                    case 0:
                                        this.a.resume(this.a.n);
                                    case 1:
                                        this.a.i(this.a.n);
                                    default:
                                        break;
                                }
                            }
                        default:
                            break;

 */