package org.arcball.internal;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.SubScene;

/**
 * Adaptor class to convert a <code>SubScene</code> to an {@link org.arcball.internal.InteractionHost InteractionHost}.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public class InteractionHostSubScene implements InteractionHost {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionHostSubScene(SubScene subscene) {
        this.subscene = subscene;
    }
    
    @Override public <T extends Event> void addEventHandler(EventType<T> eventType,
            EventHandler<? super T> eventHandler) 
    {
        subscene.addEventHandler(eventType, eventHandler);
    }

    @Override public <T extends Event> void removeEventHandler(EventType<T> eventType,
            EventHandler<? super T> eventHandler) 
    {
        subscene.removeEventHandler(eventType, eventHandler);
    }

    @Override public ReadOnlyDoubleProperty widthProperty() { return subscene.widthProperty(); }

    @Override public ReadOnlyDoubleProperty heightProperty() { return subscene.heightProperty(); }

    @Override public double getWidth() { return subscene.getWidth(); }

    @Override public double getHeight() { return subscene.getHeight(); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private final SubScene subscene;    
    
}
