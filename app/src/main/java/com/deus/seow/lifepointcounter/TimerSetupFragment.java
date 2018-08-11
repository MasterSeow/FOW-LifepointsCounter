package com.deus.seow.lifepointcounter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

public class TimerSetupFragment extends Fragment {

    private static int RESULT_LOAD_SOUND = 2;

    private SeekBar timeSeek;
    private TextView timeText, songText;
    private CheckBox noAlarmCb;
    private boolean alarmSound;

    public interface TimerListener {
        void timerStarted(int minutes, boolean noAlarm);
    }

    private TimerListener tListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timeSeek = view.findViewById(R.id.time_seekbar);
        timeText = view.findViewById(R.id.time_textview);
        songText = view.findViewById(R.id.song_title);
        Button chooseSong = view.findViewById(R.id.choose_song);
        Button start = view.findViewById(R.id.timer_start);
        Button abort = view.findViewById(R.id.timer_abort);
        noAlarmCb = view.findViewById(R.id.no_alarm_cb);

        alarmSound = !noAlarmCb.isChecked();

        timeText.setText(String.valueOf(timeSeek.getProgress()));

        timeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 1) {
                    progress = 1;
                    timeText.setText(String.valueOf(progress) + " Minute");
                } else
                    timeText.setText(String.valueOf(progress) + " Minuten");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        String uriString = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("soundPath", null);

        if (uriString != null)
            songText.setText(getFilenameFromUriString(uriString));
        else
            songText.setText(R.string.standard);

        chooseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_SOUND);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSound = !noAlarmCb.isChecked();
                getFragmentManager().popBackStack();
                int timeSet = timeSeek.getProgress();
                if (timeSet == 0)
                    timeSet = 1;

                tListener.timerStarted(timeSet, alarmSound);

            }
        });

        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private String getFilenameFromUriString(String uriString) {
        int i = uriString.lastIndexOf("/");
        int j = uriString.lastIndexOf(".");
        return uriString.substring(i + 1, j);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof TimerListener) {
            tListener = (TimerListener) getActivity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_SOUND && resultCode == RESULT_OK && null != data) {
            Uri selectedSound = data.getData();
            String[] filePathColumn = {MediaStore.Audio.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedSound,
                    filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String soundPath = cursor.getString(columnIndex);
                cursor.close();
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("soundPath", soundPath).apply();
                songText.setText(getFilenameFromUriString(soundPath));
            }
        }
    }
}
