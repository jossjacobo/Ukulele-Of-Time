package com.joss.ukuleleoftime.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.joss.ukuleleoftime.R;
import com.joss.ukuleleoftime.models.SongModel;
import com.joss.ukuleleoftime.views.SongView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: jossayjacobo
 * Date: 2/26/15
 * Time: 3:29 PM
 */
public class SongAdapter extends BaseAdapter {

    Context context;
    List<SongModel> items;

    public SongAdapter(Context context){
        this.context = context;
        this.items = getSongs(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongView songView = convertView == null
                ? new SongView(context)
                : (SongView) convertView;
        songView.setContent(items.get(position));
        return songView;
    }

    public static List<SongModel> getSongs(Context context) {
        List<SongModel> items = new ArrayList<>();
        items.add(new SongModel(context.getString(R.string.song_zeldas_lullaby), R.mipmap.zeldas_lullaby));
        items.add(new SongModel(context.getString(R.string.song_eponas), R.mipmap.eponas_song));
        items.add(new SongModel(context.getString(R.string.song_sarias), R.mipmap.sarias_song));
        items.add(new SongModel(context.getString(R.string.song_suns), R.mipmap.suns_song));
        items.add(new SongModel(context.getString(R.string.song_of_time), R.mipmap.song_of_time));
        items.add(new SongModel(context.getString(R.string.song_of_storm), R.mipmap.song_of_storms));
        items.add(new SongModel(context.getString(R.string.song_minuet_of_forest), R.mipmap.minuet_of_forest));
        items.add(new SongModel(context.getString(R.string.song_bolero_of_fire), R.mipmap.bolero_of_fire));
        items.add(new SongModel(context.getString(R.string.song_serenade_of_water), R.mipmap.serenade_of_water));
        items.add(new SongModel(context.getString(R.string.song_requiem_of_spirit), R.mipmap.requiem_of_spirit));
        items.add(new SongModel(context.getString(R.string.song_nocturne_of_shadow), R.mipmap.nocturne_of_shadow));
        items.add(new SongModel(context.getString(R.string.song_prelude_of_light), R.mipmap.prelude_of_light));
        return items;
    }
}
