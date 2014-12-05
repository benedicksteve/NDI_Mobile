package com.ndi_mobile.ndi_mobile;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ndi_mobile.ndi_mobile.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String token;

    private PingTask pingTask=null;
    private TextView email;
    private TextView name;
    private TextView lastname;
    private TextView number;
    private ImageView profilePictureView;

    private String id;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentMyProfileView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        name = (TextView) fragmentMyProfileView.findViewById((R.id.firstname));
        lastname = (TextView) fragmentMyProfileView.findViewById((R.id.lastname));
        email = (TextView) fragmentMyProfileView.findViewById((R.id.email));
        number = (TextView) fragmentMyProfileView.findViewById((R.id.number));

        getAndFillUserInfo();

        ImageButton mOkButton = (ImageButton) fragmentMyProfileView.findViewById(R.id.okButton);
            mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okButtonAction();
            }
        });

        ImageButton mKoButton = (ImageButton) fragmentMyProfileView.findViewById(R.id.koButton);
        mKoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koButtonAction();
            }
        });

        ImageButton mWarningButton = (ImageButton) fragmentMyProfileView.findViewById(R.id.warningButton);
        mWarningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningButtonAction();
            }
        });

        return fragmentMyProfileView;
    }

    //Todo
    private void getAndFillUserInfo() {

    }

    private void okButtonAction() {
        if(pingTask==null)
            pingTask = new PingTask("ok");
    }

    private void koButtonAction() {
       if(pingTask==null)
            pingTask = new PingTask("ko");
    }

    private void warningButtonAction() {
        if(pingTask==null)
            pingTask = new PingTask("warning");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class getUserInfoTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPingStatus;

        private JSONParser jsonParser = new JSONParser();
        private JSONObject json;

        PingTask(String pingStatus) {
            mPingStatus = pingStatus;
        }

        @Override
        protected Boolean doInBackground(Void... param) {

            boolean boolSuccess = false;
            String URL = "http://localhost:9000/?";
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("user", id);
                jParam.put("status", mPingStatus);
                jParam.put("localisation", "{\"latitude\":\"48.583\",\"longitude\":\"7.75\"}");

                json = jsonParser.makeHttpRequest(URL, "POST", null, jParam);
                try {

                    //TODO modifier quand succes mis a jour coté serveur
                    System.out.println(json.toString());

                    boolSuccess = true;

                } catch (Exception e) {
                    System.out.println("erreur 1");
                    boolSuccess = false;
                }
            } catch (Exception e1) {
                System.out.println("erreur 2");

                boolSuccess = false;
            }
            return boolSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pingTask = null;
        }


        @Override
        protected void onCancelled() {
            pingTask = null;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class PingTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPingStatus;

        private JSONParser jsonParser = new JSONParser();
        private JSONObject json;

        PingTask(String pingStatus) {
            mPingStatus = pingStatus;
        }

        @Override
        protected Boolean doInBackground(Void... param) {

            boolean boolSuccess = false;
            String URL = "http://localhost:9000/?";
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("user", id);
                jParam.put("status", mPingStatus);
                jParam.put("localisation", "{\"latitude\":\"48.583\",\"longitude\":\"7.75\"}");

                json = jsonParser.makeHttpRequest(URL, "POST", null, jParam);
                try {

                    //TODO modifier quand succes mis a jour coté serveur
                    System.out.println(json.toString());

                    boolSuccess = true;

                } catch (Exception e) {
                    System.out.println("erreur 1");
                    boolSuccess = false;
                }
            } catch (Exception e1) {
                System.out.println("erreur 2");

                boolSuccess = false;
            }
            return boolSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pingTask = null;
        }


        @Override
        protected void onCancelled() {
            pingTask = null;
        }
    }
}
