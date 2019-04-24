package gr.auebhci.hcilauncher.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.arrayadapters.SettingsArrayAdapter;
import gr.auebhci.hcilauncher.arrayadapters.SettingsConctactsArrayAdapter;
import gr.auebhci.hcilauncher.database.ContactsDatabase;
import gr.auebhci.hcilauncher.databinding.ActivitySettingsBinding;
import gr.auebhci.hcilauncher.databinding.FragmentContactsSettingsBinding;
import gr.auebhci.hcilauncher.databinding.FragmentSettingsMainBinding;
import gr.auebhci.hcilauncher.tasks.DecodeCoords;

/**
 * This class is used for application's settings.
 */
public class SettingsActivity extends AppCompatActivity{

    ActivitySettingsBinding mainSettingsBinding;
    public static MenuItem actionBarAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainSettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        //add settings options
        MainSettingsFragment fragmentA = new MainSettingsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.settings_fragment, fragmentA);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_settings_contacts, menu);
        actionBarAddButton = menu.findItem(R.id.add_contact);
        //at start hide "add contacts button"
        actionBarAddButton.setVisible(false);
        return true;
    }

    /**
     * Contacts settings
     */
    public static class ContactsSettingsFragment extends Fragment{

        ContactsDatabase db; //database
        FragmentContactsSettingsBinding binding;
        SettingsConctactsArrayAdapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts_settings, container, false);
            actionBarAddButton.setVisible(true);

            db = new ContactsDatabase(getActivity());
            db.open();

            adapter = new SettingsConctactsArrayAdapter(getActivity(), db.getContacts(), db);
            db.close();
            actionBarAddButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    showAddContactDialog();
                    return true;
                }
            });
            binding.settingsContactsListview.setAdapter(adapter);
            return binding.getRoot();
        }

        @Override
        public void onDetach() {
            super.onDetach();
            //hide "add contacts" button if we leave fragment.
            actionBarAddButton.setVisible(false);
        }

        /**
         * Small dialog used in adding contacts after pressing the button
         */
        private void showAddContactDialog(){
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
            final View mView = layoutInflaterAndroid.inflate(R.layout.settings_add_contact, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
            alertDialogBuilderUserInput.setView(mView);
            final EditText name = (EditText) mView.findViewById(R.id.settings_add_contact_name);
            final EditText phone = (EditText) mView.findViewById(R.id.settings_add_contact_phone);

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Προσθήκη", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            db.open();
                            db.addContact(name.getText().toString(), phone.getText().toString()); //add to database
                            db.close();
                            adapter.refresh(); //add item to listview.
                        }
                    })

                    .setNegativeButton("Ακύρωση",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });
            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }
    }

    /**
     * Fragment used to change values for identity screen
     */
    public static class IdentitySettingsFragment extends Fragment{

        FragmentSettingsMainBinding binding;

        String[] onoma = new String[2];
        String[] epitheto = new String[2];
        String[] birth = new String[2];
        String[] addr = new String[2];
        String[] adt = new String[2];
        String[] amka = new String[2];
        String[] afm = new String[2];
        ArrayList<String[]> adapter_data = new ArrayList<>(7);
        boolean added = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_main, container, false);

            if(!added) {
                onoma[0] = "Όνομα";
                onoma[1] = "Όνομα κατόχου";
                epitheto[0] = "Επίθετο";
                epitheto[1] = "Επίθετο κατόχου";
                birth[0] = "Ημερομηνία γέννησης";
                birth[1] = "Ημερομηνία γέννησης κατόχου";
                addr[0] = "Διεύθυνση";
                addr[1] = "Διεύθυνση κατοικίας κατόχου";
                adt[0] = "ΑΔΤ";
                adt[1] = "Αριθμός δελτίου ταυτότητας κατόχου";
                amka[0] = "ΑΜΚΑ";
                amka[1] = "Αριθμός κοιν. ασφάλισης κατόχου";
                afm[0] = "ΑΦΜ";
                afm[1] = "Αριθμός φορ. μητρώου κατόχου";
                adapter_data.add(onoma);
                adapter_data.add(epitheto);
                adapter_data.add(birth);
                adapter_data.add(addr);
                adapter_data.add(adt);
                adapter_data.add(amka);
                adapter_data.add(afm);
                added = true;

                SettingsArrayAdapter setAdapter = new SettingsArrayAdapter(getContext(), adapter_data);
                binding.settingsMainListview.setAdapter(setAdapter);
                binding.settingsMainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        showIdentityDialog(i);
                    }
                });
            }
            return binding.getRoot();
        }

        /**
         * Small dialog to change value for each item in identity screen.
         * @param pos
         */
        private void showIdentityDialog(final int pos){
            //init sharedPreferences.
            SharedPreferences sp = getActivity().getSharedPreferences("IDENTITY_DATA", getActivity().MODE_PRIVATE);
            final SharedPreferences.Editor editor = sp.edit();
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
            final View mView = layoutInflaterAndroid.inflate(R.layout.settings_change_identity, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
            alertDialogBuilderUserInput.setView(mView);
            TextView label = (TextView) mView.findViewById(R.id.settings_identity_label);

            switch (pos){
                case 0: label.setText("Όνομα:");
                    break;
                case 1: label.setText("Επίθετο:");
                    break;
                case 2: label.setText("Ημερομηνία Γέννησης (ΗΗ/ΜΜ/ΕΕΕΕ):");
                    break;
                case 3: label.setText("Διεύθυνση κατοικίας:");
                    break;
                case 4: label.setText("ΑΔΤ:");
                    break;
                case 5: label.setText("ΑΜΚΑ:");
                    break;
                case 6: label.setText("ΑΦΜ:");
                    break;
            }
            final EditText val = (EditText) mView.findViewById(R.id.settings_identity_tv);

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Αποθήκευση", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                        switch (pos){
                            case 0: editor.putString("name", val.getText().toString());
                                break;
                            case 1: editor.putString("surname", val.getText().toString());
                                break;
                            case 2: editor.putString("birthdate", val.getText().toString());
                                break;
                            case 3: editor.putString("address", val.getText().toString());
                                break;
                            case 4: editor.putString("idn", val.getText().toString());
                                break;
                            case 5: editor.putString("amka", val.getText().toString());
                                break;
                            case 6: editor.putString("afm", val.getText().toString());
                                break;
                        }
                            editor.commit(); //save data.
                        }
                    })
                    .setNegativeButton("Ακύρωση",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }
    }

    /**
     * First appearing fragment. Shows settings categories.
     */
    public static class MainSettingsFragment extends Fragment{

        FragmentSettingsMainBinding mainBinding;
        String[] contacts_selection = new String[2];
        String[] gps_selection = new String[2];
        String[] identity_selection = new String[2];
        ArrayList<String[]> adapter_data = new ArrayList<>(3);
        boolean added = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_main, container, false);

            contacts_selection[0] = "Επαφές";
            contacts_selection[1] = "Προσθήκη ή αφαίρεση επαφών από το τηλέφωνο.";
            gps_selection[0] = "Διεύθυνση κατοικίας";
            gps_selection[1] = "Αφορά τη λειτουργία της επιλογής 'Χάθηκα'.";
            identity_selection[0] = "Στοιχεία Χρήστη";
            identity_selection[1] = "Εισαγωγή στοιχείων χρήστη του τηλεφώνου.";
            if(!added) {
                adapter_data.add(contacts_selection);
                adapter_data.add(gps_selection);
                adapter_data.add(identity_selection);
                added = true;
            }
            SettingsArrayAdapter setAdapter = new SettingsArrayAdapter(getContext(), adapter_data);
            mainBinding.settingsMainListview.setAdapter(setAdapter);

            mainBinding.settingsMainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i == 1) lostDialog();
                    if(i == 0) openContactsSettings();
                    if(i == 2) openIdentityFragment();

                }
            });
            return mainBinding.getRoot();
        }

        /**
         * Changes fragment on screen to identity settings.
         */
        private void openIdentityFragment(){
            IdentitySettingsFragment fragmentA = new IdentitySettingsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.settings_fragment, fragmentA);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        /**
         * Changes fragment on screen to contacts settings.
         */
        private void openContactsSettings(){
            ContactsSettingsFragment fragmentA = new ContactsSettingsFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.settings_fragment, fragmentA);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        /**
         * Small dialog to define address for the "I'm lost" screen.
         */
        private void lostDialog(){
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
            final View mView = layoutInflaterAndroid.inflate(R.layout.lost_setting_dialog, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
            alertDialogBuilderUserInput.setView(mView);
            final EditText et = (EditText) mView.findViewById(R.id.lost_dialog_et);
            final EditText phone = (EditText) mView.findViewById(R.id.lost_phone_dialog);

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            // Do nothing here...will override later because it closes dialog
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
            alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //use this task to convert address to LatLng, using Google's Geolocation API.
                    DecodeCoords c = new DecodeCoords(et.getText().toString(), (TextView)mView.findViewById(R.id.lost_dialog_error_tv), getActivity(), alertDialogAndroid);
                    c.execute();
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("USER_HOME_LOCATION", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor ed = sharedPref.edit();
                    ed.putString("EM_PHONE", phone.getText().toString());
                    ed.commit();
                }
            });
        }
    }
}
