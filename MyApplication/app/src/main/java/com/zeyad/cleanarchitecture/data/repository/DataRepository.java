package com.zeyad.cleanarchitecture.data.repository;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.zeyad.cleanarchitecture.data.entities.mapper.EntityMapper;
import com.zeyad.cleanarchitecture.data.entities.mapper.UserEntityDataMapper;
import com.zeyad.cleanarchitecture.data.repository.datastore.DataStoreFactory;
import com.zeyad.cleanarchitecture.domain.repository.Repository;
import com.zeyad.cleanarchitecture.utilities.Utils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmQuery;
import rx.Observable;

// TODO: 13/05/16 Document!
@Singleton
public class DataRepository implements Repository {

    private final DataStoreFactory mDataStoreFactory;
    private EntityMapper mEntityDataMapper;

    /**
     * Constructs a {@link Repository}.
     *
     * @param dataStoreFactory A factory to construct different data source implementations.
     */
    @Inject
    public DataRepository(DataStoreFactory dataStoreFactory) {
        mDataStoreFactory = dataStoreFactory;
        mEntityDataMapper = new UserEntityDataMapper();
    }

    @Override
    @RxLogObservable
    public Observable<List> dynamicList(String url, Class domainClass, Class dataClass, boolean persist) {
        return mDataStoreFactory.dynamically(url, Utils.getDataMapper(dataClass))
                .dynamicList(url, domainClass, dataClass, persist);
    }

    @Override
    @RxLogObservable
    public Observable<?> getObjectDynamicallyById(String url, String idColumnName, int itemId,
                                                  Class domainClass, Class dataClass,
                                                  boolean persist) {
        if (persist)
            return mDataStoreFactory.dynamically(url, idColumnName, itemId, Utils.getDataMapper(dataClass), dataClass)
                    .dynamicObject(url, idColumnName, itemId, domainClass, dataClass, true);
        else
            return mDataStoreFactory.cloud(Utils.getDataMapper(dataClass))
                    .dynamicObject(url, idColumnName, itemId, domainClass, dataClass, false);
    }

    @Override
    @RxLogObservable
    public Observable<?> postObjectDynamically(String url, HashMap<String, Object> keyValuePairs,
                                               Class domainClass, Class dataClass, boolean persist) {
        mEntityDataMapper = Utils.getDataMapper(dataClass);
        if (persist)
            return Observable.concat(mDataStoreFactory
                            .disk(mEntityDataMapper)
                            .putToDisk(keyValuePairs, dataClass),
                    mDataStoreFactory.cloud(mEntityDataMapper).dynamicPostObject(url,
                            keyValuePairs, domainClass, dataClass, true))
                    .distinct();
        return mDataStoreFactory.cloud(mEntityDataMapper).dynamicPostObject(url, keyValuePairs,
                domainClass, dataClass, false);
    }

    @Override
    @RxLogObservable
    public Observable<List> postListDynamically(String url, HashMap<String, Object> keyValuePairs,
                                                Class domainClass, Class dataClass, boolean persist) {
        return mDataStoreFactory.cloud(mEntityDataMapper).dynamicPostList(url, keyValuePairs,
                domainClass, dataClass, persist);
    }

    @Override
    @RxLogObservable
    public Observable<?> deleteListDynamically(String url, HashMap<String, Object> keyValuePairs,
                                               Class domainClass, Class dataClass, boolean persist) {
        mEntityDataMapper = Utils.getDataMapper(dataClass);
        if (persist)
            return Observable.concat(mDataStoreFactory
                            .disk(mEntityDataMapper)
                            .deleteCollectionFromDisk(keyValuePairs, dataClass),
                    mDataStoreFactory.cloud(mEntityDataMapper).deleteCollectionFromCloud(url,
                            keyValuePairs, dataClass, true))
                    .distinct();
        return mDataStoreFactory.cloud(mEntityDataMapper).dynamicPostObject(url, keyValuePairs,
                domainClass, dataClass, false);
    }

    // TODO: 13/05/16 Generalize!
    @Override
    @RxLogObservable
    public Observable<List> searchDisk(String query, String column, Class domainClass, Class dataClass) {
        return mDataStoreFactory.disk(Utils.getDataMapper(dataClass)).searchDisk(query, column,
                domainClass, dataClass);
    }

    @Override
    public Observable<List> searchDisk(RealmQuery query, Class domainClass) {
        return mDataStoreFactory.disk(Utils.getDataMapper(domainClass)).searchDisk(query, domainClass);
    }
}