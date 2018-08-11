package com.deus.seow.lifepointcounter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LifeFragment extends Fragment implements Resetable {

    private TextView p1life, p2life;

    private final LinearLayout.LayoutParams gone = new LinearLayout.LayoutParams(0, 0);
    private final LinearLayout.LayoutParams there = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);

    public interface OnLifeFragmentCreateListener {
        void lifeFragmentCreated(LifeFragment lifeFragment);
    }

    private OnLifeFragmentCreateListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_life, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getChildFragmentManager().beginTransaction().add(R.id.container, new LifeTouchFragment()).commit();

        p1life = view.findViewById(R.id.p1_life);
        p2life = view.findViewById(R.id.p2_life);

        lifeChanged(4000, 4000);
        colorChanged(PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("color", Color.BLACK));

        listener.lifeFragmentCreated(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final FragmentActivity activity = getActivity();

        if (getActivity() instanceof OnLifeFragmentCreateListener) {
            listener = (OnLifeFragmentCreateListener) getActivity();
        } else
            throw new IllegalStateException("Activity is not an instance of OnLifeFragmentCreateListener");

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) activity).registerResetable(this);
        } else
            throw new IllegalStateException("Activity is not an instance of MainActivity");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).unregisterResetable(this);
    }

    @Override
    public void reset() {
        lifeChanged(4000, 4000);
    }

    public void onPlayerNumberChanged(boolean p2Visible) {
        if (p2Visible)
            p2life.setLayoutParams(gone);
        else p2life.setLayoutParams(there);
    }

    public void lifeChanged(int player1Life, int player2Life) {
        p1life.setText(String.valueOf(player1Life));
        p2life.setText(String.valueOf(player2Life));
    }

    public void colorChanged(int color) {
        p1life.setTextColor(color);
        p2life.setTextColor(color);
    }
}

