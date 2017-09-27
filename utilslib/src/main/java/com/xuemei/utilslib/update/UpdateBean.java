package com.xuemei.utilslib.update;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/27
 *     desc   : apk更新实体类
 *     version: 1.0
 * </pre>
 */

public class UpdateBean {
  private String downdApkUrl;//apk下载地址
  private String savePath; //apk保存到SD卡的路径
  private String saveFileName = ""; //完整路径名=mSavePath+"xxx.apk"
  private String versionName; //从服务器获取的版本名称
  private double versionCode; //从服务器获取的版本号
  private String  updateDescription = ""; //更新内容描述信息
  private boolean forceUpdate       = true; //是否强制更新

  public String getDowndApkUrl() {
    return downdApkUrl;
  }

  public void setDowndApkUrl(String downdApkUrl) {
    this.downdApkUrl = downdApkUrl;
  }

  public String getSavePath() {
    return savePath;
  }

  public void setSavePath(String savePath) {
    this.savePath = savePath;
  }

  public String getSaveFileName() {
    return saveFileName;
  }

  public void setSaveFileName(String saveFileName) {
    this.saveFileName = saveFileName;
  }

  public String getVersionName() {
    return versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  public double getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(double versionCode) {
    this.versionCode = versionCode;
  }

  public String getUpdateDescription() {
    return updateDescription;
  }

  public void setUpdateDescription(String updateDescription) {
    this.updateDescription = updateDescription;
  }

  public boolean isForceUpdate() {
    return forceUpdate;
  }

  public void setForceUpdate(boolean forceUpdate) {
    this.forceUpdate = forceUpdate;
  }
}
