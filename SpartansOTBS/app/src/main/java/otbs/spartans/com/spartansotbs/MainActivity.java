package otbs.spartans.com.spartansotbs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread startTimer = new Thread()
        {
            public void run()
            {
                try
                {
                    int timer = 0;
                    while(timer < 25)
                    {
                        sleep(100);
                        timer = timer + 1;

                    }
                    startActivity(new Intent("otbs.spartans.com.spartansotbs.home"));

                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();

                }

                finally
                {
                    finish();
                }

            }

        };
        startTimer.start();
    }



}
