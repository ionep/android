package minorproject.ionep.codescanner;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    Mat mat1,mat2;
    FloatingActionButton capture,cancel,process;

    TableLayout tableLayout;
    TextView leftred,leftgreen,leftblue,rightred,rightgreen,rightblue;
    ImageButton back;
    View line;
    ImageView focus1,focus2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(OpenCVLoader.initDebug())
        {
            //Toast.makeText(getApplicationContext(), "OpenCV loaded",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Failed to load OpenCV",Toast.LENGTH_SHORT).show();
        }

        cameraBridgeViewBase=(JavaCameraView)findViewById(R.id.myCamera);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        baseLoaderCallback=new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status)
                {
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };

        line=(View)findViewById(R.id.line);
        focus1=(ImageView) findViewById(R.id.focus);
        focus2=(ImageView) findViewById(R.id.focus2);
        tableLayout=(TableLayout)findViewById(R.id.table);
        back=(ImageButton)findViewById(R.id.back);
        leftred=(TextView)findViewById(R.id.leftred);
        leftgreen=(TextView)findViewById(R.id.leftgreen);
        leftblue=(TextView)findViewById(R.id.leftblue);
        rightred=(TextView)findViewById(R.id.rightred);
        rightgreen=(TextView)findViewById(R.id.rightgreen);
        rightblue=(TextView)findViewById(R.id.rightblue);

        capture=(FloatingActionButton)findViewById(R.id.capture);
        process=(FloatingActionButton)findViewById(R.id.process);
        cancel=(FloatingActionButton)findViewById(R.id.cancel);

        //input display
        inputMode();


        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mat2=mat1.clone();
                cameraBridgeViewBase.disableView();
                capture.setVisibility(View.INVISIBLE);
                process.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraBridgeViewBase.enableView();
                capture.setVisibility(View.VISIBLE);
                process.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                mat2.release();
            }
        });

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] data;
                int r=mat2.rows()/2;
                int c=mat2.cols()/2;
                int val;

                //check to see if the 40*40 pixel box is not out of bounds of the image matrix
                if( (r+r/2+20)<2*r && (r-r/2-20)>0 && (c+c/2+20)<2*c && (c-c/2-20)>0)
                {
                    val=20;
                }
                else if( (r+r/2+10)<2*r && (r-r/2-10)>0 && (c+c/2+10)<2*c && (c-c/2-10)>0)
                {
                    val=10;
                }
                else if( (r+r/2+5)<2*r && (r-r/2-5)>0 && (c+c/2+5)<2*c && (c-c/2-5)>0)
                {
                    val=5;
                }
                else{
                    val=0;
                }
                data=getAverage(mat2,r-r/2,c-c/2,val);
                Log.d("develop", "Red="+String.valueOf(data[0])+" Green="+String.valueOf(data[1])+" Blue="+String.valueOf(data[2]));

                leftred.setText(String.valueOf(data[0]));
                leftgreen.setText(String.valueOf(data[1]));
                leftblue.setText(String.valueOf(data[2]));

                data=getAverage(mat2,r+r/2,c+c/2,val);
                Log.d("develop", "Red="+String.valueOf(data[0])+" Green="+String.valueOf(data[1])+" Blue="+String.valueOf(data[2]));

                rightred.setText(String.valueOf(data[0]));
                rightgreen.setText(String.valueOf(data[1]));
                rightblue.setText(String.valueOf(data[2]));

                outputMode();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMode();
                cameraBridgeViewBase.enableView();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null)
        {
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug())
        {
            Toast.makeText(getApplicationContext(), "OpenCV not loaded",Toast.LENGTH_SHORT).show();
        }
        else{
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraBridgeViewBase!=null)
        {
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mat1=new Mat(width,height, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mat1.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mat1=inputFrame.rgba();
        return mat1;
    }

    public double[] getAverage(Mat mat,int r,int c,int val)
    {
        double[] data=new double[3];
        double[] sum={0.0,0.0,0.0};
        int count=0;
        for(int i=r-val;i<=r+val;i++)
        {
            for(int j=c-val;j<=c+val;j++) {
                count++;
                data = mat.get(i, j);
                sum[0]+=data[0];
                sum[1]+=data[1];
                sum[2]+=data[2];
            }
        }
        data[0]=(int)sum[0]/count;
        data[1]=(int)sum[1]/count;
        data[2]=(int)sum[2]/count;
        return data;
    }

    public void inputMode(){
        cameraBridgeViewBase.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        focus1.setVisibility(View.VISIBLE);
        focus2.setVisibility(View.VISIBLE);
        capture.setVisibility(View.VISIBLE);
        process.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        tableLayout.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
    }

    public void outputMode()
    {
        cameraBridgeViewBase.setVisibility(View.INVISIBLE);
        line.setVisibility(View.INVISIBLE);
        focus1.setVisibility(View.INVISIBLE);
        focus2.setVisibility(View.INVISIBLE);
        capture.setVisibility(View.INVISIBLE);
        process.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        tableLayout.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
    }
}
