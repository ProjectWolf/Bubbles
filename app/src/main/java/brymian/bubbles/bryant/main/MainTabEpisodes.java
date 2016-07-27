package brymian.bubbles.bryant.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import brymian.bubbles.R;
import brymian.bubbles.bryant.main.mainTabEpisodesRecyclerAdapter.MainTabEpisodesAllTimeMostDislikesRecyclerAdapter;
import brymian.bubbles.bryant.main.mainTabEpisodesRecyclerAdapter.MainTabEpisodesAllTimeMostLikesRecyclerAdapter;
import brymian.bubbles.bryant.main.mainTabEpisodesRecyclerAdapter.MainTabEpisodesAllTimeMostViewsRecyclerAdapter;
import brymian.bubbles.bryant.main.mainTabEpisodesRecyclerAdapter.MainTabEpisodesAllTimeTopRatedRecyclerAdapter;
import brymian.bubbles.bryant.main.mainTabEpisodesRecyclerAdapter.MainTabEpisodesLiveMostLikesRecyclerAdapter;
import brymian.bubbles.bryant.main.mainTabEpisodesRecyclerAdapter.MainTabEpisodesLiveMostViewsRecyclerAdapter;
import brymian.bubbles.bryant.main.mainTabEpisodesRecyclerAdapter.MainTabEpisodesLiveTopRatedRecyclerAdapter;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.EventListCallback;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.StringCallback;
import brymian.bubbles.damian.nonactivity.ServerRequest.EventRequest;
import brymian.bubbles.objects.Event;

public class MainTabEpisodes extends Fragment {
    RecyclerView rvLiveInYourNeighborhood, rvLiveMostViews, rvLiveTopRated, rvLiveMostLikes, rvAllTimeMostViews, rvAllTimeTopRated, rvAllTimeMostLikes, rvAllTimeMostDislikes;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_tab_episodes, container, false);
        rvLiveInYourNeighborhood = (RecyclerView) rootView.findViewById(R.id.rvLiveInYourNeighborhood);
        rvLiveMostViews = (RecyclerView) rootView.findViewById(R.id.rvLiveMostViews);
        rvLiveTopRated = (RecyclerView) rootView.findViewById(R.id.rvLiveTopRated);
        rvLiveMostLikes = (RecyclerView) rootView.findViewById(R.id.rvLiveMostLikes);
        rvAllTimeMostViews = (RecyclerView) rootView.findViewById(R.id.rvAllTimeMostViews);
        rvAllTimeTopRated = (RecyclerView) rootView.findViewById(R.id.rvAllTimeTopRated);
        rvAllTimeMostLikes = (RecyclerView) rootView.findViewById(R.id.rvAllTimeMostLikes);
        rvAllTimeMostDislikes = (RecyclerView) rootView.findViewById(R.id.rvAllTimeMostDislikes);

        /* in order from top to bottom */
        /* LIVE */
        setLiveInYourNeighborhood();
        setLiveMostViewsEpisodes();
        setLiveTopRated();
        setLiveMostLikesEpisodes();
        setLiveMostDislikesEpisodes();

        /* ALL TIME */
        setAllTimeMostViewsEpisodes();
        setALlTimeTopRated();
        setAllTimeMostLikesEpisodes();
        setAllTimeMostDislikes();

        return rootView;
    }

    private void setLiveInYourNeighborhood(){
        new EventRequest(getActivity()).getLiveEventDataByRadius(1.0, 0.0, 0.0, new StringCallback() {
            @Override
            public void done(String string) {

            }
        });
    }

    private void setLiveMostViewsEpisodes(){
        new EventRequest(getActivity()).getLiveEventDataByTopNViews(5, new EventListCallback() {
            @Override
            public void done(List<Event> eventList) {
                System.out.println("Live Most Views");
                Log.e("Live Views", ""+eventList.size());
                if(eventList.size() > 0){
                    List<String> episodeTitle = new ArrayList<>();
                    List<String> episodeHostName = new ArrayList<>();
                    List<Integer> episodeEid = new ArrayList<>();
                    List<Long> episodeViewCount = new ArrayList<>();
                    for(int i = 0; i < eventList.size(); i++){
                        episodeTitle.add(eventList.get(i).eventName);
                        episodeHostName.add(eventList.get(i).eventHostFirstName + " " + eventList.get(i).eventHostLastName);
                        Log.e("live views", "eid: "+eventList.get(i).eid);
                        episodeEid.add(eventList.get(i).eid);
                        episodeViewCount.add(eventList.get(i).eventViewCount);
                    }

                    adapter = new MainTabEpisodesLiveMostViewsRecyclerAdapter(getActivity(), episodeTitle, episodeHostName, episodeEid, episodeViewCount);
                    layoutManager = new LinearLayoutManager(getActivity());
                    rvLiveMostViews.setLayoutManager(layoutManager);
                    rvLiveMostViews.setAdapter(adapter);
                }
            }
        });
    }

    private void setLiveTopRated(){
        new EventRequest(getActivity()).getLiveEventDataByTopNRatings(5, new StringCallback() {
            @Override
            public void done(String string) {
                try{
                    System.out.println("Live Top Rated");
                    Log.e("Live Top Rated", "string: "+string.length());
                    if(string.length() > 0) {
                        List<String> episodeTitle = new ArrayList<>();
                        List<String> episodeHostName = new ArrayList<>();
                        List<Integer> episodeEid = new ArrayList<>();
                        List<Double> episodeRating = new ArrayList<>();
                        JSONArray jArray = new JSONArray(string);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jArray_jObject = jArray.getJSONObject(i);
                            JSONObject jEvent = jArray_jObject.getJSONObject("event");
                            episodeTitle.add(jEvent.getString("eventName"));
                            episodeHostName.add(jEvent.getString("eventHostFirstName") + " " + jEvent.getString("eventHostLastName"));
                            episodeEid.add(jEvent.getInt("eid"));
                            episodeRating.add(jEvent.getDouble("eventRatingRatio"));
                            Log.e("Live Top Rated", "eventName: "+jEvent.getString("eventName"));
                            Log.e("Live Top Rated", "eventHostName: " + jEvent.getString("eventHostFirstName") + " " + jEvent.getString("eventHostLastName"));
                            Log.e("Live Top Rated", "eid: " +jEvent.getInt("eid"));
                        }

                        adapter = new MainTabEpisodesLiveTopRatedRecyclerAdapter(getActivity(), episodeTitle, episodeHostName, episodeEid, episodeRating);
                        layoutManager = new LinearLayoutManager(getActivity());
                        rvLiveTopRated.setLayoutManager(layoutManager);
                        rvLiveTopRated.setAdapter(adapter);
                    }
                }
                catch (JSONException | NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setLiveMostLikesEpisodes(){
        new EventRequest(getActivity()).getLiveEventDataByTopNLikes(5, new EventListCallback() {
            @Override
            public void done(List<Event> eventList) {
                try{
                    System.out.println("Live Most Likes");
                    Log.e("Live Likes", ""+eventList.size());
                    if(eventList.size() > 0){
                        List<String> episodeTitle = new ArrayList<>();
                        List<String> episodeHostName = new ArrayList<>();
                        List<Integer> episodeEid = new ArrayList<>();
                        List<Integer> episodeLikeCount = new ArrayList<>();
                        for (int i = 0; i < eventList.size(); i++) {
                            episodeTitle.add(eventList.get(i).eventName);
                            episodeHostName.add(eventList.get(i).eventHostFirstName + " " + eventList.get(i).eventHostLastName);
                            episodeEid.add(eventList.get(i).eid);
                            Log.e("live likes", "eid: "+eventList.get(i).eid);
                            episodeLikeCount.add(eventList.get(i).eventLikeCount);
                        }

                        adapter = new MainTabEpisodesLiveMostLikesRecyclerAdapter(getActivity(), episodeTitle, episodeHostName, episodeEid, episodeLikeCount);
                        layoutManager = new LinearLayoutManager(getActivity());
                        rvLiveMostLikes.setLayoutManager(layoutManager);
                        rvLiveMostLikes.setAdapter(adapter);
                    }
                }
                catch (NullPointerException npe){
                    npe.printStackTrace();
                }

            }
        });
    }

    private void setLiveMostDislikesEpisodes(){
        new EventRequest(getActivity()).getLiveEventDataByTopNDislikes(5, new EventListCallback() {
            @Override
            public void done(List<Event> eventList) {

            }
        });
    }


    private void setAllTimeMostViewsEpisodes(){
        new EventRequest(getActivity()).getEventDataByTopNViews(5, new EventListCallback() {
            @Override
            public void done(List<Event> eventList) {
                try {
                    if (eventList.size() > 0) {
                        List<String> episodeTitle = new ArrayList<>();
                        List<String> episodeHostName = new ArrayList<>();
                        List<Integer> episodeEid = new ArrayList<>();
                        List<String> episodeViewCount = new ArrayList<>();
                        for (int i = 0; i < eventList.size(); i++) {
                            episodeTitle.add(eventList.get(i).eventName);
                            episodeHostName.add(eventList.get(i).eventHostFirstName + " " + eventList.get(i).eventHostLastName);
                            episodeEid.add(eventList.get(i).eid);
                            episodeViewCount.add(String.valueOf(eventList.get(i).eventViewCount));
                            Log.e("Alltime Views","View count" + eventList.get(i).eventViewCount);
                        }

                        adapter = new MainTabEpisodesAllTimeMostViewsRecyclerAdapter(getActivity(), episodeTitle, episodeHostName, episodeEid, episodeViewCount);
                        layoutManager = new LinearLayoutManager(getActivity());
                        rvAllTimeMostViews.setLayoutManager(layoutManager);
                        rvAllTimeMostViews.setAdapter(adapter);
                    }
                }
                catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }
        });
    }

    private void setALlTimeTopRated(){
        new EventRequest(getActivity()).getEventDataByTopNRatings(5, new StringCallback() {
            @Override
            public void done(String string) {
                try{
                    if(string.length() > 0) {
                        List<String> episodeTitle = new ArrayList<>();
                        List<String> episodeHostName = new ArrayList<>();
                        List<Integer> episodeEid = new ArrayList<>();
                        List<Double> episodeRating = new ArrayList<>();
                        JSONArray jArray = new JSONArray(string);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jArray_jObject = jArray.getJSONObject(i);
                            JSONObject jEvent = jArray_jObject.getJSONObject("event");
                            episodeTitle.add(jEvent.getString("eventName"));
                            episodeHostName.add(jEvent.getString("eventHostFirstName") + " " + jEvent.getString("eventHostLastName"));
                            episodeEid.add(jEvent.getInt("eid"));
                            episodeRating.add(jEvent.getDouble("eventRatingRatio"));
                        }

                        adapter = new MainTabEpisodesAllTimeTopRatedRecyclerAdapter(getActivity(), episodeTitle, episodeHostName, episodeEid, episodeRating);
                        layoutManager = new LinearLayoutManager(getActivity());
                        rvAllTimeTopRated.setLayoutManager(layoutManager);
                        rvAllTimeTopRated.setAdapter(adapter);
                    }
                }
                catch (JSONException | NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setAllTimeMostLikesEpisodes(){
        new EventRequest(getActivity()).getEventDataByTopNLikes(5, new EventListCallback() {
            @Override
            public void done(List<Event> eventList) {
                try {
                    if (eventList.size() > 0) {
                        List<String> episodeTitle = new ArrayList<>();
                        List<String> episodeHostName = new ArrayList<>();
                        List<Integer> episodeEid = new ArrayList<>();
                        List<String> episodeLikeCount = new ArrayList<>();
                        for (int i = 0; i < eventList.size(); i++) {
                            episodeTitle.add(eventList.get(i).eventName);
                            episodeHostName.add(eventList.get(i).eventHostFirstName + " " + eventList.get(i).eventHostLastName);
                            episodeEid.add(eventList.get(i).eid);
                            episodeLikeCount.add(String.valueOf(eventList.get(i).eventLikeCount));
                        }

                        adapter = new MainTabEpisodesAllTimeMostLikesRecyclerAdapter(getActivity(), episodeTitle, episodeHostName, episodeEid, episodeLikeCount);
                        layoutManager = new LinearLayoutManager(getActivity());
                        rvAllTimeMostLikes.setLayoutManager(layoutManager);
                        rvAllTimeMostLikes.setAdapter(adapter);
                    }
                }catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }
        });
    }

    private void setAllTimeMostDislikes(){
        new EventRequest(getActivity()).getEventDataByTopNDislikes(5, new EventListCallback() {
            @Override
            public void done(List<Event> eventList) {
                try {
                    if (eventList.size() > 0) {
                        List<String> episodeTitle = new ArrayList<>();
                        List<String> episodeHostName = new ArrayList<>();
                        List<Integer> episodeEid = new ArrayList<>();
                        List<String> episodeDislikeCount = new ArrayList<>();
                        for (int i = 0; i < eventList.size(); i++) {
                            episodeTitle.add(eventList.get(i).eventName);
                            episodeHostName.add(eventList.get(i).eventHostFirstName + " " + eventList.get(i).eventHostLastName);
                            episodeEid.add(eventList.get(i).eid);
                            episodeDislikeCount.add(String.valueOf(eventList.get(i).eventDislikeCount));
                        }

                        adapter = new MainTabEpisodesAllTimeMostDislikesRecyclerAdapter(getActivity(), episodeTitle, episodeHostName, episodeEid, episodeDislikeCount);
                        layoutManager = new LinearLayoutManager(getActivity());
                        rvAllTimeMostDislikes.setLayoutManager(layoutManager);
                        rvAllTimeMostDislikes.setAdapter(adapter);
                    }
                }
                catch(NullPointerException npe){
                    npe.printStackTrace();
                }
            }
        });
    }
}