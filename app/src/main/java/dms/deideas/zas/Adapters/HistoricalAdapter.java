package dms.deideas.zas.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;

import dms.deideas.zas.Utils;

/**
 * Created by dmadmin on 06/07/2016.
 */
public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder> implements View.OnClickListener {

    private List<Order> orders;
    private HistoricalListener listener;

    private Comparator<Order> comparator;
    private Utils utils;

    public HistoricalAdapter(HistoricalAdapter.HistoricalListener listener) {
        this.orders = new ArrayList<>();
        this.listener = listener;
    }

    public HistoricalAdapter(List<Order> orders, HistoricalListener listener) {
        this.orders = orders;
        this.listener = listener;
    }


    @Override
    public HistoricalAdapter.HistoricalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_historical, parent, false);
        card.setOnClickListener(this);
        return new HistoricalViewHolder(card);
    }

    @Override
    public void onBindViewHolder(HistoricalAdapter.HistoricalViewHolder holder, int position) {
        holder.card.setTag(position);
        holder.idorder.setText(String.valueOf(orders.get(position).getId()));
        //holder.state.setText(getOrderStatusName(holder.state.getContext(), orders.get(position).getOrderstatus()));
        //String data = utils.convertFormatDate(orders.get(position).getCompleted_at().substring(0,10));
        holder.day.setText(orders.get(position).getCompleted_at().substring(0,10));
        holder.money.setText(orders.get(position).getTotal());
    }



    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = (int) v.getTag();
            Order order = orders.get(position);
            listener.onOrderClicked(v, order);
        }
    }

    public class HistoricalViewHolder extends RecyclerView.ViewHolder {
        public TextView idorder, day, state, money;

        public View card;

        public HistoricalViewHolder(View itemView) {
            super(itemView);

            card = itemView;
            idorder = (TextView) itemView.findViewById(R.id.idorder);
            day = (TextView) itemView.findViewById(R.id.day);
            state = (TextView) itemView.findViewById(R.id.state);
            money = (TextView) itemView.findViewById(R.id.money);
        }
    }

    public interface HistoricalListener {

        void onOrderClicked(View card, Order order);
    }

    public String getOrderStatusName(Context context, String status) {
        String ret_status = status;
        switch (ret_status) {
            case Constants.ORDER_STATUS_problem:
                ret_status = context.getResources().getString(R.string.problem_status);
                break;
            case Constants.ORDER_STATUS_driver_has_accepted:
                ret_status = context.getResources().getString(R.string.motodriver_accept_status);
                break;
            case Constants.ORDER_STATUS_rest_has_accepted:
                ret_status = context.getResources().getString(R.string.rest_has_accepted_status);
                break;
            case  Constants.ORDER_STATUS_driver_in_rest:
                ret_status = context.getResources().getString(R.string.driver_in_rest_status);
                break;
            case Constants.ORDER_STATUS_driver_on_road:
                ret_status = context.getResources().getString(R.string.driver_on_road_status);
                break;
            case Constants.ORDER_STATUS_order_delivered:
                ret_status = context.getResources().getString(R.string.order_delivered_status);
                break;
            default:
                ret_status = status;
                break;
        }

        return ret_status;

    }


    public void setComparador(Comparator<Order> comparator) {
        this.comparator = comparator;
        reorder();
        notifyDataSetChanged();
    }

    private void reorder() {
        if (comparator != null && !orders.isEmpty()) {
            Collections.sort(orders, comparator);
        }
    }

    public void add(List<Order> newElements) {
        orders.addAll(newElements);
        reorder();
        notifyDataSetChanged();
    }

    public void replace(List<Order> elements) {
        orders.clear();
        add(elements);
    }
}
