package com.mfinance.everjoy.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import net.mfinance.commonlib.share.Utils;
import net.mfinance.commonlib.share.bean.LoginBean;
import net.mfinance.commonlib.share.bean.ShareBean;
import net.mfinance.commonlib.share.wechat.WechatUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 微信分享登录
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI iwxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WechatUtils.initWechat(this);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        // 请求
    }

    @Override
    public void onResp(BaseResp baseResp) {
        // 响应
        int type = baseResp.getType();
        int errCode = baseResp.errCode;
        Log.e("wechat", "errCode = " + errCode + ";type = " + type);

        // 登录
        if (type == ConstantsAPI.COMMAND_SENDAUTH) {
            if (errCode == 0) {
                SendAuth.Resp authResp = (SendAuth.Resp) baseResp;
                String code = authResp.code;
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + Utils.WECHAT_APP_ID + "&secret=" + Utils.WECHAT_APP_KEY
                        + "&code=" + code + "&grant_type=authorization_code";
                OkGo.<String>get(url)
                        .tag(this)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                requestUserInfo(response.body());
                            }
                        });
            } else {
                Log.e("wechat", "登录失败===============");
                finish();
            }
        }

        // 分享
        if (type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            if (errCode == 0) {
                // TODO 分享取消，分享成功，都会调用
                ShareBean shareBean = new ShareBean();
                EventBus.getDefault().post(shareBean);
            } else {
                Log.e("wechat", "分享失败===============");
            }
            finish();
        }
    }

    private void requestUserInfo(String jsonUser) {
        String access_token = "";
        String openid = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonUser);
            access_token = jsonObject.optString("access_token");
            openid = jsonObject.optString("openid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(access_token) || TextUtils.isEmpty(openid)) {
            Log.e("wechat", "登录失败，token为null 或openid为null ===============");
            return;
        }

        String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        OkGo.<String>get(userUrl)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            String openid1 = jsonObject.optString("openid");
                            String nickname = jsonObject.optString("nickname");
                            int sex = jsonObject.optInt("sex", 1);
                            String headimgurl = jsonObject.optString("headimgurl");
                            String gender = sex == 1 ? LoginBean.SEX_MALE : LoginBean.SEX_FEMALE;
                            LoginBean loginBean = new LoginBean(openid1, nickname, gender, headimgurl);
                            // 发送登录成功的消息到登录界面
                            EventBus.getDefault().post(loginBean);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }
}
