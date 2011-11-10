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

package at.newmedialab.ldclient.api;

import at.newmedialab.ldclient.model.CacheEntry;
import org.openrdf.repository.Repository;

import java.util.Map;

/**
 * An interface specifying a factory that provides different caching mechanisms, e.g. in-memory or file storage.
 * A cache provider typically offers two kinds of caching services, one for the retrieved triples (a sesame repository)
 * and one for the caching metadata (a map from URIs to CacheEntry).
 * <p/>
 * Author: Sebastian Schaffert
 */
public interface LDCacheProvider {

    /**
     * Return the sesame repository used for storing the triples that are retrieved from the Linked Data Cloud.
     * Triples will always be added to the context http://www.newmedialab.at/ldclient/cache to be able to distinguish
     * them from other triples.
     *
     * @return an initialised Sesame repository that can be used for caching triples
     */
    public Repository getTripleRepository();


    /**
     * Return a map that can be used to store caching metadata about resources. The LDCacheProvider should take care
     * of persisting the metadata if desired.
     *
     * @return a map for storing caching metadata
     */
    public Map<String,CacheEntry> getMetadataRepository();
}
