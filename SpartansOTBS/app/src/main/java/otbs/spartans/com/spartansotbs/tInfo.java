package otbs.spartans.com.spartansotbs;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class tInfo extends Activity {

    TextView ret;
    Button receive,home,book,contact;
    EditText ticket,code;
    RequestQueue queue;

    //url varies according to connected router/isp
    String getDataUrl="http://10.5.33.72/hello.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tinfo);

        home=(Button)findViewById(R.id.home);
        book=(Button)findViewById(R.id.book);
        contact=(Button)findViewById(R.id.contact);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("otbs.spartans.com.spartansotbs.home"));
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("otbs.spartans.com.spartansotbs.book"));
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("otbs.spartans.com.spartansotbs.contact"));
            }
        });

        ret=(TextView)findViewById(R.id.ret);
        receive=(Button)findViewById(R.id.recieve);
        ticket=(EditText)findViewById(R.id.ticket);
        code=(EditText)findViewById(R.id.code);
        queue= Volley.newRequestQueue(getApplicationContext());

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Error connecting to Server",Toast.LENGTH_LONG).show();
                JsonObjectRequest jObjReq=new JsonObjectRequest(Request.Method.POST, getDataUrl,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray customer = response.getJSONArray("trial");
                                    for(int i=0;i<customer.length();i++)
                                    {
                                        JSONObject jObj=customer.getJSONObject(i);
                                        String id=jObj.getString("id");
                                        String sname=jObj.getString("name");
                                        ret.append(id+" "+sname);
                                    }
                                }
                                catch(JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                queue.add(jObjReq);
            }
        });
    }
}
