package com.test.imageload.download;

public interface OnDownloadListener {

    void startDownload();

    void onDownloading(int progress);

    void onDownloadSuccess();

    void onDownloadFailed();

}
