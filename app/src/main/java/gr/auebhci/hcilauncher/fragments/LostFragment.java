package gr.auebhci.hcilauncher.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import gr.auebhci.hcilauncher.R;
import static android.content.Context.LOCATION_SERVICE;

/**
 * This class represents the "I'm Lost" screen.
 */
public class LostFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lost, null, false);

        //if LatLng has not been fetched show alert dialog.
        if(getHomeLatLong().latitude == -1 && getHomeLatLong().longitude == -1){
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Δε βρέθηκε διεύθυνση");
            alertDialog.setMessage("Μπορείτε να εισάγετε την διεύθυνση του σπιτιού στις ρυθμίσεις.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().onBackPressed();
                        }
                    });
            alertDialog.show();
            v.findViewById(R.id.map).setVisibility(View.GONE);
        }else{
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        return v;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationManager lm = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        Location loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        while (loc == null){
            //wait for location be fetched.
        }
        sendSMS(loc);
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(), loc.getLongitude())));
        mMap.addMarker(new MarkerOptions().position(getHomeLatLong()).title("Σπίτι"));
    }

    /**
     * Gets LatLng from shared preferences.
     * @return a LatLng obj.
     */
    private LatLng getHomeLatLong(){
        sharedPref = getActivity().getSharedPreferences("USER_HOME_LOCATION", getActivity().MODE_PRIVATE);
        return new LatLng(sharedPref.getFloat("lat", -1), sharedPref.getFloat("lng", -1));
    }

    /**
     * Sends SMS message to user specified number.
     * @param loc current Location.
     */
    private void sendSMS(Location loc){
        sharedPref = getActivity().getSharedPreferences("USER_HOME_LOCATION", getActivity().MODE_PRIVATE);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sharedPref.getString("EM_PHONE", "6987264149"), null, "Χάθηκα! Οι συντεταγμένες είναι: ("
                + loc.getLatitude() + "," + loc.getLongitude() +")", null, null);
        Toast.makeText(getActivity(), "Το μήνυμα με τις συνταταγμένενες εστάλη", Toast.LENGTH_LONG).show();
    }
}
