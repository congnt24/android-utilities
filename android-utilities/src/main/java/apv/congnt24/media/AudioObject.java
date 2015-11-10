package apv.congnt24.media;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

/**
 * Created by congn_000 on 11/5/2015.
 */
public class AudioObject {

    private String queryTitle;
    private String queryAlbum;
    private String queryArtist;
    private String queryDuration;
    private String filepath;
    private String _id;
    private Bitmap cover = null;

    public AudioObject(Cursor cursor) {
        queryTitle = cursor.getString(0);
        queryArtist = cursor.getString(1);
        queryAlbum = cursor.getString(2);
        queryDuration = cursor.getString(3);
        filepath = cursor.getString(4);
        _id = cursor.getString(5);
    }

    public String getQueryTitle() {
        return queryTitle;
    }

    public String getQueryAlbum() {
        return queryAlbum;
    }

    public String getQueryArtist() {
        return queryArtist;
    }

    public String getQueryDuration() {
        return queryDuration;
    }

    public String getFilepath() {
        return filepath;
    }

    public String get_id() {
        return _id;
    }

    public Bitmap getCover() {
        return cover;
    }
}
