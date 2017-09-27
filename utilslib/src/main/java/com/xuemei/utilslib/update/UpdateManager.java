package com.xuemei.utilslib.update;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.xuemei.utilslib.BuildConfig;
import com.xuemei.utilslib.LogUtil;
import com.xuemei.utilslib.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/27
 *     desc   : 适配了Android7.0的apk更新
 *     version: 1.0
 * </pre>
 */

public class UpdateManager {
  private Context     mContext; //上下文
  private UpdateBean  mUpdateBean;//封装了apk更新所需要的所有信息
  private double clientVersion; //客户端当前的版本号
  private boolean forceUpdate = true; //是否强制更新

  private ProgressBar mProgress; //下载进度条控件
  private static final int DOWNLOADING     = 1; //表示正在下载
  private static final int DOWNLOADED      = 2; //下载完毕
  private static final int DOWNLOAD_FAILED = 3; //下载失败
  private int progress; //下载进度
  private boolean cancelFlag = false; //取消下载标志位
  private AlertDialog alertDialog1, alertDialog2; //表示提示对话框、进度条对话框

  /**
   * 构造函数
   */
  public UpdateManager(Context context, UpdateBean updateBean) {
    this.mContext = context;
    this.mUpdateBean = updateBean;
    clientVersion = BuildConfig.VERSION_CODE;
    if (updateBean.isForceUpdate()) {
      LogUtil.d("强制升级");
      forceUpdate = true;
    } else {
      LogUtil.d("手动升级");
      forceUpdate = false;
    }
  }

  /**
   * 显示更新对话框
   */
  public void showNoticeDialog() {
    //如果版本最新，则不需要更新
    if (mUpdateBean.getVersionCode() <= clientVersion) return;
    ContextThemeWrapper contextThemeWrapper =
        new ContextThemeWrapper(mContext, R.style.AlertDialogCustom);
    final TextView tvUpdateNote = new TextView(mContext);
    tvUpdateNote.setText(mUpdateBean.getUpdateDescription());
    tvUpdateNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
    AlertDialog.Builder dialog = new AlertDialog.Builder(contextThemeWrapper);
    dialog.setTitle("发现新版本:" + mUpdateBean.getVersionName());
    dialog.setView(tvUpdateNote);
    dialog.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface arg0, int arg1) {
        // TODO Auto-generated method stub
        arg0.dismiss();
        showDownloadDialog();
      }
    });
    //是否强制更新
    if (!forceUpdate) {
      dialog.setNegativeButton("待会更新", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface arg0, int arg1) {
          // TODO Auto-generated method stub
          arg0.dismiss();
        }
      });
    }
    alertDialog1 = dialog.create();
    alertDialog1.setCancelable(false);
    alertDialog1.show();
  }

  /**
   * 显示进度条对话框
   */
  public void showDownloadDialog() {
    ContextThemeWrapper contextThemeWrapper =
        new ContextThemeWrapper(mContext, R.style.AlertDialogCustom);
    AlertDialog.Builder dialog = new AlertDialog.Builder(contextThemeWrapper);
    dialog.setTitle("正在更新");
    final LayoutInflater inflater = LayoutInflater.from(mContext);
    View v = inflater.inflate(R.layout.progress_softupdate, null);
    mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
    dialog.setView(v);
    //如果是强制更新，则不显示取消按钮
    if (!forceUpdate) {
      dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface arg0, int arg1) {
          // TODO Auto-generated method stub
          arg0.dismiss();
          cancelFlag = true;
        }
      });
    }
    alertDialog2 = dialog.create();
    alertDialog2.setCancelable(false);
    alertDialog2.show();

    final String packageName = "com.android.providers.downloads";
    int state = mContext.getPackageManager().getApplicationEnabledSetting(packageName);
    if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
        || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
      AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("温馨提示")
          .setMessage("系统下载管理器被禁止，需手动打开")
          .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              try {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
                mContext.startActivity(intent);
              } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                mContext.startActivity(intent);
              }
            }
          })
          .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
          });
      builder.create().show();
    } else {
      //下载apk
      downloadAPK();
    }
  }

  /**
   * 下载apk的线程
   */
  public void downloadAPK() {
    new Thread(new Runnable() {
      @Override public void run() {
        // TODO Auto-generated method stub
        try {
          URL url = new URL(mUpdateBean.getDowndApkUrl());
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.connect();

          int length = conn.getContentLength();
          InputStream is = conn.getInputStream();

          File file = new File(mUpdateBean.getSavePath());
          if (!file.exists()) {
            file.mkdir();
          }
          String apkFile = mUpdateBean.getSaveFileName();
          File ApkFile = new File(apkFile);
          FileOutputStream fos = new FileOutputStream(ApkFile);

          int count = 0;
          byte buf[] = new byte[1024];

          do {
            int numread = is.read(buf);
            count += numread;
            progress = (int) (((float) count / length) * 100);
            //更新进度
            mHandler.sendEmptyMessage(DOWNLOADING);
            if (numread <= 0) {
              //下载完成通知安装
              mHandler.sendEmptyMessage(DOWNLOADED);
              break;
            }
            fos.write(buf, 0, numread);
          } while (!cancelFlag); //点击取消就停止下载.

          fos.close();
          is.close();
        } catch (Exception e) {
          mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
          e.printStackTrace();
        }
      }
    }).start();
  }

  /**
   * 更新UI的handler
   */
  private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      // TODO Auto-generated method stub
      switch (msg.what) {
        case DOWNLOADING://正在下载
          mProgress.setProgress(progress);
          break;
        case DOWNLOADED://下载完成
          if (alertDialog2 != null) alertDialog2.dismiss();
          installAPK();
          break;
        case DOWNLOAD_FAILED://下载失败
          Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG).show();
          break;
        default:
          break;
      }
    }
  };

  /**
   * 下载完成后自动安装apk
   */
  public void installAPKs() {
    File apkFile = new File(mUpdateBean.getSaveFileName());
    if (!apkFile.exists()) {
      return;
    }
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
        "application/vnd.android.package-archive");
    mContext.startActivity(intent);
  }

  /**
   * 兼容Android7.0
   */
  private void installAPK() {
    File apkFile = new File(mUpdateBean.getSaveFileName());
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Uri apkUri =
          FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", apkFile);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
    } else {
      //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
      //            intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
    }
    mContext.startActivity(intent);
  }
}
