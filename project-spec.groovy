/*
 * Copyright (c) 2012 Salzburg Research.
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






spec = [
        name: 'ldpath',
        group: 'at.newmedialab.ldpath',
        version: '0.9.10',

        projectName: 'LDPath',

        versions: [
                sesame:     '2.6.5',            // RDF API
                jena:       '2.6.4',            // RDF API

                slf4j:      '1.6.4',            // logging interface
                logback:    '1.0.0',            // logging backend

                guava:      '12.0',
                jdom:       '2.0.2',
                jsoup:      '1.6.4a-LMF',             // HTML parsing

                cli:        '1.2',              // Apache Commons CLI
                io:         '2.4',              // Apache Commons IO
                httpclient: '4.2',            // Apache Commons HTTP Client
                config:     '1.8',              // Apache Commons Configuration

                junit:      '4.10',             // Testing
                hamcrest:   '1.2.1',

        ]
]
