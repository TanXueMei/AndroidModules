package com.modules.androidmodules;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.xuemei.utilslib.permission.PermissionUtil;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button tvPermission= (Button) findViewById(R.id.tv_permission);
    tvPermission.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        PermissionUtil.requestEachRxPermission(MainActivity.this, new PermissionUtil.PermissonCallBack() {
              @Override public void permissonAllow() {

              }

              @Override public void permissonDeny() {

              }

              @Override public void permissonNotAsk() {

              }
            },Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
      }
    });
  }
}
