package com.weallone.raz.together.Activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.FirebaseApp;
import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.AsyncTasks.GetEntityCheck;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.R;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Collections;

import javax.net.ssl.SSLEngineResult;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * This activity represents the ActionBar and Other uniform behavior for all app activities.
 */
public class MyActionBarActivity extends AppCompatActivity implements AsyncResponse, Cookied, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MyActionBarActivity";
    private static final int RC_AUTHORIZE_CONTACTS = 3;
    private SharedPreferences settings = null;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private SharedPreferences sharedPreferences;
    private GoogleAccountCredential mCredential = null;
    private GoogleAccountCredential credential;
    private static final int CHOOSE_ACCOUNT = 1;
    private static final int REQUEST_AUTHORIZATION = 2;
    private Account mAuthorizedAccount;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Get method
     * @return answer
     */
    public Boolean getAnswer() {
        return answer;
    }

    /**
     * Set method
     * @param answer
     */
    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    private String conversation = null;
    private Boolean answer = false;

    private TextView title = null;
    private TextView chatPeerName = null;
    private TextView chatPeerStatus = null;

    /**
     * Get method
     * @return
     */
    public String getConversation() {
        return conversation;
    }

    /**
     * Set method
     * @param conversation
     */
    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    /**
     * Set method.
     * @param _title
     */
    public void setTitle(String _title){
        if(title!=null){
            title.setText(_title);

        }
    }

    /**
     * Child and Psycho Override method and there send to Chat and setConversation
     * @param num not used
     * @param id not used
     * @param name not used
     */
    public void setChatImmediateCall(String num, String id, String name){}

    /**
     * Set method.
     * @param _name
     */
    public void setPeerName(String _name){
        if(chatPeerName!=null){
            chatPeerName.setText(_name);
        }
    }

    /**
     * Set method.
     * @param _status
     */
    public void setPeerStatus(String _status){
        if(chatPeerStatus!=null){
            chatPeerStatus.setText(_status);
        }
    }

    /**
     * get method
     * @return
     */
    public GoogleAccountCredential getmCredential() {
        return mCredential;
    }

    /**
     * Set mehtod.
     * @param mCredential
     */
    public void setmCredential(GoogleAccountCredential mCredential) {
        this.mCredential = mCredential;
    }

    /**
     * Oncreate standard method.
     * init shared prefences and setting action bar.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_action_bar);
        SetSharedPrefences();
        getAuth();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.color_activity_bar)));
        getSupportActionBar().setElevation(0);
        sharedPreferences = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setupActionBar();

    }

    /** Authorizes the installed application to access user's protected data. */
//    private static Credential authorize() throws Exception {
//        // load client secrets
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
//                new InputStreamReader(CalendarSample.class.getResourceAsStream("/client_secrets.json")));
//        // set up authorization code flow
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, JSON_FACTORY, clientSecrets,
//                Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
//                .build();
//        // authorize
//        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//    }

    private void getAuth() {
//        credential = GoogleAccountCredential.usingOAuth2(this, DriveScopes.DRIVE, PlusScopes.PLUS_ME);
//        startActivityForResult(credential.newChooseAccountIntent(), CHOOSE_ACCOUNT);
    }

    /**
     * Setting action bar,
     * setting title
     * and attaching name and status titles for Chat UI.
     */
    private void setupActionBar() {
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu_main_user_item, null);

        chatPeerName = (TextView) v.findViewById(R.id.chat_menu_peer_name);
        chatPeerStatus = (TextView) v.findViewById(R.id.chat_menu_peer_status);

        ab.setCustomView(v);

    }

    /**
     * Emitting an update request for the server - notifying this user
     * deleting the app.
     */
    @Override
    protected void onDestroy() {
        String key = getResources().getString(R.string.key_set_last_login_time);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String destroyAppUrl = getResources().getString(R.string.request_destroy_app_url);
        getEntity.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[]{destroyAppUrl});
        super.onDestroy();
    }

    /**
     * For Menu action bar.
     * @param menu - default
     * @return true;
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Sending request to server - this user is logged.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setLoginTime(getResources().getString(R.string.logged_in));
    }

    /**
     * Sendign request to server - this user is not logged in.
     */
    @Override
    protected void onPause() {
        setLoginTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        super.onPause();
    }

    /**
     * Emitting the request to server.
     * Notyifing when is the last time this user was logged in .
     * @param time
     */
    private void setLoginTime(String time){
        SetSharedPrefences();
        UserInfo userInfo = null;
        Gson gson = new Gson();
        if(settings==null) return;
        String json = settings.getString(getResources().getString(R.string.key_user), "");
        if(json.equals("")){
        } else {
            userInfo = gson.fromJson(json, UserInfo.class);
        }
        if(userInfo==null) return;
        String key = getResources().getString(R.string.key_set_last_login_time);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String setLastloginTime = getResources().getString(R.string.request_set_last_login_time_url)
                + userInfo.getId() + getResources().getString(R.string.slash) + time;
        getEntity.execute(new String[]{setLastloginTime});
    }

    /**
     * Defining the menu options of this action bar.
     * 1. deleting this user credential.
     * 2. defining a google account for the calendar options.
     * @param item - default.
     * @return true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.reset_account_btn:
                sharedPreferences.edit().remove(getResources().getString(R.string.account_type))
                        .apply();
                SharedPreferences settings = getApplicationContext().getSharedPreferences(
                        getResources().getString(R.string.prefs), 0);
                settings.edit().remove(getResources().getString(R.string.key_user))
                        .apply();
                settings.edit().remove(getResources().getString(R.string.key_user_approve))
                        .apply();
                Intent intent = new Intent(MyActionBarActivity.this,LoginActivity.class);
                intent.putExtra(getResources().getString(R.string.check_approve), false);
                startActivity(intent);
                return true;
            case R.id.menu_choose_account:
                setGoogleAccount();
//                authorizeContactsAccess();
                if(mCredential!=null){
                    startActivityForResult(mCredential.newChooseAccountIntent(),
                            REQUEST_ACCOUNT_PICKER);
                    return true;
                } else {
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void authorizeContactsAccess() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
        .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_AUTHORIZE_CONTACTS);
    }


    /**
     * Premmitions and Choice of google account option.
     * Starting activity and getting the result.
     */
    private void setGoogleAccount() {
        if (EasyPermissions.hasPermissions(
                this, new String[]{android.Manifest.permission.GET_ACCOUNTS})) {
            if (mCredential != null) {
                startActivityForResult(mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    new String[]{android.Manifest.permission.GET_ACCOUNTS});
        }
    }

    /**
     //     * Called when an activity launched here (specifically, AccountPicker
     //     * and authorization) exits, giving you the requestCode you started it with,
     //     * the resultCode it returned, and any additional data from it.
     //     * @param requestCode code indicating which activity result is incoming.
     //     * @param resultCode code indicating the result of the incoming
     //     *     activity result.
     //     * @param data Intent (containing result data) returned by incoming
     //     *     activity result.
     //     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
//            case REQUEST_GOOGLE_PLAY_SERVICES:
//                if (resultCode != RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
//                } else {
//                    getResultsFromApi();
//                }
//                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(getResources().getString(R.string.key_account_google_name), accountName);
                        editor.apply();
                        Log.d(TAG,"saved: " + accountName);
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "authorized");
//                    getResultsFromApi();
                } else {
                    Log.d(TAG, "not authorized");
                }
                break;
//            case RC_AUTHORIZE_CONTACTS:
//                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//                if (result.isSuccess) {
//                    GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
//                    mAuthorizedAccount = googleSignInAccount.getAccount();
//                    getContacts();
//                }
            case RC_AUTHORIZE_CONTACTS:
                Log.d(TAG,"###");
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(getResources().getString(R.string.key_account_google_name), accountName);
                        editor.apply();
                        Log.d(TAG,"saved: " + accountName);
                    }
                }
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                Log.d(TAG,result.getStatus().toString());
                if (result.getStatus().isSuccess()) {
                    GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
//                    mAuthorizedAccount = googleSignInAccount.getAccount();
                }




        }
    }

    /**
     * For menu option - setting the action bar title.
     * @param menuLabel
     */
    public void setMenuLabel(String menuLabel) {
        if(getActionBar()!=null) getActionBar().setTitle(menuLabel);
        if(getSupportActionBar()!=null) getSupportActionBar().setTitle(menuLabel);
    }

    /**
     * Not implememnted.
     * @param result
     */
    @Override
    public void OnFinished(String result) {

    }

    /**
     * Setting the shared prefences object.
     */
    private void SetSharedPrefences() {
        settings = getSharedPreferences(getResources().getString(R.string.data), MODE_PRIVATE);
        settings =  getApplicationContext().getSharedPreferences(getResources().getString(R.string.prefs), 0);
    }

    /**
     * Setting the cookie.
     * @param cookie
     */
    @Override
    public void setCookie(String cookie) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(getResources().getString(R.string.cookiekey), cookie);
        edit.apply();
    }

    /**
     * Getting the cookie.
     * @return
     */
    @Override
    public String getCookie() {
        return settings.getString(getResources().getString(R.string.cookiekey),
                getResources().getString(R.string.empty));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG," FAILED TO CONNECT ###");
    }
}
