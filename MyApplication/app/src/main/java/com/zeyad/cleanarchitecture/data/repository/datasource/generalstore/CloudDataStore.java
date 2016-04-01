package com.zeyad.cleanarchitecture.data.repository.datasource.generalstore;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.gson.Gson;
import com.zeyad.cleanarchitecture.data.db.RealmManager;
import com.zeyad.cleanarchitecture.data.db.generalize.GeneralRealmManager;
import com.zeyad.cleanarchitecture.data.entities.mapper.EntityDataMapper;
import com.zeyad.cleanarchitecture.data.network.RestApi;
import com.zeyad.cleanarchitecture.data.repository.datasource.userstore.UserDataStore;
import com.zeyad.cleanarchitecture.domain.services.ImageDownloadGcmService;
import com.zeyad.cleanarchitecture.domain.services.ImageDownloadIntentService;
import com.zeyad.cleanarchitecture.utilities.Utils;

import java.util.ArrayList;
import java.util.Collection;

import io.realm.RealmObject;
import rx.Observable;
import rx.functions.Action1;

public class CloudDataStore implements DataStore {

    private final RestApi restApi;
    private GeneralRealmManager realmManager;
    private EntityDataMapper entityDataMapper;
    private static final String TAG = "CloudDataStore", POST_TAG = "postObject", DELETE_TAG = "delete",
            DELETE_BY_ID_TAG = "deleteById";
    private Class dataClass;
    private final Action1<Object> saveGenericToCacheAction = object -> realmManager.put((RealmObject) entityDataMapper.transformToRealm(object, dataClass));
    private final Action1<Collection> saveAllGenericsToCacheAction = collection -> {
        Collection<RealmObject> realmObjectCollection = new ArrayList<>();
        realmObjectCollection.addAll((Collection) entityDataMapper.transformAllToRealm(collection, dataClass));
        realmManager.putAll(realmObjectCollection);
    };
    private Action1<Object> queuePost = object -> {
        if (Utils.hasLollipop()) {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(realmManager.getContext())
                    == ConnectionResult.SUCCESS) {
                Bundle extras = new Bundle();
                extras.putString(ImageDownloadIntentService.POST_OBJECT, new Gson().toJson(object));
                GcmNetworkManager.getInstance(realmManager.getContext()).schedule(new OneoffTask.Builder()
                        .setService(ImageDownloadGcmService.class)
                        .setRequiredNetwork(OneoffTask.NETWORK_STATE_CONNECTED)
                        .setExtras(extras)
                        .setTag(POST_TAG)
                        .build()); // gcm service
            }
        }
    };
    private final Action1<Integer> queueDeleteById = integer -> {
        if (Utils.hasLollipop()) {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(realmManager.getContext())
                    == ConnectionResult.SUCCESS) {
                Bundle extras = new Bundle();
                extras.putInt(ImageDownloadIntentService.DELETE_OBJECT, integer);
                GcmNetworkManager.getInstance(realmManager.getContext()).schedule(new OneoffTask.Builder()
                        .setService(ImageDownloadGcmService.class)
                        .setRequiredNetwork(OneoffTask.NETWORK_STATE_CONNECTED)
                        .setExtras(extras)
                        .setTag(DELETE_BY_ID_TAG)
                        .build()); // gcm service
            }
        }
    };
    private final Action1<Object> queueDelete = object -> {
        if (Utils.hasLollipop()) {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(realmManager.getContext())
                    == ConnectionResult.SUCCESS) {
                Bundle extras = new Bundle();
                extras.putString(ImageDownloadIntentService.POST_OBJECT, new Gson().toJson(object));
                GcmNetworkManager.getInstance(realmManager.getContext()).schedule(new OneoffTask.Builder()
                        .setService(ImageDownloadGcmService.class)
                        .setRequiredNetwork(OneoffTask.NETWORK_STATE_CONNECTED)
                        .setExtras(extras)
                        .setTag(DELETE_TAG)
                        .build()); // gcm service
            }
        }
    };

    /**
     * Construct a {@link UserDataStore} based on connections to the api (Cloud).
     *
     * @param restApi      The {@link RestApi} implementation to use.
     * @param realmManager A {@link RealmManager} to cache data retrieved from the api.
     */
    public CloudDataStore(RestApi restApi, GeneralRealmManager realmManager, EntityDataMapper entityDataMapper) {
        this.restApi = restApi;
        this.entityDataMapper = entityDataMapper;
        this.realmManager = realmManager;
    }

    @Override
    public Observable<Collection> entityListFromDisk(Class clazz) {
        return Observable.error(new Exception("cant get from disk in cloud data store"));
    }

    @Override
    public Observable<Collection> collectionFromCloud(Class domainClass, Class dataClass) {
        this.dataClass = dataClass;
        return restApi.userCollection()
//                .retryWhen(observable -> {
//                    Log.v(TAG, "retryWhen, call");
//                    return observable.compose(Utils.zipWithFlatMap(TAG));
//                }).repeatWhen(observable -> {
//                    Log.v(TAG, "repeatWhen, call");
//                    return observable.compose(Utils.zipWithFlatMap(TAG));
//                })
                .doOnNext(saveAllGenericsToCacheAction)
                .map(realmModels -> entityDataMapper.transformAllToDomain(realmModels, domainClass))
                .compose(Utils.logSources(TAG, realmManager));
    }

    @Override
    public Observable<?> entityDetailsFromCloud(final int itemId, Class domainClass, Class dataClass) {
        this.dataClass = dataClass;
        return restApi.objectById(itemId)
//                .retryWhen(observable -> {
//                    Log.v(TAG, "retryWhen, call");
//                    return observable.compose(Utils.zipWithFlatMap(TAG));
//                }).repeatWhen(observable -> {
//                    Log.v(TAG, "repeatWhen, call");
//                    return observable.compose(Utils.zipWithFlatMap(TAG));
//                })
                .doOnNext(saveGenericToCacheAction)
                .map(entities -> entityDataMapper.transformToDomain(entities, domainClass));
//                .compose(Utils.logSource(TAG, realmManager));
    }

    @Override
    public Observable<?> postToCloud(Object object) {
        return Observable.create(subscriber -> {
            if (Utils.isNetworkAvailable(realmManager.getContext())) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(restApi.postItem(object));
                    subscriber.onCompleted();
                }
            } else {
                queuePost.call(object);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(object);
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public Observable<?> deleteFromCloud(int itemId, Class clazz) {
        return Observable.create(subscriber -> {
            if (Utils.isNetworkAvailable(realmManager.getContext())) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(restApi.deleteItemById(itemId));
                    subscriber.onCompleted();
                }
            } else {
                queueDeleteById.call(itemId);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(false);
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public Observable<?> deleteFromCloud(Object object, Class clazz) {
        return Observable.create(subscriber -> {
            if (Utils.isNetworkAvailable(realmManager.getContext())) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(restApi.deleteItem(object));
                    subscriber.onCompleted();
                }
            } else {
                queueDelete.call(object);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(false);
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public Observable<?> deleteCollectionFromCloud(Collection collection, Class clazz) {
        return Observable.create(subscriber -> {
            if (Utils.isNetworkAvailable(realmManager.getContext())) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(restApi.deleteCollection(collection));
                    subscriber.onCompleted();
                }
            } else {
                for (Object object : collection) {
                    queueDelete.call(object);
                    if (!subscriber.isUnsubscribed())
                        subscriber.onNext(false);
                }
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<?> entityDetailsFromDisk(int itemId, Class clazz) {
        return Observable.error(new Exception("cant get from disk in cloud data store"));
    }

    @Override
    public Observable<?> putToDisk(RealmObject object) {
        return Observable.error(new Exception("cant get from disk in cloud data store"));
    }

    @Override
    public Observable<?> deleteFromDisk(int itemId, Class clazz) {
        return Observable.error(new Exception("cant get from disk in cloud data store"));
    }

    @Override
    public Observable<?> deleteFromDisk(Object realmObject, Class clazz) {
        return Observable.error(new Exception("cant get from disk in cloud data store"));
    }

    @Override
    public Observable<?> deleteCollectionFromDisk(Collection collection, Class clazz) {
        return Observable.error(new Exception("cant get from disk in cloud data store"));
    }
}