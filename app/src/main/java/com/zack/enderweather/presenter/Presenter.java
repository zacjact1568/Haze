package com.zack.enderweather.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
