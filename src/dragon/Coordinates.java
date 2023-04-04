package dragon;

import java.io.Serializable;

public class Coordinates implements Serializable {

    private float x;
    private Integer y; //Значение поля должно быть больше -323, Поле не может быть null

    public Coordinates(Float x, Integer y) {
        this.x = x;
        this.y = y;
     }

    public float getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
