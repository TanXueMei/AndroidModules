package com.xuemei.zxinglib.encoding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 */
public class EncodingUtils {

  /**
   * 创建二维码
   *
   * @param content content
   * @param widthPix widthPix
   * @param heightPix heightPix
   * @param logoBm logoBm
   * @return 二维码
   */
  public static Bitmap createQRCode(String content, int widthPix, int heightPix, Bitmap logoBm) {
    try {
      if (content == null || "".equals(content)) {
        return null;
      }
      // 配置参数
      Map<EncodeHintType, Object> hints = new HashMap<>();
      hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
      // 容错级别
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
      // 图像数据转换，使用了矩阵转换
      BitMatrix bitMatrix =
          new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
      int[] pixels = new int[widthPix * heightPix];
      // 下面这里按照二维码的算法，逐个生成二维码的图片，
      // 两个for循环是图片横列扫描的结果
      for (int y = 0; y < heightPix; y++) {
        for (int x = 0; x < widthPix; x++) {
          if (bitMatrix.get(x, y)) {
            pixels[y * widthPix + x] = 0xff000000;
          } else {
            pixels[y * widthPix + x] = 0xffffffff;
          }
        }
      }
      // 生成二维码图片的格式，使用ARGB_8888
      Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
      bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
      if (logoBm != null) {
        bitmap = addLogo(bitmap, logoBm);
      }
      //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
      return bitmap;
    } catch (WriterException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 在二维码中间添加Logo图案
   */
  public static Bitmap addLogo(Bitmap src, Bitmap logo) {
    if (src == null) {
      return null;
    }
    if (logo == null) {
      return src;
    }
    //获取图片的宽高
    int srcWidth = src.getWidth();
    int srcHeight = src.getHeight();
    int logoWidth = logo.getWidth();
    int logoHeight = logo.getHeight();
    if (srcWidth == 0 || srcHeight == 0) {
      return null;
    }
    if (logoWidth == 0 || logoHeight == 0) {
      return src;
    }
    //logo大小为二维码整体大小的1/5
    float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
    Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
    try {
      Canvas canvas = new Canvas(bitmap);
      canvas.drawBitmap(src, 0, 0, null);
      canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
      canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
      canvas.save(Canvas.ALL_SAVE_FLAG);
      canvas.restore();
    } catch (Exception e) {
      bitmap = null;
      e.getStackTrace();
    }
    return bitmap;
  }

  /**
   * 生成条形码
   * http://blog.csdn.net/zhoumushui/article/details/51008264
   *
   * @param contents 需要生成的内容
   * @param desiredWidth 生成条形码的宽带
   * @param desiredHeight 生成条形码的高度
   * @param displayCode 是否在条形码下方显示内容
   */

  public static Bitmap creatBarcode(Context context, String contents, int desiredWidth,
      int desiredHeight, boolean displayCode) {
    Bitmap ruseltBitmap = null;
    /**
     * 图片两端所保留的空白的宽度
     */
    int marginW = 20;
    /**
     * 条形码的编码类型
     */
    BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
    if (displayCode) {
      //条形图片
      Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat, desiredWidth, desiredHeight);
      //条形图片 的数字码值
      Bitmap codeBitmap =
          creatCodeBitmap(contents, desiredWidth + 2 * marginW, desiredHeight, context);
      //最终图片
      ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(0, desiredHeight));
    } else {
      ruseltBitmap = encodeAsBitmap(contents, barcodeFormat, desiredWidth, desiredHeight);
    }

    return ruseltBitmap;
  }

  /**
   * 生成条形码的Bitmap
   *
   * @param contents 需要生成的内容
   * @param format 编码格式
   * @throws WriterException
   */
  protected static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth,
      int desiredHeight) {
    final int WHITE = 0xFFFFFFFF;
    final int BLACK = 0xFF000000;
    MultiFormatWriter writer = new MultiFormatWriter();
    BitMatrix result = null;
    try {
      result = writer.encode(contents, format, desiredWidth, desiredHeight, null);
    } catch (WriterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (result != null) {
      int width = result.getWidth();
      int height = result.getHeight();
      int[] pixels = new int[width * height];
      // All are 0, or black, by default
      for (int y = 0; y < height; y++) {
        int offset = y * width;
        for (int x = 0; x < width; x++) {
          pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
        }
      }
      Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
      return bitmap;
    }
    return null;
  }

  /**
   * 生成显示编码的Bitmap
   */
  protected static Bitmap creatCodeBitmap(String contents, int width, int height, Context context) {
    TextView tv = new TextView(context);
    LayoutParams layoutParams =
        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    tv.setLayoutParams(layoutParams);
    tv.setText(contents);
    tv.setHeight(height);
    tv.setGravity(Gravity.CENTER_HORIZONTAL);
    tv.setWidth(width);
    tv.setDrawingCacheEnabled(true);
    tv.setTextColor(Color.BLACK);
    //tv.setTextSize(dp2px(context, 16));
    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);//这样也可实现适配
    tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
    tv.buildDrawingCache();
    Bitmap bitmapCode = tv.getDrawingCache();
    return bitmapCode;
  }

  public static int dp2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  /**
   * 将两个Bitmap合并成一个
   *
   * @param fromPoint 第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
   */
  protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
    if (first == null || second == null || fromPoint == null) {
      return null;
    }
    int marginW = 20;
    Bitmap newBitmap = Bitmap.createBitmap(first.getWidth() + second.getWidth() + marginW,
        first.getHeight() + second.getHeight(), Bitmap.Config.ARGB_4444);
    Canvas cv = new Canvas(newBitmap);
    cv.drawBitmap(first, marginW, 0, null);
    cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
    cv.save(Canvas.ALL_SAVE_FLAG);
    cv.restore();
    return newBitmap;
  }
}
