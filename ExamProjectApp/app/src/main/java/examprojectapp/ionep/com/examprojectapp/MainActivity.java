package examprojectapp.ionep.com.examprojectapp;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
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
                    startActivity(new Intent(MainActivity.this,Login.class));

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
