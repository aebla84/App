package dms.deideas.zas.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dms.deideas.zas.Adapters.OrderAdapter;
import dms.deideas.zas.Adapters.OrderListIncidenciasAdapter;
import dms.deideas.zas.Adapters.OrderNoteAdapter;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;

/**
 * Created by bnavarro on 12/07/2016.
 */
public class OrderIncidenciasFragment extends Fragment implements RetrofitDelegateHelper.AlRecibirListaDelegate {

    private RetrofitDelegateHelper restHelper;
    private String title;

    private int id_order = 0;
    private String typeOfProblem;
    private Spinner sp_typeOfProblem;
    private String strTypeOfProblem;
    private View view;
    private View view2;
    private ImageButton FAB;
    private ImageButton btnSaveComment;
    private EditText et_addcomment;
    private LinearLayout linearLayout_editcomment;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private Incidencia problem_details;
    private TextView tv_nocomments;

    public OrderIncidenciasFragment() {
        // Required empty public constructor
    }

    // Inicializar Incidencias
    private ArrayList<Incidencia> lstincidencias;

    // Inicializar el layout manager
    private LinearLayoutManager layout;


    public static OrderIncidenciasFragment newInstance(String title, Order order) {
        OrderIncidenciasFragment fragment = new OrderIncidenciasFragment();

        Bundle b = new Bundle();
        b.putString("title", title);
        b.putInt("idOrder", order.getId());
        ArrayList<Incidencia> lstIncidencias = (ArrayList<Incidencia>) order.getLista_incidencias();
        b.putParcelableArrayList("lstincidencias", lstIncidencias);
        order = order;
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_order = getArguments().getInt("idOrder");
            title = getArguments().getString("title");
            lstincidencias = getArguments().getParcelableArrayList("lstincidencias");
            typeOfProblem = getArguments().getString("typeOfProblem");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.incidencias_orders_fragment, container, false);
        tv_nocomments = (TextView)view.findViewById(R.id.tv_nocomments);
        getActivity().setTitle(title);


        FAB = (ImageButton)view.findViewById(R.id.fab_addcomment);
        linearLayout_editcomment = (LinearLayout)view.findViewById(R.id.send_message);
        et_addcomment = (EditText)view.findViewById(R.id.write_comment);
        btnSaveComment = (ImageButton)view.findViewById(R.id.send_comment);
        sp_typeOfProblem = (Spinner)view.getRootView().findViewById(R.id.problems_spinner);
        strTypeOfProblem = String.valueOf(sp_typeOfProblem.getSelectedItem());


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(view.getContext(),
                R.array.problems_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp_typeOfProblem.setAdapter(adapter2);

        loadIncidencias(lstincidencias);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout_editcomment.setVisibility(View.VISIBLE);
            }
        });
        btnSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTypeOfProblem = String.valueOf(sp_typeOfProblem.getSelectedItem());
                if(validate()) {
                    lstincidencias = new ArrayList<Incidencia>();
                    Incidencia incidenciaObj = new Incidencia();
                    incidenciaObj.setPosition("0");
                    List<String> lstnew = new ArrayList<String>();
                    lstnew.add(et_addcomment.getText().toString());
                    incidenciaObj.setLstdescription(lstnew);
                    incidenciaObj.setTypeofincidencia(strTypeOfProblem);
                    addIncidencia(incidenciaObj);

                    InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    et_addcomment.clearFocus();

                    linearLayout_editcomment.setVisibility(View.INVISIBLE);
                }

            }
        });

        return view;
    }


    @Override
    public void listaRecibida(OrderSearch body) {

    }

    @Override
    public void errorRecibido(Object error) {

    }

    public void loadIncidencias(ArrayList<Incidencia> lstincidencias){

        if(lstincidencias!=null){
            adapter = new OrderListIncidenciasAdapter(lstincidencias);
            recycler = (RecyclerView) view.findViewById(R.id.recycle2);
            recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recycler.setAdapter(adapter);
            tv_nocomments.setVisibility(View.INVISIBLE);
        }
        else
        {
            tv_nocomments.setVisibility(View.VISIBLE);
        }

    }

    public void addIncidencia(Incidencia incidenciaObj){
        // Indicates the service call
        Globals g = Globals.getInstance();
        try {
            g.setServiceCode(8);
            restHelper = new RetrofitDelegateHelper(id_order,0,incidenciaObj);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        restHelper.addIncidencia(true);
    }

    public Boolean validate()
    {
        Boolean bResult = false;
        bResult = (et_addcomment.getText().length()>0)?true:false;
        if(et_addcomment.getText().length()<=0)
        {
            bResult = false;
            et_addcomment.setError(getResources().getString(R.string.alert_edittext_problem));
        }
        else
        { bResult = true;}
        if(strTypeOfProblem.equals("Elige el tipo de incidencia"))
        {
            bResult = false;
            TextView errorText = (TextView)sp_typeOfProblem.getSelectedView();
            errorText.setText(getResources().getString(R.string.alert_spinner_problem));//changes the selected item text to this
        }
        else
        { bResult = true;}
        return bResult;
    }


}