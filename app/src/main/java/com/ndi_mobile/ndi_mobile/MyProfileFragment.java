package com.ndi_mobile.ndi_mobile;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;


public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TOKEN = "token";

    // TODO: Rename and change types of parameters
    private String token;

    private View fragmentMyProfileView;
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mAdapter;

    private PingTask pingTask=null;
    private TextView email;
    private TextView name;
    private TextView lastname;
    private TextView number;
    private ImageView profilePictureView;

    private String id=null;


    public static MyProfileFragment newInstance(String token) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(ARG_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMyProfileView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        name = (TextView) fragmentMyProfileView.findViewById((R.id.firstname));
        lastname = (TextView) fragmentMyProfileView.findViewById((R.id.lastname));
        email = (TextView) fragmentMyProfileView.findViewById((R.id.email));
        number = (TextView) fragmentMyProfileView.findViewById((R.id.number));

        //getAndFillUserInfo();

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

        mRecyclerView = (RecyclerView) fragmentMyProfileView.findViewById(R.id.list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final ArrayList<String[]> contacts = new ArrayList<String[]>();
        contacts.add(new String[]{"Joris", "5 minutes", "KO"});
        contacts.add(new String[]{"Fadoua", "10 minutes", "OK"});
        contacts.add(new String[]{"Hugo", "12 minutes", "OK"});
        contacts.add(new String[]{"Stephane", "12 minutes", "OK"});
        contacts.add(new String[]{"Antoine", "15 minutes", "WARNING"});
        contacts.add(new String[]{"Steve", "20 minutes", "OK"});

        mAdapter = new TimeLineAdapter(contacts, R.layout.row_timeline, getActivity());
        mRecyclerView.setAdapter(mAdapter);



        return fragmentMyProfileView;
    }

    //Todo
    private void getAndFillUserInfo() {
        GetUserInfoTask getUserInfoTask = new GetUserInfoTask();
        getUserInfoTask.execute((Void) null);
    }

    private void okButtonAction() {
        if(pingTask==null && id!=null) {
            pingTask = new PingTask("ok");
            pingTask.execute((Void) null);
        }
    }

    private void koButtonAction() {
       if(pingTask==null && id!=null) {
           pingTask = new PingTask("ko");
           pingTask.execute((Void) null);
       }
    }

    private void warningButtonAction() {
        if(pingTask==null && id!=null){
            pingTask = new PingTask("warning");
            pingTask.execute((Void) null);
        }
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
    public class GetUserInfoTask extends AsyncTask<Void, Void, Boolean> {

        private JSONParser jsonParser = new JSONParser();
        private JSONObject json;

        private String mFirstname;
        private String mLastname;
        private String mNumber;
        private String mEmail;

        GetUserInfoTask() {
        }

        @Override
        protected Boolean doInBackground(Void... param) {

            boolean boolSuccess = false;
            String URL = "http://localhost:9000/user/me";
            JSONObject jParam = new JSONObject();
            try {


                json = jsonParser.makeHttpRequest(URL, "GET", token, jParam);
                try {

                    //TODO modifier quand succes mis a jour coté serveur
                    System.out.println(json.toString());
                    id = json.getString("_id");
                    mFirstname = json.getString("first_name");
                    mLastname = json.getString("last_name");
                    mEmail = json.getString("email");
                    boolSuccess = true;

                    try {
                        mNumber = json.getString("number");
                    }
                    catch (Exception e){
                        mNumber = null;
                    }
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
            name.setText(mFirstname);
            lastname.setText(mLastname);
            email.setText(mEmail);
            if(mNumber!=null){
                number.setText(mNumber);
            }
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
            String URL = "http://localhost:9000/api/pings";
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("user", id);
                jParam.put("status", mPingStatus);
                jParam.put("localisation", "{\"latitude\":\"48.583\",\"longitude\":\"7.75\"}");

                json = jsonParser.makeHttpRequest(URL, "POST", token, jParam);
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
