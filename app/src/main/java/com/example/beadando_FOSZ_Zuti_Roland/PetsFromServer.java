package com.example.beadando_FOSZ_Zuti_Roland;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beadando_FOSZ_Zuti_Roland.database.PetService;
import com.example.beadando_FOSZ_Zuti_Roland.service.MusicOnOpenPetData;
import com.example.beadando_FOSZ_Zuti_Roland.ui.PetAdapter;
import com.example.beadando_FOSZ_Zuti_Roland.ui.PetClick;
import com.example.beadando_FOSZ_Zuti_Roland.ui.PetViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetsFromServer extends AppCompatActivity {


    private PetAdapter petAdapter;
    private PetViewModel petViewModel;
    List<Pet> pets;

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
        setContentView(R.layout.activity_petsfromserver);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://my-json-server.typicode.com/zutiroland/beadando_mobilprogramozas/").
                addConverterFactory(GsonConverterFactory.create()).build();

        PetService petService = retrofit.create(PetService.class);

        Call<List<Pet>> listCall= petService.getPets();
        try {
            listCall.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                    System.out.println("HTTP kérés válasza:");
                    System.out.println(response.body());
                    pets = response.body();
                    if(!pets.isEmpty()) {
                        petViewModel.setPets(pets);
                    }
                }

                @Override
                public void onFailure(Call<List<Pet>> call, Throwable t) {
                    System.out.println("HTTP kérés nem sikerült!");
                    t.printStackTrace();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.petsfromserverlista);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

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
                        Intent intent = new Intent(PetsFromServer.this, PetsData.class);
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
