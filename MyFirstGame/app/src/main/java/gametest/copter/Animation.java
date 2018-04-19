package gametest.copter;


import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] frames;
    private int currentframe;
    private long startTime;
    private long delay;
    private boolean playedonce;

    public void setFrames(Bitmap[] frames)
    {
        this.frames=frames;
        currentframe=0;
        startTime=System.nanoTime();
    }

    public void setDelay(long d){delay=d;}
    public void setFrame(int i){currentframe=i;}

    public void update()
    {
        long elapsed=(System.nanoTime()-startTime)/1000000;

        if(elapsed>delay)
        {
            currentframe++;
            startTime=System.nanoTime();
        }

        if(currentframe==frames.length){
            currentframe=0;
            playedonce=true;
        }
    }

    public Bitmap getImage()
    {
        return frames[currentframe];
    }

    public int getFrame()
    {
        return currentframe;
    }

    public boolean playedOnce()
    {
        return playedonce;
    }

}
