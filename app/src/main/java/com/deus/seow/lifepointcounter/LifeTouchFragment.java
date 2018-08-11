package com.deus.seow.lifepointcounter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LifeTouchFragment extends Fragment {

    public interface OnLifeInputListener {
        void lifeChanged(int amount, int player);
    }

    OnLifeInputListener liListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_touch_mask, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView p1up = view.findViewById(R.id.p1_plus);
        TextView p1down = view.findViewById(R.id.p1_minus);
        TextView p2up = view.findViewById(R.id.p2_plus);
        TextView p2down = view.findViewById(R.id.p2_minus);


        p1up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liListener.lifeChanged(100, 1);
            }
        });

        p1down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liListener.lifeChanged(-100, 1);
            }
        });

        p2up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liListener.lifeChanged(100, 2);
            }
        });

        p2down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liListener.lifeChanged(-100, 2);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof OnLifeInputListener) {
            liListener = (OnLifeInputListener) getActivity();
        } else
            throw new IllegalStateException("Activity is not an instance of OnLifeInputListener");
    }


}
