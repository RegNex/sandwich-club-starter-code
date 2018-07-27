package co.etornam.sandwichclub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import co.etornam.sandwichclub.model.Sandwich;
import co.etornam.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity implements Animation.AnimationListener {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final String TAG = "DetailActivity";
    ProgressBar progressBar;
    TextView titleTV, descriptionTV, originTV, ingredientsTV, alsoKnownTV;
    boolean enableBackBtn = false;
    Animation fadeInAnim, slideDownAnim, slideUpAnim, slideUpAnimOne, slideOutDown, slideOutDownOne, slideOutUp, fadeOutAnim;
    LinearLayout descriptionBox, ingredientsBox, alsoKnowAsBox;
    private ImageView imageView;
    private View bottomScrimView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getSupportActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        assert intent != null;
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        initViews();
        populateUI(sandwich);
        imageView.setTransitionName(String.valueOf(position));
        Picasso.get()
                .load(sandwich.getImage())
                .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        progressBar = findViewById(R.id.thumb_progressbar);
        titleTV = findViewById(R.id.title_tv);
        descriptionTV = findViewById(R.id.description_tv);
        originTV = findViewById(R.id.origin_tv);
        ingredientsTV = findViewById(R.id.ingredients_tv);
        alsoKnownTV = findViewById(R.id.also_known_tv);

        imageView = findViewById(R.id.image_iv);
        bottomScrimView = findViewById(R.id.bottom_scrim);
        descriptionBox = findViewById(R.id.description_box_ll);
        ingredientsBox = findViewById(R.id.ingredients_box_ll);
        alsoKnowAsBox = findViewById(R.id.also_known_as_box_ll);

        fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInAnim.setDuration(1000);
        fadeInAnim.setAnimationListener(this);

        slideDownAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideDownAnim.setDuration(1000);
        slideDownAnim.setAnimationListener(this);

        slideUpAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideUpAnim.setDuration(1000);
        slideUpAnim.setAnimationListener(this);

        slideUpAnimOne = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideUpAnimOne.setDuration(500);
        slideUpAnimOne.setAnimationListener(this);

        slideOutDown = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
        slideOutDown.setAnimationListener(this);

        slideOutDownOne = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
        slideOutDownOne.setAnimationListener(this);

        slideOutUp = AnimationUtils.loadAnimation(this, R.anim.slide_out_up);
        slideOutUp.setAnimationListener(this);

        fadeOutAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeOutAnim.setDuration(500);
        fadeOutAnim.setAnimationListener(this);

        bottomScrimView.startAnimation(fadeInAnim);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void populateUI(Sandwich sandwich) {
        String ingredient = TextUtils.join(", ", sandwich.getIngredients());
        String alsoKnownAs = TextUtils.join(", ", sandwich.getAlsoKnownAs());

        titleTV.setText(sandwich.getMainName());
        descriptionTV.setText(sandwich.getDescription());
        ingredientsTV.setText(ingredient);
        if (sandwich.getPlaceOfOrigin().isEmpty() || sandwich.getPlaceOfOrigin().equals(" ")) {
            originTV.setText(getResources().getString(R.string.not_avail));
        } else {
            originTV.setText(sandwich.getPlaceOfOrigin());
        }
        if (sandwich.getIngredients().isEmpty() || sandwich.getIngredients().equals(" ")) {
            alsoKnownTV.setText(getResources().getString(R.string.not_avail));
        } else {
            alsoKnownTV.setText(alsoKnownAs);
        }
    }


    @Override
    public void onAnimationEnd(Animation animation) {

        if (animation == fadeInAnim) {
            descriptionBox.setVisibility(View.VISIBLE);
            descriptionBox.setAnimation(slideDownAnim);
        }
        if (animation == slideDownAnim) {
            ingredientsBox.setVisibility(View.VISIBLE);
            ingredientsBox.setAnimation(slideUpAnim);
        }
        if (animation == slideUpAnim) {
            alsoKnowAsBox.setVisibility(View.VISIBLE);
            alsoKnowAsBox.setAnimation(slideUpAnimOne);
            enableBackBtn = true;
        }
        if (animation == slideOutDown) {
            alsoKnowAsBox.setVisibility(View.INVISIBLE);
            ingredientsBox.setAnimation(slideOutDownOne);
        }
        if (animation == slideOutDownOne) {
            ingredientsBox.setVisibility(View.INVISIBLE);
            descriptionBox.setAnimation(slideOutUp);
        }
        if (animation == slideOutUp) {
            descriptionBox.setVisibility(View.INVISIBLE);
            bottomScrimView.startAnimation(fadeOutAnim);
        }
        if (animation == fadeOutAnim) {
            bottomScrimView.setVisibility(View.INVISIBLE);
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {

        if (enableBackBtn) {
            alsoKnowAsBox.startAnimation(slideOutDown);
            enableBackBtn = false;
        }
    }
}
