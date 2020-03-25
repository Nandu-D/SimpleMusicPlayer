package com.hands_on_android.simplemusicplayer;

public class Music {
    private String title;
    private String artist;
    private int songRes;

    // A Model class that contains information about a music.
    public Music(String title, String artist, int songRes) {
        this.title = title;
        this.artist = artist;
        this.songRes = songRes;
    }

    public String getSongInfo() {
        return title + " - " + artist;
    }

    public int getSongRes() {
        return songRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Music music = (Music) o;

        if (!title.equals(music.title)) return false;
        return artist.equals(music.artist);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + artist.hashCode();
        return result;
    }
}
