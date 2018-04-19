package otbs.spartans.com.spartansotbs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends Activity {

    Button home,book,tInfo,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        home=(Button)findViewById(R.id.home);
        book=(Button)findViewById(R.id.book);
        tInfo=(Button)findViewById(R.id.tInfo);
        contact=(Button)findViewById(R.id.contact);

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
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("otbs.spartans.com.spartansotbs.contact"));
            }
        });
    }
}
