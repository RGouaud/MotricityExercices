package com.example.ex_motricite;

import androidx.annotation.NonNull;

/**
 * The {@code CordsSample} class represents a sample of coordinates with associated time.
 * It includes information about the X and Y coordinates along with the timestamp.
 *
 * <p>
 * This class is used to store information about a specific point in a coordinate system.
 * </p>
 *
 * @author Segot
 * @version 1.0
 */
public class CordsSample {
    /**
     * The X-coordinate value.
     */
    private String cX;

    /**
     * The Y-coordinate value.
     */
    private String cY;

    /**
     * The timestamp associated with the coordinates.
     */
    private String time;

    /**
     * Gets the X-coordinate value.
     *
     * @return The X-coordinate value.
     */
    public String getCX() {
        return cX;
    }

    /**
     * Sets the X-coordinate value.
     *
     * @param cX The new X-coordinate value.
     */
    public void setCX(String cX) {
        this.cX = cX;
    }

    /**
     * Gets the Y-coordinate value.
     *
     * @return The Y-coordinate value.
     */
    public String getCY() {
        return cY;
    }

    /**
     * Sets the Y-coordinate value.
     *
     * @param cY The new Y-coordinate value.
     */
    public void setCY(String cY) {
        this.cY = cY;
    }

    /**
     * Gets the timestamp associated with the coordinates.
     *
     * @return The timestamp associated with the coordinates.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the timestamp associated with the coordinates.
     *
     * @param time The new timestamp.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Returns a string representation of the {@code CordsSample} object.
     *
     * @return A string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {
        return "CordsSample{" +
                "cX='" + cX + '\'' +
                ", cY='" + cY + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
