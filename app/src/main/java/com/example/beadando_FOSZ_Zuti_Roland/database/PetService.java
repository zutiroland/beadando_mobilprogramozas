package com.example.beadando_FOSZ_Zuti_Roland.database;

import com.example.beadando_FOSZ_Zuti_Roland.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PetService {
    @GET("pets")
    Call<List<Pet>> getPets();

    @GET("pets")
    Call<List<Pet>> getPetByChipszam(@Query("chipszam") String chipszam);

    @POST("pets")
    Call<Pet> createPet(@Body Pet pet);

}