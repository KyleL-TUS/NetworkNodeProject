package org.ericsson.miniproject;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class NetworkNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String node_name;
    private String node_location;
    private int latitude;
    private int longitude;

    public NetworkNode() {
        // Default constructor
    }

    public NetworkNode(String name, String location, int latitude, int longitude) {
        this.node_name = name;
        this.node_location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String name) {
        this.node_name = name;
    }

    public String getNode_location() {
        return node_location;
    }

    public void setNode_location(String location) {
        this.node_location = location;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
}