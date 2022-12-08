package com.example.beadando_FOSZ_Zuti_Roland;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.beadando_FOSZ_Zuti_Roland.database.PetDAO;
import com.example.beadando_FOSZ_Zuti_Roland.database.PetDatabase;
import com.example.beadando_FOSZ_Zuti_Roland.service.MusicOnOpenPetData;
import com.example.beadando_FOSZ_Zuti_Roland.ui.PetAdapter;
import com.example.beadando_FOSZ_Zuti_Roland.ui.PetClick;
import com.example.beadando_FOSZ_Zuti_Roland.ui.PetViewModel;

import java.util.ArrayList;
import java.util.List;

public class PetsFromDatabase extends AppCompatActivity {


    PetDatabase db;
    PetDAO petDAO;

    private PetViewModel petViewModel;
    private PetAdapter petAdapter;

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MusicOnOpenPetData.class);
        stopService(intent);
        finish();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petsfromdb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), PetDatabase.class, "petDatabase").build();
        petDAO = db.getPetDAO();


        RecyclerView recyclerView = findViewById(R.id.petslista);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Pet> petsFromDb = petDAO.getAll();
                if(!petsFromDb.isEmpty()) {
                    petViewModel.setPets(petsFromDb);
                }

            }
        });
        petAdapter = new PetAdapter(new ArrayList<>());
        recyclerView.setAdapter(petAdapter);
        if(petViewModel.getPets()!=null) {
            petViewModel.getPets().observe(this, pets -> {
                petAdapter.setPets(pets);
                petAdapter.setClickListener(new PetClick() {
                    @Override
                    public void onAutoClick(int position, View v) {
                        Pet pet = pets.get(position);
                        System.out.println("Ez az kisállat lett kiválasztva: " + pets.get(position));
                        Intent intent = new Intent(PetsFromDatabase.this, PetsData.class);
                        intent.putExtra("pet", pet);
                        startActivity(intent);

                    }
                });
                recyclerView.setAdapter(petAdapter);

            });
        } else{
            System.out.println("Nincs ilyen adat az adatbázisban!");
        }



    }
}
