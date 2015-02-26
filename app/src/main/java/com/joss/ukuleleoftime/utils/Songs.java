package com.joss.ukuleleoftime.utils;

import android.util.SparseArray;

import com.joss.ukuleleoftime.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: jossayjacobo
 * Date: 2/24/15
 * Time: 4:20 PM
 */
public class Songs {

    public static final String TAG = Songs.class.getSimpleName();

    private static final SparseArray<Integer> NOTES;
    private static final int A         = 1; // 0001;
    private static final int C_DOWN    = 2; // 0010;
    private static final int C_RIGHT   = 3; // 0011;
    private static final int C_UP      = 4; // 0100;
    private static final int C_LEFT    = 5; // 0101;

    static {
        NOTES = new SparseArray<>();
        NOTES.put(R.id.a, A);
        NOTES.put(R.id.c_down, C_DOWN);
        NOTES.put(R.id.c_right, C_RIGHT);
        NOTES.put(R.id.c_up, C_UP);
        NOTES.put(R.id.c_left, C_LEFT);
    }

    public List<Integer> ZELDAS_LULLABY;
    public List<Integer> ARIAS_SONG;
    public List<Integer> EPONAS_SONG;
    public List<Integer> SUNS_SONG;
    public List<Integer> SONG_OF_TIME;
    public List<Integer> SONG_OF_STORMS;
    public List<Integer> MINUET_OF_THE_FOREST;
    public List<Integer> BOLERO_OF_FIRE;
    public List<Integer> SERENADE_OF_WATER;
    public List<Integer> NOCTURNE_OF_SHADOW;
    public List<Integer> REQUIEM_OF_SPIRIT;
    public List<Integer> PRELUDE_OF_LIGHT;

    public Songs() {
        ZELDAS_LULLABY = new ArrayList<>();
        ZELDAS_LULLABY.add(R.id.c_left);
        ZELDAS_LULLABY.add(R.id.c_up);
        ZELDAS_LULLABY.add(R.id.c_right);
        ZELDAS_LULLABY.add(R.id.c_left);
        ZELDAS_LULLABY.add(R.id.c_up);
        ZELDAS_LULLABY.add(R.id.c_right);

        ARIAS_SONG = new ArrayList<>();
        ARIAS_SONG.add(R.id.c_down);
        ARIAS_SONG.add(R.id.c_right);
        ARIAS_SONG.add(R.id.c_left);
        ARIAS_SONG.add(R.id.c_down);
        ARIAS_SONG.add(R.id.c_right);
        ARIAS_SONG.add(R.id.c_left);

        EPONAS_SONG = new ArrayList<>();
        EPONAS_SONG.add(R.id.c_up);
        EPONAS_SONG.add(R.id.c_left);
        EPONAS_SONG.add(R.id.c_right);
        EPONAS_SONG.add(R.id.c_up);
        EPONAS_SONG.add(R.id.c_left);
        EPONAS_SONG.add(R.id.c_right);

        SUNS_SONG = new ArrayList<>();
        SUNS_SONG.add(R.id.c_right);
        SUNS_SONG.add(R.id.c_down);
        SUNS_SONG.add(R.id.c_up);
        SUNS_SONG.add(R.id.c_right);
        SUNS_SONG.add(R.id.c_down);
        SUNS_SONG.add(R.id.c_up);

        SONG_OF_TIME = new ArrayList<>();
        SONG_OF_TIME.add(R.id.c_right);
        SONG_OF_TIME.add(R.id.a);
        SONG_OF_TIME.add(R.id.c_down);
        SONG_OF_TIME.add(R.id.c_right);
        SONG_OF_TIME.add(R.id.a);
        SONG_OF_TIME.add(R.id.c_down);

        SONG_OF_STORMS = new ArrayList<>();
        SONG_OF_STORMS.add(R.id.a);
        SONG_OF_STORMS.add(R.id.c_down);
        SONG_OF_STORMS.add(R.id.c_up);
        SONG_OF_STORMS.add(R.id.a);
        SONG_OF_STORMS.add(R.id.c_down);
        SONG_OF_STORMS.add(R.id.c_up);

        MINUET_OF_THE_FOREST = new ArrayList<>();
        MINUET_OF_THE_FOREST.add(R.id.a);
        MINUET_OF_THE_FOREST.add(R.id.c_up);
        MINUET_OF_THE_FOREST.add(R.id.c_left);
        MINUET_OF_THE_FOREST.add(R.id.c_right);
        MINUET_OF_THE_FOREST.add(R.id.c_left);
        MINUET_OF_THE_FOREST.add(R.id.c_right);

        BOLERO_OF_FIRE = new ArrayList<>();
        BOLERO_OF_FIRE.add(R.id.c_down);
        BOLERO_OF_FIRE.add(R.id.a);
        BOLERO_OF_FIRE.add(R.id.c_down);
        BOLERO_OF_FIRE.add(R.id.a);
        BOLERO_OF_FIRE.add(R.id.c_right);
        BOLERO_OF_FIRE.add(R.id.c_down);
        BOLERO_OF_FIRE.add(R.id.c_right);
        BOLERO_OF_FIRE.add(R.id.c_down);

        SERENADE_OF_WATER = new ArrayList<>();
        SERENADE_OF_WATER.add(R.id.a);
        SERENADE_OF_WATER.add(R.id.c_down);
        SERENADE_OF_WATER.add(R.id.c_right);
        SERENADE_OF_WATER.add(R.id.c_right);
        SERENADE_OF_WATER.add(R.id.c_left);

        NOCTURNE_OF_SHADOW = new ArrayList<>();
        NOCTURNE_OF_SHADOW.add(R.id.c_left);
        NOCTURNE_OF_SHADOW.add(R.id.c_right);
        NOCTURNE_OF_SHADOW.add(R.id.c_right);
        NOCTURNE_OF_SHADOW.add(R.id.a);
        NOCTURNE_OF_SHADOW.add(R.id.c_left);
        NOCTURNE_OF_SHADOW.add(R.id.c_right);
        NOCTURNE_OF_SHADOW.add(R.id.c_down);

        REQUIEM_OF_SPIRIT = new ArrayList<>();
        REQUIEM_OF_SPIRIT.add(R.id.a);
        REQUIEM_OF_SPIRIT.add(R.id.c_down);
        REQUIEM_OF_SPIRIT.add(R.id.a);
        REQUIEM_OF_SPIRIT.add(R.id.c_right);
        REQUIEM_OF_SPIRIT.add(R.id.c_down);
        REQUIEM_OF_SPIRIT.add(R.id.a);

        PRELUDE_OF_LIGHT = new ArrayList<>();
        PRELUDE_OF_LIGHT.add(R.id.c_up);
        PRELUDE_OF_LIGHT.add(R.id.c_right);
        PRELUDE_OF_LIGHT.add(R.id.c_up);
        PRELUDE_OF_LIGHT.add(R.id.c_right);
        PRELUDE_OF_LIGHT.add(R.id.c_left);
        PRELUDE_OF_LIGHT.add(R.id.c_up);
    }

    public static boolean compareNotes(List<Integer> notes, List<Integer> song) {
        if (notes.size() != song.size()) {
            return false;
        }

        for (int i = 0; i < notes.size(); i++) {
            if (!notes.get(i).equals(song.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static byte[] songToByteArray(List<Integer> song){
        int half = song.size() /2;
        byte[] songBytes = new byte[song.size() % 2 == 0 ? half : half + 1];

        int arrayIndex = 0;
        int i = 0;
        while(i < song.size()){

            int note1 = NOTES.get(song.get(i)) << 4;
            int note2 = ++i < song.size() ? NOTES.get(song.get(i)) : 0;

            songBytes[arrayIndex] = (byte) (note1 | note2);

            arrayIndex++;
            i++;
        }
        return songBytes;
    }

}
