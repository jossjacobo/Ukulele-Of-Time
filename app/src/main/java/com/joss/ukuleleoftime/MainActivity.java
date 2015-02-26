package com.joss.ukuleleoftime;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joss.ukuleleoftime.adapters.SongAdapter;
import com.joss.ukuleleoftime.ble.BleService;
import com.joss.ukuleleoftime.utils.Songs;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements View.OnTouchListener,
        SoundPool.OnLoadCompleteListener, View.OnClickListener, ServiceConnection {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 456;
    private static final int INCORRECT = 0;
    private static final int CORRECT = 1;
    private static final int CLICK1 = 2;
    private static final int OPEN = 3;
    private static final int CLOSE = 4;
    private static final int HEY = 5;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.a)
    FloatingActionButton a;
    @InjectView(R.id.b)
    FloatingActionButton b;
    @InjectView(R.id.c_down)
    FloatingActionButton cDown;
    @InjectView(R.id.c_right)
    FloatingActionButton cRight;
    @InjectView(R.id.c_up)
    FloatingActionButton cUp;
    @InjectView(R.id.c_left)
    FloatingActionButton cLeft;
    @InjectView(R.id.notes)
    TextView notesText;
    @InjectView(R.id.progress)
    LinearLayout progressBar;
    @InjectView(R.id.circle)
    ImageView circle;
    @InjectView(R.id.bluetooth_text)
    TextView bluetoothText;

    SoundPool soundPool;
    SparseArray<Integer> sounds;

    MediaPlayer mediaPlayer;

    int loadedSounds = 0;
    boolean soundsLoaded = false;
    Songs songs;

    List<Integer> notes;

    int aPlayingID;
    int cDownPlayingID;
    int cRightPlayingID;
    int cUpPlayingID;
    int cLeftPlayingID;

    BleService bleService;
    LocalBroadcastManager broadcastManager;
    BleReceiver bleReceiver;
    boolean showingBluetoothDialog = false;

    int incorrectAttemptsInARow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        songs = new Songs();
        notes = new ArrayList<>();

        b.setOnClickListener(this);
        a.setOnTouchListener(this);
        cDown.setOnTouchListener(this);
        cRight.setOnTouchListener(this);
        cUp.setOnTouchListener(this);
        cLeft.setOnTouchListener(this);

        broadcastManager = LocalBroadcastManager.getInstance(this);
        bleReceiver = new BleReceiver();

        if(DataStore.getFirstLaunch(this)){
            DataStore.persistFirstLaunch(this, false);
            showInstructionsDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgress();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build())
                .build();
        soundPool.setOnLoadCompleteListener(this);
        loadSounds(soundPool);

        broadcastManager.registerReceiver(bleReceiver, new IntentFilter(BleService.BROADCAST));

        Intent intent= new Intent(this, BleService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

        updateBluetoothText();
    }

    @Override
    public void onPause() {
        super.onPause();
        soundsLoaded = false;
        soundPool.release();
        soundPool = null;
        broadcastManager.unregisterReceiver(bleReceiver);

        if(bleService != null){
            bleService.stopAdvertising();
            unbindService(this);
        }
    }

    private void loadSounds(SoundPool soundPool) {
        loadedSounds = 0;
        showProgress();
        sounds = new SparseArray<>();
        sounds.put(R.id.a, soundPool.load(this, R.raw.a_d_med, 1));
        sounds.put(R.id.c_down, soundPool.load(this, R.raw.c_down_f_med, 1));
        sounds.put(R.id.c_right, soundPool.load(this, R.raw.c_right_a_med, 1));
        sounds.put(R.id.c_up, soundPool.load(this, R.raw.c_up_d2_med, 1));
        sounds.put(R.id.c_left, soundPool.load(this, R.raw.c_left_b_med, 1));
        sounds.put(CORRECT, soundPool.load(this, R.raw.song_correct, 1));
        sounds.put(INCORRECT, soundPool.load(this, R.raw.song_wrong, 1));
        sounds.put(CLICK1, soundPool.load(this, R.raw.click, 1));
        sounds.put(OPEN, soundPool.load(this, R.raw.menu_open, 1));
        sounds.put(CLOSE, soundPool.load(this, R.raw.menu_close, 1));
        sounds.put(HEY, soundPool.load(this, R.raw.hey_listen, 1));
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        loadedSounds++;
        if(loadedSounds == sounds.size()) {
            soundsLoaded = true;
            hideProgress();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b:
                playSound(CLICK1);
                notes.clear();
                notesText.setText("");
                stopAndReleaseMediaPlayer(mediaPlayer);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.a:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        aPlayingID = playSound(R.id.a);
                        addNote(R.id.a);
                        break;

                    case MotionEvent.ACTION_UP:
                        stopSound(aPlayingID);
                        break;
                }
                break;

            case R.id.c_down:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cDownPlayingID = playSound(R.id.c_down);
                        addNote(R.id.c_down);
                        break;

                    case MotionEvent.ACTION_UP:
                        stopSound(cDownPlayingID);
                        break;
                }
                break;

            case R.id.c_right:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cRightPlayingID = playSound(R.id.c_right);
                        addNote(R.id.c_right);
                        break;

                    case MotionEvent.ACTION_UP:
                        stopSound(cRightPlayingID);
                        break;
                }
                break;

            case R.id.c_up:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cUpPlayingID = playSound(R.id.c_up);
                        addNote(R.id.c_up);
                        break;

                    case MotionEvent.ACTION_UP:
                        stopSound(cUpPlayingID);
                        break;
                }
                break;

            case R.id.c_left:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cLeftPlayingID = playSound(R.id.c_left);
                        addNote(R.id.c_left);
                        break;

                    case MotionEvent.ACTION_UP:
                        stopSound(cLeftPlayingID);
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        playSound(CLICK1);

        switch (item.getItemId()){
            case R.id.action_start_ble_adv:
                DataStore.persistBroadcastBleService(this, true);
                updateBluetoothText();
                checkStartBleAdvertising(notes);
                return true;

            case R.id.action_stop_ble_adv:
                DataStore.persistBroadcastBleService(this, false);
                updateBluetoothText();
                stopAdvertising();
                return true;

            case R.id.action_song_list:
                showYouSuckDialog();
                break;

            case R.id.action_instructions:
                showInstructionsDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        BleService.UkuleleBinder b = (BleService.UkuleleBinder) service;
        bleService = b.getService();
        bleService.initialize();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bleService = null;
    }

    private class BleReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int action_type  = intent.getIntExtra(BleService.ACTION_ID, -1);
            switch (action_type){
                case BleService.START_ADVERTISING:
                    if(DataStore.getBroadcastBleService(MainActivity.this))
                        circle.setImageResource(R.drawable.circle_blue);
                    break;

                case BleService.STOP_ADVERTISING:
                    circle.setImageResource(R.drawable.circle_white);
                    break;
            }

        }

    }

    private void addNote(int id) {
        addText(id);
        notes.add(id);
        checkSong();

        if (notes.size() == 8) {
            notes.clear();
            notesText.setText("");
            playSound(INCORRECT);
            incorrectAttemptsInARow++;

            if(incorrectAttemptsInARow == 3){
                showYouSuckDialog();
                incorrectAttemptsInARow = 0;
            }
        }
    }

    private void checkSong() {
        if (soundsLoaded) {
            if (Songs.compareNotes(notes, songs.ZELDAS_LULLABY)) {
                playSong(R.raw.music__zelda_lullaby, notes);
            } else if (Songs.compareNotes(notes, songs.ARIAS_SONG)) {
                playSong(R.raw.music__saria_song, notes);
            } else if (Songs.compareNotes(notes, songs.EPONAS_SONG)) {
                playSong(R.raw.music__epona_song, notes);
            } else if (Songs.compareNotes(notes, songs.SUNS_SONG)) {
                playSong(R.raw.music__sun_song, notes);
            } else if (Songs.compareNotes(notes, songs.SONG_OF_TIME)) {
                playSong(R.raw.music__song_of_time, notes);
            } else if (Songs.compareNotes(notes, songs.SONG_OF_STORMS)) {
                playSong(R.raw.music__song_of_storms, notes);
            } else if (Songs.compareNotes(notes, songs.MINUET_OF_THE_FOREST)) {
                playSong(R.raw.music__minuet_of_forest, notes);
            } else if (Songs.compareNotes(notes, songs.BOLERO_OF_FIRE)) {
                playSong(R.raw.music__bolero_of_fire, notes);
            } else if (Songs.compareNotes(notes, songs.SERENADE_OF_WATER)) {
                playSong(R.raw.music__serenade_of_water, notes);
            } else if (Songs.compareNotes(notes, songs.NOCTURNE_OF_SHADOW)) {
                playSong(R.raw.music__nocturne_of_shadow, notes);
            } else if (Songs.compareNotes(notes, songs.REQUIEM_OF_SPIRIT)) {
                playSong(R.raw.music__requiem_of_spirit, notes);
            } else if (Songs.compareNotes(notes, songs.PRELUDE_OF_LIGHT)) {
                playSong(R.raw.music__prelude_of_light, notes);
            }
        }
    }

    private void playSong(int song, List<Integer> notes) {
        incorrectAttemptsInARow = 0;

        if(DataStore.getBroadcastBleService(this))
            checkStartBleAdvertising(notes);

        playSound(CORRECT);
        notes.clear();
        notesText.setText("");

        stopAndReleaseMediaPlayer(mediaPlayer);
        mediaPlayer = MediaPlayer.create(this, song);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAndReleaseMediaPlayer(mp);
            }
        });
        mediaPlayer.start();
    }

    private void addText(int id) {
        switch (id) {
            case R.id.a:
                notesText.setText(notesText.getText().toString() + " " + "A,");
                break;

            case R.id.c_down:
                notesText.setText(notesText.getText().toString() + " " + "C DOWN,");
                break;

            case R.id.c_right:
                notesText.setText(notesText.getText().toString() + " " + "C RIGHT,");
                break;

            case R.id.c_up:
                notesText.setText(notesText.getText().toString() + " " + "C UP,");
                break;

            case R.id.c_left:
                notesText.setText(notesText.getText().toString() + " " + "C LEFT,");
                break;
        }
    }

    private void stopAndReleaseMediaPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void checkStartBleAdvertising(List<Integer> notes) {
        if(bleService != null && !bleService.isBluetoothEnabled()){
            if(!showingBluetoothDialog){
                showingBluetoothDialog = true;
                showBluetoothDialog();
            }
        }else{
            setAdvertisingData(notes);
            startAdvertising();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED){
            Toast.makeText(this, getString(R.string.bluetooth_not_enabled), Toast.LENGTH_SHORT).show();
        }else{
            bleService.initialize();
            startAdvertising();
        }
    }

    private void showBluetoothDialog() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.bluetooth))
                .content(getString(R.string.bluetooth_message))
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no))
                .titleColor(getResources().getColor(R.color.colorPrimary))
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        playSound(CLICK1);

                        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                        playSound(CLICK1);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        showingBluetoothDialog = false;
                        playSound(CLOSE);
                    }
                })
                .show();
        playSound(OPEN);
    }

    private void showYouSuckDialog() {
        ListView listView = new ListView(this);
        listView.setAdapter(new SongAdapter(this));
        new MaterialDialog.Builder(this)
                .title(getString(R.string.hey_listen))
                .customView(listView, false)
                .positiveText(getString(R.string.okay))
                .titleColor(getResources().getColor(R.color.colorPrimary))
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        playSound(CLICK1);
                        dialog.dismiss();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        playSound(CLOSE);
                    }
                })
                .show();
        playSound(HEY);
        playSound(OPEN);
    }

    private void showInstructionsDialog() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.instructions_title))
                .content(getString(R.string.instructions))
                .positiveText(getString(R.string.instructions_okay))
                .titleColor(getResources().getColor(R.color.colorPrimary))
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        playSound(CLICK1);
                        dialog.dismiss();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        playSound(CLOSE);
                    }
                })
                .show();
        playSound(OPEN);
    }

    private int playSound(int id){
        if(soundsLoaded)
            return soundPool.play(sounds.get(id), 1f, 1f, 1, 1, 1f);
        return 0;
    }

    private void stopSound(int id){
        if (soundsLoaded)
            soundPool.stop(id);
    }

    public void startAdvertising(){
        if(bleService != null){
            bleService.startAdvertising();
        }
    }

    private void stopAdvertising(){
        if(bleService != null){
            bleService.stopAdvertising();
        }
    }

    private void setAdvertisingData(List<Integer> song){
        if(bleService != null)
            bleService.setData(Songs.songToByteArray(song));
    }

    private void updateBluetoothText() {
        bluetoothText.setText(DataStore.getBroadcastBleService(this)
                ? getString(R.string.bluetooth_advertising_on)
                : getString(R.string.bluetooth_advertising_off));
    }

}
