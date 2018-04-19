package gametest.copter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH=856;
    public static final int HEIGHT=480;
    public static final int MOVESPEED=-5;
    private long smokeStartTime;
    private long missileStartTime;
    private MainThread mainThread;
    private Background background;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missile;
    private ArrayList<TopBorder> topBorders;
    private ArrayList<BottomBorder> bottomBorders;
    private Random rand=new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown=true;
    private boolean bottomDown=true;
    private boolean newGameCreated;
    SharedPreferences pref;

    //difficulty progression,increase to slow difficulty progresssion
    private int progressDenom=20;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;
    private int best;

    public GamePanel(Context context,SharedPreferences pref)
    {
        super(context);

        this.pref=pref;
        getHolder().addCallback(this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        background=new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        player=new Player(BitmapFactory.decodeResource(getResources(),R.drawable.helicopter),65,25,3);
        smoke=new ArrayList<Smokepuff>();
        missile=new ArrayList<Missile>();
        topBorders=new ArrayList<TopBorder>();
        bottomBorders=new ArrayList<BottomBorder>();

        smokeStartTime=System.nanoTime();
        missileStartTime=System.nanoTime();

        int bestDef=Integer.parseInt(getResources().getString(R.string.best_default));
        best=pref.getInt(getResources().getString(R.string.best_id),bestDef);


        mainThread=new MainThread(getHolder(),this);

        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry=true;
        int counter=0;
        while (retry && counter<1000)
        {
            counter++;
            try {
                mainThread.setRunning(false);
                mainThread.join();
                retry=false;
                mainThread=null;
            }catch(Exception e){}
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            if(!player.getPlaying() && newGameCreated && reset)
            {
                player.setPlaying(true);
                if(started)
                {
                    started=false;
                }
                player.setUp(true);
                reset=false;
            }
            else if(player.getPlaying())
            {
                if(started)
                {
                    started=false;
                }
                reset=false;
                player.setUp(true);
            }

            return true;
        }
        else if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update()
    {
        if(player.getPlaying()) {

            if(bottomBorders.isEmpty() || topBorders.isEmpty())
            {
                player.setPlaying(false);
                return;
            }

            background.update();
            player.update();


            //calculate the max and min height borders can have
            //they are updated based on score
            //they switch direction on max or min
            maxBorderHeight=30+player.getScore()/progressDenom;

            if(maxBorderHeight>HEIGHT/4)
            {
                maxBorderHeight=HEIGHT/4;
            }
            minBorderHeight=5+player.getScore()/progressDenom;


            //check collision with borders
            for(int i=0;i<topBorders.size();i++)
            {
                if(collision(topBorders.get(i),player))
                {
                    player.setPlaying(false);
                }
            }

            for(int i=0;i<bottomBorders.size();i++)
            {
                if(collision(bottomBorders.get(i),player))
                {
                    player.setPlaying(false);
                }
            }

            //update topborder
            this.updateTopBorder();

            //update bottomborder
            this.updateBottomBorder();

            //add missile
            long missileElapsed=(System.nanoTime()-missileStartTime)/1000000;

            if(missileElapsed>(2000-player.getScore()/4))
            {
                //first missile always goews down the middle
                if(missile.size()==0)
                {
                    missile.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),
                            WIDTH+10,HEIGHT/2,45,15, player.getScore(),13));
                }
                else {
                    missile.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),WIDTH+10,
                            (int)(rand.nextDouble()*(HEIGHT-maxBorderHeight*2))+maxBorderHeight,45,15,player.getScore(),13));
                }
                missileStartTime=System.nanoTime();
            }

            for(int i=0;i<missile.size();i++)
            {
                missile.get(i).update();
                if(collision(missile.get(i),player))
                {
                    missile.remove(i);
                    player.setPlaying(false);
                    break;
                }
                if(missile.get(i).getX()<-100)
                {
                    missile.remove(i);
                    break;
                }
            }

            //add smokepuffs
            long elapsed=(System.nanoTime()-smokeStartTime)/1000000;
            if(elapsed>120)
            {
                smoke.add(new Smokepuff(player.getX(),player.getY()+10));
                smokeStartTime=System.nanoTime();
            }

            for (int i=0;i<smoke.size();i++)
            {
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10)
                {
                    smoke.remove(i);
                }
            }
        }
        else
        {
            player.resetdy();
            if(!reset)
            {
                newGameCreated=false;
                startReset=System.nanoTime();
                reset=true;
                disappear=true;
                explosion=new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion)
                ,player.getX(),player.getY()-30,100,100,25);
            }
            explosion.update(this);
            if(started)
            {
                if(explosion.playedOnce())
                {
                    if(!newGameCreated) {
                        newGame();
                    }
                }
            }
            else if(!newGameCreated)
            {
                newGame();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX=getWidth()/(WIDTH*1.f);
        final float scaleFactorY=getHeight()/(HEIGHT*1.f);
        if(canvas!=null) {
            final int savedState=canvas.save();
            canvas.scale(scaleFactorX,scaleFactorY);
            background.draw(canvas);
            if(!disappear) {
                player.draw(canvas);
            }

            //draw smokepuffs
            for(Smokepuff sp: smoke)
            {
                sp.draw(canvas);
            }

            //draw missiles
            for(Missile m:missile)
            {
                m.draw(canvas);
            }

            //draw topborders
            for(TopBorder tp:topBorders)
            {
                tp.draw(canvas);
            }

            //draw bottomborders
            for(BottomBorder bp:bottomBorders)
            {
                bp.draw(canvas);
            }

            //draw explosion
            //reset is used to make sure draw happens after explosion's position is determined
            if(started && reset)
            {
                explosion.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState );
        }
    }

    public boolean collision(GameObject a,GameObject b)
    {
        if(Rect.intersects(a.getRectangle(),b.getRectangle()))
        {
            started=true;
            return true;
        }
        return false;
    }

    public void updateBottomBorder()
    {
        //every 50 points insert a random pattern
        if(player.getScore()%50==0)
        {
            bottomBorders.add(new BottomBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick)
            ,bottomBorders.get(bottomBorders.size()-1).getX()+20,(int)(rand.nextDouble()*(maxBorderHeight))+(HEIGHT-maxBorderHeight)));
        }

        for(int i=0;i<bottomBorders.size();i++)
        {
            bottomBorders.get(i).update();
            if(bottomBorders.get(i).getX()<-20)
            {
                bottomBorders.remove(i);
                //replace with a new one

                //calculate topdown which determines the direction
                if(bottomBorders.get(bottomBorders.size()-1).getHeight()>=maxBorderHeight)
                {
                    bottomDown=false;
                }
                else if(topBorders.get(topBorders.size()-1).getHeight()<=minBorderHeight)
                {
                    bottomDown=true;
                }

                if(bottomDown)
                {
                    bottomBorders.add(new BottomBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                            bottomBorders.get(bottomBorders.size()-1).getX()+20,bottomBorders.get(bottomBorders.size()-1).getY()+1));
                }
                else {
                    bottomBorders.add(new BottomBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                            bottomBorders.get(bottomBorders.size()-1).getX()+20,bottomBorders.get(bottomBorders.size()-1).getY()-1));
                }
            }
        }
    }

    public void updateTopBorder()
    {
        //every 40 points insert a random pattern
        if(player.getScore()%40==0)
        {
            topBorders.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick)
                    ,topBorders.get(topBorders.size()-1).getX()+20,0,(int)(rand.nextDouble()*(maxBorderHeight))+1));
        }

        for(int i=0;i<topBorders.size();i++)
        {
            topBorders.get(i).update();
            if(topBorders.get(i).getX()<-20)
            {
                topBorders.remove(i);
                //replace with a new one

                //calculate topdown which determines the direction
                if(topBorders.get(topBorders.size()-1).getHeight()>=maxBorderHeight)
                {
                    topDown=false;
                }
                else if(topBorders.get(topBorders.size()-1).getHeight()<=minBorderHeight)
                {
                    topDown=true;
                }

                if(topDown)
                {
                    topBorders.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                            topBorders.get(topBorders.size()-1).getX()+20,0,topBorders.get(topBorders.size()-1).getHeight()+1));
                }
                else {
                    topBorders.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                            topBorders.get(topBorders.size()-1).getX()+20,0,topBorders.get(topBorders.size()-1).getHeight()-1));
                }
            }
        }
    }

    public void newGame()
    {
        disappear=false;

        bottomBorders.clear();
        topBorders.clear();
        missile.clear();
        smoke.clear();

        minBorderHeight=5;
        maxBorderHeight=30;

        if(player.getScore()>best)
        {
            best=player.getScore();
            SharedPreferences.Editor editor=pref.edit();
            editor.putInt(getResources().getString(R.string.best_id),best);
            editor.commit();
        }

        player.resetdy();
        player.resetScore();
        player.setY(HEIGHT/2);


        //create initial borders
        for(int i=0;i*20<WIDTH+40;i++)
        {
            if(i==0)
            {
                topBorders.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),i*20,0,10));
            }
            else {
                topBorders.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),i*20,0,
                        topBorders.get(i-1).getHeight()+1));
            }
        }

        for(int i=0;i*20<WIDTH+40;i++)
        {
            if(i==0)
            {
                bottomBorders.add(new BottomBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),i*20,
                        HEIGHT-minBorderHeight));
            }
            else {
                bottomBorders.add(new BottomBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),i*20,
                        bottomBorders.get(i-1).getY()-1));
            }
        }
        newGameCreated=true;

    }

    public void drawText(Canvas canvas)
    {
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawText("Distance: "+(player.getScore()),10,HEIGHT-10,paint);
        canvas.drawText("Best: "+best,WIDTH-215,HEIGHT-10,paint);

        if(!player.getPlaying() && newGameCreated && reset)
        {
            Paint paint1=new Paint();
            paint1.setTextSize(40);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            canvas.drawText("Press to start",WIDTH/2-50,HEIGHT/2,paint1);

            paint1.setTextSize(20);
            canvas.drawText("Press and Hold to go up",WIDTH/2-50,HEIGHT/2+20,paint1);
            canvas.drawText("Release to go down",WIDTH/2-50,HEIGHT/2+40,paint1);
        }
    }

    public void setStarted(boolean b)
    {
        started=b;
    }
}
