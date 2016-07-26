package dms.deideas.zas.Services.Retrofit;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dms.deideas.zas.Constants;
import dms.deideas.zas.Globals;
import dms.deideas.zas.Model.Incidencia;
import dms.deideas.zas.Model.Order;
import dms.deideas.zas.Model.OrderNote;
import dms.deideas.zas.Services.OrderNoteGet;
import dms.deideas.zas.Services.OrderNoteService;
import dms.deideas.zas.Services.OrderNoteUpdate;
import dms.deideas.zas.Services.OrderSearch;
import dms.deideas.zas.Services.OrderService;
import dms.deideas.zas.Services.OrderUpdate;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by dmadmin on 02/06/2016.
 */
public class RetrofitDelegateHelper {

    private final Retrofit retrofit;
    private final OrderService orderService;
    private final OrderNoteService ordernoteService;

    private String oauth_consumer_key = "ck_40b166eb08943c530d82aab33c3bdb572ad0966d";
    private String oauth_consumer_secret = "cs_beaa6104237452538253e9df1160163daa7b98ae";
    private String oauth_signature_method = "HMAC-SHA1";
    private String oauth_timestamp = null;
    private String oauth_nonce = null;
    private String oauth_signature = null;


    public static final String BASE_URL = "http://zascomidaentuboca.com/";
    public static String BASE_URL_CODIFIED = "";

    private int idUser = 0;
    private int idOrder = 0;
    private Incidencia problem_details;
    private OrderNote order_note;
    private List<String> lstIncidencias;
    private Boolean bResult = false;

    public RetrofitDelegateHelper(int idOrder, int idUser) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        this.idUser = idUser;
        this.idOrder = idOrder;

        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        // Llamamos a la clase retrofit que se encargará de realizar la llamada asíncrona
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderService = retrofit.create(OrderService.class);
        ordernoteService = retrofit.create(OrderNoteService.class);
    }

    public RetrofitDelegateHelper(int idOrder, int idUser, Incidencia problem_details) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        this.idUser = idUser;
        this.idOrder = idOrder;

        //this.lstIncidencias = lstIncidencias;
        this.problem_details = problem_details;

        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        // Llamamos a la clase retrofit que se encargará de realizar la llamada asíncrona
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderService = retrofit.create(OrderService.class);
        ordernoteService = retrofit.create(OrderNoteService.class);
    }

    public RetrofitDelegateHelper(int idOrder, int idUser, Incidencia problem_details, OrderNote order_note) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        this.idUser = idUser;
        this.idOrder = idOrder;

        //this.lstIncidencias = lstIncidencias;
        this.problem_details = problem_details;
        this.order_note = order_note;

        // Initiate params
        oauth_nonce = getNonce();
        oauth_timestamp = getTimestamp();
        oauth_signature = getSignature(oauth_consumer_secret);

        // Llamamos a la clase retrofit que se encargará de realizar la llamada asíncrona
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderService = retrofit.create(OrderService.class);
        ordernoteService = retrofit.create(OrderNoteService.class);
    }

    // Método para calcular el nonce de OAuth1.0
    private String getNonce() {

        String oauthNonce = UUID.randomUUID().toString().replaceAll("-", "");

        return oauthNonce;
    }

    // Método que devuelve el current time del sistema en segundos desde 1-1-1970
    private static String getTimestamp() {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    // Método para calcular el signature
    public String getSignature(String oauth_consumer_secret) throws
            UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String baseString = getBaseString();
        SecretKeySpec key = new SecretKeySpec((oauth_consumer_secret).getBytes("UTF-8"), "HMAC-SHA1");
        Mac mac = Mac.getInstance("HMAC-SHA1");
        mac.init(key);

        byte[] bytes = mac.doFinal(baseString.getBytes("UTF-8"));

        // Codificamos en Base64, realizamos un trim y retornamos
        return new String(Base64.encodeToString(bytes, 0).trim());
    }


    public void getListaPedidos(final AlRecibirListaDelegate delegate) {
        orderService.list(oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {

                Log.d("URL getListaPedidos: ", response.raw().request().url().toString());
                Log.d("CODE getListaPedidos: ", String.valueOf(response.code()));


                // Al recibir datos llamamos al método
                delegate.listaRecibida(response.body());
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
            }
        });
    }

    public void getOrdersAccepted(final listaRecibidaOrdenada delegate) {
        orderService.list_accepted(oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {

                Log.d("URL getOrdersAccepted: ", response.raw().request().url().toString());
                Log.d("CODE OrdersAcce : ", String.valueOf(response.code()));

                // Al recibir datos llamamos al método
                delegate.listaRecibidaOrdenada(response.body());
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
            }
        });
    }

    public void getOrdersByUser(final AlRecibirListaDelegate delegate) {
        orderService.list_byuser(idUser, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {
                Log.d("URL getOrdersAccepted: ", response.raw().request().url().toString());
                Log.d("CODE OrdersAcce : ", String.valueOf(response.code()));

                // Al recibir datos llamamos al método
                delegate.listaRecibida(response.body());
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {
                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
            }
        });
    }

    public void getOrdersProblem(final AlRecibirListaDelegate delegate) {
        orderService.list_problem(oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {

                Log.d("URL getOrdersProblem: ", response.raw().request().url().toString());
                Log.d("CODE getOrdersProblem: ", String.valueOf(response.code()));

                // Al recibir datos llamamos al método
                delegate.listaRecibida(response.body());
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
            }
        });
    }

    public void getOrdersProblemByIdUser(final AlRecibirListaDelegate delegate) {
        orderService.list_problembyiduser(idUser, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {

                if (response.isSuccessful()) {
                    Log.d("URL OrdersProblemById: ", response.raw().request().url().toString());
                    Log.d("CODE OrdProblemById: ", String.valueOf(response.code()));
                    // Al recibir datos llamamos al método
                    delegate.listaRecibida(response.body());
                } else {

                    Log.d("error: ", String.valueOf(response.errorBody()));


                }
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
            }
        });
    }

    public void getOrderNotesByIdOrder(final AlRecibirListaCommentsDelegate delegate) {
        ordernoteService.getlistNoteOrder(idOrder, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderNoteGet>() {
            @Override
            public void onResponse(Call<OrderNoteGet> call, Response<OrderNoteGet> response) {

                Log.d("URL OrdersProblemById: ", response.raw().request().url().toString());
                Log.d("CODE OrdProblemById: ", String.valueOf(response.code()));

                delegate.listaRecibida(response.body());
            }

            @Override
            public void onFailure(Call<OrderNoteGet> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
            }
        });
    }


    public void getOrderHistorical(final AlRecibirListaDelegate delegate) {
        orderService.list_history(idUser, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderSearch>() {
            @Override
            public void onResponse(Call<OrderSearch> call, Response<OrderSearch> response) {

                Log.d("URL OrdersProblemById: ", response.raw().request().url().toString());
                Log.d("CODE OrdProblemById: ", String.valueOf(response.code()));

                delegate.listaRecibida(response.body());
            }

            @Override
            public void onFailure(Call<OrderSearch> call, Throwable t) {

                // En caso que no haya respuesta lanzamos el método para que indique el error
                delegate.errorRecibido(t);
            }
        });
    }


    public void updateStatus(String status) {


        Globals g = Globals.getInstance();
        int screenCode = g.getScreenCode();

        Order order = new Order();
        OrderUpdate orderUpdate = new OrderUpdate();

        String motodriver = order.getMotodriver();

        if (screenCode == 0) {

            Log.d("idUser: ", String.valueOf(idUser));
            Log.d("idOrder: ", String.valueOf(idOrder));
            order.setOrderstatus("driver_has_accepted");
            // Setting the motodriver
            order.setMotodriver(String.valueOf(idUser));
            orderUpdate.setOrder(order);
            orderService.acceptOrderByMotodriver(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("URL 3_POST: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));
                    } else {

                        Log.d("error: ", String.valueOf(response.errorBody()));


                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");

                }
            });
        } else if (screenCode == 1) {
            String newStatus = orderState(status);
            order.setOrderstatus(newStatus);
            orderUpdate.setOrder(order);
            orderService.updateOrderStatus(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("URL 3_POST: ", response.raw().request().url().toString());
                    Log.d("CODE_POST: ", String.valueOf(response.code()));
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");

                }
            });
        }
    }

    public void addIncidencia(Boolean isNew) {
        Order order = new Order();
        order.setProblem_details(this.problem_details);
        OrderUpdate orderUpdate = new OrderUpdate();
        orderUpdate.setOrder(order);

        if (isNew) {
            //Send Typeofproblem and description of problem
            //'lista_incidencias_problemtype'
            //'lista_incidencias_description'
            orderService.addincidencia_completed(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("URL 3_POST: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));

                    } else {

                        Log.d("error: ", String.valueOf(response.errorBody()));


                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");

                }
            });
        } else {
            //Send position to  add new description and description of problem
            //'lista_incidencias_posicion'
            //'lista_incidencias_description'
            orderService.addincidencia_description(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                    oauth_signature_method, oauth_timestamp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("URL 3_POST: ", response.raw().request().url().toString());
                        Log.d("CODE_POST: ", String.valueOf(response.code()));

                    } else {

                        Log.d("error: ", String.valueOf(response.errorBody()));

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", "No respuesta");

                }
            });
        }

    }

    public Boolean addOrderNote() {
        bResult = false;
        OrderNote order = new OrderNote();
        order.setNote(this.order_note.getNote());
        OrderNoteUpdate orderUpdate = new OrderNoteUpdate();
        orderUpdate.setOrder_note(order);

        ordernoteService.createNoteOrder(idOrder, orderUpdate, oauth_consumer_key, oauth_nonce, oauth_signature,
                oauth_signature_method, oauth_timestamp).enqueue(new Callback<OrderNoteGet>() {
            @Override
            public void onResponse(Call<OrderNoteGet> call, Response<OrderNoteGet> response) {
                if (response.isSuccessful()) {
                    Log.d("URL 3_POST: ", response.raw().request().url().toString());
                    Log.d("CODE_POST: ", String.valueOf(response.code()));
                    bResult = true;
                } else {

                    Log.d("error: ", String.valueOf(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<OrderNoteGet> call, Throwable t) {
                Log.d("Error", "No respuesta");

            }
        });
        return bResult;
    }

    private String orderState(String orderstatus) {

        String newStatus = "";

        switch (orderstatus) {
            case "driver_has_accepted":
                newStatus = "driver_in_rest";
                break;
            case "driver_in_rest":
                newStatus = "driver_on_road";
                break;
            case "driver_on_road":
                newStatus = "order_delivered";
                break;

            default:
                orderstatus = orderstatus;
                break;

        }
        return newStatus;
    }


    //Method that build baseString depending getServiceCode
    private String getBaseString() {
        String baseString = "";
        Globals g = Globals.getInstance();
        int serviceCode = g.getServiceCode();

       /* switch (g.getServiceCode()){
            case  Constants.SERVICE_CODE_order_edit:
                Log.d("Service Code", "0, haciendo POST");
                BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Feditfromapp%2F" + idOrder;
                baseString = "POST&" + BASE_URL_CODIFIED ;
                break;
            case Constants.SERVICE_CODE_order_get:
                Log.d("Service Code", "1, haciendo GET");
                BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders";
                baseString = "GET&" + BASE_URL_CODIFIED ;
                break;
        }*/

        // In fuction of the service called, we do a post o get signature calculation
        if (serviceCode == Constants.PROBLEM_drop_food) {
            Log.d("Service Code", "0, haciendo POST");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Feditfromapp%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;

        } else if (serviceCode == Constants.SERVICE_CODE_order_get) {
            Log.d("Service Code", "1, haciendo GET");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == Constants.SERVICE_CODE_order_accepted) {
            Log.d("Service Code", "2, haciendo GET");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Faccepted";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 3) {
            Log.d("Service Code", "3, haciendo GET");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Faccepted%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 4) {
            Log.d("Service Code", "4, haciendo GET");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Fproblem";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 5) {
            Log.d("Service Code", "5, haciendo GET");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Fproblem%2F" + idUser;

            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 6) {
            Log.d("Service Code", "6, haciendo GET");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2F" + idOrder + "%2Fcomments";
            baseString = "GET&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 7) {
            Log.d("Service Code", "7, haciendo POST");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Fedit_order_acceptbymotodriver%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;

        } else if (serviceCode == 8) {
            Log.d("Service Code", "8, haciendo POST");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Faddincidencia_completed%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 9) {
            Log.d("Service Code", "9, haciendo POST");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Faddincidencia_description%2F" + idOrder;
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 10) {
            Log.d("Service Code", "10, haciendo POST");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2F" + idOrder + "%2Fcomments";
            baseString = "POST&" + BASE_URL_CODIFIED;
        } else if (serviceCode == 11) {
            Log.d("Service Code", "11, haciendo GET");
            BASE_URL_CODIFIED = "http%3A%2F%2Fzascomidaentuboca.com%2Fwc-api%2Fv2%2Forders%2Fhistory%2F" + idUser;
            baseString = "GET&" + BASE_URL_CODIFIED;
        }

        baseString = baseString + "&oauth_consumer_key%3D" + oauth_consumer_key
                + "%26oauth_nonce%3D" + oauth_nonce + "%26oauth_signature_method%3D" + "HMAC-SHA1" +
                "%26oauth_timestamp%3D" + oauth_timestamp;
        return baseString;
    }


    public interface AlRecibirListaDelegate {

        void listaRecibida(OrderSearch body);

        void errorRecibido(Object error);

    }

    public interface AlRecibirListaCommentsDelegate {

        void listaRecibida(OrderNoteGet body);

        void errorRecibido(Object error);

    }

    public interface listaRecibidaOrdenada {

        void listaRecibidaOrdenada(OrderSearch body);

        void errorRecibido(Object error);

    }

}
