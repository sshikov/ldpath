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

package at.newmedialab.ldclient.service;

import at.newmedialab.ldclient.model.Endpoint;
import org.openrdf.model.URI;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class LDEndpoints {

    private Logger log;


    private List<Endpoint> endpoints;


    private static Endpoint[] blacklist = new Endpoint[] {
        new Endpoint("Wikipedia", Endpoint.EndpointType.NONE,"http://wikipedia.org","","",(long)86400),
        new Endpoint("Wikipedia EN", Endpoint.EndpointType.NONE,"http://en.wikipedia.org","","",(long)86400),
        new Endpoint("HolyGoat", Endpoint.EndpointType.NONE,"http://www.holygoat.co.uk","","",(long)86400),
        new Endpoint("KiWi Project", Endpoint.EndpointType.NONE,"http://www.kiwi-project.eu/","","",(long)86400)
    };



    public LDEndpoints() {
        endpoints = new LinkedList<Endpoint>();


        // load all endpoints into memory

        // ensure that some typical non-LD sites are blacklisted
        for(Endpoint e : blacklist) {
            if(!endpoints.contains(e)) {
                addEndpoint(e);
            }
        }
    }


    /**
     * Add a new endpoint to the system. The endpoint will be persisted in the database.
     *
     * @param endpoint
     */
    public void addEndpoint(Endpoint endpoint) {
        endpoints.add(endpoint);
    }


    /**
     * List all endpoints registered in the system.
     *
     * @return a list of endpoints in the order they were added to the database.
     */
    public List<Endpoint> listEndpoints() {
        return endpoints;
    }

    /**
     * Remove the endpoint given as argument. The endpoint will be deleted in the database.
     *
     * @param endpoint
     */
    public void removeEndpoint(Endpoint endpoint) {
        endpoints.remove(endpoint);
    }


    /**
     * Retrieve the endpoint matching the KiWiUriResource passed as argument. The endpoint is determined by
     * matching the endpoint's URI prefix with the resource URI. If no matching endpoint exists, returns null.
     * The LinkedDataClientService can then decide (based on configuration) whether to try with a standard
     * LinkedDataRequest or ignore the request.
     *
     * @param resource the KiWiUriResource to check.
     */
    public Endpoint getEndpoint(URI resource) {
        for(Endpoint endpoint : endpoints) {
            if (endpoint.handles(resource)) {
                return endpoint;
            }
        }

        return null;
    }

}
