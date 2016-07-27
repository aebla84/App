package dms.deideas.zas.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

import dms.deideas.zas.Adapters.HistoricalAdapter;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;

/**
 * Created by dmadmin on 31/05/2016.
 */


public class HistoricalFragment extends Fragment implements View.OnClickListener, RetrofitDelegateHelper.AlRecibirListaDelegate, HistoricalAdapter.HistoricalListener{

    private String title;
    private String motodriver;

    private RecyclerView recycler;

    private HistoricalAdapter adapter;

    // Inicializar el layout manager
    private LinearLayoutManager layout;

    private RetrofitDelegateHelper restHelper;

    private View view;

    public HistoricalFragment() {

    }


    public static HistoricalFragment newInstance(String title, String motodriver) {

        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("motodriver", motodriver);

        HistoricalFragment fragment = new HistoricalFragment();
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.historical_fragment, container, false);

        getActivity().setTitle(getArguments().getString("title"));

        recycler = (RecyclerView) view.findViewById(R.id.recycle);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        motodriver = getArguments().getString("motodriver");

        adapter = new HistoricalAdapter(this);
        adapter.setComparador(new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {

                String statusL = lhs.getOrderstatus();
                String statusR = rhs.getOrderstatus();

                if (statusL.equalsIgnoreCase(statusR)) {
                    return 0;
                }
                if (statusL.equalsIgnoreCase("problem")) {
                    return -1;
                }
                if (statusR.equalsIgnoreCase("problem")) {
                    return 1;
                }

                return 0;
            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onResume();
        final SharedPreferences prefs
                = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        int idUser = prefs.getInt("idUser", 0);


        // Indicates the service call
        Globals g = Globals.getInstance();
        g.setServiceCode(11);
        try {
            restHelper = new RetrofitDelegateHelper(0, idUser);
            restHelper.getOrderHistorical(this);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listaRecibida(OrderSearch body) {

        if (body!=null && body.getOrders() != null) {
            adapter.add(body.getOrders());
        }
        else
        {
            int lengthLong = Toast.LENGTH_LONG;
            Toast.makeText(view.getContext(),"NO HAY FINALIZADOS", lengthLong);
        }

    }



    @Override
    public void errorRecibido(Object error) {
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onOrderClicked(View card, Order order) {

        String title = getResources().getString(R.string.order_id_item) + " : " + String.valueOf(order.getId());

        DetailMyOrderFragment fragment = DetailMyOrderFragment.newInstance(title, order);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();


        getActivity().setTitle(title);

    }
}
