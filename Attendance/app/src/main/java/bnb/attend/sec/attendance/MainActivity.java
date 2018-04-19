package bnb.attend.sec.attendance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    Button home,book,tInfo,contact;
    MyAdapter mAdapter;

    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        home=(Button)findViewById(R.id.home);
        book=(Button)findViewById(R.id.book);
        tInfo=(Button)findViewById(R.id.tInfo);
        contact=(Button)findViewById(R.id.contact);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(0);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(1);
            }
        });
        tInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(2);
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(3);
            }
        });
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

    public static class ArrayListFragment extends ListFragment {
        int mNum;

        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num as an argument for input
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if(mNum==0)
            {
                View v = inflater.inflate(R.layout.fragment_layout, container, false);
                View tv = v.findViewById(R.id.text);
                ((TextView)tv).setText(R.string.page1);
                return v;
            }
            else if(mNum==1)
            {
                View v = inflater.inflate(R.layout.fragment_layout, container, false);
                View tv = v.findViewById(R.id.text);
                ((TextView)tv).setText(R.string.page2);
                return v;
            }
            else if(mNum==2) {
                View v = inflater.inflate(R.layout.fragment_layout, container, false);
                View tv = v.findViewById(R.id.text);
                ((TextView) tv).setText(R.string.page3);
                return v;
            }
            else if(mNum==3) {
                View v = inflater.inflate(R.layout.fragment_layout, container, false);
                View tv = v.findViewById(R.id.text);
                ((TextView) tv).setText(R.string.page4);
                return v;
            }
            else{
                View v = inflater.inflate(R.layout.fragment_layout, container, false);
                return v;
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
    }
}
