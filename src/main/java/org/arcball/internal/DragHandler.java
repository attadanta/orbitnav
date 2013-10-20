package org.arcball.internal;

/**
 * Drag handler.  Handles dragging events for 3D interaction.
 * <p>
 * Implementors of this interface can be used in conjunction with {@link org.arcball.internal.DragHelper DragHelper} 
 * to handle dragging events for 3D interaction.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public interface DragHandler {
    /**
     * Handle a drag event.  This method is normally called by {@link org.arcball.internal.DragHelper DragHelper} when
     * a drag has been detected.
     * @param deltaX the change in mouse (scene) x coordinate
     * @param deltaY the change in mouse (scene) y coordinate
     */
    void handleDrag(double deltaX, double deltaY);
}
