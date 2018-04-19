package luniva.test.com.lunivatest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    Button send,receive;
    EditText name;
    TextView ret;
    RequestQueue queue;
    String putDataUrl="http://10.5.33.72/Spartans/hello2.php";
    String getDataUrl="http://10.5.33.72/Spartans/hello.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send=(Button)findViewById(R.id.send);
        receive=(Button)findViewById(R.id.recieve);
        name=(EditText)findViewById(R.id.name);
        ret=(TextView)findViewById(R.id.ret);

        queue= Volley.newRequestQueue(getApplicationContext());

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"I was here",Toast.LENGTH_LONG).show();
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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        return params;
                    }
                };
                queue.add(strReq);
            }
        });
    }
}
