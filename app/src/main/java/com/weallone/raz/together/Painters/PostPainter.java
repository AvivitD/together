package com.weallone.raz.together.Painters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.weallone.raz.together.AsyncTasks.DeletePost;
import com.weallone.raz.together.AsyncTasks.EditPost;
import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.AsyncTasks.SendComment;
import com.weallone.raz.together.Entities.Comment;
import com.weallone.raz.together.Entities.Post;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Enums.Account;
import com.weallone.raz.together.Fragments.Posts;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Packs.CommentPack;
import com.weallone.raz.together.Packs.PostPack;
import com.weallone.raz.together.R;
import com.weallone.raz.together.Tools.BitmapHelper;
import com.weallone.raz.together.Tools.ImageBase64;
import com.weallone.raz.together.Tools.MyColorUtils;
import com.weallone.raz.together.Tools.Time;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Post Painter - adding the post to Posts layout.
 */
public class PostPainter {
    private static final String TAG = "PostPainter";
    private List<Post> posts;
    private LinearLayout layout;
    private LayoutInflater inflater;
    private Activity activity;
    private Uri emojiUri;
    private UserInfo user;
    private AsyncResponse response;
    private Cookied cookied;
    private CommentsPainter commentsPainter;
    private transient  String image;
    private final static Integer NUM_OF_COMMENTS_IN_UPDATE = 5;
    private String lastImage = "";
    private String imageX = "";
    private String imageY = "";
    private String lastLink = "";
    private static Integer SEARCH_COLOR;
    private static Integer PSYCHOLOGIST_COLOR;
    private static Integer DEVELOPER_COLOR;
    private Posts _posts;
    private View mainLayout;

    /**
     * Constructor - init members.
     * @param posts posts list
     * @param layout container
     * @param inflater
     * @param activity
     * @param user - this user
     * @param emojiUri - link to emoji
     * @param mainLayout container of layout
     * @param _posts - fragment
     */
    public PostPainter(List<Post> posts, LinearLayout layout, LayoutInflater inflater,
                       Activity activity, UserInfo user, Uri emojiUri, View mainLayout, Posts _posts) {
        this.posts = posts;
        this.layout = layout;
        this.inflater = inflater;
        this.activity = activity;
        this.user = user;
        this.emojiUri = emojiUri;
        this.mainLayout = mainLayout;
        this._posts = _posts;
        SEARCH_COLOR = activity.getResources().getColor(R.color.search_color_post);
        PSYCHOLOGIST_COLOR = activity.getResources().getColor(R.color.psychologist_name);
        DEVELOPER_COLOR = activity.getResources().getColor(R.color.developer_name);
    }

    /**
     * Setting the response sand cookied object.
     * @param response - response object
     * @param cookied - cookied object.
     */
    public void setCommunication(AsyncResponse response, Cookied cookied){
        this.response = response;
        this.cookied = cookied;
        this.commentsPainter = new CommentsPainter(activity,response,cookied,inflater,user);
    }

    /**
     * Paint function, creating a view for each post object and
     * adding it to the layout container.
     * @param posts - posts list.
     */
    public void paint(List<Post> posts) {
        this.posts = posts;
        layout.removeAllViews();
        for (Post p: posts){
            View child = inflater.inflate(R.layout.post_layout,null);
            if(child ==null) continue;
            setPostDetails(child, p, p.getSearched());
            setCommentsSendButton(child, p);
            setAdditionPanel(child, p);
            commentsPainter.paint(child,p.getComments(),
                    (p.getColor()==0)?Color.BLACK:MyColorUtils.getComplimentColor(p.getColor()));

            layout.addView(child);
        }
    }

    /**
     * Painting post by index.
     * @param posts - list of posts
     * @param index - index to paint.
     */
    public void paint(List<Post> posts, Integer index) {
        this.posts = posts;
        layout.removeViewAt(index);

        View updatedPost = inflater.inflate(R.layout.post_layout,null);
        if(updatedPost == null) return;
        setPostDetails(updatedPost, posts.get(index),posts.get(index).getSearched());
        setCommentsSendButton(updatedPost, posts.get(index));
        setAdditionPanel(updatedPost, posts.get(index));
        commentsPainter.paint(updatedPost, posts.get(index).getComments(),
                (posts.get(index).getColor() == 0) ? Color.BLACK : MyColorUtils.getComplimentColor(posts.get(index).getColor()));

        layout.addView(updatedPost, index);
    }

    /**
     * Painting additonal panel to addd image andlink
     * @param updatedPost - updated Post
     * @param post - post object.
     */
    private void setAdditionPanel(View updatedPost, Post post) {
        final View _main = mainLayout;
        ImageView addFile = (ImageButton) updatedPost.findViewById(R.id.comment_add_file);
        Button addImage = (Button) updatedPost.findViewById(R.id.comment_add_image_btn);
        Button addLink = (Button) updatedPost.findViewById(R.id.comment_add_link_btn);
        final LinearLayout addLayout = (LinearLayout) updatedPost.findViewById(R.id.post_addition_layout);
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addLayout.getVisibility() == View.GONE) {
                    addLayout.setVisibility(View.VISIBLE);
                    lastImage = "";
                    imageX = "";
                    imageY = "";
                    lastLink = "";
                } else {
                    addLayout.setVisibility(View.GONE);
                }
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _posts.GetImage();
                addLayout.setVisibility(View.GONE);
            }
        });

        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_main.findViewById(R.id.post_add_link_layout).getVisibility() == View.INVISIBLE) {
                    _main.findViewById(R.id.post_add_link_layout).setVisibility(View.VISIBLE);
                } else {
                    _main.findViewById(R.id.post_add_link_layout).setVisibility(View.INVISIBLE);
                }
                addLayout.setVisibility(View.GONE);
            }
        });

    }

    /**
     * Sending comment POST request to server
     * @param child - layout to attach
     * @param post - post object.
     */
    private void setCommentsSendButton(View child,Post post) {
        final String id = post.getId();
        final EditText commentNew = (EditText) child.findViewById(R.id.post_new_comment_edittext);
        final Button commentBtn = (Button) child.findViewById(R.id.post_new_comment_submit_btn);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cookied == null || response == null) return;
                if(commentNew.getText().toString().equals(activity.getResources().getString(R.string.empty))){
                    Toast.makeText(activity,activity.getResources().getString(R.string.please_insert_message),Toast.LENGTH_SHORT).show();
                    return;
                }
                String sendCommentUrl = activity.getResources().getString(R.string.request_send_comment_url);
                String[] keys = {
                        activity.getResources().getString(R.string.key_comment_message),
                        activity.getResources().getString(R.string.key_comment_publisher),
                        activity.getResources().getString(R.string.key_comment_publisher_id),
                        activity.getResources().getString(R.string.key_comment_date),
                        activity.getResources().getString(R.string.key_comment_post_id),
                        activity.getResources().getString(R.string.key_comment_image).toString(),
                        activity.getResources().getString(R.string.key_comment_link).toString(),
                        activity.getResources().getString(R.string.key_image_dim_x).toString(),
                        activity.getResources().getString(R.string.key_image_dim_y).toString(),
                        activity.getResources().getString(R.string.key_comment_publisher_entity).toString(),

                };

                SendComment req = new SendComment(response,
                        activity.getResources().getString(R.string.add_comment), cookied,
                        activity.getResources().getString(R.string.getcookie),
                        activity.getResources().getString(R.string.setCookie), activity);
                req.execute(new CommentPack[]{new CommentPack(sendCommentUrl, new Comment(
                        commentNew.getText().toString(), user.getName(), user.getId(),
                        String.valueOf(Calendar.getInstance().getTimeInMillis()), id,
                        lastImage, lastLink,imageX,imageY,user.getAccount().toString()), keys, false)});
                commentNew.setText(activity.getResources().getString(R.string.empty));
            }
        });
    }

    /**
     * Setting post details
     * Attaching UI and defining listeners
     * @param child - layout
     * @param post - post object
     * @param searchString - search string for the search post option.
     */
    private void setPostDetails(View child, Post post, String searchString) {
        //Attaching UI.
        TextView post_username = (TextView) child.findViewById(R.id.post_username);
        ImageView post_username_emoji = (ImageView) child.findViewById(R.id.post_username_emoji);
        TextView post_title = (TextView) child.findViewById(R.id.post_title);
        TextView post_date = (TextView) child.findViewById(R.id.post_date);
        TextView post_message = (TextView) child.findViewById(R.id.post_message);
        TextView is_public = (TextView) child.findViewById(R.id.is_public_label);
        Integer textViewsColor = (post.getColor()==0)?Color.BLACK:MyColorUtils.getComplimentColor(post.getColor());

        if(post.getPublisherEntity().equals(Account.PSYCHOLOGIST.toString())
                || post.getPublisherEntity().equals(Account.MANAGER.toString())) {
            post_username.setTextColor(PSYCHOLOGIST_COLOR);
            post_username.setTypeface(null, Typeface.BOLD);
            post_username.setTextSize(activity.getResources().getDimension(R.dimen.small_meduim_text));
        }
        else if(post.getPublisherEntity().equals(Account.DEVELOPER.toString())) {
            post_username.setTextColor(DEVELOPER_COLOR);
            post_username.setTypeface(null, Typeface.BOLD);
            post_username.setTextSize(activity.getResources().getDimension(R.dimen.small_meduim_text));
        }
        Uri emojiUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + activity.getPackageName() + "/drawable/" + post.getPublisherIcon());
        if(post_username!=null) {
            post_username.setTextColor(textViewsColor);
            post_username.setText(post.getPublisher());
        }
        if(emojiUri!=null && post_username_emoji!=null) setScaledBitmap(post_username_emoji, emojiUri);
        if(post_title!=null){
            post_title.setTextColor(textViewsColor);
            post_title.setText(paintString(post.getTitle(), searchString));
        }
        if(post_message!=null) {
            post_message.setTextColor(textViewsColor);
            post_message.setText(paintString(post.getMessage(), searchString));
        }
        if(post.getDate()!=null && post_date!=null){
            post_date.setTextColor(textViewsColor);
            Time.setTime(activity, post_date, Calendar.getInstance().getTimeInMillis(),
                    post.getDate(), activity.getResources().getString(R.string.date_ago), "");
        }
        if(post.getIsPublic()!= null && is_public!= null){
            is_public.setTextColor(textViewsColor);
            is_public.setText((post.getIsPublic()==true)
                    ?activity.getResources().getString(R.string.public_text)
                    :activity.getResources().getString(R.string.private_text));
        }

        setImageAndLink(child,post);
        setAnsweredTimeTextView(child,post, textViewsColor);
        setBackgroundColor(child,post.getColor());
        setMoreComments(child, post,textViewsColor);
        if(user.getAccount()!=Account.CHILD || user.getId().equals(post.getPublisherID())) {
            setEditSettings(child, post, post_title, post_message);
            setDelSettings(child, post);
        } else {
            disableEditDelete(child);
        }
    }

    /**
     * Setting image and link.
     * including their listeners.
     * @param child - layout
     * @param post - post object
     */
    private void setImageAndLink(View child, Post post) {
        final View _view = child;
        TextView link = (TextView) child.findViewById(R.id.post_link);
        ImageView image = (ImageView) child.findViewById(R.id.post_image);
        if(link !=null && !post.getLink().equals("")) link.setText(activity.getResources().getString(R.string.link_text));
        if(image !=null && !post.getImage().equals("")) {
            image.getLayoutParams().width = Integer.valueOf(post.getImageX());
            image.getLayoutParams().height = Integer.valueOf(post.getImageY());
            image.requestLayout();
            Bitmap bitmap = ImageBase64.decodeBase64(post.getImage(),activity);
            Drawable dr = new BitmapDrawable(bitmap);
            image.setBackground(dr);
        }
        if(post.getLink().equals("")) _view.findViewById(R.id.link_layout_in_post).setVisibility(View.INVISIBLE);
        final String url = post.getLink();
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url != "" && url.startsWith("http")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    activity.startActivity(browserIntent);
                } else if (url != "") {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url));
                    activity.startActivity(browserIntent);
                }
            }
        });
    }

    /**
     * Disabling the edit and delete function (used where this is a child in this is not his post).
     * @param child  - layout.
     */
    private void disableEditDelete(View child) {
        final TextView editLabel = (TextView) child.findViewById(R.id.post_edit_text);
        final TextView delLabel = (TextView) child.findViewById(R.id.post_del_text);
        editLabel.setVisibility(View.INVISIBLE);
        delLabel.setVisibility(View.INVISIBLE);
    }

    /**
     * Setting the answer/not answered text of post.
     * @param child - layout
     * @param post post object.
     */
    private void setAnsweredTimeTextView(View child, Post post, Integer textViewsColor) {
        TextView answeredTextView = (TextView) child.findViewById(R.id.post_answered_textview);
        answeredTextView.setTextColor(textViewsColor);
        if(post.getAnswered()){
            answeredTextView.setText(activity.getResources().getString(R.string.post_answered));
        } else {
            if(post.getPublisherID().equals(user.getId())) {
                Time.setTime(activity, answeredTextView,Long.parseLong(post.getDate()) + 3000*60*60,
                        String.valueOf(Calendar.getInstance().getTimeInMillis()),
                        "", activity.getResources().getString(R.string.post_to_be_answered));
            }
        }
    }

    /**
     * Setting the background color of post
     * @param child - layout
     * @param color - color of post
     */
    private void setBackgroundColor(View child, Integer color) {
        LinearLayout view = (LinearLayout) child.findViewById(R.id.post_layout);
        if(color!=0){
            view.setBackground(activity.getResources().getDrawable(R.drawable.background_style_8));
            GradientDrawable drawable = (GradientDrawable) view.getBackground();
            drawable.setColor(color);
        }
    }

    /**
     * Setting custom string text to search textview box.
     * @param text - string
     * @param searchQuery - query.
     * @return
     */
    private SpannableStringBuilder paintString(String text,String searchQuery) {
        final SpannableStringBuilder sb = new SpannableStringBuilder(text);
        if(searchQuery.equals("")) return sb;
        String[] parts = text.split(searchQuery);
        //if starts with searchQuery:
        if(text.startsWith(searchQuery)) sb.setSpan(new ForegroundColorSpan(SEARCH_COLOR),0,searchQuery.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        int acuumaltingLength = 0;
        for(int i = 0; i < parts.length-1; i++){
            if(!text.substring(acuumaltingLength + parts[i].length(), acuumaltingLength + parts[i].length() + searchQuery.length()).equals(searchQuery)) continue;
            sb.setSpan(new ForegroundColorSpan(SEARCH_COLOR),
                    acuumaltingLength + parts[i].length(),
                    acuumaltingLength + parts[i].length() + searchQuery.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                    acuumaltingLength + parts[i].length(),
                    acuumaltingLength + parts[i].length() + searchQuery.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            acuumaltingLength += parts[0].length() + searchQuery.length();
        }
        return sb;
    }

    /**
     * Requesting more comments from server.
     * @param child layout
     * @param post - post object.
     * @param textViewsColor
     */
    private void setMoreComments(View child, Post post, Integer textViewsColor) {
        final TextView moreCommentsLabel = (TextView) child.findViewById(R.id.post_more_comments_text);
        moreCommentsLabel.setTextColor(textViewsColor);
        final Post newPost = new Post(post);
        moreCommentsLabel.setText(activity.getResources().getString(R.string.more_comments) +
                    " " + activity.getResources().getString(R.string.overall) +
                ": " + String.valueOf(post.getOverallComments()));
        moreCommentsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPost.setShown_comments(newPost.getShown_comments()+NUM_OF_COMMENTS_IN_UPDATE);
                String getCommentUrl = activity.getResources().getString(R.string.request_get_comments_url) +
                        activity.getResources().getString(R.string.slash) + newPost.getId() +
                        activity.getResources().getString(R.string.slash) + String.valueOf(newPost.getShown_comments());
                String key = activity.getResources().getString(R.string.comments_arrived_by_user);
                GetEntity getComments = new GetEntity(response,key,cookied,
                        activity.getResources().getString(R.string.getcookie),
                        activity.getResources().getString(R.string.setCookie));
                getComments.execute(new String[]{getCommentUrl});
            }
        });
    }

    /**
     * setting the functiallty of delete post.
     * @param child - layout
     * @param post - post object
     */
    private void setDelSettings(View child, Post post) {
        final TextView delLabel = (TextView) child.findViewById(R.id.post_del_text);
        final Post newPost = new Post(post);
        delLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = activity.getResources().getString(R.string.key_del_post);
                DeletePost deletePost = new DeletePost(response,key,cookied,
                        activity.getResources().getString(R.string.getcookie),
                        activity.getResources().getString(R.string.setCookie));

                String delPostsUrl = activity.getResources().getString(R.string.request_del_post_url)
                                        + newPost.getId();
                deletePost.execute(new String[]{delPostsUrl});
            }
        });
    }

    /**
     * setting the functiallty of edit button.
     * attaching UI and defining the listerners to UI componenets.
     * @param child - layout
     * @param post - post object
     * @param post_title - post title
     * @param post_message - post message
     */
    private void setEditSettings(View child, Post post, final TextView post_title, final TextView post_message) {
        final ViewSwitcher titleSwitcher = (ViewSwitcher) child.findViewById(R.id.post_title_switcher);
        final ViewSwitcher messageSwitcher = (ViewSwitcher) child.findViewById(R.id.post_message_switcher);
        final TextView editLabel = (TextView) child.findViewById(R.id.post_edit_text);
        final EditText titleEditText = (EditText) child.findViewById(R.id.post_title_edit_box);
        final EditText messageEditText = (EditText) child.findViewById(R.id.post_message_edit_box);
        final Post newPost = new Post(post);

        editLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleSwitcher.showNext();
                messageSwitcher.showNext();
                if(editLabel.getText().toString().equals(activity.getResources().getString(R.string.edit_text))){
                    titleEditText.setText(post_title.getText().toString());
                    messageEditText.setText(post_message.getText().toString());
                    editLabel.setText(activity.getResources().getString(R.string.finish));
                } else {
                    EditPost editPost = new EditPost(response, activity.getResources().getString(R.string.key_edit_post),
                            cookied,activity.getResources().getString(R.string.getcookie),
                            activity.getResources().getString(R.string.setCookie),activity);
                    String[] keys = {
                            activity.getResources().getString(R.string.key_post_title),
                            activity.getResources().getString(R.string.key_post_message),
                            activity.getResources().getString(R.string.key_post_publisher),
                            activity.getResources().getString(R.string.key_post_publisherID),
                            activity.getResources().getString(R.string.key_post_date),
                    };
                    newPost.setTitle(titleEditText.getText().toString());
                    newPost.setMessage(messageEditText.getText().toString());
                    newPost.setDate(String.valueOf(Calendar.getInstance().getTimeInMillis()));

                    String sendMessageUrl = activity.getResources().getString(R.string.request_edit_post_url)
                            + String.valueOf(newPost.getId());
                    editPost.execute(new PostPack[]{new PostPack(sendMessageUrl, newPost, keys,true)});

                    editLabel.setText(activity.getResources().getString(R.string.edit_text));
                }
            }
        });
    }

    /**
     * Setting the scaled bitmap image.
     * @param view
     * @param uri - uri to image
     */
    private void setScaledBitmap(View view, Uri uri){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            Bitmap b = BitmapHelper.getScaledBitmap(bitmap, 400, 400);
            if(bitmap!=null) {
                image = ImageBase64.encodeTobase64(b);
                view.setBackground(new BitmapDrawable(b));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set method
     * @param lastImage
     */
    public void setLastImage(String lastImage) {
        this.lastImage = lastImage;
    }

    /**
     * set method
     * @param x
     */
    public void SetLastImageX(String x) {this.imageX = x;
    }

    /**
     * set method
     * @param y
     */
    public void SetLastImageY(String y) {this.imageY = y;
    }

    /**
     * set method
     * @param lastLink
     */
    public void setLastLink(String lastLink) {
        this.lastLink = lastLink;
    }

}
