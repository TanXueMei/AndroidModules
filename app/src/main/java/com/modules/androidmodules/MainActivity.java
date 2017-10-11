package com.modules.androidmodules;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.xuemei.utilslib.permission.PermissionUtil;
import com.xuemei.utilslib.statusbar.StatusBarUtil;
import com.xuemei.zxinglib.encoding.EncodingUtils;

public class MainActivity extends AppCompatActivity {
  private ImageView mImageView;
  private Button    btnPermission;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mImageView= (ImageView) findViewById(R.id.iv_code);
    StatusBarUtil.setSystemUI(MainActivity.this, true);
    StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.colortitle));
    StatusBarUtil.setFitsSystemWindows(MainActivity.this, false);
    btnPermission = (Button) findViewById(R.id.tv_permission);
    btnPermission.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        PermissionUtil.requestEachRxPermission(MainActivity.this,
            new PermissionUtil.PermissonCallBack() {
              @Override public void permissonAllow() {

              }

              @Override public void permissonDeny() {

              }

              @Override public void permissonNotAsk() {

              }
            }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA);
      }
    });
    //  测试生产二维码
    createCodeImage(this,"https://www.baidu.com",350,350, BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
  }

  /**
   * 创建二维码
   *
   * @param content content   需要生产二维码的内容
   * @param widthPix widthPix  二维码宽度
   * @param heightPix heightPix 二维码高度
   * @param logoBm logoBm    二维码中间logo（BitmapFactory.decodeResource(getResources(),
   * R.mipmap.app_logo)）
   * @return 二维码
   */
  private void createCodeImage(Context context, String content, int widthPix, int heightPix,
      Bitmap logoBm) {
    if (!content.equals("")) {
      //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
      Bitmap qrCodeBitmap = EncodingUtils.createQRCode(content, widthPix, heightPix, logoBm);
      //设置二维码中间的团logo
      EncodingUtils.addLogo(logoBm, logoBm);
      mImageView.setImageBitmap(qrCodeBitmap);
    } else {
      Toast.makeText(context, "Text can not be empty", Toast.LENGTH_SHORT).show();
    }
  }
}
