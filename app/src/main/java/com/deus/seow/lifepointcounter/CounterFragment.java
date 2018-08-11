package com.deus.seow.lifepointcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class CounterFragment extends Fragment implements Resetable {

    public static final String COUNTER_BLACK = "counter_black";
    public static final String COUNTER_YELLOW = "counter_yellow";
    public static final String COUNTER_BLUE = "counter_blue";
    public static final String COUNTER_RED = "counter_red";
    public static final String COUNTER_GREEN = "counter_green";

    private int black, yellow, blue, red, green;

    private TextView blackTextview, blueTextview, greenTextview, redTextview, yellowTextview;

    private EditText edit;
    SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_treasure_counter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        edit = view.findViewById(R.id.notes);
        edit.setText(prefs.getString("notes", null));

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prefs.edit().putString("notes", String.valueOf(s)).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        black = prefs.getInt(COUNTER_BLACK, 0);
        blue = prefs.getInt(COUNTER_BLUE, 0);
        yellow = prefs.getInt(COUNTER_YELLOW, 0);
        red = prefs.getInt(COUNTER_RED, 0);
        green = prefs.getInt(COUNTER_GREEN, 0);

        TextView blackUp = view.findViewById(R.id.black_up);
        TextView blueUp = view.findViewById(R.id.blue_up);
        TextView yellowUp = view.findViewById(R.id.yellow_up);
        TextView redUp = view.findViewById(R.id.red_up);
        TextView greenUp = view.findViewById(R.id.green_up);
        TextView blackDown = view.findViewById(R.id.black_down);
        TextView blueDown = view.findViewById(R.id.blue_down);
        TextView yellowDown = view.findViewById(R.id.yellow_down);
        TextView redDown = view.findViewById(R.id.red_down);
        TextView greenDown = view.findViewById(R.id.green_down);


        blackTextview = view.findViewById(R.id.black);
        blueTextview = view.findViewById(R.id.blue);
        redTextview = view.findViewById(R.id.red);
        greenTextview = view.findViewById(R.id.green);
        yellowTextview = view.findViewById(R.id.yellow);

        updateView();

        blackUp.setOnClickListener(click(COUNTER_BLACK, true));
        blackDown.setOnClickListener(click(COUNTER_BLACK, false));
        blueUp.setOnClickListener(click(COUNTER_BLUE, true));
        blueDown.setOnClickListener(click(COUNTER_BLUE, false));
        yellowUp.setOnClickListener(click(COUNTER_YELLOW, true));
        yellowDown.setOnClickListener(click(COUNTER_YELLOW, false));
        redUp.setOnClickListener(click(COUNTER_RED, true));
        redDown.setOnClickListener(click(COUNTER_RED, false));
        greenUp.setOnClickListener(click(COUNTER_GREEN, true));
        greenDown.setOnClickListener(click(COUNTER_GREEN, false));

    }

    private void updateView() {
        String blackText = String.valueOf(black);
        String blueText = String.valueOf(blue);
        String redText = String.valueOf(red);
        String yellowText = String.valueOf(yellow);
        String greenText = String.valueOf(green);

        blackTextview.setText(blackText);
        blueTextview.setText(blueText);
        redTextview.setText(redText);
        greenTextview.setText(greenText);
        yellowTextview.setText(yellowText);
    }

    private View.OnClickListener click(final String counter, boolean up) {
        if (up)
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (counter) {
                        case COUNTER_BLACK:
                            black++;
                            break;
                        case COUNTER_BLUE:
                            blue++;
                            break;
                        case COUNTER_GREEN:
                            green++;
                            break;
                        case COUNTER_RED:
                            red++;
                            break;
                        case COUNTER_YELLOW:
                            yellow++;
                            break;
                    }
                    updateView();
                    saveValues();
                }
            };
        else
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (counter) {
                        case COUNTER_BLACK:
                            if (black > 0)
                                black--;
                            break;
                        case COUNTER_BLUE:
                            if (blue > 0)
                                blue--;
                            break;
                        case COUNTER_GREEN:
                            if (green > 0)
                                green--;
                            break;
                        case COUNTER_RED:
                            if (red > 0)
                                red--;
                            break;
                        case COUNTER_YELLOW:
                            if (yellow > 0)
                                yellow--;
                            break;
                    }
                    updateView();
                    saveValues();
                }
            };
    }

    private void saveValues() {
        prefs.edit().putInt(COUNTER_BLACK, black)
                .putInt(COUNTER_YELLOW, yellow)
                .putInt(COUNTER_RED, red)
                .putInt(COUNTER_GREEN, green)
                .putInt(COUNTER_BLUE, blue)
                .apply();
    }

    @Override
    public void reset() {
        blue = 0;
        black = 0;
        green = 0;
        red = 0;
        yellow = 0;
        edit.setText(null);
        saveValues();
        updateView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).registerResetable(this);
        } else
            throw new IllegalStateException("Activity is not an instance of MainActivity");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).unregisterResetable(this);
    }
}
