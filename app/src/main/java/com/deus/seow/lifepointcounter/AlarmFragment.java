package com.deus.seow.lifepointcounter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AlarmFragment extends Fragment {

    public interface Alarm {
        void shutUp();
    }

    Alarm aListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button snooze = view.findViewById(R.id.timer_off);

        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aListener.shutUp();
                getFragmentManager().popBackStack();
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof Alarm) {
            aListener = (Alarm) getActivity();
        } else
            throw new IllegalStateException("Activity is not an instance of OnColorChosenListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        aListener.shutUp();
    }
}
