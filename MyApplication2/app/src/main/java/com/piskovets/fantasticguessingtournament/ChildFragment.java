package com.piskovets.fantasticguessingtournament;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ChildFragment extends Fragment {
    Activity fa;
    View rellayout;
    public static final String EXTRA_IMAGE = "ChildActivity:points";
    private static final String MyPREFERENCES = "MyPrefs" ;
    private TransitionFragment transitionFragment;
    View activity;
    String Points;
    String Theme;
    String image;
    String name;
    int high_score;

    public void setPoints(String points) {
        Points = points;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHigh_score(int high_score) {
        this.high_score = high_score;
    }

    public void setColor(int color) {
        this.color = color;
    }

    int color;


    public ChildFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fa=super.getActivity();
        rellayout= inflater.inflate(R.layout.fragment_child, container, false);
        //setEnterTransition(TransitionUtils.makeEnterTransition());
        //setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition());
        //setEnterSharedElementCallback(new EnterSharedElementCallback(getActivity()));

        Initialize();
        return rellayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Initialize();
    }

    public void Initialize(){

        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        activity=rellayout.findViewById(R.id.transitionElement);

        //final Toolbar toolbar = (Toolbar) findViewById(R.id.category_image_view);
        //toolbar.setBackgroundColor(getIntent().getIntExtra("Color",getResources().getColor(R.color.transition)));
        //toolbar.setBackgroundResource(getResources().getIdentifier("@drawable/" + getIntent().getStringExtra("Image"), "drawable", getApplicationContext().getPackageName()));
        //toolbar.setTitle("");

        //setSupportActionBar(toolbar);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;
        transitionFragment = new TransitionFragment();
        getFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, transitionFragment).commit();
        rellayout.findViewById(R.id.transitionElement3).setVisibility(View.INVISIBLE);

        SharedPreferences sharedpreferences = fa.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        TextView points=(TextView)rellayout.findViewById(R.id.activity_my_points_tv);



        points.setText(Points);
        TextView highscore=(TextView)rellayout.findViewById(R.id.highscore_tv);

        final String theme=Theme;
        final ImageView imageView=(ImageView)rellayout.findViewById(R.id.category_image_view);
        Log.d("Image Tag:", image);
        imageView.setImageResource(getResources().getIdentifier("@drawable/" + image, "drawable", fa.getApplicationContext().getPackageName()));
        imageView.setTag(image);

        if(Integer.valueOf(Points)>high_score){
            SharedPreferences.Editor editor= sharedpreferences.edit();
            editor.putString(theme,String.valueOf(points.getText()));
            editor.apply();
            highscore.setText(getString(R.string.new_high_score));
        }else{
            highscore.setText(getString(R.string.high_score)+String.valueOf(high_score));
        }
        final Button okButton=(Button)rellayout.findViewById(R.id.button2);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(fa, HListViewTest.class);
                startActivity(intent);
                fa.finish();

            }
        });
        Button replayButton=(Button)rellayout.findViewById(R.id.button8);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(fa,ButtonActivity.class);
                intent.putExtra("File", name);
                intent.putExtra("Image", String.valueOf(imageView.getTag()));
                intent.putExtra("Color",color);
                okButton.setVisibility(View.INVISIBLE);
                v.setVisibility(View.INVISIBLE);
                //toolbar.setVisibility(View.INVISIBLE);
                transitionFragment.setColor(color);
                transitionFragment.changeText(name);
                transitionFragment.revealShape(width/2, height/2);
                startActivity(intent);
                fa.finish();
            }
        });
    }


}
