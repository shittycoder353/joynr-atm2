package io.joynr.accesscontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

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

import io.joynr.accesscontrol.primarykey.UserDomainInterfaceOperationKey;
import io.joynr.accesscontrol.primarykey.UserRoleKey;
import joynr.infrastructure.DacTypes.ControlEntry;
import joynr.infrastructure.DacTypes.DomainRoleEntry;
import joynr.infrastructure.DacTypes.MasterAccessControlEntry;
import joynr.infrastructure.DacTypes.OwnerAccessControlEntry;
import joynr.infrastructure.DacTypes.Role;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

/**
 * Uses EhCache to implement a GlobalDomainAccessStore.
 * Add/Remove operations can be expensive. Get operations should be fast.
 */
public class DomainAccessControlStoreEhCache implements DomainAccessControlStore {
    private static final Logger logger = LoggerFactory.getLogger(DomainAccessControlStoreEhCache.class);
    private static final String WILDCARD = "*";
    private final CacheManager cacheManager;

    public enum CacheId {

        MASTER_ACL("io.joynr.MasterACL"), OWNER_ACL("io.joynr.OwnerACL"), MEDIATOR_ACL("io.joynr.MediatorACL"), DOMAIN_ROLES(
                "io.joynr.DomainRoleTable");

        private final String idAsString;

        CacheId(String id) {
            this.idAsString = id;
        }

        public String getIdAsString() {
            return idAsString;
        }
    }

    @Inject
    public DomainAccessControlStoreEhCache(CacheManager ehCacheManager,
                                           DomainAccessControlProvisioning domainAccessControlProvisioning) {
        this.cacheManager = ehCacheManager;
        Collection<DomainRoleEntry> domainRoleEntries = domainAccessControlProvisioning.getDomainRoleEntries();
        for (DomainRoleEntry provisionedDomainRoleEntry : domainRoleEntries) {
            updateDomainRole(provisionedDomainRoleEntry);
        }

        Collection<MasterAccessControlEntry> masterAccessControlEntries = domainAccessControlProvisioning.getMasterAccessControlEntries();
        for (MasterAccessControlEntry provisionedMasterAccessControlEntry : masterAccessControlEntries) {
            updateMasterAccessControlEntry(provisionedMasterAccessControlEntry);
        }
    }

    @Override
    public List<DomainRoleEntry> getDomainRoles(String uid) {
        Cache cache = getCache(CacheId.DOMAIN_ROLES);
        List<DomainRoleEntry> domainRoles = new ArrayList<DomainRoleEntry>();
        Attribute<String> uidAttribute = cache.getSearchAttribute(UserRoleKey.USER_ID);
        // query is the fastest if you search for keys and if you need value then call Cache.get(key)
        Query queryRequestedUid = cache.createQuery().addCriteria(uidAttribute.eq(uid)).includeKeys().end();
        Results results = queryRequestedUid.execute();
        for (Result result : results.all()) {
            domainRoles.add(DomainAccessControlStoreEhCache.<DomainRoleEntry> getElementValue(cache.get(result.getKey())));
        }

        return domainRoles;
    }

    @Override
    public DomainRoleEntry getDomainRole(String uid, Role role) {
        Cache cache = getCache(CacheId.DOMAIN_ROLES);
        Attribute<String> uidAttribute = cache.getSearchAttribute(UserRoleKey.USER_ID);
        Attribute<Role> roleAttribute = cache.getSearchAttribute(UserRoleKey.ROLE);
        // query is the fastest if you search for keys and if you need value then call Cache.get(key)
        Query queryRequestedUid = cache.createQuery()
                                       .addCriteria(uidAttribute.eq(uid))
                                       .addCriteria(roleAttribute.eq(role))
                                       .includeKeys()
                                       .end();
        Results results = queryRequestedUid.execute();
        DomainRoleEntry domainRole = null;
        if (!results.all().isEmpty()) {
            // Note: since (uid, role) is the primary key in domain role table
            // results is either empty or contains exactly one entry
            assert (results.all().size() == 1);
            domainRole = (DomainAccessControlStoreEhCache.<DomainRoleEntry> getElementValue(cache.get(results.all()
                                                                                                             .get(0)
                                                                                                             .getKey())));
        }

        return domainRole;
    }

    @Override
    public Boolean updateDomainRole(@Nonnull DomainRoleEntry updatedEntry) {
        boolean updateSuccess = false;
        Cache cache = getCache(CacheId.DOMAIN_ROLES);
        UserRoleKey dreKey = new UserRoleKey(updatedEntry.getUid(), updatedEntry.getRole());
        try {
            cache.put(new Element(dreKey, updatedEntry));
            updateSuccess = true;
        } catch (IllegalArgumentException | IllegalStateException | CacheException e) {
            logger.error("updateDomainRole failed.", e);
        }

        return updateSuccess;
    }

    @Override
    public Boolean removeDomainRole(String uid, Role role) {
        UserRoleKey dreKey = new UserRoleKey(uid, role);
        return removeAce(CacheId.DOMAIN_ROLES, dreKey);
    }

    @Override
    public List<MasterAccessControlEntry> getMasterAccessControlEntries(String uid) {
        return getAces(uid, CacheId.MASTER_ACL);
    }

    @Override
    public List<MasterAccessControlEntry> getEditableMasterAccessControlEntries(String uid) {
        return getEditableAces(uid, CacheId.MASTER_ACL, Role.MASTER);
    }

    @Override
    public List<MasterAccessControlEntry> getMasterAccessControlEntries(String domain, String interfaceName) {
        return getAces(domain, interfaceName, CacheId.MASTER_ACL);
    }

    @Override
    public List<MasterAccessControlEntry> getMasterAccessControlEntries(String uid, String domain, String interfaceName) {
        return getAces(CacheId.MASTER_ACL, uid, domain, interfaceName);
    }

    @Override
    public MasterAccessControlEntry getMasterAccessControlEntry(String uid,
                                                                String domain,
                                                                String interfaceName,
                                                                String operation) {
        MasterAccessControlEntry masterAce = getAce(CacheId.MASTER_ACL, uid, domain, interfaceName, operation);
        if (masterAce == null) {
            masterAce = getAce(CacheId.MASTER_ACL, uid, domain, interfaceName, WILDCARD);
        }

        return masterAce;
    }

    @Override
    public Boolean updateMasterAccessControlEntry(MasterAccessControlEntry updatedMasterAce) {
        boolean updateSuccess = false;
        UserDomainInterfaceOperationKey aceKey = new UserDomainInterfaceOperationKey(updatedMasterAce.getUid(),
                                                                                     updatedMasterAce.getDomain(),
                                                                                     updatedMasterAce.getInterfaceName(),
                                                                                     updatedMasterAce.getOperation());
        updateSuccess = updateAce(updatedMasterAce, CacheId.MASTER_ACL, aceKey);

        return updateSuccess;
    }

    @Override
    public Boolean removeMasterAccessControlEntry(String uid, String domain, String interfaceName, String operation) {
        UserDomainInterfaceOperationKey aceKey = new UserDomainInterfaceOperationKey(uid,
                                                                                     domain,
                                                                                     interfaceName,
                                                                                     operation);
        return removeAce(CacheId.MASTER_ACL, aceKey);
    }

    @Override
    public List<MasterAccessControlEntry> getMediatorAccessControlEntries(String uid) {
        return getAces(uid, CacheId.MEDIATOR_ACL);
    }

    @Override
    public List<MasterAccessControlEntry> getEditableMediatorAccessControlEntries(String uid) {
        return getEditableAces(uid, CacheId.MEDIATOR_ACL, Role.MASTER);
    }

    @Override
    public List<MasterAccessControlEntry> getMediatorAccessControlEntries(String domain, String interfaceName) {
        return getAces(domain, interfaceName, CacheId.MEDIATOR_ACL);
    }

    @Override
    public List<MasterAccessControlEntry> getMediatorAccessControlEntries(String uid,
                                                                          String domain,
                                                                          String interfaceName) {
        return getAces(CacheId.MEDIATOR_ACL, uid, domain, interfaceName);
    }

    @Override
    public MasterAccessControlEntry getMediatorAccessControlEntry(String uid,
                                                                  String domain,
                                                                  String interfaceName,
                                                                  String operation) {
        MasterAccessControlEntry mediatorAce = getAce(CacheId.MEDIATOR_ACL, uid, domain, interfaceName, operation);
        if (mediatorAce == null) {
            mediatorAce = getAce(CacheId.MEDIATOR_ACL, uid, domain, interfaceName, WILDCARD);
        }

        return mediatorAce;
    }

    @Override
    public Boolean updateMediatorAccessControlEntry(MasterAccessControlEntry updatedMediatorAce) {

        boolean updateSuccess = false;
        MasterAccessControlEntry masterAce = getMasterAccessControlEntry(updatedMediatorAce.getUid(),
                                                                         updatedMediatorAce.getDomain(),
                                                                         updatedMediatorAce.getInterfaceName(),
                                                                         updatedMediatorAce.getOperation());

        AceValidator aceValidator = new AceValidator(masterAce, updatedMediatorAce, null);
        if (aceValidator.isMediatorValid()) {
            UserDomainInterfaceOperationKey aceKey = new UserDomainInterfaceOperationKey(updatedMediatorAce.getUid(),
                                                                                         updatedMediatorAce.getDomain(),
                                                                                         updatedMediatorAce.getInterfaceName(),
                                                                                         updatedMediatorAce.getOperation());
            updateSuccess = updateAce(updatedMediatorAce, CacheId.MASTER_ACL, aceKey);
        }

        return updateSuccess;
    }

    @Override
    public Boolean removeMediatorAccessControlEntry(String uid, String domain, String interfaceName, String operation) {
        UserDomainInterfaceOperationKey aceKey = new UserDomainInterfaceOperationKey(uid,
                                                                                     domain,
                                                                                     interfaceName,
                                                                                     operation);
        return removeAce(CacheId.MEDIATOR_ACL, aceKey);
    }

    @Override
    public List<OwnerAccessControlEntry> getOwnerAccessControlEntries(String uid) {
        return getAces(uid, CacheId.OWNER_ACL);
    }

    @Override
    public List<OwnerAccessControlEntry> getEditableOwnerAccessControlEntries(String uid) {
        return getEditableAces(uid, CacheId.OWNER_ACL, Role.OWNER);
    }

    @Override
    public List<OwnerAccessControlEntry> getOwnerAccessControlEntries(String domain, String interfaceName) {
        return getAces(domain, interfaceName, CacheId.OWNER_ACL);
    }

    @Override
    public List<OwnerAccessControlEntry> getOwnerAccessControlEntries(String uid, String domain, String interfaceName) {
        return getAces(CacheId.OWNER_ACL, uid, domain, interfaceName);
    }

    @Override
    public OwnerAccessControlEntry getOwnerAccessControlEntry(String uid,
                                                              String domain,
                                                              String interfaceName,
                                                              String operation) {
        OwnerAccessControlEntry ownerAce = getAce(CacheId.OWNER_ACL, uid, domain, interfaceName, operation);
        if (ownerAce == null) {
            ownerAce = getAce(CacheId.OWNER_ACL, uid, domain, interfaceName, WILDCARD);
        }

        return ownerAce;
    }

    @Override
    public Boolean updateOwnerAccessControlEntry(OwnerAccessControlEntry updatedOwnerAce) {
        boolean updateSuccess = false;
        MasterAccessControlEntry masterAce = getMasterAccessControlEntry(updatedOwnerAce.getUid(),
                                                                         updatedOwnerAce.getDomain(),
                                                                         updatedOwnerAce.getInterfaceName(),
                                                                         updatedOwnerAce.getOperation());

        MasterAccessControlEntry mediatorAce = getMediatorAccessControlEntry(updatedOwnerAce.getUid(),
                                                                             updatedOwnerAce.getDomain(),
                                                                             updatedOwnerAce.getInterfaceName(),
                                                                             updatedOwnerAce.getOperation());

        AceValidator aceValidator = new AceValidator(masterAce, mediatorAce, updatedOwnerAce);
        if (aceValidator.isOwnerValid()) {
            UserDomainInterfaceOperationKey aceKey = new UserDomainInterfaceOperationKey(updatedOwnerAce.getUid(),
                                                                                         updatedOwnerAce.getDomain(),
                                                                                         updatedOwnerAce.getInterfaceName(),
                                                                                         updatedOwnerAce.getOperation());
            updateSuccess = updateAce(updatedOwnerAce, CacheId.OWNER_ACL, aceKey);
        }

        return updateSuccess;
    }

    @Override
    public Boolean removeOwnerAccessControlEntry(String uid, String domain, String interfaceName, String operation) {
        UserDomainInterfaceOperationKey aceKey = new UserDomainInterfaceOperationKey(uid,
                                                                                     domain,
                                                                                     interfaceName,
                                                                                     operation);
        return removeAce(CacheId.OWNER_ACL, aceKey);
    }

    private <T extends ControlEntry> T getAce(CacheId cacheId,
                                              String uid,
                                              String domain,
                                              String interfaceName,
                                              String operation) {
        Cache cache = getCache(cacheId);
        Attribute<String> uidAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.USER_ID);
        Attribute<String> domainAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.DOMAIN);
        Attribute<String> interfaceAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.INTERFACE);
        Attribute<String> operationAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.OPERATION);
        Query queryAllOperations = cache.createQuery()
                                        .addCriteria(uidAttribute.eq(uid).or(uidAttribute.eq(WILDCARD)))
                                        .addCriteria(domainAttribute.eq(domain))
                                        .addCriteria(interfaceAttribute.eq(interfaceName))
                                        .addCriteria(operationAttribute.eq(operation))
                                        // have specific user ids appear before wildcards
                                        .addOrderBy(uidAttribute, Direction.DESCENDING)
                                        .includeKeys()
                                        .end();
        Results results = queryAllOperations.execute();
        T ace = null;
        if (!results.all().isEmpty()) {
            ace = DomainAccessControlStoreEhCache.<T> getElementValue(cache.get(results.all().get(0).getKey()));
        }

        return ace;
    }

    private <T extends ControlEntry> List<T> getAces(String uid, CacheId cacheId) {
        Cache cache = getCache(cacheId);
        List<T> aces = new ArrayList<T>();
        // here search on uid take place
        Attribute<String> uidAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.USER_ID);
        // query is the fastest if you search for keys and if you need value then call Cache.get(key)
        Query queryRequestedUid = cache.createQuery().addCriteria(uidAttribute.eq(uid).or(uidAttribute.eq(WILDCARD)))
        // have specific user ids appear before wildcards
                                       .addOrderBy(uidAttribute, Direction.DESCENDING)
                                       .includeKeys()
                                       .end();
        Results results = queryRequestedUid.execute();
        for (Result result : results.all()) {
            aces.add(DomainAccessControlStoreEhCache.<T> getElementValue(cache.get(result.getKey())));
        }

        return aces;
    }

    private <T extends ControlEntry> List<T> getAces(String domain, String interfaceName, CacheId cacheId) {
        Cache cache = getCache(cacheId);
        List<T> aces = new ArrayList<T>();
        // here search on domain and interface take place
        Attribute<String> domainAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.DOMAIN);
        Attribute<String> interfaceAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.INTERFACE);
        // query is the fastest if you search for keys and if you need value then call Cache.get(key)
        Query queryDomainInterface = cache.createQuery()
                                          .addCriteria(domainAttribute.eq(domain)
                                                                      .and(interfaceAttribute.eq(interfaceName)))
                                          .includeKeys()
                                          .end();
        Results results = queryDomainInterface.execute();
        for (Result result : results.all()) {
            T ace = DomainAccessControlStoreEhCache.<T> getElementValue(cache.get(result.getKey()));
            aces.add(ace);
        }

        return aces;
    }

    private <T extends ControlEntry> List<T> getAces(CacheId cacheId, String uid, String domain, String interfaceName) {
        Cache cache = getCache(cacheId);
        Attribute<String> uidAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.USER_ID);
        Attribute<String> domainAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.DOMAIN);
        Attribute<String> interfaceAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.INTERFACE);
        Query queryAllOperations = cache.createQuery()
                                        .addCriteria(uidAttribute.eq(uid).or(uidAttribute.eq(WILDCARD)))
                                        .addCriteria(domainAttribute.eq(domain))
                                        .addCriteria(interfaceAttribute.eq(interfaceName))
                                        // have specific user ids appear before wildcards
                                        .addOrderBy(uidAttribute, Direction.DESCENDING)
                                        .includeKeys()
                                        .end();
        Results results = queryAllOperations.execute();
        List<T> aces = new ArrayList<T>();
        String currentUid = null;
        for (Result result : results.all()) {
            T ace = DomainAccessControlStoreEhCache.<T> getElementValue(cache.get(result.getKey()));

            // Don't add uid wildcards if a specific uid has been added to the results
            if (currentUid == null) {
                currentUid = ace.getUid();
            } else if (!currentUid.equals(ace.getUid())) {
                break;
            }

            aces.add(ace);
        }

        return aces;
    }

    private <T extends ControlEntry> List<T> getEditableAces(String uid, CacheId cacheId, Role role) {
        List<T> aces = new ArrayList<T>();
        // find out first on which domains uid has specified role
        Cache drtCache = getCache(CacheId.DOMAIN_ROLES);
        UserRoleKey dreKey = new UserRoleKey(uid, role);
        String[] uidDomains = null;
        // read domains from DRE
        if (drtCache.isKeyInCache(dreKey)) {
            DomainRoleEntry dre = DomainAccessControlStoreEhCache.<DomainRoleEntry> getElementValue(drtCache.get(dreKey));
            uidDomains = dre.getDomains();
        }
        // if uid has no domains with specified role return empty list
        if (uidDomains == null || uidDomains.length == 0) {
            return aces;
        }

        Cache cache = getCache(cacheId);
        // here should search on uid and domain take place
        Attribute<String> uidAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.USER_ID);
        Attribute<String> domainAttribute = cache.getSearchAttribute(UserDomainInterfaceOperationKey.DOMAIN);
        for (String domain : uidDomains) {
            Query query = cache.createQuery()
                               .addCriteria(uidAttribute.eq(uid).and(domainAttribute.eq(domain)))
                               .includeKeys()
                               .end();
            Results results = query.execute();
            for (Result result : results.all()) {
                aces.add(DomainAccessControlStoreEhCache.<T> getElementValue(cache.get(result.getKey())));
            }

        }

        return aces;
    }

    private <T extends ControlEntry> Boolean updateAce(T accessControlEntry, CacheId cacheId, Object aceKey) {
        Cache cache = getCache(cacheId);
        boolean updateSuccess = false;
        try {
            cache.put(new Element(aceKey, accessControlEntry));
            updateSuccess = true;
        } catch (IllegalArgumentException | IllegalStateException | CacheException e) {
            logger.error("update " + cacheId + "failed.", e);
        }

        return updateSuccess;
    }

    private boolean removeAce(CacheId cacheId, Object aceKey) {
        Cache cache = getCache(cacheId);
        boolean removeResult = false;
        try {
            removeResult = cache.remove(aceKey);
        } catch (IllegalArgumentException | IllegalStateException | CacheException e) {
            logger.error("remove " + cacheId + " failed.", e);
        }

        return removeResult;
    }

    protected Cache getCache(CacheId cacheId) {
        Cache cache = cacheManager.getCache(cacheId.getIdAsString());
        if (cache == null) {
            switch (cacheId) {
            case MASTER_ACL:
            case MEDIATOR_ACL:
            case OWNER_ACL: {
                cache = createAclCache(cacheId);
                break;
            }
            case DOMAIN_ROLES: {
                cache = createDrtCache();
                break;
            }
            default: {
                break;
            }
            }
        }

        return cache;
    }

    private Cache createAclCache(CacheId cacheId) {
        // configure cache as searchable
        CacheConfiguration cacheConfig = new CacheConfiguration(cacheId.getIdAsString(), 0).eternal(true);
        Searchable searchable = new Searchable();
        cacheConfig.addSearchable(searchable);
        // register searchable attributes
        searchable.addSearchAttribute(new SearchAttribute().name(UserDomainInterfaceOperationKey.USER_ID));
        searchable.addSearchAttribute(new SearchAttribute().name(UserDomainInterfaceOperationKey.DOMAIN));
        searchable.addSearchAttribute(new SearchAttribute().name(UserDomainInterfaceOperationKey.INTERFACE));
        searchable.addSearchAttribute(new SearchAttribute().name(UserDomainInterfaceOperationKey.OPERATION));
        cacheManager.addCache(new Cache(cacheConfig));
        return cacheManager.getCache(cacheId.getIdAsString());
    }

    private Cache createDrtCache() {
        // configure cache as searchable
        CacheConfiguration cacheConfig = new CacheConfiguration(CacheId.DOMAIN_ROLES.getIdAsString(), 0).eternal(true);
        Searchable searchable = new Searchable();
        cacheConfig.addSearchable(searchable);
        // register searchable attributes
        searchable.addSearchAttribute(new SearchAttribute().name(UserRoleKey.USER_ID));
        searchable.addSearchAttribute(new SearchAttribute().name(UserRoleKey.ROLE));
        cacheManager.addCache(new Cache(cacheConfig));
        return cacheManager.getCache(CacheId.DOMAIN_ROLES.getIdAsString());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getElementValue(Element e) {
        return (T) e.getObjectValue();
    }
}
