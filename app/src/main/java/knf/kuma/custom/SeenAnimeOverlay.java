package knf.kuma.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import knf.kuma.R;

public class SeenAnimeOverlay extends LinearLayout {
    public SeenAnimeOverlay(Context context) {
        super(context);
        inflate(context);
    }

    public SeenAnimeOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context);
    }

    public SeenAnimeOverlay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context);
    }

    private void inflate(Context context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_seen_overlay, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setSeen(final boolean seen,boolean animate){
        setState(seen);
        if (animate) {
            post(() -> {
                Animation animation = AnimationUtils.loadAnimation(getContext(), seen ? R.anim.fadein : R.anim.fadeout);
                animation.setDuration(200);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                startAnimation(animation);
            });
        }
    }

    private void setState(final boolean seen){
        post(() -> setVisibility(seen ? VISIBLE : GONE));
    }
}
