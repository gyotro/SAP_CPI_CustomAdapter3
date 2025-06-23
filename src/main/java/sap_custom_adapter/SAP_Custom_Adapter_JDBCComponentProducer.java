/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sap_custom_adapter;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * The www.Sample.com producer.
 */
public class SAP_Custom_Adapter_JDBCComponentProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(SAP_Custom_Adapter_JDBCComponentProducer.class);
    private SAP_Custom_Adapter_JDBCComponentEndpoint endpoint;

	public SAP_Custom_Adapter_JDBCComponentProducer(SAP_Custom_Adapter_JDBCComponentEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

    public void process(Exchange exchange) throws Exception {
        LOG.info("Processing exchange in Producer with update query: {}", endpoint.getUpdateQuery());

        String connectionString = String.format("jdbc:sqlserver://%s:%s;%s",
                endpoint.getDbHost(),
                endpoint.getDbPort(),
                endpoint.getCustomConnectionString() != null ? endpoint.getCustomConnectionString() : "");

        Properties props = new Properties();
        props.put("user", endpoint.getDbUser());
        props.put("password", endpoint.getDbPassword());
        if (endpoint.getCloudConnectorLocation() != null && !endpoint.getCloudConnectorLocation().isEmpty()) {
            props.put("sap.cloud.connector.locationid", endpoint.getCloudConnectorLocation());
            LOG.info("Using Cloud Connector with location: {}", endpoint.getCloudConnectorLocation());
        } else {
            LOG.info("Connecting without Cloud Connector.");
        }

        try (Connection conn = DriverManager.getConnection(connectionString, props);
             Statement stmt = conn.createStatement()) {

            int result = stmt.executeUpdate(endpoint.getUpdateQuery());
            LOG.info("Update result: {}", result);

            exchange.getMessage().setBody("Updated rows: " + result);
        } catch (Exception e) {
            LOG.error("Error during update execution: ", e);
            throw e;
        }
    }

}
