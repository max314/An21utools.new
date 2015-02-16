package ru.max314.an21utools;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import ru.max314.an21utools.util.LogHelper;

public class MainActivity extends TabActivity {
    private static LogHelper Log = new LogHelper(MainActivity.class);
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Start application");
        setContentView(R.layout.main);
        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("auto");
        tabSpec.setIndicator("Автозапуск");
        tabSpec.setContent(new Intent(this, AutoRunActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("about");
        tabSpec.setIndicator("О программе");
        tabSpec.setContent(new Intent(this, AboutActivity.class));
        tabHost.addTab(tabSpec);
        Log.d("MainActivity Init...");
    }
}
