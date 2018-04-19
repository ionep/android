package fnf.pro.sag.fnf;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Contact extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_contact, container, false);
        TextView tv=(TextView) v.findViewById(R.id.contacts);
        //CallInfo info=new CallInfo();
        //String contacts[][]=info.readContacts(getActivity().getContentResolver());
        String[][] contacts=null;
        Object[] objectArray = (Object[]) getActivity().getIntent().getExtras().getSerializable("contact");
        if(objectArray!=null){
            contacts = new String[objectArray.length][];
            for(int i=0;i<objectArray.length;i++){
                contacts[i]=(String[]) objectArray[i];
            }
        }
        int i=0;
        while(i<Integer.parseInt(contacts[0][0]))
        {
            tv.append(contacts[1][i]+" : "+contacts[2][i]);
            i++;
        }
        return v;
    }

}
