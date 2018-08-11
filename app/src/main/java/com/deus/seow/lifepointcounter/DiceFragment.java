package com.deus.seow.lifepointcounter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class DiceFragment extends Fragment {

    ImageView diceView;
    TextView dicebg;
    Random random = new Random();
    Drawable dice1, dice2, dice3, dice4, dice5, dice6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        diceView = view.findViewById(R.id.dice_image_view);
        dicebg = view.findViewById(R.id.dice_bg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dice1 = getResources().getDrawable(R.drawable.dice_one, null);
            dice2 = getResources().getDrawable(R.drawable.dice_two, null);
            dice3 = getResources().getDrawable(R.drawable.dice_three, null);
            dice4 = getResources().getDrawable(R.drawable.dice_four, null);
            dice5 = getResources().getDrawable(R.drawable.dice_five, null);
            dice6 = getResources().getDrawable(R.drawable.dice_six, null);
        }

        random.setSeed(System.currentTimeMillis());

        rollTheDice();

        diceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollTheDice();
            }
        });

        dicebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

    }

    private void rollTheDice() {

        int result = random.nextInt(6) + 1;

        switch (result) {
            case 1:
                diceView.setImageDrawable(dice1);
                break;
            case 2:
                diceView.setImageDrawable(dice2);
                break;
            case 3:
                diceView.setImageDrawable(dice3);
                break;
            case 4:
                diceView.setImageDrawable(dice4);
                break;
            case 5:
                diceView.setImageDrawable(dice5);
                break;
            case 6:
                diceView.setImageDrawable(dice6);
                break;


        }


    }

}
