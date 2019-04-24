package gr.auebhci.hcilauncher.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.databinding.FragmentCallScreenBinding;

/**
 * This fragment represents the call screen.
 */
public class CallScreenFragment extends Fragment implements View.OnClickListener{

    FragmentCallScreenBinding binding;
    boolean onSpeaker = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_screen, container, false);
        //Add name to screen.
        Bundle b = this.getArguments();
        if (b.getString("contact_name") != null) {//contact name exists
            binding.contactName.setText(b.getString("contact_name"));
        }
        binding.buttonHangUp.setOnClickListener(this);
        binding.speaker.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_hang_up:
                getActivity().onBackPressed(); //remove fragment if hang up is pressed.
                break;
            case R.id.speaker:
                if(!onSpeaker) {
                    binding.speaker.setImageResource(R.drawable.speaker_off);
                    onSpeaker = true;
                }
                else {
                    binding.speaker.setImageResource(R.drawable.speaker_on);
                    onSpeaker = false;
                }

                break;
        }
    }
}
