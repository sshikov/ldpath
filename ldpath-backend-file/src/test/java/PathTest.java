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

import at.newmedialab.ldpath.LDPath;
import at.newmedialab.ldpath.backend.file.FileBackend;
import at.newmedialab.ldpath.backend.sesame.GenericSesameBackend;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.Value;
import org.openrdf.repository.RepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class PathTest {


    private static GenericSesameBackend backend;
    private static LDPath<Value> ldPath;

    @BeforeClass
    public static void setupRepository() throws RepositoryException {
        backend = new FileBackend(PathTest.class.getResource("demo-data.foaf"),"application/rdf+xml");
        ldPath = new LDPath<Value>(backend);
    }

    @Test
    public void simpleResourcePath() throws Exception {

        Map<Value, List<Value>> paths = new HashMap<Value, List<Value>>();
        Collection<Value> values = ldPath.pathQuery(backend.createURI("http://localhost:8080/LMF/resource/hans_meier"), "foaf:interest", null, paths);
        Assert.assertEquals(4,values.size());
        Assert.assertThat(values,hasItems(
                    backend.createURI("http://rdf.freebase.com/ns/en.software_engineering"),
                    backend.createURI("http://rdf.freebase.com/ns/en.linux"),
                    backend.createURI("http://dbpedia.org/resource/Java"),
                    backend.createURI("http://dbpedia.org/resource/Climbing")
                ));

    }

    @Test
    public void simpleValuePath() throws Exception {

        Collection<String> values = ldPath.pathTransform(backend.createURI("http://localhost:8080/LMF/resource/hans_meier"), "foaf:name :: xsd:string", null);
        Assert.assertEquals(1,values.size());
        Assert.assertThat(values,hasItem("Hans Meier"));
    }
}
