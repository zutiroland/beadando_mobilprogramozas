package com.example.beadando_FOSZ_Zuti_Roland.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.beadando_FOSZ_Zuti_Roland.Pet;

@Database(entities = {Pet.class}, version = 1)
public abstract class PetDatabase extends RoomDatabase {

        public abstract PetDAO getPetDAO();

}
