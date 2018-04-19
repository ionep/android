package fnf.pro.sag.fnf;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        Thread thread=new Thread(){
            public void run(){
                try{
                    CallInfo c=new CallInfo();
                    String[][] contact=c.readContacts(getContentResolver());
                    Bundle b=new Bundle();
                    b.putSerializable("contact",contact);
                    Intent intent=new Intent(MainActivity.this,Home.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
            }
        };
        thread.start();

    }

}