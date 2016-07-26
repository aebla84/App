package dms.deideas.zas;

/**
 * Created by bnavarro on 20/07/2016.
 */
public class Utils {

    public String convertFormatDate(String date)
    {
        String str_date="";
        String day = date.substring(9,10);
        String month = date.substring(6,7);
        String year = date.substring(0,3);
        str_date = day.concat("-").concat(month).concat("-").concat(year);
        return str_date;
    }
}
