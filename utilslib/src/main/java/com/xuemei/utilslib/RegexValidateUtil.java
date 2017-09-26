package com.xuemei.utilslib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/26
 *     desc   : 正则表达式验证工具类
 *             验证邮箱，手机号，车牌号等
 *     version: 1.0
 * </pre>
 */

public class RegexValidateUtil {
  private static final String MOBIEL_PATTERN = "^(13|14|15|17|18)\\d{9}$";
  private static final String EMAIL_PATTERN = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{_2,}$";
  private static final String INPUT_PSWORD = "^[A-Za-z0-_9]{6,16}$";
  private static final String INPUT_CODE = "^[A-Za-z0-_9]{4}$";
  private static final String INPUT_NICKNAME = "[\\u4E00-\\u9FA5\\\\w]+";
  private static final String PSW="^(?=.*?[a-zA-Z]).{6,16}$";
  private static final String INPUT_PLATENUMBER="^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽港澳贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4,5}[A-Z0-9挂学警港澳]{1,2}$";

  /**
   * 验证车牌号
   */
  public static boolean checkPlateNumber(String plateNumber){
    return matches(plateNumber,INPUT_PLATENUMBER);
  }
  /**
   * 验证邮箱
   *
   * @param email 要验证的邮箱
   * @return
   */
  public static boolean checkEmail(String email) {
    return matches(email, EMAIL_PATTERN);
  }

  /**
   * 验证手机号码
   *
   * @param mobileNumber
   * @return true 正确  false 错误
   */
  public static boolean checkMobileNumber(String mobileNumber) {
    return matches(mobileNumber, MOBIEL_PATTERN);
  }

  private static boolean matches(String text, String pattern) {
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(text);
    return matcher.matches();

  }

  /**
   * 校验输入昵称
   */
  public static boolean checkNickname(String nickname) {
    return matches(nickname, INPUT_NICKNAME);
  }

  public static boolean checkPsw(String psw) {
    return matches(psw, INPUT_PSWORD);
  }
  public static boolean checkPsww(String psw) {
    return matches(psw, PSW);
  }
  /**
   * 校验验证码
   */
  public static boolean checkCode(String code) {
    return matches(code, INPUT_CODE);
  }

  /**
   * 校验身份证号码是否符合规则
   */
  public static boolean personIdValidation(String text) {
    String regx = "[0-9]{17}x";
    String reg1 = "[0-9]{15}";
    String regex = "[0-9]{18}";
    return text.matches(regx) || text.matches(reg1) || text.matches(regex);
  }

  /**
   * 检测String是否全是中文
   *
   * @param name
   * @return
   */
  public static boolean checkNameChese(String name) {
    boolean res = true;
    char[] cTemp = name.toCharArray();
    for (int i = 0; i < name.length(); i++) {
      if (!isChinese(cTemp[i])) {
        res = false;
        break;
      }
    }
    return res;
  }

  /**
   * 判断是否输入汉字
   *
   * @param c
   * @return
   */
  public static boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
      return true;
    }
    return false;
  }

  /**
   * 判断字符是否是英文或数字，或-、_
   *
   * @return
   */
  public static boolean isEngOrNumOr(String string) {
    boolean result = false;
    try {
      Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]+$");
      Matcher matcher = pattern.matcher(string);
      result = matcher.matches();
    } catch (Exception e) {
      result = false;
    }
    return result;
  }
  /**
   * 判断字符串是否含中英文，数字，或-、_
   */
  public static boolean isEngOrCh(String string) {
    boolean result = false;
    try {
      Pattern pattern = Pattern.compile("^[_\\-A-Za-z0-9\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
      Matcher matcher = pattern.matcher(string);
      result = matcher.matches();
    } catch (Exception e) {
      result = false;
    }

    return result;
  }
  /**
   * 是否只含字母和数字
   *
   * @param data 可能只包含字母和数字的字符串
   * @return 是否只包含字母和数字
   */
  public static boolean isNumberLetter(String data) {
    String expr = "^[A-Za-z0-9]+$";
    return data.matches(expr);
  }

  /**
   * 是否只包含数字
   *
   * @param data 可能只包含数字的字符串
   * @return 是否只包含数字
   */
  public static boolean isNumber(String data) {
    String expr = "^[0-9]+$";
    return data.matches(expr);
  }

  /**
   * 是否只包含字母
   *
   * @param data 可能只包含字母的字符串
   * @return 是否只包含字母
   */
  public static boolean isLetter(String data) {
    String expr = "^[A-Za-z]+$";
    return data.matches(expr);
  }

  /**
   * 是否只是中文
   *
   * @param data 可能是中文的字符串
   * @return 是否只是中文
   */
  public static boolean isChinese(String data) {
    String expr = "^[\u0391-\uFFE5]+$";
    return data.matches(expr);
  }

  /**
   * 是否包含中文
   *
   * @param data 可能包含中文的字符串
   * @return 是否包含中文
   */
  public static boolean isContainChinese(String data) {
    String chinese = "[\u0391-\uFFE5]";
    if (isEmpty(data)) {
      for (int i = 0; i < data.length(); i++) {
        String temp = data.substring(i, i + 1);
        boolean flag = temp.matches(chinese);
        if (flag) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 是否包含小数点位数
   *
   * @param data   可能包含小数点的字符串
   * @param length 小数点后的长度
   * @return 是否小数点后有length位数字
   */
  public static boolean isDianWeiShu(String data, int length) {
    String expr = "^[1-9][0-9]+\\.[0-9]{" + length + "}$";
    return data.matches(expr);
  }

}
