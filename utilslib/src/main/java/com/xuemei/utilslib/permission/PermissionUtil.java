package com.xuemei.utilslib.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.functions.Action1;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PermissionUtil {
  private static StringBuffer logStringBuffer = new StringBuffer();

  /**
   * 检查权限是否已申请
   */
  public static String checkPermissions(Context context, String... permissions) {
    logStringBuffer.delete(0, logStringBuffer.length());
    for (String permission : permissions) {
      logStringBuffer.append(permission);
      logStringBuffer.append(" is applied? \n     ");
      logStringBuffer.append(isAppliedPermission(context, permission));
      logStringBuffer.append("\n\n");
    }
    return logStringBuffer.toString();
  }

  private static boolean isAppliedPermission(Context context, String permission) {
    return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * 获取多个权限但不区分（参数为可变参数，因此兼容检查单个权限和多个权限）
   */
  public static void requestRxPermissions(final Activity activity,
      final PermissonCallBack permissonCallBack, String... permissions) {
    RxPermissions rxPermissions = new RxPermissions(activity);
    rxPermissions.request(permissions).subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean granted) {
        if (granted) {
          Toast.makeText(activity, "已获取权限", Toast.LENGTH_SHORT).show();
          permissonCallBack.permissonAllow();
        } else {
          Toast.makeText(activity, "已拒绝一个或以上权限", Toast.LENGTH_SHORT).show();
          permissonCallBack.permissonDeny();
        }
      }
    });
  }

  /**
   * 获取多个权限且区分（参数为可变参数，因此兼容检查单个权限和多个权限）
   */
  public static void requestEachRxPermission(final Activity activity,
      final PermissonCallBack permissonCallBack, String... permissions) {
    RxPermissions rxPermissions = new RxPermissions(activity);
    rxPermissions.requestEach(permissions).subscribe(new Action1<Permission>() {
      @Override public void call(Permission permission) {
        if (permission.granted) {
          Toast.makeText(activity, "已获取权限" + permission.name, Toast.LENGTH_SHORT).show();
          permissonCallBack.permissonAllow();
        } else if (permission.shouldShowRequestPermissionRationale) {
          //拒绝权限请求
          Toast.makeText(activity, "已拒绝权限" + permission.name, Toast.LENGTH_SHORT).show();
          permissonCallBack.permissonDeny();
        } else {
          // 拒绝权限请求,并不再询问
          // 需要进入设置界面去设置权限
          Toast.makeText(activity, "已拒绝权限" + permission.name + "并不再询问", Toast.LENGTH_SHORT).show();
          permissonCallBack.permissonNotAsk();
        }
      }
    });
  }

  public interface PermissonCallBack {
    void permissonAllow();//获取权限成功

    void permissonDeny();//已拒绝权限

    void permissonNotAsk();//已拒绝权限，并不再询问，提示用户后续到设置界面去设置权限
  }
}
