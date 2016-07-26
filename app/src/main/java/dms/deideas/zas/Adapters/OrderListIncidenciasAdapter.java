package dms.deideas.zas.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.R;
import dms.deideas.zas.Utils;

/**
 * Created by bnavarro on 12/07/2016.
 */
public class OrderListIncidenciasAdapter extends RecyclerView.Adapter<OrderListIncidenciasAdapter.OLI_ViewHolder> {

    private ArrayList<Incidencia> lista_incidencias;
    private Utils utils;

    public OrderListIncidenciasAdapter(ArrayList<Incidencia> lstincidencias) {
        this.lista_incidencias = lstincidencias;
    }

    @Override
    public OrderListIncidenciasAdapter.OLI_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_note, parent, false);
        return new OLI_ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(OrderListIncidenciasAdapter.OLI_ViewHolder holder, int position) {
        holder.card.setTag(position);
        holder.note.setText(lista_incidencias.get(position).getLstdescription().toString());
        holder.typenote.setText(lista_incidencias.get(position).getTypeofincidencia());
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(lista_incidencias!=null){
            count = lista_incidencias.size();
        }
        return count;
    }


    public class OLI_ViewHolder extends RecyclerView.ViewHolder {

        public TextView idnote, note, typenote;
        public Spinner sp_typeproblem;

        public View card;

        public OLI_ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            typenote = (TextView) itemView.findViewById(R.id.note_createdAt);
            note = (TextView) itemView.findViewById(R.id.note_text);

        }
    }

    public String translateTypeOfIncidencia(String str_typeofincidencia)
    {
        String strResult = "";
        switch (str_typeofincidencia){
            case Constants.PROBLEM_drop_food_str:
                strResult  =  String.valueOf(R.string.problem_drop_food);
            break;
            case Constants.PROBLEM_wrong_plate_str:
                strResult  = String.valueOf(R.string.problem_wrong_plate);
                break;
            case Constants.PROBLEM_wrong_order_str:
                strResult  = String.valueOf(R.string.problem_wrong_order);
                break;
            case Constants.PROBLEM_forget_plate_str:
                strResult  =String.valueOf(R.string.problem_forget_plate);
                break;
            case Constants.PROBLEM_drop_drink_str:
                strResult  = String.valueOf(R.string.problem_drop_drink);
                break;
            case Constants.PROBLEM_wrong_drink_str:
                strResult  =String.valueOf(R.string.problem_wrong_drink);
                break;
            case Constants.PROBLEM_forget_drink_str:
                strResult  = String.valueOf(R.string.problem_forget_drink);
                break;
            default:
                break;

        }
        return strResult;
    }
}

