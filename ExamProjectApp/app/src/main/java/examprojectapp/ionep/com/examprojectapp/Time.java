package examprojectapp.ionep.com.examprojectapp;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Time extends Fragment{

    EditText roll,times;
    Button send;

    Button timerStart,timerStop;
    TextView timer;

    Handler customHandler=new Handler();

    long startTime=0L, updateTime=0L;

    Runnable updateTimerThread=new Runnable() {
        @Override
        public void run() {
            updateTime=SystemClock.uptimeMillis()-startTime;
            int sec=(int)(updateTime/1000);
            int min=sec/60;
            sec%=60;
            int hr=min/60;
            min%=60;
            hr%=12;
            ((MainPage)getActivity()).setTime(hr,min,sec);
            timer.setText(""+hr+":"+String.format("%02d",min)+":"+String.format("%02d",sec));
            customHandler.postDelayed(this,0);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.time, container, false);

        timerStart=(Button) rootView.findViewById(R.id.timer_start);
        timerStop=(Button) rootView.findViewById(R.id.timer_stop);
        timer=(TextView) rootView.findViewById(R.id.timer);

        timerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set app to start reading
                ((MainPage)getActivity()).setAppState(1);

                //send data over bluetooth to start arduino
                ((MainPage)getActivity()).writeBT("A");

                Toast.makeText(getActivity().getApplicationContext(),"Starting",Toast.LENGTH_SHORT).show();
                startTime= SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread,0);
            }
        });
        timerStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set app to stop reading
                ((MainPage)getActivity()).setAppState(0);

                //set data over bluetooth to stop arduino
                ((MainPage)getActivity()).writeBT("B");

                Toast.makeText(getActivity().getApplicationContext(),"Stopping",Toast.LENGTH_SHORT).show();
                startTime=0L;
                updateTime=0L;
                customHandler.removeCallbacks(updateTimerThread);
                timer.setText("0:00:00");
            }
        });

        /*
        roll=(EditText) rootView.findViewById(R.id.roll);
        times=(EditText) rootView.findViewById(R.id.timer);
        send=(Button)rootView.findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted=(((MainPage)getActivity()).getDB()).insertData(roll.getText().toString(),0,23,12,1);
                if(isInserted)
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Data Inserted",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Error in inserting",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        return rootView;
    }
}
