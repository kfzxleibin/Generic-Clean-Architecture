package com.zeyad.cleanarchitecture.data.db;

import android.content.Context;

import org.json.JSONObject;

import java.util.List;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import rx.Observable;

// TODO: 8/06/16 remove!
public interface DataBaseManager {
    /**
     * Gets an {@link Observable} which will emit an Object.
     *
     * @param userId The user id to retrieve data.
     */
    @NonNull
    Observable<?> getById(final String idColumnName, final int userId, Class clazz);

    /**
     * Gets an {@link Observable} which will emit a List of Objects.
     */
    @NonNull
    Observable<List> getAll(Class clazz);

    /**
     * Puts and element into the cache.
     *
     * @param realmModel Element to insert in the cache.
     */
    @NonNull
    Observable<?> put(RealmObject realmModel, Class dataClass);

    @NonNull
    Observable<?> put(RealmModel realmModel, Class dataClass);

    @NonNull
    Observable<?> put(JSONObject realmObject, String idColumnName, Class dataClass);

    /**
     * Puts and element into the cache.
     *
     * @param realmModels Element to insert in the cache.
     */
    void putAll(List<RealmObject> realmModels, Class dataClass);

    /**
     * Puts and element into the cache.
     *
     * @param jsonArray Element to insert in the cache.
     */
    @NonNull
    Observable putAll(JSONArray jsonArray, Class dataClass);

    /**
     * Checks if an element (User) exists in the cache.
     *
     * @param itemId The id used to look for inside the cache.
     * @return true if the element is cached, otherwise false.
     */
    boolean isCached(final int itemId, String columnId, Class clazz);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isItemValid(final int itemId, String columnId, Class clazz);

    boolean areItemsValid(String destination);

    /**
     * Evict all elements of the cache.
     */
    @NonNull
    Observable<Boolean> evictAll(Class clazz);

    void evict(final RealmObject realmModel, Class clazz);

    boolean evictById(Class clazz, String idFieldName, long idFieldValue);

    @NonNull
    Observable<?> evictCollection(String idFieldName, List<Long> list, Class dataClass);

    Context getContext();

    @NonNull
    Observable<List> getWhere(Class clazz, String query, String filterKey);

    @NonNull
    Observable<List> getWhere(RealmQuery realmQuery);
}