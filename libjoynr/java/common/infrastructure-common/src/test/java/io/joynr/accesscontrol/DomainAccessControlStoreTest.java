package io.joynr.accesscontrol;

/*
 * #%L
 * %%
 * Copyright (C) 2011 - 2017 BMW Car IT GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import joynr.infrastructure.DacTypes.DomainRoleEntry;
import joynr.infrastructure.DacTypes.MasterAccessControlEntry;
import joynr.infrastructure.DacTypes.OwnerAccessControlEntry;
import joynr.infrastructure.DacTypes.Permission;
import joynr.infrastructure.DacTypes.Role;
import joynr.infrastructure.DacTypes.TrustLevel;
import net.sf.ehcache.CacheManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DomainAccessControlStoreTest {

    private static final String WILDCARD = "*";
    private static final String UID1 = "uid1";
    private static final String UID2 = "uid2";
    private static final String DOMAIN1 = "domain1";
    private static final String INTERFACE1 = "interface1";
    private static final String INTERFACEX = "interfaceX";
    private static final String OPERATION1 = "operation1";
    private static final String OPERATIONX = "operationX";

    private static CacheManager cacheManager;
    private static DomainAccessControlStore store;
    private MasterAccessControlEntry expectedMasterAccessControlEntry;
    private OwnerAccessControlEntry expectedOwnerAccessControlEntry;
    private DomainRoleEntry expectedUserDomainRoleEntry;

    @BeforeClass
    public static void setupTestSuite() {
        cacheManager = CacheManager.create();
        store = new DomainAccessControlStoreEhCache(cacheManager, new DefaultDomainAccessControlProvisioning());
    }

    @Before
    public void setup() {
        // instantiate some template objects
        expectedUserDomainRoleEntry = new DomainRoleEntry(UID1, new String[0], Role.OWNER);
        expectedMasterAccessControlEntry = new MasterAccessControlEntry(UID1,
                                                                        DOMAIN1,
                                                                        INTERFACE1,
                                                                        TrustLevel.LOW,
                                                                        new TrustLevel[]{ TrustLevel.MID,
                                                                                TrustLevel.LOW },
                                                                        TrustLevel.LOW,
                                                                        new TrustLevel[]{ TrustLevel.MID,
                                                                                TrustLevel.LOW },
                                                                        OPERATION1,
                                                                        Permission.NO,
                                                                        new Permission[]{ Permission.ASK, Permission.NO });
        expectedOwnerAccessControlEntry = new OwnerAccessControlEntry(UID1,
                                                                      DOMAIN1,
                                                                      INTERFACE1,
                                                                      TrustLevel.LOW,
                                                                      TrustLevel.LOW,
                                                                      OPERATION1,
                                                                      Permission.NO);
    }

    @After
    public void tearDown() {
        cacheManager.removeAllCaches();
    }

    @Test
    public void testGetDomainRoles() throws Exception {
        store.updateDomainRole(expectedUserDomainRoleEntry);

        assertEquals("DRE for UID1 should be the same as expectedOwnerAccessControlEntry",
                     expectedUserDomainRoleEntry,
                     store.getDomainRoles(UID1).get(0));
        assertEquals("DRE for UID1 and Role.OWNER should be the same as expectedOwnerAccessControlEntry",
                     expectedUserDomainRoleEntry,
                     store.getDomainRole(UID1, Role.OWNER));
    }

    @Test
    public void testUpdateDomainRole() throws Exception {
        store.updateDomainRole(expectedUserDomainRoleEntry);
        // update dre role to MASTER
        DomainRoleEntry dre = store.getDomainRoles(UID1).get(0);
        dre.setRole(Role.MASTER);
        store.updateDomainRole(dre);

        assertTrue("UID1 role should be MASTER", store.getDomainRoles(UID1).get(0).getRole().equals(Role.MASTER));
    }

    @Test
    public void testRemoveDomainRole() throws Exception {
        store.updateDomainRole(expectedUserDomainRoleEntry);

        assertTrue("Remove UID1 DRE should return true", store.removeDomainRole(UID1, Role.OWNER));
        assertTrue("There should be no UID1 DREs in DRT any more", store.getDomainRoles(UID1).isEmpty());
    }

    @Test
    public void testGetMasterAce() throws Exception {
        store.updateMasterAccessControlEntry(expectedMasterAccessControlEntry);

        assertEquals("Master ACE associated to UID1 from Master ACL should be the same as expectedMasterAccessControlEntry",
                     expectedMasterAccessControlEntry,
                     store.getMasterAccessControlEntries(UID1).get(0));
        assertEquals("Master ACE associated to DOMAIN1 and INTERFACE1 should be the same as expectedMasterAccessControlEntry",
                     expectedMasterAccessControlEntry,
                     store.getMasterAccessControlEntries(DOMAIN1, INTERFACE1).get(0));
        assertEquals("Master ACE associated to UID1, DOMAIN1 and INTERFACE1 should be the same as expectedMasterAccessControlEntry",
                     expectedMasterAccessControlEntry,
                     store.getMasterAccessControlEntries(UID1, DOMAIN1, INTERFACE1).get(0));

        MasterAccessControlEntry masterAceWithWildcardOperation = new MasterAccessControlEntry(expectedMasterAccessControlEntry);
        masterAceWithWildcardOperation.setOperation(WILDCARD);
        store.updateMasterAccessControlEntry(masterAceWithWildcardOperation);
        int expectedAceCount = 2;
        assertEquals("There are two (2) master ACEs associated to UID1, DOMAIN1 and INTERFACE1",
                     expectedAceCount,
                     store.getMasterAccessControlEntries(UID1, DOMAIN1, INTERFACE1).size());

        MasterAccessControlEntry returnedMasterAce = store.getMasterAccessControlEntry(UID1,
                                                                                       DOMAIN1,
                                                                                       INTERFACE1,
                                                                                       OPERATION1);
        assertEquals("Master ACE associated to UID1, DOMAIN1, INTERFACE1 and OPERATION1 should be the same as expectedMasterAccessControlEntry",
                     expectedMasterAccessControlEntry,
                     returnedMasterAce);
    }

    @Test
    public void testGetMasterAceWithWildcardOperation() throws Exception {
        expectedMasterAccessControlEntry.setOperation(WILDCARD);
        store.updateMasterAccessControlEntry(expectedMasterAccessControlEntry);

        assertEquals("Master ACE associated to UID1, DOMAIN1, INTERFACE1 and OPERATION1 should be the same as expectedMasterAccessControlEntry",
                     expectedMasterAccessControlEntry,
                     store.getMasterAccessControlEntry(UID1, DOMAIN1, INTERFACE1, OPERATION1));
    }

    @Test
    public void testGetEditableMasterAcl() throws Exception {
        expectedUserDomainRoleEntry.setDomains(new String[]{ DOMAIN1 });
        expectedUserDomainRoleEntry.setRole(Role.MASTER);
        store.updateDomainRole(expectedUserDomainRoleEntry);
        store.updateMasterAccessControlEntry(expectedMasterAccessControlEntry);

        assertEquals("Editable master ACE for UID1 should be equal to expectedMasterAccessControlEntry",
                     expectedMasterAccessControlEntry,
                     store.getEditableMasterAccessControlEntries(UID1).get(0));
    }

    @Test
    public void testEditableMasterAccessControlEntryNoMatchingDre() throws Exception {
        expectedMasterAccessControlEntry.setUid(UID2);
        store.updateMasterAccessControlEntry(expectedMasterAccessControlEntry);

        assertTrue("There should be no editable master ACE for UID1 in Master ACL",
                   store.getEditableMasterAccessControlEntries(UID1).isEmpty());
    }

    @Test
    public void testUpdateMasterAccessControlEntry() throws Exception {
        expectedMasterAccessControlEntry.setDefaultConsumerPermission(Permission.YES);
        boolean expectedUpdateResult = true;

        assertEquals("Update master ACE should return true",
                     expectedUpdateResult,
                     store.updateMasterAccessControlEntry(expectedMasterAccessControlEntry));
        assertTrue("After update master ACE for UID1 should have default Permission.YES",
                   store.getMasterAccessControlEntries(UID1)
                        .get(0)
                        .getDefaultConsumerPermission()
                        .equals(Permission.YES));
    }

    @Test
    public void testRemoveMasterAccessControlEntry() throws Exception {
        store.updateMasterAccessControlEntry(expectedMasterAccessControlEntry);
        boolean expectedRemoveResult = true;

        assertEquals("Remove master ACE for given userId, domain, interface and operation should return true",
                     expectedRemoveResult,
                     store.removeMasterAccessControlEntry(UID1, DOMAIN1, INTERFACE1, OPERATION1));
        assertTrue("In Master ACL no master ACE for given domain, interface should remain",
                   store.getMasterAccessControlEntries(DOMAIN1, INTERFACE1).isEmpty());
        assertTrue("In Master ACL no master ACE for UID1 should remain", store.getMasterAccessControlEntries(UID1)
                                                                              .isEmpty());
    }

    @Test
    public void testGetOwnerAccessControlEntry() throws Exception {
        store.updateOwnerAccessControlEntry(expectedOwnerAccessControlEntry);

        assertEquals("Owner ACE for UID1 should be equal to expectedOwnerAccessControlEntry",
                     expectedOwnerAccessControlEntry,
                     store.getOwnerAccessControlEntries(UID1).get(0));
        assertEquals("Owner ACE associated to DOMAIN1 and INTERFACE1 should be the same as expectedOwnerAccessControlEntry",
                     expectedOwnerAccessControlEntry,
                     store.getOwnerAccessControlEntries(DOMAIN1, INTERFACE1).get(0));
        assertEquals("Owner ACE associated to UID1, DOMAIN1 and INTERFACE1 should be the same as expectedOwnerAccessControlEntry",
                     expectedOwnerAccessControlEntry,
                     store.getOwnerAccessControlEntries(UID1, DOMAIN1, INTERFACE1).get(0));
        OwnerAccessControlEntry returnedOwnerAce = store.getOwnerAccessControlEntry(UID1,
                                                                                    DOMAIN1,
                                                                                    INTERFACE1,
                                                                                    OPERATION1);
        assertEquals("Owner ACE associated to UID1, DOMAIN1, INTERFACE1 and OPERATION1 should be the same as expectedOwnerAccessControlEntry",
                     expectedOwnerAccessControlEntry,
                     returnedOwnerAce);
    }

    @Test
    public void testEditableOwnerAccessControlEntry() throws Exception {
        expectedUserDomainRoleEntry.setDomains(new String[]{ DOMAIN1 });
        store.updateDomainRole(expectedUserDomainRoleEntry);
        store.updateOwnerAccessControlEntry(expectedOwnerAccessControlEntry);

        assertEquals("Editable Owner ACE for UID1 should be equal to expectedOwnerAccessControlEntry",
                     expectedOwnerAccessControlEntry,
                     store.getEditableOwnerAccessControlEntries(UID1).get(0));
    }

    @Test
    public void testEditableOwnerAccessControlEntryNoMatchingDre() throws Exception {
        store.updateDomainRole(expectedUserDomainRoleEntry);
        expectedOwnerAccessControlEntry.setUid(UID2);
        store.updateOwnerAccessControlEntry(expectedOwnerAccessControlEntry);

        assertTrue("No editable owner ACEs for UID2 should be in Owner ACL",
                   store.getEditableOwnerAccessControlEntries(UID2).isEmpty());
    }

    @Test
    public void testUpdateOwnerAccessControlEntry() throws Exception {
        expectedOwnerAccessControlEntry.setConsumerPermission(Permission.YES);
        store.updateOwnerAccessControlEntry(expectedOwnerAccessControlEntry);

        assertTrue("Owner ACE for UID1 should have Permission.YES", store.getOwnerAccessControlEntries(UID1)
                                                                         .get(0)
                                                                         .getConsumerPermission()
                                                                         .equals(Permission.YES));
    }

    @Test
    public void testRemoveOwnerAccessControlEntry() throws Exception {
        store.updateOwnerAccessControlEntry(expectedOwnerAccessControlEntry);
        boolean expectedRemoveResult = true;

        assertEquals("Remove owner ACE for given userId, domain, interface and operation should return true",
                     expectedRemoveResult,
                     store.removeOwnerAccessControlEntry(UID1, DOMAIN1, INTERFACE1, OPERATION1));
        assertTrue("In Owner ACL no owner ACE for given domain, interface should remain",
                   store.getOwnerAccessControlEntries(DOMAIN1, INTERFACE1).isEmpty());
        assertTrue("In Owner ACL no owner ACE for UID1 should remain", store.getOwnerAccessControlEntries(UID1)
                                                                            .isEmpty());
    }

    @Test
    public void testGetWildcardUser() throws Exception {
        OwnerAccessControlEntry expectedOwnerAccessControlEntryWildcard = new OwnerAccessControlEntry(WILDCARD,
                                                                                                      DOMAIN1,
                                                                                                      INTERFACEX,
                                                                                                      TrustLevel.HIGH,
                                                                                                      TrustLevel.HIGH,
                                                                                                      OPERATIONX,
                                                                                                      Permission.YES);
        store.updateOwnerAccessControlEntry(expectedOwnerAccessControlEntryWildcard);

        assertTrue("Exactly one owner ACE for WILDCARD user should be in Owner ACL",
                   store.getOwnerAccessControlEntries(WILDCARD).size() == 1);
        assertTrue("In case no USER2_ID ACE found, WILDCARD user ACE should be returned",
                   store.getOwnerAccessControlEntries(UID2).get(0).getUid().equals(WILDCARD));
        assertTrue("Uid of returned owner ACEs associated to DOMAIN1 and INTERFACEX should be WILDCARD",
                   store.getOwnerAccessControlEntries(DOMAIN1, INTERFACEX).get(0).getUid().equals(WILDCARD));
    }
}
