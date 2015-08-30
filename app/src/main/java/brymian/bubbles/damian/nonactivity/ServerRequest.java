package brymian.bubbles.damian.nonactivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import brymian.bubbles.R;

import static brymian.bubbles.damian.nonactivity.Miscellaneous.getJsonNullableInt;
/**
 * Created by Ziomster on 7/12/2015.
 */
public class ServerRequest {

    //private final String SERVER = "http://73.194.170.63:8080/";
    private final String SERVER = "http://192.168.1.12:8080/";
    private final String UPLOADS = "Bubbles/Uploads/";
    private final String PHP = "BubblesServer/";

    private ProgressDialog pd;

    private Activity activity;

    public ServerRequest(Activity activity) {
        this.activity = activity;
        pd = new ProgressDialog(activity);
        pd.setCancelable(false);
        pd.setTitle("Processing");
        pd.setMessage("Please wait...");
    }

    public void createUserNormal(User user, StringCallback stringCallback) {
        pd.show();
        new CreateUserNormal(user, stringCallback).execute();
    }

    public void createUserFacebook(User user, StringCallback stringCallback) {
        pd.show();
        new CreateUserFacebook(user, stringCallback).execute();
    }

    public void authUserNormal(User user, VoidCallback voidCallback) {
        pd.show();
        new AuthUserNormal(user, voidCallback).execute();
    }

    public void authUserFacebook(User user, VoidCallback voidCallback) {
        pd.show();
        new AuthUserFacebook(user, voidCallback).execute();
    }

    public void getUsers(String searchedUser, UserListCallback userListCallback) {
        pd.show();
        new GetUsers(searchedUser, userListCallback).execute();
    }

    public void getFriendStatus(int loggedUserUid, int otherUserUid, StringCallback stringCallback) {
        pd.show();
        new GetFriendStatus(loggedUserUid, otherUserUid, stringCallback).execute();
    }

    public void getFriends(int uid, UserListCallback userListCallback) {
        pd.show();
        new GetFriends(uid, userListCallback).execute();
    }

    public void getImagePaths(int uid, String purpose, StringListCallback stringListCallback) {
        pd.show();
        new GetImagePaths(uid, purpose, stringListCallback).execute();
    }

    public void setFriendStatus(int loggedUserUid, int otherUserUid, StringCallback stringCallback) {
        pd.show();
        new SetFriendStatus(loggedUserUid, otherUserUid, stringCallback).execute();
    }

    public void uploadImage(int uid, String name, String purpose, String privacy,
        double latitude, double longitude, String image, StringCallback stringCallback) {
        pd.show();
        new UploadImage(uid, name, purpose, privacy, latitude, longitude, image, stringCallback).execute();
    }

    /*
    public void uploadVideo(String name, StringCallback stringCallback) {
        pd.show();
        new UploadVideo(name, stringCallback).execute();
    }
    */

    private class CreateUserNormal extends AsyncTask<Void, Void, String> {

        User user;
        StringCallback stringCallback;

        private CreateUserNormal(User user, StringCallback stringCallback) {
            this.user = user;
            this.stringCallback = stringCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = SERVER + PHP + "DBIO/createUserNormal.php";

            String jsonUser =
                "{\"username\":\"" + user.getUsername() + "\"," +
                " \"password\":\"" + user.getPassword() + "\"," +
                " \"firstName\":\"" + user.getFirstName() + "\"," +
                " \"lastName\":\"" + user.getLastName() + "\"," +
                " \"email\":\"" + user.getEmail() + "\"}";
            Post request = new Post();
            try
            {   // Successful SQL command returns one empty space (" ")
                return request.post(url, jsonUser);
            }
            catch (IOException ioe)
            {
                return ioe.toString();
            }
        }

        @Override
        protected void onPostExecute(String string) {
            pd.dismiss();
            stringCallback.done(string);

            super.onPostExecute(string);
        }

    }

    private class CreateUserFacebook extends AsyncTask<Void, Void, String> {

        User user;
        StringCallback stringCallback;

        private CreateUserFacebook(User user, StringCallback stringCallback) {
            this.user = user;
            this.stringCallback = stringCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = SERVER + PHP + "DBIO/createUserFacebook.php";

            String jsonUser =
                "{\"facebookUid\":\"" + user.getFacebookUid() + "\"," +
                " \"firstName\":\"" + user.getFirstName() + "\"," +
                " \"lastName\":\"" + user.getLastName() + "\"," +
                " \"email\":\"" + user.getEmail() + "\"}";
            Post request = new Post();
            try {
                String response = request.post(url, jsonUser);
                return response; // Successful SQL command returns one empty space (" ")
            } catch (IOException ioe) {
                return ioe.toString();
            }
        }

        @Override
        protected void onPostExecute(String string) {
            pd.dismiss();
            stringCallback.done(string);

            super.onPostExecute(string);
        }

    }

    private class AuthUserNormal extends AsyncTask<Void, Void, Void> {

        User user;
        UserDataLocal udl;
        VoidCallback voidCallback;

        private AuthUserNormal(User user, VoidCallback voidCallback) {
            this.user = user;
            this.voidCallback = voidCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = SERVER + PHP + "DBIO/authUserNormal.php";

            String jsonUser =
                "{\"username\":\"" + user.getUsername() + "\"," +
                " \"password\":\"" + user.getPassword() + "\"}";
            Post request = new Post();

            try
            {
                String response = request.post(url, jsonUser);
                System.out.println("RESPONSE: " + response);

                if (response.equals("USERNAME AND PASSWORD COMBINATION IS INCORRECT."))
                {
                    udl = new UserDataLocal(activity);
                    udl.resetUserData();
                    udl.setLoggedStatus(false);
                }
                else
                {
                    JSONObject jObject = new JSONObject(response);
                    System.out.println("jObject length: " + jObject.length());
                    if (jObject.length() == 8)
                    {
                        user.setUser(
                            getJsonNullableInt(jObject, "uid"),
                            jObject.getString("facebookUid"),
                            jObject.getString("googlepUid"),
                            jObject.getString("username"),
                            jObject.getString("password"),
                            jObject.getString("firstName"),
                            jObject.getString("lastName"),
                            jObject.getString("email")
                        );
                        udl = new UserDataLocal(activity);
                        udl.setUserData(user);
                        udl.setLoggedStatus(true);
                        try{ Thread.sleep(1000); } catch (InterruptedException ie) {}
                        System.out.println("getLoggedStatus; " + udl.getLoggedStatus());
                    }
                    else {
                        System.out.println("Unknown login error: ");
                    }
                }
            }
            catch (IOException ioe)
            {
                User user = new User();
                user.initUserNormal(activity.getString(R.string.null_user), "");
                udl = new UserDataLocal(activity);
                udl.setUserData(user);
                ioe.printStackTrace();
            }
            catch (JSONException jsone)
            {
                jsone.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
            voidCallback.done(aVoid);

            super.onPostExecute(aVoid);
        }
    }

    private class AuthUserFacebook extends AsyncTask<Void, Void, Void> {

        User user;
        UserDataLocal udl;
        VoidCallback voidCallback;

        private AuthUserFacebook(User user, VoidCallback voidCallback) {
            this.user = user;
            this.voidCallback = voidCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = SERVER + PHP + "DBIO/authUserFacebook.php";

            String jsonUser = "{\"facebookUid\":\"" + user.getFacebookUid() + "\"}";
            Post request = new Post();
            //System.out.println(jsonUser);
            try {
                String response = request.post(url, jsonUser);

                System.out.println("RESPONSE: " + response);

                if (response.equals("FACEBOOK USER DOES NOT EXIST IN THE DATABASE."))
                {
                    System.out.println("FACEBOOK USER DOES NOT EXIST IN THE DATABASE.");
                    System.out.println("UID: " + user.getUid());
                }
                else
                {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.length() == 8) {
                        System.out.println("jObject length: " + jObject.length());
                        System.out.println("UID : " + user.getUid());
                        user.setUser(
                            getJsonNullableInt(jObject, "uid"),
                            jObject.getString("facebookUid"),
                            jObject.getString("googlepUid"),
                            jObject.getString("username"),
                            jObject.getString("password"),
                            jObject.getString("firstName"),
                            jObject.getString("lastName"),
                            jObject.getString("email")
                        );
                        udl = new UserDataLocal(activity);
                        udl.setUserData(user);
                        udl.setLoggedStatus(true);
                    }
                }
            } catch (IOException ioe) {
                User user = new User();
                user.initUserFacebook(Resources.getSystem().getString(R.string.null_user));
                udl = new UserDataLocal(activity);
                udl.setUserData(user);
                ioe.printStackTrace();
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
            voidCallback.done(aVoid);

            super.onPostExecute(aVoid);
        }
    }

    private class GetUsers extends AsyncTask<Void, Void, List<User>> {

        String searchedUser;
        UserListCallback userListCallback;

        private GetUsers(String searchedUser, UserListCallback userListCallback) {
            this.searchedUser = searchedUser;
            this.userListCallback = userListCallback;
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            String url = SERVER + PHP + "DBIO/getUsers.php";

            System.out.println("SEARCHED USER: " + searchedUser);
            //String searched_user = "";
            String jsonSearchedUser = "{\"searchedUser\":\"" + searchedUser + "\"}";
            Post request = new Post();
            //FriendsActivity friendsActivity = new FriendsActivity();
            try {
                String response = request.post(url, jsonSearchedUser);
                //friendsActivity.tShowFriends.setText(response);
                //System.out.println("RESPONSE: " + response);
                if (response.equals("INCORRECT STRING."))
                {
                    System.out.println(response);
                    return null;
                }
                else
                {
                    JSONArray j2dArray = new JSONArray(response);
                    List<User> userList = new ArrayList<User>();
                    for (int i = 0; i < j2dArray.length(); i++)
                    {
                        JSONObject jUser = (JSONObject) j2dArray.get(i);
                        int uid = jUser.getInt("uid");
                        String username = jUser.getString("username");
                        String firstName = jUser.getString("firstName");
                        String lastName = jUser.getString("lastName");
                        User user = new User();
                        user.setUser(uid, null, null, username, null, firstName, lastName, null);
                        userList.add(user);
                    }
                    // The set will be null if nothing matched in the database
                    return userList;
                }
            }
            catch (IOException ioe)
            {
                System.out.println(ioe.toString());
                return null;
            }
            catch (JSONException jsone)
            {
                jsone.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<User> users)
        {
            pd.dismiss();
            userListCallback.done(users);

            super.onPostExecute(users);
        }

    }

    private class GetFriendStatus extends AsyncTask<Void, Void, String> {

        int loggedUid;
        int otherUid;
        StringCallback stringCallback;

        private GetFriendStatus(int loggedUserUid, int otherUserUid, StringCallback stringCallback) {
            this.loggedUid = loggedUserUid;
            this.otherUid = otherUserUid;
            this.stringCallback = stringCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = SERVER + PHP + "DBIO/getFriendStatus.php";

            String jsonFriends = "{\"uid1\":" + loggedUid + "," + " \"uid2\":" + otherUid + "}";
            Post request = new Post();
            try {
                String response = request.post(url, jsonFriends);
                return response; // Successful SQL command returns one empty space (" ")
            } catch (IOException ioe) {
                return ioe.toString();
            }
        }

        @Override
        protected void onPostExecute(String string) {
            pd.dismiss();
            stringCallback.done(string);

            super.onPostExecute(string);
        }

    }

    private class GetFriends extends AsyncTask<Void, Void, List<User>> {

        int uid;
        UserListCallback userListCallback;

        private GetFriends(int uid, UserListCallback userListCallback) {
            this.uid = uid;
            this.userListCallback = userListCallback;
        }

        @Override
        protected List<User> doInBackground(Void... params) {

            String url = SERVER + PHP + "DBIO/Functions/Friend.php?function=getFriends";
            String jsonLoggedUserUid = "{\"uid\":" + uid + "}";
            Post request = new Post();

            try {

                String response = request.post(url, jsonLoggedUserUid);
                if (response.equals("UID IS NOT A NUMBER."))
                {
                    System.out.println(response);
                    return null;
                }
                else if (response.equals("USER WITH PROVIDED UID DOES NOT EXIST."))
                {
                    System.out.println(response);
                    return null;
                }
                else
                {
                    JSONArray j2dArray = new JSONArray(response);
                    List<User> userList = new ArrayList<User>();
                    for (int i = 0; i < j2dArray.length(); i++) {
                        JSONObject jUser = (JSONObject) j2dArray.get(i);
                        int uid = jUser.getInt("uid");
                        String namefirst = jUser.getString("firstName");
                        String namelast = jUser.getString("lastName");
                        User user = new User();
                        user.setUser(uid, null, null, null, null, namefirst, namelast, null);
                        userList.add(user);
                    }
                    // The set will be null if nothing matched in the database
                    return userList;
                }
            } catch (IOException ioe) {
                System.out.println(ioe.toString());
                return null;
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<User> users) {
            pd.dismiss();
            userListCallback.done(users);

            super.onPostExecute(users);
        }

    }

    private class GetImagePaths extends AsyncTask<Void, Void, List<String>> {

        int uid;
        String imagePurposeLabel;
        StringListCallback stringListCallback;

        private GetImagePaths(int uid, String imagePurposeLabel, StringListCallback stringListCallback) {
            this.uid = uid;
            this.imagePurposeLabel = imagePurposeLabel;
            this.stringListCallback = stringListCallback;
        }

        @Override
        protected List<String> doInBackground(Void... params) {

            String url = SERVER + PHP + "DBIO/Functions/Image.php?function=getImagePaths";
            String jsonGetImagePaths =
                "{\"uid\":" + uid + "," +
                " \"imagePurposeLabel\":\"" + imagePurposeLabel + "\"}";
            Post request = new Post();

            try {

                String response = request.post(url, jsonGetImagePaths);
                System.out.println("RESPONSE: " + response);

                if (response.equals("PURPOSE DOES NOT EXIST IN THE DATABASE."))
                {
                    System.out.println(response);
                    return null;
                }
                else if (response.equals("UID IS NOT A NUMBER."))
                {
                    System.out.println(response);
                    return null;
                }
                else if (response.equals("USER WITH PROVIDED UID DOES NOT EXIST."))
                {
                    System.out.println(response);
                    return null;
                }
                else
                {
                    JSONArray jPaths = new JSONArray(response);
                    List<String> pathList = new ArrayList<String>();
                    for (int i = 0; i < jPaths.length(); i++) {
                        String path = SERVER + UPLOADS + jPaths.getString(i);
                        pathList.add(path);
                    }
                    // The set will be null if nothing matched in the database
                    return pathList;
                }
            } catch (IOException ioe) {
                System.out.println(ioe.toString());
                return null;
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            pd.dismiss();
            stringListCallback.done(strings);

            super.onPostExecute(strings);
        }

    }

    private class SetFriendStatus extends AsyncTask<Void, Void, String> {

        int loggedUserUid;
        int otherUserUid;
        StringCallback stringCallback;

        private SetFriendStatus(int loggedUserUid, int otherUserUid, StringCallback stringCallback) {
            this.loggedUserUid = loggedUserUid;
            this.otherUserUid = otherUserUid;
            this.stringCallback = stringCallback;
        }

        @Override
        protected String doInBackground(Void... params) {

            String url = SERVER + PHP + "DBIO/setFriendStatus.php";

            String jsonFriends = "{\"uid1\":" + loggedUserUid + "," + " \"uid2\":" + otherUserUid + "}";
            Post request = new Post();
            try {
                String response = request.post(url, jsonFriends);
                return response; // Successful SQL command returns one empty space (" ")
            } catch (IOException ioe) {
                return ioe.toString();
            }
        }

        @Override
        protected void onPostExecute(String string) {
            pd.dismiss();
            stringCallback.done(string);

            super.onPostExecute(string);
        }

    }

    private class UploadImage extends AsyncTask<Void, Void, String> {

        int uid;
        String userImageName;
        String userImagePurposeLabel;
        String userImagePrivacyLabel;
        double userImageGpsLatitude;
        double userImageGpsLongitude;
        String userImage;
        StringCallback stringCallback;

        private UploadImage(int uid, String userImageName, String userImagePurposeLabel,
            String userImagePrivacyLabel, double userImageGpsLatitude, double userImageGpsLongitude,
            String userImage, StringCallback stringCallback) {

            this.uid = uid;
            this.userImageName = userImageName;
            this.userImagePurposeLabel = userImagePurposeLabel;
            this.userImagePrivacyLabel = userImagePrivacyLabel;
            this.userImageGpsLatitude = userImageGpsLatitude;
            this.userImageGpsLongitude = userImageGpsLongitude;
            this.userImage = userImage;
            this.stringCallback = stringCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = SERVER + PHP + "DBIO/Functions/Image.php?function=uploadImage";

            try {
                JSONObject jsonImageObject = new JSONObject();
                jsonImageObject.put("uid", uid);
                jsonImageObject.put("userImageName", userImageName);
                jsonImageObject.put("userImagePurposeLabel", userImagePurposeLabel);
                jsonImageObject.put("userImagePrivacyLabel", userImagePrivacyLabel);
                jsonImageObject.put("userImageGpsLatitude", userImageGpsLatitude);
                jsonImageObject.put("userImageGpsLongitude", userImageGpsLongitude);
                jsonImageObject.put("userImage", userImage);
                /*
                String jsonImage =
                    "{\"uid\":" + uid + "," +
                    " \"purpose\":\"" + purpose + "\"," +
                    " \"name\":\"" + name + "\"," +
                    " \"image\":\"" + image + "\"}";
                System.out.println("[DAMIAN] uid: " + uid);
                System.out.println("[DAMIAN] purpose: " + purpose);
                System.out.println("[DAMIAN] name: " + name);
                System.out.println("[DAMIAN] image length: " + image.length());
                */
                String jsonImage = jsonImageObject.toString();
                System.out.println(jsonImage);
                Post request = new Post();
                String response = request.post(url, jsonImage);
                //System.out.println(response);
                return response; // Successful SQL command returns one empty space (" ")
            } catch (IOException ioe) {
                return ioe.toString();
            } catch (JSONException jsone) {
                return jsone.toString();
            }
        }

        @Override
        protected void onPostExecute(String string) {
            pd.dismiss();
            stringCallback.done(string);
            //Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();

            super.onPostExecute(string);
        }

    }
}
