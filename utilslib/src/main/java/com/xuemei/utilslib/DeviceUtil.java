package com.xuemei.utilslib;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/26
 *     desc   : 设备相关工具类
 *     version: 1.0
 * </pre>
 */

public class DeviceUtil {

  /**
   * 获取本机IP地址
   */
  public static String getLocalIPAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
          en.hasMoreElements(); ) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
            enumIpAddr.hasMoreElements(); ) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            return inetAddress.getHostAddress().toString();
          }
        }
      }
    } catch (SocketException ex) {
      return "0.0.0.0";
    }
    return "0.0.0.0";
  }

  /**
   * 服务是否运行
   */
  public static boolean isServiceRunning(Context mContext, String className) {
    boolean isRunning = false;
    ActivityManager activityManager =
        (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningServiceInfo> serviceList =
        activityManager.getRunningServices(Integer.MAX_VALUE);
    if (serviceList.size() == 0) {
      return false;
    }
    for (int i = 0; i < serviceList.size(); i++) {
      if (serviceList.get(i).service.getClassName().equals(className) == true) {
        isRunning = true;
        break;
      }
    }
    return isRunning;
  }

  /**
   * 进程是否运行
   */
  public static boolean isProessRunning(Context context, String proessName) {
    boolean isRunning = false;
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

    List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo info : lists) {
      if (info.processName.equals(proessName)) {
        isRunning = true;
        return isRunning;
      }
    }

    return isRunning;
  }

  /**
   * 获取IMEI
   */
  public static String getIMEI(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    String imei = tm.getDeviceId();
    if (TextUtils.isEmpty(imei)) {
      imei = "";
    }

    return imei;
  }

  /**
   * 获取MAC地址
   */
  public static String getMac(Context context) {
    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo info = wifi.getConnectionInfo();
    String mac = info.getMacAddress();
    if (TextUtils.isEmpty(mac)) {
      mac = "";
    }
    return mac;
  }

  /**
   * 获取UDID
   */
  public static String getUDID(Context context) {
    String udid =
        Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    if (TextUtils.isEmpty(udid) || udid.equals("9774d56d682e549c") || udid.length() < 15) {
      SecureRandom random = new SecureRandom();
      udid = new BigInteger(64, random).toString(16);
    }

    if (TextUtils.isEmpty(udid)) {
      udid = "";
    }

    return udid;
  }

  /**
   * 获取设备唯一标识 本方法调用需要READ_PHONE_STATE权限
   */
  public static String getUUID(Context context) {
    String tmDevice = "", tmSerial = "", tmPhone = "", androidId = "";
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
        == PackageManager.PERMISSION_GRANTED) {
      try {
        final TelephonyManager tm =
            (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
            android.provider.Settings.Secure.ANDROID_ID);
      } catch (Exception e) {
        Log.e("AppUtils", "exception:" + e.getMessage());
      }
    } else {
      Log.e("AppUtils", "没有 android.permission.READ_PHONE_STATE 权限");
      tmDevice = "device";
      tmSerial = "serial";
      androidId = "androidid";
    }

    UUID deviceUuid =
        new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
    String uniqueId = deviceUuid.toString();
    if (BuildConfig.DEBUG) Log.d(TAG, "uuid=" + uniqueId);

    return uniqueId;
  }

  /**
   * 震动
   */
  public static void vibrate(Context context, long duration) {
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    long[] pattern = {
        0, duration
    };
    vibrator.vibrate(pattern, -1);
  }

  /**
   * 获取手机大小（分辨率）
   */
  public static DisplayMetrics getScreenPix(Activity activity) {
    // DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
    DisplayMetrics displaysMetrics = new DisplayMetrics();
    // 获取手机窗口的Display 来初始化DisplayMetrics 对象
    // getManager()获取显示定制窗口的管理器。
    // 获取默认显示Display对象
    // 通过Display 对象的数据来初始化一个DisplayMetrics 对象
    activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
    return displaysMetrics;
  }

  /**
   * 复制到剪切板
   */
  @TargetApi(11) public static void coptyToClipBoard(Context context, String content) {
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
      ClipboardManager clipboard =
          (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      ClipData clip = ClipData.newPlainText("label", content);
      clipboard.setPrimaryClip(clip);
    } else {
      android.text.ClipboardManager clipboard =
          (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      clipboard.setText(content);
    }
  }

  /**
   * 获取非系统应用包名
   */
  public static List<String> getAppPackageNamelist(Context context) {
    List<String> packList = new ArrayList<>();
    PackageManager pm = context.getPackageManager();
    List<PackageInfo> packinfos = pm.getInstalledPackages(0);
    for (PackageInfo packinfo : packinfos) {
      String packname = packinfo.packageName;
      packList.add(packname);
    }

    return packList;
  }

  /**
   * 判断某个应用是否已经安装
   *
   * @param context 上下文
   * @param packageName 包名
   * @return 是否已经安装
   */
  public static boolean isAppInstall(Context context, String packageName) {
    // 获取packagemanager
    final PackageManager packageManager = context.getPackageManager();
    // 获取所有已安装程序的包信息
    List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
    // 用于存储所有已安装程序的包名
    List<String> packageNames = new ArrayList<String>();
    // 从pinfo中将包名字逐一取出，压入pName list中
    if (packageInfos != null) {
      for (int i = 0; i < packageInfos.size(); i++) {
        String packName = packageInfos.get(i).packageName;
        packageNames.add(packName);
      }
    }
    // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
    return packageNames.contains(packageName);
  }

  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * 判断是否有软控制键（手机底部几个按钮）
   */
  public boolean isSoftKeyAvail(Activity activity) {
    final boolean[] isSoftkey = { false };
    final View activityRootView =
        (activity).getWindow().getDecorView().findViewById(android.R.id.content);
    activityRootView.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            int rootViewHeight = activityRootView.getRootView().getHeight();
            int viewHeight = activityRootView.getHeight();
            int heightDiff = rootViewHeight - viewHeight;
            if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
              isSoftkey[0] = true;
            }
          }
        });
    return isSoftkey[0];
  }

  /**
   * 获取statusbar高度
   */
  public static int getStatusBarHeight(Context context) {
    int height = 0;
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      height = context.getResources().getDimensionPixelSize(resourceId);
    }

    return height;
  }

  /**
   * 获取navigationbar高度
   */
  public static int getNavigationBarHeight(Context context) {
    int height = 0;
    Resources resources = context.getResources();
    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    //获取NavigationBar的高度
    if (resourceId > 0) {
      height = resources.getDimensionPixelSize(resourceId);
    }
    return height;
  }

  /**
   * 获取状态栏高度＋标题栏(ActionBar)高度
   * (注意，如果没有ActionBar，那么获取的高度将和上面的是一样的，只有状态栏的高度)
   */
  public static int getTopBarHeight(Activity activity) {
    return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
  }

  /**
   * 根据包名启动Activity
   *
   * @param @param context
   * @param @param packageName   包名
   * @return void    返回类型
   * @throws
   * @Title: startActivityForPackage
   * @Description: TODO(通过)
   */
  @SuppressLint("NewApi") public static boolean startActivityForPackage(Context context,
      String packageName) {
    PackageInfo pi = null;
    try {
      pi = context.getPackageManager().getPackageInfo(packageName, 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return false;
    }

    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    resolveIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    //		resolveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    resolveIntent.setPackage(pi.packageName);

    List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

    ResolveInfo ri = apps.iterator().next();
    if (ri != null) {
      String packageName1 = ri.activityInfo.packageName;
      String className = ri.activityInfo.name;

      Intent intent = new Intent(Intent.ACTION_MAIN);
      intent.addCategory(Intent.CATEGORY_LAUNCHER);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

      ComponentName cn = new ComponentName(packageName1, className);

      intent.setComponent(cn);
      context.startActivity(intent);
      return true;
    }
    return false;
  }

  /**
   * 隐藏键盘：强制隐藏
   */
  public static void hideInputSoftFromWindowMethod(Context context, View view) {
    try {
      InputMethodManager imm =
          (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 显示输入法
   */
  public static void showInputSoftFromWindowMethod(Context context, View view) {
    try {
      InputMethodManager imm =
          (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 判断输入法是否处于激活状态
   */
  public static boolean isActiveSoftInput(Context context) {
    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    return imm.isActive();
  }

  /**
   * 主动回到Home，后台运行
   */
  public static void goHome(Context context) {
    Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
    mHomeIntent.addCategory(Intent.CATEGORY_HOME);
    mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
    context.startActivity(mHomeIntent);
  }

  /**
   * 拨打电话
   *
   * @param phoneNumber 电话号码
   */
  public static void callPhone(Context context, String phoneNumber) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
        != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
  }

  /**
   * 跳转至拨号界面
   *
   * @param phoneNumber 电话号码电话号码
   */
  public static void callDial(Context context, String phoneNumber) {
    context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
  }

  /**
   * 发送短信
   */
  public static void sendSms(Context context, String phoneNumber, String content) {
    Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
    intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
    context.startActivity(intent);
  }

  /**
   * 判断当前设备是否为手机
   */
  public static boolean isPhone(Context context) {
    TelephonyManager telephony =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 获取最后一次拍照的图片
   */
  public static String getLatestCameraPicture(Context context) {

    if (!FileUtil.isSdCardCanWork()) {
      return null;
    }
    String[] projection = new String[] {
        MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.MIME_TYPE
    };
    Cursor cursor = context.getContentResolver()
        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
    if (cursor.moveToFirst()) {
      String path = cursor.getString(1);
      return path;
    }
    return null;
  }
}
