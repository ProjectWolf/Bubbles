package brymian.bubbles.bryant.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import brymian.bubbles.R;
import brymian.bubbles.bryant.account.Email;
import brymian.bubbles.bryant.account.Password;
import brymian.bubbles.bryant.account.PhoneNumber;
import brymian.bubbles.bryant.episodes.EpisodeMy2;
import brymian.bubbles.bryant.friends.Friends;
import brymian.bubbles.bryant.logIn.LoginActivity;
import brymian.bubbles.bryant.map.MapActivity;
import brymian.bubbles.bryant.nonactivity.SaveSharedPreference;
import brymian.bubbles.bryant.pictures.ProfilePicturesActivity2;
import brymian.bubbles.bryant.profile.ProfileActivity;
import brymian.bubbles.bryant.settings.Notifications;
import brymian.bubbles.bryant.settings.Privacy;
import brymian.bubbles.bryant.settings.blocking.Blocking;
import brymian.bubbles.damian.activity.TESTActivity;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.StringCallback;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.UserCallback;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.UserListCallback;
import brymian.bubbles.damian.nonactivity.ServerRequest.EventRequest;
import brymian.bubbles.damian.nonactivity.ServerRequest.UserImageRequest;
import brymian.bubbles.damian.nonactivity.ServerRequestMethods;
import brymian.bubbles.objects.User;

import static com.facebook.FacebookSdk.getApplicationContext;


public class MainTabPersonal extends Fragment implements View.OnClickListener{
    CardView  cvMyProfile, cvMyEpisodes, cvMyMap, cvProfilePictures, cvFriends,
                            cvNotifications, cvPrivacy, cvBlocking, cvAbout,
                            cvPassword, cvEmail, cvPhoneNumber, cvSyncWithOtherMedia, cvLogOut;
    TextView tvUserUsername, tvUserFirstLastName, tvNumberOfEpisodes, tvNumberOfFriends, tvEmail, tvPhoneNumber;
    ImageView ivProfilePicture;
    boolean isPersonalLoaded = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_tab_personal, container, false);
        /* Profile */
        cvMyProfile = (CardView) rootView.findViewById(R.id.cvMyProfile);
        cvMyProfile.setOnClickListener(this);
        ivProfilePicture = (ImageView) rootView.findViewById(R.id.ivProfilePicture);
        tvUserUsername = (TextView) rootView.findViewById(R.id.tvUserUsername);
        tvUserFirstLastName = (TextView) rootView.findViewById(R.id.tvUserFirstLastName);

        cvMyEpisodes = (CardView) rootView.findViewById(R.id.cvMyEpisodes);
        cvMyEpisodes.setOnClickListener(this);
        tvNumberOfEpisodes = (TextView) rootView.findViewById(R.id.tvNumberOfEpisodes);

        cvMyMap = (CardView) rootView.findViewById(R.id.cvMyMap);
        cvMyMap.setOnClickListener(this);

        cvProfilePictures = (CardView) rootView.findViewById(R.id.cvProfilePictures);
        cvProfilePictures.setOnClickListener(this);

        cvFriends = (CardView) rootView.findViewById(R.id.cvFriends);
        cvFriends.setOnClickListener(this);
        tvNumberOfFriends = (TextView) rootView.findViewById(R.id.tvNumberOfFriends);

        /* Settings */
        cvNotifications = (CardView) rootView.findViewById(R.id.cvNotifications);
        cvNotifications.setOnClickListener(this);

        cvPrivacy = (CardView) rootView.findViewById(R.id.cvPrivacy);
        cvPrivacy.setOnClickListener(this);

        cvBlocking = (CardView) rootView.findViewById(R.id.cvBlocking);
        cvBlocking.setOnClickListener(this);

        cvAbout = (CardView) rootView.findViewById(R.id.cvAbout);
        cvAbout.setOnClickListener(this);

        /* Account */
        cvPassword = (CardView) rootView.findViewById(R.id.cvPassword);
        cvPassword.setOnClickListener(this);

        cvEmail = (CardView) rootView.findViewById(R.id.cvEmail);
        cvEmail.setOnClickListener(this);
        tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);

        cvPhoneNumber = (CardView) rootView.findViewById(R.id.cvPhoneNumber);
        cvPhoneNumber.setOnClickListener(this);
        tvPhoneNumber = (TextView) rootView.findViewById(R.id.tvPhoneNumber);

        cvSyncWithOtherMedia = (CardView) rootView.findViewById(R.id.cvSyncWithOtherMedia);

        final TextView tvDamiansTestActivity = (TextView) rootView.findViewById(R.id.tvDamiansTestActivity);
        if (SaveSharedPreference.getUserUID(getActivity()) == 1){
            tvDamiansTestActivity.setVisibility(View.VISIBLE);
        }
        tvDamiansTestActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TESTActivity.class));
            }
        });

        cvLogOut = (CardView) rootView.findViewById(R.id.cvLogOut);
        cvLogOut.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isPersonalLoaded){
            getPersonalInfo();
            isPersonalLoaded = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /* Profile */
            case R.id.cvMyProfile:
                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View statusBar = getActivity().findViewById(android.R.id.statusBarBackground);
                    View navigationBar = getActivity().findViewById(android.R.id.navigationBarBackground);
                    View image = getActivity().findViewById(R.id.ivProfilePicture);

                    List<Pair<View, String>> pairs = new ArrayList<>();
                    pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
                    pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
                    pairs.add(Pair.create(image, image.getTransitionName()));

                    Bundle options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs.toArray(new Pair[pairs.size()])).toBundle();
                    startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("uid", SaveSharedPreference.getUserUID(getActivity())), options);
                }
                else {
                    startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra("uid", SaveSharedPreference.getUserUID(getActivity())));
                }*/
                startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra("uid", SaveSharedPreference.getUserUID(getActivity())));

                break;

            case R.id.cvMyEpisodes:
                EpisodeMy2 episodeMy2 = new EpisodeMy2();
                Bundle b = new Bundle();
                b.putInt("uid", SaveSharedPreference.getUserUID(getActivity()));
                b.putString("from", "maintab");
                episodeMy2.setArguments(b);
                startFragment(episodeMy2);
                break;

            case R.id.cvMyMap:
                startActivity(new Intent(getActivity(), MapActivity.class).putExtra("profile", "logged in user"));
                break;

            case R.id.cvProfilePictures:
                startActivity(new Intent(getActivity(), ProfilePicturesActivity2.class));
                break;

            case R.id.cvFriends:
                Friends friends = new Friends();
                Bundle bundle = new Bundle();
                bundle.putInt("uid", SaveSharedPreference.getUserUID(getActivity()));
                bundle.putString("from", "maintab");
                friends.setArguments(bundle);
                startFragment(friends);
                break;
            /* Settings */
            case R.id.cvNotifications:
                startActivity(new Intent(getActivity(), Notifications.class));
                break;

            case R.id.cvPrivacy:
                startActivity(new Intent(getActivity(), Privacy.class));
                break;

            case R.id.cvBlocking:
                startActivity(new Intent(getActivity(), Blocking.class));
                break;

            case R.id.cvAbout:
                break;
            /* Account */
            case R.id.cvPassword:
                startFragment(new Password());
                break;

            case R.id.cvEmail:
                startFragment(new Email());
                break;

            case R.id.cvPhoneNumber:
                startFragment(new PhoneNumber());
                break;

            case R.id.cvSyncWithOtherMedia:

                break;

            case R.id.cvLogOut:
                logOut();
                break;
        }
    }

    private void startFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_activity, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getPersonalInfo(){
        tvUserUsername.setText(SaveSharedPreference.getUsername(getActivity()));
        tvUserFirstLastName.setText(SaveSharedPreference.getUserFirstName(getActivity()) + " " + SaveSharedPreference.getUserLastName(getActivity()));

        if (SaveSharedPreference.getUserProfileImagePath(getActivity()).isEmpty()){
            new UserImageRequest(getActivity()).getImagesByUid(SaveSharedPreference.getUserUID(getActivity()), true, new StringCallback() {
                @Override
                public void done(String string) {
                    Log.e("string", string);
                    try{
                        JSONObject jsonObject = new JSONObject(string);
                        String images = jsonObject.getString("images");
                        JSONArray jsonArray = new JSONArray(images);
                        if (jsonArray.length() > 0){
                            JSONObject imageObj = jsonArray.getJSONObject(0);
                            String userImagePath = imageObj.getString("userImagePath");
                            Picasso.with(getActivity()).load(userImagePath).into(ivProfilePicture);
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            Log.e("pathELSE", SaveSharedPreference.getUserProfileImagePath(getActivity()));
            Picasso.with(getActivity()).load(SaveSharedPreference.getUserProfileImagePath(getActivity())).fit().centerCrop().into(ivProfilePicture);
        }

        new EventRequest(getActivity()).getEventDataByMember(SaveSharedPreference.getUserUID(getActivity()), new StringCallback() {
            @Override
            public void done(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String eventsString = jsonObject.getString("events");
                    JSONArray eventsArray = new JSONArray(eventsString);
                    tvNumberOfEpisodes.setText(String.valueOf(eventsArray.length()));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        new ServerRequestMethods(getActivity()).getFriends(SaveSharedPreference.getUserUID(getActivity()), new UserListCallback() {
            @Override
            public void done(List<User> users) {
                try{
                    tvNumberOfFriends.setText(String.valueOf(users.size()));
                }
                catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }
        });

        new ServerRequestMethods(getActivity()).getUserData(SaveSharedPreference.getUserUID(getActivity()), new UserCallback() {
            @Override
            public void done(User user) {
                Log.e("user", user.getPhone()+"\t"+ user.getUid()+"\t"+user.getEmail());
            }
        });

        tvEmail.setText(SaveSharedPreference.getEmail(getActivity()));
        tvPhoneNumber.setText(SaveSharedPreference.getUserPhoneNumber(getActivity()));
    }

    private void logOut() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.main_tab_personal_logout_alertdialog, null);

        Button bCancel = (Button) alertLayout.findViewById(R.id.bCancel);
        Button bYes = (Button) alertLayout.findViewById(R.id.bYes);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SaveSharedPreference.clearAll(getApplicationContext());
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
