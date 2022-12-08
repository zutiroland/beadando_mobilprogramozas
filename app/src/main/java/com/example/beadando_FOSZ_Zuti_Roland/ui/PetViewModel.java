package com.example.beadando_FOSZ_Zuti_Roland.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.beadando_FOSZ_Zuti_Roland.Pet;

import java.util.List;

public class PetViewModel extends ViewModel {

    private MutableLiveData<List<Pet>> pets =new MutableLiveData<>();

    public PetViewModel() {
    }

    public PetViewModel(List<Pet> pets) {
        this.pets.postValue(pets);
    }

    public void setPets(List<Pet> pets) {
        this.pets.postValue(pets);
    }

    public LiveData<List<Pet>> getPets(){
          return this.pets;
    }
}
