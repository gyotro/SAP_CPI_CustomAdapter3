package sap_custom_adapter;
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


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.support.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Sample.com consumer.
 */
public class SAP_Custom_Adapter_JDBCComponentConsumer extends ScheduledPollConsumer {
    private Logger LOG = LoggerFactory.getLogger(SAP_Custom_Adapter_JDBCComponentConsumer.class);

    private final SAP_Custom_Adapter_JDBCComponentEndpoint endpoint;


    public SAP_Custom_Adapter_JDBCComponentConsumer(final SAP_Custom_Adapter_JDBCComponentEndpoint endpoint, final Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

    @Override
    protected int poll() throws Exception {
        LOG.info("Polling database with query: {}", endpoint.getSelectQuery());

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
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(endpoint.getSelectQuery())) {
            Exchange exchange = getEndpoint().createExchange();
            while (rs.next()) {

                StringBuilder row = new StringBuilder();
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    row.append(rs.getString(i));
                    if (i < cols) row.append(",");
                }
                exchange.getIn().setBody(row.toString());
                LOG.debug("Processing row: {}", row);
                getProcessor().process(exchange);
            }
        } catch (Exception e) {
            LOG.error("Polling error: ", e);
            throw e;
        }

        return 1;
    }
}
