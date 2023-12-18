package com.redhat.demo.clnr;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;


@XmlRootElement
public class Reading implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String customerId;

    private String timestamp;

    private Double kWh;

    public Reading() {
    }

    public Reading(String customerId, String timestamp, Double kWh) {
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.kWh = kWh;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getkWh() {
        return kWh;
    }

    public void setkWh(Double kWh) {
        this.kWh = kWh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reading)) return false;
        Reading reading = (Reading) o;
        return Objects.equals(id, reading.id) &&
                Objects.equals(customerId, reading.customerId) &&
                Objects.equals(timestamp, reading.timestamp) &&
                Objects.equals(kWh, reading.kWh);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, customerId, timestamp, kWh);
    }

    @Override
    public String toString() {
        return "Reading{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", kWh=" + kWh +
                '}';
    }
}
