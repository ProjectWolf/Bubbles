package com.brymian.boppo.bryant.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

import com.brymian.boppo.R;


public class ProfileName extends FragmentActivity implements View.OnClickListener {
    ImageButton ibMenu;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_name);

        ibMenu = (ImageButton) findViewById(R.id.ibMenu);

        ibMenu.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibMenu:

                break;
        }
    }
}
