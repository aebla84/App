package dms.deideas.zas.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.R;
import dms.deideas.zas.Services.Retrofit.RetrofitDelegateHelper;

/**
 * Created by dmadmin on 29/06/2016.
 */
public class DialogFragment extends android.support.v4.app.DialogFragment {

    private RetrofitDelegateHelper restHelper;

    private Globals g = Globals.getInstance();

    private Order order;

    private String toastMessage = "";


    public static DialogFragment newInstance(String title, Order order) {
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putSerializable("order", order);
        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        String title = getArguments().getString("title");
        order = (Order) getArguments().getSerializable("order");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_access_time_black_24dp)
                .setTitle(title)
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String status = orderState(order.getOrderstatus());

                        Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();


                        // Making Post
                        if(g.getScreenCode() == 0){
                            Toast.makeText(getContext(), " POST 7", Toast.LENGTH_SHORT).show();
                            g.setServiceCode(7);
                        }
                        else {
                            Toast.makeText(getContext(), " POST 0", Toast.LENGTH_SHORT).show();
                            g.setServiceCode(0);
                        }


                        try {
                            final SharedPreferences prefs = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                            int idUser = prefs.getInt("idUser", 0);
                            restHelper = new RetrofitDelegateHelper(order.getId(),idUser);
                            restHelper.updateStatus(order.getOrderstatus());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }

                        if (g.getScreenCode() == 0) {

                            // return to Orders Fragment
                            String title = getResources().getString(R.string.orders_sb_title);
                            OrdersFragment fragment = OrdersFragment.newInstance(title);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .addToBackStack(null)
                                    .commit();

                            getActivity().setTitle(title);

                        } else if (g.getScreenCode() == 1) {
                            // return to My Orders Fragment
                            String title = getResources().getString(R.string.myorders_sb_title);
                            MyOrdersFragment fragment = MyOrdersFragment.newInstance(title, "4");
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .addToBackStack(null)
                                    .commit();

                            getActivity().setTitle(title);
                        }


                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (g.getScreenCode() == 0) {

                            // return to Detail Order Fragment
                            String title = getResources().getString(R.string.order_id_item) + " : " + String.valueOf(order.getId());
                            DetailOrderFragment fragment = DetailOrderFragment.newInstance(title, order);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .addToBackStack(null)
                                    .commit();


                            getActivity().setTitle(title);
                        } else if (g.getScreenCode() == 1) {
                            // return to Detail Order Fragment
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
                })
                .create();
    }

    private String orderState(String orderstatus) {

        switch (orderstatus) {
            case "rest_has_accepted":
                toastMessage = getResources().getString(R.string.toast_order_accepted);
                break;
            case "driver_has_accepted":
                toastMessage = getResources().getString(R.string.toast_in_rest);
                break;
            case "driver_in_rest":
                toastMessage = getResources().getString(R.string.toast_collected);
                break;
            case "driver_on_road":
                toastMessage = getResources().getString(R.string.toast_finished);
                break;
            default:
                orderstatus = orderstatus;
                break;

        }
        return toastMessage;
    }
}
