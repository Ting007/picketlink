/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.picketlink.test.idm;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.Role;
import org.picketlink.idm.model.SimpleRole;

/**
 * <p>
 * Test case for {@link Role} basic management operations.
 * </p>
 * 
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 * 
 */
public class RoleManagementTestCase {
    
    private IdentityManager identityManager;
    
    /**
     * <p>
     * Creates a new {@link Role} instance using the API. This method also checks if the user was properly created by retrieving
     * his information from the store.
     * </p>
     * 
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        IdentityManager identityManager = getIdentityManager();

        Role newRoleInstance = new SimpleRole("someRole");

        // let's create the new role
        identityManager.createRole(newRoleInstance);

        // let's retrieve the role information and see if they are properly stored
        Role storedRoleInstance = identityManager.getRole(newRoleInstance.getName());

        assertNotNull(storedRoleInstance);
        
        assertEquals(newRoleInstance.getKey(), storedRoleInstance.getKey());
        assertEquals(newRoleInstance.getName(), storedRoleInstance.getName());
    }

    /**
     * <p>Loads from the LDAP tree an already stored role.</p>
     * 
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        IdentityManager identityManager = getIdentityManager();

        Role storedRoleInstance = identityManager.getRole("Administrator");

        assertNotNull(storedRoleInstance);

        assertEquals("ROLE://Administrator", storedRoleInstance.getKey());
        assertEquals("Administrator", storedRoleInstance.getName());
    }
    
    /**
     * <p>Remove from the LDAP tree an already stored role.</p>
     * 
     * @throws Exception
     */
    @Test
    public void testRemove() throws Exception {
        IdentityManager identityManager = getIdentityManager();

        Role storedRoleInstance = identityManager.getRole("Administrator");

        assertNotNull(storedRoleInstance);
        
        identityManager.removeRole(storedRoleInstance);
        
        Role removedRoleInstance = identityManager.getRole("Administrator");
        
        assertNull(removedRoleInstance);
    }
    
    /**
     * <p>Sets an one-valued attribute.</p>
     * 
     * @throws Exception
     */
    @Test
    public void testSetOneValuedAttribute() throws Exception {
        IdentityManager identityManager = getIdentityManager();

        Role storedRoleInstance = identityManager.getRole("Administrator");
        
        storedRoleInstance.setAttribute(new Attribute<String>("one-valued", "1"));
        
        identityManager.updateRole(storedRoleInstance);
        
        Role updatedRoleInstance = identityManager.getRole(storedRoleInstance.getName());
        
        Attribute<String> oneValuedAttribute = updatedRoleInstance.getAttribute("one-valued");
        
        assertNotNull(oneValuedAttribute);
        assertEquals("1", oneValuedAttribute.getValue());
    }
    
    /**
     * <p>Sets a multi-valued attribute.</p>
     * 
     * @throws Exception
     */
    @Test
    public void testSetMultiValuedAttribute() throws Exception {
        IdentityManager identityManager = getIdentityManager();

        Role storedRoleInstance = identityManager.getRole("Administrator");
        
        storedRoleInstance.setAttribute(new Attribute<String[]>("multi-valued", new String[] {"1", "2", "3"}));
        
        identityManager.updateRole(storedRoleInstance);
        
        Role updatedRoleInstance = identityManager.getRole(storedRoleInstance.getName());
        
        Attribute<String[]> multiValuedAttribute = updatedRoleInstance.getAttribute("multi-valued");
        
        assertNotNull(multiValuedAttribute);
        assertEquals("1", multiValuedAttribute.getValue()[0]);
        assertEquals("2", multiValuedAttribute.getValue()[1]);
        assertEquals("3", multiValuedAttribute.getValue()[2]);
    }
    
    /**
     * <p>Updates an attribute.</p>
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateAttribute() throws Exception {
        IdentityManager identityManager = getIdentityManager();

        Role storedRoleInstance = identityManager.getRole("Administrator");
        
        storedRoleInstance.setAttribute(new Attribute<String[]>("multi-valued", new String[] {"1", "2", "3"}));
        
        identityManager.updateRole(storedRoleInstance);
        
        Role updatedRoleInstance = identityManager.getRole(storedRoleInstance.getName());
        
        Attribute<String[]> multiValuedAttribute = updatedRoleInstance.getAttribute("multi-valued");
        
        assertNotNull(multiValuedAttribute);

        multiValuedAttribute.setValue(new String[] {"3", "4", "5"});
        
        updatedRoleInstance.setAttribute(multiValuedAttribute);
        
        identityManager.updateRole(updatedRoleInstance);
        
        updatedRoleInstance = identityManager.getRole("Administrator");
        
        multiValuedAttribute = updatedRoleInstance.getAttribute("multi-valued");
        
        assertNotNull(multiValuedAttribute);
        assertEquals("3", multiValuedAttribute.getValue()[0]);
        assertEquals("4", multiValuedAttribute.getValue()[1]);
        assertEquals("5", multiValuedAttribute.getValue()[2]);
    }
    
    /**
     * <p>Removes an attribute.</p>
     * 
     * @throws Exception
     */
    @Test
    public void testRemoveAttribute() throws Exception {
        IdentityManager identityManager = getIdentityManager();

        Role storedRoleInstance = identityManager.getRole("Administrator");
        
        storedRoleInstance.setAttribute(new Attribute<String[]>("multi-valued", new String[] {"1", "2", "3"}));
        
        identityManager.updateRole(storedRoleInstance);
        
        Role updatedRoleInstance = identityManager.getRole(storedRoleInstance.getName());
        
        Attribute<String[]> multiValuedAttribute = updatedRoleInstance.getAttribute("multi-valued");
        
        assertNotNull(multiValuedAttribute);
        
        updatedRoleInstance.removeAttribute("multi-valued");
        
        identityManager.updateRole(updatedRoleInstance);
        
        updatedRoleInstance = identityManager.getRole("Administrator");
        
        multiValuedAttribute = updatedRoleInstance.getAttribute("multi-valued");
        
        assertNull(multiValuedAttribute);
    }
    
    public IdentityManager getIdentityManager() {
        return this.identityManager;
    }
    
    public void setIdentityManager(IdentityManager identityManager) {
        this.identityManager = identityManager;
    }
}