package com.hunchback.noahchristmas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private static final int MAX_STREAMS = 100;
    private GameThread gameThread;
    private GameObject santaHead;
    private GameObject santaTorso;
    private GameObject santaLeftArm;
    private GameObject santaRightArm;
    private GameObject santaLeftLeg;
    private GameObject santaRightLeg;
    private double counter;
    private SoundPool soundPool;
    private boolean soundPoolLoaded;
    private int soundIdBackground;
    private int soundIdHoHoHo;

    public GameSurface(Context context) {
        super(context);

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
        initSoundPool();
    }

    private void initSoundPool() {
        // With Android API >= 21.
        if (Build.VERSION.SDK_INT >= 21) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        // With Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When SoundPool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;

                // Playing background sound.
                playSoundBackground();
            }
        });

        // Load the sound background.mp3 into SoundPool
        this.soundIdBackground = this.soundPool.load(this.getContext(), R.raw.wewishyouamerrychristmas, 1);

        // Load the sound explosion.wav into SoundPool
        this.soundIdHoHoHo = this.soundPool.load(this.getContext(), R.raw.hohoho, 1);
    }

    public void playSoundBackground() {
        if (this.soundPoolLoaded) {
            float leftVol = 0.6f;
            float rightVol = 0.6f;
            // Play sound background.mp3
            int streamId = this.soundPool.play(this.soundIdBackground, leftVol, rightVol, 1, -1, 1f);
        }
    }

    public void playSoundHoHoHo() {
        if (this.soundPoolLoaded) {
            float leftVol = 1.0f;
            float rightVol = 1.0f;
            int streamId = this.soundPool.play(this.soundIdHoHoHo, leftVol, rightVol, 1, 0, 0.85f);
        }
    }

    public void update() {
        this.santaHead.update();
        counter += 1.0f;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawARGB(255, 0, 64, 0);
        //draw order is important
        canvas.save();
        canvas.translate(15 * (float) Math.cos(counter/10), 15 * (float) Math.sin(counter/10));
        this.santaRightLeg.draw(canvas);
        this.santaLeftLeg.draw(canvas);
        this.santaLeftArm.draw(canvas);
        this.santaRightArm.draw(canvas);
        this.santaTorso.draw(canvas);
        this.santaHead.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            playSoundHoHoHo();
        }
        return true;
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int surfaceWidth = holder.getSurfaceFrame().width();
        int surfaceHeight = holder.getSurfaceFrame().height();

        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.sc_head);
        this.santaHead = new GameObject(this, bmp, surfaceWidth / 2 - bmp.getWidth() / 2 + 20, (surfaceHeight / 2) - (bmp.getHeight() / 2) - 650);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.sc_torso);
        this.santaTorso = new GameObject(this, bmp, surfaceWidth / 2 - bmp.getWidth() / 2, surfaceHeight / 2 - bmp.getHeight() / 2);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.sc_la);
        this.santaLeftArm = new GameObject(this, bmp, surfaceWidth / 2 - bmp.getWidth() / 2 + 480, (surfaceHeight / 2 - bmp.getHeight() / 2) - 270);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.sc_ra);
        this.santaRightArm = new GameObject(this, bmp, surfaceWidth / 2 - bmp.getWidth() / 2 - 480, (surfaceHeight / 2 - bmp.getHeight() / 2) - 270);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.sc_ll);
        this.santaLeftLeg = new GameObject(this, bmp, surfaceWidth / 2 - bmp.getWidth() / 2 + 450, (surfaceHeight / 2 - bmp.getHeight() / 2) + 500);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.sc_rl);
        this.santaRightLeg = new GameObject(this, bmp, surfaceWidth / 2 - bmp.getWidth() / 2 - 450, (surfaceHeight / 2 - bmp.getHeight() / 2) + 500);

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                this.gameThread.setRunning(false);
                this.soundPool.unload(this.soundIdBackground);
                this.soundPool.unload(this.soundIdHoHoHo);
                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.soundPool.release();
            }
            retry = true;
        }
    }

}