package examprojectapp.ionep.com.examprojectapp;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;

public class Attendance extends Fragment{

   // TextView data;

    TableLayout tableLayout;
    TableRow tableRow;
    TextView roll,time;

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.attendance, container, false);

        tableLayout=(TableLayout) rootView.findViewById(R.id.tablelayout);
        onHiddenChanged(false);
        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            try{
                tableLayout.removeAllViews();
                tableRow=new TableRow(getActivity());
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                roll=new TextView(getActivity());
                roll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                roll.setText("Roll No.");
                roll.setTextSize(25);
                roll.setPadding(5,5,5,5);
                roll.setEms(5);
                time=new TextView(getActivity());
                time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                time.setText("Time");
                time.setTextSize(25);
                time.setPadding(5,5,5,5);
                time.setEms(5);
                tableRow.addView(roll);
                tableRow.addView(time);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT , TableLayout.LayoutParams.WRAP_CONTENT));

            Cursor crs = (((MainPage) getActivity()).getDB()).readData();
            if (crs.getCount() == 0)
            {
                tableRow=new TableRow(getActivity());
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                roll=new TextView(getActivity());
                roll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                roll.setText("No datas found");
                roll.setTextSize(20);
                roll.setPadding(5,5,5,5);
                roll.setEms(5);
                tableRow.addView(roll);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT , TableLayout.LayoutParams.WRAP_CONTENT));
            }
                crs = (((MainPage) getActivity()).getDB()).readData(0);
            if(crs.getCount()==0)
            {
                tableRow=new TableRow(getActivity());
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                roll=new TextView(getActivity());
                roll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                roll.setText("No datas found");
                roll.setTextSize(20);
                roll.setPadding(5,5,5,5);
                roll.setEms(5);
                tableRow.addView(roll);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT , TableLayout.LayoutParams.WRAP_CONTENT));
            }
            else
            {
                crs=(((MainPage) getActivity()).getDB()).readData();
                while(crs.moveToNext())
                {
                    if(crs.getInt(5)==0) {
                        tableRow = new TableRow(getActivity());
                        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        roll = new TextView(getActivity());
                        roll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        roll.setText(crs.getString(1));
                        roll.setTextSize(20);
                        roll.setPadding(5,5,5,5);
                        roll.setEms(5);
                        tableRow.addView(roll);
                        time = new TextView(getActivity());
                        time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        String t = crs.getString(2) + ":" + crs.getString(3) + ":" + crs.getString(4);
                        time.setText(t);
                        time.setTextSize(20);
                        time.setPadding(5,5,5,5);
                        time.setEms(5);
                        tableRow.addView(time);
                        tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    }
                }
            }}
            catch (Exception e)
            {
                Toast.makeText(getActivity().getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
