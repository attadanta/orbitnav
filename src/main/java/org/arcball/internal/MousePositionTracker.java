package org.arcball.internal;

/**
 * Tracker for mouse position between press and drag events.
 * 
 * This is a utility class that tracks the mouse position changes as the mouse is pressed and dragged.
 * 
 * The mouse position can be recorded when the mouse is first pressed 
 * ({@link #mousePress(double, double) mousePress} method), and this position is then updated as the mouse is 
 * dragged ({@link #mouseDrag(double, double) mouseDrag} method).  The change in position that occurs with each drag
 * event can the be queried ({@link #getDeltaX() getDeltaX} and {@link #getDeltaY() getDeltaY} methods).
 * 
 * @author Jonathan Merritt <j.s.merritt@gmail.com>
 */
public final class MousePositionTracker {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    /**
     * Records a mouse press.
     * 
     * This method should be called in response to a mouse press event.
     * 
     * @param pressX x location of the mouse press (typically the scene X coordinate)
     * @param pressY y location of the mouse press (typically the scene Y coordinate)
     */
    public void mousePress(double pressX, double pressY) {
        x = pressX;
        y = pressY;
        deltaX = 0;
        deltaY = 0;
    }

    /**
     * Records a mouse drag.
     * 
     * This method should be called in response to a mouse drag event.
     * 
     * @param dragX x location of the mouse drag (typically the scene X coordinate)
     * @param dragY y location of the mouse drag (typically the scene Y coordinate)
     */
    public void mouseDrag(double dragX, double dragY) {
        double oldX = x;
        double oldY = y;
        x = dragX;
        y = dragY;
        deltaX = x - oldX;
        deltaY = y - oldY;
    }

    /**
     * Returns the change in the x coordinate that occurred in the most recent mouse drag.
     * 
     * @return change in the x coordinate
     */
    public double getDeltaX() { return deltaX; }
    
    /**
     * Returns the change in the y coordinate that occurred in the most recent mouse drag.
     * 
     * @return change in the y coordinate
     */
    public double getDeltaY() { return deltaY; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private double x;
    private double y;
    private double deltaX;
    private double deltaY;
    
}
