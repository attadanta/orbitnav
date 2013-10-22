package org.arcball.internal;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public interface InteractionHost {
    <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler);
    <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler);
    ReadOnlyDoubleProperty widthProperty();
    ReadOnlyDoubleProperty heightProperty();
    double getWidth();
    double getHeight();
}
