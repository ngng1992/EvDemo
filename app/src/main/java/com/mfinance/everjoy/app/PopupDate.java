package com.mfinance.everjoy.app;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.OnWheelChangedListener;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.app.widget.wheel.adapter.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class PopupDate {
    Context context;
    public CustomPopupWindow popup;
    public WheelView[] wvDate = new WheelView[2];
    public WheelView wvDay;
    Button btnFrom;
    Button btnTo;
    String[] years = new String[21];
    String[] months = new String[12];
    ArrayList<String> alDay = new ArrayList<String>();
    Button btnSelected = null;
    public Button btnCommit;
    public Button btnClose;

    public PopupDate(Context context, View vParent) {
        this.context = context;
        popup = new CustomPopupWindow(vParent);
        popup.setContentView(R.layout.popup_date);

        wvDate[0] = (WheelView) popup.findViewById(R.id.p1);
        wvDate[1] = (WheelView) popup.findViewById(R.id.p2);
        wvDay = (WheelView) popup.findViewById(R.id.p3);
        btnFrom = (Button) popup.findViewById(R.id.btnFrom);
        btnTo = (Button) popup.findViewById(R.id.btnTo);

        int iYear = Calendar.getInstance().getTime().getYear() + 1900;

        for (int i = 0; i <= 10; i++) {
            years[i] = String.valueOf(iYear - 10 + i);
        }

        for (int i = 11; i <= 20; i++) {
            years[i] = String.valueOf(iYear - 10 + i);
        }

        for (int i = 0; i < 12; i++) {
            months[i] = String.format("%02d", i + 1);
        }

        updateDateWheel(wvDate, new String[][]{years, months});
        updateDays(wvDate[0], wvDate[1], wvDay);

        btnCommit = (Button) popup.findViewById(R.id.btnOK);
        btnClose = (Button) popup.findViewById(R.id.btnClose);

        bindEvent();
    }

    public void bindEvent() {
        wvDate[0].addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays();
                if (btnSelected != null) {
                    String sValue = getDate();
                    if (sValue != null)
                        btnSelected.setText(sValue);
                }
            }
        });

        wvDate[1].addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays();
                if (btnSelected != null) {
                    String sValue = getDate();
                    if (sValue != null)
                        btnSelected.setText(sValue);
                }
            }
        });

        wvDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(wvDate[0], wvDate[1], wvDay);
                if (btnSelected != null) {
                    String sValue = getDate();
                    if (sValue != null)
                        btnSelected.setText(sValue);
                }
            }
        });

        btnFrom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected = (Button) v;
                setCurrDate((btnSelected.getText().toString()).split("-"));
            }

        });

        btnTo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected = (Button) v;
                setCurrDate((btnSelected.getText().toString()).split("-"));
            }
        });
    }

    public void showLikeQuickAction() {
        popup.showLikeQuickAction();
        btnSelected = btnFrom;
    }

    public void dismiss() {
        popup.dismiss();
    }

    public void setFromToValue(String sFrom, String sTo) {
        btnFrom.setText(sFrom);
        btnTo.setText(sTo);
    }

    /*
        public String getValue(){
            StringBuilder sb = new StringBuilder();

            if(wvLot[0].getCurrentItem() != 0)
                sb.append(GUIUtility.LOT.get(0).get(wvLot[0].getCurrentItem()));

            sb.append(GUIUtility.LOT.get(1).get(wvLot[1].getCurrentItem())).append(GUIUtility.LOT.get(2).get(wvLot[2].getCurrentItem()));

            return sb.toString();
        }
    */
    public void updateDateWheel(WheelView[] wvDate, String[][] values) {
        for (int i = 0; i < wvDate.length; i++) {
            WheelView wv = wvDate[i];
            if (wv.getViewAdapter() == null) {
                wv.setViewAdapter(new ArrayWheelAdapter<String>(context, values[i]));
            } else {
                ((ArrayWheelAdapter) wv.getViewAdapter()).updateItems(values[i]);
            }
        }
    }

    public String getDate() {
        try {
            return years[wvDate[0].getCurrentItem()] + "-" + months[wvDate[1].getCurrentItem()] + "-" + alDay.get(wvDay.getCurrentItem());
        } catch (Exception e) {
            return null;
        }
    }

    public void setCurrDate(String[] sValue) {
        int iYear = -1;
        int iMonth = -1;
        int iDay = -1;

        for (int i = 0; i < years.length; i++) {
            if (sValue[0].equals(years[i])) {
                iYear = i;
                break;
            }
        }

        for (int i = 0; i < months.length; i++) {
            if (sValue[1].equals(months[i])) {
                iMonth = i;
                break;
            }
        }

        iDay = alDay.indexOf(String.valueOf(Integer.valueOf(sValue[2])));

        wvDate[0].setCurrentItem(iYear, true);
        wvDate[1].setCurrentItem(iMonth, true);
        wvDay.setCurrentItem(iDay, true);
    }

    public void updateDays() {
        updateDays(wvDate[0], wvDate[1], wvDay);
    }

    void updateDays(WheelView year, WheelView month, WheelView day) {
        int maxDays = Utility.getLastDayOfMonth(Integer.valueOf(years[wvDate[0].getCurrentItem()]), Integer.valueOf(months[wvDate[1].getCurrentItem()]) - 1);

        updateDayList(maxDays);

        if (day.getViewAdapter() == null)
            day.setViewAdapter(new ArrayWheelAdapter<String>(context, alDay.toArray(new String[alDay.size()])));
        else
            ((ArrayWheelAdapter) day.getViewAdapter()).updateItems(alDay.toArray(new String[alDay.size()]));

        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    public void updateDayList(int iMax) {
        alDay.clear();
        for (int i = 1; i <= iMax; i++) {
            alDay.add(String.valueOf(i));
        }
    }

    public String[] getValue() {
        return new String[]{btnFrom.getText().toString(), btnTo.getText().toString()};
    }
}
