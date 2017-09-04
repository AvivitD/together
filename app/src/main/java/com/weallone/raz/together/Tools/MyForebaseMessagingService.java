package com.weallone.raz.together.Tools;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.Entities.Shift;
import com.weallone.raz.together.Entities.ShiftCollection;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * ForeBase service.
 */
public class MyForebaseMessagingService extends FirebaseMessagingService implements AsyncResponse, Cookied {
    private static final String TAG = "MyFirebaseMsgService";
    public static String BROADCAST_ACTION = "com.example.raz.chat.update";
    private SharedPreferences settings;

    /**
     * Default constructor.
     */
    public MyForebaseMessagingService() {
    }


    /**
     * When Firebase emits a message to device ID -it is recieved HERE.
     * @param remoteMessage - the message sent.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "FROM: " + remoteMessage.getFrom());
        settings = getSharedPreferences(getResources().getString(R.string.data),
                MODE_PRIVATE);
        settings = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
        settings.edit().putString(getResources().getString(R.string.key_user_code),
                "blabla").apply();
        //DATA - JSON OF MESSAGE
        if(remoteMessage.getData().size()>0 || remoteMessage.getNotification() != null){
            if(remoteMessage.getNotification()!=null
                    && remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.togther_app))){
                //BROADDCASTING:
                if(remoteMessage.getData().containsValue(getResources().getString(R.string.shift_id))) {
                    Broadcast(getResources().getString(R.string.calendar_fragment),
                            new String[]{getResources().getString(R.string.id)},
                            new String[]{remoteMessage.getData().get(getResources().getString(R.string.shift_id))});
                    getShift(remoteMessage.getData().get(getResources().getString(R.string.shift_id)));
                }
            } else if(remoteMessage.getNotification()!=null
                    && remoteMessage.getNotification().getTitle().startsWith(getResources().getString(R.string.help_now_text))){
                //BROADDCASTING:
                Broadcast(getResources().getString(R.string.app_fragment),
                        new String[]{getResources().getString(R.string.key_help_request_num),
                                getResources().getString(R.string.key_help_child_id),
                                getResources().getString(R.string.key_help_child_name)
                        },
                        new String[]{remoteMessage.getData().get(getResources().getString(R.string.key_help_request_num)),
                                remoteMessage.getData().get(getResources().getString(R.string.key_help_child_id)),
                                remoteMessage.getData().get(getResources().getString(R.string.key_help_child_name))
                        });
            //NOTIFICATION OF MESSAGE
            } else if(remoteMessage.getNotification()!=null
                    && new JSONObject(remoteMessage.getData()).has(getResources().getString(R.string.approve_key))){
                JSONObject json = new JSONObject(remoteMessage.getData());
                try {
                    //BROADDCASTING:
                    Broadcast(getResources().getString(R.string.login_fragment),
                            new String[]{
                                    getResources().getString(R.string.key_user_approve),
                                    getResources().getString(R.string.key_user_first_name),
                                    getResources().getString(R.string.key_user_last_name),
                                    getResources().getString(R.string.key_entity),
                                    getResources().getString(R.string.key_user_code)
                            },
                            new String[]{
                                    json.getString(getResources().getString(R.string.key)),
                                    json.getString(getResources().getString(R.string.first_name)),
                                    json.getString(getResources().getString(R.string.last_name)),
                                    json.getString(getResources().getString(R.string.key_entity)),
                                    json.getString(getResources().getString(R.string.key_user_code_text)),
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Broadcast(getResources().getString(R.string.chat_fragment),null,null);
            }
        }

        if(remoteMessage.getData().size()>0){
            CheckForLogin(remoteMessage);
            CheckForPeerChatStatus(remoteMessage);
            Log.d(TAG, "Message data: " + remoteMessage.getData());
        }

        if(remoteMessage.getNotification() != null){
            CheckForLogin(remoteMessage);
            Log.d(TAG, "Message body: " + remoteMessage.getNotification().getBody());
            setChatChoice();
            if(IsAppInBackground()){
                sendNotification(remoteMessage.getNotification().getBody());
            }
        }
    }

    /**
     * Checking if peer chat status changed.
     * @param remoteMessage
     */
    private void CheckForPeerChatStatus(RemoteMessage remoteMessage) {
        JSONObject json = new JSONObject(remoteMessage.getData());
        try {
            if (json.has(getResources().getString(R.string.my_key))
                    && json.getString(getResources().getString(R.string.my_key)).equals(getResources().getString(R.string.status_update))) {
                Broadcast(getResources().getString(R.string.chat_fragment),
                        new String[]{getResources().getString(R.string.my_key).toString()},
                        new String[]{getResources().getString(R.string.status_update).toString()});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checking if this user is log in.
     * @param remoteMessage - from server.
     * @return true/false if logged.
     */
    private Boolean CheckForLogin(RemoteMessage remoteMessage) {
        JSONObject json = new JSONObject(remoteMessage.getData());
        if (json.has(getResources().getString(R.string.approve_key))
                && json.has(getResources().getString(R.string.key))){
            SetSharedPrefences();
            try {
                settings.edit().putString(getResources().getString(R.string.key_user_approve),
                        json.getString(getResources().getString(R.string.key))).apply();
                settings.edit().putString(getResources().getString(R.string.key_user_first_name),
                        json.getString(getResources().getString(R.string.first_name))).apply();
                settings.edit().putString(getResources().getString(R.string.key_user_last_name),
                        json.getString(getResources().getString(R.string.last_name))).apply();
                settings.edit().putString(getResources().getString(R.string.key_entity),
                        json.getString(getResources().getString(R.string.key_entity))).apply();
                settings.edit().putString(getResources().getString(R.string.key_user_code),
                        json.getString(getResources().getString(R.string.key_user_code_text))).apply();
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Setting shared prefences.
     */
    private void SetSharedPrefences() {
        settings = getSharedPreferences(getResources().getString(R.string.data),
                MODE_PRIVATE);
        settings = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
    }

    /**
     * Setting chat choice.
     */
    private void setChatChoice() {
        SetSharedPrefences();
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(getResources().getString(R.string.key_tabs), "0");
        edit.apply();
    }

    /**
     * Checking if this service is running in the back ground.
     * @return
     */
    private boolean IsAppInBackground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(getApplicationContext().getPackageName().toString())) {
            return false;
        }
        return true;
    }

    /**
     * Broaddcasting with the following properties:
     * @param frag - frag as fragment extra
     * @param keys - keys to add
     * @param values - values to add
     */
    private void Broadcast(String frag, String[] keys, String[] values) {
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.fragment), frag);
        if(keys!=null && values!=null){
            for(int i = 0; i < keys.length; i++){
                Log.d(TAG,"set:" + keys[i] + "  " + values[i]);
                intent.putExtra(keys[i], values[i]);
            }
        }
        intent.setAction(BROADCAST_ACTION);
        sendBroadcast(intent);
    }

    /**
     * Getting a shift.
     * @param id - of psychologist
     */
    private void getShift(String id){
        String key = getResources().getString(R.string.key_get_shift);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String getShiftUrl = getResources().getString(R.string.request_get_approved_shift_url)  + id;
        getEntity.execute(new String[]{getShiftUrl});
    }

    /**
     * Sending notification to android for prompt.
     * @param body
     */
    private void sendNotification(String body) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0/*Request code*/,intent,0);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Cloud Messaging")
                .setContentTitle(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0/*id of notification*/, notificBuilder.build());
    }

    /**
     * Callback function from server.
     * @param result - string from server.
     */
    @Override
    public void OnFinished(String result) {
        Log.d("RECIEVED:", result);
        try {
            if (result.startsWith(getResources().getString(R.string.key_get_shift))) {
                result = result.substring(13);
                if (result.equals(getResources().getString(R.string.empty))) return;
                saveShiftCollection(getShiftCollection().add(DechipherShift(result)));
            }
        } catch (JSONException e){
            Log.d(TAG, e.toString());
        }
    }

    /**
     * Getting shift object from JSON object.
     * @param result - from server
     * @return shift object
     * @throws JSONException
     */
    private Shift DechipherShift(String result) throws JSONException {
        JSONObject obj = new JSONArray(result).getJSONObject(0);
        return new Shift(obj.getString(getResources().getString(R.string.key_calendar_id)),
                obj.getString(getResources().getString(R.string.key_calendar_start)),
                obj.getString(getResources().getString(R.string.key_calendar_end)),
                obj.getString(getResources().getString(R.string.key_calendar_psycho_id)),
                obj.getString(getResources().getString(R.string.key_calendar_psycho_deviceId)),
                obj.getString(getResources().getString(R.string.key_calendar_psycho_name)),
                obj.getString(getResources().getString(R.string.key_calendar_psycho_mail)));
    }

    /**
     * set method
     * @param cookie
     */
    @Override
    public void setCookie(String cookie) {
        //set Cookie to Shared references.
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(getResources().getString(R.string.cookiekey), cookie);
        edit.apply();
    }

    /**
     * get method
     * @return
     */
    @Override
    public String getCookie() {
        SetSharedPrefences();
        return settings.getString(getResources().getString(R.string.cookiekey),
                getResources().getString(R.string.empty));
    }

    /**
     * Save shift collection to bundle.
     * @param shiftCollection
     */
    private void saveShiftCollection(ShiftCollection shiftCollection){
        SharedPreferences  mPrefs = getSharedPreferences(getResources().getString(R.string.data),
                MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shiftCollection);
        prefsEditor.putString(getResources().getString(R.string.my_shifts), json).apply();
    }

    /**
     * get shift collection from bundlel
     * @return te shift collection.
     */
    private ShiftCollection getShiftCollection(){
        ShiftCollection shiftCollection = null;
        SharedPreferences  mPrefs = getSharedPreferences(getResources().getString(R.string.data),
                MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(getResources().getString(R.string.my_shifts), getResources().getString(R.string.empty));
        if(json.equals(getResources().getString(R.string.empty))) shiftCollection = new ShiftCollection();
        else {
            shiftCollection = gson.fromJson(json, ShiftCollection.class);
        }
        return shiftCollection;
    }
}
