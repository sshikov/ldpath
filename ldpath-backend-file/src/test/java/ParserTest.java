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

import at.newmedialab.ldpath.api.selectors.NodeSelector;
import at.newmedialab.ldpath.backend.sesame.GenericSesameBackend;
import at.newmedialab.ldpath.model.programs.Program;
import at.newmedialab.ldpath.model.selectors.PropertySelector;
import at.newmedialab.ldpath.parser.ParseException;
import at.newmedialab.ldpath.parser.RdfPathParser;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.Value;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import java.io.StringReader;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class ParserTest {

    private static GenericSesameBackend backend;

    @BeforeClass
    public static void setupRepository() throws RepositoryException {
        Repository repository = new SailRepository(new MemoryStore());
        repository.initialize();

        backend = new GenericSesameBackend(repository);
    }



    @Test
    public void testParsePath() throws Exception {
        String path1 = "rdfs:label";

        NodeSelector<Value> s1 = parseSelector(path1);
        Assert.assertTrue(s1 instanceof PropertySelector);
        Assert.assertEquals("<http://www.w3.org/2000/01/rdf-schema#label>",s1.getPathExpression(backend));



    }

    private NodeSelector<Value> parseSelector(String selector) throws ParseException {
        return new RdfPathParser<Value>(backend,new StringReader(selector)).parseSelector(null);
    }

    @Test
    public void testParseProgram() throws Exception {

        Program<Value> p1 = parseProgram(IOUtils.toString(ParserTest.class.getResource("stanbol.search")));
        Assert.assertEquals(12,p1.getFields().size());
        Assert.assertNull(p1.getBooster());
        Assert.assertNotNull(p1.getFilter());
        Assert.assertEquals(5,p1.getNamespaces().size());


        Program<Value> p2 = parseProgram(IOUtils.toString(ParserTest.class.getResource("sn.search")));
        Assert.assertEquals(11,p2.getFields().size());
        Assert.assertNotNull(p2.getBooster());
        Assert.assertNotNull(p2.getFilter());
        Assert.assertEquals(8,p2.getNamespaces().size());


    }

    private Program<Value> parseProgram(String selector) throws ParseException {
        return new RdfPathParser<Value>(backend,new StringReader(selector)).parseProgram();
    }


}
