package apv.congnt24.media;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.IOException;

/**
 * Created by congn_000 on 11/4/2015.
 * Create simple base class for play audio. concreate need to extends to change
 */
public abstract class BaseAudioPlayer implements AudioManager.OnAudioFocusChangeListener {
    private MediaMetadataRetriever metadataRetriever;
    private MediaPlayer mediaPlayer;
    public static final int NORMAL = 0;
    public static final int REPEATE_TRACK = 1;
    public static final int REPEATE_PLAYLIST = 2;
    public static final int REPEATE_RANDOM = 3;

    protected int repeateMode = 0;
    private Context context;

    protected BaseAudioPlayer() {
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onFinishTrack();
            }
        });
    }
    public BaseAudioPlayer initialize(Context context){
        this.context = context;
        return this;
    }
    public BaseAudioPlayer setAudio(String path){
        if (mediaPlayer==null) {
            return null;
        }
        try {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    public BaseAudioPlayer setAudio(Context context, Uri uri){
        if (mediaPlayer==null)
            return null;
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    //We can get real path fromurl
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.MediaColumns.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            int column_index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int getRepeateMode() {
        return repeateMode;
    }

    //---------------------Play pause----------------------
    public void play(){
        if (mediaPlayer==null) {
            throw new IllegalStateException("MediaPlayer cannot be null");
        }
        if (mediaPlayer.isPlaying())
            return;
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

    // Request audio focus for playback
        int result = am.requestAudioFocus(this,
    // Use the music stream.
                AudioManager.STREAM_MUSIC,
    // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
    // other app had stopped playing song now , so u can do u stuff now .
        }
        mediaPlayer.start();
        setPausable();
    }


    public void pause(){
        if (mediaPlayer==null){
            return;
        }
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            setPlayable();
        }else{
            setPlayable();
        }
    }
    //Release mediaPlayer
    public void release(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    protected abstract void onFinishTrack();
    protected abstract void setPausable();
    protected abstract void setPlayable();

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    public boolean isLooping(){
        return mediaPlayer.isLooping();
    }
    public int getCurrent(){
        return mediaPlayer.getCurrentPosition();
    }
    public void seekTo(int post){
        mediaPlayer.seekTo(post);
    }
    @Override
    public void onAudioFocusChange(int focusChange) {
        //When another app play
        switch (focusChange)
        {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mediaPlayer.start();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mediaPlayer.pause();// Pause your media player here
                break;
        }
    }
}
