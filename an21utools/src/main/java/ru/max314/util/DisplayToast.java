package ru.max314.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by max on 11.11.2014.
 */
public class DisplayToast  implements Runnable {

    private final Context mContext;
    String mText;
    int duration;

    public DisplayToast(Context mContext, String text, boolean isShort){
        this.mContext = mContext;
        mText = text;
        if (isShort)
            duration = Toast.LENGTH_SHORT;
        else
            duration = Toast.LENGTH_LONG;
    }

    public void run(){
        Toast.makeText(mContext, mText, this.duration).show();
    }
}
