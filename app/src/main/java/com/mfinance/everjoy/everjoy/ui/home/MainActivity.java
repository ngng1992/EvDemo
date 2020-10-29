package com.mfinance.everjoy.everjoy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.service.event.base.MessageEvent;
import com.mfinance.everjoy.everjoy.service.event.base.LoginEvent;
import com.mfinance.everjoy.everjoy.ui.ipo.IPOFragment;
import com.mfinance.everjoy.everjoy.ui.mine.MineFragment;
import com.mfinance.everjoy.everjoy.ui.news.NewsFragment;

import net.mfinance.commonlib.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseViewActivity {

    @BindView(R.id.tv_nav_main)
    TextView tvNavMain;
    @BindView(R.id.tv_nav_ipo)
    TextView tvNavIpo;
    @BindView(R.id.tv_nav_news)
    TextView tvNavNews;
    @BindView(R.id.tv_nav_mine)
    TextView tvNavMine;

    private MainFragment mainFragment;
    private IPOFragment iPOFragment;
    private NewsFragment newsFragment;
    private MineFragment mineFragment;

    /**
     * 当前显示的fragment
     */
    private Fragment currentFragment;

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startMainActivity2(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(View currentView) {
        loginEMMob();
        initDefaultFragment();


//        MessageEvent messageEvent = new MessageEvent(1, new LoginEvent("johnnymf", "mF123456"));
//        EventBus.getDefault().post(messageEvent);

//        initPriceProcess();
    }

//    private void initPriceProcess() {
//        ConnectionStatus connectionStatus = mMobileTraderApplication.data.getGuestPriceAgentConnectionStatus();
//        switch (connectionStatus) {
//            case CONNECTING:
//            case CONNECTED:
//                Log.e("msg", "=====================");
//                Message message = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
//                message.arg1 = PriceAgentConnectionProcessor.ActionType.DISCONNECT.getValue();
//                try {
//                    mService.send(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Message message1 = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
//                message1.arg1 = PriceAgentConnectionProcessor.ActionType.RESET.getValue();
//                try {
//                    mService.send(message1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 默认首页
     */
    private void initDefaultFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance("", "");
        }
        if (!mainFragment.isAdded()) {
            fragmentTransaction.add(R.id.frame_main, mainFragment);
            fragmentTransaction.commit();
        }
        currentFragment = mainFragment;

        tvNavMain.setSelected(true);
        tvNavIpo.setSelected(false);
        tvNavNews.setSelected(false);
        tvNavMine.setSelected(false);
    }

    /**
     * 显示当前页
     */
    private void showFragment(Fragment fragment) {
        if (currentFragment == fragment) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.add(R.id.frame_main, fragment);
        } else {
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
        currentFragment = fragment;
    }

    /**
     * 判断环信有没有登录，环信可能会掉线，这里再登录一次
     */
    private void loginEMMob() {
//        UserInfo userInfo = UserSharedPUtils.getUserInfo();
//        if (userInfo != null) {
//            UserInfo.DataBean data = userInfo.getData();
//            String easemobUsername = data.getEasemobUsername();
//            EMLogin.loginEm(this, easemobUsername, null);
//        }
    }

    @OnClick({R.id.tv_nav_main, R.id.tv_nav_ipo, R.id.tv_nav_news, R.id.tv_nav_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_nav_main:
                if (mainFragment == null) {
                    mainFragment = MainFragment.newInstance("", "");
                }
                showFragment(mainFragment);
                tvNavMain.setSelected(true);
                tvNavIpo.setSelected(false);
                tvNavNews.setSelected(false);
                tvNavMine.setSelected(false);
                break;
            case R.id.tv_nav_ipo:
                if (iPOFragment == null) {
                    iPOFragment = IPOFragment.newInstance("", "");
                }
                showFragment(iPOFragment);
                tvNavMain.setSelected(false);
                tvNavIpo.setSelected(true);
                tvNavNews.setSelected(false);
                tvNavMine.setSelected(false);
                break;
            case R.id.tv_nav_news:
                if (newsFragment == null) {
                    newsFragment = NewsFragment.newInstance("", "");
                }
                showFragment(newsFragment);
                tvNavMain.setSelected(false);
                tvNavIpo.setSelected(false);
                tvNavNews.setSelected(true);
                tvNavMine.setSelected(false);
                break;
            case R.id.tv_nav_mine:
                if (mineFragment == null) {
                    mineFragment = MineFragment.newInstance(app, mService, mServiceMessengerHandler, "", "");
                }
                showFragment(mineFragment);
                tvNavMain.setSelected(false);
                tvNavIpo.setSelected(false);
                tvNavNews.setSelected(false);
                tvNavMine.setSelected(true);
                break;
            default:
                break;
        }
    }

    // 第一次按back时的时间
    private long mExitTime;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            String name = String.format(getString(R.string.toast_exit), getString(R.string.app_name));
            ToastUtils.showToast(this, name);
            mExitTime = System.currentTimeMillis();
        } else {
            // 进入桌面，不finish，第二次进入更快，数据是上一次的刷新，可以刷新
            Intent intent = new Intent(Intent.ACTION_MAIN);
            // 同目标Activity一起销毁，然后重新创建目标Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

            // 退出程序
//                System.exit(0);
        }
    }
}