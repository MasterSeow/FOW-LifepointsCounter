package com.deus.seow.lifepointcounter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements Resetable {

    private ListView lv, lv2;

    private LinearLayout p2Container;

    private final LinearLayout.LayoutParams gone = new LinearLayout.LayoutParams(0, 0);
    private final LinearLayout.LayoutParams there = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

    public void setP2Visible(boolean player2isVisible) {
        if (!player2isVisible)
            p2Container.setLayoutParams(there);
        else
            p2Container.setLayoutParams(gone);

    }

    public interface OnHistoryFragmentActive {
        void historyFragmentCreated(HistoryFragment historyFragment);
    }

    private OnHistoryFragmentActive hListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv = view.findViewById(R.id.list);
        lv2 = view.findViewById(R.id.list2);
        p2Container = view.findViewById(R.id.p2_history_container);
        View ht = view.findViewById(R.id.history_touch);
        View ht2 = view.findViewById(R.id.history_touch2);
        hListener.historyFragmentCreated(this);

        ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        ht2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final FragmentActivity activity = getActivity();

        if (activity instanceof OnHistoryFragmentActive) {
            hListener = (OnHistoryFragmentActive) activity;
        } else
            throw new IllegalStateException("Activity is not an instance of OnHistoryFragmentActive");

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

    public void showHistory(List<Integer> historyP1, List<Integer> historyP2) {

        HistoryAdapter arrayAdapter = new HistoryAdapter(historyP1, getContext());
        lv.setAdapter(arrayAdapter);

        HistoryAdapter arrayAdapter2 = new HistoryAdapter(historyP2, getContext());
        lv2.setAdapter(arrayAdapter2);

    }

    @Override
    public void reset() {
        List<Integer> history = new ArrayList<>();
        history.add(4000);
        HistoryAdapter arrayAdapter = new HistoryAdapter(history, getContext());

        lv2.setAdapter(arrayAdapter);
        lv.setAdapter(arrayAdapter);
    }
}
