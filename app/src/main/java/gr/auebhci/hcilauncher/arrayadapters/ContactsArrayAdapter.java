package gr.auebhci.hcilauncher.arrayadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.database.ContactsDatabase;

/**
 * This class represents a contact line used in "Contacts" screen in MainMenu.
 */
public class ContactsArrayAdapter extends ArrayAdapter<String[]>{

    private ArrayList<String[]> data; //data to show.
    private Context context;
    private ContactsDatabase db;

    public ContactsArrayAdapter(Context context, ArrayList<String[]> data, ContactsDatabase db) {
        super(context, R.layout.contact_line, data);
        this.data = data;
        this.context = context;
        this.db = db;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(context, R.layout.contact_line, null);
        String[] contact = data.get(position);
        TextView name = (TextView) convertView.findViewById(R.id.contact_name);
        TextView number = (TextView) convertView.findViewById(R.id.contact_number);

        //set textviews.
        name.setText(contact[0]);
        number.setText(contact[1]);

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
