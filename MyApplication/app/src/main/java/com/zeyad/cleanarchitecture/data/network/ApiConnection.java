package com.zeyad.cleanarchitecture.data.network;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zeyad.cleanarchitecture.data.entities.UserEntity;
import com.zeyad.cleanarchitecture.data.entities.UserRealmModel;
import com.zeyad.cleanarchitecture.data.executor.JobExecutor;
import com.zeyad.cleanarchitecture.utilities.Constants;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements {@link Callable} so when executed asynchronously can
 * return a value.
 */
public class ApiConnection {

    private static Retrofit retrofit;

    private static Retrofit createRetro2Client() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setCache(new Cache(new File(Constants.CACHE_DIR, "http"), 10485760));
        okHttpClient.interceptors().add(chain -> {
            Request request = chain.request();
            Log.d("OkHttp REQUEST", request.toString());
            Log.d("OkHttp REQUEST Headers", request.headers().toString());
            Response response = chain.proceed(request);
            response = response.newBuilder()
                    .header("Cache-Control", String.format("public, max-age=%d, max-stale=%d",
                            60, 2419200)).build();
            Log.d("OkHttp RESPONSE", response.toString());
            Log.d("OkHttp RESPONSE Headers", response.headers().toString());
            return response;
        });
        return new Retrofit.Builder()
                .baseUrl(RestApi.API_BASE_URL)
                .client(okHttpClient)
                .callbackExecutor(new JobExecutor())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setExclusionStrategies(new ExclusionStrategy() {
                            @Override
                            public boolean shouldSkipField(FieldAttributes f) {
                                return f.getDeclaringClass().equals(RealmObject.class);
                            }

                            @Override
                            public boolean shouldSkipClass(Class<?> clazz) {
                                return false;
                            }
                        }).create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Observable<Collection<UserEntity>> userEntityCollection() {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).userEntityCollection();
    }

    public static Observable<Collection<UserRealmModel>> userRealmCollection() {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).userRealmModelCollection();
    }

    public static Observable<Collection<RealmObject>> realmCollection() {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).userRealmObjectCollection();
    }

    public static Observable<UserEntity> user(int userId) {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).userEntityById(userId);
    }

    public static Observable<Collection> userCollection() {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).userCollection();
    }

    public static Observable<UserRealmModel> userRealm(int userId) {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).userRealmById(userId);
    }

    public static Observable<?> userById(int id) {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).userById(id);
    }

    public static Observable<Object> objectById(int id) {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).objectById(id);
    }

    public static Observable<RealmObject> realmObject(int userId) {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).realmObjectById(userId);
    }

    // TODO: 3/6/16 Test!
    public static Observable<retrofit.Response> getStream(String userId) {
        if (retrofit == null)
            retrofit = createRetro2Client();
        return retrofit.create(RestApi.class).getStream(userId);
    }
}