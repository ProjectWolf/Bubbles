package brymian.bubbles.bryant.profile;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import brymian.bubbles.R;
import brymian.bubbles.bryant.nonactivity.SaveSharedPreference;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.StringCallback;
import brymian.bubbles.damian.nonactivity.ServerRequestMethods;

/**
 * Created by Almanza on 9/21/2015.
 */
public class Privacy extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    Switch sPictures, sAccount;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_privacy);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.Privacy);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sPictures = (Switch) findViewById(R.id.sPictures);
        sAccount = (Switch) findViewById(R.id.sAccount);

        if(SaveSharedPreference.getUserAccountPrivacy(this).length() !=0){
            sAccount.setChecked(true);
        }
        if(SaveSharedPreference.getUserPicturePrivacy(this).length() != 0){
            sPictures.setChecked(true);
        }
        sAccount.setOnCheckedChangeListener(this);
        sPictures.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b){
        switch (compoundButton.getId()){
            case R.id.sAccount:
                if(b){
                    if(SaveSharedPreference.getUserAccountPrivacy(this).length() == 0){
                        SaveSharedPreference.setUserAccountPrivacy(this);
                        new ServerRequestMethods(this)
                                .setUserAccountPrivacy(
                                        SaveSharedPreference.getUserUID(this),  /* user UID */
                                        "Private",                              /* Public/Private */
                                        new StringCallback() {
                            @Override
                            public void done(String string) {
                                if(string.equals("User updated successfully.")){
                                    Toast.makeText(Privacy.this, "Pictures switched to private", Toast.LENGTH_SHORT).show();
                                }                            }
                        });
                    }
                }
                else{
                    if(SaveSharedPreference.getUserAccountPrivacy(this).length() != 0){
                        SaveSharedPreference.clearUserAccountPrivacy(this);
                        new ServerRequestMethods(this)
                                .setUserAccountPrivacy(
                                        SaveSharedPreference.getUserUID(this),
                                        "Public",
                                        new StringCallback() {
                            @Override
                            public void done(String string) {
                                if(string.equals("User updated successfully.")){
                                    Toast.makeText(Privacy.this, "Pictures switched to public", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                break;

            case R.id.sPictures:
                if(b){
                    if(SaveSharedPreference.getUserPicturePrivacy(this).length() == 0){
                        SaveSharedPreference.setUserPicturePrivacy(this);
                    }
                }
                else{
                    if(SaveSharedPreference.getUserPicturePrivacy(this).length() != 0){
                        SaveSharedPreference.clearUserPicturePrivacy(this);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}