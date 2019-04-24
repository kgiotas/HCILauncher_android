package gr.auebhci.hcilauncher.arrayadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import gr.auebhci.hcilauncher.R;

/**
 * This class represents a contact line used in "First Aid" screen in MainMenu.
 */
public class FirstAidArrayAdapter extends ArrayAdapter<Object[]>{

    private ArrayList<Object[]> data;
    private Context context;

    public FirstAidArrayAdapter(Context context, ArrayList<Object[]> data) {
        super(context, R.layout.first_aid_line, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.first_aid_line, null);
        TextView name = (TextView) convertView.findViewById(R.id.first_aid_tv);
        ImageView image = (ImageView)convertView.findViewById(R.id.first_aid_iv);
        name.setText((String) data.get(position)[0]);
        image.setImageResource((int) data.get(position)[1]);
        return convertView;
    }
}
