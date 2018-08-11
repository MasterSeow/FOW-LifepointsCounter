package com.deus.seow.lifepointcounter;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity implements LifeFragment.OnLifeFragmentCreateListener, HistoryFragment.OnHistoryFragmentActive, LifeTouchFragment.OnLifeInputListener, ColorChooserFragment.OnColorChosenListener, SharedPreferences.OnSharedPreferenceChangeListener, TimerSetupFragment.TimerListener, AlarmFragment.Alarm {

    private static final int MY_READ_EXTERNAL_STORAGE = 102;
    private static final String STATE_HISTORY = "history1";
    private static final String STATE_HISTORY_SECOND = "history2";
    private static final String STATE_P2 = "p2visible";
    private static int RESULT_LOAD_IMAGE = 1;

    int player1Life = 4000, player2Life = 4000;

    private boolean player2isVisible = true;

    CountDownTimer timeoutTimer;
    TextView timer;
    long timeLeftLong;


    private String selImg, timeLeft;
    private List<Integer> historyP1, historyP2;
    private String[] comingSoonMessages;
    private MediaPlayer mediaPlayer;
    private FragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private SharedPreferences preferences;

    private final Random random = new Random();

    private List<Integer> lastLifeChangesP1;
    private List<Integer> lastLifeChangesP2;
    private CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            toHistory();
        }
    };

    private List<Resetable> resetables = new CopyOnWriteArrayList<>();


    LifeFragment lifeFragment;
    HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            player2isVisible = savedInstanceState.getBoolean(STATE_P2);
        }

        historyP1 = new ArrayList<>();
        historyP2 = new ArrayList<>();
        historyP1.add(4000);
        historyP2.add(4000);

        setContentView(R.layout.activity_main);

        pagerAdapter = new SitePagerAdapter(getSupportFragmentManager());

        timer = findViewById(R.id.timer_time);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        lastLifeChangesP1 = new ArrayList<>();
        lastLifeChangesP2 = new ArrayList<>();

        comingSoonMessages = getResources().getStringArray(R.array.coming_soon_messages);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        player1Life = preferences.getInt("p1Life", 4000);
        player2Life = preferences.getInt("p2Life", 4000);
        loadHistories();

        random.setSeed(System.currentTimeMillis());

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_READ_EXTERNAL_STORAGE);

        } else {
            selImg = preferences.getString("imagePath", null);
            loadImage();
        }

        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_P2, player2isVisible);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_READ_EXTERNAL_STORAGE:
                recreate();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.reset:
                reset();
                return true;
            case R.id.player_1_2:
                playerNumberChange();
                return true;
            case R.id.timer:
                if (fragmentManager.findFragmentByTag("TIMER_TAG") == null)
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.the_container, new TimerSetupFragment(), "TIMER_TAG").commit();
                return true;
            case R.id.dice:
                if (fragmentManager.findFragmentByTag("DICE_TAG") == null)
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.the_container, new DiceFragment(), "DICE_TAG").commit();
                return true;
            case R.id.color:
                if (fragmentManager.findFragmentByTag("COLOR_CHOOSER_TAG") == null)
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.container_color, new ColorChooserFragment(), "COLOR_CHOOSER_TAG").commit();
                return true;
            case R.id.new_background:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                return true;
            case R.id.info:
                showAppInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key) {
            case "imagePath":
                selImg = sharedPreferences.getString(key, null);
                break;
            case "p1Life":
                player1Life = sharedPreferences.getInt(key, 4000);
                break;
            case "p2Life":
                player2Life = sharedPreferences.getInt(key, 4000);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                preferences.edit().putString("imagePath", picturePath).apply();
            }
        }
        loadImage();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1 || getSupportFragmentManager().getBackStackEntryCount() > 0)
            super.onBackPressed();
        else
            viewPager.setCurrentItem(1);
    }


    private void showAppInfo() {
        String output = "App-Version : " + BuildConfig.VERSION_NAME + "\n" +
                "over 2.000 lines of code.\nMade by SEOW.";

        Toast.makeText(this, output, Toast.LENGTH_LONG).show();
    }


    @SuppressWarnings("unused")
    private void randomFutureImplementationToast() {
        String randomOutput = comingSoonMessages[random.nextInt(24)];
        Toast.makeText(this, randomOutput, Toast.LENGTH_SHORT).show();
    }

    private void playerNumberChange() {
        lifeFragment.onPlayerNumberChanged(player2isVisible);
        if (historyFragment != null)
            historyFragment.setP2Visible(player2isVisible);
        player2isVisible = !player2isVisible;
    }

    private void reset() {
        player1Life = 4000;
        player2Life = 4000;
        historyP1.clear();
        historyP1.add(4000);
        historyP2.clear();
        historyP2.add(4000);

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        notifyResetables();

        preferences.edit().remove("history1").remove("history2").remove("notes").apply();
        preferences.edit().putInt("p1Life", 4000).apply();
        preferences.edit().putInt("p2Life", 4000).apply();

        countDownTimer.cancel();
    }

    private void loadImage() {
        if (selImg == null) return;
        ImageView imageView = findViewById(R.id.background_image);
        imageView.setImageBitmap(BitmapFactory.decodeFile(selImg));
    }

    private void toHistory() {
        int result = 0;

        for (int llc : lastLifeChangesP1) {
            result += llc;
        }

        if (result != 0)
            historyUpdateP1(player1Life);
        result = 0;

        for (int llc : lastLifeChangesP2) {
            result += llc;
        }

        if (result != 0)
            historyUpdateP2(player2Life);

        lastLifeChangesP1.clear();
        lastLifeChangesP2.clear();
        saveHistories();
        if (historyFragment != null)
            historyFragment.showHistory(historyP1, historyP2);
    }

    public void historyUpdateP1(int history) {
        this.historyP1.add(history);
        preferences.edit().putInt("p1Life", player1Life).apply();
    }

    public void historyUpdateP2(int history) {
        this.historyP2.add(history);
        preferences.edit().putInt("p2Life", player2Life).apply();
    }

    //--------------------interface-methodes-------------------------\\
    @Override
    public void timerStarted(int minutes, final boolean alarmSound) {
        if (timeoutTimer != null)
            timeoutTimer.cancel();
        long timeInMillis = minutes * 60 * 1000;
        timeoutTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                shutUp();
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                timeLeft = " " + String.valueOf(minutes) + " : ";
                if (seconds > 9)
                    timeLeft = timeLeft + String.valueOf(seconds) + " ";
                else
                    timeLeft = timeLeft + "0" + String.valueOf(seconds) + " ";
                timeLeftLong = millisUntilFinished;
                timer.setText(timeLeft);
            }

            @Override
            public void onFinish() {


                String filePath = preferences.getString("soundPath", null);

                if (alarmSound) {
                    mediaPlayer = filePath == null ? MediaPlayer.create(getBaseContext(), R.raw.sound) : MediaPlayer.create(getBaseContext(), Uri.parse(filePath));
                    mediaPlayer.start();
                }
                timer.setText(null);
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, new AlarmFragment()).commit();
            }
        }.start();
    }

    @Override
    public void lifeChanged(int amount, int player) {

        countDownTimer.cancel();

        if (player2isVisible)
            switch (player) {
                case 1:
                    if (!(amount < 0 && player1Life == 0)) {
                        player1Life += amount;
                        lastLifeChangesP1.add(amount);
                    }
                    break;
                case 2:
                    if (!(amount < 0 && player2Life == 0)) {
                        player2Life += amount;
                        lastLifeChangesP2.add(amount);
                    }
                    break;
            }
        else switch (player) {
            case 1:
                player1Life += 100;
                lastLifeChangesP1.add(amount);
                break;
            case 2:
                if (player1Life > 0) {
                    player1Life -= 100;
                    lastLifeChangesP1.add(amount);
                }
                break;
        }

        countDownTimer.start();

        lifeFragment.lifeChanged(player1Life, player2Life);
    }

    @Override
    public void colorChosen(int chosenColor) {
        preferences.edit().putInt("color", chosenColor).apply();
        if (lifeFragment != null)
            lifeFragment.colorChanged(chosenColor);
    }

    @Override
    public void historyFragmentCreated(HistoryFragment historyFragment) {
        this.historyFragment = historyFragment;
        historyFragment.showHistory(historyP1, historyP2);
        historyFragment.setP2Visible(!player2isVisible);
    }

    @Override
    public void lifeFragmentCreated(LifeFragment lifeFragment) {
        this.lifeFragment = lifeFragment;
        lifeFragment.lifeChanged(player1Life, player2Life);
        lifeFragment.onPlayerNumberChanged(!player2isVisible);
    }

    public void registerResetable(Resetable resetable) {
        resetables.add(resetable);
    }

    public void unregisterResetable(Resetable resetable) {
        resetables.remove(resetable);
    }

    private void notifyResetables() {
        for (Resetable resetable : resetables) {
            resetable.reset();
        }
    }

    @Override
    public void shutUp() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    public void saveHistories() {
        StringBuilder sb = new StringBuilder();
        for (int i : historyP1) {
            sb.append(String.valueOf(i));
            sb.append("-");
        }
        preferences.edit().putString(STATE_HISTORY, sb.toString()).apply();
        sb = new StringBuilder();
        for (int i : historyP2) {
            sb.append(String.valueOf(i));
            sb.append("-");
        }
        preferences.edit().putString(STATE_HISTORY_SECOND, sb.toString()).apply();
    }

    public void loadHistories() {
        String pre = preferences.getString(STATE_HISTORY, "4000");
        String h1[] = pre.split("-");
        historyP1.clear();
        for (String s : h1) {
            historyP1.add(Integer.parseInt(s));
        }
        pre = preferences.getString(STATE_HISTORY_SECOND, "4000");
        String h2[] = pre.split("-");
        historyP2.clear();
        for (String s : h2) {
            historyP2.add(Integer.parseInt(s));
        }

    }
}
