package com.xuemei.utilslib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/26
 *     desc   : APP相关信息工具类。获取版本信息，版本号等信息
 *     version: 1.0
 * </pre>
 */

public class AppUtil {
  /**
   * 获取Manifest Meta Data
   */
  public static String getMetaData(Context context, String metaKey) {
    String name = context.getPackageName();
    ApplicationInfo appInfo;
    String msg = "";
    try {
      appInfo = context.getPackageManager().getApplicationInfo(name, PackageManager.GET_META_DATA);
      msg = appInfo.metaData.getString(metaKey);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    if (TextUtils.isEmpty(msg)) {
      return "";
    }

    return msg;
  }

  /**
   * 获得渠道号
   */
  public static String getChannelNo(Context context, String channelKey) {
    return getMetaData(context, channelKey);
  }

  /**
   * 获取包名
   */
  public static String getPackageName(Context context) {
    PackageManager packageManager = context.getPackageManager();
    String packageName = context.getPackageName();
    return packageName;
  }

  /**
   * 获取版本号
   * 也可使用 BuildConfig.VERSION_NAME 替换
   *
   * @param context 上下文
   * @return 版本号
   */
  public static String getVersionName(Context context) {
    PackageManager packageManager = context.getPackageManager();
    String packageName = context.getPackageName();
    try {
      PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
      return packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return "1.0.0";
  }

  /**
   * 获取版本code
   * 也可使用 BuildConfig.VERSION_CODE 替换
   *
   * @param context 上下文
   * @return 版本code
   */
  public static int getVersionCode(Context context) {
    PackageManager packageManager = context.getPackageManager();
    String packageName = context.getPackageName();
    try {
      PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
      return packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return 1;
  }

  /**
   * 获取应用签名
   *
   * @param context 上下文
   * @param pkgName 包名
   */
  public static String getSign(Context context, String pkgName) {
    try {
      PackageInfo pis =
          context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
      return hexdigest(pis.signatures[0].toByteArray());
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将签名字符串转换成需要的32位签名
   *
   * @param paramArrayOfByte 签名byte数组
   * @return 32位签名字符串
   */
  private static String hexdigest(byte[] paramArrayOfByte) {
    final char[] hexDigits = {
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102
    };
    try {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramArrayOfByte);
      byte[] arrayOfByte = localMessageDigest.digest();
      char[] arrayOfChar = new char[32];
      for (int i = 0, j = 0; ; i++, j++) {
        if (i >= 16) {
          return new String(arrayOfChar);
        }
        int k = arrayOfByte[i];
        arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
        arrayOfChar[++j] = hexDigits[(k & 0xF)];
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 安装一个apk文件
   *
   * @param context 上下文
   * @param uriFile APK文件
   */
  public static void install(Context context, File uriFile) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(uriFile), "application/vnd.android.package-archive");
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  /**
   * 卸载一个app
   */
  public static void uninstall(Context context, String packageName) {
    //通过程序的包名创建URI
    Uri packageURI = Uri.parse("package:" + packageName);
    //创建Intent意图
    Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
    //执行卸载程序
    context.startActivity(intent);
  }

  /**
   * 检查手机上是否安装了指定的软件
   *
   * @param packageName 应用包名
   */
  public static boolean isAvilible(Context context, String packageName) {
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

  /**
   * 从apk中获取版本信息
   */
  public static String getChannelFromApk(Context context, String channelPrefix) {
    //从apk包中获取
    ApplicationInfo appinfo = context.getApplicationInfo();
    String sourceDir = appinfo.sourceDir;
    //默认放在meta-inf/里， 所以需要再拼接一下
    String key = "META-INF/" + channelPrefix;
    String ret = "";
    ZipFile zipfile = null;
    try {
      zipfile = new ZipFile(sourceDir);
      Enumeration<?> entries = zipfile.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = ((ZipEntry) entries.nextElement());
        String entryName = entry.getName();
        if (entryName.startsWith(key)) {
          ret = entryName;
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (zipfile != null) {
        try {
          zipfile.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    String[] split = ret.split(channelPrefix);
    String channel = "";
    if (split != null && split.length >= 2) {
      channel = ret.substring(key.length());
    }
    return channel;
  }
}
