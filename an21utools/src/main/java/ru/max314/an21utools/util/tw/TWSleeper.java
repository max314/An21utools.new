package ru.max314.an21utools.util.tw;



import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import ru.max314.an21utools.App;
import ru.max314.an21utools.util.LogHelper;

/**
 * Created by max on 15.02.2015.
 */
public class TWSleeper extends TWUtilDecorator {
    final static LogHelper Log = new LogHelper(TWSleeper.class);
    private static final String HANDLERR_TAG = "sleep_listener";
    private static final String BRD_TAG_SLEEP = "ru.max314.an21utools.sleep";
    private static final String BRD_TAG_WAKEUP = "ru.max314.an21utools.wakeup";
    private static final String BRD_TAG_SHUTDOWN = "ru.max314.an21utools.shutdown";
    Handler handler;

    public TWSleeper() {
        super(new short[]{514});
        Log.d("TWSleeper.ctor");
        // getTwUtil().write(514, 3); // нахуя  - хуйего знает
        // не нужно так делать

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d("Handle message from TWutil: "+ dumpMessage(msg));
                switch (msg.what){
                    case TWU_CODE_SLEEP:
                        switch (msg.arg1){
                            case 3:
                                createAndSayIntent(BRD_TAG_SLEEP);
                                break;
                            default:
                                break;
                        }


                        break;
                    case TWU_CODE_REQUEST_SHUTDOWN:
                        createAndSayIntent(BRD_TAG_SHUTDOWN);
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
    }

    private void createAndSayIntent(String action){
        Log.d("say broadcast:"+action);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(action);
        App.getInstance().sendBroadcast(intent);
    }

    @Override
    public void end() {
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