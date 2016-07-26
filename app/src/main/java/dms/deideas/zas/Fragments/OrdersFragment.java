package dms.deideas.zas.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import dms.deideas.zas.Adapters.OrderAdapter;
import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Elements;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.row;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.DistanceSearch;
import dms.deideas.zas.Services.DistanceService;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmadmin on 01/06/2016.
 */
public class OrdersFragment extends Fragment implements RetrofitDelegateHelper.AlRecibirListaDelegate, OrderAdapter.OrderListener, LocationListener, RetrofitDelegateHelper.listaRecibidaOrdenada {

    private String title;

    private RecyclerView recycler;

    private OrderAdapter adapter;

    private LocationManager locationManager;
    private String provider;

    private double latitudeUser;
    private double longitudeuser;

    // Inicializar el layout manager
    private LinearLayoutManager layout;

    private RetrofitDelegateHelper restHelperOrders;
    private RetrofitDelegateHelper restHelperProblems;

    private View view;

    private Retrofit retrofit;
    private DistanceService distanceService;

    private OrderSearch ordersearchObje;


    private class ObjOrderDataLocation {
        public int idOrder;
        public String latitud;
        public String longitud;
        public int distance;
        public String duration;
        public int minutesMotoDriverPickupInRestaurant;

        public int getIdOrder() {
            return idOrder;
        }

        public void setIdOrder(int idOrder) {
            this.idOrder = idOrder;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }
    }

    public ObjOrderDataLocation obj = new ObjOrderDataLocation();
    public List<ObjOrderDataLocation> lstObjDataLocation = new ArrayList<ObjOrderDataLocation>();
    private List<String[]> lstDestination = new ArrayList<String[]>();

    public OrdersFragment() {
    }

    public static OrdersFragment newInstance(String title) {
        Bundle b = new Bundle();
        b.putString("title", title);
        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_orders, container, false);

        getActivity().setTitle(getArguments().getString("title"));

        recycler = (RecyclerView) view.findViewById(R.id.recycle);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);

        return view;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Title "Pedidos"
        title = getArguments().getString("title");
        //Get position user - Latitud and longitud in this moment.
        getpositionUser();
        //Init OrderAdapter
        adapter = new OrderAdapter(this);
        //Get List of orders (Orders Accepted by restaurant and Orders with problem - Without motodriver)
        getOrders();
    }

    @Override
    public void onStart() {
        super.onResume();
    }

    private void delegateHelper(int id) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        switch (id) {
            case Constants.SERVICE_CODE_order_accepted://rest_has_accepted
                restHelperOrders = new RetrofitDelegateHelper(0, 0);
                restHelperOrders.getOrdersAccepted(this);
                break;
            case Constants.SERVICE_CODE_order_problem://incidencias
                restHelperProblems = new RetrofitDelegateHelper(0, 0);
                restHelperProblems.getOrdersProblem(this);
                break;
            default:
                break;
        }
    }


    @Override
    public void listaRecibida(OrderSearch body) {
        //If we receive orders correctly
        if (body != null && body.getOrders() != null) {
            adapter.add(body.getOrders());

        }
    }

    @Override
    public void listaRecibidaOrdenada(OrderSearch body) {


        //If we receive orders correctly
        if (body != null && body.getOrders() != null) {
            // Si son las aceptadas --> vamos a crearnos un objeto objOrderDataLocation que contenga el idOrder y la lat y la long. Y un array con dichos datos.
            for (Order order : body.getOrders()) {
                ObjOrderDataLocation obj1 = new ObjOrderDataLocation();
                obj1.idOrder = order.getId();
                obj1.minutesMotoDriverPickupInRestaurant = order.getMinutesMotoDriverPickupInRestaurant();
                obj1.latitud = order.getRestaurant().getData_map().getLat();
                obj1.longitud = order.getRestaurant().getData_map().getLng();
                String[] strArray = new String[]{obj1.latitud, obj1.longitud};
                lstDestination.add(strArray);
                lstObjDataLocation.add(obj1);

            }
            //Convert lstDestination in a String to send Google.
            String strlstDestination = "";
            if (lstDestination.size() > 0) {
                for (String[] locRestaurant : lstDestination
                        ) {
                    if (lstDestination.indexOf(locRestaurant) == 0) {
                        strlstDestination = locRestaurant[0].toString() + "," + locRestaurant[1].toString();
                    } else {
                        strlstDestination = strlstDestination + "|" + locRestaurant[0].toString() + "," + locRestaurant[1].toString();
                    }
                }
            }

            ordersearchObje = body;
            //Llamamos a calculateDistance pasandole un array de destinos. Y si nos devuelve datos rellenamos objOrderDataLocation con distancia y duracion
            calculateDistance(strlstDestination,ordersearchObje);
            //Vamos leyendo la lista de ordenes y si hay algunas con el mismo tiempo , comparamos los km en la  otra lista.
            //Si la lista tiene alguno con el mismo tiempo se ordena por distancia


            //addAdapter(body);


        }
    }


    @Override
    public void errorRecibido(Object error) {
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onOrderClicked(View card, Order order) {

        String title = getResources().getString(R.string.order_id_item) + " : " + String.valueOf(order.getId());

        DetailOrderFragment fragment = DetailOrderFragment.newInstance(title, order);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();


        getActivity().setTitle(title);
    }


    private void getpositionUser() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(getContext(), "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.latitudeUser = (double) (location.getLatitude());
        this.longitudeuser = (double) (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void calculateDistance(String lstDestination, final OrderSearch osBody) {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/distancematrix/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        distanceService = retrofit.create(DistanceService.class);
        distanceService.getdistance(latitudeUser + "," + longitudeuser, lstDestination, "AIzaSyDuV9LxbQDRp4cXRQO523wKhxW7BPCRASk").enqueue(new Callback<DistanceSearch>() {
            @Override
            public void onResponse(Call<DistanceSearch> call, Response<DistanceSearch> response) {
                if (response.isSuccessful()) {
                    Log.d("URL GET Distance: ", response.raw().request().url().toString());
                    Log.d("CODE Distance: ", String.valueOf(response.code()));
                    List<String> lstdistancias = new ArrayList<String>();
                    //Rellenamos el objeto ObjDistOrder con la distancia y el tiempo recibido
                    if (response.body() != null && response.body().getRows() != null) {
                        row rowelement = response.body().getRows().get(0);
                        for (Elements el : rowelement.getElements()
                                ) {
                            //Obtenemos la distancia y la duracion y la ponemos en el obj

                            ObjOrderDataLocation obj = lstObjDataLocation.get(rowelement.getElements().indexOf(el));
                            obj.duration = el.getDuration().getText();
                            obj.distance = el.getDistance().getValue();
                            lstObjDataLocation.get(rowelement.getElements().indexOf(el)).duration = obj.duration;
                            lstObjDataLocation.get(rowelement.getElements().indexOf(el)).distance = obj.distance;
                        }

                    }

                    addAdapter(osBody);

                } else {

                    Log.d("error: ", String.valueOf(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<DistanceSearch> call, Throwable t) {

                Log.d("Error", call.request().url().toString());
            }
        });




    }

    public void getOrders() {
        // Indicates the service call
        Globals g = Globals.getInstance();

        // Get orders problems
        g.setServiceCode(4);
        try {
            delegateHelper(g.getServiceCode());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


        // Get orders accepted
        g.setServiceCode(2);
        try {
            delegateHelper(g.getServiceCode());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }


    private void addAdapter(OrderSearch body){
        adapter.setComparador(new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {
                //minutesMotoDriverPickupInRestaurant;

                int timeL = lhs.getMinutesMotoDriverPickupInRestaurant();
                int timeR = rhs.getMinutesMotoDriverPickupInRestaurant();

                int distanceL = 0;
                int distanceR = 0;

                if (timeL == timeR) {
                    int idL = lhs.getId();
                    int idR = rhs.getId();

                    Toast.makeText(getContext(), "HORAS IGUALES", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < lstObjDataLocation.size(); i++) {
                        Toast.makeText(getContext(), "ORDER OBJETO:" + lstObjDataLocation.get(i).getIdOrder(), Toast.LENGTH_SHORT).show();

                        if (lstObjDataLocation.get(i).getIdOrder() == idL) {
                            distanceL = lstObjDataLocation.get(i).getDistance();

                        }
                        if (lstObjDataLocation.get(i).getIdOrder() == idR) {
                            distanceR = lstObjDataLocation.get(i).getDistance();
                        }
                    }

                    Toast.makeText(getContext(), "distanceL:" + distanceL, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "distanceR:" + distanceR, Toast.LENGTH_SHORT).show();


                    if (distanceL == distanceR) {
                        return 0;
                    }
                    if (distanceL < distanceR) {
                        return -1;
                    }
                    if (distanceL > distanceR) {
                        return 1;
                    }

                    return 0;
                }

                return 0;
            }
        });

        //Las añadimos en el adapter.
        adapter.add(body.getOrders());
    }

}


