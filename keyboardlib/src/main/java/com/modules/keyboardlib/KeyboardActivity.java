package com.modules.keyboardlib;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class KeyboardActivity extends AppCompatActivity
    implements KeyboardView.OnKeyboardActionListener {

  RadioButton  mRbCode01;
  RadioButton  mRbCode02;
  RadioButton  mRbCode03;
  RadioButton  mRbCode04;
  RadioButton  mRbCode05;
  RadioButton  mRbCode06;
  RadioButton  mRbCode07;
  RadioButton  mRbCode08;
  RadioGroup   mRgCode;
  CheckBox     mCkbXinnengyuan;
  KeyboardView mKeyboardView;
  private Keyboard mKbProvince;//省键盘
  private Keyboard mKbLettes;//字母键盘
  private Keyboard mKbNumberLetters;//字母数字键盘
  private Keyboard mKbSpecial;//特殊字符
  private int mIndex = 0;//车牌位置下标
  private int mOkMax = 7;//车牌长度
  private RadioButton[] mRbArr;//车牌号

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_keyboard);
    init();
  }

  private void init() {
    mRbCode01 = (RadioButton) findViewById(R.id.rb_code_01);
    mRbCode02 = (RadioButton) findViewById(R.id.rb_code_02);
    mRbCode03 = (RadioButton) findViewById(R.id.rb_code_03);
    mRbCode04 = (RadioButton) findViewById(R.id.rb_code_04);
    mRbCode05 = (RadioButton) findViewById(R.id.rb_code_05);
    mRbCode06 = (RadioButton) findViewById(R.id.rb_code_06);
    mRbCode07 = (RadioButton) findViewById(R.id.rb_code_07);
    mRbCode08 = (RadioButton) findViewById(R.id.rb_code_08);
    mRgCode = (RadioGroup) findViewById(R.id.rg_code);
    mCkbXinnengyuan = (CheckBox) findViewById(R.id.ckb_xinnengyuan);
    mKeyboardView = (KeyboardView) findViewById(R.id.kbv_keyboard);

    mKbProvince = new Keyboard(this, R.xml.kb_province);
    mKbLettes = new Keyboard(this, R.xml.kb_letters);
    mKbNumberLetters = new Keyboard(this, R.xml.kb_number_letters);
    mKbSpecial = new Keyboard(this, R.xml.kb_special);
    mRbArr = new RadioButton[] {
        mRbCode01, mRbCode02, mRbCode03, mRbCode04, mRbCode05, mRbCode06, mRbCode07, mRbCode08
    };
    mKeyboardView.setKeyboard(mKbProvince);
    mKeyboardView.setEnabled(true);
    mKeyboardView.setPreviewEnabled(false);
    mKeyboardView.setOnKeyboardActionListener(this);

    mCkbXinnengyuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          mRbCode08.setVisibility(View.VISIBLE);
          mCkbXinnengyuan.setBackgroundResource(R.mipmap.ic_checkbox_select);
          mOkMax = 8;
        } else {
          mRbCode08.setVisibility(View.GONE);
          mCkbXinnengyuan.setBackgroundResource(R.mipmap.ic_checkbox_normal);
          mOkMax = 7;
        }
      }
    });
    mRgCode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rb_code_01) {
          mIndex = 0;
          mKeyboardView.setKeyboard(mKbProvince);
        } else if (checkedId == R.id.rb_code_02) {
          mIndex = 1;
          mKeyboardView.setKeyboard(mKbLettes);
        } else if (checkedId == R.id.rb_code_03) {
          mIndex = 2;
          mKeyboardView.setKeyboard(mKbNumberLetters);
        } else if (checkedId == R.id.rb_code_04) {
          mIndex = 3;
          mKeyboardView.setKeyboard(mKbNumberLetters);
        } else if (checkedId == R.id.rb_code_05) {
          mIndex = 4;
          mKeyboardView.setKeyboard(mKbNumberLetters);
        } else if (checkedId == R.id.rb_code_06) {
          mIndex = 5;
          mKeyboardView.setKeyboard(mKbNumberLetters);
        } else if (checkedId == R.id.rb_code_07) {
          mIndex = 6;
          mKeyboardView.setKeyboard(mKbNumberLetters);
        } else if (checkedId == R.id.rb_code_08) {
          mIndex = 7;
          mKeyboardView.setKeyboard(mKbNumberLetters);
        }
      }
    });
  }

  /** setOnKeyboardActionListener  start */
  @Override public void onPress(int primaryCode) {

  }

  @Override public void onRelease(int primaryCode) {

  }

  @Override public void onText(CharSequence text) {

  }

  @Override public void swipeLeft() {

  }

  @Override public void swipeRight() {

  }

  @Override public void swipeDown() {

  }

  @Override public void swipeUp() {

  }

  @Override public void onKey(int primaryCode, int[] keyCodes) {
    switch (primaryCode) {
      case -2://删除
        mRbArr[mIndex].setText(BuildConfig.FLAVOR);
        mIndex = mIndex - 1;
        if (mIndex < 0) {
          mIndex = 0;
        }
        mRbArr[mIndex].setChecked(true);
        mRbArr[mIndex].setText(BuildConfig.FLAVOR);
        break;
      case -4://数英
        changeSpecialToNumLetter(true);
        break;
      case -3://港澳学
        changeNumLetterToSpecial(true);
        break;
      case -1://-1完成
        String carCode = getCarCode();
        if (carCode.length() == 7 || carCode.length() == 8) {
          Toast.makeText(this, carCode, Toast.LENGTH_SHORT).show();
          resustCarcode(carCode);
        } else {
          Toast.makeText(this, "请输入完整车牌", Toast.LENGTH_SHORT).show();
        }
        break;
      default://其他键盘
        if (mIndex < 0 || mIndex > 7) {
          mIndex = 0;
        }
        mRbArr[mIndex].setChecked(true);
        mRbArr[mIndex].setText(Character.toString((char) primaryCode));
        mIndex = mIndex + 1;
        if (mIndex > 7) {
          mIndex = 7;
        }
        mRbArr[mIndex].setChecked(true);

        break;
    }
  }
  /**setOnKeyboardActionListener  end*/

  /**
   * 将输入完成的车牌号传回上一个界面
   */
  private void resustCarcode(String sCarCode) {
    Intent intent = new Intent();
    intent.putExtra("carcode", sCarCode);
    setResult(999, intent);
    finish();
  }

  /**
   * 获取车牌号
   */
  private String getCarCode() {
    try {
      if (mOkMax != 7 && mOkMax != 8) {
        return BuildConfig.FLAVOR;
      }
      StringBuilder mSb = new StringBuilder();
      for (int i = 0; i < mOkMax; i++) {
        mSb.append(mRbArr[i].getText());
      }
      return mSb.toString().replace("", BuildConfig.FLAVOR);
    } catch (Exception e) {
      return BuildConfig.FLAVOR;
    }
  }

  /**
   * 清除键盘
   */
  private void cleanView() {
    for (int i = 0; i < 8; i++) {
      mRbArr[i].setText(BuildConfig.FLAVOR);
    }
  }

  /**
   * 切换键盘：省键盘-->字母键盘
   */
  private void changeProToLetter(boolean isChange) {
    if (isChange) {
      mKeyboardView.setKeyboard(mKbLettes);
    } else {
      mKeyboardView.setKeyboard(mKbProvince);
    }
  }

  /**
   * 切换键盘：字母键盘-->数字字母键盘
   */
  private void changeLettersToNumLetter(boolean isChange) {
    if (isChange) {
      mKeyboardView.setKeyboard(mKbNumberLetters);
    } else {
      mKeyboardView.setKeyboard(mKbLettes);
    }
  }

  /**
   * 切换键盘：数字字母键盘-->特殊键盘
   */
  private void changeNumLetterToSpecial(boolean isChange) {
    if (isChange) {
      mKeyboardView.setKeyboard(mKbSpecial);
    } else {
      mKeyboardView.setKeyboard(mKbNumberLetters);
    }
  }

  /**
   * 切换键盘：特殊键盘-->数英
   */
  private void changeSpecialToNumLetter(boolean isChange) {
    if (isChange) {
      mKeyboardView.setKeyboard(mKbNumberLetters);
    } else {
      mKeyboardView.setKeyboard(mKbSpecial);
    }
  }

  /**
   * 键盘展示状态
   */
  private boolean isShow() {
    return mKeyboardView.getVisibility() == View.VISIBLE;
  }

  /**
   * 显示键盘
   */
  private void showKeyboard() {
    int visibility = mKeyboardView.getVisibility();
    if (visibility == View.GONE || visibility == View.INVISIBLE) {
      mKeyboardView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * 隐藏键盘
   */
  private void hideKeyboard() {
    int visibility = mKeyboardView.getVisibility();
    if (visibility == View.VISIBLE) {
      mKeyboardView.setVisibility(View.INVISIBLE);
    }
  }
}
