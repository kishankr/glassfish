/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.enterprise.resource;

import com.sun.appserv.connectors.internal.api.ConnectorConstants;
import com.sun.enterprise.connectors.PoolMetaData;

import java.io.Serializable;

/**
 * ResourceSpec is used as a key to locate the correct resource pool
 */

public class ResourceSpec implements Serializable {
    private String resourceId;
    private int resourceIdType;

    private boolean pmResource;
    private boolean nonTxResource;
    private boolean isXA_;

    private String connectionPoolName;

    static public final int JDBC_URL = 0;
    static public final int JNDI_NAME = 1;
    static public final int JMS = 2;

    public ResourceSpec(String resourceId,
                        int resourceIdType) {
        if (resourceId == null) throw new NullPointerException();
        this.resourceId = resourceId;
        this.resourceIdType = resourceIdType;

        if (resourceId.endsWith(ConnectorConstants.NON_TX_JNDI_SUFFIX)) {
            nonTxResource = true;
        }

        if (resourceId.endsWith(ConnectorConstants.PM_JNDI_SUFFIX) ) {
            pmResource = true;
        }

    }

    public ResourceSpec(String resourceId, int resourceIdType,
                        PoolMetaData pmd) {
        this(resourceId, resourceIdType);


        if ( pmd.isPM() ) {
            pmResource = true;
        }

        if (pmd.isNonTx()) {
            nonTxResource = true;
        }
    }

    public String getConnectionPoolName() {
        return connectionPoolName;
    }


    public void setConnectionPoolName(String name) {
        connectionPoolName = name;
    }

    /**
     * The  logic is                                              *
     * If the connectionpool exist then equality check is against *
     * connectionPoolName                                         *
     * *
     * If connection is null then equality check is made against  *
     * resourceId and resourceType                                *
     */

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other instanceof ResourceSpec) {
            ResourceSpec obj = (ResourceSpec) other;
            if (connectionPoolName == null) {
                return (resourceId.equals(obj.resourceId) &&
                        resourceIdType == obj.resourceIdType);
            } else {
                return (connectionPoolName.equals(obj.connectionPoolName));

            }
        } else {
            return false;
        }
    }

    /**
     * If the connectionpool exist then hashcode of connectionPoolName
     * is returned.
     * <p/>
     * If connectionpool is null return the hashcode of
     * resourceId + resourceIdType
     */
    public int hashCode() {
        if (connectionPoolName == null) {
            return resourceId.hashCode() + resourceIdType;
        } else {
            return connectionPoolName.hashCode();
        }
    }

    public String getResourceId() {
        return resourceId;
    }

    public boolean isPM() {
        return pmResource;
    }
    
    /**
     * Returns the status of the noTxResource flag
     *
     * @return true if this resource is a noTx resource
     */

    public boolean isNonTx() {
        return nonTxResource;
    }

    public boolean isXA() {
        return isXA_;
    }

    public void markAsXA() {
        isXA_ = true;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer("ResourceSpec :- ");
        sb.append("\nconnectionPoolName : ").append(connectionPoolName);
        sb.append("\nisXA_ : ").append(isXA_);
        sb.append("\nresoureId : ").append(resourceId);
        sb.append("\nresoureIdType : ").append(resourceIdType);
        sb.append("\npmResource : ").append(pmResource);
        sb.append("\nnonTxResource : ").append(nonTxResource);

        return sb.toString();
    }
}
