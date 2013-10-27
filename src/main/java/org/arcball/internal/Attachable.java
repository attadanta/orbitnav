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
