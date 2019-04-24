package gr.auebhci.hcilauncher.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.Calendar;
import java.util.List;
import gr.auebhci.hcilauncher.R;
import gr.auebhci.hcilauncher.databinding.ActivityBootBinding;
import gr.auebhci.hcilauncher.fragments.MenuFragment;

/**
 * This class holds the main screen and calculates battery level,
 * signal and time with appropriate intervals
 */
public class BootActivity extends FragmentActivity{

    String LOG_TAG = "BootActivity";
    ActivityBootBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Notify system to make the activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_boot); //initialize layout

        //start calculation threads
        timeThread.start();
        batteryThread.start();
        //signalThread.start();

        //Initialize BroadcaseReceiver to update battery state status (charging or not)
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                if (plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                    // on AC power
                    binding.chargeico.setVisibility(View.VISIBLE);
                } else if (plugged == 0) {
                    // on battery power
                    binding.chargeico.setVisibility(View.INVISIBLE);
                } else {
                    // intent did not  include extra info
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        //Open main menu fragment
        MenuFragment fragmentA = new MenuFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment, fragmentA);
        transaction.commit();
    }

    //This thread updates the time. Runs every 30 seconds.
    Thread timeThread = new Thread() {
        Calendar c;

        @Override
        public void run() {
            Log.d(LOG_TAG, "Time thread just started.");
            while(true){
                c = Calendar.getInstance(); //get current calendar instance
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);
                        String hour_s = "";
                        String minute_s = "";
                        //if hour or minute is one digit, add a zero at the front.
                        if(hour < 10)
                            hour_s = "0" + hour;
                        else
                            hour_s += hour;
                        if(minute < 10)
                            minute_s = "0" + minute;
                        else
                            minute_s += minute;
                        binding.timeTv.setText(hour_s + ":" + minute_s); //update textview.
                    }
                });
                try {
                    Thread.sleep(30000);
                }catch (InterruptedException intex){
                    Log.e(LOG_TAG, "Timer: Thread could not sleep");
                }
            }
        }
    };

    //This thread updates signal levels. Will not be used cause it depends on hardware.
    Thread signalThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                runOnUiThread(new Runnable() {



                    @Override
                    public void run() {
                        String strength = "";
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
                        if(cellInfos!=null){
                            for (int i = 0 ; i<cellInfos.size(); i++){
                                if (cellInfos.get(i).isRegistered()){
                                    if(cellInfos.get(i) instanceof CellInfoWcdma){
                                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                                        strength = String.valueOf(cellSignalStrengthWcdma.getDbm());
                                    }else if(cellInfos.get(i) instanceof CellInfoGsm){
                                        CellInfoGsm cellInfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
                                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                                        strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                                    }else if(cellInfos.get(i) instanceof CellInfoCdma){
                                        CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                                        strength = String.valueOf(cellSignalStrengthLte.getDbm());
                                    }
                                }
                            }
                        }
                        int ste = -60;
                        if(ste > -70)
                            binding.signalico.setImageResource(R.drawable.signal5);
                        if(ste >= -85 && ste <= -70)
                            binding.signalico.setImageResource(R.drawable.signal4);
                        if(ste >= -100 && ste <= -86)
                            binding.signalico.setImageResource(R.drawable.signal3);
                        if(ste < -100)
                            binding.signalico.setImageResource(R.drawable.signal2);
                        if(ste > -110)
                            binding.signalico.setImageResource(R.drawable.signal1);
                    }

                });
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException intex){
                    Log.e(LOG_TAG, "Timer: Thread could not sleep");
                }
            }
        }
    };

    //This thread updates battery levels.
    Thread batteryThread = new Thread(){
        @Override
        public void run(){
            Log.d(LOG_TAG, "Battery thread just started.");
            while (true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int batteryLevel = getBatteryLevel();
                        if (batteryLevel < 8)
                            binding.batteryico.setImageResource(R.drawable.bat_0_per);
                        if (batteryLevel > 25)
                            binding.batteryico.setImageResource(R.drawable.bat_25_per);
                        if (batteryLevel > 50)
                            binding.batteryico.setImageResource(R.drawable.bat_50_per);
                        if (batteryLevel > 75)
                            binding.batteryico.setImageResource(R.drawable.bat_75_per);
                        if (batteryLevel > 85)
                            binding.batteryico.setImageResource(R.drawable.bat_100_per);
                    }
                });
                try{
                    Thread.sleep(180000);
                }catch (InterruptedException intex){
                    Log.e(LOG_TAG, "Timer: Thread could not sleep");
                }
            }
        }
    };

    /**
     * Gets current battery level
     * @return int between 100 - 0;
     */
    private int getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // fail safe
        if(level == -1 || scale == -1) {
            return 50;
        }
        return (int) (((float)level / (float)scale) * 100.0f);
    }
}
