package dms.deideas.zas.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by bnavarro on 18/07/2016.
 */
public class Incidencia implements Parcelable {
    public Incidencia() {

    }
    private String position;
    private String problem_type;
    private List<String> problems;


    protected Incidencia(Parcel in) {
        position = in.readString();
        problem_type = in.readString();
        problems = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(position);
        dest.writeString(problem_type);
        dest.writeStringList(problems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Incidencia> CREATOR = new Creator<Incidencia>() {
        @Override
        public Incidencia createFromParcel(Parcel in) {
            return new Incidencia(in);
        }

        @Override
        public Incidencia[] newArray(int size) {
            return new Incidencia[size];
        }
    };

    public String getTypeofincidencia() {
        return problem_type;
    }

    public void setTypeofincidencia(String problem_type) {
        this.problem_type = problem_type;
    }

    public List<String> getLstdescription() {
        return problems;
    }

    public void setLstdescription(List<String> lstdescription) {
        this.problems = lstdescription;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
