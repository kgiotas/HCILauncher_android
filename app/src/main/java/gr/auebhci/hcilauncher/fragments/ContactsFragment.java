package gr.auebhci.hcilauncher.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import gr.auebhci.hcilauncher.arrayadapters.ContactsArrayAdapter;
import gr.auebhci.hcilauncher.database.ContactsDatabase;
import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.databinding.FragmentContactsBinding;
import gr.auebhci.hcilauncher.databinding.NoContactsFoundBinding;
import gr.auebhci.hcilauncher.tasks.DecodeCoords;

/**
 * This fragment represents the "Contacts" screen.
 */
public class ContactsFragment extends Fragment{


    ContactsDatabase database;
    ArrayList<String[]> contactsData;
    ContactsArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get contacts and show them on screen
        database = new ContactsDatabase(getActivity());
        database.open();
        contactsData= database.getContacts();
        database.close();
        if(contactsData.size() == 0){
            NoContactsFoundBinding binding = DataBindingUtil.inflate(inflater, R.layout.no_contacts_found, container, false);
            binding.addContactNoContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addContact(false);
                }
            });
            return binding.getRoot();
        }else {
            FragmentContactsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false);
            adapter = new ContactsArrayAdapter(getActivity(), contactsData, database); //init and populate adapter
            binding.contactsLv.setAdapter(adapter);
            binding.addContactContactsCreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addContact(true);
                }
            });
            binding.contactsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
                    final View mView = layoutInflaterAndroid.inflate(R.layout.call_confirmation_dialog, null);
                    final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
                    alertDialogBuilderUserInput.setView(mView);
                    Button ok = (Button) mView.findViewById(R.id.button_call_yes);
                    Button notok = (Button) mView.findViewById(R.id.button_call_no);
                    TextView label = (TextView) mView.findViewById(R.id.contacts_call_label);
                    label.setText(contactsData.get(i)[0] + "?");
                    final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();
                    alertDialogBuilderUserInput.setCancelable(false);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle b = new Bundle();
                            b.putString("contact_name", contactsData.get(i)[0]);
                            CallScreenFragment callScreenFragment = new CallScreenFragment();
                            callScreenFragment.setArguments(b);
                            FragmentTransaction transactionCall = getFragmentManager().beginTransaction();
                            transactionCall.replace(R.id.main_fragment, callScreenFragment);
                            transactionCall.addToBackStack(null);
                            transactionCall.commit();
                            alertDialogAndroid.dismiss();
                        }
                    });

                    notok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialogAndroid.dismiss();
                        }
                    });
                }
            });
            return binding.getRoot();
        }
    }
    private void addContact(final boolean haveContacts){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        final View mView = layoutInflaterAndroid.inflate(R.layout.add_contact_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        final EditText name = (EditText) mView.findViewById(R.id.add_contact_name);
        final EditText phone = (EditText) mView.findViewById(R.id.add_contact_phone);
        Button save = (Button)mView.findViewById(R.id.but_save_cont);
        Button cancel = (Button)mView.findViewById(R.id.but_cancel_save_cont);

        alertDialogBuilderUserInput.setCancelable(true);

        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAndroid.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactsDatabase db = new ContactsDatabase(getActivity());
                db.open();
                db.addContact(name.getText().toString(), phone.getText().toString());
                db.close();
                if(haveContacts)adapter.refresh();
                else{
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    ContactsFragment confr = new ContactsFragment();
                    transaction.replace(R.id.main_fragment, confr);
                    transaction.commit();
                }
                alertDialogAndroid.dismiss();
            }
        });
    }
}

