#utilslib
## avatar包
 封装了通过相机拍照，相册选择图片进行头像设置(适配Android7.0)。只能再Activity中使用
 使用步骤
 * CropImageUtils.register(Context applicationcontext, String providerPath, String photoPath,
                                String cropPath)
 * 相册获取： CropImageUtils.getInstance().openAlbum(this);
 * 相机获取： CropImageUtils.getInstance().takePhoto(this);
 * onActivityResult回调
     @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("onActivityResult requestCode:"+requestCode+"resultCode:"+resultCode);
        CropImageUtils.getInstance()
            .onActivityResult(this, requestCode, resultCode, data,
                new CropImageUtils.OnResultListener() {
                  @Override public void takePhotoFinish(String path) {
                    //拍照回调，去裁剪
                    CropImageUtils.getInstance().cropPicture(this, path);
                  }

                  @Override public void selectPictureFinish(String path) {
                    //相册回调，去裁剪
                    CropImageUtils.getInstance().cropPicture(this, path);
                  }

                  @Override public void cropPictureFinish(File file, final String path) {
                    LogUtil.d("裁剪保存在这个文件：" + file +" 所在路径为："+path);
                  }
                });
      }


## update包
  此包包含apk更新包含适配了Android7.0适配的下载更新
  * DowndApkProgress 下载apk安装包进度条。 资源文件包含：drawable小的bg_progress和bg_progress_back，，styles属性定义
  * UpdateBean apk下载更新所需的信息统一封装到此类中，传到UpdateManager中完成更新
  * UpdateManager 调起apk更新的入口。资源文件包含：layout下的progress_softupdate，styles属性定义
## permission包
  Rxjava结合RxPermssion实现Android6.0的权限动态请求
  参考1[http://www.91display.com/api/blog/detail.do?bid=589843799]
  参考2[https://github.com/totond/PermissionsApplyDemo/blob/master/app/src/main/java/yanzhikai/com/permissionsapplydemo/RxPermissionsActivity.java]
  注意：
  * 动态请求的权限在清单文件中也要写，不然会弹不出权限框
  * 引入的Rxjava的依赖包，引入库到项目中注意包的重复问题
## statusbar包
  修改状态栏颜色和修改状态栏字体颜色。来自：[Dynckathline的github项目](https://github.com/DyncKathline/ChangeStatusColor-Android)
  * 使用前再onstart()方法中 设置导航栏是否可见：StatusBarUtil.setSystemUI(MainActivity.this, true);

  * 状态栏字体为白色：StatusBarUtil.StatusBarLightMode(MainActivity.this, false);
  * 状态栏字体为深色：StatusBarUtil.StatusBarLightMode(MainActivity.this, true);

  * 添加全屏(横屏)：StatusBarUtil.setFitsSystemWindows(MainActivity.this, true);
  * 清除全屏(竖屏)：StatusBarUtil.setFitsSystemWindows(MainActivity.this, false);

  * 修改状态栏颜色：StatusBarUtil.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.colorAccent));
  * 也可自己写一个颜色选择器根据用户选择显示不用颜色风格的颜色

## Android7.0适配注意
  * avatar包和update包都涉及到了Android7.0的适配问题，会涉及到provider的路径问题。因为规则是:包名+.provider
  * 都要传入对应项目的包名。进行provider的拼接才可实现
  * 头像设置时，CropImageUtils直接传入对应的包名.provider 形如：com.xuemei.utilslib.provider即可
  * apk更新时，直接把对应的包名也封装到UpdateBean实体类中即可
  * 对于清单文件的配置Provider是否成功，后续用到进行测试，现在未进行测试

## ActivityManager
  应用程序Activity管理类：用于Activity管理和应用程序退出
  * 添加Activity到堆栈
  * 获取当前Activity（堆栈中最后一个压入的）
  * 结束当前Activity（堆栈中最后一个压入的）
  * 结束指定的Activity
  * 结束指定类名的Activity
  * 结束所有Activity
  * 根据ActivityName获取堆中Activity实例
  * 退出应用程序

## AppUtil
  APP相关信息工具类。
  * 获得渠道号
  * 获取版本名称
  * 获取版本号
  * 获取应用签名
  * 安装apk文件
  * 卸载apk
  * 检查手机上是否安装了指定的软件
  * 从apk中获取版本信息


## BitmapUtil
 图片处理工具类，比较全。写着专项研究

## DeviceUtil
 设备相关工具类
  * 获取本机IP地址
  * 服务是否运行
  * 进程是否运行
  * 获取IMEI
  * 获取MAC地址
  * 获取UDID
  * 获取设备唯一标识UUID 本方法调用需要READ_PHONE_STATE权限
  * 震动
  * 获取手机大小（分辨率）
  * 复制到剪切板
  * 获取非系统应用包名
  * 判断某个应用是否已经安装
  * dp2px,px2dp,px转sp
  * 判断是否有软控制键（手机底部几个按钮）
  * 获取statusbar高度
  * 获取navigationbar高度
  * 获取状态栏高度＋标题栏(ActionBar)高度(注意，如果没有ActionBar，那么获取的高度将和上面的是一样的，只有状态栏的高度)
  * 根据包名启动Activity
  * 隐藏键盘：强制隐藏
  * 显示输入法
  * 判断输入法是否处于激活状态
  * 主动回到Home，后台运行
  * 拨打电话
  * 跳转至拨号界面
  * 发送短信
  * 判断当前设备是否为手机
  * 获取最后一次拍照的图片

## FileUtil
文件操作工具类
  * 判断SD卡是否可以工作
  * 多个SD卡时 取外置SD卡
  * 获取可用空间大小
  * 获取SD大小
  * 文件是否创建
  * 删除文件夹
  * 根据路径删除某个文件
  * 根据File 删除文件
  * 删除所有文件夹和文件
  * 复制单个文件
  * 复制整个文件夹内容
  * 根据路径判断File是否存在是否为File文件
  * 获取SD卡的路径
  * 获取服务器文件的名称
  * 字符串转化成输入流
  * 输入流转byte[]
  * 获取所有SD卡路径
  * 获取所有的U盘、内置sd卡  、外置sd卡 路径
  * 获取已经挂载的路径
  * 根据文件后缀名获得对应的MIME类型
  * 根据文件名，将文件内容转成byte[]
  * 把字节数组写到文件中
  * 创建文件
  * 获取所有文件
  * 文件是否存在
  * 读取文件内容

## LogUtil
参考地址1[https://github.com/pengwei1024/LogUtils],具体相关功能请查看相关连接(类似但未使用)
需传入Context，首先需要在Application中先LogUtil.register(Context context)
  * log过滤器(包含不同日志级别)
  * log总开关(是否打开Log打印开关)
  * log标签
  * log标签是否为空白
  * log写入文件开关
  * log存储目录
  * log边框开关
  * 可打印file，json，xml

## NetworkUtil
网络工具类
  * 判断网络连接是否打开(是否可用),包括移动数据连接
  * GPS是否打开
  * 检测当前打开的网络类型是否WIFI
  * 检测当前打开的网络类型是否3G
  * 检测当前开打的网络类型是否4G
  * 判断是否打开WIFI
  * IP地址校验
  * IP转化成int数字
  * 判断当前是否网络连接属于什么网络类型连接
  * 获取移动终端类型
  * 获取URL中参数 并返回Map
  * 是否是网络链接

## RegexValidateUtil
  * 验证车牌号
  * 验证邮箱
  * 验证手机号码
  * 校验输入昵称
  * 校验验证码
  * 校验身份证号码是否符合规则
  * 检测String是否全是中文
  * 判断是否输入汉字
  * 判断字符是否是英文或数字，或-、_
  * 判断字符串是否含中英文，数字，或-、_
  * 是否只含字母和数字
  * 是否只包含数字
  * 是否只包含字母
  * 是否只是中文
  * 是否包含中文
  * 是否包含小数点位数

## TimeUtil
  * 获取当前时间
  * 获取当前时间为本月的第几周
  * 获取当前时间为本周的第几天
  * 获取当前时间的年份
  * 获取当前时间的月份
  * 获取当前时间是哪天
  * 时间比较大小
  * 时间加减
  * 毫秒格式化
  * 时间戳转北京时间
  * 北京时间转时间戳
  * 获得口头时间字符串，如今天，昨天，昨天，刚刚，分钟前，小时前

## ToastUtil
使用步骤：
* 1、在Application中先ToastUtil.register(Context context)
* 2、注册完成后项目中可进行正常使用
包含功能：
  * 短时间显示
  * 长时间显示
  * 线程内短时间显示
  * 线程内长时间显示
  * 线程外短时间显示
  * 线程外长时间显示



