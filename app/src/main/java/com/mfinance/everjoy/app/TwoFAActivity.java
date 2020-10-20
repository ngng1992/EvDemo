package com.mfinance.everjoy.app;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mfinance.everjoy.R;

public class TwoFAActivity extends BaseActivity{

	TextView tvDevice;
	TextView tvOTP;
	EditText inOTP;
	Button btnResend;
	Button btnCancel;
	Button btnOK;

	private HashMap<String, String> hmTwoFA = new HashMap<String, String>();
	private Timer t = new Timer();

	@Override
	public void bindEvent() {
		btnCancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				TwoFAActivity.this.finish();
			}
		});

		btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if ("".equals(inOTP.getEditableText().toString()) ){
					Bundle bundle = new Bundle();
					bundle.putString(ServiceFunction.MESSAGE, (String)res.getText(R.string.msg_empty_otp));
					goTo(ServiceFunction.SRV_SHOW_TOAST,bundle);
					return;
				}

				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						if( dialog == null )
							dialog = ProgressDialog.show(TwoFAActivity.this, "", res.getString(R.string.please_wait), true);
					}
				});

				login(inOTP.getEditableText().toString());
			}
		});

		btnResend.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString(ServiceFunction.MESSAGE, (String)res.getText(R.string.msg_request_sent));
				goTo(ServiceFunction.SRV_SHOW_TOAST,bundle);

				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						if( dialog == null )
							dialog = ProgressDialog.show(TwoFAActivity.this, "", res.getString(R.string.please_wait), true);
					}
				});

				login("<--Resend-->");
			}
		});
	}


	@Override
	public void handleByChild(Message msg) {}

	@Override
	public void loadLayout() {
		setContentView(R.layout.v_two_fa);

		tvDevice = (TextView) findViewById(R.id.tvDevice);
		tvOTP = (TextView) findViewById(R.id.tvOTP);
		inOTP = (EditText) findViewById(R.id.inOTP);
		btnResend = (Button) findViewById(R.id.btnResend);
		btnCancel = (Button) findViewById(R.id.btnCancel2);
		btnOK = (Button) findViewById(R.id.btnSubmit);

	}

	private void updateCounter(final int sec){
		t.cancel();
		t.purge();
		t = new Timer();
		t.schedule(new TimerTask() {
			int second = sec;
			@Override
			public void run() {
				try {
					btnResend.setText(res.getText(R.string.two_fa_resend) + " (" + second-- + " s)");
					if (second < 0) {
						btnResend.setText(res.getText(R.string.two_fa_resend));
						btnResend.setTextColor(getResources().getColor(R.color.blue_text));
						btnResend.setEnabled(true);
						return;
					}
				}
				catch (Exception e){
					return;
				}
			}
		}, 0, 1000);
	}

	@Override
	public void updateUI() {
		hmTwoFA = app.data.getTwoFAMessage();
		if (hmTwoFA.get(Protocol.LoginResponse.TWO_FA).equals("1")){
			tvDevice.setText(res.getText(R.string.two_fa_mobile) + " XXXX-" + hmTwoFA.get(Protocol.LoginResponse.TWO_FA_MOBILE));
		}
		else {
			tvDevice.setText(res.getText(R.string.two_fa_mail) +" "+ hmTwoFA.get(Protocol.LoginResponse.TWO_FA_EMAIL));
		}

		tvOTP.setText(hmTwoFA.get(Protocol.LoginResponse.TWO_FA_PREFIX) + " - ");
		if (hmTwoFA.get(Protocol.LoginResponse.TWO_FA_EXPIRY).equals("0")){
			btnResend.setText(res.getText(R.string.two_fa_resend));
			btnResend.setTextColor(getResources().getColor(R.color.blue_text));
			btnResend.setEnabled(true);
		}
		else {
			btnResend.setTextColor(Color.GRAY);
			btnResend.setEnabled(false);

			if (app.data.twoFARefreshTimer) {
				btnResend.setText(res.getText(R.string.two_fa_resend) + " in " + hmTwoFA.get(Protocol.LoginResponse.TWO_FA_EXPIRY) + " sec");
				updateCounter(Integer.parseInt(hmTwoFA.get(Protocol.LoginResponse.TWO_FA_EXPIRY)));
			}
			else
				app.data.twoFARefreshTimer = true;
		}
		btnResend.setPaintFlags(btnResend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
	}

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
		return ServiceFunction.SRV_MOVE_TO_IDENTITY_CHECK;
	}

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_IDENTITY_CHECK;
	}

	public boolean isIdentityCheckActivity(){
		return true;
	}

	public void login(String otp){

		Message loginMsg = Message.obtain(null, ServiceFunction.SRV_LOGIN);
		loginMsg.replyTo = mServiceMessengerHandler;

		if (!otp.equals("<--Resend-->"))
			loginMsg.getData().putString(Protocol.LoginResponse.TWO_FA_OTP, tvOTP.getText().toString().replace(" ", "") + otp);

		loginMsg.getData().putString(ServiceFunction.LOGIN_PLATFORM_TYPE, hmTwoFA.get(ServiceFunction.LOGIN_PLATFORM_TYPE));
		loginMsg.getData().putString(ServiceFunction.LOGIN_ID, hmTwoFA.get(ServiceFunction.LOGIN_ID));
		loginMsg.getData().putString(ServiceFunction.LOGIN_PASSWORD, hmTwoFA.get(ServiceFunction.LOGIN_PASSWORD));
		loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, Integer.parseInt(hmTwoFA.get(ServiceFunction.LOGIN_CONN_INDEX)));

		try {
			mService.send(loginMsg);
		} catch (RemoteException e) {
			Log.e(TAG, "Unable to send login message", e.fillInStackTrace());
		}
	}

}