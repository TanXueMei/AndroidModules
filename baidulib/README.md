#本库说明

百度地图相关功能模块
此模块是高磊大神
[github项目](https://github.com/gaoleiandroid1201/BiuBike)

##一、导航模块

**custom包**

* NaviSelectDialog:内置导航，百度地图，高德地图导航选择。当安装有百度地图和高德地图，列表就展示，否则只显示内置导航。
* SelectDialog:是否打开GPS对话框

**util包**

* BNinitFile:百度地图导航文件夹初始化。路径，文件名称都在此类
* Utils：距离转换，判断gps是否打开等等百度地图需要的工具类
* LocationManager:定位完成，保存定位信息管理类(主要保存当前位置可设置导航起始位置)
* NavUtil：这个工具类实现了调用内置导航和打开第三方App导航。注意：assets中的文件必须拷贝到项目，想使用内置导航，必须初始化导航， NavUtil.initNavi(this);

**导航调用**

在需要导航的界面：

1、NavUtil.initNavi(this);//初始化内置导航

2、NavUtil.showChoiceNaviWayDialog(this, startLL, endLL, startName, endName);

**导航存在问题**

1、导航初始化目前存在：LBSAuthManager报内存泄露

2、导航时：NaviModuleManager报内存泄露