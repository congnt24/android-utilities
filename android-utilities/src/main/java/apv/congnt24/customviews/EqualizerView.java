package apv.congnt24.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import apv.congnt24.android_utilities.R;

/**
 * Created by congn_000 on 11/5/2015.
 */
public class EqualizerView extends LinearLayout {
    private int count = 5;
    private Context context;
    private LayoutInflater layoutInflater;
    private LinearLayout layout;
    private EqualizerSlider[] sliders;
    private LayoutParams layout_weight = new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT, 1.0f);
    private String[] labels = new String[]{"Bass", "Low", "Mid", "Upper", "High", "Higher"};
    private Equalizer equalizer;
    private BassBoost bassBoost;
    private List<String> equalizerPresetNames = new ArrayList<>();



    public EqualizerView(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public EqualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EqualizerView,
                0, 0);
        try {
            count = a.getInteger(R.styleable.EqualizerView_count, 5);
        } finally {
            a.recycle();
        }
        initialize();
    }
    private void initialize() {
        equalizer = new Equalizer(0, 0);
        bassBoost = new BassBoost(0, 0);
        equalizer.setEnabled(true);
        bassBoost.setEnabled(true);
        //        get number frequency bands supported by the equalizer_view engine
        short numberFrequencyBands = equalizer.getNumberOfBands(); //5
        for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
            equalizerPresetNames.add(equalizer.getPresetName(i));
            /*11-05 02:11:30.902 5158-5158/? D/PRESET NAME:: PRESET NAME: 0: Normal
            11-05 02:11:30.902 5158-5158/? D/PRESET NAME:: PRESET NAME: 1: Classical
            11-05 02:11:30.902 5158-5158/? D/PRESET NAME:: PRESET NAME: 2: Dance
            11-05 02:11:30.902 5158-5158/? D/PRESET NAME:: PRESET NAME: 3: Flat
            11-05 02:11:30.902 5158-5158/? D/PRESET NAME:: PRESET NAME: 4: Folk
            11-05 02:11:30.902 5158-5158/? D/PRESET NAME:: PRESET NAME: 5: Heavy Metal
            11-05 02:11:30.902 5158-5158/? D/PRESET NAME:: PRESET NAME: 6: Hip Hop
            11-05 02:11:30.910 5158-5158/? D/PRESET NAME:: PRESET NAME: 7: Jazz
            11-05 02:11:30.926 5158-5158/? D/PRESET NAME:: PRESET NAME: 8: Pop
            11-05 02:11:30.926 5158-5158/? D/PRESET NAME:: PRESET NAME: 9: Rock*/
        }
        //        get the level ranges to be used in setting the band level
//        get lower limit of the range in milliBels
        final short lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0]; //-1500
//        get the upper limit of the range in millibels
        final short upperEqualizerBandLevel = equalizer.getBandLevelRange()[1]; //1500
        Log.d("BAND LEVEL: ", "BAND LEVEL: "+lowerEqualizerBandLevel+" - "+upperEqualizerBandLevel +" - "+numberFrequencyBands);
        for (short i = 0; i < numberFrequencyBands; i++) {
            Log.d("TAG", "FREQUENCY BAND LEVEL "+i+" - " + equalizer.getCenterFreq(i));
            /*11-05 02:24:04.654 5393-5393/? D/TAG: FREQUENCY BAND LEVEL 0 - 60000
            11-05 02:24:04.654 5393-5393/? D/TAG: FREQUENCY BAND LEVEL 1 - 230000
            11-05 02:24:04.654 5393-5393/? D/TAG: FREQUENCY BAND LEVEL 2 - 910000
            11-05 02:24:04.654 5393-5393/? D/TAG: FREQUENCY BAND LEVEL 3 - 3600000
            11-05 02:24:04.658 5393-5393/? D/TAG: FREQUENCY BAND LEVEL 4 - 14000000*/
        }




        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        layout = (LinearLayout) layoutInflater.inflate(R.layout.equalizer_view, this, true).findViewById(R.id.group_layout);
        layout.setWeightSum(count);
        sliders = new EqualizerSlider[count];
        //BASS
        sliders[0] = new EqualizerSlider(context);
        sliders[0].setLayoutParams(layout_weight);
        sliders[0].setLabel(labels[0]);
        sliders[0].getSeekBar().setMax(1000);
        if (bassBoost.getStrengthSupported())
        {
            short strength = bassBoost.getRoundedStrength();
            bassBoost.setStrength(strength);
            sliders[0].getSeekBar().setProgress(strength);
            sliders[0].getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    bassBoost.setStrength((short) progress);
                    Log.d("BASS", "BASS BAND::::"+ (short) progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        layout.addView(sliders[0]);
        //BAND
        for (int i = 1; i < count; i++) {
            final short equalizerBandIndex = (short)(i-1);
            sliders[i] = new EqualizerSlider(context);
            sliders[i].setLayoutParams(layout_weight);
            sliders[i].setLabel(labels[i]);
            sliders[i].getSeekBar().setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
            sliders[i].getSeekBar().setProgress(equalizer.getBandLevel(equalizerBandIndex));
            sliders[i].getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    equalizer.setBandLevel(equalizerBandIndex ,
                            (short) (progress + lowerEqualizerBandLevel));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            layout.addView(sliders[i]);
        }
    }

    public void usePreset(short position){
        equalizer.usePreset(position);
//                get the number of frequency bands for this equalizer_view engine
        short numberFrequencyBands = equalizer.getNumberOfBands();
//                get the lower gain setting for this equalizer_view band
        final short lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];

//                set seekBar indicators according to selected preset
        for (short i = 1; i < count; i++) {
            short equalizerBandIndex = (short) (i-1);
            sliders[i].setProgress(equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
        }
    }
    public int[] getPresetState(){
        int[] progress = new int[count];
        for (short i = 0; i < count; i++) {
            progress[i] = sliders[i].getProgress();
        }
        return progress;
    }
    public short getPresetIndex(){
        return equalizer.getCurrentPreset();
    }
}
