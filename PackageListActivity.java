package com.example.yangshenghui.androiddectector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yangshenghui on 2017/2/27.
 */

public class PackageListActivity extends Activity implements AdapterView.OnItemClickListener{
    private ListView package_list;
    private List<File> file_list = new ArrayList<>();
    private String key = ".apk";
    private File file = new File("/system/app");
    final List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_main);
        findFile(file);

        Iterator<File> file_iterator = file_list.iterator();
        while (file_iterator.hasNext()){
            Map<String,Object> map= new HashMap<>();
            File f = file_iterator.next();
            map.put("name","包名："+f.getName());
            map.put("size","大小："+Math.round(f.length()/(1024*1024.0)*1000)/1000.0+"MB");
            map.put("position","位置："+f.getPath());
            list.add(map);
        }

        package_list = (ListView) findViewById(R.id.package_list);

        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.package_item,new String[]{"name","size","position"},new int[]{R.id.package_name,R.id.package_size,R.id.package_position});
        package_list.setAdapter(adapter);

        package_list.setOnItemClickListener(this);


    }


    private void findFile(File file) {
        if (!file.isDirectory()){
            Log.i("File",file.toString());
            if (file.toString().contains(key)){
               file_list.add(file);
           }
        }else{
            Log.i("Directory",file.toString());
            File[] files = file.listFiles();
            if (files != null){
                for (int i = 0; i < files.length; i++) {
                    Log.i("Directory",files[i].toString());
                    findFile(files[i]);
                }
            }
        }
        return;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Bundle b = new Bundle();
        DialogUIUtils.showMdAlert(this, "提示", "是否检测"+list.get(position).get("name").toString(), new DialogUIListener() {
            @Override
            public void onPositive() {
                DialogUIUtils.showLoadingVertical(PackageListActivity.this,"检测中...").show();
                if (detector(list.get(position).get("position").toString())){
                    //// TODO: 2017/3/2

                }else {

                }
            }
            @Override
            public void onNegative() {
                return;
            }
        }).show();

    }

    public boolean detector(String packagePosition){
        //// TODO: 2017/3/2
        File f = new File(packagePosition);
        return true;
    }


}
