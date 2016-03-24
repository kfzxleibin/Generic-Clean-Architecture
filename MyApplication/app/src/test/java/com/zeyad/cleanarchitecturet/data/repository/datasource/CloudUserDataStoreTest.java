package com.zeyad.cleanarchitecturet.data.repository.datasource;

import com.zeyad.cleanarchitecturet.data.ApplicationTestCase;
import com.zeyad.cleanarchitecturet.data.db.RealmManager;
import com.zeyad.cleanarchitecturet.data.entities.mapper.UserEntityDataMapper;
import com.zeyad.cleanarchitecturet.data.network.RestApi;
import com.zeyad.cleanarchitecturet.data.repository.datasource.userstore.CloudUserDataStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class CloudUserDataStoreTest extends ApplicationTestCase {

    private static final int FAKE_USER_ID = 765;
    private CloudUserDataStore cloudUserDataStore;
    @Mock
    private RestApi mockRestApi;
    @Mock
    private RealmManager mockRealmManager;
    @Mock
    private UserEntityDataMapper mockUserEntityDataMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cloudUserDataStore = new CloudUserDataStore(mockRestApi, mockRealmManager, mockUserEntityDataMapper);
    }

    @Test
    public void testGetUserEntityListFromApi() {
        cloudUserDataStore.userEntityList();
        verify(mockRestApi).userEntityCollection();
    }

    @Test
    public void testGetUserEntityDetailsFromApi() {
        UserEntity fakeUserEntity = new UserEntity();
        Observable<UserEntity> fakeObservable = Observable.just(fakeUserEntity);
        given(mockRestApi.userEntityById(FAKE_USER_ID)).willReturn(fakeObservable);
        cloudUserDataStore.userEntityDetails(FAKE_USER_ID);
        verify(mockRestApi).userEntityById(FAKE_USER_ID);
    }
}