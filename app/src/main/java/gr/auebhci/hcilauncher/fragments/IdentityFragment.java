package gr.auebhci.hcilauncher.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ScrollView;

import gr.auebhci.hcilauncher.Helpers.ScrollViewDirectionHelper;
import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.databinding.FragmentIdentityBinding;

public class IdentityFragment extends Fragment implements View.OnClickListener{

    FragmentIdentityBinding binding;
    SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_identity, container, false);
        sp = getActivity().getSharedPreferences("IDENTITY_DATA", getActivity().MODE_PRIVATE);
        binding.nameTxt.setText(sp.getString("name", "ΟΝΟΜΑ"));
        binding.surnameTxt.setText(sp.getString("surname", "ΕΠΙΘΕΤΟ"));
        binding.birthDateTxt.setText(sp.getString("birthdate", "ΗΜ/ΝΙΑ ΓΕΝΝΗΣΗΣ"));
        binding.addressTxt.setText(sp.getString("address", "ΔΙΕΥΘΥΝΣΗ"));
        binding.identityNumberTxt.setText(sp.getString("idn", "ΑΔΤ"));
        binding.amkaNumberTxt.setText(sp.getString("amka", "ΑΜΚΑ"));
        binding.afmNumberTxt.setText(sp.getString("afm", "ΑΦΜ"));
        binding.arrowup.setOnClickListener(this);
        binding.arrowdown.setOnClickListener(this);
        arrowChooser.start();
        return binding.getRoot();
    }

    private Animation createBlinkAnimation(){
        Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        return animation;
    }

    Thread arrowChooser = new Thread() {
        @Override
        public void run() {
            final Animation animation = createBlinkAnimation();
            int state = 0;
            while(true){
                if(getActivity() == null) break; //if system closes activity close thread
                if(ScrollViewDirectionHelper.y == ScrollViewDirectionHelper.max){
                    //we're down
                    if (state!=0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.arrowdown.clearAnimation();
                                binding.arrowup.startAnimation(animation);
                                binding.arrowdown.setVisibility(View.GONE);
                                binding.arrowup.setVisibility(View.VISIBLE);
                            }
                        });
                        state = 0;
                    }
                }else if(ScrollViewDirectionHelper.y == 0.0){
                    //we're up
                    if(state != 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                binding.arrowup.clearAnimation();
                                binding.arrowdown.startAnimation(animation);
                                binding.arrowdown.setVisibility(View.VISIBLE);
                                binding.arrowup.setVisibility(View.GONE);
                            }
                        });
                        state = 1;
                    }
                }else {
                    //somewhere in the middle
                    if(state!=2) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.arrowup.startAnimation(animation);;
                                binding.arrowdown.startAnimation(animation);
                                binding.arrowdown.setVisibility(View.VISIBLE);
                                binding.arrowup.setVisibility(View.VISIBLE);
                            }
                        });
                        state = 2;
                    }
                }
                try {
                    Thread.sleep(10);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.arrowdown:
                binding.identityScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            case R.id.arrowup:
                binding.identityScrollView.fullScroll(ScrollView.FOCUS_UP);
                break;
        }
    }
}
