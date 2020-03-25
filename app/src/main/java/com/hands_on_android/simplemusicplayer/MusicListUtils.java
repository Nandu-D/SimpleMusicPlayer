package com.hands_on_android.simplemusicplayer;

import java.util.ArrayList;

public class MusicListUtils {
    private static final String[] SONG_TITLES = {"Allemande (Sting)", "Bomber (Sting)", "Detour (Sting)", "Donations (Sting)", "Ersatz Bossa (Sting)"};
    private static final String[] ARTIST_LIST = {"Wahneta Meixsell", "Riot", "Gunnar Olsen", "Riot", "John Deley and the 41 Players"};
    private static final int[] SONG_RES = {R.raw.allemande_sting, R.raw.bomber_sting, R.raw.detour_sting, R.raw.donations_sting, R.raw.ersatz_bossa_sting};

    private static MusicListUtils INSTANCE;


    private ArrayList<Music> musicList = new ArrayList<>();
    private Music currentMusic;

    private MusicListUtils() {
        for (int i = 0; i < SONG_TITLES.length; i++) {
            musicList.add(new Music(SONG_TITLES[i], ARTIST_LIST[i], SONG_RES[i]));
        }
        currentMusic = musicList.get(0);
    }

    public static void initialize() {
        INSTANCE = new MusicListUtils();
    }

    public static MusicListUtils getInstance() {
        return INSTANCE;
    }

    public ArrayList<Music> getMusicList() {
        return musicList;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
    }

    public void setNext() {
        int currentIndex = musicList.indexOf(currentMusic);
        if (currentIndex == SONG_TITLES.length - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        currentMusic = musicList.get(currentIndex);
    }

    public void setPrevious() {
        int currentIndex = musicList.indexOf(currentMusic);
        if (currentIndex == 0) {
            currentIndex = SONG_TITLES.length - 1;
        } else {
            currentIndex--;
        }
        currentMusic = musicList.get(currentIndex);
    }
}
