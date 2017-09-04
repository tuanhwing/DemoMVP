package project.vehiclessharing;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Tuan on 30/08/2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser muser;
    private TextView tvFullName;
    private TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//DO NOT ROTATE the screen even if the user is shaking his phone like mad
//        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        muser = FirebaseAuth.getInstance().getCurrentUser();
        tvFullName = (TextView) findViewById(R.id.tv_fullname);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvFullName.setText(muser.getDisplayName());
        tvPhone.setText(muser.getPhoneNumber());


    }

    /**
     * Button back in tollbar
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
