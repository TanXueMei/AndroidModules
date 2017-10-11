package com.viewlib;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.aokente.viewlib.R;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/08/01
 *     desc   : 圆形加载进度
 *     version: 1.0
 * </pre>
 */

public class DgCircleProgress extends DgBase {
  private String  strText;
  private Context context;

  private ImageView img_progress;
  private TextView  text_progress;

  // 控制刷新按钮旋转的动画
  private Animation operatingAnim;

  public DgCircleProgress(Context context, String strText) {
    super(context);
    this.context = context;
    this.strText = strText;
    // 设置触摸不可取消
    setCanceledOnTouchOutside(false);
    getWindow().setWindowAnimations(R.style.DialogShow);
  }

  @Override protected void onCreateFindView(Bundle savedInstanceState) {
    setContentView(R.layout.dialog_progress);

    img_progress = (ImageView) findViewById(R.id.img_progress);
    text_progress = (TextView) findViewById(R.id.text_progress);
    text_progress.setText(strText);
    // 进度框开始旋转动画
    initProgressCircleAnim();
  }

  @Override protected void onCreateAddListener(Bundle savedInstanceState) {

  }

  /**
   * 进度框旋转动画
   */
  private void initProgressCircleAnim() {
    // 初始化加载的旋转动画
    operatingAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_circle);
    LinearInterpolator lin = new LinearInterpolator();
    operatingAnim.setInterpolator(lin);
    img_progress.startAnimation(operatingAnim);
  }
}
