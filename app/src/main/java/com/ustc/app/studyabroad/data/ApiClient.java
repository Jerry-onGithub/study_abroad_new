package com.ustc.app.studyabroad.data;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    // Base Url
    public static final String BASE_URL = "https://jerry-ongithub.github.io/jsonapi/"; //"https://8hbuuj3yt1.execute-api.us-west-2.amazonaws.com/2022/";

    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        }
        return retrofit;
    }
}
