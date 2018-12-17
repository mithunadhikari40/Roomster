package np.com.mithunadhikari.roomster;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(splash.this,signIn_signUp.class);
                splash.this.startActivity(mainIntent);
                splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
