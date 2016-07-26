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

import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements View.OnClickListener {

    private List<Order> orders;
    private OrderListener listener;

    private Comparator<Order> comparator;


    public OrderAdapter(OrderListener listener){
        this.orders = new ArrayList<>();
        this.listener = listener;
    }

    public OrderAdapter(List<Order> orders, OrderListener listener) {
        this.orders = orders;
        this.listener = listener;
    }


    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card, parent, false);
        card.setOnClickListener(this);
        return new OrderViewHolder(card);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.OrderViewHolder holder, int position) {
        holder.card.setTag(position);
        holder.restaurant.setText(orders.get(position).getRestaurant().getName());
        holder.direction.setText(orders.get(position).getRestaurant().getStreet());
        holder.hourOrder.setText(orders.get(position).getCreated_at().substring(11, 16) + "h");
        holder.hourKitchen.setText(orders.get(position).getTimeKitchen() + " min");
        holder.state.setText(getOrderStatusName(holder.state.getContext(), orders.get(position).getOrderstatus()));
        holder.idorder.setText(String.valueOf(orders.get(position).getId()));

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

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView idorder, restaurant, direction, hourOrder, hourKitchen, state;

        public View card;

        public OrderViewHolder(View itemView) {
            super(itemView);

            card = itemView;
            state = (TextView) itemView.findViewById(R.id.state);
            idorder = (TextView) itemView.findViewById(R.id.idorder);
            restaurant = (TextView) itemView.findViewById(R.id.restaurant);
            direction = (TextView) itemView.findViewById(R.id.direction);
            hourOrder = (TextView) itemView.findViewById(R.id.hourOrder);
            hourKitchen = (TextView) itemView.findViewById(R.id.hourLeft);
            card.getContext().getResources().getString(R.string.problem_status);


        }
    }

    public interface OrderListener {

        void onOrderClicked(View card, Order order);
    }

    public String getOrderStatusName(Context context, String status) {
        String ret_status = status;
        switch (ret_status) {
            case "problem":
                ret_status = context.getResources().getString(R.string.problem_status);
                break;
            case "driver_has_accepted":
                ret_status = context.getResources().getString(R.string.motodriver_accept_status);
                break;
            case "rest_has_accepted":
                ret_status = context.getResources().getString(R.string.rest_has_accepted_status);
                break;
            case "driver_in_rest":
                ret_status = context.getResources().getString(R.string.driver_in_rest_status);
                break;
            case "driver_on_road":
                ret_status = context.getResources().getString(R.string.driver_on_road_status);
                break;
            case "order_delivered":
                ret_status = context.getResources().getString(R.string.order_delivered_status);
                break;
            default:
                ret_status = status;
                break;
        }

        return ret_status;

    }


    public void setComparador(Comparator<Order> comparator){
        this.comparator = comparator;
        reorder();
        notifyDataSetChanged();
    }

    private void reorder() {
        if (comparator != null && !orders.isEmpty()){
            Collections.sort(orders, comparator);
        }
    }

    public void add(List<Order> newElements){
        orders.addAll(newElements);
        reorder();
        notifyDataSetChanged();
    }

    public void replace(List<Order> elements){
        orders.clear();
        add(elements);
    }
}