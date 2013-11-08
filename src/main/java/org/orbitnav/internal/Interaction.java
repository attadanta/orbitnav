package org.orbitnav.internal;

import javafx.beans.property.ObjectProperty;
import org.orbitnav.NavigationBehavior;

/**
 * Interaction interface.
 *
 * <p>
 * Interactions extend {@link Attachable Attachable}, allowing them to be attached and detached from a
 * {@link Host Host}.  They also have a {@link NavigationBehavior NavigationBehavior} property, which associates
 * other required information such as modifier keys, mouse buttons, etc.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public interface Interaction extends Attachable {

    /**
     * Returns the navigation behavior object property.
     *
     * @return navigation behavior object property
     */
    public ObjectProperty<NavigationBehavior> navigationBehaviorProperty();

    /**
     * Sets the {@link NavigationBehavior NavigationBehavior} associated with this <code>Interaction</code>.
     *
     * @param nb navigation behavior
     */
    public void setNavigationBehavior(NavigationBehavior nb);

    /**
     * Returns the {@link NavigationBehavior NavigationBehavior} associated with this <code>Interaction</code>.
     *
     * @return navigation behavior
     */
    public NavigationBehavior getNavigationBehavior();

}
