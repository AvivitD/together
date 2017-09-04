package com.weallone.raz.together.Painters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Raz on 3/15/2017.
 */
public class QueuePostPainter {
    private LinearLayout layout;
    private LayoutInflater inflater;
    private Activity activity;
    private UserInfo user;
    private AsyncResponse response;
    private Cookied cookied;
    private LinearLayout container;

    public QueuePostPainter(LinearLayout layout, LayoutInflater inflater,
                       Activity activity, UserInfo user, Cookied cookied, AsyncResponse response, LinearLayout container) {
        this.layout = layout;
        this.inflater = inflater;
        this.activity = activity;
        this.user = user;
        this.cookied = cookied;
        this.response = response;
        this.container = container;
    }

    public void paint(String queueString){
        List<PostRepresentation> queue = DecihperQueue(queueString);
        layout.removeAllViews();
        for(PostRepresentation p: queue){
            View child = inflater.inflate(R.layout.post_queue_representation,null);
            if(child==null) continue;

            setPostRepresentationDetails(child,p);
            setOnClick(child,p.getId());
            layout.addView(child);
        }

    }

    private void setPostRepresentationDetails(View child, PostRepresentation p) {
        TextView author = (TextView) child.findViewById(R.id.post_queue_rep_author);
        TextView time = (TextView) child.findViewById(R.id.post_queue_rep_time);

        if(author!=null) author.setText(p.getAuthor());
        if(time!=null) setTime(time, Calendar.getInstance().getTimeInMillis(),
                p.getTime(),activity.getResources().getString(R.string.date_ago),"");
    }

    private List<PostRepresentation> DecihperQueue(String queueString){
        List<PostRepresentation> queue = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(queueString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                queue.add(DecihperPostRepresentation(obj));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return queue;
    }

    private void setTime(TextView view,long toWhen, String published, String before,String after){
        long diff = toWhen - Long.parseLong(published);
        int seconds = (int) (diff / 1000);
        int mins = (int) (diff / (1000 * 60));
        int hours = (int) (diff / (1000 * 60 * 60));
        int days = (int) (diff / (1000 * 60 * 60 * 24));
        int weeks = (int) (diff / (1000 * 60 * 60 * 24 * 7));
        int months = (int) (diff / (1000 * 60 * 60 * 24 * 7 *4));
        int years = (int) (diff / (1000 * 60 * 60 * 24 * 7 * 4 * 12));
        if(seconds >=0) view.setText(before + " " + String.valueOf(seconds)+ " " +activity.getResources().getString(R.string.seconds) + " " + after);
        if(mins >0) view.setText(before+ " " + String.valueOf(mins)+ " " +activity.getResources().getString(R.string.mins)+ " " + after);
        if(hours >0)view.setText(before+ " " + String.valueOf(hours)+ " " +activity.getResources().getString(R.string.hours)+ " " + after);
        if(days >0)view.setText(before+ " " + String.valueOf(days)+ " " +activity.getResources().getString(R.string.days)+ " " + after);
        if(weeks >0)view.setText(before+ " " + String.valueOf(weeks)+ " " +activity.getResources().getString(R.string.weeks)+ " " + after);
        if(months >0)view.setText(before+ " " + String.valueOf(months)+ " " +activity.getResources().getString(R.string.months)+ " " + after);
        if(years >0)view.setText(before+ " " + String.valueOf(years)+ " " +activity.getResources().getString(R.string.years)+ " " + after);
    }

    private PostRepresentation DecihperPostRepresentation(JSONObject obj) throws JSONException {
        return new PostRepresentation(obj.get(activity.getResources().getString(R.string.post_id)).toString(),
                obj.get(activity.getResources().getString(R.string.time_text)).toString(),
                obj.get(activity.getResources().getString(R.string.author)).toString());
    }

    public void setOnClick(View child, String id) {
        final String _id = id;
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = activity.getResources().getString(R.string.getfront);
                GetEntity getEntity = new GetEntity(response,key,cookied,
                        activity.getResources().getString(R.string.getcookie),
                        activity.getResources().getString(R.string.setCookie));
                String getPostUrl = activity.getResources().getString(R.string.request_get__specific_posts_url).toString()
                        + _id;
                getEntity.execute(new String[]{getPostUrl});
                container.setVisibility(View.INVISIBLE);
            }
        });
    }

    class PostRepresentation {
        private String id;
        private String time;
        private String author;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public PostRepresentation(String id, String time,String author) {
            this.id = id;
            this.time = time;
            this.author = author;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
