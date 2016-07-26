package dms.deideas.zas.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;

/**
 * Created by dmadmin on 30/06/2016.
 */
public class DetailMyOrderFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

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
    private TextView comments;
    private TextView incidents;
    private Button accept;
    private ImageButton btnincidencia;
    private ImageButton cancel;

    private int MAP_CONTROL = 0;

    private String questionStatus = "";

    private ImageView icRestaurantWhite;
    private ImageView icRestaurantRed;
    private TextView txtRestaurantWhite;
    private TextView txtRestaurantRed;

    private ImageView icRecogidoWhite;
    private ImageView icRecogidoRed;
    private TextView txtRecogidoWhite;
    private TextView txtRecogidoRed;

    private ImageView icFinishedWhite;
    private ImageView icFinishedRed;
    private TextView txtFinishedWhite;
    private TextView txtFinishedRed;


    public DetailMyOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_my_order_fragment, container, false);

        getActivity().setTitle(getArguments().getString("title"));
        findViewById(view);
        setValues();
        setOnClickListener();
        setVisibility();
        return view;
    }


    private void setValues() {
        id_order.setText(String.valueOf(order.getId()));
        restaurant_name.setText(order.getRestaurant().getName());
        restaurant_direction.setText(order.getRestaurant().getStreet());
        customer_name.setText(order.getShipping_address().getFirst_name() + " " + order.getShipping_address().getLast_name());
        hour_order.setText(order.getCreated_at().substring(11, 16) + "h");
        time_left_of_order.setText(order.getTimeKitchen() + " min");
        //hour_order_title.setText(order.getCreated_at().substring(11, 16) + "h");
        customer_direction.setText(order.getShipping_address().getAddress_1());
        // Set visibility of layout Incidents invisible if not is an order with problem
        /*if(order.getOrderstatus()!= "problem"){
            incidents.setVisibility(View.GONE);
        }*/
        /*else{
            incidents.setVisibility(View.VISIBLE);
        }*/
        if (order.getPayment_details().getPaid() == true) {
            state_of_payment.setText("PAGADO");
        } else {
            state_of_payment.setText("NO PAGADO");
        }
        acceptState();
    }

    private void findViewById(View view) {
        //Initiate Views
        id_order = (TextView) view.findViewById(R.id.idorder);
        restaurant_name = (TextView) view.findViewById(R.id.restaurant_name);
        time_left_of_order = (TextView) view.findViewById(R.id.time_left_of_order);
        restaurant_direction = (TextView) view.findViewById(R.id.restaurant_direction);
        phone_restaurant = (ImageView) view.findViewById(R.id.imgPhoneRestaurant);
        customer_name = (TextView) view.findViewById(R.id.customer_name);
        hour_order = (TextView) view.findViewById(R.id.hour_order);
        customer_direction = (TextView) view.findViewById(R.id.customer_direction);
        phone_customer = (ImageView) view.findViewById(R.id.imgPhoneCustomer);
        state_of_payment = (TextView) view.findViewById(R.id.state_of_payment);
        comments = (TextView) view.findViewById(R.id.comments);
        incidents = (TextView) view.findViewById(R.id.incidents);
        accept = (Button) view.findViewById(R.id.accept);
        cancel = (ImageButton) view.findViewById(R.id.cancel);
        btnincidencia = (ImageButton) view.findViewById(R.id.btnincidencia);

        icRestaurantWhite = (ImageView) view.findViewById(R.id.icRestaurantWhite);
        icRestaurantRed = (ImageView) view.findViewById(R.id.icRestaurantRed);
        txtRestaurantWhite = (TextView) view.findViewById(R.id.txtRestaurantWhite);
        txtRestaurantRed = (TextView) view.findViewById(R.id.txtRestaurantRed);

        icRecogidoWhite = (ImageView) view.findViewById(R.id.icRecogidoWhite);
        icRecogidoRed = (ImageView) view.findViewById(R.id.icRecogidoRed);
        txtRecogidoWhite = (TextView) view.findViewById(R.id.txtRecogidoWhite);
        txtRecogidoRed = (TextView) view.findViewById(R.id.txtRecogidoRed);


        icFinishedWhite = (ImageView) view.findViewById(R.id.icFinishedWhite);
        icFinishedRed = (ImageView) view.findViewById(R.id.icFinishedRed);
        txtFinishedWhite = (TextView) view.findViewById(R.id.txtFinishedWhite);
        txtFinishedRed = (TextView) view.findViewById(R.id.txtFinishedRed);

    }

    private void setOnClickListener() {
        restaurant_direction.setOnClickListener(this);
        phone_restaurant.setOnClickListener(this);
        customer_direction.setOnClickListener(this);
        phone_customer.setOnClickListener(this);
        comments.setOnClickListener(this);
        incidents.setOnClickListener(this);
        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        btnincidencia.setOnClickListener(this);
    }

    private void setVisibility() {
        String status = order.getOrderstatus();
        switch (status) {
            case Constants.ORDER_STATUS_driver_has_accepted:
            case Constants.ORDER_STATUS_driver_in_rest:
                cancel.setVisibility(View.INVISIBLE);
                btnincidencia.setVisibility(View.INVISIBLE);
                break;
            case Constants.ORDER_STATUS_driver_on_road:
            case Constants.ORDER_STATUS_order_delivered:
            case Constants.ORDER_STATUS_order_delivered_w_problem:
                cancel.setVisibility(View.INVISIBLE);
                btnincidencia.setVisibility(View.VISIBLE);
                break;
            case Constants.ORDER_STATUS_problem:
                cancel.setVisibility(View.VISIBLE);
                btnincidencia.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    private void acceptState() {

        String status = order.getOrderstatus();


        switch (status) {
            case Constants.ORDER_STATUS_driver_has_accepted :
                accept.setText(getResources().getString(R.string.driver_in_rest_status));
                questionStatus = getResources().getString(R.string.in_rest);

                break;
            case Constants.ORDER_STATUS_driver_in_rest:
                accept.setText(getResources().getString(R.string.driver_on_road_status));
                questionStatus = getResources().getString(R.string.order_collected);

                icRestaurantRed.setVisibility(View.GONE);
                txtRestaurantRed.setVisibility(View.GONE);
                icRestaurantWhite.setVisibility(View.VISIBLE);
                txtRestaurantWhite.setVisibility(View.VISIBLE);


                break;
            case Constants.ORDER_STATUS_driver_on_road:
                accept.setText(getResources().getString(R.string.order_delivered_status));
                questionStatus = getResources().getString(R.string.order_finished);

                icRecogidoRed.setVisibility(View.GONE);
                txtRecogidoRed.setVisibility(View.GONE);
                icRecogidoWhite.setVisibility(View.VISIBLE);
                txtRecogidoWhite.setVisibility(View.VISIBLE);

                //icFinished.setImageDrawable(getResources().getDrawable(R.drawable.ic_finalizado));

                break;

            default:
                break;
        }
    }

    public static DetailMyOrderFragment newInstance(String title, Order order) {
        Globals g = Globals.getInstance();

        g.setScreenCode(1);
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putSerializable("order", order);
        DetailMyOrderFragment fragment = new DetailMyOrderFragment();
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
        int idOrder = order.getId();
        ArrayList<Incidencia> lstIncidencias = (ArrayList<Incidencia>) order.getLista_incidencias();
        if (v == restaurant_direction) {

            String title = order.getRestaurant().getName();
            MAP_CONTROL = 0;
            MapFragment fragment = MapFragment.newInstance(title);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            fragment.getMapAsync(this);

            getActivity().setTitle(title);
        } else if (v == customer_direction) {
            String title = order.getShipping_address().getFirst_name() + " " + order.getShipping_address().getLast_name();
            MAP_CONTROL = 1;
            MapFragment fragment = MapFragment.newInstance(title);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            fragment.getMapAsync(this);

            getActivity().setTitle(title);
        } else if (v == phone_restaurant) {
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
        } else if (v == comments) {
            String title = getResources().getString(R.string.comments);
            OrderNoteFragment fragment = OrderNoteFragment.newInstance(title, idOrder);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            getActivity().setTitle(title);
        } else if (v == incidents) {
            String title = getResources().getString(R.string.incidents);
            OrderIncidenciasFragment fragment = OrderIncidenciasFragment.newInstance(title, order);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            getActivity().setTitle(title);
        } else if (v == accept) {
            Toast.makeText(getContext(), "PULSADO:"+questionStatus, Toast.LENGTH_SHORT).show();
            DialogFragment dialogFragment = DialogFragment.newInstance(questionStatus, order);
            dialogFragment.show(getFragmentManager(), "dialog");
        } else if (v == cancel) {

            String title = getResources().getString(R.string.myorders_sb_title);
            MyOrdersFragment fragment = MyOrdersFragment.newInstance(title, order.getMotodriver());
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();


            getActivity().setTitle(title);

        } else if (v == btnincidencia) {
            String title = getResources().getString(R.string.incidents);
            OrderIncidenciasFragment fragment = OrderIncidenciasFragment.newInstance(title, order);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

            getActivity().setTitle(title);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        /*double latitude=0.0, longitude = 0.0;

        Geocoder geocoder = new Geocoder (getContext());
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

        double latitude = 0.0, longitude = 0.0;
        String title = null;

        // Restaurant Map called
        if (MAP_CONTROL == 0) {

            /*latitude = Double.parseDouble(order.getRestaurant().getData_map().getLat());
            longitude = Double.parseDouble(order.getRestaurant().getData_map().getLng());*/
            latitude = 41.383150;
            longitude = 2.134603;
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


}