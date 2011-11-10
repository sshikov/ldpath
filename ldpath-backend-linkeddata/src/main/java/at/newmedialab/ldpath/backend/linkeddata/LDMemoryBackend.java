/*
 * Copyright (c) 2011 Salzburg Research.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.newmedialab.ldpath.backend.linkeddata;

import at.newmedialab.ldclient.api.LDCacheProvider;
import at.newmedialab.ldclient.model.CacheEntry;
import at.newmedialab.ldclient.service.LDCache;
import at.newmedialab.ldpath.backend.sesame.GenericSesameBackend;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class LDMemoryBackend extends GenericSesameBackend implements LDCacheProvider {

    private static final Logger log = LoggerFactory.getLogger(LDMemoryBackend.class);

    private Map<String,CacheEntry> cacheEntries;

    private LDCache ldCache;

    public LDMemoryBackend() {
        super();

        cacheEntries = new HashMap<String,CacheEntry>();

        try {
            Repository repository = new SailRepository(new MemoryStore());
            repository.initialize();
            setRepository(repository);

        } catch (RepositoryException e) {
            log.error("error initialising connection to Sesame in-memory repository",e);
        }

        ldCache = new LDCache(this);
    }


    /**
     * Return the sesame repository used for storing the triples that are retrieved from the Linked Data Cloud.
     * Triples will always be added to the context http://www.newmedialab.at/ldclient/cache to be able to distinguish
     * them from other triples.
     *
     * @return an initialised Sesame repository that can be used for caching triples
     */
    @Override
    public Repository getTripleRepository() {
        return getRepository();
    }

    /**
     * Return a map that can be used to store caching metadata about resources. The LDCacheProvider should take care
     * of persisting the metadata if desired.
     *
     * @return a map for storing caching metadata
     */
    @Override
    public Map<String,CacheEntry> getMetadataRepository() {
        return cacheEntries;
    }

    /**
     * List the objects of triples in the triple store underlying this backend that have the subject and
     * property given as argument.
     *
     * @param subject  the subject of the triples to look for
     * @param property the property of the triples to look for
     * @return all objects of triples with matching subject and property
     */
    @Override
    public Collection<Value> listObjects(Value subject, Value property) {
        if(isURI(subject)) {
            ldCache.refreshResource((URI)subject);
        }
        return super.listObjects(subject, property);
    }

    /**
     * List the subjects of triples in the triple store underlying this backend that have the object and
     * property given as argument.
     *
     * @param object   the object of the triples to look for
     * @param property the property of the triples to look for
     * @return all dubjects of triples with matching object and property
     * @throws UnsupportedOperationException in case reverse selection is not supported (e.g. when querying Linked Data)
     */
    @Override
    public Collection<Value> listSubjects(Value property, Value object) {
        throw new IllegalArgumentException("reverse navigation not supported by Linked Data backend");
    }
}
