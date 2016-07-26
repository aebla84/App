package dms.deideas.zas.Services;

import java.util.ArrayList;
import java.util.List;

import dms.deideas.zas.Model.Order;

/**
 * Created by Jordi on 29/05/2016.
 */
public class OrderSearch {

    List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

}

