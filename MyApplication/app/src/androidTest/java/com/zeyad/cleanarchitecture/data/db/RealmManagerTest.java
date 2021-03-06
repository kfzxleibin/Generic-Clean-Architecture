package com.zeyad.cleanarchitecture.data.db;

import android.content.Context;
import android.test.AndroidTestCase;

import com.zeyad.cleanarchitecture.data.entities.UserRealmModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Subscriber;

import static org.mockito.Mockito.verify;

public class RealmManagerTest extends AndroidTestCase {

    private RealmManagerImpl realmManager;
    @Mock
    Context context;
    private static final int FAKE_USER_ID = 1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        assertNotNull(context);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build());
        realmManager = new RealmManagerImpl(context);
    }

    @After
    public void tearDown() throws Exception {
        realmManager.getRealm().close();
    }

    @Test
    public void testGet() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        userRealmModel.setUserId(FAKE_USER_ID);
        userRealmModel.setCover_url("www.test.com");
        userRealmModel.setDescription("fake description");
        userRealmModel.setEmail("fake@email.com");
        userRealmModel.setFollowers(22);
        userRealmModel.setFullName("Fake Name");
        realmManager.put(userRealmModel);
        verify(realmManager).get(FAKE_USER_ID);
    }

    @Test
    public void testGetAll() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        for (int i = 0; i < 3; i++) {
            userRealmModel.setUserId(FAKE_USER_ID + i);
            userRealmModel.setCover_url("www.test.com");
            userRealmModel.setDescription("fake description");
            userRealmModel.setEmail("fake@email.com");
            userRealmModel.setFollowers(22);
            userRealmModel.setFullName("Fake Name");
            realmManager.put(userRealmModel);
        }
        verify(realmManager).getAll();
    }

    @Test
    public void testPut() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        userRealmModel.setUserId(FAKE_USER_ID);
        userRealmModel.setCover_url("www.test.com");
        userRealmModel.setDescription("fake description");
        userRealmModel.setEmail("fake@email.com");
        userRealmModel.setFollowers(22);
        userRealmModel.setFullName("Fake Name");
        verify(realmManager).put(userRealmModel);
        verify(realmManager).get(FAKE_USER_ID);
    }

    @Test
    public void testPutAll() throws Exception {
        List<UserRealmModel> userRealmModels = new ArrayList<>();
        UserRealmModel userRealmModel;
        for (int i = 0; i < 10; i++) {
            userRealmModel = new UserRealmModel();
            userRealmModel.setUserId(FAKE_USER_ID + i);
            userRealmModel.setCover_url("www.test.com");
            userRealmModel.setDescription("fake description");
            userRealmModel.setEmail("fake@email.com");
            userRealmModel.setFollowers(22);
            userRealmModel.setFullName("Fake Name");
            userRealmModels.add(userRealmModel);
        }
        verify(realmManager).putAll(userRealmModels);
        for (int i = 0; i < 10; i++)
            verify(realmManager).get(FAKE_USER_ID + i);
    }

    @Test
    public void testIsCached() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        userRealmModel.setUserId(FAKE_USER_ID);
        userRealmModel.setCover_url("www.test.com");
        userRealmModel.setDescription("fake description");
        userRealmModel.setEmail("fake@email.com");
        userRealmModel.setFollowers(22);
        userRealmModel.setFullName("Fake Name");
        realmManager.put(userRealmModel);
        assertTrue(realmManager.isCached(FAKE_USER_ID));
    }

    @Test
    public void testIsValid() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        userRealmModel.setUserId(FAKE_USER_ID);
        userRealmModel.setCover_url("www.test.com");
        userRealmModel.setDescription("fake description");
        userRealmModel.setEmail("fake@email.com");
        userRealmModel.setFollowers(22);
        userRealmModel.setFullName("Fake Name");
        realmManager.put(userRealmModel);
        assertTrue(realmManager.isUserValid(FAKE_USER_ID));
    }

    @Test
    public void testIsValid1() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        userRealmModel.setUserId(FAKE_USER_ID);
        userRealmModel.setCover_url("www.test.com");
        userRealmModel.setDescription("fake description");
        userRealmModel.setEmail("fake@email.com");
        userRealmModel.setFollowers(22);
        userRealmModel.setFullName("Fake Name");
        realmManager.put(userRealmModel);
        assertTrue(realmManager.areUsersValid());
    }

    @Test
    public void testEvictAll() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        for (int i = 0; i < 3; i++) {
            userRealmModel.setUserId(FAKE_USER_ID + i);
            userRealmModel.setCover_url("www.test.com");
            userRealmModel.setDescription("fake description");
            userRealmModel.setEmail("fake@email.com");
            userRealmModel.setFollowers(22);
            userRealmModel.setFullName("Fake Name");
            realmManager.put(userRealmModel);
        }
        realmManager.evictAll();
        realmManager.getAll().subscribe(new Subscriber<Collection<UserRealmModel>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                fail();
            }

            @Override
            public void onNext(Collection<UserRealmModel> userRealmModels) {
                assertEquals(userRealmModels.size(), 0);
            }
        });
    }

    @Test
    public void testEvictById() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        userRealmModel.setUserId(FAKE_USER_ID);
        userRealmModel.setCover_url("www.test.com");
        userRealmModel.setDescription("fake description");
        userRealmModel.setEmail("fake@email.com");
        userRealmModel.setFollowers(22);
        userRealmModel.setFullName("Fake Name");
        realmManager.put(userRealmModel);
        realmManager.evictById(FAKE_USER_ID);
        realmManager.get(FAKE_USER_ID).subscribe(new Subscriber<UserRealmModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                fail();
            }

            @Override
            public void onNext(UserRealmModel userRealmModel) {
                assertNull(userRealmModel);
            }
        });
    }

    @Test
    public void testEvict() throws Exception {
        UserRealmModel userRealmModel = new UserRealmModel();
        userRealmModel.setUserId(FAKE_USER_ID);
        userRealmModel.setCover_url("www.test.com");
        userRealmModel.setDescription("fake description");
        userRealmModel.setEmail("fake@email.com");
        userRealmModel.setFollowers(22);
        userRealmModel.setFullName("Fake Name");
        realmManager.put(userRealmModel);
        realmManager.evict(userRealmModel);
        realmManager.get(FAKE_USER_ID).subscribe(new Subscriber<UserRealmModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                fail();
            }

            @Override
            public void onNext(UserRealmModel userRealmModel) {
                assertNull(userRealmModel);
            }
        });
    }
}