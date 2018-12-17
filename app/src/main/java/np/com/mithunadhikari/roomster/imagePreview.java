package np.com.mithunadhikari.roomster;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class imagePreview extends Fragment  {
    ImageView imageView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.preview_image, container, false);
        String imageUrl = getArguments().getString("image");
        imageView = (ImageView) view.findViewById(R.id.iv_preview_image);
        Glide.with(getActivity().getBaseContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        return view;
    }
}
