package com.xuemei.utilslib.update;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.xuemei.utilslib.DeviceUtil;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/27
 *     desc   : 下载apk安装包进度条
 *     version: 1.0
 * </pre>
 */

public class DowndApkProgress extends ProgressBar {
  String text;
  Paint  mPaint;

  public DowndApkProgress(Context context) {
    super(context);
    // TODO Auto-generated constructor stub
    System.out.println("1");
    initText();
  }

  public DowndApkProgress(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    // TODO Auto-generated constructor stub
    System.out.println("2");
    initText();
  }

  public DowndApkProgress(Context context, AttributeSet attrs) {
    super(context, attrs);
    // TODO Auto-generated constructor stub
    System.out.println("3");
    initText();
  }

  @Override public synchronized void setProgress(int progress) {
    // TODO Auto-generated method stub
    setText(progress);
    super.setProgress(progress);
  }

  @Override protected synchronized void onDraw(Canvas canvas) {
    // TODO Auto-generated method stub
    super.onDraw(canvas);
    //this.setText();
    Rect rect = new Rect();
    this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
    int x = (getWidth() / 2) - rect.centerX();
    int y = (getHeight() / 2) - rect.centerY();
    canvas.drawText(this.text, x, y, this.mPaint);
  }

  //初始化，画笔
  private void initText() {
    this.mPaint = new Paint();
    this.mPaint.setTypeface(Typeface.DEFAULT_BOLD);
    this.mPaint.setTextSize(DeviceUtil.px2sp(getContext(), 100));
    this.mPaint.setColor(Color.WHITE);
  }

  private void setText() {
    setText(this.getProgress());
  }

  //设置文字内容
  private void setText(int progress) {
    int i = (progress * 100) / this.getMax();
    this.text = String.valueOf(i) + "%";
  }
}