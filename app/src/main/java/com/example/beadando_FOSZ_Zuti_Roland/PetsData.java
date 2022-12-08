package com.example.beadando_FOSZ_Zuti_Roland;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.beadando_FOSZ_Zuti_Roland.databinding.ActivityPetsBinding;
import com.example.beadando_FOSZ_Zuti_Roland.service.MusicOnOpenPetData;


public class PetsData extends AppCompatActivity {
    ActivityPetsBinding binding;




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

        binding = ActivityPetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Pet pet = (Pet) intent.getSerializableExtra("pet");
        if (pet != null){
            binding.chipszamField.setText(pet.getChipszam());
            binding.kutyanevField.setText(pet.getKutyanev());
            binding.fajtaField.setText(pet.getFajta());
            binding.tulajField.setText(pet.getTulaj());
        }
        Intent serviceIntent = new Intent(this, MusicOnOpenPetData.class);
        startService(serviceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ActivityResultLauncher<Intent> kamerakep = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Bitmap img = (Bitmap) result.getData().getExtras().get("data");
                binding.serverLayout.setBackground(new BitmapDrawable(getResources(), img));
            }
        });

        binding.fotoGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                kamerakep.launch(intent);


            }
        });
    }




}
