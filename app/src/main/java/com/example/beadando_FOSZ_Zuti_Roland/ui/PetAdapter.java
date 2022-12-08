package com.example.beadando_FOSZ_Zuti_Roland.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beadando_FOSZ_Zuti_Roland.Pet;
import com.example.beadando_FOSZ_Zuti_Roland.R;
import com.example.beadando_FOSZ_Zuti_Roland.databinding.ActivityPetsfromserverBinding;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetViewHolder> {

    private List<Pet> pets;

    private static PetClick clickListener;

    public PetAdapter(List<Pet> pets) {
        this.pets = pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    ActivityPetsfromserverBinding binding;

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_pets, parent, false);
        PetViewHolder petViewHolder = new PetViewHolder(layout, clickListener);
        return petViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder petViewHolder, int position) {
        Pet pet = pets.get(position);
        petViewHolder.chipszam.setText(pet.getChipszam());
        petViewHolder.kutyanev.setText(pet.getKutyanev());
        
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public void setClickListener(PetClick clickListener) {
        PetAdapter.clickListener = clickListener;
    }



}
