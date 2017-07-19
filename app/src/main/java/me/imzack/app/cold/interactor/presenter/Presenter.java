package me.imzack.app.cold.interactor.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
