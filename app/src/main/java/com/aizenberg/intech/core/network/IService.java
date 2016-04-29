package com.aizenberg.intech.core.network;

import com.aizenberg.intech.core.model.Melodies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Yuriy Aizenberg
 */
public interface IService {


    String MELODIES = "melodies";

    @GET(MELODIES)
    Call<Melodies> getMelodies(@Query("limit") int limit, @Query("from") int offset);

}
