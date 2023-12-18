package edu.kit.ipd.are.esda;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Measurement {

    private final String point;

    private final String time;

    private final double value;

    public Measurement(@JsonProperty("point") final String point,
            @JsonProperty("time") final String time, @JsonProperty("value") final double value) {
        this.point = point;
        this.time = time;
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Measurement other = (Measurement) obj;
        return Objects.equals(point, other.point) && Objects.equals(time, other.time)
                && (Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value));
    }

    public String getPoint() {
        return point;
    }

    public String getTime() {
        return time;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, time, value);
    }

    @Override
    public String toString() {
        return "Measurement [point=" + point + ", time=" + time + ", value=" + value + "]";
    }

}
