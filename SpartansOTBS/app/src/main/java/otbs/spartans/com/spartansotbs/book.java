package otbs.spartans.com.spartansotbs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class book extends Activity {

    Button send;
    Button home,tInfo,contact;
    EditText name,verification,ocountry,email,start,destination;
    RadioButton nepal,other;
    RequestQueue queue;
    String countryText;

    //url varies according to connected router/isp
    String putDataUrl="http://10.5.33.72/Spartans/hello2.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);

        home=(Button)findViewById(R.id.home);
        tInfo=(Button)findViewById(R.id.tInfo);
        contact=(Button)findViewById(R.id.contact);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("otbs.spartans.com.spartansotbs.home"));
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

        name=(EditText)findViewById(R.id.name);
        verification=(EditText)findViewById(R.id.verification);
        send=(Button)findViewById(R.id.send);
        nepal=(RadioButton) findViewById(R.id.cradio1);
        other=(RadioButton)findViewById(R.id.cradio2);
        email=(EditText)findViewById(R.id.email);
        ocountry=(EditText) findViewById(R.id.ocountry);
        start=(EditText) findViewById(R.id.start);
        destination=(EditText) findViewById(R.id.destination);

        queue= Volley.newRequestQueue(getApplicationContext());

        nepal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    other.setChecked(false);
                    ocountry.setVisibility(View.INVISIBLE);
                    countryText="Nepal";
                }
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    nepal.setChecked(false);
                    ocountry.setVisibility(View.VISIBLE);
                    countryText=ocountry.getText().toString();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Error connecting to Server",Toast.LENGTH_LONG).show();
                StringRequest strReq=new StringRequest(Request.Method.POST, putDataUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("name",name.getText().toString());
                        params.put("email",email.getText().toString());
                        params.put("verification",verification.getText().toString());
                        params.put("start",start.getText().toString());
                        params.put("destination",destination.getText().toString());
                        params.put("source","A");
                        Toast.makeText(getApplicationContext(),"sent",Toast.LENGTH_LONG).show();
                        return params;
                    }
                };
                queue.add(strReq);
            }
        });
    }
}
