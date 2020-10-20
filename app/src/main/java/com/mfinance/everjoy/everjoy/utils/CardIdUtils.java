package com.mfinance.everjoy.everjoy.utils;

import com.blankj.utilcode.util.RegexUtils;

public class CardIdUtils {

    /**
     * 验证香港身份证号码
     * <p>
     * 香港身份证号码由三部分组成：一个英文字母；6个数字；括号及0-9中的任一个数字，或者字母A。
     * 括号中的数字或字母A，是校验码，用于检验括号前面的号码的逻辑正确性。
     *
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    public static boolean validateHKCard(String idCard) {
        String match = "/^((\\s?[A-Za-z])|([A-Za-z]{2}))\\d{6}(\\([0−9aA]\\)|[0-9aA])$/";
        return RegexUtils.isMatch(match, idCard);
    }

}
