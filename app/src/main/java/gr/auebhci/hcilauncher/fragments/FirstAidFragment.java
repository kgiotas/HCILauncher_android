package gr.auebhci.hcilauncher.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import java.util.ArrayList;
import gr.auebhci.hcilauncher.arrayadapters.FirstAidArrayAdapter;
import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.databinding.FragmentFirstaidBinding;

/**
 * This class represents the "FirstAid" Screen.
 */
public class FirstAidFragment extends Fragment {

    FragmentFirstaidBinding binding;
    ArrayList<Object[]> data;
    Object[] health = new Object[2];
    Object[] fire = new Object[2];
    Object[] police = new Object[2];
    Object[] poison = new Object[2];
    boolean added = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_firstaid, container, false);

        if(!added) {
            //populate adapter with data.
            data = new ArrayList<>(4);
            health[0] = "Ασθενοφόρο";
            health[1] = R.drawable.hosp_dept;
            police[0] = "Αστυνομία";
            police[1] = R.drawable.police_dept;
            fire[0] = "Πυροσβεστική";
            fire[1] = R.drawable.fire_dept;
            poison[0] = "Κέντρο δηλητηριάσεων";
            poison[1] = R.drawable.poison;
            data.add(health);
            data.add(police);
            data.add(fire);
            data.add(poison);
            added = true;
        }

        FirstAidArrayAdapter adapter = new FirstAidArrayAdapter(getActivity(), data);
        binding.firstaidLv.setAdapter(adapter);//set adapter
        binding.firstaidLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String callee = "";
                switch (i){
                    case 0:
                        callee = "Ασθενοφόρο";
                        break;
                    case 1:
                        callee = "Αστυνομία";
                        break;
                    case 2:
                        callee = "Πυροσβεστική";
                        break;
                    case 3:
                        callee = "Κέντρο δηλητηριάσεων";
                        break;
                }
                //open call screen
                Bundle b = new Bundle();
                b.putString("contact_name", callee);
                CallScreenFragment callScreenFragment = new CallScreenFragment();
                callScreenFragment.setArguments(b);
                FragmentTransaction transactionCall = getFragmentManager().beginTransaction();
                transactionCall.replace(R.id.main_fragment, callScreenFragment);
                transactionCall.addToBackStack(null);
                transactionCall.commit();
            }
        });
        return binding.getRoot();
    }
}
