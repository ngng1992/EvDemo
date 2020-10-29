package com.mfinance.everjoy.everjoy.ui.mine.securities.recordvideo;

public interface RecordCallBack {

    void start();

    void progress(long millis);

    void finish();

    void filePath(String path);


}
