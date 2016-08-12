package com.zeyad.cleanarchitecture.data.network;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * RestApi for retrieving data from the network.
 */
public interface RestApi {

    @GET
    Observable<Object> dynamicGetObject(@Url String url);

    @GET
    Observable<Object> dynamicGetObject(@Url String url, @Field("") boolean shouldCache);

    @GET
    Observable<List> dynamicGetList(@Url String url);

    @GET
    Observable<List> dynamicGetList(@Url String url, @Field("") boolean shouldCache);

    @POST
    Observable<Object> dynamicPostObject(@Url String url, @Body RequestBody body);

    @POST
    Observable<List> dynamicPostList(@Url String url, @Body RequestBody body);

    @PUT
    Observable<Object> dynamicPutObject(@Url String url, @Body RequestBody body);

    @PUT
    Observable<List> dynamicPutList(@Url String url, @Body RequestBody body);

    @DELETE
    Observable<Object> dynamicDeleteObject(@Url String url, @Body RequestBody body);

    @DELETE
    Observable<List> dynamicDeleteList(@Url String url, @Body RequestBody body);

    @Streaming
    @GET
    Observable<ResponseBody> dynamicDownload(@Url String fileUrl);

    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url, @Part("description") RequestBody description,
                                    @Part MultipartBody.Part file);

    @Multipart
    @POST
    Observable<Object> upload(@Url String url, @Part(value = "file\";filename=\"somename.jpg\"", encoding = "binary") RequestBody description);

    @Multipart
    @POST
    Observable<Object> upload(@Url String url, @Part(value = "file\";filename=\"somename.jpg\"", encoding = "binary") RequestBody description, boolean shouldCache);

    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url, @Part("description") RequestBody description,
                                    @Part MultipartBody.Part file, boolean shouldCache);

    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url,
                                    @Part MultipartBody.Part file);

    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url,
                                    @Part MultipartBody.Part file, boolean shouldCache);


    @POST
    Call<RefreshTokenEntity> refreshToken(@Url String url, @Body RequestBody body);
}