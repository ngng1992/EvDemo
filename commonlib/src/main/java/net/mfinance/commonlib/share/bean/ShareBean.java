package net.mfinance.commonlib.share.bean;

import android.graphics.Bitmap;

public class ShareBean {

    private String title;
    private String description;
    private int localThumb;
    private String thumbUrl;
    private String url;
    /**
     * 单独设置，sina不超过32k
     */
    private Bitmap bitmap;

    public ShareBean() {
    }

    public ShareBean(String title, String description, int localThumb, String thumbUrl, String url) {
        this.title = title;
        this.description = description;
        this.localThumb = localThumb;
        this.thumbUrl = thumbUrl;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLocalThumb() {
        return localThumb;
    }

    public void setLocalThumb(int localThumb) {
        this.localThumb = localThumb;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
