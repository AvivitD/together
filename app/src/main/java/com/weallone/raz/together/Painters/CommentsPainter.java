package com.weallone.raz.together.Painters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.weallone.raz.together.AsyncTasks.DeleteComment;
import com.weallone.raz.together.AsyncTasks.EditComment;
import com.weallone.raz.together.Entities.Comment;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Enums.Account;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.Packs.CommentPack;
import com.weallone.raz.together.R;
import com.weallone.raz.together.Tools.ImageBase64;
import com.weallone.raz.together.Tools.Time;

import java.util.Calendar;
import java.util.List;

/**
 * Painter for Comments lists in Posts.
 */
public class CommentsPainter {


    private final Activity activity;
    private final AsyncResponse response;
    private final Cookied cookied;
    private final LayoutInflater inflater;
    private static Integer PSYCHOLOGIST_COLOR;
    private static Integer DEVELOPER_COLOR;
    private UserInfo user;

    /**
     * Constructor - init members.
     * @param activity - actviity
     * @param response - response object(Post)
     * @param cookied - cookie object(Post)
     * @param inflater - inflater
     * @param user - user objct
     */
    public CommentsPainter(Activity activity, AsyncResponse response, Cookied cookied, LayoutInflater inflater
                            ,UserInfo user) {
        this.activity = activity;
        this.response = response;
        this.cookied = cookied;
        this.inflater = inflater;
        this.user = user;
        PSYCHOLOGIST_COLOR = activity.getResources().getColor(R.color.psychologist_name);
        DEVELOPER_COLOR = activity.getResources().getColor(R.color.developer_name);
    }

    /**
     * Paint function, paint each comment on the child view
     * @param child - layout view
     * @param comments - the list of comments object.
     * @param complimentColor
     */
    public void paint(View child, List<Comment> comments, int complimentColor) {
        LinearLayout layout = (LinearLayout) child.findViewById(R.id.comments_container);
        for(Comment comment : comments){
            View commentLayout = inflater.inflate(R.layout.comment_layout,null);
            if(commentLayout==null) continue;

            setCommentDetails(commentLayout, comment, complimentColor);

            layout.addView(commentLayout);
        }
    }

    /**
     * Populating the comment views.
     * Attaching UI and defining listerners.
     * @param commentLayout - layout for this specific comment
     * @param comment comment object
     * @param complimentColor
     */
    private void setCommentDetails(View commentLayout, Comment comment, int complimentColor) {
        final TextView editBtn = (TextView) commentLayout.findViewById(R.id.post_comment_edit_btn);
        TextView delBtn = (TextView) commentLayout.findViewById(R.id.post_comment_del_btn);
        final TextView msg = (TextView) commentLayout.findViewById(R.id.post_comment_message);
        TextView author = (TextView) commentLayout.findViewById(R.id.post_comment_author);
        TextView comment_date = (TextView) commentLayout.findViewById(R.id.post_comment_time);
        final ViewSwitcher switcher = (ViewSwitcher) commentLayout.findViewById(R.id.comment_switcher);
        final EditText editMsgText = (EditText) commentLayout.findViewById(R.id.post_comment_edit_box);
        final Comment newComment = comment;

        if((comment.getPublisherEntity().equals(Account.PSYCHOLOGIST.toString())
                || comment.getPublisherEntity().equals(Account.MANAGER.toString()))) {
            author.setTextColor(PSYCHOLOGIST_COLOR);
            author.setTypeface(null, Typeface.BOLD);
            author.setTextSize(activity.getResources().getDimension(R.dimen.small_meduim_text));
        }
        else if(comment.getPublisherEntity().equals(Account.DEVELOPER.toString())) {
            author.setTextColor(DEVELOPER_COLOR);
            author.setTypeface(null, Typeface.BOLD);
            author.setTextSize(activity.getResources().getDimension(R.dimen.small_meduim_text));
        } else {
            author.setTextColor(complimentColor);
        }
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
                if (editBtn.getText().toString().equals(activity.getResources().getString(R.string.edit_text))) {
                    editMsgText.setText(msg.getText().toString());

                    editBtn.setText(activity.getResources().getString(R.string.finish));
                } else {
                    EditComment editComment = new EditComment(response, activity.getResources().getString(R.string.key_edit_comment),
                            cookied, activity.getResources().getString(R.string.getcookie),
                            activity.getResources().getString(R.string.setCookie),activity);
                    String[] keys = {
                            activity.getResources().getString(R.string.key_comment_message),
                            activity.getResources().getString(R.string.key_comment_publisher),
                            activity.getResources().getString(R.string.key_comment_publisher_id),
                            activity.getResources().getString(R.string.key_comment_date),
                            activity.getResources().getString(R.string.key_comment_post_id),
                    };
                    newComment.setMessage(editMsgText.getText().toString());
                    String sendMessageUrl = activity.getResources().getString(R.string.request_edit_comment_url)
                            + activity.getResources().getString(R.string.slash) + String.valueOf(newComment.getId());
                    editComment.execute(new CommentPack[]{new CommentPack(sendMessageUrl, newComment, keys, true)});

                    msg.setText(newComment.getMessage().toString());
                    editBtn.setText(activity.getResources().getString(R.string.edit_text));
                }
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteComment deleteComment = new DeleteComment(response, activity.getResources().getString(R.string.key_del_comment),
                        cookied,activity.getResources().getString(R.string.getcookie),
                        activity.getResources().getString(R.string.setCookie));
                String deleteCommentURL = activity.getResources().getString(R.string.request_del_comment_url)
                        + newComment.getId() + activity.getResources().getString(R.string.slash)
                        + newComment.getPostID();
                deleteComment.execute(new String[]{deleteCommentURL});
            }
        });
        if(user.getAccount()==Account.CHILD && !user.getId().equals(comment.getPublisherID())){
            editBtn.setVisibility(View.INVISIBLE);
            delBtn.setVisibility(View.INVISIBLE);
        }
        if(msg !=null){
            msg.setTextColor(complimentColor);
            msg.setText(comment.getMessage().toString());
        }
        if(author != null) author.setText(comment.getPublisher().toString());
        if(comment.getDate()==null) Log.d("COMMENT-DATE", "NULL");
        if(comment_date==null) Log.d("view-DATE", "NULL");
        if(comment.getDate()!=null && comment_date!=null){
            comment_date.setTextColor(complimentColor);
            Time.setTime(activity, comment_date, Calendar.getInstance().getTimeInMillis(),
                    comment.getDate(), activity.getResources().getString(R.string.date_ago), "");
        }
        setImageAndLink(commentLayout, comment);
    }

    /**
     * Setting the image and link view.
     * @param child - view to attach to
     * @param comment - object containing the image and link
     */
    private void setImageAndLink(View child, Comment comment) {
        final View _view = child;
        TextView link = (TextView) child.findViewById(R.id.comment_link);
        ImageView image = (ImageView) child.findViewById(R.id.comment_image);
        if(link !=null && !comment.getLink().equals("")) link.setText(activity.getResources().getString(R.string.link_text));
        if(image !=null && !comment.getImage().equals("") && comment.getImage().length() > 30) {
            Log.d("IMAGE",comment.getImage());
            image.getLayoutParams().width = Integer.valueOf(comment.getImageX());
            image.getLayoutParams().height = Integer.valueOf(comment.getImageY());
            image.requestLayout();
            Bitmap bitmap = ImageBase64.decodeBase64(comment.getImage(), activity);
            Drawable dr = new BitmapDrawable(bitmap);
            image.setBackground(dr);
        }
        if(comment.getLink().equals("")) _view.findViewById(R.id.link_layout_in_comment).setVisibility(View.INVISIBLE);
        final String url = comment.getLink();
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
        if(comment.getLink().equals("")) child.findViewById(R.id.link_layout_in_comment).setVisibility(View.GONE);
    }
}
