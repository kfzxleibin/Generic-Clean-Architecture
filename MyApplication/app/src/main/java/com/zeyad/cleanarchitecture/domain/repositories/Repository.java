package com.zeyad.cleanarchitecture.domain.repositories;

import com.zeyad.cleanarchitecture.domain.models.User;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link User} related data.
 */
public interface Repository {
    /**
     * Get an {@link Observable} which will emit a collection of Items.
     */
    Observable<List> collection(Class presentationClass, Class domainClass, Class dataClass);

    /**
     * Get an {@link Observable} which will emit an Item.
     *
     * @param itemId The user id used to retrieve getById data.
     */
    Observable<?> getById(final int itemId, Class presentationClass, Class domainClass, Class dataClass);

    Observable<List> dynamicCollection(final String url, Class presentationClass, Class domainClass, Class dataClass);

    Observable<?> dynamicObject(final String url, Class presentationClass, Class domainClass, Class dataClass);

    Observable<?> put(final Object object, Class presentationClass, Class domainClass, Class dataClass);

    Observable<?> deleteCollection(final List<Integer> list, Class domainClass, Class dataClass);

    Observable<List> search(String query, String column, Class presentationClass, Class domainClass, Class dataClass);
}