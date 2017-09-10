package com.weallone.raz.together.Fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.weallone.raz.together.Activities.MyActionBarActivity;
import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.AsyncTasks.SendHelpRequest;
import com.weallone.raz.together.AsyncTasks.SendQuote;
import com.weallone.raz.together.Entities.HelpRequest;
import com.weallone.raz.together.Entities.Quote;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Enums.Account;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Interfaces.TabAbleFragment;
import com.weallone.raz.together.Packs.HelpRequestPack;
import com.weallone.raz.together.Packs.QuotePack;
import com.weallone.raz.together.Painters.QuotesPainter;
import com.weallone.raz.together.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Fragment represnets the Quotes window from 3-tab container.
 */
public class Quotes extends Fragment implements TabAbleFragment, AsyncResponse, Cookied, View.OnClickListener {

    private String TAG= "Quotes";
    private LinearLayout quotesContainer;
    private Button addQuote;
    private LinearLayout quoteCreationContainer;
    private LinearLayout helpContainer;
    private EditText createAuthorTextBox;
    private EditText createContentTextBox;
    private Button submitNewQuoteBtn;
    private FloatingActionButton helpSignBtn;
    private QuotesPainter painter;
    private SharedPreferences settings;
    private UserInfo user;
    private List<Quote> quotes = new ArrayList<>();
    private MyActionBarActivity myActionBarActivity = null;
    private EditText helpTextBox;
    private Button helpSubmitBtn;
    private boolean exitedTimeout = false;
    private Boolean shown = false;
    private LinearLayout[] faceContainers = new LinearLayout[5];
    private TextView[] facesText = new TextView[5];
    private ScrollView quotesScrollView;

    /**
     * default construcot
     */
    public Quotes() {
        // Required empty public constructor
    }

    /**
     * Quote fragment creation method
     * @param actionBarActivity - activity
     * @return Quote fragment object.
     */
    public static Quotes newInstance(MyActionBarActivity actionBarActivity){
        Quotes fragment = new Quotes();
        Bundle bundle = new Bundle();
        bundle.putSerializable(actionBarActivity.getResources().getString(R.string.key_quotes_pass_argument), (Serializable) actionBarActivity);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * onCreateView standard method.
     * Init shared prefences, UI, painter, and getting quotes.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = getActivity();
        if(activity != null){
            View view = inflater.inflate(R.layout.fragment_qoutes, container, false);
            SetSharedPrefences();
            InitUI(view);
            InitPainter(inflater);
            GetQuotes();
            clearChatLabels();
            // Inflate the layout for this fragment
            return view;
        }
        return null;
    }

    /**
     * Getting quotes from server.
     */
    private void GetQuotes() {
        String key = getResources().getString(R.string.key_get_quote);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String getQuotesUrl = getResources().getString(R.string.request_get_quotes_url);
        getEntity.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{getQuotesUrl});
    }

    /**
     * Setting shared prefences, cookie and defining user.
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
     * Setting User from Bundle.
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
     * Attaching UI components and defining listeners.
     * @param view
     */
    private void InitUI(View view) {
        quotesContainer = (LinearLayout) view.findViewById(R.id.quotes_container);
        addQuote = (Button) view.findViewById(R.id.quote_add_quote_btn);
        quoteCreationContainer = (LinearLayout) view.findViewById(R.id.quote_create_container);;
        createAuthorTextBox = (EditText) view.findViewById(R.id.quote_create_quote_author_textbox);
        createContentTextBox = (EditText) view.findViewById(R.id.quote_create_quote_content_textbox);;
        submitNewQuoteBtn = (Button) view.findViewById(R.id.quote_submit_new_quote);
        helpSignBtn = (FloatingActionButton) view.findViewById(R.id.quotes_help_sign);
        helpContainer = (LinearLayout) view.findViewById(R.id.quote_help_container);
        helpTextBox = (EditText) view.findViewById(R.id.quote_help_textbox);
        helpSubmitBtn = (Button) view.findViewById(R.id.quote_help_submit);
        quotesScrollView = (ScrollView) view.findViewById(R.id.quotes_scroll_view);

        initFaces(view);
        myActionBarActivity = (MyActionBarActivity) getArguments().
                getSerializable(getResources().getString(R.string.key_quotes_pass_argument));
        addQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quoteCreationContainer.getVisibility() == View.INVISIBLE) {
                    quoteCreationContainer.setVisibility(View.VISIBLE);
                } else {
                    quoteCreationContainer.setVisibility(View.INVISIBLE);
                    createAuthorTextBox.setText(getResources().getString(R.string.empty));
                    createContentTextBox.setText(getResources().getString(R.string.empty));
                }
            }
        });
        if(user.getAccount()!=Account.CHILD) {
            ((LinearLayout) view.findViewById(R.id.emergency_layout)).setVisibility(View.GONE);
        }
        helpSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpContainer.getVisibility() == View.INVISIBLE) {
                    helpContainer.setVisibility(View.VISIBLE);
                } else {
                    helpContainer.setVisibility(View.INVISIBLE);
                    helpTextBox.setText(getResources().getString(R.string.empty));
                }
            }
        });
        submitNewQuoteBtn.setOnClickListener(this);
        if(user.getAccount()== Account.CHILD){
            addQuote.setVisibility(View.GONE);
        }
        helpSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendHelpRequest();
            }
        });
    }

    /**
     * Setting the mode of the user, this mood will be later dispay in statistics fragment.
     * @param view
     */
    private void initFaces(View view) {
        for(int i=1; i<6;i++){
            Log.d(TAG, String.valueOf(i));
            faceContainers[i-1] = (LinearLayout) view.findViewById(getActivity().getResources().getIdentifier("qoute_layout_face"
                    + String.valueOf(i), "id", getActivity().getPackageName()));
            facesText[i-1] = (TextView) view.findViewById(getResources().getIdentifier("qoute_textview_face"
                    + String.valueOf(i), "id", getActivity().getPackageName()));
            final int face = i;
            faceContainers[i-1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.setFace(face-1);
                    //save user to shared prefences
                    Gson gson = new Gson();
                    String user_json = gson.toJson(user);
                    notifyServer(face-1);
                    settings.edit().putString(getResources().getString(R.string.key_user), user_json).apply();
                    settings.edit().putString(getResources().getString(R.string.account_type),user.getAccount().toString()).apply();
                    //setting all faces to regular
                    for(int j = 1; j < 6; j++){
                        facesText[j-1].setTextColor(Color.BLACK);
                        facesText[j-1].setTypeface(null, Typeface.NORMAL);
                        facesText[j-1].setTextSize(getResources().getDimension(R.dimen.small_text));
                    }
                    //setting choosen face.
                    facesText[face-1].setTextColor(getActivity().getResources().getColor(R.color.color_activity_bar));
                    facesText[face-1].setTypeface(null, Typeface.BOLD);
                    facesText[face-1].setTextSize(getResources().getDimension(R.dimen.small__small_mid_text));

                }
            });
        }
        for(int j = 1; j < 5; j++){
            facesText[j].setTextColor(Color.BLACK);
            facesText[j].setTypeface(null, Typeface.NORMAL);
            facesText[j].setTextSize(getResources().getDimension(R.dimen.small_text));
        }
        //checking if face allready initiated
        if(user.getFace()!=null){
            facesText[user.getFace()].setTextColor(getActivity().getResources().getColor(R.color.color_activity_bar));
            facesText[user.getFace()].setTypeface(null, Typeface.BOLD);
            facesText[user.getFace()].setTextSize(getResources().getDimension(R.dimen.small__small_mid_text));
        }
    }

    /**
     * Notifying server of the changed mood of user.
     * @param face - mood to change to
     */
    private void notifyServer(int face) {
        String id= user.getId();

        String key = getResources().getString(R.string.key_mood_changed);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String moodChangeUrl = getResources().getString(R.string.request_mood_change_url)
                + id + getResources().getString(R.string.slash) + String.valueOf(face);
        getEntity.execute(new String[]{moodChangeUrl});
    }

    /**
     * Sending help request to server.
     * simple request to server.
     */
    private void SendHelpRequest() {
        String[] keys = {
              getResources().getString(R.string.key_help_time),
              getResources().getString(R.string.key_help_child_id),
              getResources().getString(R.string.key_help_child_name),
              getResources().getString(R.string.key_help_message),
        };
        SendHelpRequest sendHelpRequest = new SendHelpRequest(this,
                getResources().getString(R.string.key_send_help), this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie), getActivity());
        HelpRequest helpRequest = new HelpRequest(String.valueOf(Calendar.getInstance().getTimeInMillis()),
                user.getId(),helpTextBox.getText().toString(),user.getName());
        String url = getResources().getString(R.string.request_send_help_request_url);
        sendHelpRequest.execute(new HelpRequestPack[]{new HelpRequestPack(url,helpRequest,keys)});
        helpTextBox.setText(getResources().getString(R.string.empty));
        helpContainer.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(),getResources().getString(R.string.message_sent_to_psycho),Toast.LENGTH_SHORT).show();
        quotesScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    /**
     * Init the quotes painter.
     * @param inflater
     */
    private void InitPainter(LayoutInflater inflater) {
        painter = new QuotesPainter(getActivity(),this,this,inflater,user);
    }

    /**
     * When this fragment appear. If the user stay in this fragment for more then two seconds
     * then a request is been sent to the server notifying someone lookks at quotes.
     * @param first
     */
    @Override
    public void Shown(Boolean first) {
        setHasOptionsMenu(false);
        shown = true;
        if(!first){
            myActionBarActivity.setPeerStatus(getResources().getString(R.string.empty));
            myActionBarActivity.setPeerName(getResources().getString(R.string.empty));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    exitedTimeout = false;
                    Thread.sleep(2000);
                    if(!exitedTimeout){
                        SendReadingQoutes();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Request for getting the quotes from server.
     */
    private void SendReadingQoutes() {
        Activity activity = getActivity();
        if(activity != null) {
            String key = getResources().getString(R.string.key_reading_quotes);
            GetEntity getEntity = new GetEntity(this, key, this,
                    getResources().getString(R.string.getcookie),
                    getResources().getString(R.string.setCookie));
            String readingQuotesUrl = getResources().getString(R.string.request_reading_qoutes_url);
            getEntity.execute(new String[]{readingQuotesUrl});
        }
    }

    /**
     * Clearing the pper name and status from actionbar(Chat).
     */
    private void clearChatLabels(){
        myActionBarActivity.setPeerStatus(getResources().getString(R.string.empty));
        myActionBarActivity.setPeerName(getResources().getString(R.string.empty));
    }

    /**
     * Adjusting members.
     */
    @Override
    public void Hidden() {
        exitedTimeout = true;
        shown = false;
    }

    /**
     * Get method
     * @return
     */
    @Override
    public Boolean isShown() {
        return shown;
    }

    /**
     * Callback function from server
     * @param result - string from server.
     */
    @Override
    public void OnFinished(String result) {
        Log.d("RECIEVED:", result);
        if(getActivity()==null) return;
        try {
            //Get Quotes:
            if (result.startsWith(getResources().getString(R.string.key_get_quote))) {
                result = result.substring(13);
                if (result.equals(getResources().getString(R.string.empty))) return;
                BuildQuotes(result);
                painter.paint(quotesContainer, quotes);
            //Quote Edited:
            } else if (result.startsWith(getResources().getString(R.string.key_edit_quote))){
                result = result.substring(14);
                if (result.equals(getResources().getString(R.string.empty))) return;
                EditQuote(result);
                painter.paint(quotesContainer, quotes);
            //Quote deleted:
            } else if (result.startsWith(getResources().getString(R.string.key_del_quote))){
                result = result.substring(13);
                if (result.equals(getResources().getString(R.string.empty))) return;
                GetQuotes();
            //Quote created:
            } else if(result.startsWith(getResources().getString(R.string.key_create_quote))){
                result = result.substring(16);
                quotes.add(DecihperQuote(new JSONObject(result)));
                painter.paint(quotesContainer, quotes);
            } else if(result.startsWith(getResources().getString(R.string.key_mood_changed))){
                Toast.makeText(getContext(),getResources().getString(R.string.mood_changed), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e){
            Log.d(TAG,"JSONException");
        }
    }

    /**
     * Edit quote request to server
     * @param result - dechipering the string from server and adjusting the quote in list.
     * @throws JSONException
     */
    private void EditQuote(String result) throws JSONException {
        Quote editedQuote = DecihperQuote(new JSONObject(result));
        List<Quote> newQuotes = new ArrayList<>();
        for(Quote q: quotes){
            if(q.getNum().equals(editedQuote.getNum())){
                newQuotes.add(editedQuote);
            } else {
                newQuotes.add(q);
            }
        }
        quotes = newQuotes;
    }

    /**
     * "Painting" the quotes to quotes list.
     * @param result - string frmo server.
     * @throws JSONException
     */
    private void BuildQuotes(String result) throws JSONException {
        quotes.clear();
        JSONArray array = new JSONArray(result);
        for(int i = 0; i < array.length(); i++){
            JSONObject obj = array.getJSONObject(i);
            quotes.add(DecihperQuote(obj));
        }
    }

    /**
     * Gettin Quote object from JSON object
     * @param obj - JSON object
     * @return Quote object.
     * @throws JSONException
     */
    private Quote DecihperQuote(JSONObject obj) throws JSONException {
        return new Quote(obj.getString(getResources().getString(R.string.key_quote_num)),
                obj.getString(getResources().getString(R.string.key_quote_content)),
                obj.getString(getResources().getString(R.string.key_quote_creator)));
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
        return settings.getString(getResources().getString(R.string.cookiekey),
                getResources().getString(R.string.empty));
    }

    /**
     * not used.
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * not used.
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Submiting new quote and sending to server.
     * adjusting envirnoment for next quote.
     * @param v - clicked
     */
    @Override
    public void onClick(View v) {
        SendQuote editQuote = new SendQuote(this, getResources().getString(R.string.key_create_quote),
                this, getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie), getActivity());
        String[] keys = {
                getResources().getString(R.string.key_quote_num),
                getResources().getString(R.string.key_quote_content),
                getResources().getString(R.string.key_quote_creator),
        };
        String content = createContentTextBox.getText().toString();
        String author = createAuthorTextBox.getText().toString();
        String editQuoteUrl = getResources().getString(R.string.request_create_quote_url);
        editQuote.execute(new QuotePack[]{new QuotePack(editQuoteUrl, new Quote(content, author), keys)});
        quoteCreationContainer.setVisibility(View.INVISIBLE);
        createContentTextBox.setText(getResources().getString(R.string.empty));
        createAuthorTextBox.setText(getResources().getString(R.string.empty));
    }
}
