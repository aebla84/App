package dms.deideas.zas.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dmadmin on 01/06/2016.
 */
public class Order implements Serializable {

    public Order() {
    }

    @SerializedName("restaurant")
    private Restaurant restaurant;
    @SerializedName("shipping_address")
    private Customer shipping_address;
    @SerializedName("payment_details")
    private Payment payment_details;

    private Incidencia problem_details; //Object that is used to add new problem in BBDD
    @SerializedName("id")
    private int id;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("completed_at")
    private String completed_at;
    @SerializedName("orderstatus")
    private String orderstatus;
    @SerializedName("motodriver")
    private String motodriver;
    @SerializedName("note")
    private String note;
    @SerializedName("minutesMotoDriverPickupInRestaurant")
    private int minutesMotoDriverPickupInRestaurant;
    @SerializedName("total")
    private String total;
    @SerializedName("lista_incidencias")
    private List<Incidencia> lista_incidencias;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(String completed_at) {
        this.completed_at = completed_at;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getMotodriver() {
        return motodriver;
    }

    public void setMotodriver(String motodriver) {
        this.motodriver = motodriver;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Customer getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(Customer shipping_address) {
        this.shipping_address = shipping_address;
    }

    public Payment getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(Payment payment_details) {
        this.payment_details = payment_details;
    }

    public Incidencia getProblem_details() {
        return problem_details;
    }

    public void setProblem_details(Incidencia problem_details) {
        this.problem_details = problem_details;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Incidencia> getLista_incidencias() {
        return lista_incidencias;
    }

    public void setLista_incidencias(List<Incidencia> lista_incidencias) {
        this.lista_incidencias = lista_incidencias;
    }


    public String getTimeKitchen() {
        return String.valueOf(minutesMotoDriverPickupInRestaurant);
    }

    public void setTimeKitchen(String minutesMotoDriverPickupInRestaurant) {
        this.minutesMotoDriverPickupInRestaurant = Integer.parseInt(minutesMotoDriverPickupInRestaurant);
    }

    public int getMinutesMotoDriverPickupInRestaurant() {
        return minutesMotoDriverPickupInRestaurant;
    }

    public void setMinutesMotoDriverPickupInRestaurant(int minutesMotoDriverPickupInRestaurant) {
        this.minutesMotoDriverPickupInRestaurant = minutesMotoDriverPickupInRestaurant;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    //TODO
    @Override
    public String toString() {
        return "Order{" +
                "orderstatus='" + orderstatus + '\'' +
                '}';
    }


}
