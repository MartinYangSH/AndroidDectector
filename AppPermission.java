package com.example.yangshenghui.androiddectector;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.yang.appOpsManager.MyAppOpsManager;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.List;

/**
 * Created by yangshenghui on 2017/2/26.
 */

public class AppPermission extends Activity implements CompoundButton.OnCheckedChangeListener{
    private PackageManager pm;
    private List<AndroidAppProcess> runningAppList;
    private int appUid;
    private String appName;
    private LinearLayout ll;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_main);
        //从前一个activity中获得数据（通过bundle绑定的数据）
        Bundle b = getIntent().getExtras();
        //利用pid找到确定的程序
        int pid = (int) b.get("pid");
        //利用PackageInfo获得包权限
        PackageInfo pi = null;
        try {
            pi = findApplicationByPid(pid,this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pm = this.getPackageManager();
        String[]permissions = new String[100];
        try {
            permissions = pm.getPackageInfo(pi.packageName,PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int length = permissions.length;
        ll = (LinearLayout) findViewById(R.id.pll);
        for (int i = 0;i < length; i++){
            Switch s = new Switch(this);
            String[] p = permissions[i].split("\\.");
            s.setText(p[p.length-1]);
            s.setPadding(10,10,0,10);
            s.setChecked(true);
            s.setOnCheckedChangeListener(this);
            ll.addView(s);
        }
        Button changeBt = new Button(this);
        changeBt.setText("改变权限");
        changeBt.setOnClickListener(v -> {
            //TODO

        });
        changeBt.setGravity(Gravity.CENTER);
        ll.addView(changeBt);
    }

    //pid --> package
    protected PackageInfo findApplicationByPid(int pid, Context context) throws PackageManager.NameNotFoundException {
        runningAppList = AndroidProcesses.getRunningAppProcesses();
        //PackageInfo list

        for (AndroidAppProcess appProcessInfo : runningAppList){
            if (appProcessInfo.pid == pid) {
                appUid = appProcessInfo.uid;
                appName = appProcessInfo.getPackageName();
                return appProcessInfo.getPackageInfo(context,0);
            }
        }
        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            changePermission(AppPermission.this,buttonView.getText().toString(),1);
        }else{
            changePermission(AppPermission.this,buttonView.getText().toString(),0);
        }
    }

    private void changePermission(Context context,String permission,int mode) {
        MyAppOpsManager aom = (MyAppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        //改变权限，需要安卓API 5.1
        for (int i = -1; i <= 48;i++){
            if (permission == aom.opToName(i)){
                aom.setMode(i,appUid,appName,mode);
            }
        }
    }
}
