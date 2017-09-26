package com.xuemei.utilslib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/08/01
 *     desc   : Toast工具类
 *     version: 1.0
 * </pre>
 */

public class ToastUtil {
  private static String oldMsg;
  protected static Toast toast   = null;
  private static   long  oneTime = 0;
  private static   long  twoTime = 0;
  public static Context sContext;

  /**
   * 引入ApplicationContext
   */
  public static void register(Context context) {
    sContext = context.getApplicationContext();
  }

  /**
   * 检测Context是否为空，防止空指针
   */
  private static void check() {
    if (sContext == null) {
      throw new NullPointerException(
          "Must initial call ToastUtils.register(Context context) in your "
              + "<? "
              + "extends Application class>");
    }
  }

  /**
   * 短时间显示
   *
   * @param message 需要显示的消息
   */
  public static void showShortToast(String message) {
    showToast(message, Toast.LENGTH_SHORT);
  }

  /**
   * 长时间显示
   *
   * @param message 需要显示的消息
   */
  public static void showLongToast(String message) {
    showToast(message, Toast.LENGTH_LONG);
  }

  public static void showShortToastInThread(final String strMessage) {
    showToastInThread(strMessage, Toast.LENGTH_SHORT, Gravity.CENTER_HORIZONTAL);
  }

  public static void showLongToastInThread(final String strMessage) {
    showToastInThread(strMessage, Toast.LENGTH_LONG, Gravity.CENTER_HORIZONTAL);
  }

  /**
   * @param message toast的消息
   * @param duration 显示时长
   */
  public static void showToast(String message, int duration) {
    check();
    if (toast == null) {
      toast = Toast.makeText(sContext, message, duration);
      toast.show();
      oneTime = System.currentTimeMillis();
    } else {
      twoTime = System.currentTimeMillis();
      if (message.equals(oldMsg)) {
        if (twoTime - oneTime > duration) {
          toast.show();
        }
      } else {
        oldMsg = message;
        toast.setText(message);
        toast.show();
      }
      oneTime = twoTime;
    }
  }

  /**
   * 用于在线程中显示Toast的界面，用于提示信息显示
   *
   * @param message 需要显示的消息
   * @param duration 显示的等待时间
   */
  public static void showToastInThread(final String message, final int duration,
      final int iGravity) {
    check();
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override public void run() {
        Toast toast = Toast.makeText(sContext, message, Toast.LENGTH_SHORT);
        toast.setDuration(duration);
        toast.setGravity(iGravity, 0, 0);
        toast.show();
      }
    });
  }
}
