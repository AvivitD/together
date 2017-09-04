package com.weallone.raz.together.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.weallone.raz.together.Activities.MyActionBarActivity;
import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.Entities.Updates.Update;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Callback;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Interfaces.TabAbleFragment;
import com.weallone.raz.together.Painters.UpdatePainter;
import com.weallone.raz.together.R;
import com.weallone.raz.together.Sensors.ShakeDetector;
import com.weallone.raz.together.Sensors.SwipeGestureDetector;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Updates fragment.
 */
public class Updates extends Fragment implements TabAbleFragment, AsyncResponse, Cookied, Callback {

    private static final String TAG = "Updates.Fragment";
    private static final Integer maxUpdatesInRequest = 10;
    private SharedPreferences settings;
    private MyActionBarActivity myActionBarActivity = null;
    private List<Update> updates = new ArrayList<>();
    private UpdatePainter painter = null;
    private UserInfo user = null;
    private LinearLayout updatesLayout = null;
    private ScrollView scroll = null;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private boolean swipeRefresh = false;
    private Boolean shown = false;

    /**
     * updates fragment creation method
     * @param actionBarActivity
     * @return
     */
    public static Updates newInstance(MyActionBarActivity actionBarActivity){
        Updates fragment = new Updates();
        Bundle bundle = new Bundle();
        bundle.putSerializable(actionBarActivity.getResources().getString(R.string.key_updates_pass_argument), (Serializable) actionBarActivity);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * default constrctuor.
     */
    public Updates() {
        // Required empty public constructor
    }

    /**
     * standard onCreateView method.
     * setting shared prefences, init UI and getting updates.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the edited view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_updates, container, false);
        if(getActivity()==null) return view;
        SetSharedPrefences();
        initUI(view, inflater);
        GetUpdates(-1, 10);
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Unused - no sensors in fragment.
     */
    private void InitSensors() {
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });
        final GestureDetector gd = new GestureDetector(getActivity(), new SwipeGestureDetector(this,
                getResources().getString(R.string.duration)));
        View.OnTouchListener gl = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        };
        scroll.setOnTouchListener(gl);
    }

    /**
     * Unused - no sensors in fragment.
     */
    private void handleShakeEvent(int count) {
        Toast.makeText(getActivity(), getResources().getString(R.string.loading_new_updates),
                Toast.LENGTH_SHORT).show();
        GetUpdates(-1, maxUpdatesInRequest);
    }

    /**
     * Getting User from bundle.
     */
    private void SetUser() {
        Gson gson = new Gson();
        String json = settings.getString(getResources().getString(R.string.key_user), "");
        if(json.equals("")){
            Log.d(TAG, "No User defined");
        } else {
            user = gson.fromJson(json, UserInfo.class);
        }
    }

    /**
     * Init UI - attach UI components and defining listeners.
     * @param view
     * @param inflater
     */
    private void initUI(View view, LayoutInflater inflater) {
        painter = new UpdatePainter(getActivity(),this,this,inflater,user);
        updatesLayout = (LinearLayout) view.findViewById(R.id.updates_container);
        myActionBarActivity = (MyActionBarActivity) getArguments().
                getSerializable(getResources().getString(R.string.key_updates_pass_argument));
        myActionBarActivity.setMenuLabel(getActivity().getResources().getString(R.string.empty));
        scroll = (ScrollView) view.findViewById(R.id.update_scrollview);
        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scroll.getScrollY(); //for verticalScrollView
                if (scrollY == 0)
                    swipeRefresh = true;
                else
                    swipeRefresh = false;
            }
        });
    }

    /**
     * Reuqesting updates from server
     * @param num - from update value num
     * @param amount - amount of updates.
     */
    private void GetUpdates(long num, int amount) {
        String key = getResources().getString(R.string.key_get_updates);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String getUpdatesUrl = getResources().getString(R.string.request_get_updates_url)
                + String.valueOf(num) + getResources().getString(R.string.slash)
                + String.valueOf(amount);
        getEntity.execute(new String[]{getUpdatesUrl});
    }

    /**
     * setting shared prefences, cookie and user.
     */
    private void SetSharedPrefences() {
        settings = getActivity().getSharedPreferences(getResources().getString(R.string.data),
                getContext().MODE_PRIVATE);
        settings = getActivity().getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
        setCookie(getResources().getString(R.string.setCookie));
        SetUser();
    }

    /**
     * Callback function from server
     * @param result - output string from server.
     */
    @Override
    public void OnFinished(String result) {
        Log.d("RECIEVED:", result);
        if(getActivity()==null) return;
        try {
            //Getting updates.
            if (result.startsWith(getResources().getString(R.string.key_get_updates))) {
                result = result.substring(15);
                if (result.equals(getResources().getString(R.string.empty))) return;
                ReArrangeUpdates(result);
                painter.paint(updatesLayout, updates);
            //Approved shift update.
            } else if(result.startsWith(getResources().getString(R.string.key_approve_shift))){
                GetUpdates(-1,10);
            } else if(result.startsWith(getResources().getString(R.string.key_approev_login_req))){
                Log.d(TAG,result);
                Log.d(TAG,getResources().getString(R.string.key_approev_login_req));
                result = result.substring(22);
                CheckForSpecificUpdate(new JSONObject(result));
                CheckForSpecificUpdate(new JSONObject(result));
            }
            scroll.fullScroll(View.FOCUS_DOWN);
        } catch (JSONException e){
            e.printStackTrace();
            Log.d("JSON","Failed to parse JSON");
        }
    }

    private void CheckForSpecificUpdate(JSONObject jsonObject) {
        try {
            Integer updateNum = (jsonObject.has(getResources().getString(R.string.key_update_num)))?
                    jsonObject.getInt(getResources().getString(R.string.key_update_num)):null;
            if(updateNum==null) return;
            GetUpdates(updateNum,1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Arranging updates list by the result string from server
     * @param result - string from server containg updates.
     * @throws JSONException
     */
    private void ReArrangeUpdates(String result) throws JSONException {
        List<Update> downloadedUpdates = PullUpdates(result);
        for(Update update: downloadedUpdates){
            Boolean replaced = false;
            for(Update update1: updates){
                if(update.getNum()==update1.getNum()){
                    update1.setJson(update.getJson());
                    update1.setTime(update.getTime());
                    update1.setUpdate(update.getUpdate());
                    replaced = true;
                }
            }
            if(!replaced){
                updates.add(update);
            }
        }

        Collections.sort(updates, new Update());
    }

    /**
     * Pulling updates from result string from server
     * @param result - string from server
     * @return list of updates
     * @throws JSONException
     */
    private List<Update> PullUpdates(String result) throws JSONException {
        List<Update> newUpdates = new ArrayList<>();
        JSONArray array = new JSONArray(result);
        for(int i = array.length() -1; i >= 0; i--){
            newUpdates.add(DechiperUpdate(array.getJSONObject(i)));
        }
        return newUpdates;
    }

    /**
     * getting an update object from json object
     * @param obj - json object
     * @return update object.
     * @throws JSONException
     */
    private Update DechiperUpdate(JSONObject obj) throws JSONException {
        return new Update(obj.getString(getResources().getString(R.string.type)),
                obj.getString(getResources().getString(R.string.time)),
                obj.getLong(getResources().getString(R.string.num)),
                new JSONObject(obj.getString(getResources().getString(R.string.json))));
    }

    /**
     * set method
     * @param cookie
     */
    @Override
    public void setCookie(String cookie) {
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
        if(getActivity()==null) return "";
        return settings.getString(getResources().getString(R.string.cookiekey),
                getResources().getString(R.string.empty));
    }

    /**
     * registing sensors and setting peer action bar information to empty.
     * @param first
     */
    @Override
    public void Shown(Boolean first) {
        if(!first) {
            if(myActionBarActivity!=null) {
                myActionBarActivity.setPeerStatus(getResources().getString(R.string.empty));
                myActionBarActivity.setPeerName(getResources().getString(R.string.empty));
            }
            if(mSensorManager==null || mShakeDetector==null || mAccelerometer==null) return;
            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            myActionBarActivity.setPeerStatus(getResources().getString(R.string.empty));
            myActionBarActivity.setPeerName(getResources().getString(R.string.empty));
        }
        shown = true;
    }

    /**
     * Unregisting recievers
     */
    @Override
    public void Hidden() {
        if(mSensorManager!=null)
            mSensorManager.unregisterListener(mShakeDetector);
        shown = false;
    }

    /**
     * get method.
     * @return
     */
    @Override
    public Boolean isShown() {
        return shown;
    }

    /**
     * callback function from sensors - NOT USED.
     */
    @Override
    public void Callback() {
        if(swipeRefresh) {
            Toast.makeText(getActivity(), (String) getResources().getString(R.string.loading_updates),
                    Toast.LENGTH_SHORT).show();
            if (updates.isEmpty())
                GetUpdates(-1, maxUpdatesInRequest);
            else
                GetUpdates(updates.get(0).getNum(), maxUpdatesInRequest);
        }
    }
}
