package com.fredericlavigne.somusic.services.stats;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.ektorp.ComplexKey;

import com.fredericlavigne.somusic.common.model.Song;
import com.google.gson.annotations.Expose;

@SuppressWarnings("serial")
public class StatEntry extends Song {

  String type;

  Date date;

  @Expose
  int count;

  public static enum StatType {
    SONG, ARTIST, ALBUM
  }

  StatType statType;

  public StatType getStatType() {
    return statType;
  }

  public void setStatType(StatType statType) {
    this.statType = statType;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public static StatEntry dailyEntryFor(Song song) {
    StatEntry entry = new StatEntry();
    entry.setAlbum(song.getAlbum());
    entry.setArtist(song.getArtist());

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendar.setTime(song.getCreatedAt());
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    entry.setCreatedAt(calendar.getTime());

    entry.setImage(song.getImage());
    entry.setLink(song.getLink());
    entry.setSourceId(song.getSourceId());
    entry.setTitle(song.getTitle());

    return entry;
  }

  public ComplexKey key() {
    return ComplexKey.of(getCreatedAt(), getArtist(), getAlbum(), getTitle());
  }

  public void increment() {
    count++;
  }

}
