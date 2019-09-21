package com.vikpoo.a5cakes;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

public class ImageSliderAdapter extends PagerAdapter {

    public static int IMAGES[] = {R.drawable.india,R.drawable.india,R.drawable.india,R.drawable.india};

    private Context context;
    private String[] CAKE_IMG_URLS;

    public ImageSliderAdapter(Context context, String[] URLS) {
        this.context = context;
        CAKE_IMG_URLS = URLS;
    }

    @Override
    public int getCount() {
        return CAKE_IMG_URLS.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_slide_view,container,false);
        ImageView imageView = view.findViewById(R.id.image);
//        imageView.setImageResource(IMAGES[position]);
        Glide.with(context)
                .load(CAKE_IMG_URLS[position])
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}