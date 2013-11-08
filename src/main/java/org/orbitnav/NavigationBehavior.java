/**
 * Copyright 2013 Dr Jonathan S Merritt
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.orbitnav;

import javafx.scene.input.*;

/**
 * Specifies a desired kind of navigation behavior for an {@link OrbitalCameraRig OrbitalCameraRig}.
 *
 * <p>
 * The navigation behavior is specified by a combination of a navigation input, optional {@link Modifier Modifier}
 * keys, and an {@link Activity Activity} (or response) that is associated with the behavior.  The navigation behaviors
 * can be added to an {@link OrbitalCameraRig OrbitalCameraRig} using the
 * {@link OrbitalCameraRig#addNavigationBehavior(NavigationBehavior) addNavigationBehavior} method, and removed using
 * the {@link OrbitalCameraRig#removeNavigationBehavior(NavigationBehavior) removeNavigationBehavior} method.  The
 * {@link OrbitalCameraRig OrbitalCameraRig} will adapt its behavior to include the specified new behavior as
 * required.
 *
 * <p>
 * Navigation inputs can be any of the following:
 * <ul>
 *     <li>mouse drags</li>
 *     <li>scroll gestures</li>
 *     <li>zoom gestures</li>
 *     <li>rotate gestures</li>
 * </ul>
 *
 * <p>
 * Each of these inputs can be associated with some combination of {@link Modifier Modifier} keys (shift, alt and
 * control).  The input is also associated with a required {@link Activity Activity} or response (pan, zoom or rotate).
 * The combination of input and activity specify the behavior.
 *
 * <p>
 * <code>NavigationBehavior</code>s should be constructed using the
 * {@link #mouseDrag(MouseButton, Activity, Modifier...) mouseDrag},
 * {@link #gestureScroll(Activity, Modifier...) gestureScroll},
 * {@link #gestureZoom(Modifier...) gestureZoom} or
 * {@link #gestureRotate(Modifier...) gestureRotate} static methods.
 *
 * <p>
 * There are some limitations on the kinds of {@link Activity Activity} that can associated with different inputs.  The
 * zoom and rotate gestures can only be associated with zoom and rotate activities respectively.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public abstract class NavigationBehavior {

    //---------------------------------------------------------------------------------------------------------- PUBLIC

    /**
     * Creates a new instance of <code>NavigationBehavior</code> representing a mouse drag input.
     *
     * @param button mouse button that the behavior is triggered by
     * @param activity activity with which the behavior should respond
     * @param modifiers modifier keys required
     * @return new <code>NavigationBehavior</code> instance representing a mouse drag input
     */
    public static NavigationBehavior mouseDrag(MouseButton button, Activity activity, Modifier... modifiers) {
        return new Drag(button, activity, modifiers);
    }

    /**
     * Creates a new instance of <code>NavigationBehavior</code> representing a scroll gesture input.
     *
     * @param activity activity with which the behavior should respond
     * @param modifiers modifier keys required
     * @return new <code>NavigationBehavior</code> instance representing a scroll gesture input
     */
    public static NavigationBehavior gestureScroll(Activity activity, Modifier... modifiers) {
        return new Scroll(activity, modifiers);
    }

    /**
     * Creates a new instance of <code>NavigationBehavior</code> representing a zoom gesture input.
     *
     * @param modifiers modifier keys required
     * @return new <code>NavigationBehavior</code> instance representing a zoom gesture input
     */
    public static NavigationBehavior gestureZoom(Modifier... modifiers) {
        return new Zoom(modifiers);
    }

    /**
     * Creates a new instance of <code>NavigationBehavior</code> representing a rotation gesture input.
     *
     * @param modifiers modifier keys required
     * @return new <code>NavigationBehavior</code> instance representing a rotation gesture input
     */
    public static NavigationBehavior gestureRotate(Modifier... modifiers) {
        return new Rotate(modifiers);
    }

    /**
     * Checks whether the input conditions of a <code>NavigationBehavior</code> match those of the current behavior.
     *
     * <p>
     * Input conditions comprise the type of navigation input (eg. drag or gesture) and the modifier keys, but
     * <b>not</b> the type of activity (eg. pan, zoom or rotation).
     *
     * @param b the behavior to check against this behavior
     * @return <code>true</code> if the input conditions match and <code>false</code> if they don't match
     */
    public boolean inputConditionsMatch(NavigationBehavior b) {
        return modifiersMatch(b.shift, b.alt, b.control);
    }

    /**
     * Checks whether an <code>InputEvent</code> matches the current <code>NavigationBehavior</code>.
     *
     * <p>
     * The <code>InputEvent</code> matches if its class is appropriate to the type of navigation input (eg. a
     * <code>MouseEvent</code> is required for a drag gesture), and if the combination of modifier keys matches the
     * set of specified modifiers.
     *
     * @param e event to check
     * @return <code>true</code> if the <code>InputEvent</code> matches the criteria required by this behavior
     */
    public abstract boolean inputEventMatches(InputEvent e);

    /**
     * Indicates whether this behavior is a mouse drag.
     *
     * @return <code>true</code> if this behavior is a mouse drag
     */
    public boolean isMouseDrag() { return false; }

    /**
     * Indicates whether this behavior is a scroll gesture.
     *
     * @return <code>true</code> if this behavior is a scroll gesture
     */
    public boolean isGestureScroll() { return false; }

    /**
     * Indicates whether this behavior is a zoom gesture.
     *
     * @return <code>true</code> if this behavior is a zoom gesture
     */
    public boolean isGestureZoom() { return false; }

    /**
     * Indicates whether this behavior is a rotation gesture.
     *
     * @return <code>true</code> if this behavior is a rotation gesture
     */
    public boolean isGestureRotate() { return false; }

    /**
     * Returns the kind of activity (the response) of the behavior.
     *
     * @return kind of activity
     */
    public Activity getActivity() { return activity; }

    /**
     * Specifies a modifier key.  Combinations of modifier keys can be specified for all types of input.
     */
    public static enum Modifier { SHIFT, ALT, CONTROL }

    /**
     * Specifies an activity.  An activity is the response that a navigation behavior should have to a particular
     * kind of input.
     */
    public static enum Activity { PAN, ZOOM, ROTATE }

    //--------------------------------------------------------------------------------------------------------- PRIVATE

    /** The activity for the abstract <code>NavigationBehavior</code>. */
    private final Activity activity;

    /** Indicates whether or not the shift modifier is required. */
    private final boolean shift;

    /** Indicates whether or not the alt modifier is required. */
    private final boolean alt;

    /** Indicates whether or not the control modifier is required. */
    private final boolean control;

    /**
     * Private constructor.
     *
     * <p>
     * The <code>NavigationBehavior</code> class is abstract and should be instantiated using one of the
     * {@link #mouseDrag(MouseButton, Activity, Modifier...) mouseDrag},
     * {@link #gestureScroll(Activity, Modifier...) gestureScroll},
     * {@link #gestureZoom(Modifier...) gestureZoom} or
     * {@link #gestureRotate(Modifier...) gestureRotate} static methods.
     *
     * @param activity the kind of activity (i.e. the response to the input) required by this behavior
     * @param modifiers the modifiers required by this behavior
     */
    private NavigationBehavior(Activity activity, Modifier... modifiers) {

        // set the activity
        this.activity = activity;

        // set the required modifier keys
        boolean eShift = false, eAlt = false, eControl = false;
        for (Modifier m : modifiers) {
            switch (m) {
                case SHIFT:   eShift   = true; break;
                case ALT:     eAlt     = true; break;
                case CONTROL: eControl = true; break;
            }
        }
        shift   = eShift;
        alt     = eAlt;
        control = eControl;

    }

    //------------------------------------------------------------------------------------------------------- PROTECTED

    /**
     * Indicates whether the passed-in modifiers match the required modifiers for this behavior.
     *
     * @param shift passed-in status of the shift key (<code>true</code> if down)
     * @param alt passed-in status of the alt key (<code>true</code> if down)
     * @param control passed-in status of the control key (<code>true</code> if down)
     * @return <code>true</code> if the modifiers passed-in match those required for this behavior
     */
    protected boolean modifiersMatch(boolean shift, boolean alt, boolean control) {
        return (shift == this.shift) && (alt == this.alt) && (control == this.control);
    }

    //-------------------------------------------------------------------------------------------------- PRIVATE STATIC

    /** Private implementation class for the mouse drag navigation behavior. */
    private static final class Drag extends NavigationBehavior {
        @Override public boolean inputConditionsMatch(NavigationBehavior b) {
            return super.inputConditionsMatch(b) && b.isMouseDrag() && (button == ((Drag)b).button);
        }
        @Override public boolean inputEventMatches(InputEvent e) {
            if (e instanceof MouseEvent) {
                final MouseEvent me = (MouseEvent)e;
                return (
                        (me.getButton() == button) &&
                                modifiersMatch(me.isShiftDown(), me.isAltDown(), me.isControlDown())
                );
            } else {
                return false;
            }
        }
        @Override public boolean isMouseDrag() { return true; }
        private Drag(MouseButton button, Activity activity, Modifier... modifiers) {
            super(activity, modifiers);
            this.button = button;
        }
        private final MouseButton button;
    }

    /** Private implementation class for the scroll gesture navigation behavior. */
    private static final class Scroll extends NavigationBehavior {
        @Override public boolean inputConditionsMatch(NavigationBehavior b) {
            return super.inputConditionsMatch(b) && b.isGestureScroll();
        }
        @Override public boolean inputEventMatches(InputEvent e) {
            if (e instanceof ScrollEvent) {
                final ScrollEvent se = (ScrollEvent)e;
                return modifiersMatch(se.isShiftDown(), se.isAltDown(), se.isControlDown());
            } else {
                return false;
            }
        }
        @Override public boolean isGestureScroll() { return true; }
        private Scroll(Activity activity, Modifier... modifiers) {
            super(activity, modifiers);
        }
    }

    /** Private implementation class for the zoom gesture navigation behavior. */
    private static final class Zoom extends NavigationBehavior {
        @Override public boolean inputConditionsMatch(NavigationBehavior b) {
            return super.inputConditionsMatch(b) && b.isGestureZoom();
        }
        @Override public boolean inputEventMatches(InputEvent e) {
            if (e instanceof ZoomEvent) {
                final ZoomEvent ze = (ZoomEvent)e;
                return modifiersMatch(ze.isShiftDown(), ze.isAltDown(), ze.isControlDown());
            } else {
                return false;
            }
        }
        @Override public boolean isGestureZoom() { return true; }
        private Zoom(Modifier... modifiers) {
            super(Activity.ZOOM, modifiers);
        }
    }

    /** Private implementation class for the rotation gesture navigation behavior. */
    private static final class Rotate extends NavigationBehavior {
        @Override public boolean inputConditionsMatch(NavigationBehavior b) {
            return super.inputConditionsMatch(b) && b.isGestureRotate();
        }
        @Override public boolean inputEventMatches(InputEvent e) {
            if (e instanceof RotateEvent) {
                final RotateEvent re = (RotateEvent)e;
                return modifiersMatch(re.isShiftDown(), re.isAltDown(), re.isControlDown());
            } else {
                return false;
            }
        }
        @Override public boolean isGestureRotate() { return true; }
        private Rotate(Modifier... modifiers) {
            super(Activity.ROTATE, modifiers);
        }
    }

}
