package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.network.URLContents;
import com.mfinance.everjoy.everjoy.network.okgo.OkGoBodyToStringCallBack;
import com.mfinance.everjoy.everjoy.sp.AppSharedPUtils;

import java.util.concurrent.locks.ReentrantLock;

public class ForgotPasswordOTPRequestProcessor implements MessageProcessor {
    private final String TAG = this.getClass().getSimpleName();
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        Bundle data = msg.getData();

        OkGo.<String>get(URLContents.FORGOTPASSWORD)
                .tag("ForgotPasswordOTPRequestProcessor")
                .params("type", 2)
                .params("email", data.getString(ServiceFunction.FORGETPASSWORD_EMAIL))
                .params("otp", data.getString(ServiceFunction.FORGETPASSWORD_OTP))
                .execute(new OkGoBodyToStringCallBack() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccessBody(BaseBean baseBean) {
                        super.onSuccessBody(baseBean);
                        System.out.println("onSuccessBody " + baseBean.getMessage());
                        service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
                        service.broadcast(ServiceFunction.ACT_GO_TO_LOGIN, null);
                    }

                    @Override
                    public void onFail(BaseBean baseBean) {
                        super.onFail(baseBean);
                        System.out.println("onFail " + baseBean.getMessage());
                        service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

        return false;
    }
}
