package apv.congnt24.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import apv.congnt24.android_utilities.R;

/**
 * Created by congn_000 on 11/5/2015.
 */
public class EqualizerSlider extends LinearLayout{
    LinearLayout layout;
    SeekBar seekBar;
    TextView textView;
    public EqualizerSlider(Context context) {
        super(context);
        initialize(context);
    }

    public EqualizerSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) inflater.inflate(R.layout.equalizer_slider, this, true);//false mean ca't add any in this
        seekBar = (SeekBar) layout.findViewById(R.id.seekbar);
        textView = (TextView) layout.findViewById(R.id.textview);
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public int getProgress() {
        return seekBar.getProgress();
    }

    public void setProgress(int i) {
        seekBar.setProgress(i);
    }

    public void setLabel(String str) {
        this.textView.setText(str);
    }
}
