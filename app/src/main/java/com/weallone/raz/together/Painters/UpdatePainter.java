package com.weallone.raz.together.Painters;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.weallone.raz.together.AsyncTasks.GetEntity;
import com.weallone.raz.together.AsyncTasks.SendLoginRequest;
import com.weallone.raz.together.Entities.Updates.Update;
import com.weallone.raz.together.Entities.UserInfo;
import com.weallone.raz.together.Interfaces.AsyncResponse;
import com.weallone.raz.together.Interfaces.Cookied;
import com.weallone.raz.together.R;
import com.google.api.client.util.DateTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Update painter
 */
public class UpdatePainter {


    private final Activity activity;
    private final AsyncResponse response;
    private final Cookied cookied;
    private final LayoutInflater inflater;
    private UserInfo user;
    private String TAG = "UpdatesPainter";
    private LinearLayout container = null;

    /**
     * Constructor - init members
     * @param activity - activity
     * @param response - response
     * @param cookied - cookie
     * @param inflater
     * @param user - use info
     */
    public UpdatePainter(Activity activity, AsyncResponse response, Cookied cookied, LayoutInflater inflater
            , UserInfo user) {
        this.activity = activity;
        this.response = response;
        this.cookied = cookied;
        this.inflater = inflater;
        this.user = user;
    }

    /**
     * Paint each update in the layout container.
     * Choosing which view too put by the type property.
     * @param container
     * @param updates
     */
    public void paint(LinearLayout container, List<Update> updates) {
        this.container = container;
        container.removeAllViews();
        for (int i = 0; i < updates.size(); i++) {
            String type = updates.get(i).getUpdate();
            View updateView = null;
            if (type == null) continue;
            if (type.equals(Update.NEW_SHIFT)) {
                updateView = createNewShift(updates.get(i));
            } else if (type.equals(Update.NEW_USER)) {
                updateView = createNewUser(updates.get(i));
            } else {
                continue;
            }
            if (updateView != null) container.addView(updateView);
            if (i != updates.size() - 1) container.addView(createDivider());
        }
    }

    /**
     * Create new user option view.
     * Attaching UI and defining listeners for UI componenets
     * @param update - object
     * @return the view populated.
     */
    private View createNewUser(final Update update) {
        View userUpdateLayout = inflater.inflate(R.layout.update_user, null);
        if (userUpdateLayout == null) return null;
        TextView title = (TextView) userUpdateLayout.findViewById(R.id.update_user_title);
        TextView content = (TextView) userUpdateLayout.findViewById(R.id.update_user_content);
        TextView approvedText = (TextView) userUpdateLayout.findViewById(R.id.update_user_approved_text);
        ViewSwitcher swichter = (ViewSwitcher) userUpdateLayout.findViewById(R.id.update_user_switcher);
        Button approveManagerBtn = (Button) userUpdateLayout.findViewById(R.id.update_user_approve_manager_btn);
        Button approvePsychoBtn = (Button) userUpdateLayout.findViewById(R.id.update_user_approve_psycho_btn);
        Button approveChildBtn = (Button) userUpdateLayout.findViewById(R.id.update_user_approve_child_btn);
        LinearLayout btnContainer = (LinearLayout) userUpdateLayout.findViewById(R.id.update_user_btn_container);

        final UserDetailes details;
        Boolean approved = null;
        try {
            details = DechiperUserDetailes(update.getJson());
            approved = DetectApprove(update.getJson());
        } catch (JSONException e) {
            return null;
        }

        if (title != null)
            title.setText(activity.getResources().getString(R.string.update_user_title)
                    + " " + details.getFirstName() + " " + details.getLastName());
        if (content != null) {
            if (details.getContent() == activity.getResources().getString(R.string.mail_update)) {
                content.setText(activity.getResources().getString(R.string.mail_update_text)
                        + " " + details.getContent());
            } else {
                content.setText(activity.getResources().getString(R.string.phone_update_text)
                        + " " + details.getContent());
            }

        }
        if (approved != null) {
            swichter.showNext();
            btnContainer.setVisibility(View.GONE);
            if (approvedText != null && approved == true)
                approvedText.setText(activity.getResources().getString(R.string.user_approved));
        }
        if(details.getWay().equals(activity.getResources().getString(R.string.mail_update)) && content!=null){
            content.setText(Html.fromHtml("<a href=\"mailto:" + details.getContent() + "?subject=" + activity.getResources().getString(R.string.update_user_mail_subject) +
                     "&amp;body=" + activity.getResources().getString(R.string.update_user_mail_mail1)
                    + " " + details.getFirstName() + " " + details.getLastName() + " " + activity.getResources().getString(R.string.update_user_mail_mail2) + "\" >" +
                    activity.getResources().getString(R.string.mail_update_text)
                    + " " + details.getContent() + "</a>"));
            content.setMovementMethod(LinkMovementMethod.getInstance());
        } else if(content!=null){
            Linkify.addLinks(content, Linkify.PHONE_NUMBERS);
            content.setLinksClickable(true);
            content.setMovementMethod(LinkMovementMethod.getInstance());
        }
        final String parentEmail = (details.getWay().equals(activity.getResources().getString(R.string.mail_update)))?details.getContent():"";
        final String parentPhone = (details.getWay().equals(activity.getResources().getString(R.string.mail_update)))?"":details.getContent();
        approveManagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveUser(details.getCode(), details.getFirstName(), details.getLastName(),
                        activity.getResources().getString(R.string.updates_new_user_manager_text_eng),update.getNum(),
                        parentEmail,parentPhone);
                Toast.makeText(activity.getApplicationContext(),activity.getResources().getString(R.string.update_was_sent),Toast.LENGTH_SHORT);
            }
        });
        approvePsychoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveUser(details.getCode(), details.getFirstName(), details.getLastName(),
                        activity.getResources().getString(R.string.updates_new_user_psycho_text_eng),update.getNum(),
                        parentEmail,parentPhone);
                Toast.makeText(activity.getApplicationContext(),activity.getResources().getString(R.string.update_was_sent),Toast.LENGTH_SHORT);
            }
        });
        approveChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveUser(details.getCode(), details.getFirstName(), details.getLastName(),
                        activity.getResources().getString(R.string.updates_new_user_child_text_eng),update.getNum(),
                        parentEmail, parentPhone);
                Toast.makeText(activity.getApplicationContext(),activity.getResources().getString(R.string.update_was_sent),Toast.LENGTH_SHORT);
            }
        });
        return userUpdateLayout;
    }

    /**
     * Send approval of user.
     * @param code - his device ID
     * @param first_name - his.
     * @param last_name - his
     * @param entity - the approval entity.
     * @param updatenum - update number.
     */
    private void ApproveUser(String code, String first_name, String last_name, String entity, Long updatenum,
                             String parentEmail, String parentPhone) {
        HashMap<String, String> hash = new HashMap<>();
        hash.put(activity.getResources().getString(R.string.key_phone_code), code);
        hash.put(activity.getResources().getString(R.string.first_name), first_name);
        hash.put(activity.getResources().getString(R.string.last_name), last_name);
        hash.put(activity.getResources().getString(R.string.key_entity), entity);
        hash.put(activity.getResources().getString(R.string.key_update_num), String.valueOf(updatenum));
        hash.put(activity.getResources().getString(R.string.key_parent_email), parentEmail);
        hash.put(activity.getResources().getString(R.string.key_parent_phone), parentPhone);
        new SendLoginRequest(response,activity.getResources().getString(R.string.key_approev_login_req), cookied,
                activity.getResources().getString(R.string.getcookie), activity.getResources().getString(R.string.setCookie),
                activity.getResources().getString(R.string.request_approve_login_req_url), hash, activity).execute();
    }

    /**
     * Getting update object from JSON object
     * @param json object
     * @return user.
     * @throws JSONException
     */
    private UserDetailes DechiperUserDetailes(JSONObject json) throws JSONException {
        return new UserDetailes(json.getString(activity.getResources().getString(R.string.code)),
                json.getString(activity.getResources().getString(R.string.first_name)),
                json.getString(activity.getResources().getString(R.string.last_name)),
                json.getString(activity.getResources().getString(R.string.content)),
                json.getString(activity.getResources().getString(R.string.way)));
    }

    /**
     * Creating new shift view.
     * Attaching UI components and defining listeners to those components.
     * @param update object
     * @return the populated view.
     */
    private View createNewShift(final Update update) {
        View shiftUpdateLayout = inflater.inflate(R.layout.update_shift,null);
        if(shiftUpdateLayout==null) return null;
        TextView title = (TextView) shiftUpdateLayout.findViewById(R.id.update_shift_title);
        TextView day = (TextView) shiftUpdateLayout.findViewById(R.id.update_shift_day);
        TextView start = (TextView) shiftUpdateLayout.findViewById(R.id.update_shift_start);
        TextView end = (TextView) shiftUpdateLayout.findViewById(R.id.update_shift_end);
        Button approve = (Button) shiftUpdateLayout.findViewById(R.id.update_shift_approve);
        Button disApprove = (Button) shiftUpdateLayout.findViewById(R.id.update_shift_disapprove);

        ViewSwitcher switcher = (ViewSwitcher) shiftUpdateLayout.findViewById(R.id.update_shift_switcher);
        TextView approvedText = (TextView) shiftUpdateLayout.findViewById(R.id.update_shift_approve_text);

        final ShiftDetailes details;
        Boolean approved = null;
        try {
            details = DechiperShiftDetailes(update.getJson());
            approved = DetectApprove(update.getJson());
        } catch (JSONException e) {
            return null;
        }

        if(title!=null && details.getName()!=null)
            title.setText(activity.getResources().getString(R.string.update_shift_title)
                    + " " +  details.getName());
        if(day!=null && details.getStart()!=null)
            day.setText(activity.getResources().getString(R.string.update_shift_day_text) + " "
                    + Day(details.getStart()));
        if(start!=null && details.getStart()!=null)
            start.setText(activity.getResources().getString(R.string.update_shift_beginging) + " "
                    + Hour(details.getStart()));
        if(end!=null && details.getEnd()!=null)
            end.setText(activity.getResources().getString(R.string.update_shift_finish) + " "
                    + Hour(details.getEnd()));

        if(approved!=null){
            switcher.showNext();
            ((LinearLayout)shiftUpdateLayout.findViewById(R.id.update_shift_btns_container)).setVisibility(View.GONE);
            if(approvedText!=null && approved==true) approvedText.setText(activity.getResources().getString(R.string.shift_approved));
            if(approvedText!=null && approved==false) approvedText.setText(activity.getResources().getString(R.string.shift_disapproved));
        }
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveShift(details.getId(), true, update.getNum());
//                UpdateShiftUpdate(update, details, true);
                Toast.makeText(activity.getApplicationContext(),activity.getResources().getString(R.string.update_was_sent),Toast.LENGTH_SHORT);
            }
        });
        disApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveShift(details.getId(), false, update.getNum());
            }
        });
        return shiftUpdateLayout;
    }

    /**
     * Check if approved
     * @param json object
     * @return if approved or not(the update).
     * @throws JSONException
     */
    private Boolean DetectApprove(JSONObject json) throws JSONException {
        if(json.has(activity.getResources().getString(R.string.approve_english_text))){
            return json.getBoolean(activity.getResources().getString(R.string.approve_english_text));
        } else {
            return null;
        }
    }

    /**
     * Not used.
     * @param update
     * @param details
     * @param b
     */
    private void UpdateShiftUpdate(Update update, ShiftDetailes details, boolean b) {
        String key = activity.getResources().getString(R.string.key_update_shift_update);
        GetEntity getEntity = new GetEntity(response,key,cookied,
                activity.getResources().getString(R.string.getcookie),
                activity.getResources().getString(R.string.setCookie));
        String updateShiftUpdateUrl = activity.getResources().getString(R.string.request_update_shift_update_url)
                + update.getNum() + activity.getResources().getString(R.string.slash)
                + "\"{ \"start\" :\""+ details.getStart() + "\" , \"end\" : \"" + details.getEnd() + "\", \"name\": \""
                + details.getName() + "\", \"id\": \"" + details.getId() + "\", \"approve\": \""
                + String.valueOf(b) +"\" }\"";
        getEntity.execute(new String[]{updateShiftUpdateUrl});
    }

    /**
     * Approved shift
     * @param id - of shift
     * @param approve - true/false
     * @param num - update number.
     */
    private void ApproveShift(String id, Boolean approve, Long num) {
        String key = activity.getResources().getString(R.string.key_approve_shift);
        GetEntity getEntity = new GetEntity(response,key,cookied,
                activity.getResources().getString(R.string.getcookie),
                activity.getResources().getString(R.string.setCookie));
        String approveShiftUrl = activity.getResources().getString(R.string.request_approve_shift_url)
                + id + activity.getResources().getString(R.string.slash) + String.valueOf(approve)
                + activity.getResources().getString(R.string.slash) + String.valueOf(num);
        getEntity.execute(new String[]{approveShiftUrl});
    }

    /**
     * getting hour from string
     * @param d string
     * @return the hour string
     */
    private String Hour(String d) {
        Date date = new Date(new DateTime(d).getValue());
        DateFormat formatter = new SimpleDateFormat(activity.getResources().getString(R.string.hour_format));
        return formatter.format(date);
    }

    /**
     * getting dat from date string
     * @param d string
     * @return day as string.
     */
    private String Day(String d) {
        Date date = new Date(new DateTime(d).getValue());
        DateFormat formatter = new SimpleDateFormat(activity.getResources().getString(R.string.date_format));
        return formatter.format(date);
    }

    /**
     * getting shift object from JSON object.
     * @param json
     * @return
     * @throws JSONException
     */
    private ShiftDetailes DechiperShiftDetailes(JSONObject json) throws JSONException {
        return new ShiftDetailes(json.getString(activity.getResources().getString(R.string.name)),
                json.getString(activity.getResources().getString(R.string.id)),
                json.getString(activity.getResources().getString(R.string.start)),
                json.getString(activity.getResources().getString(R.string.end)));
    }

    /**
     * creating divder between two updates.
     * @return
     */
    private View createDivider() {
        View view = inflater.inflate(R.layout.quote_divider,null);
        return view;
    }

    /**
     * Helper function for shift representation.
     */
    private class ShiftDetailes {
        private String name = null;
        private String id = null;
        private String start = null;
        private String end = null;

        /**
         * Constructor
         * @param name - psycho
         * @param id - of psycho
         * @param start - time
         * @param end - time
         */
        public ShiftDetailes(String name, String id, String start, String end) {
            this.name = name;
            this.id = id;
            this.start = start;
            this.end = end;
        }

        /**
         * get method
         * @return
         */
        public String getName() {

            return name;
        }

        /**
         * set method
         * @param name
         */
        public void setName(String name) {
            this.name = name;
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

        /**
         * get method
         * @return
         */
        public String getStart() {
            return start;
        }

        /**
         * set method
         * @param start
         */
        public void setStart(String start) {
            this.start = start;
        }

        /**
         * get method
         * @return
         */
        public String getEnd() {
            return end;
        }

        /**
         * set method
         * @param end
         */
        public void setEnd(String end) {
            this.end = end;
        }
    }

    /**
     * Helper class for user representation.
     */
    private class UserDetailes {
        private String code;
        private String first_name;
        private String last_name;
        private String content;
        private String way;

        /**
         * get method
         * @return
         */
        public String getCode() {
            return code;
        }

        /**
         * set method
         * @param code
         */
        public void setCode(String code) {
            this.code = code;
        }

        /**
         * getmethod
         * @return
         */
        public String getContent() {
            return content;
        }

        /**
         * set method
         * @param content
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         * get method
         * @return
         */
        public String getFirstName() {
            return first_name;
        }

        /**
         * set method
         * @param first_name
         */
        public void setFirstName(String first_name) {
            this.first_name = first_name;
        }

        /**
         * get method
         * @return
         */
        public String getLastName() {
            return last_name;
        }

        /**
         * set method
         * @param last_name
         */
        public void setLastName(String last_name) {
            this.last_name = last_name;
        }

        /**
         * get method
         * @return
         */
        public String getWay() {
            return way;
        }

        /**
         * set method
         * @param way
         */
        public void setWay(String way) {
            this.way = way;
        }

        /**
         * Constructor - init members
         * @param code - deviceId
         * @param first_name- name
         * @param last_name- name
         * @param content
         * @param way
         */
        public UserDetailes(String code, String first_name, String last_name, String content, String way) {

            this.code = code;
            this.first_name = first_name;
            this.last_name = last_name;
            this.content = content;
            this.way = way;
        }
    }
}
