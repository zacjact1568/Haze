package com.zack.enderweather.interactor.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
