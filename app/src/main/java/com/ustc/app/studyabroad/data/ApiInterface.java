package com.ustc.app.studyabroad.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("getPrograms")
    Call<String> getPrograms(@Query("name") String name, @Query("index") String index);

    @GET("search_university")
    Call<String> searchUniversity(@Query("name") String name, @Query("type") String type);

    @GET("list_all_name_and_address")
    Call<String> listAll();

    @GET("getUniversity")
    Call<String> getUniversityDetail(@Query("name") String name);

    @GET("getProgramsListByField")
    Call<String> getProgramsAndFieldsList();

    @GET("filter")
    Call<String> filter(@Query("q") String filter, @Query("w") String type);

    @GET("index_query")
    Call<String> indexQuery(@Query("i") String filter, @Query("q") String type);

    @GET("search")
    Call<String> searchQuery(@Query("chars") String chars, @Query("q") String query, @Query("country") String country);

    @GET("gallery.json")//@GET("getGalleryImages")
    Call<String> getGalleryImages();

    @GET("list.json")//@GET("getGalleryImages")
    Call<String> list();

    @GET("categories.json")//@GET("getGalleryImages")
    Call<String> categories();

    @GET("programs.json")//@GET("getGalleryImages")
    Call<String> programs();

    @GET("main.json")//@GET("getGalleryImages")
    Call<String> main();

    @GET("fields.json")//@GET("getGalleryImages")
    Call<String> fields();
}

