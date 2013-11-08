package org.orbitnav;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public final class NavigationBehavior {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public static NavigationBehavior drag(MouseButton button, Response response) {
        return new NavigationBehavior(Input.DRAG, button, Modifier.NONE, response);
    }
    
    public static NavigationBehavior drag(MouseButton button, Modifier modifier, Response response) {
        return new NavigationBehavior(Input.DRAG, button, modifier, response);
    }
    
    public static NavigationBehavior scroll(Response response) {
        return new NavigationBehavior(Input.SCROLL, null, Modifier.NONE, response);
    }
    
    public static NavigationBehavior scroll(Modifier modifier, Response response) {
        return new NavigationBehavior(Input.SCROLL, null, modifier, response);
    }
    
    public Input getInput() { return input; }
    public MouseButton getButton() { return button; }
    public Modifier getModifier() { return modifier; }
    public Response getResponse() { return response; }

    public boolean inputConditionsMatch(NavigationBehavior b) {
        return ((input == b.input) && (button == b.button) && (modifier == b.modifier));
    }
    
    public boolean mouseEventMatches(MouseEvent me) {
        return ((input == Input.DRAG) && (me.getButton() == button) && 
                (me.isShiftDown() == shift()) && (me.isAltDown() == alt()) && (me.isControlDown() == ctrl()));
    }
    
    public boolean scrollEventMatches(ScrollEvent se) {
        return ((input == Input.SCROLL) &&
                (se.isShiftDown() == shift()) && (se.isAltDown() == alt()) && (se.isControlDown() == ctrl()));
    }
    
    public static enum Input {
        DRAG, SCROLL
    }
    public static enum Modifier {
        NONE, SHIFT, ALT, CONTROL
    }
    public static enum Response {
        PAN, ZOOM, ROTATE
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private NavigationBehavior(Input input, MouseButton button, Modifier modifier, Response response) 
    {
        this.input = input;
        this.button = button;
        this.modifier = modifier;
        this.response = response;
    }    
    
    private final Input input;
    private final MouseButton button;
    private final Modifier modifier;
    private final Response response;
    
    private boolean shift() { return (modifier == Modifier.SHIFT); }
    private boolean alt() { return (modifier == Modifier.ALT); }
    private boolean ctrl() { return (modifier == Modifier.CONTROL); }
    
}
