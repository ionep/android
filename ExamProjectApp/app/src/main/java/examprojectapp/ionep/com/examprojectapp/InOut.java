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

public class InOut extends Fragment{


    TableLayout tableLayout;
    TableRow tableRow;
    TextView roll,time,io;

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.inout, container, false);
        tableLayout=(TableLayout) rootView.findViewById(R.id.tablelayoutio);
        onHiddenChanged(false);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                tableLayout.removeAllViews();
                tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                roll = new TextView(getActivity());
                roll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                roll.setText("Roll No.");
                roll.setTextSize(20);
                roll.setPadding(5,5,5,5);
                roll.setEms(4);
                time = new TextView(getActivity());
                time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                time.setText("Time");
                time.setTextSize(20);
                time.setPadding(5,5,5,5);
                time.setEms(4);
                io = new TextView(getActivity());
                io.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                io.setText("In/Out");
                io.setTextSize(20);
                io.setPadding(5,5,5,5);
                io.setEms(4);

                tableRow.addView(roll);
                tableRow.addView(time);
                tableRow.addView(io);
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                Cursor crs = (((MainPage) getActivity()).getDB()).readData();
                if (crs.getCount() == 0) {
                    tableRow = new TableRow(getActivity());
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    roll = new TextView(getActivity());
                    roll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    roll.setText("No datas found");
                    roll.setTextSize(15);
                    roll.setPadding(5,5,5,5);
                    roll.setEms(4);
                    tableRow.addView(roll);
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
                crs = (((MainPage) getActivity()).getDB()).readData(1);
                Cursor crs2=(((MainPage) getActivity()).getDB()).readData(2);
                if (crs.getCount() == 0 && crs2.getCount()==0) {
                    tableRow = new TableRow(getActivity());
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    roll = new TextView(getActivity());
                    roll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    roll.setText("No datas found");
                    roll.setTextSize(15);
                    roll.setPadding(5,5,5,5);
                    roll.setEms(4);
                    tableRow.addView(roll);
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                } else {
                    crs = (((MainPage) getActivity()).getDB()).readData();
                    while (crs.moveToNext()) {
                        if (crs.getInt(5) == 1 || crs.getInt(5)==2) {
                            String iostate;
                            tableRow = new TableRow(getActivity());
                            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            roll = new TextView(getActivity());
                            roll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            roll.setText(crs.getString(1));
                            roll.setTextSize(15);
                            roll.setPadding(5,5,5,5);
                            roll.setEms(4);
                            tableRow.addView(roll);
                            time = new TextView(getActivity());
                            time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            String t = crs.getString(2) + ":" + crs.getString(3) + ":" + crs.getString(4);
                            time.setText(t);
                            time.setTextSize(15);
                            time.setPadding(5,5,5,5);
                            time.setEms(4);
                            io = new TextView(getActivity());
                            io.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                            if(crs.getInt(5)==2)
                            {
                                iostate="In";
                            }
                            else {
                                iostate="Out";
                            }
                            io.setText(iostate);
                            io.setTextSize(15);
                            io.setPadding(5,5,5,5);
                            io.setEms(4);
                            tableRow.addView(time);
                            tableRow.addView(io);
                            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
