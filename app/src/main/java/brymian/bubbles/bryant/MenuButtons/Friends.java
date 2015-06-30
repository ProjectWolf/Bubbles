package brymian.bubbles.bryant.MenuButtons;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import brymian.bubbles.R;
import brymian.bubbles.bryant.MenuButtons.FriendsButtons.FriendsButtonTest;

public class Friends extends FragmentActivity{

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_friends);
    }

    public void onClickFriendsButtonTest(View view){
        Intent friendsbuttontestIntent = new Intent(this, FriendsButtonTest.class);
        startActivity(friendsbuttontestIntent);
    }
}
