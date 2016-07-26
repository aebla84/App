package dms.deideas.zas.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dms.deideas.zas.R;

/**
 * Created by dmadmin on 31/05/2016.
 */
public class ProfileFragment extends Fragment {

    private String title;

    private TextView state;
    private TextView name;
    private TextView email;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String title) {

        Bundle b = new Bundle();
        b.putString("title", title);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        getActivity().setTitle(getArguments().getString("title"));

        state = (TextView) view.findViewById(R.id.state);
        name = (TextView) view.findViewById(R.id.name);
        email = (TextView) view.findViewById(R.id.email);

        final SharedPreferences prefs = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String names =  prefs.getString("name","");

        Log.d("NAME: ", names);

        return view;
    }

    @Override
    public void onStart() {

        super.onResume();

        final SharedPreferences prefs = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        //state.setText();
        name.setText(prefs.getString("name", ""));
        email.setText(prefs.getString("email", ""));
    }
}
