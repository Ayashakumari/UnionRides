package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;


/**
 * <p>ç»Ÿä¸€ç®¡ç�†MediaPlayerçš„åœ°æ–¹,å�ªæœ‰ä¸€ä¸ªmediaPlayerå®žä¾‹ï¼Œé‚£ä¹ˆä¸�ä¼šæœ‰å¤šä¸ªè§†é¢‘å�Œæ—¶æ’­æ”¾ï¼Œä¹ŸèŠ‚çœ�èµ„æº�ã€‚</p>
 * <p>Unified management MediaPlayer place, there is only one MediaPlayer instance, then there will be no more video broadcast at the same time, also save resources.</p>
 * Created by Nathen
 * On 2015/11/30 15:39
 */
public class JCMediaManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;
    private static JCMediaManager jcMediaManager;
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;
    public JCMediaPlayerListener listener;
    public JCMediaPlayerListener lastListener;
    public int lastState;

    public static JCMediaManager intance() {
        if (jcMediaManager == null) {
            jcMediaManager = new JCMediaManager();
        }
        return jcMediaManager;
    }

    public JCMediaManager() {
    	try{
        mediaPlayer = new MediaPlayer();
    	}
    	catch(Exception e){
    		
    	}
    }

    public void prepareToPlay(Context context, String url) {
        if (TextUtils.isEmpty(url)) return;
        try {
            currentVideoWidth = 0;
            currentVideoHeight = 0;
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, Uri.parse(url));
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (listener != null) {
            listener.onPrepared();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (listener != null) {
            listener.onAutoCompletion();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (listener != null) {
            listener.onBufferingUpdate(percent);
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (listener != null) {
            listener.onSeekComplete();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (listener != null) {
            listener.onError(what, extra);
        }
        return true;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        currentVideoWidth = mp.getVideoWidth();
        currentVideoHeight = mp.getVideoHeight();
        if (listener != null) {
            listener.onVideoSizeChanged();
        }
    }

    interface JCMediaPlayerListener {
        void onPrepared();

        void onAutoCompletion();

        void onCompletion();

        void onBufferingUpdate(int percent);

        void onSeekComplete();

        void onError(int what, int extra);

        void onVideoSizeChanged();

        void onBackFullscreen();
    }
}
