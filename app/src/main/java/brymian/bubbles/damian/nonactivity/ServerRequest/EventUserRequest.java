package brymian.bubbles.damian.nonactivity.ServerRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import brymian.bubbles.damian.nonactivity.Connection.HTTPConnection;
import brymian.bubbles.damian.nonactivity.CustomException.SetOrNotException;
import brymian.bubbles.damian.nonactivity.Post;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.EventUserCallback;
import brymian.bubbles.damian.nonactivity.ServerRequest.Callback.StringCallback;
import brymian.bubbles.objects.EventUser;

import static brymian.bubbles.damian.nonactivity.Miscellaneous.getBooleanObjectFromObject;
import static brymian.bubbles.damian.nonactivity.Miscellaneous.getDoubleObjectFromObject;
import static brymian.bubbles.damian.nonactivity.Miscellaneous.getIntegerObjectFromObject;
import static brymian.bubbles.damian.nonactivity.Miscellaneous.getLongObjectFromObject;
import static brymian.bubbles.damian.nonactivity.Miscellaneous.getNullOrValue;
import static brymian.bubbles.damian.nonactivity.Miscellaneous.getNullOrValue;

public class EventUserRequest {

    private HTTPConnection httpConnection = null;
    private ProgressDialog pd = null;

    public EventUserRequest(Activity activity) {
        pd = new ProgressDialog(activity);
        pd.setCancelable(false);
        pd.setTitle("Processing");
        pd.setMessage("Please wait...");
        httpConnection = new HTTPConnection();
    }

    public void addUserToEvent(Integer eid, Integer inviterUid, Integer inviteeUid, StringCallback stringCallback)
    {
        pd.show();
        new AddUserToEvent(eid, inviterUid, inviteeUid, stringCallback).execute();
    }

    public void getEventUserData(Integer eid, Integer uid, EventUserCallback eventUserCallback)
    {
        pd.show();
        new GetEventUserData(eid, uid, eventUserCallback).execute();
    }

    public void getEventUsersData(String eventUserInviteStatusTypeLabel, Integer eid, StringCallback stringCallback)
    {
        pd.show();
        new GetEventUsersData(eventUserInviteStatusTypeLabel, eid, stringCallback).execute();
    }

    public void setEventUser(Integer eid, Integer uid,
        String eventUserTypeLabel, String eventUserInviteStatusTypeLabel,
        Boolean[] setOrNot, StringCallback stringCallback) throws SetOrNotException
    {
        pd.show();
        new SetEventUser(eid, uid, eventUserTypeLabel, eventUserInviteStatusTypeLabel,
            setOrNot, stringCallback).execute();
    }




    private class AddUserToEvent extends AsyncTask<Void, Void, String> {

        Integer eid;
        Integer inviterUid;
        Integer inviteeUid;
        StringCallback stringCallback;

        private AddUserToEvent(Integer eid, Integer inviterUid, Integer inviteeUid, StringCallback stringCallback)
        {
            this.eid = eid;
            this.inviterUid = inviterUid;
            this.inviteeUid = inviteeUid;
            this.stringCallback = stringCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = httpConnection.getWebServerString() +
                    "AndroidIO/EventUserRequest.php?function=addUserToEvent";

            Post request = new Post();

            try
            {
                JSONObject jsonEventUserObject = new JSONObject();
                jsonEventUserObject.put("eid", getNullOrValue(eid));
                jsonEventUserObject.put("inviterUid", getNullOrValue(inviterUid));
                jsonEventUserObject.put("inviteeUid", getNullOrValue(inviteeUid));

                String jsonEventUserString = jsonEventUserObject.toString();
                String response = request.post(url, jsonEventUserString);

                return response;
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                return "ERROR ENCOUNTERED. SEE ANDROID LOG.";
            }
            catch (JSONException jsone)
            {
                jsone.printStackTrace();
                return "ERROR ENCOUNTERED. SEE ANDROID LOG.";
            }
        }

        @Override
        protected void onPostExecute(String string) {
            pd.dismiss();
            stringCallback.done(string);

            super.onPostExecute(string);
        }

    }



    private class GetEventUserData extends AsyncTask<Void, Void, EventUser> {

        Integer eid;
        Integer uid;
        EventUserCallback eventUserCallback;

        private GetEventUserData(Integer eid, Integer uid, EventUserCallback eventUserCallback)
        {
            this.eid = eid;
            this.uid = uid;
            this.eventUserCallback = eventUserCallback;
        }

        @Override
        protected EventUser doInBackground(Void... params) {
            String url = httpConnection.getWebServerString() +
                    "AndroidIO/EventUserRequest.php?function=getEventUserData";

            Post request = new Post();
            JSONObject jsonEventUserObject;

            try
            {
                jsonEventUserObject = new JSONObject();
                jsonEventUserObject.put("eid", getNullOrValue(eid));
                jsonEventUserObject.put("uid", getNullOrValue(uid));

                String jsonEventUserString = jsonEventUserObject.toString();
                String response = request.post(url, jsonEventUserString);

                if (response.equals("This user is not a member of this event."))
                    return  null;
                else
                {
                    jsonEventUserObject = new JSONObject(response);
                    EventUser jsonEventUser = new EventUser(
                        getIntegerObjectFromObject(jsonEventUserObject.get("eid")),
                        getIntegerObjectFromObject(jsonEventUserObject.get("uid")),
                        jsonEventUserObject.getString("eventUserTypeLabel"),
                        jsonEventUserObject.getString("eventUserInviteStatusLabel"),
                        jsonEventUserObject.getString("eventUserInviteStatusActionTimestamp")
                    );
                    return jsonEventUser;
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                return null;
            }
            catch (JSONException jsone)
            {
                jsone.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(EventUser eventUser) {
            pd.dismiss();
            eventUserCallback.done(eventUser);

            super.onPostExecute(eventUser);
        }

    }



    private class GetEventUsersData extends AsyncTask<Void, Void, String> {

        Integer eid;
        String eventUserInviteStatusTypeLabel;
        StringCallback stringCallback;

        private GetEventUsersData(String eventUserInviteStatusTypeLabel, Integer eid, StringCallback stringCallback)
        {
            this.eventUserInviteStatusTypeLabel = eventUserInviteStatusTypeLabel;
            this.eid = eid;
            this.stringCallback = stringCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = httpConnection.getWebServerString() +
                    "AndroidIO/EventUserRequest.php?function=getEventUsersData";

            Post request = new Post();

            try
            {
                JSONObject jsonEventUserObject = new JSONObject();
                jsonEventUserObject.put("eid", getNullOrValue(eid));
                jsonEventUserObject.put("eventUserInviteStatusTypeLabel", getNullOrValue(eventUserInviteStatusTypeLabel));

                String jsonEventUserString = jsonEventUserObject.toString();
                String response = request.post(url, jsonEventUserString);

                return response;
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                return "ERROR ENCOUNTERED. SEE ANDROID LOG.";
            }
            catch (JSONException jsone)
            {
                jsone.printStackTrace();
                return "ERROR ENCOUNTERED. SEE ANDROID LOG.";
            }
        }

        @Override
        protected void onPostExecute(String string) {
            pd.dismiss();
            stringCallback.done(string);

            super.onPostExecute(string);
        }
    }



    private class SetEventUser extends AsyncTask<Void, Void, String> {

        Integer eid;
        Integer uid;
        String eventUserTypeLabel;
        String eventUserInviteStatusTypeLabel;

        Map<String,Boolean> setOrNot = new HashMap<String,Boolean>();
        StringCallback stringCallback;

        private SetEventUser(
            Integer eid, Integer uid,
            String eventUserTypeLabel, String eventUserInviteStatusTypeLabel,
            Boolean[] setOrNot, StringCallback stringCallback) throws SetOrNotException
        {
            if (setOrNot.length != 4)
                throw new SetOrNotException("Incorrect quantity of booleans set in the SetOrNot array.");

            this.eid = eid;
            this.uid = uid;
            this.eventUserTypeLabel = eventUserTypeLabel;
            this.eventUserInviteStatusTypeLabel = eventUserInviteStatusTypeLabel;

            this.setOrNot.put("eid", null);
            this.setOrNot.put("uid", null);
            this.setOrNot.put("eventUserTypeLabel", setOrNot[2]);
            this.setOrNot.put("eventUserInviteStatusTypeLabel", setOrNot[3]);

            this.stringCallback = stringCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = httpConnection.getWebServerString() + "AndroidIO/EventUserRequest.php?function=setEventUser";

            try
            {
                JSONObject jsonSetOrNotObject = new JSONObject(setOrNot);

                JSONObject jsonImageObject = new JSONObject();
                jsonImageObject.put("eid", getNullOrValue(eid));
                jsonImageObject.put("uid", getNullOrValue(uid));
                jsonImageObject.put("eventUserTypeLabel", getNullOrValue(eventUserTypeLabel));
                jsonImageObject.put("eventUserInviteStatusTypeLabel", getNullOrValue(eventUserInviteStatusTypeLabel));
                jsonImageObject.put("setOrNot", jsonSetOrNotObject);

                String jsonImage = jsonImageObject.toString();
                Post request = new Post();
                String response = request.post(url, jsonImage);
                return response;
            }
            catch (IOException ioe)
            {
                return ioe.toString();
            }
            catch (JSONException jsone)
            {
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
