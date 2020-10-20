package com.mfinance.everjoy.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.NewsSummary;
import com.mfinance.everjoy.app.util.CommonFunction;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.mfinance.everjoy.app.constant.ServiceFunction.SRV_ANNOUNCEMENT;

public class AnnouncementActivity extends BaseActivity {
    private List<NewsSummary> newsSummaries = Collections.EMPTY_LIST;
    private List<NewsSummary> newsSummariesRaw = Collections.EMPTY_LIST;
    private Runnable updateInternalUI;

    public class ViewHolder {
        TextView tvDateTime;
        TextView tvTitle;
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void loadLayout() {
        setContentView(R.layout.v_announcement);
        ListView listViewAnnouncement = findViewById(R.id.listViewAnnouncement);
        Drawable backgroundOdd = getDrawable(R.drawable.list_row_odd);
        Drawable backgroundEven = getDrawable(R.drawable.list_row_even);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return newsSummaries.size();
            }

            @Override
            public Object getItem(int position) {
                return newsSummaries.get(position);
            }

            @Override
            public long getItemId(int position) {
                return newsSummaries.get(position).getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v;
                ViewHolder vh;
                if (convertView == null) {
                    v = getLayoutInflater().inflate(R.layout.list_item_announcement, null, true);
                    vh = new ViewHolder();
                    vh.tvDateTime = v.findViewById(R.id.tvDateTime);
                    vh.tvTitle = v.findViewById(R.id.tvTitle);
                    if (position % 2 > 0) {
                        v.setBackground(backgroundOdd);
                    } else {
                        v.setBackground(backgroundEven);
                    }
                    v.setTag(vh);
                } else {
                    v = convertView;
                    vh = (ViewHolder) v.getTag();
                }
                NewsSummary newsSummary = newsSummaries.get(position);
                if (newsSummary == null) {
                    return v;
                }
                String title;
                String langQueryString;
                Locale locale = getLanguage();
                if (locale.equals(Locale.TRADITIONAL_CHINESE)) {
                    title = newsSummary.getTitleTraditionalChinese();
                    langQueryString = "tw";
                } else if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
                    title = newsSummary.getTitleSimplifiedChinese();
                    langQueryString = "cn";
                } else {
                    title = newsSummary.getTitleEnglish();
                    langQueryString = "";
                }
                vh.tvTitle.setText(title);
                vh.tvDateTime.setText(newsSummary.getDateTime().format(dateTimeFormatter));
                v.setOnClickListener(v1 -> {
                    CommonFunction cf = new CommonFunction(false);
                    cf.setKey(CompanySettings.HTTP_KEY);
                    Uri.Builder builder = new Uri.Builder();
                    builder.appendQueryParameter("newsid", newsSummary.getId().toString());
                    builder.appendQueryParameter("lang", langQueryString);
                    Intent intent = new Intent(AnnouncementActivity.this, WebViewActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("content", CompanySettings.m_strWebLink + "/getNews.asp?" + cf.encryptText(builder.toString().substring(1)));
                    intent.putExtra(ServiceFunction.REQUIRE_LOGIN, false);
                    startActivity(intent);
                });
                return v;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public int getItemViewType(int position) {
                return position % 2;
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }
        };
        listViewAnnouncement.setAdapter(adapter);
        updateInternalUI = adapter::notifyDataSetChanged;
        newsSummariesRaw = app.data.getNewsSummaries();
        newsSummaries = newsSummariesRaw.stream()
                .filter(s -> s.getNewsType() == NewsSummary.NewsType.SYSTEM_MESSAGE)
                .filter(s -> s.getReportGroups().size() != 0 && s.getReportGroups().contains(app.data.getBalanceRecord().strGroup))
                .collect(Collectors.toList());
        updateInternalUI.run();
    }

    @Override
    public void updateUI() {
        List<NewsSummary> old = newsSummariesRaw;
        newsSummariesRaw = app.data.getNewsSummaries();
        if (newsSummariesRaw != old) {
            newsSummaries = newsSummariesRaw.stream()
                    .filter(s -> s.getNewsType() == NewsSummary.NewsType.SYSTEM_MESSAGE)
                    .filter(s -> s.getReportGroups().size() != 0 && s.getReportGroups().contains(app.data.getBalanceRecord().strGroup))
                    .collect(Collectors.toList());
            updateInternalUI.run();
        }
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
        return SRV_ANNOUNCEMENT;
    }

    @Override
    public int getActivityServiceCode() {
        return SRV_ANNOUNCEMENT;
    }
}
