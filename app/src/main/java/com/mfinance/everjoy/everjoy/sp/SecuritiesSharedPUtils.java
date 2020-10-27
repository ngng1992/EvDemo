package com.mfinance.everjoy.everjoy.sp;

import com.blankj.utilcode.util.SPUtils;

/**
 * 证券开户的资料填写保存在本地
 * 如果提交成功，清空
 * 如果未提交，或提交失败，不清空，以备下次直接使用
 * <p>
 * key为控件的id
 * value为选择项或选择项的内容
 */
public class SecuritiesSharedPUtils {

    /**
     * 全局sp对象，app的若干设置
     */
    private static SPUtils SPUTILS = null;

    /**
     * 调用保存方法时，必须先调用init
     */
    public static SPUtils newInstance() {
        if (SPUTILS == null) {
            SPUTILS = SPUtils.getInstance("sp_securities");
        }
        return SPUTILS;
    }

}
