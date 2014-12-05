package com.ndi_mobile.ndi_mobile;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DangerZonesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DangerZonesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DangerZonesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private WebView webView;
    private double longitude;
    private double latitude;

    // TODO: Rename and change types of parameters
    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DangerZonesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DangerZonesFragment newInstance(int sectionNumber) {
        DangerZonesFragment fragment = new DangerZonesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DangerZonesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentDangerZonesView = inflater.inflate(R.layout.fragment_danger_zones, container, false);

        webView = (WebView) fragmentDangerZonesView.findViewById(R.id.webview_frame);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        String url = "file:///android_asset/map.html";
        //Used to obtain permission to disclose the user's location to JavaScript.

            /*webView.setWebChromeClient(new WebChromeClient() {
                public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
                    callback.invoke(origin, true, false);
                }
            });*/


        /*LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);


        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extra)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Toast.makeText(getActivity().getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider)
            {
                Toast.makeText( getActivity().getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
            }

        };

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);*/

        latitude = 48.583;
        longitude = 7.75;

        final double[] markerLong = {7.2, 7};
        final double[] markerLat = {47, 46};

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                webView.loadUrl("javascript:initialize("+ latitude + ", "+ longitude + ", "+ java.util.Arrays.toString(markerLat) + ", "+ java.util.Arrays.toString(markerLong) +")");
                webView.loadUrl("javascript:go()");
            }
        });


        webView.setBackgroundColor(0xFFFFFFFF);

        return fragmentDangerZonesView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
