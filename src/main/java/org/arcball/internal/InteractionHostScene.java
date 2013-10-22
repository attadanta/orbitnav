package org.arcball.internal;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;

/**
 * Adaptor class to convert a <code>Scene</code> to an {@link org.arcball.internal.InteractionHost InteractionHost}.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class InteractionHostScene implements InteractionHost {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionHostScene(Scene scene) {
        this.scene = scene;
    }
    
    @Override public <T extends Event> void addEventHandler(EventType<T> eventType,
            EventHandler<? super T> eventHandler) 
    {
        scene.addEventHandler(eventType, eventHandler);
    }

    @Override public <T extends Event> void removeEventHandler(EventType<T> eventType,
            EventHandler<? super T> eventHandler) 
    {
        scene.removeEventHandler(eventType, eventHandler);
    }

    @Override public ReadOnlyDoubleProperty widthProperty() { return scene.widthProperty(); }

    @Override public ReadOnlyDoubleProperty heightProperty() { return scene.heightProperty(); }

    @Override public double getWidth() { return scene.getWidth(); }

    @Override public double getHeight() { return scene.getHeight(); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private final Scene scene;
    
}
