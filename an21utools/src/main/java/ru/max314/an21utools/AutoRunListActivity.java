package ru.max314.an21utools;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ru.max314.an21utools.model.AppInfo;
import ru.max314.an21utools.model.IndentActivityCodes;
import ru.max314.an21utools.util.LogHelper;

/**
 * Активити для запускаемых приложений
 * Created by max on 28.10.2014.
 */
public class AutoRunListActivity extends Activity {
    private static LogHelper Log = new LogHelper(AutoRunListActivity.class);

    private final int CM_DELETE_ID = 1;
    private final int CM_UP_ID = 2;
    private final int CM_DOWN_ID = 3;
    private ArrayList<String> model;
    private Button buAddAplication;
    private ListView listviewAutoRun;

    private AppInfoAdapter appInfoAdapter;

    /**
     * Флаг указывающий что модель считываеться и в этот момент ненадо обрабатывать листененры привет андройдику
     */
    private boolean insideReadFromModel = false;

    /**
     * Сдвинуть приложение вверх по спискау
     * @param index
     */
    private void shiftUpAppinfo(int index){
        if (index<=0)
            return;
        this.swapAppInfo(index - 1, index);
    }
    /**
     * Сдвинуть приложение вниз по списку
     * @param index
     */
    private void shiftDownAppinfo(int index){
        if (index>=model.size()-1)
            return;
        this.swapAppInfo(index, index + 1);
    }

    /**
     * Поменять 2 элемента массива
     * @param index1
     * @param index2
     */
    private void swapAppInfo(int index1,int index2){
        String buff = model.get(index1);
        model.set(index1, model.get(index2));
        model.set(index2, buff);
        appInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate enter");
        super.onCreate(savedInstanceState);

        model = App.getInstance().getModel().getAppList();

        setContentView(R.layout.autorun_list_activity);
        // кнопарь добавления
        buAddAplication = (Button) findViewById(R.id.buttonTest);
        buAddAplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AutoRunListActivity.this, SelectApplicationActivity.class);
                startActivityForResult(intent, IndentActivityCodes.SELECT_APPLICATION_CODE);
            }
        });
        // листвью
        listviewAutoRun = (ListView) findViewById(R.id.listviewAutoRun);
        appInfoAdapter = new AppInfoAdapter();
        listviewAutoRun.setAdapter(appInfoAdapter);
        registerForContextMenu(listviewAutoRun);
        Log.d("onCreate leave");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.add(0, CM_DELETE_ID, 0, "Удалить запись");
        if (info.position != 0)
            menu.add(0, CM_UP_ID, 0, "Вверх по списку");
        if (info.position != model.size() - 1)
            menu.add(0, CM_DOWN_ID, 0, "Вниз по списку");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // Дряпнем элемент
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            model.remove(info.position);
        }
        if (item.getItemId() == CM_DOWN_ID) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            shiftDownAppinfo(info.position);
        }
        if (item.getItemId() == CM_UP_ID) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            shiftUpAppinfo(info.position);
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        App.getInstance().getModel().setAppList(model);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IndentActivityCodes.SELECT_APPLICATION_CODE) {
            String str = data.getStringExtra(IndentActivityCodes.SELECT_APPLICATION_KEY);
            if (!str.isEmpty()) {
                Toast.makeText(this, "You selected: " + str, Toast.LENGTH_SHORT).show();
                model.add(str);
                appInfoAdapter.notifyDataSetChanged();
            }
        }
    }

    public class AppInfoAdapter extends BaseAdapter {
        public AppInfoAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item;

            if (convertView == null) {
                LayoutInflater ltInflater = getLayoutInflater();
                item = ltInflater.inflate(R.layout.select_application_item, null);
            } else {
                item = convertView;
            }

            String appInfo = model.get(position);
            try {
                ApplicationInfo info = getPackageManager().getApplicationInfo(appInfo, 0);
                ((ImageView) item.findViewById(R.id.imageView)).setImageDrawable(info.loadIcon(getPackageManager()));
                ((TextView) item.findViewById(R.id.appName)).setText(info.loadLabel(getPackageManager()));
                ((TextView) item.findViewById(R.id.appVersion)).setText(info.packageName);
//                ((TextView)item.findViewById(R.id.appVersion)).setText(info.serviceInfo.applicationInfo.loadDescription(getPackageManager()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Log.e("Error in display package", e);
                ((TextView) item.findViewById(R.id.appName)).setText("Package " + appInfo + " not found!!!!");
            }
            return item;
        }


        public final int getCount() {
            return model.size();
        }

        public final Object getItem(int position) {
            return model.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }

}