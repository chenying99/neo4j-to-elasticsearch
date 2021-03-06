/*
 * Copyright (c) 2013-2016 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.graphaware.module.es.proc;

import com.graphaware.integration.es.test.ElasticSearchServer;
import com.graphaware.integration.es.test.EmbeddedElasticSearchServer;
import com.graphaware.test.integration.GraphAwareIntegrationTest;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;

public abstract class ESProcedureIntegrationTest extends GraphAwareIntegrationTest {
    private ElasticSearchServer esServer;

    @Override
    protected String configFile() {
        return "integration/int-test-default.conf";
    }

    @Override
    public void setUp() throws Exception {
        esServer = new EmbeddedElasticSearchServer();
        esServer.start();

        // start Neo4j server before registering procedures
        super.setUp();

        registerProcedure();
    }

    private void registerProcedure() throws KernelException {
        ((GraphDatabaseAPI) getDatabase())
                .getDependencyResolver()
                .resolveDependency(Procedures.class)
                .register(ElasticSearchProcedures.class);
    }

    @Override
    public void tearDown() throws Exception {
        esServer.stop();
        super.tearDown();
    }
}
