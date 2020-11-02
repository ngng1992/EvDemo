服务器

External IP: 202.155.229.105:24387
Username: MFHKW005\szdev
Password: s!9nF#SAVMz


ftp

szftp.mfcloud.net:24388
szdev
3Ra@pUCDrTz$


测试邮箱

https://webmail.tradingsky.net/
mfdev
mF34266223ok


文档地址

    http://ejconfluence.m-finance.net/display/EJ/Web+Services
    
    https://srcsvn.m-finance.net/svn/mF/_CustomerImage/Everjoy/desktop-preview
    
    http://ejconfluence.m-finance.net/pages/viewpage.action?pageId=1048588

Weibo
App Key：1736060757
App Secret：bf264786dc4d9d1ef1d32fb1fc0c3992

Twitter
API key: 1TD1MKyHcf6sUbg4201vzZzrZ
API key secret: ymqYcbmFvzhLPvI9wyowsdPAiohkFaCYVwfA17LWdsL6glPUfL
Bearer token: AAAAAAAAAAAAAAAAAAAAAM%2BpJAEAAAAA7lXNhs9Zxdp%2F2utZR9KCtBg6XUw%3DKg6vU4izHukatq3ULDOZbGEtns8sN1w



companySetting loginInfoTest 換成"202.155.229.105", 15100
jimmy.xiao@m-finance.net mF123456

confluence app文档

    http://ejconfluence.m-finance.net/

    devandroid
    mF123456

每天发送工作日志

    tommy.ng@m-finance.net

    terry.chan@m-finance.net

测试账号

    帳號：johnnymf 密碼：mF123456

证券登录账号

  User: TEST01
  Password:        htqbatyrgwk

  User: TEST02
  Password:        shchejgveqb

  User: TEST03
   Password:        emuxnmwbqmh

收件人 : Chris Chui
联系电话 : 3979 6648
香港告士打道77告士打道77-79号富通大厦1801室


1. Facebook App ID,
3368598136542224

Facebook Testing account,
open_jqrwmyj_user@tfbnw.net
mF123456

2. Instagram App ID,
3019715511588677

Instagram App Secret,
a1d9a65e800c93225e14e1dcf2bb43cf

Instagram Testing account,
everjoy.android.test
mF123456

Android and iOS:

    https://srcsvn.m-finance.net/svn/mF/_CustomerImage/Everjoy/mobile

## 第三方平台

twitter推特

    开发者地址：https://developer.twitter.com/en/verify

    github文档：https://github.com/twitter-archive/twitter-kit-android

Instagram

    开发者官网：https://www.instagram.com/developer/

友盟账号【<https://www.umeng.com】>

    账号：
    密码：
    手机号：
    邮箱：
    
环信账号【<https://www.easemob.com】>

    账号：
    密码：
    手机号： 
    邮箱：
    
微信开放平台【<https://open.weixin.qq.com】>

      账号：devsz@m-finance.net
      密码：hkfinance

        AppID：wx12cc7dcb43d518c2

## 第三方库

    网络：[okhttp3]()
    网络：[retrofit2]()

    动态权限：[AndPermission](https://github.com/yanzhenjie/AndPermission)

    Utils工具类：[utilcode](https://github.com/Blankj/AndroidUtilCode)

    RecyclerView使用：[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

    数据解析：[gson](https://github.com/google/gson)

    图片加载：[Glide](https://github.com/bumptech/glide)

    轮播图[banner](https://github.com/youth5201314/banner)

    view注解：[butterknife](https://github.com/JakeWharton/butterknife)

    小红点：[BadgeView](https://github.com/qstumn/BadgeView)

    内存泄露检测：[leakcanary](https://github.com/square/leakcanary)

    图片、视频、文件选择器：[PictureSelector](https://github.com/LuckSiege/PictureSelector)

    图片查看器，仿微信：[BigImageViewPager](https://github.com/SherlockGougou/BigImageViewPager)

    仿微信长按menu：[BottomNavigationViewEx](https://github.com/JavaNoober/FloatMenu)

    侧滑删除：[SwipeRecyclerView](https://github.com/yanzhenjie/SwipeRecyclerView)

    时间、多项选择、地址选择等选择器：[Android-PickerView](https://github.com/Bigkoo/Android-PickerView)

    Utils工具：[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

    EventBus：[EventBus消息传递](https://github.com/greenrobot/EventBus)
    
    PickerView：[PickerView滚动选择器](https://github.com/duanhong169/PickerView)

    新浪sdk：[新浪sdk](https://github.com/sinaweibosdk/weibo_android_sdk)

    facebook：[fecebook登录分享](https://github.com/facebook/facebook-android-sdk)
    
    wlmedia：[音视频播放](https://github.com/wanliyang1990/wlmedia)

## cmd命令

    查看签名：keytool -list -v -keystore apk签名文件
    app：
        MD5  52:AD:DC:E0:30:59:42:A3:E7:27:FF:DD:45:B8:0D:82:05:72:68:C1
        SHA1: 52:AD:DC:E0:30:59:42:A3:E7:27:FF:DD:45:B8:0D:82:05:72:68:C1
        SHA256: DF:04:7E:4C:39:F7:27:A1:46:CE:8C:4D:20:65:A3:56:BD:B4:2B:28:E0:
        D7:A7:57:39:8E:76:7E:9D:7E:76:12
        
    keytool -list -v -keystore debug.keystore
        开发版：SHA1: E8:CB:5E:CA:BC:F5:BF:D5:57:26:7E:97:16:62:49:BE:13:E7:C5:42

    ping + ip： 查看某一个ip地址是否能够连通，如： ping 114.80.67.193

    telnet ip port ： 查看某一个机器上的某一个端口是否可以访问，如：telnet 114.80.67.193 8080

## 注意：

    1. butterknife使用
    
        组件化之后butterknife注解view时，使用@BindView(R2.id.btn_next)，
        点击事件里面@OnClick({R2.id.bt_login})，
        不能使用switch，要使用if...else...
        if (id == R.id.bt_login)
    
## 账号测试

    SIB：
    帳號：johnnymf 密碼：mF123456
