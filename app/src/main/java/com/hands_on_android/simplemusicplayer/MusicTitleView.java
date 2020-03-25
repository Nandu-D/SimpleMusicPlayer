package com.hands_on_android.simplemusicplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MusicTitleView extends FrameLayout {

    // Custom View for showing song list in MainActivity.
    public interface MusicSelectedListener {
        void onMusicSelected(Music music);
    }

    private TextView title;
    private MusicSelectedListener onMusicSelectedListener;
    private Music music;

    public MusicTitleView(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    public MusicTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public MusicTitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_music_title, this, true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelOffset(R.dimen.music_title_margin);
        params.setMargins(margin, 0, margin, margin / 2);
        setLayoutParams(params);
        title = findViewById(R.id.music_title);
        setOnClickListener(v -> {
            if (onMusicSelectedListener != null) {
                onMusicSelectedListener.onMusicSelected(music);
            }
        });
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
        title.setText(music.getSongInfo());
    }

    public void setOnMusicSelectedListener(MusicSelectedListener onMusicSelectedListener) {
        this.onMusicSelectedListener = onMusicSelectedListener;
    }
}
