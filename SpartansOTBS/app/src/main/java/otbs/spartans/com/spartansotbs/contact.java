package otbs.spartans.com.spartansotbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class contact extends AppCompatActivity {

    Button home,book,tInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        home=(Button)findViewById(R.id.home);
        book=(Button)findViewById(R.id.book);
        tInfo=(Button)findViewById(R.id.tInfo);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("otbs.spartans.com.spartansotbs.book"));
            }
        });
        tInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("otbs.spartans.com.spartansotbs.tInfo"));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("otbs.spartans.com.spartansotbs.home"));
            }
        });
    }
}
