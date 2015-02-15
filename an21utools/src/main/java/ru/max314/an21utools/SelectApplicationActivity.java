package ru.max314.an21utools;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ru.max314.an21utools.R;
import ru.max314.an21utools.model.IndentActivityCodes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by max on 29.10.2014.
 */
public class SelectApplicationActivity extends Activity {

    private List<ResolveInfo> mApps;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_application);
        // Загрузили приложения
        loadApps();
        dumpData();
        listView = (ListView) findViewById(R.id.select_application_view);
        listView.setAdapter(new AppsAdapter());
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ResolveInfo info = mApps.get(i);
                Intent intent = new Intent();
                intent.putExtra(IndentActivityCodes.SELECT_APPLICATION_KEY, info.activityInfo.packageName);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
        Collections.sort(mApps, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
                return resolveInfo.activityInfo.loadLabel(getPackageManager()).toString().compareToIgnoreCase(resolveInfo2.activityInfo.loadLabel(getPackageManager()).toString());
            }
        });
    }
    private void dumpData(){
        Log.d("SelectApplicationActivity","Dump packages");
        for (int i = 0; i <mApps.size(); i++) {
            Log.d("SelectApplicationActivity",mApps.get(i).activityInfo.packageName);
        }
    }

    public class AppsAdapter extends BaseAdapter {
        public AppsAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item;

                if (convertView == null) {
                    LayoutInflater ltInflater = getLayoutInflater();
                    item  = ltInflater.inflate(R.layout.select_application_item,null);
//                    ResolveInfo info = mApps.get(position);
//                    ((ImageView)item.findViewById(R.id.imageView)).setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
//                    ((TextView)item.findViewById(R.id.appName)).setText(info.activityInfo.loadLabel(getPackageManager()));
//                    ((TextView)item.findViewById(R.id.appVersion)).setText(info.activityInfo.packageName);

                } else {
                    item = convertView;
                }

                ResolveInfo info = mApps.get(position);
                ((ImageView)item.findViewById(R.id.imageView)).setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
                ((TextView)item.findViewById(R.id.appName)).setText(info.activityInfo.loadLabel(getPackageManager()));
                ((TextView)item.findViewById(R.id.appVersion)).setText(info.activityInfo.packageName);
//                ((TextView)item.findViewById(R.id.appVersion)).setText(info.serviceInfo.applicationInfo.loadDescription(getPackageManager()));
                return item;

        }


        public final int getCount() {
            return mApps.size();
        }

        public final Object getItem(int position) {
            return mApps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }


}