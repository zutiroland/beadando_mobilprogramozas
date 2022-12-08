package com.example.beadando_FOSZ_Zuti_Roland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beadando_FOSZ_Zuti_Roland.database.PetDAO;
import com.example.beadando_FOSZ_Zuti_Roland.database.PetDatabase;
import com.example.beadando_FOSZ_Zuti_Roland.database.PetService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText chipszam;
    EditText kutyanev;
    EditText fajta;
    EditText tulaj;
    Button loadButton;
    Button saveButton;
    Button saveToFileButton;
    Button saveToServerButton;
    Button loadFromServerButton;
    Button loadFromServerByChipszam;
    Button petsFromWeb;
    PetDatabase db;
    PetDAO petDAO;
    PetService petService;
    String regex = "^[a-zA-Z0-9]*$";
    Pattern pattern = Pattern.compile(regex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            db =Room.databaseBuilder(

            getApplicationContext(), PetDatabase.class,"petDatabase").

            build();

            petDAO =db.getPetDAO();

            Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://my-json-server.typicode.com/zutiroland/beadando_mobilprogramozas/").
                addConverterFactory(GsonConverterFactory.create()).build();

            petService = retrofit.create(PetService.class);

    }

    @Override
    protected void onStart() {
        super.onStart();

        kutyanev = findViewById(R.id.kutyanev);
        fajta = findViewById(R.id.fajta);
        chipszam = findViewById(R.id.chipszam);
        tulaj = findViewById(R.id.tulaj);

        loadButton = findViewById(R.id.betoltGomb);
        loadFromServerButton = findViewById(R.id.loadFromServerGomb);
        loadFromServerByChipszam = findViewById(R.id.loadFromServerByChipszamGomb);
        saveButton = findViewById(R.id.mentesGomb);
        saveToFileButton= findViewById(R.id.fajlbaMentGomb);
        saveToServerButton =findViewById(R.id.saveToServerGomb);
        petsFromWeb = findViewById(R.id.autokfromwebGomb);



       SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        if (!prefs.contains("chipszam")) {
            saveButton.setClickable(false);
            saveToFileButton.setClickable(false);
        } else {
            saveButton.setClickable(false);
            saveToFileButton.setClickable(false);
        }


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }

       saveButton.setOnClickListener(view -> {
            ConstraintLayout layout = findViewById(R.id.mainlayout);
            if (checkIfFieldsAreFilled(layout)&&checkFieldsComplyRegex(layout)) {
                Pet pet = new Pet(chipszam.getText().toString(), kutyanev.getText().toString(), fajta.getText().toString(), tulaj.getText().toString());
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        petDAO.insert(pet);
                        System.out.println("insertion ok");
                    }
                });
                Toast.makeText(MainActivity.this, getString(R.string.adatbazisba_mentve), Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(MainActivity.this, getString(R.string.error_uzenet), Toast.LENGTH_SHORT).show();
            }
        });


        loadButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PetsFromDatabase.class);
            startActivity(intent);
        });

        loadFromServerButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PetsFromServer.class);
            startActivity(intent);

        });

        petsFromWeb.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.petvetdata.hu"));
            startActivity(intent);

        });

        saveToFileButton.setOnClickListener(view -> {
            ConstraintLayout layout = findViewById(R.id.mainlayout);
            if (checkIfFieldsAreFilled(layout)&&checkFieldsComplyRegex(layout)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("chipszam", chipszam.getText().toString());
                editor.putString("kutyanev", kutyanev.getText().toString());
                editor.putString("fajta", fajta.getText().toString());
                editor.putString("tulaj", tulaj.getText().toString());
                editor.apply();


                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "pets.txt");

                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.append(String.format("%s, %s, %s, %s\n", chipszam.getText().toString(), kutyanev.getText().toString(), fajta.getText().toString(),tulaj.getText().toString()));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Toast.makeText(MainActivity.this, getString(R.string.sikeres_fajlba_mentes), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });



        loadFromServerByChipszam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout layout = findViewById(R.id.mainlayout);

                if (checkIfChipszamFieldIsFilled(layout)) {


                    Call<List<Pet>> petByChipszamCall= petService.getPetByChipszam(chipszam.getText().toString());

                    try {
                        petByChipszamCall.enqueue(new Callback<List<Pet>>() {
                            @Override
                            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                                if (!response.body().isEmpty()){
                                    Intent intent = new Intent(MainActivity.this, PetsData.class);
                                    Pet pet = response.body().get(0);
                                    intent.putExtra("pet", pet);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(MainActivity.this, getString(R.string.pet_nem_talalhato),
                                            Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<List<Pet>> call, Throwable t) {
                                System.out.println("Get hívás nem kapott választ!");
                            }
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.error_no_chipszam), Toast.LENGTH_LONG).show();
                }


            }


        });

        saveToServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout layout = findViewById(R.id.mainlayout);

                if (checkIfFieldsAreFilled(layout)) {
                    Pet pet = new Pet(chipszam.getText().toString(), kutyanev.getText().toString(), fajta.getText().toString(), tulaj.getText().toString());


                    Call<Pet> createPet= petService.createPet(pet);

                    createPet.enqueue(new Callback<Pet>() {
                        @Override
                        public void onResponse(Call<Pet> call, Response<Pet> response) {

                            Toast.makeText(MainActivity.this, getString(R.string.mentes_serverre),
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Pet> call, Throwable t) {
                            Toast.makeText(MainActivity.this, getString(R.string.sikertelen_mentes_serverre), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.error_uzenet), Toast.LENGTH_LONG).show();
                }
            }
        });


    }




    public boolean checkIfFieldsAreFilled(ViewGroup viewGroup) {
        boolean result = true;
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                checkIfFieldsAreFilled((ViewGroup) view);
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                if (editText.getText().toString().trim().isEmpty()) {
                    result = false;
                    editText.setError(getString(R.string.kotelezo));
                }
            }
        }
        return result;
    }

    public boolean checkIfChipszamFieldIsFilled(ViewGroup viewGroup) {
        boolean result = true;

            View view = viewGroup.getChildAt(0);
            if (view instanceof ViewGroup) {
                checkIfChipszamFieldIsFilled((ViewGroup) view);
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                if (editText.getText().toString().trim().isEmpty()||!pattern.matcher(editText.getText().toString()).matches()) {
                    result = false;
                    editText.setError(getString(R.string.kotelezo));
                }
            }
        return result;
    }

    public boolean checkFieldsComplyRegex(ViewGroup viewGroup) {
        boolean result = true;
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                checkFieldsComplyRegex((ViewGroup) view);
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                if (!pattern.matcher(editText.getText().toString()).matches()) {
                    result = false;
                    editText.setError(getString(R.string.bad_character));
                }
            }
        }
        return result;
    }
}