package apv.congnt24.customviews;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import apv.congnt24.android_utilities.R;

/**
 * Created by cong on 11/9/2015.
 */
public class CustomFlashCard extends LinearLayout {
    private int frontColor, backColor, frontTextColor, backTextColor;
    private boolean mShowingBack = false;
    private FragmentManager fragmentManager;
    ICustomColorFlashCard iCustomColorFlashCard;
    Context context;
    String text1, text2;
    boolean customColor = false;
    public CustomFlashCard(Context context) {
        super(context);
        this.context = context;
    }

    public CustomFlashCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FlashCardView,
                0, 0);
        try {
            frontColor = a.getColor(R.styleable.FlashCardView_frontColor, Color.WHITE);
            backColor = a.getColor(R.styleable.FlashCardView_backColor, Color.BLUE);
            frontTextColor = a.getColor(R.styleable.FlashCardView_frontTextColor, Color.BLACK);
            backTextColor = a.getColor(R.styleable.FlashCardView_backTextColor, Color.BLACK);
            customColor = true;
        } finally {
            a.recycle();
        }
    }

    public void init(String tv[],ICustomColorFlashCard iCustomColorFlashCard) {
        this.iCustomColorFlashCard = iCustomColorFlashCard;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(10, 10, 10, 10);
        setLayoutParams(layoutParams);
        setMinimumHeight(400);

        text1 = tv[0];
        text2 = tv[1];
        fragmentManager = ((Activity) context).getFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(getId(), new CardFrontFragment(text1))
                .commit();

    }


    class CardFrontFragment extends Fragment {
        String text;
        public CardFrontFragment(String text){
            this.text = text;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_card_front, container, false);
            TextView tv1 = (TextView) v.findViewById(R.id.tv1);
            tv1.setText(text);
            tv1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipCard();
                }
            });
            iCustomColorFlashCard.changeFrontColor(v);
            iCustomColorFlashCard.changeFrontTextColor(tv1);
            return v;
        }
    }
    class CardBackFragment  extends Fragment {
        String text;
        public CardBackFragment(String text){
            this.text = text;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_card_back, container, false);
            TextView tv2 = (TextView) v.findViewById(R.id.tv2);
            tv2.setText(text);
            tv2.findViewById(R.id.tv2).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipCard();
                }
            });
            iCustomColorFlashCard.changeBackColor(v);
            iCustomColorFlashCard.changeBackTextColor(tv2);
            return v;
        }
    }
    private void flipCard() {
        if (mShowingBack) {
//            fragmentManager.popBackStack();

            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.card_flip_left_in, R.anim.card_flip_left_out,
                            R.anim.card_flip_left_in, R.anim.card_flip_left_out)
                    .replace(getId(), new CardFrontFragment(text1))
                    .addToBackStack(null)
                    .commit();
            mShowingBack = false;
            return;
        }
        mShowingBack = true;
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.card_flip_left_in, R.anim.card_flip_left_out,
                        R.anim.card_flip_left_in, R.anim.card_flip_left_out)
                .replace(getId(), new CardBackFragment(text2))
                .addToBackStack(null)
                .commit();
    }
}
