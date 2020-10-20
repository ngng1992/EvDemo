package com.mfinance.everjoy.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.ContactUs;

import java.util.Locale;

public class ContactActivity extends BaseActivity {

    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public boolean isTopBarExist() {
        return true;
    }

    @Override
    public boolean isBottonBarExist() {
        return false;
    }

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_contact_us);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public boolean showLogout() {
        return false;
    }

    @Override
    public boolean showTopNav() {
        return true;
    }

    @Override
    public boolean showConnected() {
        return false;
    }

    @Override
    public boolean showPlatformType() {
        return false;
    }

    @Override
    public int[] getTitleText() {
        return new int[]{R.id.tvTitle, R.string.db_contact_us};
    }

    public int getHeaderId() {
        return -1;
    }

    public int getHeaderTemplateId() {
        return -1;
    }

    public int getContentTemplateId() {
        return R.layout.d_t3;
    }

    @Override
    public int getServiceId(){
        return ServiceFunction.SRV_MOVE_TO_CONTACT_US;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_MOVE_TO_CONTACT_US;
    }

    @Override
    public boolean isLoadedData(){
        if( CompanySettings.ENABLE_WEBVIEW_CONTACT_US == true )
            return true;
        else
            return    ( (app).data.getContactUs()!=null );
    }

    @Override
    public int getLoadingViewId() {
        return R.id.flLoading;
    }


    @Override
    public void loadUIData(){
        if( CompanySettings.ENABLE_WEBVIEW_CONTACT_US == true )
        {
            WebView myWebView = (WebView)findViewById(R.id.wvContent);
            if( getLanguage() == Locale.ENGLISH )
            {
                myWebView.loadUrl(app.hmContactUSUrls.get("EN"), null);
            }
            else if( getLanguage() == Locale.TRADITIONAL_CHINESE )
            {
                myWebView.loadUrl(app.hmContactUSUrls.get("TC"), null);
            }
            else if( getLanguage() == Locale.SIMPLIFIED_CHINESE )
            {
                myWebView.loadUrl(app.hmContactUSUrls.get("SC"), null);
            }

            myWebView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
        else
        {
            if ( (app).data.getContactUs() != null){
                String sIntro = null;
                String sCompany = null;
                String sAddress = null;
                String sDisclaim = null;
                ContactUs contactUs = (app).data.getContactUs();
                String html = "<body style=\"color: "+res.getString(R.string.wv_font_color)+"; font-family: Helvetica; font-size: 12pt; word-wrap: break-word \">";

                if (Utility.isSimplifiedChineses()) {
                    sIntro = contactUs.getIntroGB();
                    sCompany =  contactUs.getCompanyGB();
                    sAddress = contactUs.getAddressGB();
                    sDisclaim = contactUs.getDisclaimGb();
                } else if (Utility.isTraditionalChinese()) {
                    sIntro = contactUs.getIntroBig5();
                    sCompany =  contactUs.getCompanyBig5();
                    sAddress = contactUs.getAddressBig5();
                    sDisclaim = contactUs.getDisclaimBig5();
                } else {
                    sIntro = contactUs.getIntroEN();
                    sCompany =  contactUs.getCompanyEN();
                    sAddress = contactUs.getAddressEN();
                    sDisclaim = contactUs.getDisclaimEn();
                }

                html += "<table>";
                html += "<tr VALIGN=TOP>";
                html += "<td width = 90></td>";
                html += "<td></td>";
                html += "<td></td>";
                html += "</tr>";
                if(!sIntro.equals(""))
                    html += String.format("<tr><td colspan=3>%s</td></tr><tr><td>&nbsp;</td></tr>",sIntro);

                if(!sAddress.equals("")){
                    html += "<tr VALIGN=TOP>";
                    html += "<td>"+res.getText(R.string.lb_address)+"</td>";
                    html += "<td> : </td>";
                    html += "<td>"+sAddress+"</td>";
                    html += "</tr>";}

                if(!contactUs.getTel().equals("")){
                    html += "<tr>";
                    html += "<td>"+res.getText(R.string.lb_tel)+"</td>";
                    html += "<td> : </td>";
                    html += "<td>"+"<a href=\"tel:"+contactUs.getTel()+"\" style=\"color:#1E90FF;\">"+contactUs.getTel()+"</a></td>";
                    html += "</tr>";}

                if(!contactUs.getHotline().equals("")){
                    html += "<tr>";
                    html += "<td>"+res.getText(R.string.lb_24_hotline)+"</td>";
                    html += "<td> : </td>";
                    html += "<td>"+"<a href=\"tel:"+contactUs.getHotline()+"\" style=\"color:#1E90FF;\">"+contactUs.getHotline()+"</a></td>";
                    html += "</tr>";}

                if(!contactUs.getChinaHotline().equals("")){
                    html += "<tr>";
                    html += "<td>"+res.getText(R.string.lb_china_free)+"</td>";
                    html += "<td> : </td>";
                    html += "<td>"+"<a href=\"tel:"+contactUs.getChinaHotline()+"\" style=\"color:#1E90FF;\">"+contactUs.getChinaHotline()+"</a></td>";
                    html += "</tr>";}

                if(!contactUs.getFax().equals("")){
                    html += "<tr>";
                    html += "<td>"+res.getText(R.string.lb_fax)+"</td>";
                    html += "<td> : </td>";
                    html += "<td>"+"<a href=\"tel:"+contactUs.getFax()+"\" style=\"color:#1E90FF;\">"+contactUs.getFax()+"</a></td>";
                    html += "</tr>";}

                if(!contactUs.getEmail().equals("")){
                    html += "<tr>";
                    html += "<td>"+res.getText(R.string.lb_email)+"</td>";
                    html += "<td> : </td>";
                    html += "<td>"+"<a href=\"mailto:"+contactUs.getEmail()+"\" style=\"color:#1E90FF;\">"+contactUs.getEmail()+"</a></td>";
                    html += "</tr>";}

                if(!contactUs.getWebsite().equals("")){
                    html += "<tr>";
                    html += "<td>"+res.getText(R.string.lb_website)+"</td>";
                    html += "<td> : </td>";
                    html += "<td>"+"<a href=\""+contactUs.getWebsite()+"\" style=\"color:#1E90FF;\">"+contactUs.getWebsite()+"</a></td>";
                    html += "</tr>";}

                if(!sDisclaim.equals(""))
                    html += String.format("<tr><td>&nbsp;</td></tr><tr><td colspan=3>%s</td></tr>",sDisclaim);

                html += "</table>";
                ((WebView)findViewById(R.id.wvContent)).setBackgroundColor(Color.TRANSPARENT);
                ((WebView)findViewById(R.id.wvContent)).loadDataWithBaseURL(null, html, mimetype, encoding, null);


                ((WebView)findViewById(R.id.wvContent)).setWebViewClient(new WebViewClient(){
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (url.startsWith("tel:")) {
                            Intent intent = new Intent(Intent.ACTION_DIAL,
                                    Uri.parse(url));
                            startActivity(intent);
                        } else if (url.startsWith("mailto:")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(url));
                            startActivity(intent);
                        }else if(url.startsWith("http:") || url.startsWith("https:") ) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(url));
                            startActivity(intent);
                        }
                        return true;
                    }
                });


                sIntro = null;
                sCompany = null;
                sAddress = null;
                contactUs = null;
                html = null;
            }
        }
    }

}
