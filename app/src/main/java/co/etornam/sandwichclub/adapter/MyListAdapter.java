package co.etornam.sandwichclub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import co.etornam.sandwichclub.R;
import co.etornam.sandwichclub.model.Sandwich;
import co.etornam.sandwichclub.utils.JsonUtils;

public class MyListAdapter extends BaseAdapter {
    private Context context;
    private String[] sandwiches;

    public MyListAdapter(Context context, String[] sandwiches) {
        this.context = context;
        this.sandwiches = sandwiches;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.single_list_item, viewGroup, false);


        ImageView imageView = row.findViewById(R.id.item_image);
        TextView titleTV = row.findViewById(R.id.item_title);
        final ProgressBar progressBar = row.findViewById(R.id.image_progressbar);

        view = imageView;


        String json = sandwiches[i];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);

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

                    }
                });


        view.setTransitionName(sandwich.getMainName());

        titleTV.setText(sandwich.getMainName());

        return row;
    }
}
