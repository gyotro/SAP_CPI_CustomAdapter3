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

import java.net.URISyntaxException;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultPollingEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a www.Sample.com Camel endpoint.
 */
@UriEndpoint(scheme = "sap-sample", syntax = "", title = "")
public class SAP_Custom_Adapter_JDBCComponentEndpoint extends DefaultPollingEndpoint {
    private SAP_Custom_Adapter_JDBCComponentComponent component;

    private transient Logger logger = LoggerFactory.getLogger(SAP_Custom_Adapter_JDBCComponentEndpoint.class);

    public String getCloudConnectorLocation() {
        return cloudConnectorLocation;
    }

    public void setCloudConnectorLocation(String cloudConnectorLocation) {
        this.cloudConnectorLocation = cloudConnectorLocation;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getCustomConnectionString() {
        return customConnectionString;
    }

    public void setCustomConnectionString(String customConnectionString) {
        this.customConnectionString = customConnectionString;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getSelectQuery() {
        return selectQuery;
    }

    public void setSelectQuery(String selectQuery) {
        this.selectQuery = selectQuery;
    }

    public String getUpdateQuery() {
        return updateQuery;
    }

    public void setUpdateQuery(String updateQuery) {
        this.updateQuery = updateQuery;
    }

    public long getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    @UriParam
    private String cloudConnectorLocation;
    @UriParam
    private String dbHost;
    @UriParam
    private String dbPort;
    @UriParam
    private String customConnectionString;
    @UriParam
    private String dbUser;
    @UriParam
    private String dbPassword;
    @UriParam
    private String selectQuery;
    @UriParam
    private String updateQuery;
    @UriParam
    private long pollingInterval;
	public SAP_Custom_Adapter_JDBCComponentEndpoint() {
    }

    public SAP_Custom_Adapter_JDBCComponentEndpoint(final String endpointUri, final SAP_Custom_Adapter_JDBCComponentComponent component) throws URISyntaxException {
        super(endpointUri, component);
        this.component = component;
    }

    public SAP_Custom_Adapter_JDBCComponentEndpoint(final String uri, final String remaining, final SAP_Custom_Adapter_JDBCComponentComponent component) throws URISyntaxException {
        this(uri, component);
    }

    public Producer createProducer() throws Exception {
        return new SAP_Custom_Adapter_JDBCComponentProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        final SAP_Custom_Adapter_JDBCComponentConsumer consumer = new SAP_Custom_Adapter_JDBCComponentConsumer(this, processor);
        configureConsumer(consumer);
        return consumer;
    }

    public void logEndpointConfiguration() {
        String configLog = "Endpoint configuration: host=" + dbHost +
                ", port=" + dbPort +
                ", user=" + dbUser +
                ", cloudConnectorLocation=" + cloudConnectorLocation +
                ", customString=" + customConnectionString +
                ", pollingInterval=" + pollingInterval;
        logger.debug(configLog);
    }

    public boolean isSingleton() {
        return true;
    }
}
