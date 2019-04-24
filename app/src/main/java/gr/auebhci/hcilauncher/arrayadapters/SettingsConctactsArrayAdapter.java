package gr.auebhci.hcilauncher.arrayadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import gr.auebhci.hcilauncher.database.ContactsDatabase;
import gr.auebhci.hcilauncher.R;

/**
 * This class represents a contact line used in application's settings/contacts.
 */
public class SettingsConctactsArrayAdapter extends ArrayAdapter<String[]>{

    ArrayList<String[]> data;
    Context context;
    ContactsDatabase db;

    public SettingsConctactsArrayAdapter(Context context, ArrayList<String[]> data, ContactsDatabase db) {
        super(context, R.layout.settings_contacts_line, data);
        this.data = data;
        this.context = context;
        this.db = db;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.settings_contacts_line, null);

        TextView name = (TextView) convertView.findViewById(R.id.contacts_name_settings_tv);
        TextView phone = (TextView) convertView.findViewById(R.id.contacts_phone_settings_tv);
        final ImageView delete = (ImageView) convertView.findViewById(R.id.contacts_settings_delete_btn);
        final int pos = position;

        name.setText(data.get(position)[0]);
        phone.setText(data.get(position)[1]);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove data from database..
                db.open();
                db.delete(data.get(pos)[2]);
                data.clear();
                data.addAll(db.getContacts());
                notifyDataSetChanged();
                db.close();
            }
        });
        return convertView;
    }

    /**
     * Refresh listview if contact removed.
     */
    public void refresh(){
        db.open();
        data.clear();
        data.addAll(db.getContacts());
        notifyDataSetChanged();
        db.close();
    }
}
