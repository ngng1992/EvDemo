package net.mfinance.commonlib.share.bean;

public class LoginBean {

    public static final String SEX_MALE = "M";

    public static final String SEX_FEMALE = "F";

    private String openId;
    private String nickname;
    /**
     * 微信 普通用户性别，1 为男性，2 为女性 已转
     */
    private String sex;
    private String headimgurl;

    public LoginBean() {
    }

    public LoginBean(String openId, String nickname, String sex, String headimgurl) {
        this.openId = openId;
        this.nickname = nickname;
        this.sex = sex;
        this.headimgurl = headimgurl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
