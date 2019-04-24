package gr.auebhci.hcilauncher.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class uses Google's geolocation API to convert physical physicalAddress to coordinates.
 */
public class DecodeCoords extends AsyncTask{

    private String physicalAddress;
    private TextView error_tv;
    private Context context;
    private AlertDialog inputDialog;
    private String LOG_TAG = "DecodeCoordsTask";

    public DecodeCoords(String physicalAddress, TextView statusTextview, Context context, AlertDialog inputDialog){
        this.physicalAddress = physicalAddress;
        this.error_tv = statusTextview;
        this.context = context;
        this.inputDialog = inputDialog;
    }

    @Override
    protected void onPreExecute() {
        error_tv.setText("Please wait...");
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String str = "https://maps.googleapis.com/maps/api/geocode/json?address=" + physicalAddress.replace(" ", ",") + "&key=<YOUR_KEY_NOT_MINE"; //use Rest API to fetch all data/

        //Prepare to download
        URLConnection urlConn;
        BufferedReader bufferedReader;
        try {
            URL url = new URL(str);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            String line; //read file line by line
            StringBuilder fileContents = new StringBuilder(); //concat everything to this
            while ((line = bufferedReader.readLine()) != null) {
                fileContents.append(line);
            }
            bufferedReader.close();
            //No internet connectivity needed beyond this point.

            //Create the JSON object
            JSONObject obj = new JSONObject(fileContents.toString());
            String status = obj.getString("status");
            if(status.equals("ZERO_RESULTS")){
                String[] notf = new String[1];
                notf[0] = "not_found";
                publishProgress(notf);
            }else if(status.equals("OK")){
                String[] coords = new String[2]; //lat, lng
                coords[0] = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                coords[1] = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                publishProgress(coords);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can not decode coordinates.");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Can not decode coordinates.");
        }
        return null;
    }

    /**
     * Saves coords to SharedPreferences if found, or displays text message.
     * @param values
     */
    @Override
    protected void onProgressUpdate(Object[] values) {
        if(((String[]) values).length == 1){
            if(((String[])values)[0].equals("not_found")){
                error_tv.setText("Η διεύθυνση δε βρήθηκε. Προσπαθήστε ξανά...");
            }
        }else if(((String[]) values).length == 2) {
            SharedPreferences sp = context.getSharedPreferences("USER_HOME_LOCATION", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("lat", Float.parseFloat((String)values[0]));
            editor.putFloat("lng", Float.parseFloat((String)values[1]));
            editor.commit();
            error_tv.setText("");
            inputDialog.dismiss();
            Toast.makeText(context, "Η διεύθυνση βρέθηκε.", Toast.LENGTH_LONG);
        }
    }
}
