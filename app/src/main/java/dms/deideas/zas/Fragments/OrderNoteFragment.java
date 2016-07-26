package dms.deideas.zas.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dms.deideas.zas.Adapters.OrderNoteAdapter;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.OrderNoteGet;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;

/**
 * Created by bnavarro on 12/07/2016.
 */
public class OrderNoteFragment extends Fragment implements RetrofitDelegateHelper.AlRecibirListaCommentsDelegate {

    private RetrofitDelegateHelper restHelper;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private String title;
    private int id_order = 0;
    private View view;
    private ImageButton FAB;
    private ImageButton btnSaveComment;
    private EditText et_addcomment;
    private LinearLayout linearLayout_editcomment;
    private OrderNote order_note;
    private TextView tv_nocomments;
    private Boolean bResult = false;

    public OrderNoteFragment() {
        // Required empty public constructor
        order_note = new OrderNote();
    }

    // Inicializar Comentarios
    private List<OrderNote> items = new ArrayList<>();

    // Inicializar el layout manager
    private LinearLayoutManager layout;


    public static OrderNoteFragment newInstance(String title,int idOrder) {
        OrderNoteFragment fragment = new OrderNoteFragment();

        Bundle b = new Bundle();
        b.putString("title", title);
        b.putInt("idOrder", idOrder);
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_order = getArguments().getInt("idOrder");
            title = getArguments().getString("title");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.comment_orders_fragment, container, false);
        tv_nocomments = (TextView)view.findViewById(R.id.tv_nocomments);

        loadNotes();
        FAB = (ImageButton)view.findViewById(R.id.fab_addcomment);
        linearLayout_editcomment = (LinearLayout)view.findViewById(R.id.send_message);
        et_addcomment = (EditText)view.findViewById(R.id.write_comment);
        btnSaveComment = (ImageButton)view.findViewById(R.id.send_comment);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout_editcomment.setVisibility(View.VISIBLE);
            }
        });
        btnSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_addcomment.getText().length()>0) {
                    order_note.setNote(et_addcomment.getText().toString());
                    saveOrderNote();

                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    et_addcomment.clearFocus();

                    linearLayout_editcomment.setVisibility(View.INVISIBLE);

                    loadNotes();
                }
                else
                {
                    et_addcomment.setError(getResources().getString(R.string.alert_edittext_comment));
                }
            }
        });
        getActivity().setTitle(title);
        return view;
    }


    @Override
    public void listaRecibida(OrderNoteGet body) {
        try {
            if(body != null) {
                List<OrderNote> notes = body.getOrder_notes();

                if(body.getOrder_notes().isEmpty()){
                    tv_nocomments.setVisibility(View.VISIBLE);
                }
                else
                {
                    adapter = new OrderNoteAdapter(notes);
                    recycler = (RecyclerView) view.findViewById(R.id.recycle2);
                    recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recycler.setAdapter(adapter);
                    tv_nocomments.setVisibility(View.INVISIBLE);
                }

            }
        }
        catch (Exception ex)
        {

        }
    }

    @Override
    public void errorRecibido(Object error) {

    }

    public void loadNotes(){
        // Indicates the service call
        Globals g = Globals.getInstance();
        g.setServiceCode(6);
        try {
            restHelper = new RetrofitDelegateHelper(id_order,0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        restHelper.getOrderNotesByIdOrder(this);
    }

    public void saveOrderNote(){
        // Indicates the service call
        Globals g = Globals.getInstance();
        try {
            g.setServiceCode(10);
            restHelper = new RetrofitDelegateHelper(id_order,0,null,order_note);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        restHelper.addOrderNote();
    }
}