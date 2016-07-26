package dms.deideas.zas.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import dms.deideas.zas.Model.Order;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends SupportMapFragment {

    private Order order;

    public MapFragment() {
    }

    public static MapFragment newInstance(String title) {

        Bundle b = new Bundle();
        b.putString("title", title);

        MapFragment fragment = new MapFragment();

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        getActivity().setTitle(getArguments().getString("title"));


        return root;
    }
}
