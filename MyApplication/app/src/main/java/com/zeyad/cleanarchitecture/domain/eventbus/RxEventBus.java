package com.zeyad.cleanarchitecture.domain.eventbus;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Small wrapper on top of the EventBus to allow consumption of events as
 * Rx streams.
 *
 * @author Zeyad
 */
@Singleton
public class RxEventBus {
    private final Subject<Object, Object> rxBus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        rxBus.onNext(o);
    }

    @NonNull
    public Observable<Object> toObserverable() {
        return rxBus;
    }

    public boolean hasObservers() {
        return rxBus.hasObservers();
    }
}