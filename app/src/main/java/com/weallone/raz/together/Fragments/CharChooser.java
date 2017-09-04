package com.weallone.raz.together.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.ViewSwitcher;

import com.weallone.raz.together.R;

/**
 * This Fragment represents the character selection view.
 */
public class CharChooser extends Fragment implements ViewSwitcher.ViewFactory {


    private static final String TAG = "CharChooser.Fragment";
    private Switch genderSwitch = null;
    private Switch typeSwitch = null;
    private Switch faceSwitch = null;
    private RadioGroup radioGroupShirt = null;
    private RadioGroup radioGroupHair = null;
    private RadioButton radioButtonShirt1 = null;
    private RadioButton radioButtonShirt2 = null;
    private RadioButton radioButtonShirt3 = null;
    private RadioButton radioButtonShirt4 = null;
    private RadioButton radioButtonHair1 = null;
    private RadioButton radioButtonHair2 = null;
    private RadioButton radioButtonHair3 = null;
    private RadioButton radioButtonHair4 = null;
    private ImageView imageView = null;
    private String[] gender = {"boy","girl"};
    private String[] type = {"1","2"};
    private String[] face = {"happy","sad"};
    private String[] shirt1 = {"blue","green","red","yellow"};
    private String[] shirt2 = {"blue","orange","purple","red"};
    private String[] shirt3 = {"blue","green","orange","red"};
    private String[] shirt4 = {"blue","green","orange","pink"};
    private String[] hair = {"black","brown","yellow","orange"};
    private int genderInt = 0;
    private int typeInt = 0;
    private int faceInt = 1;
    private int shirtInt = 0;
    private int hairInt = 0;
    private String character = null;

    private CompoundButton.OnCheckedChangeListener listener1 = null;
    private RadioGroup.OnCheckedChangeListener listener2 = null;
    private SharedPreferences settings = null;

    /**
     * default constructor
     */
    public CharChooser() {
        // Required empty public constructor
    }


    /**
     * Standard onCreateView method,
     * Init shared prefences,
     * Init UI.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view edited.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_char_chooser, container, false);
        initSharedRefences();
        initUI(view);
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * InitShared prefences and characters bundle settings.
     */
    private void initSharedRefences() {
        settings = getActivity().getSharedPreferences(getResources().getString(R.string.data),getActivity().MODE_PRIVATE);
        settings = getActivity().getApplicationContext().getSharedPreferences(
                getResources().getString(R.string.prefs), 0);
        character = settings.getString(getResources().getString(R.string.key_user_character),null);
    }

    /**
     * AttachUI and defineing listeners to views.
     * @param view - to edit.
     */
    private void initUI(View view) {
        genderSwitch = (Switch) view.findViewById(R.id.char_chooser_gender);
        typeSwitch = (Switch) view.findViewById(R.id.char_chooser_type);
        faceSwitch = (Switch) view.findViewById(R.id.char_chooser_face);
        imageView = (ImageView) view.findViewById(R.id.char_chooser_image);
        radioGroupShirt = (RadioGroup) view.findViewById(R.id.char_chooser_radiogroup_shirt);
        radioGroupHair = (RadioGroup) view.findViewById(R.id.char_chooser_radiogroup_hair);
        radioButtonShirt1 = (RadioButton) view.findViewById(R.id.char_chooser_radio_shirt_1);
        radioButtonShirt2 = (RadioButton) view.findViewById(R.id.char_chooser_radio_shirt_2);
        radioButtonShirt3 = (RadioButton) view.findViewById(R.id.char_chooser_radio_shirt_3);
        radioButtonShirt4 = (RadioButton) view.findViewById(R.id.char_chooser_radio_shirt_4);
        radioButtonHair1 = (RadioButton) view.findViewById(R.id.char_chooser_radio_hair_1);
        radioButtonHair2 = (RadioButton) view.findViewById(R.id.char_chooser_radio_hair_2);
        radioButtonHair3 = (RadioButton) view.findViewById(R.id.char_chooser_radio_hair_3);
        radioButtonHair4 = (RadioButton) view.findViewById(R.id.char_chooser_radio_hair_4);
        setListeners();

        //definign listerens
        genderSwitch.setOnCheckedChangeListener(listener1);
        typeSwitch.setOnCheckedChangeListener(listener1);
        faceSwitch.setOnCheckedChangeListener(listener1);
        radioGroupShirt.setOnCheckedChangeListener(listener2);
        radioGroupHair.setOnCheckedChangeListener(listener2);
        setCharacter();
    }

    /**
     * Animation for image view changeing.
     * @param c - context
     * @param v - image view
     * @param new_image - bitmap.
     */
    public static void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    /**
     * Setting listeners for the changes of buttons.
     */
    private void setListeners() {
        //gender switching listener
        listener1 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView==genderSwitch){
                    genderInt = (genderInt +1) %2;
                    typeInt = (typeInt +1) %2;
                } else if(buttonView==typeSwitch){
                    typeInt = (typeInt +1) %2;
                } else if(buttonView==faceSwitch) {
                    faceInt = (faceInt + 1) % 2;
                }
                setCharacter();
            }
        };

        //shit switching listerner
        listener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==radioButtonShirt1.getId()){
                    shirtInt = 0;
                } else if(checkedId==radioButtonShirt2.getId()){
                    shirtInt = 1;
                }  else if(checkedId==radioButtonShirt3.getId()){
                    shirtInt = 2;
                }  else if(checkedId==radioButtonShirt4.getId()){
                    shirtInt = 3;
                }  else if(checkedId==radioButtonHair1.getId()){
                    hairInt = 0;
                }  else if(checkedId==radioButtonHair2.getId()){
                    hairInt = 1;
                }  else if(checkedId==radioButtonHair3.getId()){
                    hairInt = 2;
                }  else if(checkedId==radioButtonHair4.getId()){
                    hairInt = 3;
                }
                setCharacter();
            }
        };
    }

    /**
     * Setting the character from drawable folder.
     * setting it by the views selected.
     */
    private void setCharacter(){
        if(genderInt == 0 && typeInt ==0){
            character = face[faceInt] + "_" + gender[genderInt] + type[typeInt] + "_" + shirt1[shirtInt] + "_" + hair[hairInt];
        } else if(genderInt == 0 && typeInt ==1d){
            character = face[faceInt] + "_" + gender[genderInt] + type[typeInt] + "_" + shirt2[shirtInt] + "_" + hair[hairInt];
        } else if(genderInt == 1 && typeInt ==0){
            character = face[faceInt] + "_" + gender[genderInt] + type[typeInt] + "_" + shirt3[shirtInt] + "_" + hair[hairInt];
        } else if (genderInt == 1 && typeInt ==1){
            character = face[faceInt] + "_" + gender[genderInt] + type[typeInt] + "_" + shirt4[shirtInt] + "_" + hair[hairInt];
        }
        if(character==null) return;

        //Saving character to user  and char variables in bundle.
        settings.edit().putString(getResources().getString(R.string.key_user_character), character).apply();
        int id = getResources().getIdentifier(character, "drawable", getActivity().getPackageName());
        ImageViewAnimatedChange(getActivity(), imageView,
                BitmapFactory.decodeResource(getActivity().getResources(), id));
    }

    /**
     * Creating of view in middle.
     * @return
     */
    @Override
    public View makeView() {
        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setLayoutParams(
                new ImageSwitcher.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }
}
