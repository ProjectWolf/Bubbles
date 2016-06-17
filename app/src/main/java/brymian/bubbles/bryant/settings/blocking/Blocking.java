package brymian.bubbles.bryant.settings.blocking;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import brymian.bubbles.R;
import brymian.bubbles.bryant.nonactivity.SaveSharedPreference;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.UserListCallback;
import brymian.bubbles.damian.nonactivity.ServerRequest.FriendshipStatusRequest;
import brymian.bubbles.objects.User;

public class Blocking extends Fragment {

    Toolbar mToolbar;
    RecyclerView rvBlockedUsers;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_blocking, container, false);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.Blocking);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvBlockedUsers = (RecyclerView) rootView.findViewById(R.id.rvBlockedUsers);

        new FriendshipStatusRequest(getActivity()).getFriendshipStatusRequestSentUsers(SaveSharedPreference.getUserUID(getActivity()), "Blocked", new UserListCallback() {
            @Override
            public void done(List<User> users) {
                List<BlockedUser> blockedUserArrayList = new ArrayList<>();

                for (User user : users) {
                    BlockedUser friendRequester = new BlockedUser(user.getUsername(), user.getFirstName() + " " + user.getLastName(), user.getUid());
                    blockedUserArrayList.add(friendRequester);
                }
                adapter = new BlockingRecyclerAdapter(getActivity(), blockedUserArrayList);
                layoutManager = new LinearLayoutManager(getActivity());
                rvBlockedUsers.setLayoutManager(layoutManager);
                rvBlockedUsers.setAdapter(adapter);
            }
        });
        return rootView;
    }

}
