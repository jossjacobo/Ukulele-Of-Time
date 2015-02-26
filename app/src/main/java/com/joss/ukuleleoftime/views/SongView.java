package com.joss.ukuleleoftime.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joss.ukuleleoftime.R;
import com.joss.ukuleleoftime.models.SongModel;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by: jossayjacobo
 * Date: 2/26/15
 * Time: 3:23 PM
 */
public class SongView extends LinearLayout {

    @InjectView(R.id.song_name)
    TextView name;
    @InjectView(R.id.song_image)
    ImageView image;

    public SongView(Context context) {
        this(context, null);
    }

    public SongView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SongView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.li_song, this, true);
        ButterKnife.inject(this);
    }

    public void setContent(SongModel songModel){
        name.setText(songModel.title);
        image.setImageResource(songModel.imageResource);
    }
}
