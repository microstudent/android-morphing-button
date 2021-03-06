package com.dd.morphingbutton.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.impl.CircularProgressButton;
import com.dd.morphingbutton.utils.ProgressGenerator;

public class Sample1Activity extends BaseActivity {

    private int mMorphCounter1 = 1;
    private int mMorphCounter2 = 1;

    public static void startThisActivity(@NonNull Context context) {
        context.startActivity(new Intent(context, Sample1Activity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sample_morph);

        final MorphingButton btnMorph1 = (MorphingButton) findViewById(R.id.btnMorph1);
        btnMorph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton1Clicked(btnMorph1);
            }
        });

        final MorphingButton btnMorph2 = (MorphingButton) findViewById(R.id.btnMorph2);
        btnMorph2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMorphButton2Clicked(btnMorph2);
            }
        });

        final CircularProgressButton button = (CircularProgressButton) findViewById(R.id.btnMorph3);

        button.setState(CircularProgressButton.StateEnum.IDLE, false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (button.getCurrentStateEnum()) {
                    case IDLE:
                        ProgressGenerator generator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
                            @Override
                            public void onComplete() {
                                button.unblockTouch();
                            }
                        });
                        button.blockTouch(); // prevent user from clicking while button is in progress
                        button.setState(CircularProgressButton.StateEnum.PROGRESS, true);
                        generator.start(button);
                        break;
                    case PROGRESS:
                        button.setState(CircularProgressButton.StateEnum.TEXT, true);
                        break;
                    case TEXT:
                        button.setState(CircularProgressButton.StateEnum.IDLE, true);
                    default:
                        break;
                }
            }
        });

        morphToSquare(btnMorph1, 0);
        morphToFailure(btnMorph2, 0);
    }

    private void onMorphButton1Clicked(final MorphingButton btnMorph) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            morphToSquare(btnMorph, integer(R.integer.mb_animation));
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            morphToSuccess(btnMorph);
        }
    }

    private void onMorphButton2Clicked(final MorphingButton btnMorph) {
        if (mMorphCounter2 == 0) {
            mMorphCounter2++;
            morphToFailure(btnMorph,  integer(R.integer.mb_animation));
        } else if (mMorphCounter2 == 1) {
            mMorphCounter2 = 0;
            morphToSquare(btnMorph, integer(R.integer.mb_animation));
        }
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingParams square = MorphingParams.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_200))
                .height(dimen(R.dimen.mb_height_56))
                .solidColor(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
                .text(getString(R.string.mb_button));
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingParams circle = MorphingParams.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .solidColor(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }

    private void morphToFailure(final MorphingButton btnMorph, int duration) {
        MorphingParams circle = MorphingParams.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .solidColor(color(R.color.mb_red))
                .colorPressed(color(R.color.mb_red_dark))
                .icon(R.drawable.ic_lock);
        btnMorph.morph(circle);
    }

}
