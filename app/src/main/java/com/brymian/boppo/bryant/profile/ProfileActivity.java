package com.brymian.boppo.bryant.profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.brymian.boppo.R;
import com.brymian.boppo.bryant.episodes.EpisodeMy2;
import com.brymian.boppo.bryant.episodes.EpisodeMyRecyclerAdapter;
import com.brymian.boppo.bryant.friends.Friends;
import com.brymian.boppo.bryant.friends.FriendsRecyclerAdapter;
import com.brymian.boppo.bryant.nonactivity.SaveSharedPreference;
import com.brymian.boppo.bryant.photos.PhotosRecyclerAdapter;
import com.brymian.boppo.damian.nonactivity.Connection.HTTPConnection;
import com.brymian.boppo.damian.nonactivity.ServerRequest.Callback.StringCallback;
import com.brymian.boppo.damian.nonactivity.ServerRequest.EventRequest;
import com.brymian.boppo.damian.nonactivity.ServerRequest.FriendshipStatusRequest;
import com.brymian.boppo.damian.nonactivity.ServerRequest.UserRequest;
import com.brymian.boppo.damian.nonactivity.ServerRequestMethods;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView ivProfilePicture;
    FloatingActionButton fabAddFriend;
    TextView tvProfileFirstLastName, tvFriendsNum, tvFriendStatus, tvPhotosNum, tvEpisodesNum, tvSeeAllFriends, tvSeeAllEpisodes;
    int userUID;
    String privacy, friendShipStatus;
    Toolbar mToolbar;
    LinearLayout llAcceptDeclineFriendRequest;
    Button bAccept, bDecline;

    RecyclerView rvFriends, rvEpisodes, rvPhotos;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //mToolbar.setPadding(0, getStatusBarHeight(),0, 0);
        }

        /*--------------------------------Checking for putExtras()--------------------------------*/
        int uid;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                uid = 0;
            }
            else {
                uid = extras.getInt("uid");
            }
        }
        else {
            uid = savedInstanceState.getInt("uid");
        }
        /*----------------------------------------------------------------------------------------*/
        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        fabAddFriend = (FloatingActionButton) findViewById(R.id.fabAddFriend);
        fabAddFriend.setOnClickListener(this);
        tvFriendStatus = (TextView) findViewById(R.id.tvFriendStatus);
        tvFriendStatus.setOnClickListener(this);
        tvProfileFirstLastName = (TextView) findViewById(R.id.tvUserFirstLastName);
        llAcceptDeclineFriendRequest = (LinearLayout) findViewById(R.id.llAcceptDeclineFriendRequest);
        bAccept = (Button) findViewById(R.id.bAccept);
        bAccept.setOnClickListener(this);
        bDecline = (Button) findViewById(R.id.bDecline);
        bDecline.setOnClickListener(this);
        tvFriendsNum = (TextView) findViewById(R.id.tvFriendsNum);
        tvEpisodesNum = (TextView) findViewById(R.id.tvEpisodesNum);
        tvPhotosNum = (TextView) findViewById(R.id.tvPhotosNum);
        tvSeeAllEpisodes = (TextView) findViewById(R.id.tvSeeAllEpisodes);
        tvSeeAllEpisodes.setOnClickListener(this);
        tvSeeAllFriends = (TextView) findViewById(R.id.tvSeeAllFriends);
        tvSeeAllFriends.setOnClickListener(this);

        rvFriends = (RecyclerView) findViewById(R.id.rvFriends);
        rvEpisodes = (RecyclerView) findViewById(R.id.rvEpisodes);
        rvPhotos = (RecyclerView) findViewById(R.id.rvPhotos);


        if(uid != 0){
            userUID = uid;
            setFriendshipStatus(uid);
            getProfileInfo(uid);
        }
    }

    // A method to find height of the status bar
    /*
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
            Log.e("statusBarHght", result + "");
        }
        return result;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabAddFriend:
                sendFriendRequest();
                break;
            case R.id.tvFriendStatus:
                switch (getFriendShipStatus()){
                    case "logged in user":
                        startActivity(new Intent(this, ProfileEdit.class));
                        break;
                    case "Already friends with user.":
                        popUpMenu();
                        break;

                    case "Already sent friend request to user.":
                        /* friend request sent */
                        cancelFriendRequest();
                        break;
                }
                break;

            case R.id.bAccept:
                acceptFriendRequest();
                break;

            case R.id.bDecline:
                declineFriendRequest();
                break;

            case R.id.tvRemoveFriend:
                new FriendshipStatusRequest().unFriend(SaveSharedPreference.getUserUID(this), userUID, new StringCallback() {
                    @Override
                    public void done(String string) {
                        Toast.makeText(ProfileActivity.this, string, Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.tvBlockUser:
                new FriendshipStatusRequest().blockUser(SaveSharedPreference.getUserUID(this), userUID, new StringCallback() {
                    @Override
                    public void done(String string) {
                        Toast.makeText(ProfileActivity.this, string, Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.tvSeeAllEpisodes:
                EpisodeMy2 episodeMy2 = new EpisodeMy2();
                Bundle b = new Bundle();
                b.putInt("uid", userUID);
                b.putString("from", "profile");
                episodeMy2.setArguments(b);
                startFragment(episodeMy2);
                break;

            case R.id.tvSeeAllFriends:
                Friends friends = new Friends();
                Bundle bundle = new Bundle();
                bundle.putInt("uid", userUID);
                bundle.putString("from", "profile");
                friends.setArguments(bundle);
                startFragment(friends);
                break;
            case R.id.tvReportUser:
                Toast.makeText(this, "Report under construction", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getProfileInfo(int uid){
        new UserRequest(this).getUserProfileData(uid, new StringCallback() {
            @Override
            public void done(String string) {
                try{
                    JSONObject jsonObject = new JSONObject(string);
                    String user = jsonObject.getString("user");
                    JSONObject userObj = new JSONObject(user);
                    mToolbar.setTitle(userObj.getString("username"));
                    tvProfileFirstLastName.setText(userObj.getString("firstName") + " " + userObj.getString("lastName"));
                    tvFriendsNum.setText(userObj.getString("countFriends"));
                    tvEpisodesNum.setText(userObj.getString("countJoinedEvents"));
                    tvPhotosNum.setText(userObj.getString("countImages"));

                    String userProfileImages = userObj.getString("userProfileImages");
                    JSONArray userProfileImagesArray = new JSONArray(userProfileImages);
                    String imagePath;
                    if (userProfileImagesArray.length() > 0){
                        JSONObject userProfileImagesObj = userProfileImagesArray.getJSONObject(0);
                        imagePath = userProfileImagesObj.getString("userImagePath");
                    }
                    else {
                        imagePath = "empty";
                    }
                    Picasso.with(ProfileActivity.this).load(imagePath).fit().centerCrop().into(ivProfilePicture);

                    String topNRandomEpisodes = userObj.getString("topNRandomEvents");
                    JSONObject topNRandomEpisodesObj = new JSONObject(topNRandomEpisodes);
                    String episodes = topNRandomEpisodesObj.getString("events");
                    JSONArray episodesArray = new JSONArray(episodes);
                    if (episodesArray.length() > 0){
                        List<Integer> episodeEid = new ArrayList<>();
                        List<String> episodeName = new ArrayList<>();
                        List<String> episodeType = new ArrayList<>();
                        List<String> episodeImagePath = new ArrayList<>();
                        for (int i = 0; i < episodesArray.length(); i++){
                            JSONObject episodesObj = episodesArray.getJSONObject(i);
                            String eid = episodesObj.getString("eid");
                            String title = episodesObj.getString("eventName");

                            String episodeHost = episodesObj.getString("eventHost");
                            JSONObject episodeHostObj = new JSONObject(episodeHost);
                            String username = episodeHostObj.getString("username");

                            String episodeProfileImages = episodesObj.getString("eventProfileImages");
                            JSONArray episodeProfileImagesArray = new JSONArray(episodeProfileImages);
                            String eImagePath;
                            if (episodeProfileImagesArray.length() > 0){
                                JSONObject episodeImagePathObj = episodeProfileImagesArray.getJSONObject(0);
                                eImagePath = episodeImagePathObj.getString("euiThumbnailPath");
                            }
                            else {
                                eImagePath = "empty";
                            }

                            episodeEid.add(Integer.valueOf(eid));
                            episodeName.add(title);
                            episodeType.add("Social");
                            episodeImagePath.add(eImagePath);
                        }

                        adapter = new EpisodeMyRecyclerAdapter(ProfileActivity.this, "horizontal", episodeName, episodeImagePath, episodeEid, episodeType);
                        layoutManager = new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        rvEpisodes.setLayoutManager(layoutManager);
                        rvEpisodes.setNestedScrollingEnabled(false);
                        rvEpisodes.setAdapter(adapter);
                    }

                    String topNRandomFriends = userObj.getString("topNRandomFriends");
                    JSONObject topNRandomFriendsObj = new JSONObject(topNRandomFriends);
                    String friends = topNRandomFriendsObj.getString("friends");
                    JSONArray friendsArray = new JSONArray(friends);
                    if (friendsArray.length() > 0){
                        List<Integer> friendsUid = new ArrayList<>();
                        List<String> friendsFullName = new ArrayList<>();
                        List<String> friendsUsername = new ArrayList<>();
                        List<String> friendsUserImagePath = new ArrayList<>();
                        for (int i = 0; i < friendsArray.length(); i++){
                            JSONObject friendsObj = friendsArray.getJSONObject(i);
                            String uid = friendsObj.getString("uid");
                            String username = friendsObj.getString("username");
                            String fullName = friendsObj.getString("firstName") + " " + friendsObj.getString("lastName");

                            String friendsUserProfileImages = friendsObj.getString("userProfileImages");
                            JSONArray friendsUserProfileImagesArray = new JSONArray(friendsUserProfileImages);
                            String fImagePath;
                            if (friendsUserProfileImagesArray.length() > 0){
                                JSONObject fImageObj = friendsUserProfileImagesArray.getJSONObject(0);
                                fImagePath = fImageObj.getString("userImageThumbnailPath");
                            }
                            else {
                                fImagePath = "empty";
                            }

                            friendsUid.add(Integer.valueOf(uid));
                            friendsFullName.add(fullName);
                            friendsUsername.add(username);
                            friendsUserImagePath.add(fImagePath);
                        }

                        adapter = new FriendsRecyclerAdapter(ProfileActivity.this, "horizontal", friendsFullName, friendsUsername, friendsUid, friendsUserImagePath);
                        layoutManager = new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        rvFriends.setLayoutManager(layoutManager);
                        rvFriends.setNestedScrollingEnabled(false);
                        rvFriends.setAdapter(adapter);
                    }

                    String latestNImages = userObj.getString("latestNImages");
                    JSONObject latestNImagesObj = new JSONObject(latestNImages);
                    String images = latestNImagesObj.getString("images");
                    JSONArray imagesArray = new JSONArray(images);
                    if (imagesArray.length() > 0){
                        List<String> imagesPath = new ArrayList<>();
                        for (int i = 0;i < imagesArray.length(); i++){
                            JSONObject imagesObj = imagesArray.getJSONObject(i);
                            String imagesImagePath = imagesObj.getString("userImageThumbnailPath");
                            imagesPath.add(imagesImagePath);
                        }

                        adapter = new PhotosRecyclerAdapter(ProfileActivity.this, "horizontal", imagesPath);
                        layoutManager = new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        rvPhotos.setLayoutManager(layoutManager);
                        rvPhotos.setNestedScrollingEnabled(false);
                        rvPhotos.setAdapter(adapter);
                    }


                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void startFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.profile_activity, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void popUpMenu(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.profile_activity_menu_alertdialog, null);

        TextView tvRemoveFriend = (TextView) alertLayout.findViewById(R.id.tvRemoveFriend);
        tvRemoveFriend.setOnClickListener(this);
        TextView tvBlockUser = (TextView) alertLayout.findViewById(R.id.tvBlockUser);
        tvBlockUser.setOnClickListener(this);
        TextView tvReportUser = (TextView) alertLayout.findViewById(R.id.tvReportUser);
        tvReportUser.setOnClickListener(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setFriendshipStatus(int uid){
        if (uid == SaveSharedPreference.getUserUID(this)){
            setUserProfileDisplay("logged in user");
            this.friendShipStatus = "logged in user";
        }
        else{
            new ServerRequestMethods(this).getFriendStatus(SaveSharedPreference.getUserUID(ProfileActivity.this), uid, new StringCallback() {
                @Override
                public void done(String string) {
                    setUserProfileDisplay(string);
                    ProfileActivity.this.friendShipStatus = string;
                }
            });
        }
    }

    private void setUserProfileDisplay(String profile){
        switch (profile){
            case "logged in user":
                fabAddFriend.hide();
                tvFriendStatus.setText(R.string.Edit_profile);
                break;
            case "Already friends with user.":
                fabAddFriend.setVisibility(View.GONE);
                tvFriendStatus.setText(R.string.Friends);
                break;
            case "Already sent friend request to user.":
                /* friend request sent */
                fabAddFriend.setVisibility(View.GONE);
                tvFriendStatus.setText("CANCEL FRIEND REQUEST");
                break;
            case "User is awaiting confirmation for friend request.":
                /* friends request received */
                fabAddFriend.setVisibility(View.GONE);
                tvFriendStatus.setText("Friend request received");
                llAcceptDeclineFriendRequest.setVisibility(View.VISIBLE);
                break;
            case "Not friends.":

                break;
            case "User is currently being blocked.":
                /* hide everything, show user doesnt exist like facebook? */
                fabAddFriend.hide();
                break;
            case "Currently being blocked by user.":
                /* hide everything, show user doesnt exist like facebook? */
                fabAddFriend.hide();
                break;
            default:
        }
    }

    private void getEpisodes(int uid){
        new EventRequest(this).getEventDataByMember(uid, new StringCallback() {
            @Override
            public void done(String string) {
                if (string.equals("\"No such event exists.\"")){
                    tvEpisodesNum.setText("0");
                }
                else {
                    try{
                        JSONObject jsonObject = new JSONObject(string);
                        String episodesString = jsonObject.getString("events");
                        JSONArray episodeArray = new JSONArray(episodesString);
                        tvEpisodesNum.setText(String.valueOf(episodeArray.length()));
                        if (episodeArray.length() > 0){
                            List<Integer> episodeEid = new ArrayList<>();
                            List<String> episodeName = new ArrayList<>();
                            List<String> episodeType = new ArrayList<>();
                            List<String> episodeImagePath = new ArrayList<>();
                            for (int i = 0; i < episodeArray.length(); i++){
                                JSONObject episodeObj = episodeArray.getJSONObject(i);
                                String eid = episodeObj.getString("eid");
                                String name = episodeObj.getString("eventName");
                                String type = episodeObj.getString("eventTypeLabel");

                                String episodeProfileImagesString = episodeObj.getString("eventProfileImages");
                                JSONArray episodeProfileImagesArray = new JSONArray(episodeProfileImagesString);
                                String imagePath;
                                if (episodeProfileImagesArray.length() > 0){
                                    HTTPConnection httpConnection = new HTTPConnection();
                                    String path = httpConnection.getUploadServerString();
                                    JSONObject episodeProfileImagesObj = episodeProfileImagesArray.getJSONObject(0);
                                    imagePath = path + episodeProfileImagesObj.getString("euiThumbnailPath");
                                }
                                else {
                                    imagePath = "empty";
                                }
                                episodeEid.add(Integer.valueOf(eid));
                                episodeName.add(name);
                                episodeType.add(type);
                                episodeImagePath.add(imagePath);
                            }
                            adapter = new EpisodeMyRecyclerAdapter(ProfileActivity.this, "horizontal", episodeName, episodeImagePath, episodeEid, episodeType);
                            layoutManager = new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            rvEpisodes.setLayoutManager(layoutManager);
                            rvEpisodes.setNestedScrollingEnabled(false);
                            rvEpisodes.setAdapter(adapter);
                        }
                        else {
                            tvEpisodesNum.setText("0");
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendFriendRequest(){
        new ServerRequestMethods(this).setFriendStatus(SaveSharedPreference.getUserUID(ProfileActivity.this), userUID,
                new StringCallback() {
                    public void done(String string) {
                        Log.e("sendFriendRequest", string);
                        if (string.equals("Friendship Pending request sent successfully.")){
                            fabAddFriend.setVisibility(View.GONE);
                            ProfileActivity.this.friendShipStatus = "Already sent friend request to user.";
                            tvFriendStatus.setText("cancel friend request");
                        }
                    }
                }
        );
    }

    private void cancelFriendRequest(){
        new FriendshipStatusRequest().cancelFriend(SaveSharedPreference.getUserUID(ProfileActivity.this), userUID, new StringCallback() {
            @Override
            public void done(String string) {
                if (string.equals("Friendship request has been successfully canceled.")){
                    Log.e("getPrivacy", getPrivacy());
                    if (getPrivacy().equals("Public")){
                        fabAddFriend.setVisibility(View.VISIBLE);
                        tvFriendStatus.setText("");
                    }
                }
            }
        });
    }

    private void acceptFriendRequest(){
        new ServerRequestMethods(this).setFriendStatus(SaveSharedPreference.getUserUID(ProfileActivity.this), userUID, new StringCallback() {
            @Override
            public void done(String string) {
                if (string.equals("Friend request accepted.")){
                    llAcceptDeclineFriendRequest.setVisibility(View.GONE);
                    tvFriendStatus.setText("Friends");
                    ProfileActivity.this.friendShipStatus = "Already friends with user.";
                }

            }
        });
    }

    private void declineFriendRequest(){
        new FriendshipStatusRequest().rejectFriend(SaveSharedPreference.getUserUID(ProfileActivity.this), userUID, new StringCallback() {
            @Override
            public void done(String string) {
                if (string.equals("Friendship request has been successfully rejected.")){
                    llAcceptDeclineFriendRequest.setVisibility(View.GONE);
                    tvFriendStatus.setText("");
                    fabAddFriend.setVisibility(View.VISIBLE);
                    ProfileActivity.this.friendShipStatus = "Not friends.";
                }
            }
        });
    }

    private void setPrivacy(String privacy){
        this.privacy = privacy;
    }

    private String getPrivacy(){
        return privacy;
    }

    private String getFriendShipStatus(){
        return friendShipStatus;
    }

}
