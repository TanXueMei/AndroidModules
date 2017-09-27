package com.xuemei.utilslib.avatar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.xuemei.utilslib.FileUtil;
import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/27
 *     desc   : 更换头像，相机拍照，相册选择，裁剪图片
 *     version: 1.0
 * </pre>
 */
public class CropImageUtils {
  private static CropImageUtils instance;
  //打开相机的返回码
  public static final int    REQUEST_CODE_TAKE_PHOTO     = 11111;
  //打开相册的返回码
  public static final int    REQUEST_CODE_SELECT_PICTURE = 11112;
  //剪切图片的返回码
  public static final int    REQUEST_CODE_CROP_PICTURE   = 11113;
  //7.0  ContentUri
  public static       String FILE_CONTENT_FILEPROVIDER   = "";
  //相机拍照默认存储路径
  private static      String PHOTO_IMG_PATH              = null;
  private static      String CROP_IMG_PATH               = null;
  private static Context sContext;//注意引入ApplicationContext否则内存泄露

  /**
   * BaseCachePath=Environment.getExternalStorageDirectory()+ "/avatar/Temp"
   * @param providerPath 格式为：包名.provider    形如：com.xuemei.utilslib.provider
   * @param photoPath 相机拍照照片默认存储路径    形如：BaseCachePath++ "/photo.jpg"
   * @param cropPath 相机拍照裁剪图片默认存储路径 形如：BaseCachePath++ "/crop.jpg"
   */
  public static void register(Context context, String providerPath, String photoPath,
      String cropPath) {
    sContext = context;
    FILE_CONTENT_FILEPROVIDER = providerPath;
    PHOTO_IMG_PATH = photoPath;
    CROP_IMG_PATH = cropPath;
  }

  public CropImageUtils() {
  }

  public static CropImageUtils getInstance() {
    if (instance == null) {
      instance = new CropImageUtils();
    }
    return instance;
  }

  /**
   * 打开系统相册
   */
  public void openAlbum(Activity activity) {
    if (isSdCardExist()) {
      Intent intent;
      if (Build.VERSION.SDK_INT < 19) {
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
      } else {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      }
      activity.startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
    } else {
      Toast.makeText(sContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 打开系统相机
   */
  public void takePhoto(Activity activity) {
    if (isSdCardExist()) {
      File file = new File(PHOTO_IMG_PATH);
      try {
        FileUtil.createFile(file);
      } catch (IOException e) {
        e.printStackTrace();
      }
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      //添加这一句表示对目标应用临时授权该Uri所代表的文件
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      //Android7.0以上URI
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //通过FileProvider创建一个content类型的Uri
        Uri uri = FileProvider.getUriForFile(activity, FILE_CONTENT_FILEPROVIDER, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
      } else {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
      }
      try {
        activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
      } catch (ActivityNotFoundException anf) {
        Toast.makeText(sContext, "摄像头尚未准备好", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(sContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 调用系统剪裁功能
   */
  public void cropPicture(Activity activity, String path) {
    File file = new File(path);
    try {
      //FileUtil.createFile(file);
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Uri imageUri;
    Intent intent = new Intent("com.android.camera.action.CROP");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      //添加这一句表示对目标应用临时授权该Uri所代表的文件
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      //通过FileProvider创建一个content类型的Uri
      imageUri = FileProvider.getUriForFile(activity, FILE_CONTENT_FILEPROVIDER, file);
      //TODO:outputUri不需要ContentUri,否则失败
      //outputUri = FileProvider.getUriForFile(activity, "com.solux.furniture.fileprovider", new File(crop_image));
    } else {
      imageUri = Uri.fromFile(file);
    }
    intent.setDataAndType(imageUri, "image/*");
    intent.putExtra("crop", "true");
    //设置宽高比例
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    //设置裁剪图片宽高
    intent.putExtra("outputX", 300);
    intent.putExtra("outputY", 300);
    intent.putExtra("scale", true);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(CROP_IMG_PATH)));
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    intent.putExtra("noFaceDetection", true);
    activity.startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
  }

  /**
   * 拍照/打开相册/剪裁图片的回调
   */
  @RequiresApi(api = Build.VERSION_CODES.KITKAT) public void onActivityResult(Activity activity,
      int requestCode, int resultCode, Intent data, OnResultListener listener) {
    Log.e("CropImageUtils", "onActivityResult 回调data:" + data + "  resultCode:" + resultCode);
    switch (requestCode) {
      case REQUEST_CODE_TAKE_PHOTO:
        if (!TextUtils.isEmpty(PHOTO_IMG_PATH)) {
          File file = new File(PHOTO_IMG_PATH);
          if (file.isFile() && listener != null) listener.takePhotoFinish(PHOTO_IMG_PATH);
        }
        break;
      case REQUEST_CODE_SELECT_PICTURE:
        if (data != null) {
          Uri uri = data.getData();
          if (uri != null) {
            String path = GetPathFromUri.getInstance().getPath(activity, uri);
            File file = new File(path);
            if (file.isFile() && listener != null) listener.selectPictureFinish(path);
          }
        }
        break;
      case REQUEST_CODE_CROP_PICTURE:
        if (!TextUtils.isEmpty(CROP_IMG_PATH)) {
          File file = new File(CROP_IMG_PATH);
          if (file.isFile() && listener != null) listener.cropPictureFinish(file, CROP_IMG_PATH);
        }
        break;
    }
  }

  /**
   * 检查SD卡是否存在
   */
  public boolean isSdCardExist() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }

  public interface OnResultListener {
    //拍照回调
    void takePhotoFinish(String path);

    //选择图片回调
    void selectPictureFinish(String path);

    //截图回调
    void cropPictureFinish(File file, String path);
  }
}

