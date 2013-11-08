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
package org.orbitnav.internal;

/**
 * Attachable objects can be attached and detached from a {@link Host Host}.
 *
 * <p>
 * Hosts are normally JavaFX <code>Scene</code> or <code>SubScene</code> objects.  They provide event dispatching for
 * the kinds of events that {@link Interaction Interactions} can respond to, as well as width and height information.
 * Classes that implement <code>Attachable</code> can then be attached-to or detached-from {@link Host Hosts} to enable
 * or disable their functionality.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public interface Attachable {
    
    /**
     * Attaches to a host.
     *  
     * @param host host to which attachment should be made
     */
    void attachToHost(Host host);
    
    /**
     * Detaches from a host.
     * <p>
     * This method should check that the <code>Attachable</code> was previously attached to the host. 
     * 
     * @param host host from which to detach
     */
    void detachFromHost(Host host);
    
}
