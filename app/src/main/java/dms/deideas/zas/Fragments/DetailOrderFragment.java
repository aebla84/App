package dms.deideas.zas.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;

/**
 * Created by dmadmin on 14/06/2016.
 */
public class DetailOrderFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private Order order;

    private TextView id_order;
    private TextView restaurant_name;
    private TextView time_left_of_order;
    private TextView restaurant_direction;
    private ImageView phone_restaurant;
    private TextView customer_name;
    private TextView hour_order;
    private TextView customer_direction;
    private ImageView phone_customer;
    private TextView state_of_payment;
    private TextView observation;
    private Button accept;
    private Button cancel;
    private RelativeLayout rellay_comments;

    private int MAP_CONTROL = 0;

    private List<String> notes;

    private Globals g = Globals.getInstance();

    public DetailOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_order_fragment, container, false);

        getActivity().setTitle(getArguments().getString("title"));

        //Initiate Views
        id_order = (TextView) view.findViewById(R.id.idorder);
        restaurant_name = (TextView) view.findViewById(R.id.restaurant_name);
        time_left_of_order = (TextView) view.findViewById(R.id.time_left_of_order);
        restaurant_direction = (TextView) view.findViewById(R.id.restaurant_direction);
        //phone_restaurant = (ImageView) view.findViewById(R.id.imgPhoneRestaurant);
        customer_name = (TextView) view.findViewById(R.id.customer_name);
        hour_order = (TextView) view.findViewById(R.id.hour_order);
        customer_direction = (TextView) view.findViewById(R.id.customer_direction);
        //phone_customer = (ImageView) view.findViewById(R.id.imgPhoneCustomer);
        state_of_payment = (TextView) view.findViewById(R.id.state_of_payment);
        accept = (Button) view.findViewById(R.id.accept);
        cancel = (Button) view.findViewById(R.id.cancel);
        rellay_comments = (RelativeLayout) view.findViewById(R.id.relLay_comments);
        setValues();

        restaurant_direction.setOnClickListener(this);
        //phone_restaurant.setOnClickListener(this);
        customer_direction.setOnClickListener(this);
        //phone_customer.setOnClickListener(this);
        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        rellay_comments.setOnClickListener(this);
        return view;
    }

    private void setValues() {
        id_order.setText(String.valueOf(order.getId()));
        restaurant_name.setText(order.getRestaurant().getName());
        time_left_of_order.setText(order.getTimeKitchen() + " min");
        restaurant_direction.setText(order.getRestaurant().getStreet());
        customer_name.setText(order.getShipping_address().getFirst_name() + " " + order.getShipping_address().getLast_name());
        hour_order.setText(order.getCreated_at().substring(11, 16));
        customer_direction.setText(order.getShipping_address().getAddress_1());
        if (order.getPayment_details().getPaid() == true) {
            state_of_payment.setText("PAGADO");
        } else {
            state_of_payment.setText("NO PAGADO");
        }
       // observation.setText(order.getNote());


    }

    public static DetailOrderFragment newInstance(String title, Order order) {
        Globals g = Globals.getInstance();
        g.setScreenCode(0);
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putSerializable("order", order);
        DetailOrderFragment fragment = new DetailOrderFragment();
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = (Order) getArguments().getSerializable("order");
    }

    @Override
    public void onClick(View v) {
        /*if (v == restaurant_direction) {

            String title = order.getRestaurant().getName();
            MAP_CONTROL = 0;
            MapFragment fragment = MapFragment.newInstance();
            loadNextFragment(fragment,title);
            fragment.getMapAsync(this);
        } else if (v == customer_direction) {
            String title = order.getShipping_address().getFirst_name() + " " + order.getShipping_address().getLast_name();
            MAP_CONTROL = 1;
            MapFragment fragment = MapFragment.newInstance();
            loadNextFragment(fragment,title);
            fragment.getMapAsync(this);

        }*/
        /*if (v == phone_restaurant) {
            String posted_by = order.getRestaurant().getPhone();
            String uri = "tel:" + posted_by.trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } else if (v == phone_customer) {
            String posted_by = "666777888";
            String uri = "tel:" + posted_by.trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }*/
        if(v==rellay_comments){
            String title = getResources().getString(R.string.comments);
            OrderNoteFragment fragment = OrderNoteFragment.newInstance(title,order.getId());
            loadNextFragment(fragment,title);
        }
        else if (v == accept) {
            DialogFragment dialogFragment = DialogFragment.newInstance(getResources().getString(R.string.accept_order), order);
            dialogFragment.show(getFragmentManager(), "dialog");
            //accept.setText(getResources().getString(R.string.income));
            g.setServiceCode(1);

        } else if (v == cancel) {

            String title = getResources().getString(R.string.orders_sb_title);
            OrdersFragment fragment = OrdersFragment.newInstance(title);
            loadNextFragment(fragment,title);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        double latitude=0.0, longitude = 0.0;

       /* Geocoder geocoder = new Geocoder (getContext());
        List<Address> addresses;
        try {
            addresses= geocoder.getFromLocationName("Avenida Madrid,nº 192, Barcelona, españa", 1);
            if(addresses.size()>0){
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*double latitude = 0.0, longitude = 0.0;*/
        String title = null;

        // Restaurant Map called
        if (MAP_CONTROL == 0) {

            latitude = Double.parseDouble(order.getRestaurant().getData_map().getLat());
            longitude = Double.parseDouble(order.getRestaurant().getData_map().getLng());
            /*latitude = 41.383150;
            longitude = 2.134603;*/
            title = order.getRestaurant().getName();
        }
        // Customer Map called
        else if (MAP_CONTROL == 1) {
            latitude = 41.383150;
            longitude = 2.134603;
            title = order.getShipping_address().getFirst_name() + " " + order.getShipping_address().getLast_name();
        }

        LatLng cali = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title(title));
        marker.showInfoWindow();

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(cali)
                .zoom(15)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void loadNextFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();

        getActivity().setTitle(title);
    }
}
