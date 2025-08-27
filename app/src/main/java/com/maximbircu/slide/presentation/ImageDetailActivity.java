package com.maximbircu.slide.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.maximbircu.imageloader.api.Slide;
import com.maximbircu.slide.R;
import com.maximbircu.slide.domain.Image;

public class ImageDetailActivity extends AppCompatActivity {
    private static final String EXTRA_IMAGE_ID = "extra_image_id";
    private static final String EXTRA_IMAGE_TITLE = "extra_image_title";
    private static final String EXTRA_IMAGE_URL = "extra_image_url";

    public static Intent createIntent(Context context, Image image) {
        return new Intent(context, ImageDetailActivity.class)
                .putExtra(EXTRA_IMAGE_ID, image.getId())
                .putExtra(EXTRA_IMAGE_TITLE, image.getTitle())
                .putExtra(EXTRA_IMAGE_URL, image.getUrl());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        int imageId = getIntent().getIntExtra(EXTRA_IMAGE_ID, 0);
        String imageTitle = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);

        if (imageTitle == null) imageTitle = "";
        if (imageUrl == null) imageUrl = "";

        Image image = new Image(imageId, imageTitle, imageUrl);
        setupViews(image);
    }

    private void setupViews(Image image) {
        ImageView imageView = findViewById(R.id.detailImageView);
        TextView titleTextView = findViewById(R.id.detailTitleTextView);
        TextView idTextView = findViewById(R.id.detailIdTextView);
        Button invalidateCacheButton = findViewById(R.id.invalidateCacheButton);

        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle("Image Details");

        Slide.load(
                image.getUrl(),
                imageView,
                R.drawable.placeholder_loading,
                R.drawable.placeholder_failure
        );

        titleTextView.setText(image.getTitle());
        idTextView.setText("ID: " + image.getId());

        invalidateCacheButton.setOnClickListener(v -> invalidateCacheAndReload(image));
    }

    private void invalidateCacheAndReload(Image image) {
        Slide.invalidateCache();
        ImageView imageView = findViewById(R.id.detailImageView);
        Slide.load(
                image.getUrl(),
                imageView,
                R.drawable.placeholder_loading,
                R.drawable.placeholder_failure
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
