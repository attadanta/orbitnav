package org.arcball.internal;

public interface Attachable {
    
    /**
     * Attaches to a host.
     *  
     * @param host host to which attachment should be made
     */
    void attachToHost(InteractionHost host);
    
    /**
     * Detaches from a host.
     * <p>
     * This method should check that the <code>Attachable</code> was previously attached to the host. 
     * 
     * @param host host from which to detach
     */
    void detachFromHost(InteractionHost host);
    
}
