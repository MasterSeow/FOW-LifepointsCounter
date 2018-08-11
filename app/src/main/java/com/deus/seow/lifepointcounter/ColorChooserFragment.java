package com.deus.seow.lifepointcounter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ColorChooserFragment extends Fragment {

    TextView black, white, green, yellow, red, magenta, cyan, blue;

    public interface OnColorChosenListener {
        void colorChosen(int chosenColor);
    }

    private OnColorChosenListener cListener;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_color, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        black = view.findViewById(R.id.color_black);
        white = view.findViewById(R.id.color_white);
        green = view.findViewById(R.id.color_green);
        yellow = view.findViewById(R.id.color_yellow);
        red = view.findViewById(R.id.color_red);
        magenta = view.findViewById(R.id.color_magenta);
        cyan = view.findViewById(R.id.color_cyan);
        blue = view.findViewById(R.id.color_blue);

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.colorChosen(Color.BLACK);
                getFragmentManager().popBackStack();
            }
        });

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.colorChosen(Color.WHITE);
                getFragmentManager().popBackStack();
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.colorChosen(Color.GREEN);
                getFragmentManager().popBackStack();
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.colorChosen(Color.YELLOW);
                getFragmentManager().popBackStack();
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.colorChosen(Color.RED);
                getFragmentManager().popBackStack();
            }
        });

        magenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.colorChosen(Color.MAGENTA);
                getFragmentManager().popBackStack();
            }
        });

        cyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.colorChosen(Color.CYAN);
                getFragmentManager().popBackStack();
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cListener.colorChosen(Color.BLUE);
                getFragmentManager().popBackStack();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof OnColorChosenListener) {
            cListener = (OnColorChosenListener) getActivity();
        } else
            throw new IllegalStateException("Activity is not an instance of OnColorChosenListener");
    }
}
