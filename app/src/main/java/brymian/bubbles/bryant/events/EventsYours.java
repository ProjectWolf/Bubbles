package brymian.bubbles.bryant.events;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import brymian.bubbles.R;


public class EventsYours extends AppCompatActivity implements View.OnClickListener {
    Toolbar mToolbar;
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.events_yours);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("Your Events");
        mToolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onClick(View v){
        switch (v.getId()){

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
