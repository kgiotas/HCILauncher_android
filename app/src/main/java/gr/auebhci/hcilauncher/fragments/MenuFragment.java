package gr.auebhci.hcilauncher.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.activities.SettingsActivity;
import gr.auebhci.hcilauncher.databinding.FragmentMenuBinding;

/**
 * This class represents the "Main menu" screen.
 */
public class MenuFragment extends Fragment implements View.OnClickListener{

    FragmentMenuBinding binding;
    private Vibrator vibrator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        binding.actualContactsButton.setOnClickListener(this);
        binding.actualCallButton.setOnClickListener(this);
        binding.mainSettingsButton.setOnClickListener(this);
        binding.actualLostButton.setOnClickListener(this);
        binding.actualIdentityButton.setOnClickListener(this);
        binding.actualFaidButton.setOnClickListener(this);
        vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
        return binding.getRoot();
    }

    /**
     * Used to replace fragments.
     * @param view
     */
    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch(view.getId()){
            case R.id.actual_contacts_button:
                ContactsFragment cv = new ContactsFragment();
                transaction.replace(R.id.main_fragment, cv);
                break;
            case R.id.actual_call_button:
                DialerFragment df = new DialerFragment();
                transaction.replace(R.id.main_fragment, df);
                break;
            case R.id.main_settings_button:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.actual_lost_button:
                LostFragment lostFragment = new LostFragment();
                transaction.replace(R.id.main_fragment, lostFragment);
                break;
            case R.id.actual_identity_button:
                IdentityFragment identityFragment = new IdentityFragment();
                transaction.replace(R.id.main_fragment, identityFragment);
                break;
            case R.id.actual_faid_button:
                FirstAidFragment firstAidFragment = new FirstAidFragment();
                transaction.replace(R.id.main_fragment, firstAidFragment);
                break;
        }
        vibrator.vibrate(150);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
