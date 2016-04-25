package com.zeyad.cleanarchitecture.data.db.generalize;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.zeyad.cleanarchitecture.data.entities.UserRealmModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * {@link GeneralRealmManager} implementation.
 */
@Singleton
public class GeneralRealmManagerImpl implements GeneralRealmManager {

    public static final String TAG = "GeneralRealmManagerImpl",
            SETTINGS_FILE_NAME = "com.zeyad.cleanarchitecture.SETTINGS",
            COLLECTION_SETTINGS_KEY_LAST_CACHE_UPDATE = "collection_last_cache_update",
            DETAIL_SETTINGS_KEY_LAST_CACHE_UPDATE = "detail_last_cache_update";
    private static final long EXPIRATION_TIME = 600000;
    private Realm mRealm;
    private Context mContext;

    @Inject
    public GeneralRealmManagerImpl(Context mContext) {
        mRealm = Realm.getDefaultInstance();
        this.mContext = mContext;
    }

    @Override
    public Observable<?> getById(final int itemId, Class clazz) {
        return Observable.defer(() ->
                Observable.just(Realm.getDefaultInstance()
                        .where(clazz).equalTo("userId", itemId).findFirst()));
    }

    @Override
    public Observable<List> getAll(Class clazz) {
        return Observable.defer(() -> Observable.from(Collections
                .singletonList(Realm.getDefaultInstance()
                        .where(clazz)
                        .findAll())));
    }

    @Override
    public Observable<List> getWhere(Class clazz, String query, String filterKey) {
        return Observable.defer(() -> Observable.from(Collections
                .singletonList(Realm.getDefaultInstance()
                        .where(clazz)
                        .beginsWith(filterKey, query, Case.INSENSITIVE)
                        .findAll())));
    }

    @Override
    public Observable<?> put(RealmObject realmObject) {
        if (realmObject != null) {
            return Observable.defer(() -> {
                mRealm = Realm.getDefaultInstance();
                mRealm.beginTransaction();
                Observable observable = Observable.just(mRealm.copyToRealmOrUpdate(realmObject));
                mRealm.commitTransaction();
                writeToPreferences(System.currentTimeMillis(), DETAIL_SETTINGS_KEY_LAST_CACHE_UPDATE);
                return observable;
            });
        }
        return Observable.empty();
    }

    @Override
    public void putAll(List<RealmObject> realmModels) {
        Observable.defer(() -> {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(realmModels);
            mRealm.commitTransaction();
            writeToPreferences(System.currentTimeMillis(), COLLECTION_SETTINGS_KEY_LAST_CACHE_UPDATE);
            return Observable.from(realmModels);
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "all " + realmModels.getClass().getName() + "s added!");
                    }
                });
    }

    @Override
    public boolean isCached(int itemId, Class clazz) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        UserRealmModel realmObject = mRealm.where(UserRealmModel.class).equalTo("userId", itemId).findFirst();
        boolean isCached = realmObject != null;
        isCached = isCached && realmObject.getDescription() != null;
        mRealm.commitTransaction();
        mRealm.close();
        return isCached;
    }

    @Override
    public boolean isItemValid(int itemId, Class clazz) {
        return isCached(itemId, clazz) && areItemsValid(DETAIL_SETTINGS_KEY_LAST_CACHE_UPDATE);
    }

    @Override
    public boolean areItemsValid(String destination) {
        return (System.currentTimeMillis() - getFromPreferences(destination)) <= EXPIRATION_TIME;
    }

    @Override
    public void evictAll(Class clazz) {
        Observable.defer(() -> {
            mRealm = Realm.getDefaultInstance();
            RealmResults results = mRealm.where(clazz).findAll();
            mRealm.beginTransaction();
            results.deleteAllFromRealm();
            mRealm.commitTransaction();
            return Observable.just(results.isValid());
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "all " + clazz.getSimpleName() + "s deleted!");
                    }
                });
    }


    @Override
    public void evict(final RealmObject realmModel, Class clazz) {
        Observable.defer(() -> {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            realmModel.deleteFromRealm();
            mRealm.commitTransaction();
            return Observable.just(realmModel.isValid());
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, clazz.getSimpleName() + " deleted!");
                    }
                });
    }

    @Override
    public boolean evictById(final int itemId, Class clazz) {
        mRealm = Realm.getDefaultInstance();
        RealmModel toDelete = mRealm.where(clazz).equalTo("userId", itemId).findFirst();
        if (toDelete != null) {
            mRealm.beginTransaction();
            RealmObject.deleteFromRealm(toDelete);
            mRealm.commitTransaction();
            mRealm.close();
            return RealmObject.isValid(toDelete);
        } else return true;
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<Object>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//                        Log.d(TAG, clazz.getSimpleName() + " deleted!");
//                    }
//                });
    }

    @Override
    public Observable<?> evictCollection(Collection<Integer> collection, Class dataClass) {
        return Observable.defer(() -> {
            boolean isDeleted = true;
            for (int i = 0; i < collection.size(); i++)
                isDeleted = isDeleted && !evictById(collection.iterator().next(), dataClass);
            return Observable.just(isDeleted);
        });
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * Write a value to a user preferences file.
     *
     * @param value A long representing the value to be inserted.
     */
    private void writeToPreferences(long value, String destination) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(SETTINGS_FILE_NAME,
                Context.MODE_PRIVATE).edit();
        editor.putLong(destination, value);
        editor.apply();
        Log.d(TAG, "writeToPreferencesTo " + destination + ": " + value);
    }

    /**
     * Get a value from a user preferences file.
     *
     * @return A long representing the value retrieved from the preferences file.
     */
    private long getFromPreferences(String destination) {
        return mContext.getSharedPreferences(SETTINGS_FILE_NAME, Context.MODE_PRIVATE)
                .getLong(destination, 0);
    }

    public Realm getRealm() {
        return mRealm;
    }
}