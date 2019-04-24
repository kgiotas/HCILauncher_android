package gr.auebhci.hcilauncher.arrayadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import gr.auebhci.hcilauncher.R;

/**
 * This class represents a contact line used in application settings.
 */
public class SettingsArrayAdapter extends ArrayAdapter<String[]> {

    private ArrayList<String[]> data;
    private Context context;

    public SettingsArrayAdapter(Context context, ArrayList<String[]> data) {
        super(context, R.layout.settings_line, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(context, R.layout.settings_line, null);
        String[] setting = data.get(position);
        TextView header = (TextView) convertView.findViewById(R.id.settings_line_main_header);
        TextView footer = (TextView) convertView.findViewById(R.id.settings_line_main_footer);

        header.setText(setting[0]);
        footer.setText(setting[1]);

        return convertView;
    }
}
