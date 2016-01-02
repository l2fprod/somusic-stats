package com.fred.somusic.services.stats;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fred.somusic.common.BaseTask;
import com.fred.somusic.common.model.Song;
import com.fred.somusic.common.model.Song.Status;
import com.fred.somusic.common.utils.Log;

@Component
public class StatsTask extends BaseTask {

  @Scheduled(fixedDelay = 5 * 60 * 1000)
  public void stats() {
    // find all items not processed
    CouchDbConnector songDb = getSongDb();
    CouchDbConnector statsDb = getStatsDb();

    ViewQuery processed = new ViewQuery().designDocId("_design/songs").viewName("by_state").key(Status.IMAGE_FOUND)
        .includeDocs(true).limit(1000);
    List<Song> songs = songDb.queryView(processed, Song.class);
    Log.info("stats", "Found " + songs.size() + " songs to process");
    int index = 0;
    int count = songs.size();
    for (Song song : songs) {
      index++;
      Log.info("stats", "Processing [" + index + "/" + count + "] " + song.getLink());
      Calendar statDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      statDate.setTime(song.getCreatedAt());

      // reset all field to count daily stats
      StatEntry entry = StatEntry.dailyEntryFor(song);

      ViewQuery statQuery = new ViewQuery().designDocId("_design/stats").viewName("by_source_id").key(entry.key())
          .includeDocs(true);
      List<StatEntry> stats = statsDb.queryView(statQuery, StatEntry.class);
      // increment the count
      if (stats.isEmpty()) {
        entry.increment();
        statsDb.create(entry);
        Log.info("stats", "New entry");
      } else {
        entry = stats.get(0);
        entry.increment();
        statsDb.update(entry);
        Log.info("stats", "Incremented");
      }
      song.setState(Status.STATS_UPDATED);
      songDb.update(song);
    }
  }
}
