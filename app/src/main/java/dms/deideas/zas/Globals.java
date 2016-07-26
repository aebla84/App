package dms.deideas.zas;

/**
 * Created by dmadmin on 13/06/2016.
 */
public class Globals {
    private static Globals instance;

    // Global variable
    private int serviceCode;
    private int screenCode;

    // Restrict the constructor from being instantiated
    private Globals() {
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    public int getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(int serviceCode) {
        this.serviceCode = serviceCode;
    }

    public int getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(int screenCode) {
        this.screenCode = screenCode;
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }
}
