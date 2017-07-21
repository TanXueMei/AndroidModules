package com.modules.baidulib.util;

import android.os.Environment;
import java.io.File;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/07/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BNinitFile {
  /**
   * 百度导航初始化文件夹名称
   */
  public static String APP_NAVI_FOLDER = "BaiduNavi";
  /**
   * 初始化SD卡，在SD卡路径下新建文件夹：APP目录名，
   * 文件中包含了很多东西，比如log、cache等等
   * @return
   */
  public static boolean initNaviDirs() {
    String mSDCardPath = getSdcardDir();
    if (mSDCardPath == null) {
      return false;
    }
    File f = new File(mSDCardPath, APP_NAVI_FOLDER);
    if (!f.exists()) {
      try {
        f.mkdir();
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }

  public static String getSdcardDir() {
    if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
      return Environment.getExternalStorageDirectory().toString();
    }
    return null;
  }
}
