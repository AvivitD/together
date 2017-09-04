package com.weallone.raz.together.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weallone.raz.together.Entities.Message;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.R;
import com.weallone.raz.together.Tools.ImageBase64;
import com.weallone.raz.together.Tools.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This adapter is used to paint the messages in the Chat tab.
 */
public class MessageAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflator;
    private List<Message> msgs = new ArrayList<>();
    private UserInfo user;
    private AsyncResponse response;
    private Cookied cookied;

    /**
     * Constructor - init members.
     * @param activity
     * @param msgs - list of messages in conversation
     * @param user - this userinfo
     * @param response - the callback class
     * @param cookied - the cookie interface
     */
    public MessageAdapter(Activity activity, List<Message> msgs, UserInfo user, AsyncResponse response, Cookied cookied) {
        this.activity = activity;
        this.inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.msgs = msgs;
        this.user = user;
        this.response = response;
        this.cookied = cookied;
    }

    /**
     * Get functin
     * @return size of list
     */
    @Override
    public int getCount() {
        return msgs.size();
    }

    /**
     * Get method
     * @param position in list
     * @return the object
     */
    @Override
    public Object getItem(int position) {
        return msgs.get(position);
    }

    /**
     * Get method
     * @param position in list
     * @return position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get method.
     * Building the message view object and returnning it.
     * @param position - place in list
     * @param convertView - the view to edit.
     * @param parent of view.
     * @return the edited view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message msg = msgs.get(position);
        if(convertView==null)
            convertView = this.inflator.inflate(R.layout.message_layout, null);
        //Attach UI.
        LinearLayout messageMainLayout = (LinearLayout) convertView.findViewById(R.id.chat_message_orientation);
        LinearLayout messageLayout = (LinearLayout) convertView.findViewById(R.id.chat_message_layout);
        LinearLayout textOrientationLayout = (LinearLayout) convertView.findViewById(R.id.text_orienatation);
        TextView chatMessageContent = (TextView) convertView.findViewById(R.id.chat_message_content);
        TextView chatMessageTime = (TextView) convertView.findViewById(R.id.chat_message_time);
        TextView chatMessageAuthor = (TextView) convertView.findViewById(R.id.chat_message_author);
        LinearLayout messageLinkLayout = (LinearLayout) convertView.findViewById(R.id.message_link_layout);
        TextView chatMessageLink = (TextView) convertView.findViewById(R.id.chat_message_link);
        ImageView chatMessageImage = (ImageView) convertView.findViewById(R.id.chat_message_image);

        //Populate UI.
        if(msg.getMessage()!=null && chatMessageContent!=null) chatMessageContent.setText(msg.getMessage().toString());
        if(msg.getAuthorName()!=null && chatMessageAuthor!=null) chatMessageAuthor.setText(msg.getAuthorName());
        if(msg.getTime()!=null && chatMessageTime!=null)
            Time.setTime(activity, chatMessageTime, Calendar.getInstance().getTimeInMillis(),
                    msg.getTime(), activity.getResources().getString(R.string.date_ago), "");
        Log.d("IMAGE",position + "  " +msg.getNum() + "  " +msg.getImage());
        if(msg.getImage()!=null && !msg.getImage().equals("") && chatMessageImage!=null && msg.getImage().length()>30){
            setImage(chatMessageImage, msg);
//            LayoutWrapContentUpdater.wrapContentAgain(messageMainLayout);
//            LayoutWrapContentUpdater.wrapContentAgain(messageLayout);
        } else if (chatMessageImage!= null){
            chatMessageImage.setVisibility(View.GONE);
        }
        if(msg.getLink()!=null && !msg.getLink().equals("") && chatMessageLink!=null){
            setLink(chatMessageLink, msg.getLink());
            messageLinkLayout.setVisibility(View.VISIBLE);
        } else {
            messageLinkLayout.setVisibility(View.GONE);
        }
        setCorrectOrientation(messageMainLayout,messageLayout,textOrientationLayout,
                (LinearLayout) convertView.findViewById(R.id.chat_link_container),user.getId(), msg.getAuthorID());
        return convertView;
    }

    /**
     * Set correct orientation(if it is this user message or peer.
     * @param messageMainLayout - main laylout.
     * @param messageLayout - layout of message
     * @param textOriantation - desired orientation
     * @param messageLinkLayout - link view.
     * @param id - of user
     * @param authorID - of author.
     */
    private void setCorrectOrientation(LinearLayout messageMainLayout,LinearLayout messageLayout,
                                       LinearLayout textOriantation,
                                       LinearLayout messageLinkLayout, String id, String authorID) {
        if(id.equals(authorID)){
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            messageMainLayout.setLayoutParams(params);
            messageLayout.setGravity(Gravity.RIGHT);
            messageLinkLayout.setGravity(Gravity.RIGHT);
            textOriantation.setGravity(Gravity.RIGHT);

            messageLayout.setBackground(activity.getResources().getDrawable(R.drawable.message_background1));
        } else {
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            messageMainLayout.setLayoutParams(params);
            messageLayout.setGravity(Gravity.LEFT);
            messageLayout.setBackground(activity.getResources().getDrawable(R.drawable.message_background));
        }
    }

    /**
     * Setting the link property of message.
     * @param textView - symbols link.
     * @param link the string of link.
     */
    private void setLink(TextView textView, String link) {
        final String url = link;
        textView.setOnClickListener(new View.OnClickListener() {
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
     * Setting the image of a view.
     * @param image - view itself
     * @param msg - the message object.
     */
    private void setImage(ImageView image,Message msg) {
        image.getLayoutParams().width = Integer.valueOf(msg.getImageX());
        image.getLayoutParams().height = Integer.valueOf(msg.getImageY());
        image.requestLayout();
        Bitmap bitmap = ImageBase64.decodeBase64(msg.getImage(), activity);
        Drawable dr = new BitmapDrawable(bitmap);
        image.setBackground(dr);
    }

    /**
     * Set method
     * @param list to set.
     */
    public void setList(List<Message> list) {
        this.msgs = list;
    }
}
