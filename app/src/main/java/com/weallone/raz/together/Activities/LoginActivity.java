package com.weallone.raz.together.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.AsyncTasks.SendForgotPass;
import com.weallone.raz.together.AsyncTasks.SendLogin;
import com.weallone.raz.together.AsyncTasks.SendLoginRequest;
import com.weallone.raz.together.Entities.ForgotPass;
import com.weallone.raz.together.Entities.Login;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Enums.Account;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Packs.ForgotPassPack;
import com.weallone.raz.together.Packs.LoginPack;
import com.weallone.raz.together.R;
import com.weallone.raz.together.Tools.MyForebaseMessagingService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents the activity login interface.
 * It contains the main initial registration and a login option.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse, Cookied {

    private static final String TAG = "LoginActivity";
    private EditText inputTextPhone = null;
    private Button submitBtn = null;
    private TextView loginButton = null;
    private TextView forgotPassword = null;
    private EditText emailEditText = null;
    private EditText passEditText = null;
    private Button loginSubmitBtn = null;
    private LinearLayout loginPanel = null;
    private Boolean sentRequestRecently = false;
    private SharedPreferences settings;
    private BroadcastReceiver reciever = new Reciever();
    private TextView promptMessage = null;
    private TextView promptMessageLink = null;
    private LinearLayout promptMessageContainer = null;
    private LinearLayout registeratingPanel = null;

    /**
     * Standard onCreate method.
     * Setting the Sharedd Prefences objects, initating the UI
     * and checking if this device is allreaddy approved by the server.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetSharedPrefences();
        initUI();
        IsApproved();
    }

    /**
     * Emitting a Post request to check if this device ID is approved by the server.
     */
    private void IsApproved() {
        Intent intent = getIntent();
        if(intent.hasExtra(getResources().getString(R.string.check_approve))
                && !intent.getBooleanExtra(getResources().getString(R.string.check_approve),true))
            return;
        String code = FirebaseInstanceId.getInstance().getToken();
        String key = getResources().getString(R.string.key_is_approved);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String getuserStatus = getResources().getString(R.string.request_is_approved_url)
                + code;
        getEntity.execute(new String[]{getuserStatus});
    }

    /**
     * Linking between the UI objects and defineing listerners.
     */
    private void initUI() {
        inputTextPhone = (EditText) findViewById(R.id.login_input_phone);
        submitBtn = (Button) findViewById(R.id.login_send);
        loginButton = (TextView) findViewById(R.id.login_button);
        loginPanel = (LinearLayout) findViewById(R.id.login_panel);
        emailEditText = (EditText) findViewById(R.id.login_email);
        passEditText = (EditText) findViewById(R.id.login_password);
        loginSubmitBtn = (Button) findViewById(R.id.login_submit_btn);
        forgotPassword = (TextView) findViewById(R.id.login_forgot_password_text);
        promptMessageContainer = (LinearLayout) findViewById(R.id.login_prompt_message_container);
        promptMessageLink = (TextView) findViewById(R.id.login_prompt_message_link);
        promptMessage = (TextView) findViewById(R.id.login_prompt_message);
        registeratingPanel = (LinearLayout) findViewById(R.id.login_register_panel);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayDialogForgotPassMessage();
            }
        });
        loginSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginPanel.getVisibility() == View.INVISIBLE) {
                    loginPanel.setVisibility(View.VISIBLE);
                } else {
                    loginPanel.setVisibility(View.INVISIBLE);
                }
            }
        });

        submitBtn.setOnClickListener(this);

        Log.d(TAG, String.valueOf(settings.getBoolean(getResources().getString(R.string.request_sent), false)));
        if(settings.getBoolean(getResources().getString(R.string.request_sent), false)){
            promptMessageContainer.setVisibility(View.VISIBLE);
            registeratingPanel.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            promptMessageLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.beyahad-psy.co.il"));
                    startActivity(browserIntent);
                }
            });
        }
    }

    /**
     * Defining and displaying the 'forgot password' message.
     */
    private void DisplayDialogForgotPassMessage() {

        //Define:
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        SendPasswordToEmail();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        if(emailEditText.getText().toString().length()<3){
            Toast.makeText(this, getResources().getString(R.string.please_insert_your_email), Toast.LENGTH_SHORT).show();
            return;
        }
        //Building:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Showing:
        builder.setMessage(getResources().getString(R.string.send_password_to_email)
                + " " + emailEditText.getText().toString()
                + " " + getResources().getString(R.string.send_password_to_email1)).setPositiveButton(
                getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();
    }

    /**
     * Emits a Post request to send the user password to this saved user email account.
     * The SMTP submmiting is executed in the back-end server side.
     */
    private void SendPasswordToEmail() {
        String email = emailEditText.getText().toString();
        String code = FirebaseInstanceId.getInstance().getToken();

        String keys[] = {
                getResources().getString(R.string.email_key),
                getResources().getString(R.string.code_key)
        };
        String key = getResources().getString(R.string.key_null_nothing);
        SendForgotPass sendForgotPass = new SendForgotPass(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie), this);
        String forgotPassUrl = getResources().getString(R.string.request_send_password_url);
        sendForgotPass.execute(new ForgotPassPack[]{new ForgotPassPack(forgotPassUrl, new ForgotPass(
                email, code), keys)});
    }

    /**
     * Emitting a login request.
     * Sending the email + pass information to the server and awaits for an answer if the
     * information is valid.
     */
    private void LoginRequest() {
        Log.d(TAG, String.valueOf(sentRequestRecently));
        if(emailEditText.getText().toString().equals(getResources().getString(R.string.empty))
                || passEditText.getText().toString().equals(getResources().getString(R.string.empty)))
            return;
        String keys[] = {
                getResources().getString(R.string.key_user_email).toString(),
                getResources().getString(R.string.key_user_pass).toString()
        };
        SendLogin sendLogin = new SendLogin(this,getResources().getString(R.string.key_login_exists),this,
                getResources().getString(R.string.getcookie), getResources().getString(R.string.setCookie), this);
        String loginURL = getResources().getString(R.string.request_login_url);
        String email = emailEditText.getText().toString();
        if(!isEmailValid(email)){
            email = email + "@gmail.com";//in this case it is a user registered with a user name only
        }
        sendLogin.execute(new LoginPack[]{new LoginPack(loginURL, new Login(email,
                passEditText.getText().toString()), keys)});
    }



    /**
     * Representing the execution of an on-click 'sign up' action.
     * Emiting a send request to the server.
     * @param v - the view.
     */
    @Override
    public void onClick(View v) {
        if(validateInput() && !sentRequestRecently) {
            Toast.makeText(this, getResources().getString(R.string.login_request_sent), Toast.LENGTH_LONG).show();
            if (!inputTextPhone.getText().toString().equals(getResources().getString(R.string.empty))){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //for sychronization.
                            Thread.sleep(2000);
                            SendRequest(getResources().getString(R.string.login_key_2), inputTextPhone.getText().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
            sentRequestRecently = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        sentRequestRecently = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * Send the request to the server.
     * the request containes the first name, last name and device Id.
     * @param key email/phone
     * @param val value.
     */
    private void SendRequest(String key, String val) {
        if(((TextView) findViewById(R.id.login_first_name)).getText().toString().length()==0
                || ((TextView) findViewById(R.id.login_last_name)).getText().toString().length()==0)
            return;
        HashMap<String, String> hash = new HashMap<>();
        hash.put(key,val);
        hash.put(getResources().getString(R.string.key_phone_code), FirebaseInstanceId.getInstance().getToken());
        hash.put(getResources().getString(R.string.first_name),
                ((TextView) findViewById(R.id.login_first_name)).getText().toString());
        hash.put(getResources().getString(R.string.last_name),
                ((TextView) findViewById(R.id.login_last_name)).getText().toString());
        new SendLoginRequest(this,getResources().getString(R.string.key_login_req), this,
                getResources().getString(R.string.getcookie), getResources().getString(R.string.setCookie),
                getString(R.string.request_login_req_url), hash, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        settings.edit().putBoolean(getResources().getString(R.string.request_sent), true).apply();
        registeratingPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                promptMessageContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Checking that the input inserted is valid.
     * validation for email & phonoe insertion.
     * @return
     */
    private boolean validateInput() {

        Boolean emptyPhone = inputTextPhone.getText().toString().trim().equals(getResources().getString(R.string.empty));
        Boolean validPhone = isAllNumbers(inputTextPhone.getText().toString());

        if(validPhone){
            return true;
        }
        else if(!emptyPhone && !validPhone){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_insert_valid_phone),
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Check string phone containg only numbers.
     * @param phone - string to check.
     * @return - true/false.
     */
    public static boolean isAllNumbers(String phone) {
        boolean isValid = false;

        Log.d(TAG,phone);
        String expression = "\\d+";
        CharSequence inputStr = phone;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Check if this is a valid email
     * @param email - string to check
     * @return - true/false
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Callback function - for returning GET/POST requests.
     * @param result - the action + returning string from server.
     */
    @Override
    public void OnFinished(String result) {
        Log.d(TAG,"RECIEVED:" + result);
        try {
            //Case of registeration approval:
            if(result.startsWith(getResources().getString(R.string.key_is_approved))){
                result = result.substring(15);
                Log.d(TAG,result);
                if(result.equals(getResources().getString(R.string.empty))) return;
                else if(result.equals(getResources().getString(R.string.not_approved))) return;
                else {
                        JSONObject obj = new JSONObject(result);
                        String first_name = obj.getString(getResources().getString(R.string.key_first_name));
                        String last_name = obj.getString(getResources().getString(R.string.key_last_name));
                        String entity = obj.getString(getResources().getString(R.string.key_user_entity));
                        String token = obj.getString(getResources().getString(R.string.code));
                        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                        intent.putExtra(getResources().getString(R.string.key_first_name),first_name);
                        intent.putExtra(getResources().getString(R.string.key_last_name),last_name);
                        intent.putExtra(getResources().getString(R.string.key_user_entity),entity);
                        intent.putExtra(getResources().getString(R.string.key_user_code),token);
                        startActivity(intent);
                }
            //Case off successfull login:
            } else if (result.startsWith(getResources().getString(R.string.key_login_exists))){
                result = result.substring(16);
                if(result.equals(getResources().getString(R.string.empty))) {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.wrong_email_or_pass),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Account account = null;
                    account = SaveToSharedPrefences(result);
                if(account==Account.CHILD){
                    startActivity(new Intent(this, ChildActivity.class));
                } else if(account==Account.PSYCHOLOGIST){
                    startActivity(new Intent(this, PsychoActivity.class));
                } else if(account==Account.MANAGER || account==Account.DEVELOPER){
                    startActivity(new Intent(this, ManagerActivity.class));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saving the sure to shared prefences.
     * @param result - result string from user.
     * @return - the entity approved.
     * @throws JSONException - exception
     */
    private Account SaveToSharedPrefences(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        UserInfo userInfo= DecihperUserInfo(obj);
        Gson gson = new Gson();
        String user_json = gson.toJson(userInfo);
        settings.edit().putString(getResources().getString(R.string.key_user), user_json).apply();
        settings.edit().putString(getResources().getString(R.string.account_type),userInfo.getAccount().toString()).apply();
        return userInfo.getAccount();
    }

    /**
     * Creation of user object from JSONobject of server.
     * @param obj - JSON object
     * @return the UserInfo object
     * @throws JSONException - exception
     */
    private UserInfo DecihperUserInfo(JSONObject obj) throws JSONException {
        return new UserInfo(obj.getString(getResources().getString(R.string.key_id)),
                obj.getString(getResources().getString(R.string.key_entity)),
                obj.getString(getResources().getString(R.string.key_name)),
                obj.getString(getResources().getString(R.string.key_icon)),
                obj.getString(getResources().getString(R.string.key_user_deviceId)),
                obj.getString(getResources().getString(R.string.key_user_email)),
                obj.getString(getResources().getString(R.string.key_user_pass)),
                obj.getString(getResources().getString(R.string.key_parent_email)),
                obj.getString(getResources().getString(R.string.key_parent_phone)));
    }

    private void SetSharedPrefences() {
        settings = getSharedPreferences(getResources().getString(R.string.data), MODE_PRIVATE);
        settings = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
        setCookie(getResources().getString(R.string.setCookie));
    }

    /**
     * Registing Firebase service.
     */
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyForebaseMessagingService.BROADCAST_ACTION);
        registerReceiver(reciever, filter);
    }

    /**
     * Unregisting service.
     */
    @Override
    protected void onPause() {
        unregisterReceiver(reciever);
        super.onPause();
    }

    /**
     * Setting cookie object to shared prefences.
     * @param cookie - the string value.
     */
    @Override
    public void setCookie(String cookie) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(getResources().getString(R.string.cookiekey), cookie);
        edit.apply();
    }

    /**
     * Getting the cookie object.
     * @return
     */
    @Override
    public String getCookie() {
        return settings.getString(getResources().getString(R.string.cookiekey),
                getResources().getString(R.string.empty));
    }

    /**
     * Reciever object to listen for Firebase service BROADCAST messages.
     */
    class Reciever extends BroadcastReceiver {
        /**
         * When serviece broadcast, this method get the updates from server.
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra(getResources().getString(R.string.fragment))
                    .equals(getResources().getString(R.string.login_fragment))){
                //Got approval from server, saving to shared prefences and starting registeration activity.
                String first_name = intent.getStringExtra(getResources().getString(R.string.key_user_first_name));
                String last_name = intent.getStringExtra(getResources().getString(R.string.key_user_last_name));
                String entity =intent.getStringExtra(getResources().getString(R.string.key_entity));
                String code =intent.getStringExtra(getResources().getString(R.string.key_user_code));
                Intent intentRegisteration = new Intent(LoginActivity.this, RegistrationActivity.class);
                intentRegisteration.putExtra(getResources().getString(R.string.key_user_first_name),
                        first_name);
                intentRegisteration.putExtra(getResources().getString(R.string.key_user_last_name),
                        last_name);
                intentRegisteration.putExtra(getResources().getString(R.string.key_entity),
                        entity);
                intentRegisteration.putExtra(getResources().getString(R.string.key_user_code),
                        code);
                startActivity(intentRegisteration);
            }
        }
    }
}
