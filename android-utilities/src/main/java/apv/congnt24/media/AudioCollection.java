package apv.congnt24.media;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congn_000 on 11/5/2015.
 */
public class AudioCollection {
    ArrayList<AudioObject> audioList = new ArrayList<>();
    private static AudioCollection ourInstance = new AudioCollection();

    public static AudioCollection getInstance() {
        return ourInstance;
    }
    private AudioCollection() {
    }
    public AudioCollection init(Context context){
        audioList.clear();
        queryAllAudio(context);
        return this;
    }
    public List<AudioObject> getListAudio(){
        return audioList;
    }
    private void queryAllAudio(Context context) {

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,     // context id/ uri id of the file
        };

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);

        // the last parameter sorts the data alphanumerically
        if (cursor != null) {
            while (cursor.moveToNext()) {
                audioList.add(new AudioObject(cursor));
            }
        }
    }
}
