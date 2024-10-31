package com.example.demoplswork.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * The Material class represents a material with a name, quantity, and price.
 * It provides methods to get and set these properties.
 * This class uses JavaFX properties to allow for easy binding in a JavaFX application.
 */
public class Material {

    private final SimpleStringProperty name;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty price;
    /**
     * Constructs a Material object with the specified name, quantity, and price.
     * @param name the name of the material
     * @param quantity the quantity of the material
     * @param price the price of the material
     */
    public Material(String name, int quantity, double price) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
    }

    /**
     * Gets the name of the material.
     * return the name of the material
     */
    public String getName() {
        return name.get();
    }
    /**
     * Gets the quantity of the material.
     * return the quantity of the material
     */
    public int getQuantity() {
        return quantity.get();
    }
    /**
     * Gets the price of the material.
     * return the price of the material
     */
    public double getPrice() {
        return price.get();
    }
}
