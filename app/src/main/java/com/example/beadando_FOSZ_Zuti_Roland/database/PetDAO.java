package com.example.beadando_FOSZ_Zuti_Roland.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.beadando_FOSZ_Zuti_Roland.Pet;

import java.util.List;

@Dao
public interface PetDAO {

    @Query("Select * from pet")
    List<Pet> getAll();

    @Query("SELECT * from pet WHERE kutyanev=:nev")
    List<Pet> findBynev(String nev);

    @Query("SELECT * from pet WHERE chipszam=:chip")
    List<Pet> findBychip(String chip);

    @Insert
    void insert(Pet pet);

    @Delete
    void delete(Pet pet);

    @Update
    void update(Pet pet);


}
