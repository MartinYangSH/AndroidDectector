package com.example.yangshenghui.androiddectector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppListActivity extends Activity {
    private ListView lv;
    private List<AndroidAppProcess> runningApp;
    private List<ApplicationInfo> appInfoList;
    private PackageManager pm;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main);

        pm = this.getPackageManager();
        lv = (ListView) findViewById(R.id.app_list);
        appInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Log.i("Installed APP",appInfoList.get(0).toString()+appInfoList.get(1).toString());
        loadList(this);

    }


    protected void loadList(Context context){
        final ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
        try{
            //用于获取正在运行的程序 AndroidProcesses --> PackageInfo/ApplicationInfo (获得程序信息) --> ArrayList --> adapter(显示结果)
            //获得正在运行的app
            runningApp = AndroidProcesses.getRunningAppProcesses();
            //对每个正在运行的app， AndroidAppProcess --> processName/pid/icon..
            for (AndroidAppProcess appProcessInfo : runningApp){
                HashMap<String,Object> map = new HashMap<String, Object>();
                int pid = appProcessInfo.pid;
                String processName = appProcessInfo.name;
                PackageInfo p = appProcessInfo.getPackageInfo(context,0);
                Log.i("TAG","Process"+processName+" Pid:"+pid);
                //排除所有系统程序，将信息放在map中，再将map放入list
                if((processName).equals("com.android.contacts") ||(processName).equals("com.android.email") ||
                        (processName).equals("com.android.settings") || (processName).equals("com.android.music") ||
                        (processName).equals("com.android.calendar") ||(processName).equals("com.android.calculator2") ||
                        (processName).equals("com.android.browser") ||(processName).equals("com.android.camera") ||
                        (processName).equals("com.android.media") ||(processName).equals("com.android.bluetooth") ||
                        (processName).equals("com.android.mms")) {
                    continue;
                }
                map.put("icon",p.applicationInfo.loadIcon(pm));
                map.put("name","程序名："+p.applicationInfo.loadLabel(pm));
                map.put("pid","Pid："+pid);
                list.add(map);
            }
        }catch (Exception ex){
        }

        //设置适配器，用于将信息显示在activity中
        SimpleAdapter sa = new SimpleAdapter(this,list,R.layout.app_item,new String[]{"icon","name","pid"},new int[]{R.id.app_icon,R.id.app_name,R.id.app_pid});
        lv.setAdapter(sa);
        //图片无法直接显示，需要设置ViewBinder，将适配器中的Drawable数据转成Drawable类型
        sa.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable){
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                }
                return false;
            }
        });
        //对每个item设置Click监听器，用于访问该软件所拥有的权限
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putInt("pid", Integer.parseInt(((String) list.get(position).get("pid")).substring(4)));
                Intent i = new Intent(AppListActivity.this,AppPermission.class);
                i.putExtras(b);

                startActivity(i);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}

