package org.arcball.internal;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;

import org.arcball.NavigationBehavior;

public abstract class InteractionScroll extends InteractionBase {

    //---------------------------------------------------------------------------------------------------------- PUBLIC

    public InteractionScroll() {
        super();
        // it only makes sense for SCROLL navigation behavior to be assigned; so assert() that
        navigationBehaviorProperty().addListener(new ChangeListener<NavigationBehavior>() {
            @Override public void changed(ObservableValue<? extends NavigationBehavior> ob, 
                    NavigationBehavior oldnb, NavigationBehavior newnb)
            {
                assert(newnb.getInput() == NavigationBehavior.Input.SCROLL);
            }
        });                
    }
    
    @Override public void attachToHost(InteractionHost host) {
        super.attachToHost(host);
        host.addEventHandler(ScrollEvent.SCROLL, getScrollHandler());
    }
    
    @Override public void detachFromHost(InteractionHost host) {
        super.detachFromHost(host);
        host.removeEventHandler(ScrollEvent.SCROLL, getScrollHandler());
    }
    
    //------------------------------------------------------------------------------------------------------- PROTECTED
    
    protected abstract EventHandler<ScrollEvent> getScrollHandler();
    
    protected boolean scrollEventMatches(ScrollEvent se) {
        NavigationBehavior nb = getNavigationBehavior();
        return ((nb == null) || (nb.scrollEventMatches(se)));
    }
    
}
