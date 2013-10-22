package org.arcball.internal;

import javafx.scene.input.MouseEvent;

/**
 * Minimal adaptor class for a {@link org.arcball.internal.DragHandler DragHandler}.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public class DragHandlerAdaptor implements DragHandler {
    @Override public void handleClick(MouseEvent mouseEvent) { }
    @Override public void handleDrag(MouseEvent mouseEvent, double deltaX, double deltaY) { }
}
