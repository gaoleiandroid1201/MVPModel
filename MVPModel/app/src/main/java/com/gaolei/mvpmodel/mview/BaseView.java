package com.gaolei.mvpmodel.mview;

public interface BaseView {

    void showLoading();
    void hideLoading();
    void showErrorMsg(String errorMsg);
}