package com.mfinance.everjoy.everjoy.network.okgo;

import android.text.TextUtils;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;

import net.mfinance.commonlib.language.MultiLanguageUtil;
import net.mfinance.commonlib.toast.ToastUtils;

/**
 * 字符串解析
 */
public abstract class OkGoBodyToStringCallBack extends StringCallback {

    @Override
    public void onSuccess(Response<String> response) {
        int code = response.code();
        if (code != 200) {
            BaseBean baseBean = new BaseBean();
            baseBean.setCode(code);
            // 500
            baseBean.setMessage("Server side unhandled exception");
            onFail(baseBean);
            return;
        }

        String body = response.body();
        if (TextUtils.isEmpty(body)) {
            BaseBean baseBean = new BaseBean();
            baseBean.setCode(-1);
            baseBean.setMessage("body is empty");
            onFail(baseBean);
            return;
        }

        try {
            if (body.contains(",")) {
                String[] split = body.split(",");
                int status = Integer.parseInt(split[0]);
                String msg = split[1];

                BaseBean baseBean = new BaseBean();
                baseBean.setCode(status);
                baseBean.setMessage(msg);
                if (split.length >= 3) {
                    StringBuilder data = new StringBuilder();
                    for (int i = 2; i < split.length; i++) {
                        data.append(split[i]);
                    }
                    baseBean.setData(data.toString());
                }
                if (status == 0) {
                    onSuccessBody(baseBean);
                } else {
                    onFail(baseBean);
                }
            } else {
                BaseBean baseBean = new BaseBean();
                baseBean.setCode(Integer.parseInt(body));
                baseBean.setMessage("");
                onFail(baseBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            BaseBean baseBean = new BaseBean();
            baseBean.setCode(200);
            baseBean.setMessage(e.getMessage());
            onFail(baseBean);
        }
    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        // 网络连接
        boolean connected = NetworkUtils.isConnected();
        if (connected) {
            // 服务器出错，如果解析出错也会报这个错
            ToastUtils.showToast(MultiLanguageUtil.showToastChangeContext(Utils.getApp()), R.string.toast_server_unusual);
        } else {
            // 网络出错
            ToastUtils.showToast(MultiLanguageUtil.showToastChangeContext(Utils.getApp()), R.string.toast_net_unusual);
        }
        BaseBean baseBean = new BaseBean();
        onFail(baseBean);
    }

    /**
     * 成功
     */
    public void onSuccessBody(BaseBean baseBean) {

    }

    /**
     * 错误
     */
    public void onFail(BaseBean baseBean) {

    }
}
