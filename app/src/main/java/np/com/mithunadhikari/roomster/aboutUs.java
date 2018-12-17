package np.com.mithunadhikari.roomster;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mithun on 2/28/2017.
 */

public class aboutUs extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_about_us,container,false);
        try {

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("About us");
        }catch (Exception e){
            Log.e("Exception in title",e.getMessage());
        }
        return view;
    }
}
