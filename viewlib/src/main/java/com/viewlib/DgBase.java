package com.viewlib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import com.aokente.viewlib.R;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/08/01
 *     desc   : 父Dialog(自定义对话框样式，解决对话框黑色背景边框问题)
 *     version: 1.0
 * </pre>
 */
public abstract class DgBase extends Dialog {

  public DgBase(Context context) {
    super(context, R.style.DgTransparentBg);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
  }
  /*public DgBase(Context context, int theme) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}*/

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    onCreateFindView(savedInstanceState);
    onCreateAddListener(savedInstanceState);
  }

  protected abstract void onCreateFindView(Bundle savedInstanceState);

  protected abstract void onCreateAddListener(Bundle savedInstanceState);
}