package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.BankAccount;
import com.mfinance.everjoy.app.pojo.CashMovementRequest;
import com.mfinance.everjoy.app.pojo.CashMovementRequestBuilder;
import com.mfinance.everjoy.app.service.internal.CashMovementRequestProcessor;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.DecimalDigitsInputFilter;

import org.joda.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CashMovementActivity extends BaseActivity {
    private Spinner bankAccountSpinner;
    private BaseAdapter bankAccountSpinnerAdapter;
    private List<BankAccount> bankList = Collections.EMPTY_LIST;
    private Map<String, String> termsMap = Collections.EMPTY_MAP;
    private BankAccount selectedBackAccount = null;
    private TextView tvCashMovementTerms;
    private boolean waitRequest = false;
    private CashMovementRequest request;

    @Override
    public void loadLayout() {
        Bundle extras = getIntent().getExtras();
        final boolean isDeposit = extras.getInt("isDeposit", 0) > 0;
        setContentView(R.layout.v_cashmovement);

        EditText txtAmount = findViewById(R.id.btnAmount);
        txtAmount.setFilters(new InputFilter[]{
                new DecimalDigitsInputFilter(2)
        });
        bankAccountSpinner = findViewById(R.id.sBank);
        TextView tvToday = findViewById(R.id.tvToday);
        TextView tbAccountCashMove = findViewById(R.id.tbAccountCashMove);
        tbAccountCashMove.setText(app.data.getBalanceRecord().strAccount);
        TextView tvBankAccount = findViewById(R.id.tvBankAccount);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(isDeposit ? R.string.nav_deposit : R.string.nav_withdrawal);
        TextView tvDateLabel = findViewById(R.id.tvDateLabel);
        tvDateLabel.setText(isDeposit ? R.string.lb_cashmovement_request_date_deposit_label : R.string.lb_cashmovement_request_date_withdrawal_label);
        LocalDateTime localDateTime = LocalDateTime.now();
        tvToday.setText(localDateTime.toString("yyyy-MM-dd"));
        bankAccountSpinnerAdapter = new BaseAdapter() {
            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public int getCount() {
                return bankList.size();
            }

            @Override
            public Object getItem(int position) {
                return bankList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return bankList.get(position).hashCode();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView;
                if (convertView == null) {
                    textView = (TextView) View.inflate(getBaseContext(), R.layout.list_item_bankaccount, null);
                    textView.setPadding(0, 0, 0, 0);
                } else {
                    textView = (TextView) convertView;
                }
                BankAccount bankAccount = bankList.get(position);
                textView.setText(bankAccount.bankName);
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView;
                if (convertView == null) {
                    textView = (TextView) View.inflate(getBaseContext(), R.layout.list_item_bankaccount, null);
                } else {
                    textView = (TextView) convertView;
                }
                BankAccount bankAccount = bankList.get(position);
                textView.setText(bankAccount.bankName + "\n" + bankAccount.number);
                return textView;
            }
        };
        bankAccountSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                selectedBackAccount = bankList.get(position);
                if (selectedBackAccount != null) {
                    tvBankAccount.setText(selectedBackAccount.number);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSubmit.setOnClickListener(v -> {
            final String amount = txtAmount.getEditableText().toString();
            if (amount.length() == 0) {
                Toast.makeText(CashMovementActivity.this, res.getString(R.string.lb_margin_error), Toast.LENGTH_LONG).show();
                return;
            }
            if (Double.parseDouble(amount) <= 0) {
                Toast.makeText(CashMovementActivity.this, res.getString(R.string.lb_margin_amount_error), Toast.LENGTH_LONG).show();
                return;
            }

            final String bankacc = isDeposit ? "" : Optional.ofNullable(selectedBackAccount).map(b -> String.valueOf(bankList.indexOf(b) + 1)).orElse("");
            AlertDialog dialog = new AlertDialog.Builder(CashMovementActivity.this, CompanySettings.alertDialogTheme).create();
            dialog.setMessage(isDeposit ? res.getString(R.string.confirm_in) : res.getString(R.string.confirm_out));

            DialogInterface.OnClickListener onClickListener;
            if (isDeposit) {
                onClickListener = (dialog1, which) -> {
                    CommonFunction.sendCashMovementRequest(mService, mServiceMessengerHandler, amount, CashMovementRequestProcessor.RequestType.DEPOSIT, bankacc);
                    CashMovementRequestBuilder builder = new CashMovementRequestBuilder();
                    app.data.setCashMovementRequest(
                            builder.setBankacc(bankacc).setAmount(amount).setPending(true).setDeposit(isDeposit).createCashMovementRequest()
                    );
                    waitRequest = true;
                };
            } else {
                onClickListener = (dialog1, which) -> {
                    CommonFunction.sendCashMovementRequest(mService, mServiceMessengerHandler, amount, CashMovementRequestProcessor.RequestType.WITHDRAWAL, bankacc);
                    Toast.makeText(CashMovementActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
                    CashMovementActivity.this.finish();
                };
            }
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.btn_confirm), onClickListener);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.btn_cancel),
                    (dialog1, which) -> {
                    }
            );
            dialog.show();

        });
        bankAccountSpinner.setAdapter(bankAccountSpinnerAdapter);

        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
        tvCashMovementTerms = findViewById(R.id.tvCashMovementTerms);
        termsMap = isDeposit ? app.data.getDepositTermsMap() : app.data.getWithdrawalTermsMap();
        switch (app.locale.toString()) {
            case "zh_TW":
            case "zh_CN":
                tvCashMovementTerms.setText(termsMap.getOrDefault(app.locale.toString(), ""));
                break;
            default:
                tvCashMovementTerms.setText(termsMap.getOrDefault("en", ""));
                break;
        }
        View vwAccountCashMove = findViewById(R.id.vwAccountCashMove);
        View vwBank = findViewById(R.id.vwBank);
        View vwBankAcc = findViewById(R.id.vwBankAcc);
        vwAccountCashMove.setVisibility(isDeposit ? View.VISIBLE : View.GONE);
        vwBank.setVisibility(isDeposit ? View.GONE : View.VISIBLE);
        vwBankAcc.setVisibility(isDeposit ? View.GONE : View.VISIBLE);
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {

    }


    @Override
    public void updateUI() {
        List<BankAccount> oldBankList = this.bankList;
        this.bankList = app.data.getBankList();
        if (oldBankList != bankList) {
            bankAccountSpinnerAdapter.notifyDataSetChanged();
        }
        Bundle extras = getIntent().getExtras();
        final boolean isDeposit = extras.getInt("isDeposit", 0) > 0;
        Map<String, String> oldTermsMap = termsMap;
        termsMap = isDeposit ? app.data.getDepositTermsMap() : app.data.getWithdrawalTermsMap();
        if (oldTermsMap != termsMap) {
            switch (app.locale.toString()) {
                case "zh_TW":
                case "zh_CN":
                    tvCashMovementTerms.setText(termsMap.getOrDefault(app.locale.toString(), ""));
                    break;
                default:
                    tvCashMovementTerms.setText(termsMap.getOrDefault("en", ""));
                    break;
            }
        }
        if (waitRequest) {
            CashMovementRequest oldRequest = request;
            request = app.data.getCashMovementRequest();
            if (oldRequest != request) {
                if (!request.isPending()) {
                    waitRequest = false;
                    if (request.isSuccess()) {
                        if (request.isSendURL()) {
                            if (request.isDeposit()) {
                                Intent intent = new Intent(this, WebViewActivity.class);
                                intent.putExtra("title", getResources().getText(R.string.nav_deposit));
                                intent.putExtra("content", request.getUrl());
                                startActivity(intent);
                            }
                        } else {
                            this.finish();
                        }
                    }
                }
            }

        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        CommonFunction.sendBankInfoRequest(mService, mServiceMessengerHandler);
        CommonFunction.sendCompanyInfoRequest(mService, mServiceMessengerHandler);
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
        return ServiceFunction.SRV_CASH_MOVEMENT;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_CASH_MOVEMENT;
    }
}