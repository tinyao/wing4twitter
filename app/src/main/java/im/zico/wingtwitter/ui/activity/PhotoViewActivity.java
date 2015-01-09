package im.zico.wingtwitter.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.security.Key;

import im.zico.wingtwitter.R;
import im.zico.wingtwitter.dao.WingStore;
import im.zico.wingtwitter.type.WingTweet;
import im.zico.wingtwitter.ui.BaseActivity;
import im.zico.wingtwitter.ui.fragment.DMFragment;
import im.zico.wingtwitter.ui.fragment.HomeTimeLineFragment;
import im.zico.wingtwitter.ui.fragment.MentionedFragment;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by tinyao on 1/8/15.
 */
public class PhotoViewActivity extends BaseActivity {

    private ViewPager mViewPager;
//    private static boolean justLaunched = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);

        String[] mediaUrls = null;
        if (getIntent().hasExtra(WingStore.TweetColumns.MEDIAS)) {
//            justLaunched = true;
            mediaUrls = getIntent().getStringArrayExtra(WingStore.TweetColumns.MEDIAS);
            mViewPager = (ViewPager) findViewById(R.id.pager);
            PhotoPagerAdapter mPhotoPagerAdapter = new PhotoPagerAdapter(getFragmentManager());
            mPhotoPagerAdapter.setMediaUrls(mediaUrls);
            mViewPager.setAdapter(mPhotoPagerAdapter);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
        } else {
            finishAfterTransition();
        }
    }

    private class PhotoPagerAdapter extends FragmentPagerAdapter {

        String[] medias;

        public PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setMediaUrls(String[] medias) {
            this.medias = medias;
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoFragment.newInstance(position, medias[position]);
        }

        @Override
        public int getCount() {
            return medias.length;
        }
    }

    static class PhotoFragment extends Fragment {

        public static PhotoFragment newInstance(int pos, String mediaUrl) {
            PhotoFragment fragment = new PhotoFragment();
            Bundle args = new Bundle();
            args.putInt("fragment_id", pos);
            args.putString("mediaUrl", mediaUrl);
            fragment.setArguments(args);
            return fragment;
        }

        private String photoUrl;
        PhotoViewAttacher mAttacher;
        ImageView imgV;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bnd = getArguments();
            if (bnd != null && bnd.containsKey("mediaUrl")) {
                photoUrl = bnd.getString("mediaUrl");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LayoutInflater inflator =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflator.inflate(R.layout.layout_photo, null);
            imgV = (ImageView) rootView.findViewById(R.id.photo_view);
            imgV.setTransitionName(photoUrl);

            if (photoUrl != null) {
                Picasso.with(getActivity()).load(photoUrl).fit().centerInside().into(imgV, new Callback() {

                    @Override
                    public void onSuccess() {
//                        if (justLaunched) {
//                            ScaleAnimation anim = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
//                                    getResources().getDisplayMetrics().widthPixels/2,
//                                    getResources().getDisplayMetrics().heightPixels/2);
//                            anim.setDuration(400);
//                            anim.setStartOffset(50);
//                            imgV.setAnimation(anim);
//                            imgV.animate();
//                            justLaunched = false;
//                        }
                        mAttacher = new PhotoViewAttacher(imgV);
                        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                            @Override
                            public void onViewTap(View view, float v, float v2) {
                                getActivity().finish();
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });

            }

            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finishAfterTransition();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
//    }

}
