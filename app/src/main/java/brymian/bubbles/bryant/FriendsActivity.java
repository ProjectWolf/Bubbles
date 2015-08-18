package brymian.bubbles.bryant;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import brymian.bubbles.R;
import brymian.bubbles.damian.nonactivity.Post;
import brymian.bubbles.damian.nonactivity.ServerRequest;
import brymian.bubbles.damian.nonactivity.User;
import brymian.bubbles.damian.nonactivity.UserListCallback;


public class FriendsActivity extends ActionBarActivity implements View.OnClickListener{
    Button bSearchFriend, bAddFriend;
    EditText eInputUser;
    TextView tShowFriends0, tShowFriends1, tShowFriends2;
    TextView[] TVIDs = {tShowFriends0, tShowFriends1, tShowFriends2};
    int[] TVRIDs = {R.id.tShowFriends0, R.id.tShowFriends1, R.id.tShowFriends2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        bSearchFriend = (Button) findViewById(R.id.bSearchFriend);
        bAddFriend = (Button) findViewById(R.id.bAddFriend);
        bSearchFriend.setText("Search");
        eInputUser  = (EditText) findViewById(R.id.eInputUser);



        bSearchFriend.setOnClickListener(this);
        bAddFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.bSearchFriend:
                if(eInputUser.getText().toString() != null)
                {
                    new ServerRequest(this).getUsers(eInputUser.getText().toString(), new UserListCallback() {
                        @Override
                        public void done(List<User> userList) {
                            // The userList is a list of User objects, as can be seen
                            // in the User class. Below are just some examples of what
                            // user methods

                            int size = userList.size();
                            for(int i = 0; i < size; i++){

                                TVIDs[i] = (TextView) findViewById(TVRIDs[i]);
                                //TVIDs[i].setText(userList.get(i).namefirst());
                                TVIDs[i].setText(userList.get(i).username());
                                TVIDs[i].isClickable();
                            }
                        }
                    });
                    Toast.makeText(this, "Got it.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Empty String", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bAddFriend:


                break;
        }
    }
    /**
    public void AddFriend(String userSending, String userReceiving){

    }
     **/

}
