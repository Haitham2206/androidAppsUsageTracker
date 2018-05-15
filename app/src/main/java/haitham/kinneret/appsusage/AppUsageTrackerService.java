package haitham.kinneret.appsusage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.rvalerio.fgchecker.AppChecker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUsageTrackerService extends Service {
    private static final String TAG = AppUsageTrackerService.class.getSimpleName();
    AppChecker appChecker;
    String startTime ;
    String endTime = null;
    String usageDuration = null;
    boolean alredyGotStartTime = false;
    String overallUsageTime ;
    static long overallDiff = 0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        appChecker.whenAny(new AppChecker.Listener() {
            @Override
            public void onForeground(String process) {
                //Toast.makeText(getApplicationContext(),process+ " is foreground",Toast.LENGTH_SHORT).show();
                int timeInSeconds = 0;

                if(!process.contains("launcher")){
                    Log.e(TAG,process+ " is foreground");
                    if(!alredyGotStartTime) {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        startTime = format.format(new Date());
                    }

                    alredyGotStartTime = true;
                }
                else {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    endTime = format.format(new Date());
                    Date time1 = null;
                    Date time2 = null;

                    try{
                        if(startTime != null) {
                            time1 = format.parse(startTime);
                            time2 = format.parse(endTime);
                            //In milliseconds
                            long diff = time2.getTime() - time1.getTime();
                            overallDiff += diff;
                            //Convert diff from milliseconds to hh:mm:ss
                            long diffSeconds = diff / 1000 % 60;
                            long diffMinutes = diff / (60 * 1000) % 60;
                            long diffHours = diff / (60 * 50 * 1000) % 24;
                            //Convert overallDiff from milliseconds to hh:mm:ss
                            long overallDiffSeconds = overallDiff / 1000 % 60;
                            long overallDiffMinutes = overallDiff / (60 * 1000) % 60;
                            long overallDiffHours = overallDiff / (60 * 50 * 1000) % 24;
                            usageDuration = String.valueOf(diffHours) + ":" + String.valueOf(diffMinutes) + ":" + String.valueOf(diffSeconds);
                            overallUsageTime = String.valueOf(overallDiffHours) + ":" + String.valueOf(diffMinutes) + ":" + String.valueOf(overallDiffSeconds);
                            Log.e(TAG,"Usage duration: " + usageDuration);
                            Log.e(TAG,"overall Usage time: " + overallUsageTime);

                            startTime =null;
                            alredyGotStartTime=false;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).
                when("com.whatsapp", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String process) {
                        Log.e(TAG, "whatsapp -> social");
                    }
                }).
                when("com.instagram.android", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String process) {
                        Log.e(TAG, "instagram -> social");
                    }
                }).
                when("com.facebook.katana", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String process) {
                        Log.e(TAG, "facebook -> social");
                    }
                }).
                when("com.facebook.lite", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String process) {
                        Log.e(TAG, "facebook -> social");
                    }
                }).
                when("com.facebook.mlite", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String process) {
                        Log.e(TAG, "facebook -> social");
                    }
                }).
                when("com.facebook.orca", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String process) {
                        Log.e(TAG, "facebook -> social");
                    }
                }).
                when("com.snapchat.android", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String process) {
                        Log.e(TAG, "snapchat -> social");
                    }
                }).
                when("com.twitter.android", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String process) {
                        Log.e(TAG, "twitter -> social");
                    }
                }).
                timeout(1000).start(getApplicationContext());

        return START_STICKY;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        appChecker = new AppChecker();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appChecker.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {return null;}
}
