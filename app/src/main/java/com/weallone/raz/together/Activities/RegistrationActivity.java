package com.weallone.raz.together.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.AsyncTasks.SendUser;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Enums.Account;
import com.weallone.raz.together.Fragments.CharChooser;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Packs.UserPack;
import com.weallone.raz.together.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This activity represents the registeration activity. (Figure selection & name-email-password)
 */
public class RegistrationActivity extends AppCompatActivity implements AsyncResponse, Cookied, View.OnClickListener {

    private static final String TAG = "RegistrationActivity";
    private SharedPreferences settings;
    private EditText userEditText = null;
    private TextView userName = null;
    private EditText passEditText = null;
    private Boolean validEmail = false;
    private Button submitBtn = null;
    private String entity = null;
    private String token = null;
    private String name;
    //private TextView exitText = null;

    private View.OnFocusChangeListener emailListener = null;
    private View.OnFocusChangeListener passsListener = null;
    private String pEmail = "";
    private String pPhone = "";

    /**
     * Standard OnCreate method
     * init shared prefences.
     * init listeners - for changed focus of password and email.
     * init UI.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initSharedRefences();
        initListeners();
        initUI();
        getParentData();
    }

    private void getParentData() {
        String key = getResources().getString(R.string.key_get_parent);
        String token = FirebaseInstanceId.getInstance().getToken();
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String parentDataUrl = getResources().getString(R.string.request_get_parent_url)
                + token;
        getEntity.execute(new String[]{parentDataUrl});
    }

    /**
     * Init listeners,
     * Each listener valaidaites the input when the entity loss focus.
     * Emits a Toast message if the input is invalid.
     */
    private void initListeners() {
        emailListener = new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) return;
                CheckIfEmailExists(userEditText.getText().toString());
            }
        };
        passsListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) return;
                if(passEditText.getText().toString().length()<8){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.regist_pass_more_than),
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * Emits a request to server for checcking if an email like input is already exists.
     * @param email - to check
     */
    private void CheckIfEmailExists(String email) {
        email += "@gmail.com";//adding postfix gmail.com for relaxing registration.
        String key = getResources().getString(R.string.key_get_exists);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String emailExistsUrl = getResources().getString(R.string.request_get_if_email_exists_url)
                + email;
        getEntity.execute(new String[]{emailExistsUrl});
    }

    /**
     * init shared prefences.
     * entity and token.
     */
    private void initSharedRefences() {
        settings = getSharedPreferences(getResources().getString(R.string.data), MODE_PRIVATE);
        settings = getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
        setCookie(getResources().getString(R.string.setCookie));

        entity = getIntent().getStringExtra(getResources().getString(R.string.key_user_entity));
        token = getIntent().getStringExtra(getResources().getString(R.string.key_user_code));
    }

    /**
     * Attach object to UI and listeners.
     */
    private void initUI() {
        SetCharFragment();
        String firstName = getIntent().getStringExtra(getResources().getString(R.string.key_first_name));
        String lastName = getIntent().getStringExtra(getResources().getString(R.string.key_last_name));
        name = (firstName != null ? firstName  : "") + (lastName != null ? " " + lastName : "");
        userEditText = (EditText) findViewById(R.id.regist_user);
        passEditText = (EditText) findViewById(R.id.regist_password);
        submitBtn = (Button) findViewById(R.id.regist_submit_btn);
        //exitText = (TextView) findViewById(R.id.regist_exit);
        userName = (TextView) findViewById(R.id.username);
        userName.setText(name);

        /*exitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.putExtra(getResources().getString(R.string.check_approve), false);
                startActivity(intent);
            }
        });*/

        userEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckIfEmailExists(userEditText.getText().toString());
            }
        });
        pullDataFromIntent();

        userEditText.setOnFocusChangeListener(emailListener);
        passEditText.setOnFocusChangeListener(passsListener);
        submitBtn.setOnClickListener(this);
    }

    private void pullDataFromIntent() {
        Intent intent = getIntent();
        String firstName = "";
        String lastName = "";
        if(intent.hasExtra(getResources().getString(R.string.key_user_first_name))){
            firstName = intent.getStringExtra(getResources().getString(R.string.key_user_first_name));
        }
        if(intent.hasExtra(getResources().getString(R.string.key_user_last_name))){
            lastName = intent.getStringExtra(getResources().getString(R.string.key_user_last_name));
        }
        name = (firstName != null ? firstName  : "") + (lastName != null ? " " + lastName : "");

        if(intent.hasExtra(getResources().getString(R.string.key_entity))){
            entity = intent.getStringExtra(getResources().getString(R.string.key_entity));
        }
        if(intent.hasExtra(getResources().getString(R.string.key_user_code))){
            token = intent.getStringExtra(getResources().getString(R.string.key_user_code));
        }
    }

    private void SetCharFragment(){
        // Create new fragment and transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.registration_char_chooser_frame, new CharChooser());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void OnFinished(String result) {
        Log.d(TAG, "RESULT:" + result);
        try {
            if(result.startsWith(getResources().getString(R.string.key_get_exists))){
                result = result.substring(14);
                Log.d(TAG, result);
                if(result.startsWith(getResources().getString(R.string.regist_exits))){
                    validEmail = false;
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.regist_this_user_is_regist),
                            Toast.LENGTH_SHORT).show();
                } else if(result.startsWith(getResources().getString(R.string.regists_not_exists))){
                    Log.d(TAG + "$$", String.valueOf(validEmail));
                    validEmail = true;
                }
            } else if(result.startsWith(getResources().getString(R.string.key_send_user))){
                result = result.substring(13);
                if(result.equals(getResources().getString(R.string.empty))) return;
                Account account = SaveToSharedPrefences(result);
                if(account==Account.CHILD){
                    startActivity(new Intent(this, ChildActivity.class));
                } else if(account==Account.PSYCHOLOGIST){
                    startActivity(new Intent(this, PsychoActivity.class));
                } else if(account==Account.MANAGER || account==Account.DEVELOPER){
                    startActivity(new Intent(this, ManagerActivity.class));
                }
            } else if(result.startsWith(getResources().getString(R.string.key_get_parent))){
                result = result.substring(14);
                if(result.equals(getResources().getString(R.string.empty))) return;
                JSONObject obj = new JSONArray(result).getJSONObject(0);
                pEmail = (obj.has(getResources().getString(R.string.key_parent_email)))?
                        obj.getString(getResources().getString(R.string.key_parent_email)):"";
                pPhone = (obj.has(getResources().getString(R.string.key_parent_phone)))?
                        obj.getString(getResources().getString(R.string.key_parent_phone)):"";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Account SaveToSharedPrefences(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        UserInfo userInfo= DecihperUserInfo(obj);
        Gson gson = new Gson();
        String user_json = gson.toJson(userInfo);
        settings.edit().putString(getResources().getString(R.string.key_user), user_json).apply();
        settings.edit().putString(getResources().getString(R.string.account_type),userInfo.getAccount().toString()).apply();
        Log.d(TAG, "SAVED USER: " + userInfo.toString());
        return userInfo.getAccount();
    }

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

    @Override
    public void setCookie(String cookie) {
        //set Cookie to Shared references.
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(getResources().getString(R.string.cookiekey), cookie);
        edit.apply();
    }

    @Override
    public String getCookie() {
        return settings.getString(getResources().getString(R.string.cookiekey),
                getResources().getString(R.string.empty));
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG + "$$", String.valueOf(validEmail));
        if(!validEmail){
            if(!isEmailValid(userEditText.getText().toString()+"@gmail.com")){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.regist_insert_valid_email),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.regist_this_user_is_regist),
                        Toast.LENGTH_SHORT).show();
            }
            return;
        } else if(passEditText.getText().toString().length()<=7){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.regist_pass_more_than),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        CreateUser();
    }

    private void CreateUser() {
        String keys[] = {
                getResources().getString(R.string.key_user_id),
                getResources().getString(R.string.key_user_entity),
                getResources().getString(R.string.key_user_name),
                getResources().getString(R.string.key_user_icon),
                getResources().getString(R.string.key_user_deviceId),
                getResources().getString(R.string.key_user_email),
                getResources().getString(R.string.key_user_pass),
                getResources().getString(R.string.key_parent_email),
                getResources().getString(R.string.key_parent_phone),
        };
        String email = userEditText.getText().toString()+ "@gmail.com";
        String image = settings.getString(getResources().getString(R.string.key_user_character), null);
        String password = passEditText.getText().toString();
        settings.edit().putString(getResources().getString(R.string.user_pass), password);
        if(entity!=null) Log.d(TAG,entity);
        UserInfo user = new UserInfo("",entity,name,image, token, email, password, pEmail, pPhone);
        String key = getResources().getString(R.string.key_send_user);
        SendUser sendUser = new SendUser(this, key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie), this);
        String sendUserUrl = getResources().getString(R.string.request_send_user_url);
        sendUser.execute(new UserPack[]{new UserPack(sendUserUrl,user,keys)});

    }

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
}
