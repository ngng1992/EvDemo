package com.mfinance.everjoy;

import com.mfinance.everjoy.everjoy.utils.ToolsUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        boolean verfiPwd = ToolsUtils.verfiPwd("@11xiaoXiao");
        boolean verfiPwd1 = ToolsUtils.verfiPwd("XIAOxiao@");
        boolean verfiPwd2 = ToolsUtils.verfiPwd("XIAxiao11111111");
        boolean verfiPwd3 = ToolsUtils.verfiPwd("XIAxiao@@111111");
        boolean verfiPwd4 = ToolsUtils.verfiPwd("XIAxiao@@11111 ");
        System.out.println("verfiPwd=" + verfiPwd);
        System.out.println("verfiPwd1=" + verfiPwd1);
        System.out.println("verfiPwd2=" + verfiPwd2);
        System.out.println("verfiPwd3=" + verfiPwd3);
        System.out.println("verfiPwd4=" + verfiPwd4);
    }
}