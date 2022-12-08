package com.example.beadando_FOSZ_Zuti_Roland.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.beadando_FOSZ_Zuti_Roland.R;

public class PetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView chipszam;
    TextView kutyanev;
    TextView fajta;
    TextView tulaj;

    PetClick clickListener;


    public PetViewHolder(LinearLayout v, PetClick clickListener) {
        super(v);
        v.setOnClickListener(this);
        chipszam = v.findViewById(R.id.chipszamField);
        kutyanev = v.findViewById(R.id.kutyanevField);
        fajta = v. findViewById(R.id.fajtaField);
        tulaj = v. findViewById(R.id.tulajField);
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        clickListener.onAutoClick(getAdapterPosition(), view);
    }
}
