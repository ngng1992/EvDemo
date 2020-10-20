package com.mfinance.everjoy.app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;

import java.util.Calendar;
import java.util.Locale;



public class OnLineStatementActivity extends BaseActivity{	
	PopupSingleDate popFrom;
	PopupSingleDate popTo;
	
	WebView myWebView;
	Button btnClose;
	
	@Override
	public void bindEvent() {
		findViewById(R.id.btnFrom).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				popFrom.showLikeQuickAction();
				popFrom.setCurrDate((((Button)findViewById(R.id.btnFrom)).getText().toString()).split("-"));
			}
		});			
		
		findViewById(R.id.btnTo).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				popTo.showLikeQuickAction();
				popTo.setCurrDate((((Button)findViewById(R.id.btnTo)).getText().toString()).split("-"));
			}
		});				
		
		popFrom.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				if(app.dtTradeDate!=null)
					cal.setTime(app.dtTradeDate);
				
				if(CompanySettings.GET_LAST_TRADEDATE_STATEMENT)
					cal.add(Calendar.DATE, -1);
				
				Calendar calFrom = Calendar.getInstance();
				calFrom.setTime(Utility.toDate(popFrom.getValue()));
				
				if(calFrom.compareTo(cal)<=0){
					((Button)findViewById(R.id.btnFrom)).setText(popFrom.getValue());
					popFrom.dismiss();
				}else{
					Toast.makeText(OnLineStatementActivity.this, R.string.msg_311, Toast.LENGTH_SHORT).show();
				}
			}
		});	
		popFrom.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popFrom.dismiss();
			}
		});	
		
		popTo.btnCommit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				if(app.dtTradeDate!=null)
					cal.setTime(app.dtTradeDate);
				
				if(CompanySettings.GET_LAST_TRADEDATE_STATEMENT)
					cal.add(Calendar.DATE, -1);
				
				Calendar calTo = Calendar.getInstance();
				calTo.setTime(Utility.toDate(popTo.getValue()));
				
				if(calTo.compareTo(cal)<=0){
					((Button)findViewById(R.id.btnTo)).setText(popTo.getValue());
					popTo.dismiss();
				}else{
					Toast.makeText(OnLineStatementActivity.this, R.string.msg_311, Toast.LENGTH_SHORT).show();
				}
			}
		});	
		popTo.btnClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				popTo.dismiss();
			}
		});			
		
		btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				myWebView.setVisibility(View.INVISIBLE);
		        btnClose.setVisibility(View.INVISIBLE);
			}
			
		});
		
		((Button)findViewById(R.id.btnOK)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				StringBuilder sb = new StringBuilder();
				CommonFunction cf = new CommonFunction(false);
				cf.setKey(Utility.getHttpKey());
				
				String sFromDate = ((Button)findViewById(R.id.btnFrom)).getText().toString();
				sFromDate = Utility.dateToStatementString(Utility.toDate(sFromDate));
				
				String sToDate = ((Button)findViewById(R.id.btnTo)).getText().toString();
				sToDate = Utility.dateToStatementString(Utility.toDate(sToDate));
				
				sb.append("UserId=")
					.append(app.data.getBalanceRecord().strAccount)
					//.append("mf6ytjy009")
					.append("&acc=")
					.append(app.data.getBalanceRecord().strAccount)
					//.append("mf6ytjy009")
					.append("&begindate=")
					.append(sFromDate)
					.append("&enddate=")
					.append(sToDate)
					.append("&lang=");
				
				if (getLanguage().equals(Locale.SIMPLIFIED_CHINESE))
					sb.append("cn");
				else if (getLanguage().equals(Locale.TRADITIONAL_CHINESE))
					sb.append("tw");
				else
					sb.append("en");
		
	            sb.append("&date=").append(Utility.sdf6.format(app.dServerDateTime));
    	        sb.append("&time=").append(Utility.tdf.format(app.dServerDateTime));
    	        
				//System.out.println("paramter: "+sb.toString());
				//String sPara = "UserId="+app.data.getBalanceRecord().strAccount+"&Acc="+app.data.getBalanceRecord().strAccount+"&begindate="+sDate+"&enddate="+sDate+"&lang="+sLanguage;
				String report = app.getStatmentURL()+cf.encryptText(sb.toString());				
				//pdf="http://docs.google.com/gview?embedded=true&url=" + pdf;
				//System.out.println("report = "+report);
				//System.out.println("after = "+cf.decryptText(cf.encryptText(sb.toString())));
				//System.out.println(report);
				if (myWebView == null)
					myWebView = (WebView) findViewById(R.id.wvReport);				
				myWebView.getSettings().setJavaScriptEnabled(true);
				
				myWebView.setWebViewClient(new WebViewClient() {

					@Override
					public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
						final AlertDialog.Builder builder = new AlertDialog.Builder(OnLineStatementActivity.this, CompanySettings.alertDialogTheme);

						builder.setTitle(res.getText(R.string.ssl));
						builder.setMessage(res.getText(R.string.ssl_continue));
						builder.setPositiveButton(res.getText(R.string.btn_ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								handler.proceed();
							}
						});
						builder.setNegativeButton(res.getText(R.string.btn_cancel), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								handler.cancel();
							}
						});
						final AlertDialog dialog = builder.create();
						dialog.show();
					}

					@Override
			        public boolean shouldOverrideUrlLoading(WebView view, String url) {
						return false;
			        }
					
					ProgressDialog progressBar = ProgressDialog.show(OnLineStatementActivity.this, null, res.getText(R.string.lb_loading));
					
					public   void   onPageStarted(WebView view, String url, Bitmap favicon){  
						progressBar.show();
					}
					
					public void onPageFinished(WebView view, String url) {
			            progressBar.dismiss();
						myWebView.setVisibility(View.VISIBLE);
					    btnClose.setVisibility(View.VISIBLE);
					}
		
				});
				
				
				myWebView.loadUrl(report);
				myWebView.getSettings().setBuiltInZoomControls(true);
				myWebView.getSettings().setSupportZoom(true); 
				myWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
				myWebView.getSettings().setUseWideViewPort(true);
				myWebView.setInitialScale(1);				
			}
		});
		
	}


	@Override
	public void handleByChild(Message msg) {}

	@Override
	public void loadLayout() {
		setContentView(R.layout.v_online_statement);
		
		if (btnClose == null)
			btnClose = (Button) findViewById(R.id.btnClose);
		
		popFrom = new PopupSingleDate(getApplicationContext(), findViewById(R.id.rlTop));
		popTo = new PopupSingleDate(getApplicationContext(), findViewById(R.id.rlTop));
		
		Calendar cal = Calendar.getInstance();
		if(app.dtTradeDate!=null)
			cal.setTime(app.dtTradeDate);
		
		if(CompanySettings.GET_LAST_TRADEDATE_STATEMENT)
			cal.add(Calendar.DATE, -1);
		
		((Button)findViewById(R.id.btnFrom)).setText(Utility.dateToString(cal.getTime()));
		((Button)findViewById(R.id.btnTo)).setText(Utility.dateToString(cal.getTime()));
	}

	@Override
	public void updateUI() {}
	
	
	@Override
	public boolean isBottonBarExist() {
		return true;
	}

	@Override
	public boolean isTopBarExist() {
		return true;
	}
	@Override
	public boolean showLogout() {
		return true;
	}
	
	@Override
	public boolean showTopNav() {
		return true;
	}

	@Override
	public boolean showConnected() {
		return true;
	}

	@Override
	public boolean showPlatformType() {
		return true;
	}
	
	@Override
	public int getServiceId() {
		return ServiceFunction.SRV_ON_LINE_STATEMENT;
	}			

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_ON_LINE_STATEMENT;
	}		
	
}