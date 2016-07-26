package dms.deideas.zas;

/**
 * Created by bnavarro on 19/07/2016.
 */
public class Constants {


    public static final int SERVICE_CODE_order_edit = 0;
    public static final int SERVICE_CODE_order_get = 1;
    public static final int SERVICE_CODE_order_accepted = 2;
    public static final int SERVICE_CODE_order_accepted_byuser = 3;
    public static final int SERVICE_CODE_order_problem = 4;
    public static final int SERVICE_CODE_order_problem_byuser = 5;
    public static final int SERVICE_CODE_order_notes = 6;
    public static final int SERVICE_CODE_order_edit_acceptbymotodriver = 7;
    public static final int SERVICE_CODE_problem_add_completed = 8;
    public static final int SERVICE_CODE_problem_add_description = 9;
    public static final int SERVICE_CODE_notes_byuser = 10;
    public static final int SERVICE_CODE_history = 11;


    public static final int PROBLEM_drop_food = 0;
    public static final int PROBLEM_wrong_plate = 1;
    public static final int PROBLEM_wrong_order= 2;
    public static final int PROBLEM_forget_plate = 3;
    public static final int PROBLEM_drop_drink = 4;
    public static final int PROBLEM_wrong_drink = 5;
    public static final int PROBLEM_forget_drink = 6;

    public static final String PROBLEM_drop_food_str = "drop_food";
    public static final String PROBLEM_wrong_plate_str = "wrong_plate";
    public static final String PROBLEM_wrong_order_str= "wrong_order";
    public static final String PROBLEM_forget_plate_str = "forget_plate";
    public static final String PROBLEM_drop_drink_str = "drop_drink";
    public static final String PROBLEM_wrong_drink_str = "wrong_drink";
    public static final String PROBLEM_forget_drink_str = "forget_drink";


    public static final String ORDER_STATUS_order_on_hold = "order_on_hold";
    public static final String ORDER_STATUS_rest_has_accepted = "rest_has_accepted";
    public static final String ORDER_STATUS_driver_has_accepted = "driver_has_accepted";
    public static final String ORDER_STATUS_driver_in_rest = "driver_in_rest";
    public static final String ORDER_STATUS_driver_on_road = "driver_on_road";
    public static final String ORDER_STATUS_order_delivered = "order_delivered";
    public static final String ORDER_STATUS_order_delivered_w_problem = "order_delivered_w_problem";
    public static final String ORDER_STATUS_problem = "problem";

}
