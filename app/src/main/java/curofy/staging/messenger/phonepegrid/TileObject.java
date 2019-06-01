package curofy.staging.messenger.phonepegrid;

public class TileObject {

    public static int OPEN = 1;
    public static int CLOSE = 0;


    private Integer state; // open, closed
    private Integer value;

    public TileObject(Integer state, Integer value) {
        this.state = state;
        this.value = value;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
