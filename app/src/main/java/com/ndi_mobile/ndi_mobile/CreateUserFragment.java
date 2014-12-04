package com.ndi_mobile.ndi_mobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ndi_mobile.ndi_mobile.utils.JSONParser;

import org.json.JSONObject;


/**
 * A simple {@link android.app.Fragment} subclass that allow to create new User.
 * Activities that contain this fragment must implement the
 * {@link CreateUserFragment.CreateUserFragmentListener} interface
 * to handle interaction events.
 * Use the {@link CreateUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateUserFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CreateUserTask mCreateUserTask = null;

    private View mProgressView;
    private View mCreateUserButtonView;

    private CreateUserFragmentListener mListener;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText adresseEditText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateUserFragment.
     */
    public static CreateUserFragment newInstance() {
        CreateUserFragment fragment = new CreateUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CreateUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentCreateUserView = inflater.inflate(R.layout.fragment_create_user, container, false);

        mProgressView = (View) fragmentCreateUserView.findViewById(R.id.create_user_progress);

        mCreateUserButtonView = (View) fragmentCreateUserView.findViewById(R.id.create_user_button);

        usernameEditText = (EditText) fragmentCreateUserView.findViewById((R.id.username));
        passwordEditText = (EditText) fragmentCreateUserView.findViewById((R.id.password));
        emailEditText = (EditText) fragmentCreateUserView.findViewById((R.id.email));
        phoneNumberEditText = (EditText) fragmentCreateUserView.findViewById((R.id.phone_number));
        adresseEditText = (EditText) fragmentCreateUserView.findViewById((R.id.adresse));

        Button mUsernameSignInButton = (Button) fragmentCreateUserView.findViewById(R.id.create_user_button);
        mUsernameSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateUser();
            }
        });

        return fragmentCreateUserView;
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptCreateUser() {
        if (mCreateUserTask != null) {
            return;
        }

        // Reset errors.
        usernameEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String adresse = adresseEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid Username
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("error username");
            focusView = usernameEditText;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("error password");
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("error email");
            focusView = emailEditText;
            cancel = true;
        }

        // Check for a valid phoneNumber
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("error phoneNumber");
            focusView = phoneNumberEditText;
            cancel = true;
        }

        // Check for a valid adresse
        if (TextUtils.isEmpty(adresse)) {
            adresseEditText.setError("error adresse");
            focusView = adresseEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mCreateUserTask = new CreateUserTask(username, password, email, phoneNumber, adresse);
            mCreateUserTask.execute((Void) null);
        }
    }

    public void onCreateUserButtonPressed(String username) {
        if (mListener != null) {
            mListener.onCreateUserFragmentInteraction(username);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CreateUserFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CreateUserFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface CreateUserFragmentListener {
        public void onCreateUserFragmentInteraction(String username);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCreateUserButtonView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCreateUserButtonView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCreateUserButtonView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCreateUserButtonView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class CreateUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final String mEmail;
        private final String mPhoneNumber;
        private final String mAdresse;

        private JSONParser jsonParser = new JSONParser();
        private JSONObject json;

        CreateUserTask(String username, String password, String email, String phoneNumber, String adresse) {
            mUsername = username;
            mPassword = password;
            mEmail = email;
            mPhoneNumber = phoneNumber;
            mAdresse = adresse;
        }

        @Override
        protected Boolean doInBackground(Void... param) {

            boolean boolSuccess = false;
            String URL = "http://aftersoon.herokuapp.com/user";
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("username", mUsername);
                jParam.put("password", mPassword);
                jParam.put("email", mEmail);
                jParam.put("adresse",mAdresse);
                jParam.put("telephone",mPhoneNumber);

                json = jsonParser.makeHttpRequest(URL, "POST", jParam);
                try {

                    //TODO modifier quand succes mis a jour coté serveur
                    System.out.println(json.toString());

                    boolSuccess = json.getString("status").equals("OK");

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
            mCreateUserTask = null;
            showProgress(false);

            if (success) {
                onCreateUserButtonPressed(usernameEditText.getText().toString());
            } else {
                // TODO : Gerer erreur avec retour serveur succes
            }
        }


        @Override
        protected void onCancelled() {
            mCreateUserTask = null;
            showProgress(false);
        }
    }
}
