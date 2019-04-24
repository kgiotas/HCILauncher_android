package gr.auebhci.hcilauncher.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Button;

import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.activities.SettingsActivity;
import gr.auebhci.hcilauncher.database.ContactsDatabase;
import gr.auebhci.hcilauncher.databinding.FragmentDialerBinding;
import gr.auebhci.hcilauncher.fragments.CallScreenFragment;

public class DialerFragment extends Fragment implements View.OnClickListener{

    FragmentDialerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialer, container, false);
        binding.buttonDialer0.setOnClickListener(this);
        binding.buttonDialer1.setOnClickListener(this);
        binding.buttonDialer2.setOnClickListener(this);
        binding.buttonDialer3.setOnClickListener(this);
        binding.buttonDialer4.setOnClickListener(this);
        binding.buttonDialer5.setOnClickListener(this);
        binding.buttonDialer6.setOnClickListener(this);
        binding.buttonDialer7.setOnClickListener(this);
        binding.buttonDialer8.setOnClickListener(this);
        binding.buttonDialer9.setOnClickListener(this);
        binding.buttonDialerAst.setOnClickListener(this);
        binding.buttonDialerDiesi.setOnClickListener(this);
        binding.callButton.setOnClickListener(this);
        binding.buttonDialerBackspace.setOnClickListener(this);
        binding.dialNumberTV.setFocusable(false);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_dialer_0:
                    binding.dialNumberTV.append("0");
                break;
            case R.id.button_dialer_1:
                binding.dialNumberTV.append("1");
                break;
            case R.id.button_dialer_2:
                binding.dialNumberTV.append("2");
                break;
            case R.id.button_dialer_3:
                binding.dialNumberTV.append("3");
                break;
            case R.id.button_dialer_4:
                binding.dialNumberTV.append("4");
                break;
           case R.id.button_dialer_5:
               binding.dialNumberTV.append("5");
                break;
            case R.id.button_dialer_6:
                binding.dialNumberTV.append("6");
                break;
            case R.id.button_dialer_7:
                binding.dialNumberTV.append("7");
                break;
            case R.id.button_dialer_8:
                binding.dialNumberTV.append("8");
                break;
            case R.id.button_dialer_9:
                binding.dialNumberTV.append("9");
                break;
            case R.id.button_dialer_ast:
                binding.dialNumberTV.append("*");
                break;
            case R.id.button_dialer_diesi:
                binding.dialNumberTV.append("#");
                break;
            case R.id.callButton:
                if(checkInput()) {
                    Bundle b = new Bundle();
                    b.putString("contact_name", binding.dialNumberTV.getText().toString());
                    CallScreenFragment callScreenFragment = new CallScreenFragment();
                    callScreenFragment.setArguments(b);
                    FragmentTransaction transactionCall = getFragmentManager().beginTransaction();
                    transactionCall.replace(R.id.main_fragment, callScreenFragment);
                    transactionCall.addToBackStack(null);
                    transactionCall.commit();
                }else{
                    wrongNum();
                }
                break;
            case R.id.button_dialer_backspace:
                binding.dialNumberTV.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                break;
        }
    }

    private boolean checkInput(){
        if(binding.dialNumberTV.length() != 10) return false; else return true;
    }

    private void wrongNum(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        final View mView = layoutInflaterAndroid.inflate(R.layout.false_number_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        Button ok = (Button)mView.findViewById(R.id.but_wrong_number);


        alertDialogBuilderUserInput.setCancelable(true);

        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAndroid.dismiss();
            }
        });
    }
}
