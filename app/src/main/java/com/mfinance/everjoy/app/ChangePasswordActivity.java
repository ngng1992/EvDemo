package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Message;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.ChangePasswordRequest;
import com.mfinance.everjoy.app.pojo.PasswordControl;
import com.mfinance.everjoy.app.util.MessageMapping;

import java.util.concurrent.atomic.AtomicReference;

public class ChangePasswordActivity extends BaseActivity {
    public enum ValidateResult {
        VALID, INVALID_LENGTH, INVALID_COMPLEXITY, INVALID_CONFIRM_PASSWORD
    }
    public class PasswordValidator {
        public ValidateResult validate(PasswordControl passwordControl, String password, String newPassword) {
            if (password.length() > passwordControl.getMaxLength() || password.length() < passwordControl.getMinLength()) {
                return ValidateResult.INVALID_LENGTH;
            }
            int digitChar = 0;
            int lowerCaseChar = 0;
            int upperCaseChar = 0;
            int symbolChar = 0;
            for (int i = 0, len = password.length(); i < len; i++) {
                if (Character.isDigit(password.charAt(i))) {
                    digitChar++;
                }
                if (Character.isLowerCase(password.charAt(i))) {
                    lowerCaseChar++;
                }
                if (Character.isUpperCase(password.charAt(i))) {
                    upperCaseChar++;
                }
                if (passwordControl.getSymbolList().indexOf(password.charAt(i)) > 0) {
                    symbolChar++;
                }
            }
            if (digitChar < passwordControl.getMinDigitLetter() ||
                    lowerCaseChar < passwordControl.getMinLowercaseLetter() ||
                    upperCaseChar < passwordControl.getMinUppercaseLetter() ||
                    symbolChar < passwordControl.getMinSymbolLetter()) {
                return ValidateResult.INVALID_COMPLEXITY;
            }
            if (!password.equals(newPassword)) {
                return ValidateResult.INVALID_CONFIRM_PASSWORD;
            }
            return ValidateResult.VALID;
        }

    }
    private PasswordValidator passwordValidator = new PasswordValidator();
    private volatile String currentPassword = "";
    private volatile String newPassword = "";
    private volatile String newPasswordRepeat = "";
    private volatile ValidateResult validateResult = ValidateResult.VALID;
    private AtomicReference<ValidateResult> lastValidateResultRef = new AtomicReference<ValidateResult>(ValidateResult.VALID);
    private AtomicReference<ChangePasswordRequest> lastChangeRequestRef = new AtomicReference<ChangePasswordRequest>();
    private AtomicReference<PasswordControl> passwordControl = new AtomicReference<PasswordControl>();
    @Override
    public void bindEvent() {
        EditText etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etCurrentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentPassword = s.toString();
                updateUI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        EditText etNewPassword = findViewById(R.id.etNewPassword);
        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPassword = s.toString();
                validateResult = passwordValidator.validate(passwordControl.get(), newPassword, newPasswordRepeat);
                updateUI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        EditText etNewPasswordRepeat = findViewById(R.id.etNewPasswordRepeat);
        etNewPasswordRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPasswordRepeat = s.toString();
                validateResult = passwordValidator.validate(passwordControl.get(), newPassword, newPasswordRepeat);
                updateUI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                switch (lastValidateResultRef.get()) {
                    case INVALID_LENGTH:
                        showAlertMessageDialog(MessageMapping.getMessageByCode(app.getResources(), "402", app.locale));
                        return;
                    case INVALID_COMPLEXITY:
                        showAlertMessageDialog(MessageMapping.getMessageByCode(app.getResources(), "411", app.locale));
                        return;
                    case INVALID_CONFIRM_PASSWORD:;
                        showAlertMessageDialog(MessageMapping.getMessageByCode(app.getResources(), "444", app.locale));
                        return;
                    default:
                        break;
                }
                Message loginMsg = Message.obtain(null, ServiceFunction.SRV_SEND_CHANGE_PASSWORD_REQUEST);
                loginMsg.replyTo = mServiceMessengerHandler;
                loginMsg.getData().putString(Protocol.ChangePassword.OLD_PASSWORD, currentPassword);
                loginMsg.getData().putString(Protocol.ChangePassword.NEW_PASSWORD, newPassword);

                try {
                    mService.send(loginMsg);
                } catch (RemoteException e) {
                    Log.e(TAG, "Unable to send login message", e.fillInStackTrace());
                }
            }
        });

        Button btnCancel2 = findViewById(R.id.btnCancel2);
        btnCancel2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (app.bLogon) {
                    goTo(ServiceFunction.SRV_LOGOUT);
                } else {
                    finish();
                }
            }
        });
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
        return true;
    }

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_change_password);
    }

    @Override
    public void updateUI() {
        ChangePasswordRequest changePasswordRequest = app.data.getChangePasswordRequest();
        ChangePasswordRequest lastChangePasswordRequest = lastChangeRequestRef.getAndSet(changePasswordRequest);
        PasswordControl passwordControl = app.data.getPasswordControl();
        PasswordControl lastPasswordControl = this.passwordControl.getAndSet(passwordControl);
        ValidateResult validateResult = this.validateResult;
        ValidateResult lastValidateResult = lastValidateResultRef.getAndSet(validateResult);

        if (changePasswordRequest != lastChangePasswordRequest ||
                validateResult != lastValidateResult ||
                passwordControl != lastPasswordControl) {
            if (passwordControl != lastPasswordControl) {
                TextView textViewPasswordControl = findViewById(R.id.tvPasswordControl);
                StringBuilder stringBuilder = new StringBuilder();
                Resources resources = app.getResources();
                stringBuilder.append(
                        resources.getText(R.string.lb_password_criteria_length).toString()
                                .replace("#s1", String.valueOf(passwordControl.getMinLength()))
                                .replace("#s2", String.valueOf(passwordControl.getMaxLength()))
                );
                if (passwordControl.getMinDigitLetter() > 0 ||
                        passwordControl.getMinLowercaseLetter() > 0 ||
                        passwordControl.getMinUppercaseLetter() > 0 ||
                        passwordControl.getMinSymbolLetter() > 0) {
                    stringBuilder.append("\n").append(resources.getText(R.string.lb_password_criteria_optional));
                }
                if (passwordControl.getMinDigitLetter() > 0) {
                    stringBuilder.append("\n").append(
                            resources.getText(R.string.lb_password_criteria_optional_min_digit).toString()
                                    .replace("#s", String.valueOf(passwordControl.getMinDigitLetter()))
                    );
                }
                if (passwordControl.getMinLowercaseLetter() > 0) {
                    stringBuilder.append("\n").append(
                            resources.getText(R.string.lb_password_criteria_optional_min_lowercase).toString()
                                    .replace("#s", String.valueOf(passwordControl.getMinLowercaseLetter()))
                    );
                }
                if (passwordControl.getMinUppercaseLetter() > 0) {
                    stringBuilder.append("\n").append(
                            resources.getText(R.string.lb_password_criteria_optional_min_uppercase).toString()
                                    .replace("#s", String.valueOf(passwordControl.getMinUppercaseLetter()))
                    );
                }
                if (passwordControl.getMinSymbolLetter() > 0) {
                    stringBuilder.append("\n").append(
                            resources.getText(R.string.lb_password_criteria_optional_min_symbol).toString()
                                    .replace("#s1", String.valueOf(passwordControl.getMinSymbolLetter()))
                                    .replace("#s2", String.valueOf(passwordControl.getSymbolList()))
                    );
                }
                textViewPasswordControl.setText(stringBuilder.toString());

                TextView textViewPasswordControlAlert = findViewById(R.id.tvPasswordControlAlert);
                if (passwordControl.isFirstLogin()) {
                    textViewPasswordControlAlert.setText(MessageMapping.getMessageByCode(app.getResources(), "412", app.locale));
                    textViewPasswordControlAlert.setVisibility(View.VISIBLE);
                } else if (passwordControl.getAge() > passwordControl.getMaxPasswordAge()) {
                    textViewPasswordControlAlert.setText(
                            MessageMapping.getMessageByCode(app.getResources(), "413", app.locale)
                                    .replace("#s", String.valueOf(passwordControl.getAge()))
                    );
                    textViewPasswordControlAlert.setVisibility(View.VISIBLE);
                } else {
                    textViewPasswordControlAlert.setVisibility(View.INVISIBLE);
                }
            }
            Button btnSubmit = findViewById(R.id.btnSubmit);
            if (changePasswordRequest.isPending()) {
                btnSubmit.setEnabled(false);
            } else {
                btnSubmit.setEnabled(true);
            }
            if (changePasswordRequest.isSuccessful()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this, CompanySettings.alertDialogTheme);
                StringBuilder stringBuilder = new StringBuilder(MessageMapping.getMessageByCode(app.getResources(), "400", app.locale));
                stringBuilder.append("\n").append(MessageMapping.getMessageByCode(app.getResources(), "404", app.locale));
                builder.setMessage(stringBuilder.toString())
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (app.bLogon) {
                                    goTo(ServiceFunction.SRV_LOGOUT);
                                } else {
                                    finish();
                                }
                            }
                        })
                        .create()
                        .show();
            }
        }
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
        return ServiceFunction.SRV_CHANGE_PASSWORD;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_CHANGE_PASSWORD;
    }

    @Override
    public void onBackPressed() {
        if (app.bLogon) {
            goTo(ServiceFunction.SRV_LOGOUT);
        } else {
            finish();
        }
    }

    private void showAlertMessageDialog(CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this, CompanySettings.alertDialogTheme);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }
}
