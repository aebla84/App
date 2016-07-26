package dms.deideas.zas.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import dms.deideas.zas.R;


/**
 * Created by dmadmin on 31/05/2016.
 */

/**
 * Fragmento Home
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageButton order;
    private ImageButton myorder;


    public static HomeFragment newInstance(String title) {
        HomeFragment fragment = new HomeFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        fragment.setArguments(b);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        getActivity().setTitle(getArguments().getString("title"));

        setUpView(view);

        return view;
    }

    private void setUpView(View view) {

        order = (ImageButton) view.findViewById(R.id.order);
        myorder = (ImageButton) view.findViewById(R.id.myorder);
        order.setOnClickListener(this);
        myorder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == order) {

            String title = getResources().getString(R.string.orders_sb_title);
            OrdersFragment fragment = OrdersFragment.newInstance(title);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();
            getActivity().setTitle(title);


        } else {
            String title = getResources().getString(R.string.myorders_sb_title);
            int idUser = 0;


            SharedPreferences prefs =
                    this.getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            idUser = prefs.getInt("idUser",0);
            String motodriver = Integer.toString(idUser);

            MyOrdersFragment fragment = MyOrdersFragment.newInstance(title, motodriver);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            getActivity().setTitle(title);

        }
    }
}
