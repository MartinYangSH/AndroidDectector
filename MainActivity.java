package com.example.yangshenghui.androiddectector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;

public class MainActivity extends AppCompatActivity {
    private Button bt_permission;
    private Button bt_detector;

    private long mExitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_permission = (Button) findViewById(R.id.permission);
        bt_detector = (Button) findViewById(R.id.detector);

        bt_permission.setOnClickListener(v -> {
            DialogUIUtils.showLoadingVertical(MainActivity.this,"搜索正在运行的程序...").show();
            Intent i = new Intent(MainActivity.this,AppListActivity.class);
            startActivityForResult(i,1);
        });

        bt_detector.setOnClickListener(v -> {
            DialogUIUtils.showLoadingVertical(MainActivity.this,"搜索安装包中...").show();
            Intent i = new Intent(MainActivity.this,PackageListActivity.class);
            startActivity(i);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this,"再按一次退出程序！",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
