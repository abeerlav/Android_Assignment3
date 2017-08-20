package a2dv606_aa223de.assignment3.Call_History;

/**
 * Created by Abeer on 3/7/2017.
 */

public class Call {
    private long id;
    private String call;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String toString() {
        return call;
    }
}