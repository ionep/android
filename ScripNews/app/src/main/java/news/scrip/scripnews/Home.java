package news.scrip.scripnews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class Home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.home,container,false);
        LinearLayout lin=(LinearLayout) view.findViewById(R.id.home);
        final Toolbar toolbar=(Toolbar) getActivity().findViewById(R.id.toolbarbottom);
            //can be replaced by onclick listener
        lin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        ActionBar actionbar=((AppCompatActivity)getActivity()).getSupportActionBar();
                        if(actionbar.isShowing())
                        {
                            actionbar.hide();
                        }
                        else
                        {
                            actionbar.show();
                        }

                        if(toolbar.getVisibility()==View.VISIBLE)
                        {
                            toolbar.setVisibility(View.INVISIBLE);
                        }
                        else {
                            toolbar.setVisibility(View.VISIBLE);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        return view;
    }

}
