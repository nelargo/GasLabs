package com.programmers.wine.gaslabs.util;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.programmers.wine.gaslabs.R;

public class BaseFragment extends Fragment {
    protected MaterialDialog dialog;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);

        //If not, and an animation is defined, load it now
        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }

        //If there is an animation for this fragment, add a listener.
        if (animation != null) {
            animation.setAnimationListener(
                    new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            onAnimationStarted();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            onAnimationEnded();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            onAnimationRepeated();
                        }
                    }
            );
        }

        return animation;
    }

    // Following methods need to be override

    protected void onAnimationStarted() {
    }

    protected void onAnimationEnded() {
    }

    protected void onAnimationRepeated() {
    }

    /*
    * Feedback methods.
    */
    protected MaterialDialog createDialog() {
        return new MaterialDialog.Builder(getActivity())
                .content(R.string.loading)
                .contentColor(ContextCompat.getColor(getActivity(), android.R.color.black))
                .progress(true, 0)
                .cancelable(false)
                .backgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white))
                .widgetColor(ContextCompat.getColor(getActivity(), R.color.feedback_loading_progress))
                .build();
    }

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected void showDialog() {
        if (dialog != null) {
            dialog.show();
        } else {
            dialog = createDialog();
            dialog.show();
        }
    }
}
