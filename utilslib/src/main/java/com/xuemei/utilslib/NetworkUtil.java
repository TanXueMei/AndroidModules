package com.xuemei.utilslib;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/26
 *     desc   : 网络工具类
 *     version: 1.0
 * </pre>
 */

public class NetworkUtil {
  /**
   * 判断网络连接是否打开,包括移动数据连接
   *
   * @param context 上下文
   * @return 是否联网
   */
  public static boolean isNetworkAvailable(Context context) {
    boolean netstate = false;
    ConnectivityManager connectivity =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity != null) {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (int i = 0; i < info.length; i++) {

          if (info[i].getState() == State.CONNECTED) {

            netstate = true;
            break;
          }
        }
      }
    }
    return netstate;
  }

  /**
   * GPS是否打开
   *
   * @param context 上下文
   * @return Gps是否可用
   */
  public static boolean isGpsEnabled(Context context) {
    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  /**
   * 检测当前打开的网络类型是否WIFI
   *
   * @param context 上下文
   * @return 是否是Wifi上网
   */
  public static boolean isWifi(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
  }

  /**
   * 检测当前打开的网络类型是否3G
   *
   * @param context 上下文
   * @return 是否是3G上网
   */
  public static boolean is3G(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
  }

  /**
   * 检测当前开打的网络类型是否4G
   *
   * @param context 上下文
   * @return 是否是4G上网
   */
  public static boolean is4G(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    if (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting()) {
      if (activeNetInfo.getType() == TelephonyManager.NETWORK_TYPE_LTE) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断是否打开WIFI
   *
   * @param context 上下文
   * @return 是否打开Wifi
   */
  public static boolean isWiFi(Context context) {
    ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
    return wifi == State.CONNECTED || wifi == State.CONNECTING;
  }

  /**
   * IP地址校验
   *
   * @param ip 待校验是否是IP地址的字符串
   * @return 是否是IP地址
   */
  public static boolean isIP(String ip) {
    Pattern pattern = Pattern.compile(
        "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    Matcher matcher = pattern.matcher(ip);
    return matcher.matches();
  }

  /**
   * IP转化成int数字
   *
   * @param addr IP地址
   * @return Integer
   */
  public static int ipToInt(String addr) {
    String[] addrArray = addr.split("\\.");
    int num = 0;
    for (int i = 0; i < addrArray.length; i++) {
      int power = 3 - i;
      num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
    }
    return num;
  }

  /**
   * 枚举网络状态 NET_NO：没有网络 NET_2G:2g网络 NET_3G：3g网络 NET_4G：4g网络 NET_WIFI：wifi
   * NET_UNKNOWN：未知网络
   */
  public enum NetState {
    NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
  }

  /**
   * 判断当前是否网络连接属于什么网络类型连接
   * 判断手机连接的网络类型(wifi,2G,3G,4G)
   * 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
   *
   * @param context 上下文
   * @return 状态码
   */
  public NetState isConnected(Context context) {
    NetState stateCode = NetState.NET_NO;
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getActiveNetworkInfo();
    if (ni != null && ni.isConnectedOrConnecting()) {
      switch (ni.getType()) {
        case ConnectivityManager.TYPE_WIFI:
          stateCode = NetState.NET_WIFI;
          break;
        case ConnectivityManager.TYPE_MOBILE:
          switch (ni.getSubtype()) {
            case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
            case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
            case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
              stateCode = NetState.NET_2G;
              break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
              stateCode = NetState.NET_3G;
              break;
            case TelephonyManager.NETWORK_TYPE_LTE:
              stateCode = NetState.NET_4G;
              break;
            default:
              stateCode = NetState.NET_UNKNOWN;
          }
          break;
        default:
          stateCode = NetState.NET_UNKNOWN;
      }
    }
    return stateCode;
  }
  /**
   * 获取移动终端类型
   * PHONE_TYPE_NONE :0 手机制式未知
   * PHONE_TYPE_GSM :1 手机制式为GSM，移动和联通
   * PHONE_TYPE_CDMA :2 手机制式为CDMA，电信
   * PHONE_TYPE_SIP:3
   * 返回移动终端类型
   */
  public static int getPhoneType(Context context) {
    TelephonyManager telephonyManager =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return telephonyManager.getPhoneType();
  }
  /**
   * 获取URL中参数 并返回Map
   */
  public static Map<String, String> getUrlParams(String url) {
    Map<String, String> params = null;
    try {
      String[] urlParts = url.split("\\?");
      if (urlParts.length > 1) {
        params = new HashMap<>();
        String query = urlParts[1];
        for (String param : query.split("&")) {
          String[] pair = param.split("=");
          String key = URLDecoder.decode(pair[0], "UTF-8");
          String value = "";
          if (pair.length > 1) {
            value = URLDecoder.decode(pair[1], "UTF-8");
          }
          params.put(key, value);
        }
      }
    } catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }
    return params;
  }

  /**
   * 是否是网络链接
   */
  public static boolean isUrl(String url) {
    try {
      URL url1 = new URL(url);
      return true;
    } catch (MalformedURLException e) {
      return false;
    }
  }
}
