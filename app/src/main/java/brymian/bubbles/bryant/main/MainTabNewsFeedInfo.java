package brymian.bubbles.bryant.main;

import java.io.Serializable;
import java.util.List;

public class MainTabNewsFeedInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private int eid, uid, user1Uid, user2Uid;
    private String username, firstLastName, episodeTitle, timestamp, userProfileImage;
    private String user1Username, user1ProfileImage, user2Username, user2ProfileImage;
    private String uploadedImage, userImagePurposeLabel, userImageGpsLatitude, userImageGpsLongitude, userImageUploadTimestamp, userImageLikeCount, userImageDislikeCount, userImageCommentCount;
    //int uid;
    List<String> uids, usernames;

    public MainTabNewsFeedInfo(){

    }

    /* for FriendsJoinedMutualEvent */
    public MainTabNewsFeedInfo(int eid, String episodeTitle, String timestamp, List<String> usernames, List<String> uids){
        this.eid = eid;
        this.episodeTitle = episodeTitle;
        this.timestamp = timestamp;
        this.usernames = usernames;
        this.uids = uids;
    }

    /* for FriendCreatedEvent, ActiveEvent as well */
    public MainTabNewsFeedInfo(int eid, String episodeTitle, String episodeTimestamp, String username, int uid, String userProfileImage){
        this.eid = eid;
        this.episodeTitle = episodeTitle;
        this.timestamp = episodeTimestamp;
        this.username = username;
        this.uid = uid;
        this.userProfileImage = userProfileImage;
    }

    /* for FriendsThatBecameFriends */
    public MainTabNewsFeedInfo(String user1Username, int user1Uid, String user1ProfileImage, String user2Username, int user2Uid, String user2ProfileImage){
        this.user1Username = user1Username;
        this.user1Uid = user1Uid;
        this.user1ProfileImage = user1ProfileImage;
        this.user2Username = user2Username;
        this.user2Uid = user2Uid;
        this.user2ProfileImage = user2ProfileImage;
    }

    /* for UploadImages */
    public MainTabNewsFeedInfo(int uid, String username, String userProfileImage, String uploadedImage, String userImagePurposeLabel, String userImageGpsLatitude, String userImageGpsLongitude, String userImageUploadTimestamp, String userImageLikeCount, String userImageDislikeCount, String userImageCommentCount){
        this.uid = uid;
        this.username = username;
        this.userProfileImage = userProfileImage;
        this.uploadedImage = uploadedImage;
        this.userImagePurposeLabel = userImagePurposeLabel;
        this.userImageGpsLatitude = userImageGpsLatitude;
        this.userImageGpsLongitude = userImageGpsLongitude;
        this.userImageUploadTimestamp = userImageUploadTimestamp;
        this.userImageLikeCount = userImageLikeCount;
        this.userImageDislikeCount = userImageDislikeCount;
        this.userImageCommentCount = userImageCommentCount;
    }

    public int getEid(){
        return eid;
    }

    public String getEpisodeTitle(){
        return episodeTitle;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public List<String> getUsernames(){
        return usernames;
    }

    public List<String> getUids(){
        return uids;
    }

    public String getUsername(){
        return username;
    }

    public String getUser1Username(){
        return user1Username;
    }

    public int getUser1Uid(){
        return user1Uid;
    }

    public String getUser1ProfileImage(){
        return user1ProfileImage;
    }

    public int getUser2Uid(){
        return user2Uid;
    }

    public String getUser2Username(){
        return user2Username;
    }

    public String getUser2ProfileImage(){
        return user2ProfileImage;
    }

    public String getUploadedImage(){
        return uploadedImage;
    }

    public int getUid(){
        return uid;
    }

    public String getUserProfileImage(){
        return userProfileImage;
    }

}