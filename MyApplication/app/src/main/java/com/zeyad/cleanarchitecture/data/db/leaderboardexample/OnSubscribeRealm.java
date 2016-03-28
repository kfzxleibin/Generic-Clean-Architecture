package com.zeyad.cleanarchitecture.data.db.leaderboardexample;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.exceptions.RealmException;
import rx.Observable;
import rx.Subscriber;

public abstract class OnSubscribeRealm<T extends RealmObject> implements Observable.OnSubscribe<T> {
    private Context context;
    private String fileName;

    public OnSubscribeRealm(Context context) {
        this(context, null);
    }

    public OnSubscribeRealm(Context context, String fileName) {
        this.context = context.getApplicationContext();
        this.fileName = fileName;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        final Realm realm = fileName != null ? Realm.getInstance(context) : Realm.getInstance(context);
        /*subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                try {
                    realm.close();
                } catch (RealmException ex) {
                    subscriber.onError(ex);
                }
            }
        }));*/

        T object;
        realm.beginTransaction();
        try {
            object = get(realm);
            realm.commitTransaction();
            if (object != null) {
                subscriber.onNext(object);
            }
            subscriber.onCompleted();
        } catch (RuntimeException e) {
            realm.cancelTransaction();
            e.printStackTrace();
            subscriber.onError(new RealmException("Error during transaction.", e));
        } catch (Error e) {
            realm.cancelTransaction();
            subscriber.onError(e);
        } finally {
            realm.close();
        }

    }

    public abstract T get(Realm realm);
}