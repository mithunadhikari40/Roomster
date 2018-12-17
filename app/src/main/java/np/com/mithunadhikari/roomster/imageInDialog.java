package np.com.mithunadhikari.roomster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class imageInDialog extends AppCompatActivity {
    Context context;

    public void showPopUp(String image1, String image2, TextView image3, String image4) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Change Security Info");
        final ImageView first_image=new ImageView(context);
        final ImageView second_image=new ImageView(context);
        final ImageView third_image=new ImageView(context);
        final  ImageView fourth_image=new ImageView(context);
        RelativeLayout layout = new RelativeLayout(context);
        try {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 15, 20, 0);
            first_image.setLayoutParams(lp);

            second_image.setLayoutParams(lp);
            second_image.setX(first_image.getWidth());
            third_image.setLayoutParams(lp);
            third_image.setY(first_image.getHeight());
            fourth_image.setLayoutParams(lp);
            fourth_image.setX(first_image.getHeight());
            fourth_image.setY(first_image.getWidth());

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        layout.addView(first_image);
        layout.addView(second_image);
        layout.addView(third_image);
        layout.addView(fourth_image);
        alertDialog.setView(layout);
        Glide.with(context)
                .load(image1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(first_image);
        Glide.with(context)
                .load(image2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(second_image);
        Glide.with(context)
                .load(image3)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(third_image);
        Glide.with(context)
                .load(image4)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(fourth_image);
        alertDialog.setNeutralButton("Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        alertDialog.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        alertDialog.show();
    }
}
