package com.example.beadando_FOSZ_Zuti_Roland;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity
public class Pet implements Serializable {

    @PrimaryKey
    @NonNull
    private String chipszam;
    private String kutyanev;
    private String fajta;
    private String tulaj;



    public String getChipszam() {
        return chipszam;
    }

    public void setChipszam(@NonNull String chipszam) {
        this.chipszam = chipszam;
    }

    public String getKutyanev() {
        return kutyanev;
    }

    public void setKutyanev(String kutyanev) {
        this.kutyanev = kutyanev;
    }

    public String getFajta() {
        return fajta;
    }

    public void setFajta(String fajta) {
        this.fajta = fajta;
    }

    public String getTulaj() {
        return tulaj;
    }

    public void setTulaj(String tulaj) {
        this.tulaj = tulaj;
    }



    public Pet(String chipszam, String kutyanev, String fajta, String tulaj) {

        this.chipszam = chipszam;
        this.kutyanev = kutyanev;
        this.fajta = fajta;
        this.tulaj = tulaj;
    }

    @Override
    public String toString() {
        return "Pet{" +
                ", chipszam='" + chipszam + '\'' +
                ", kutyanev='" + kutyanev + '\'' +
                ", fajta='" + fajta + '\'' +
                ", tulaj='" + tulaj + '\'' +
                '}';
    }
}
