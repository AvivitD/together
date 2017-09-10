package com.weallone.raz.together.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.weallone.raz.together.Activities.MyActionBarActivity;
import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.AsyncTasks.SendGeneric;
import com.weallone.raz.together.AsyncTasks.SendPost;
import com.weallone.raz.together.Entities.Comment;
import com.weallone.raz.together.Entities.Post;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Enums.Account;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Callback;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Interfaces.TabAbleFragment;
import com.weallone.raz.together.Packs.PostPack;
import com.weallone.raz.together.Enums.Location;
import com.weallone.raz.together.Painters.QueuePostPainter;
import com.weallone.raz.together.R;
import com.weallone.raz.together.Sensors.ShakeDetector;
import com.weallone.raz.together.Sensors.SwipeGestureDetector;
import com.weallone.raz.together.Painters.PostPainter;
import com.weallone.raz.together.Tools.BitmapHelper;
import com.weallone.raz.together.Tools.ImageBase64;
import com.weallone.raz.together.Tools.MyForebaseMessagingService;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * The Post fragment. 3rd in 3Tab container.
 */
public class Posts extends Fragment implements TabAbleFragment, AsyncResponse, Cookied, Callback, View.OnClickListener {

    private static final String TAG = "Posts.Fragment";
    private SharedPreferences settings;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private List<Post> postsList = new ArrayList<>();
    private LinearLayout postsLayout;
    private Boolean init = false;
    private View view;
    private Uri emojiUri;
    private PostPainter painter;
    private QueuePostPainter queuePostPainter;
    private ViewSwitcher switcher;
    private UserInfo user;
    private Switch switch_public;
    private ScrollView scrollView;
    private EditText title;
    private EditText message;
    private Button submit;
    private int maxPostsInUpdate = 10;
    private boolean swipeRefresh = false;
    private FloatingActionButton newPostBtn;
    private FloatingActionButton searchPostBtn;
    private FloatingActionButton queuePostBtn;
    private EditText searchBox;
    private LinearLayout actionBarContainer;
    private Button choosePostColorBtn;
    private Button colorPostChoosenBtn;
    private Button postAddImage;
    private Button postAddLink;
    private LinearLayout chooseColorContainer;
    private LinearLayout queueLayoutContainer;
    private LinearLayout queueLayout;
    private ColorPickerView colorPickerView;
    private Integer lastColorChoosen = null;
    private String lastChoosenImage = null;
    private String imageX = "";
    private String imageY = "";
    private String lastChoosenLink = null;
    private int PICK_IMAGE_REQUEST = 1;
    private final static Integer MAX_POSTS_RESULT_IN_SEARCH = 15;
    private MyActionBarActivity myActionBarActivity = null;
    private BroadcastReceiver reciever = new Reciever();
    private Boolean shown = false;

    /**
     * Default constructor
     */
    public Posts() {
        // Required empty public constructor
    }

    /**
     * Creation of Posts fragment method.
     * @param actionBarActivity - activity.
     * @return Post fragment
     */
    public static Posts newInstance(MyActionBarActivity actionBarActivity){
        Posts fragment = new Posts();
        Bundle bundle = new Bundle();
        bundle.putSerializable(actionBarActivity.getResources().getString(R.string.key_posts_pass_argument), (Serializable) actionBarActivity);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * onCreaateView  standard method.
     * Initializing: shared prefencesm user, cookkie, UI, sensors, and posts.
     * Init Posts.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view edited.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = getActivity();
        if(activity != null){
            setHasOptionsMenu(false);
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_posts, container, false);
            SetSharedPrefences();
            SetUser();
            setCookie(getResources().getString(R.string.setCookie));
            InitUI(view);
            InitSensors();
            SetPainter(inflater);
            clearChatLabels();
            GetPosts(user, "-1", maxPostsInUpdate);
            return view;
        }
        return null;
    }

    /**
     * Getting user fomr bundle if existed.
     */
    private void SetUser() {
        Gson gson = new Gson();
        String json = settings.getString(getResources().getString(R.string.key_user), "");
        if(json.equals("")){
            Log.d(TAG, "No User defined");
        } else {
            user = gson.fromJson(json, UserInfo.class);
            emojiUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + getActivity().getPackageName() + "/drawable/" + user.getIcon());
        }
    }

    /**
     * Initiation the post painter.
     * @param inflater
     */
    private void SetPainter(LayoutInflater inflater) {
        painter = new PostPainter(postsList, postsLayout,inflater,getActivity(),
                user,emojiUri,view,this);
        painter.setCommunication(this, this);
        queuePostPainter = new QueuePostPainter(queueLayout,inflater,getActivity(),user,this,this,
                queueLayoutContainer);
    }

    /**
     * Initializing UI,
     * by 3 main functions.
     * @param view
     */
    private void InitUI(View view) {
        myActionBarActivity = (MyActionBarActivity) getArguments().
                getSerializable(getResources().getString(R.string.key_posts_pass_argument));
        setDisplayPostsWindow(view);
        setFloatingButtons(view);
        setCreationPostWindow(view);
    }

    /**
     * Settings the display Post window,
     * Mainly initalizing UI and defining different listeners to support
     * each UI Component.
     * @param view to edit and attach.
     */
    private void setDisplayPostsWindow(View view) {
        actionBarContainer = (LinearLayout) view.findViewById(R.id.action_bars_container);
        scrollView = (ScrollView) view.findViewById(R.id.posts_scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); //for verticalScrollView
                if (scrollY == 0)
                    swipeRefresh = true;
                else
                    swipeRefresh = false;
            }
        });
        switcher = (ViewSwitcher) view.findViewById(R.id.posts_switcher);
        view.findViewById(R.id.new_post_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
                switch_public.setChecked(false);
                title.setText(getResources().getString(R.string.empty));
                message.setText(getResources().getString(R.string.empty));
                actionBarContainer.setVisibility(View.VISIBLE);
                searchPostBtn.setColorNormalResId(R.color.lightGreen);
                searchPostBtn.setIcon(R.drawable.mindeglasses);
                searchBox.setVisibility(View.INVISIBLE);
            }
        });
        switch_public = (Switch) view.findViewById(R.id.switch_public);
        switch_public.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    switch_public.setText(getResources().getString(R.string.public_text));
                else switch_public.setText(getResources().getString(R.string.private_text));
            }
        });
        postsLayout = (LinearLayout) view.findViewById(R.id.posts_layout);
    }

    /**
     * Set the creation post window(sweep left)
     * attach UI and define listneres to UI components.
     * @param view to edit on
     */
    public void setCreationPostWindow(View view){
        final View _view = view;
        choosePostColorBtn = (Button) view.findViewById(R.id.post_choose_color);
        chooseColorContainer = (LinearLayout) view.findViewById(R.id.choose_color_container);
        colorPostChoosenBtn = (Button) view.findViewById(R.id.post_color_post_choosen_btn);
        postAddImage = (Button) view.findViewById(R.id.post_add_image);
        postAddLink = (Button) view.findViewById(R.id.post_add_link);
        postAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetImage();
            }
        });
        postAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_view.findViewById(R.id.post_add_link_layout).getVisibility()==View.INVISIBLE){
                    _view.findViewById(R.id.post_add_link_layout).setVisibility(View.VISIBLE);
                } else {
                    _view.findViewById(R.id.post_add_link_layout).setVisibility(View.INVISIBLE);
                }
            }
        });
        colorPickerView = (ColorPickerView) view.findViewById(R.id.post_view__color_picker_view);
        colorPostChoosenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.setVisibility(View.VISIBLE);
                chooseColorContainer.setVisibility(View.INVISIBLE);
                lastColorChoosen = colorPickerView.getColor();
            }
        });
        choosePostColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.setVisibility(View.INVISIBLE);
                chooseColorContainer.setVisibility(View.VISIBLE);
            }
        });
        title = (EditText) view.findViewById(R.id.check_title_edittext);
        message = (EditText) view.findViewById(R.id.check_msg_edittext);;
        submit = (Button) view.findViewById(R.id.check_send_btn);;
        submit.setOnClickListener(this);
        setAddLink();
    }

    /**
     * Add link if avaiable.
     */
    private void setAddLink() {
        final View _view = view;
        final EditText linkAddEditText = (EditText) view.findViewById(R.id.post_add_link_edittext);
        Button linkAddBtn = (Button) view.findViewById(R.id.post_add_link_btn);
        linkAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastChoosenLink = linkAddEditText.getText().toString();
                painter.setLastLink(linkAddEditText.getText().toString());
                Toast.makeText(getActivity(), getResources().getString(R.string.link_uploaded),
                        Toast.LENGTH_SHORT).show();
                linkAddEditText.setText("");
                _view.findViewById(R.id.post_add_link_layout).setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Get image start activity.
     */
    public void GetImage() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /**
     * Callback from getting image from gallery.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&  data != null && data.getData() != null) {

            Uri uri = data.getData();

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Bitmap b = BitmapHelper.getScaledRectangleBitmap(bitmap, (int) (size.x*0.75), size.y/2);
                if(bitmap!=null) {
                    String background = ImageBase64.encodeTobase64(b);
                    lastChoosenImage = background;
                    imageX = String.valueOf(b.getWidth());
                    imageY = String.valueOf(b.getHeight());
                    painter.setLastImage(background);
                    painter.SetLastImageX(imageX);
                    painter.SetLastImageY(imageY);
                    Toast.makeText(getActivity(), getResources().getString(R.string.image_uploaded),
                            Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Setting the different float buttons:
     * 1) Creation of post.
     * 2) Search post.
     * 3) Get post queue.
     * @param view to edit on.
     */
    public void setFloatingButtons(View view) {
        final View _view = view;
        newPostBtn = (FloatingActionButton) view.findViewById(R.id.new_post_icon);
        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
                lastColorChoosen = null;
                lastChoosenImage = "";
                imageX = "";
                imageY = "";
                lastChoosenLink = "";
                _view.findViewById(R.id.post_add_link_layout).setVisibility(View.INVISIBLE);
                actionBarContainer.setVisibility(View.INVISIBLE);
                searchBox.setVisibility(View.INVISIBLE);
            }
        });
        searchPostBtn = (FloatingActionButton) view.findViewById(R.id.search_post_icon);
        searchBox = (EditText) view.findViewById(R.id.post_search_box);
        searchBox.setVisibility(View.INVISIBLE);
        searchPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBox.getVisibility() == View.INVISIBLE) {
                    actionBarContainer.setGravity(Gravity.CENTER | Gravity.TOP);
                    searchPostBtn.setColorNormalResId(R.color.black_semi_transparent);
                    searchPostBtn.setIcon(R.drawable.mindeglasses1);
                    searchBox.setVisibility(View.VISIBLE);
                } else {
                    searchBox.setVisibility(View.INVISIBLE);
                    searchPostBtn.setColorNormalResId(R.color.lightGreen);
                    searchPostBtn.setIcon(R.drawable.mindeglasses);
                    if (!searchBox.getText().toString().equals(getResources().getString(R.string.empty))) {
                        sendSearchRequest();
                    }
                }
            }
        });
        queuePostBtn = (FloatingActionButton) view.findViewById(R.id.queue_post_icon);
        queueLayoutContainer = (LinearLayout) view.findViewById(R.id.queue_layout_container);
        queueLayout = (LinearLayout) view.findViewById(R.id.queue_layout);
        queuePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queueLayoutContainer.getVisibility() == View.INVISIBLE) {
                    queueLayoutContainer.setVisibility(View.VISIBLE);
                    sendQueueRequest();
                } else {
                    queueLayoutContainer.setVisibility(View.INVISIBLE);
                }
            }
        });
        if(user.getAccount()==Account.CHILD)
            queuePostBtn.setVisibility(View.INVISIBLE);
    }

    /**
     * Ask the server for post queue.
     */
    private void sendQueueRequest() {
        String key = getResources().getString(R.string.key_post_queue);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String searchPostsUrl = getResources().getString(R.string.request_post_queue_url);
        getEntity.execute(new String[]{searchPostsUrl});
    }

    /**
     * Send search request - to fet all post that their content match the content entered in
     * search textbox.
     */
    private void sendSearchRequest() {
        String key = getResources().getString(R.string.key_post_search_result);
        HashMap<String, String> hash = new HashMap<>();
        hash.put(getResources().getString(R.string.post_search_user_id),user.getId());
        hash.put(getResources().getString(R.string.post_search_string), searchBox.getText().toString());
        hash.put(getResources().getString(R.string.post_search_how_many), String.valueOf(MAX_POSTS_RESULT_IN_SEARCH));
        String searchUrl = getResources().getString(R.string.request_search_posts_url);
        new SendGeneric(this,key, this,
                getResources().getString(R.string.getcookie), getResources().getString(R.string.setCookie),
                searchUrl, hash, getActivity()).execute();
    }

    /**
     * Init gesture, shake sensors - for loading new and old posts.
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
        scrollView.setOnTouchListener(gl);
    }

    /**
     * Handle shakek event - load new post.
     * @param count - not used.
     */
    private void handleShakeEvent(int count) {
        Activity activity = getActivity();
        if(activity != null){
            Toast.makeText(getActivity(), getResources().getString(R.string.loading_new_messages),
                    Toast.LENGTH_SHORT).show();
            GetPosts(user, "-1", maxPostsInUpdate);
        }

    }

    /**
     * Define shared prefences
     */
    private void SetSharedPrefences() {
        settings = getActivity().getSharedPreferences(getResources().getString(R.string.data),
                getContext().MODE_PRIVATE);
        settings = getActivity().getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
    }

    /**
     * get posts by user information, time, and how many posts.
     * @param user - for knowing public/private.
     * @param before - from which time to get the posts
     * @param amount - how may.
     */
    private void GetPosts(UserInfo user, String before, int amount){
        String key = (before.equals("-1"))?
                getResources().getString(R.string.getfront)
                :getResources().getString(R.string.getback);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String getPostsUrl = getResources().getString(R.string.request_get_posts_url)
                + getResources().getString(R.string.slash) + user.getId()
                + getResources().getString(R.string.slash) + before
                + getResources().getString(R.string.slash) + String.valueOf(amount);
        getEntity.execute(new String[]{getPostsUrl});
    }

    /**
     * Callback function for the output from server.
     * @param result
     */
    @Override
    public void OnFinished(String result) {
        if (!init) painter.paint(postsList);
        if(getActivity()==null) return;
        Log.d("RECIEVED:", result);
        JSONObject messages = null;
        try {
            ///Get ten methods at start.
            if ( result.startsWith(getResources().getString(R.string.getfront))) {
                Log.d("READ", getResources().getString(R.string.getfront));
                result = result.substring(8);
                view.findViewById(R.id.post_loading_textview).setVisibility(View.INVISIBLE);
                if (result.equals(getResources().getString(R.string.empty))) return;
                ReadPosts(result, Location.FRONT);
                painter.paint(postsList);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

                ///Swipe down to get old messages.
            } else if (result.startsWith(getResources().getString(R.string.getback))) {
                Log.d("READ", getResources().getString(R.string.getback));
                result = result.substring(7);
                if (result.equals(getResources().getString(R.string.getback))) {
                    Toast.makeText(getActivity(), (String) getResources().getString(R.string.no_more_comments),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                ReadPosts(result, Location.BACK);
                painter.paint(postsList);
            ///When this user sends a message.
            } else if (result.startsWith(getResources().getString(R.string.save))) {
                Log.d("READ", getResources().getString(R.string.save));
                result = result.substring(5);
                messages = new JSONObject(result);
                addORreplace(Location.FRONT, DecipherPost(messages));
                painter.paint(postsList);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            //Update of post/comment
            } else if (result.startsWith(getResources().getString(R.string.update))) {
                Log.d("READ", getResources().getString(R.string.update));
                result = result.substring(6);
                if (result.equals(getResources().getString(R.string.empty))) return;
                messages = new JSONObject(result);
                for (int i = messages.length() / 4 - 1; i >= 0; --i) {
                    addORreplace(Location.FRONT, DecipherPost(messages));
                }
                painter.paint(postsList);
            //Adding a comment to post.
            } else if (result.startsWith(getResources().getString(R.string.add_comment))) {
                Log.d("READ", getResources().getString(R.string.add_comment));
                result = result.substring(15);
                if (result.equals(getResources().getString(R.string.empty))) return;
                messages = new JSONObject(result);
                addComment(messages);
                painter.paint(postsList);
            //Editing a comment to post.
            } else if (result.startsWith(getResources().getString(R.string.key_edit_comment))) {
                Log.d("READ", getResources().getString(R.string.key_edit_comment));
                //nothinng to do.
            //Deleting a comment.
            } else if(result.startsWith(getResources().getString(R.string.key_del_comment))){
                Log.d("READ", getResources().getString(R.string.key_del_comment));
                result = result.substring(15);
                RemoveComment(result);
                painter.paint(postsList);
            //Editing a post.
            } else if (result.startsWith(getResources().getString(R.string.key_edit_post))) {
                Log.d("READ", getResources().getString(R.string.key_edit_post));
                result = result.substring(13);
                if(result.equals(getResources().getString(R.string.empty))) return;
                setEditedPost(result);
                painter.paint(postsList);
            //Deleting a post.
            } else if(result.startsWith(getResources().getString(R.string.key_del_post))){
                Log.d("READ", getResources().getString(R.string.key_del_post));
                result = result.substring(12);
                RemovePost(result);
                painter.paint(postsList);
            //Add comments(of peers)
            } else if (result.startsWith(getResources().getString(R.string.comments_arrived_by_user))){
                Log.d("READ", getResources().getString(R.string.comments_arrived_by_user));
                result = result.substring(24);
                Log.d("RESULT", result);
                if (result.equals(getResources().getString(R.string.empty)) ||
                        result.equals(getResources().getString(R.string.empty_array))){
                    Toast.makeText(getActivity(), (String) getResources().getString(R.string.no_more_comments),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                painter.paint(postsList, updatePostComments(result));
            //Add comments.
            } else if (result.startsWith(getResources().getString(R.string.comments_arrived))){
                Log.d("READ", getResources().getString(R.string.comments_arrived));
                result = result.substring(16);
                if (result.equals(getResources().getString(R.string.empty_array))) return;
                painter.paint(postsList,updatePostComments(result));
            //Got post queue
            } else if(result.startsWith(getResources().getString(R.string.key_post_queue))){
                Log.d("READ", getResources().getString(R.string.key_post_queue));
                result = result.substring(14);
                if (result.equals(getResources().getString(R.string.empty))) return;
                queuePostPainter.paint(result);
            //Got post by search.
            } else if (result.startsWith(getResources().getString(R.string.key_post_search_result))){
                Log.d("READ", getResources().getString(R.string.key_post_search_result));
                result = result.substring(22);
                if (result.equals(getResources().getString(R.string.empty))) return;
                postsList.clear();
                ReadPosts(result, Location.FRONT);
                for(Post p: postsList) p.setSearched(searchBox.getText().toString());
                painter.paint(postsList);
                searchBox.setText(getResources().getString(R.string.empty));
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
            Log.d("READ","None");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updating comments of post
     * @param result - from string
     * @return the post changed or null if none.
     * @throws JSONException
     */
    private Integer updatePostComments(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        List<Comment> newComments = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            newComments.add(DecipherComment(obj));
        }
        for(Post p: postsList){
            if(p.getId().equals(newComments.get(0).getPostID())){
                if(p.getComments().size()==newComments.size()){
                    Toast.makeText(getActivity(), (String) getResources().getString(R.string.no_more_comments),
                            Toast.LENGTH_SHORT).show();
                }
                p.setComments(newComments);
                return postsList.indexOf(p);
            }
        }
        return null;
    }

    /**
     * Switching between edited post and old one.
     * @param result - new post - string from server.
     * @throws JSONException
     */
    public void setEditedPost(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        EditedPost editedPost = DecipherEditedPost(obj);
        for(int i = 0; i < postsList.size(); i++){
            if(postsList.get(i).getId().equals(editedPost.getId())){
                postsList.get(i).setTitle(editedPost.getTitle());
                postsList.get(i).setMessage(editedPost.getMessage());
            }
        }
    }

    /**
     * Read Posts from server
     * @param result - string containging one or more posts.
     * @param location - front or back(old or new)
     * @throws JSONException
     */
    private void ReadPosts(String result, Location location) throws JSONException {
        if(result.charAt(0)=='{') {
            JSONObject obj = new JSONObject(result);
            addORreplace(location, DecipherPost(obj));
        }
        else{
            JSONArray array = new JSONArray(result);
            if(array.length()==0){
                Toast.makeText(getActivity(), (String) getResources().getString(R.string.no_more_posts),
                        Toast.LENGTH_SHORT).show();
            }
            if(location==Location.BACK){
                for (int i = array.length()-1; i >=0; i--) {
                    JSONObject obj = array.getJSONObject(i);
                    addORreplace(location, DecipherPost(obj));
                }
            }else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    addORreplace(location, DecipherPost(obj));
                }
            }
        }
    }

    /**
     * Remove a specific comment
     * @param result - string containing which comment to remove.
     */
    private void RemoveComment(String result) {
        try {
            JSONObject message = new JSONObject(result);
            String postID = message.get(getResources().getString(R.string.post_text)).toString();
            String commentID = message.get(getResources().getString(R.string.comment_text)).toString();
            for (Post p: postsList){
                if(!p.getId().equals(postID)) continue;
                List<Comment> comments = new ArrayList<>();
                for(Comment comment: p.getComments()){
                    if(!comment.getId().equals(commentID)) {
                        comments.add(comment);
                    }
                }
                p.setComments(comments);
            }
        } catch(Exception e){}
    }

    /**
     * Remove post
     * @param result - containing which post to remove.
     */
    private void RemovePost(String result) {
        List<Post> posts = new ArrayList<>();
        for(Post p: postsList){
            if(!p.getId().equals(result)){
                posts.add(p);
            }
        }
        postsList = posts;
    }

    /**
     * Add comment to post.
     * @param result - JSON object from server.
     */
    private void addComment(JSONObject result) {
        try {
            Comment comment = DecipherComment(result);
            for(Post p: this.postsList){
                if(p.getId().equals(comment.getPostID())){
                    p.getComments().add(comment);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * add or replace a specific post.
     * @param location (front oro back) - old or new
     * @param p - post.
     */
    private void addORreplace(Location location,Post p){
        List<Post> removes = new ArrayList<>();
        for(Post other: this.postsList){
            if(other.getId().equals(p.getId())){
                removes.add(other);
            }
        }
        for(Post rm: removes){
            this.postsList.remove(rm);
        }
        if(location == Location.BACK){
            this.postsList.add(0,p);
        } else{
            this.postsList.add(p);
        }
    }

    /**
     * Creating a post object from JSON post object.
     * @param messages containging the post JSON object from server
     * @return post object.
     * @throws JSONException
     */
    private Post DecipherPost(JSONObject messages) throws JSONException {
        return new Post(messages.get(getResources().getString(R.string.key_post_title)).toString(),
                messages.get(getResources().getString(R.string.key_post_message)).toString(),
                messages.get(getResources().getString(R.string.key_post_publisher)).toString(),
                messages.get(getResources().getString(R.string.key_post_publisherID)).toString(),
                messages.get(getResources().getString(R.string.key_post_date)).toString(),
                messages.get(getResources().getString(R.string.key_post_id)).toString(),
                messages.get(getResources().getString(R.string.is_public)).toString(),
                Integer.parseInt(messages.get(getResources().getString(R.string.key_color)).toString()),
                Boolean.valueOf(messages.get(getResources().getString(R.string.key_answered)).toString()),
                messages.get(getResources().getString(R.string.key_post_image)).toString(),
                messages.get(getResources().getString(R.string.key_post_link)).toString(),
                messages.get(getResources().getString(R.string.key_image_dim_x)).toString(),
                messages.get(getResources().getString(R.string.key_image_dim_y)).toString(),
                messages.get(getResources().getString(R.string.key_post_publisher_entity)).toString(),
                messages.get(getResources().getString(R.string.key_post_publisher_icon)).toString(),
                messages.getJSONArray(getResources().getString(R.string.key_comments_array)).length());
    }

    /**
     * Creating edited post object from messages (JSON object)
     * @param messages - JSON object.
     * @return Edited Post.
     * @throws JSONException
     */
    private EditedPost DecipherEditedPost(JSONObject messages) throws JSONException {
        return new EditedPost(messages.get(getResources().getString(R.string.key_post_date)).toString(),
                messages.get(getResources().getString(R.string.key_post_publisherID)).toString(),
                messages.get(getResources().getString(R.string.key_post_title)).toString(),
                messages.get(getResources().getString(R.string.key_post_message)).toString(),
                messages.get(getResources().getString(R.string.key_post_id)).toString());
    }

    /**
     * Comment object creation from JSON Object.
     * @param messages - string from server.
     * @return Comment object.
     * @throws JSONException
     */
    private Comment DecipherComment(JSONObject messages) throws JSONException {
        return new Comment(messages.get(getResources().getString(R.string.key_comment_message)).toString(),
                messages.get(getResources().getString(R.string.key_comment_publisher)).toString(),
                messages.get(getResources().getString(R.string.key_comment_publisher_id)).toString(),
                messages.get(getResources().getString(R.string.key_comment_date)).toString(),
                messages.get(getResources().getString(R.string.key_comment_post_id)).toString(),
                messages.get(getResources().getString(R.string.key_comment_id)).toString(),
                messages.get(getResources().getString(R.string.key_comment_image)).toString(),
                messages.get(getResources().getString(R.string.key_comment_link)).toString(),
                messages.get(getResources().getString(R.string.key_image_dim_x)).toString(),
                messages.get(getResources().getString(R.string.key_image_dim_y)).toString(),
                messages.get(getResources().getString(R.string.key_comment_publisher_entity)).toString());
    }

    /**
     * Callback function from gesture sensor - getting more posts old from swipe.
     */
    @Override
    public void Callback() {
        if(swipeRefresh) {
            Toast.makeText(getActivity(), (String) getResources().getString(R.string.loading_posts),
                    Toast.LENGTH_SHORT).show();
            if (postsList.isEmpty())
                GetPosts(user, "-1", maxPostsInUpdate);
            else
                GetPosts(user, postsList.get(0).getDate().toString(), maxPostsInUpdate);
        }
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
     * Submiting new post to server.
     * Creating keys (to match JSON object in server),
     * executing async task and preparing environment to new post submiition.
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(validateInput()) return;
        String[] keys = {
                getResources().getString(R.string.key_post_title),
                getResources().getString(R.string.key_post_message),
                getResources().getString(R.string.key_post_publisher),
                getResources().getString(R.string.key_post_publisherID),
                getResources().getString(R.string.key_post_date),
                getResources().getString(R.string.is_public),
                getResources().getString(R.string.key_color),
                getResources().getString(R.string.key_answered).toString(),
                getResources().getString(R.string.key_post_image).toString(),
                getResources().getString(R.string.key_post_link).toString(),
                getResources().getString(R.string.key_image_dim_x).toString(),
                getResources().getString(R.string.key_image_dim_y).toString(),
                getResources().getString(R.string.key_post_publisher_entity).toString(),
                getResources().getString(R.string.key_post_publisher_icon).toString()

        };
        //Get last message id.
        SendPost sendMessage = new SendPost(this, getResources().getString(R.string.key_save_post), this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie), getActivity());
        String titleStr = title.getText().toString();
        String messageStr = message.getText().toString();
        String publisherStr = user.getName();
        String publisherIDStr = user.getId();
        String sendMessageUrl = getResources().getString(R.string.request_post_posts_url);
        String isPublic = (switch_public.getText().toString().equals(getResources().getString(R.string.public_text)))?
                getResources().getString(R.string.true_text):getResources().getString(R.string.false_text);
        Integer color = (lastColorChoosen==null)?0:lastColorChoosen;
        String image = lastChoosenImage;
        String link = lastChoosenLink;
        //Excute save to server:
        sendMessage.execute(new PostPack[]{new PostPack(sendMessageUrl, new Post(
                titleStr, messageStr, publisherStr, publisherIDStr,
                String.valueOf(Calendar.getInstance().getTimeInMillis()), isPublic, color, false, image,
                link, imageX, imageY, user.getAccount().toString(), user.getIcon(),0), keys, false)});
        //preparing environment for new Post:
        switch_public.setChecked(false);
        title.setText(getResources().getString(R.string.empty));
        lastChoosenImage = "";
        lastChoosenLink = "";
        painter.setLastImage(lastChoosenImage);
        painter.setLastLink(lastChoosenLink);
        message.setText(getResources().getString(R.string.empty));
        actionBarContainer.setVisibility(View.VISIBLE);
        searchPostBtn.setColorNormalResId(R.color.lightGreen);
        searchPostBtn.setIcon(R.drawable.mindeglasses);
        searchBox.setVisibility(View.INVISIBLE);
        switcher.showNext();
    }

    private boolean validateInput() {
        if(title.getText().toString().equals(getResources().getString(R.string.empty))){
            Toast.makeText(getActivity(),getResources().getString(R.string.please_insert_title),Toast.LENGTH_SHORT).show();
            return true;
        } else if(message.getText().toString().equals(getResources().getString(R.string.empty))){
            Toast.makeText(getActivity(),getResources().getString(R.string.please_insert_message),Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * unregisting reciever.
     */
    @Override
    public void onPause() {
        getActivity().unregisterReceiver(reciever);
        // Add the following line to unregister the Sensor Manager onPause
        super.onPause();
    }

    /**
     * Registing reciever to firebase.
     */
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyForebaseMessagingService.BROADCAST_ACTION);
        getActivity().registerReceiver(reciever, filter);
        // Add the following line to register the Session Manager Listener onResume
        Log.d(TAG, "onResume()");
    }

    /**
     * Helper post object foor editing post. creating edited post, sending it to server
     * to populate only the changed members. (title and message).
     */
    private class EditedPost {
        private String date;
        private String publisher_ID;
        private String title;
        private String message;
        private String id;

        /**
         * Constructor - init members.
         * @param date - when edited
         * @param publisher_ID - author id
         * @param title - changed title
         * @param message - changed message
         * @param id - user id.
         */
        public EditedPost(String date, String publisher_ID, String title, String message, String id) {
            this.date = date;
            this.publisher_ID = publisher_ID;
            this.title = title;
            this.message = message;
            this.id = id;
        }

        /**
         * get method
         * @return
         */
        public String getDate() {
            return date;
        }

        /**
         * set method
         * @param date
         */
        public void setDate(String date) {
            this.date = date;
        }

        /**
         * get method
         * @return
         */
        public String getPublisher_ID() {
            return publisher_ID;
        }

        /**
         * set method
         * @param publisher_ID
         */
        public void setPublisher_ID(String publisher_ID) {
            this.publisher_ID = publisher_ID;
        }

        /**
         * get method
         * @return
         */
        public String getTitle() {
            return title;
        }

        /**
         * set method
         * @param title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * get method
         * @return
         */
        public String getMessage() {
            return message;
        }

        /**
         * set method
         * @param message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * get method
         * @return
         */
        public String getId() {
            return id;
        }

        /**
         * set method
         * @param id
         */
        public void setId(String id) {
            this.id = id;
        }
    }

    /**
     * When this fragment is shown, registing sensors and reciver, deleting the peer status from action
     * bar if created by chat.
     * and getting posts from server
     * @param first - if first appears.
     */
    @Override
    public void Shown(Boolean first) {
        if(!first) {
            if(mSensorManager==null || mShakeDetector==null || mAccelerometer==null) return;
            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            myActionBarActivity.setPeerStatus(getResources().getString(R.string.empty));
            myActionBarActivity.setPeerName(getResources().getString(R.string.empty));
            GetPosts(user,"-1",maxPostsInUpdate);
        }
        shown = true;
    }

    /**
     * Cleant the peer status and name from action bar(chat).
     */
    private void clearChatLabels(){
        myActionBarActivity.setPeerStatus(getResources().getString(R.string.empty));
        myActionBarActivity.setPeerName(getResources().getString(R.string.empty));
    }

    /**
     * Hidden - unregisten shake detector
     */
    @Override
    public void Hidden() {
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
     * Class responsibile for listeneing for broadcast messages from Firebase service.
     */
    class Reciever extends BroadcastReceiver {
        /**
         * When serviece broadcast, this method get the updates from server.
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(getResources().getString(R.string.fragment))
                    .equals(getResources().getString(R.string.app_fragment)) && isShown()) {
                //Case of help request messages - may want to change to chat to answer call.
                if (intent.hasExtra(getResources().getString(R.string.key_help_child_id))
                        && intent.hasExtra(getResources().getString(R.string.key_help_request_num))
                        && intent.hasExtra(getResources().getString(R.string.key_help_child_name))) {
                    if (user.getAccount() == Account.PSYCHOLOGIST || user.getAccount() == Account.MANAGER) {
                        DisplayHelpOption(intent.getStringExtra(getResources().getString(R.string.key_help_request_num)),
                                intent.getStringExtra(getResources().getString(R.string.key_help_child_id)),
                                intent.getStringExtra(getResources().getString(R.string.key_help_child_name)));
                    }
                }
            }
        }
    }

    /**
     * Prompt message to user - if he wants to answer call or not.
     * @param num - of help request
     * @param newConversation - needed if he chose yes
     * @param child_name - child prompt the help request message
     */
    private void DisplayHelpOption(final String num, final String newConversation, final String child_name) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(getContext(), "YES", Toast.LENGTH_SHORT);
                        myActionBarActivity.setChatImmediateCall(num, newConversation,child_name);
                        sendAnsweredImmediateHelp(true,user.getId());
                        //notify server for statistics - num
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        sendAnsweredImmediateHelp(false, user.getId());
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(child_name + " " + getResources().getString(R.string.asks_for_immediate_help_can_you_help))
                .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.no),dialogClickListener).show();


    };

    /**
     * send answer/not answer to server - for statistics
     * @param b - helped or not
     * @param id - of user.
     */
    private void sendAnsweredImmediateHelp(boolean b, String id) {
        String key = getResources().getString(R.string.key_answered_help);
        GetEntity getEntity = new GetEntity(this,key,this,
                getResources().getString(R.string.getcookie),
                getResources().getString(R.string.setCookie));
        String getPeerStatus = getResources().getString(R.string.request_answered_help_url)
                + id + getResources().getString(R.string.slash) + String.valueOf(b);
        getEntity.execute(new String[]{getPeerStatus});
    }
}
