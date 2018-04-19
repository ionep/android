package minorproject.ionep.homecontrol;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class VoiceRecognition extends AppCompatActivity {

    Button btnspeak;
    ListView listView;
    static final int check=1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recognition);
        btnspeak=(Button)findViewById(R.id.btnspeak);
        listView=(ListView)findViewById(R.id.listView);

        btnspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak UP!!");
                startActivityForResult(i,check);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==check && resultCode==RESULT_OK){
            ArrayList<String> results= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, results));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
