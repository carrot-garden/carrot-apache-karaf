/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.karaf.jaas.command;

import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.config.JaasRealm;
import org.apache.karaf.jaas.modules.BackingEngine;
import org.apache.karaf.jaas.modules.BackingEngineService;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.List;
import java.util.Queue;

public abstract class JaasCommandSupport extends OsgiCommandSupport {

    public static final String JAAS_REALM = "JaasCommand.REALM";
    public static final String JAAS_ENTRY = "JaasCommand.ENTRY";
    public static final String JAAS_CMDS = "JaasCommand.COMMANDS";

    private List<JaasRealm> realms;

    protected BackingEngineService backingEngineService;

    protected abstract Object doExecute(BackingEngine engine) throws Exception;

    /**
     * Add the command to the command queue.
     *
     * @return
     * @throws Exception
     */
    protected Object doExecute() throws Exception {
        JaasRealm realm = (JaasRealm) session.get(JAAS_REALM);
        AppConfigurationEntry entry = (AppConfigurationEntry) session.get(JAAS_ENTRY);
        @SuppressWarnings("unchecked")
        Queue<JaasCommandSupport> commandQueue = (Queue<JaasCommandSupport>) session.get(JAAS_CMDS);

        if (realm != null && entry != null) {
            if (commandQueue != null) {
                commandQueue.add(this);
            }
        } else {
            System.err.println("No JAAS Realm / Module has been selected");
        }
        return null;
    }


    /**
     * Returns the JAAS Realm named as realmName.
     *
     * @param realmName the name of the JAAS Realm.
     * @return the corresponding <code>JaasRealm</code>.
     */
    public JaasRealm findRealm(String realmName) {
        if (realms != null) {
            for (JaasRealm realm : realms) {
                if (realm.getName().equals(realmName)) {
                    return realm;
                }
            }
        }
        return null;
    }

    /**
     * Returns the JAAS Module entry of the specified realm, identified by the given name.
     *
     * @param realm the JAAS realm.
     * @param moduleName the JAAS module name.
     * @return the corresponding <code>AppConfigurationEntry</code>.
     */
    public AppConfigurationEntry findLoginModule(JaasRealm realm, String moduleName) {

        AppConfigurationEntry appConfigurationEntry = null;
        if (realm != null) {
            AppConfigurationEntry[] entries = realm.getEntries();

            // if no moduleName provided and a there is a single module in the realm.
            if (entries != null && entries.length == 1 && moduleName == null) {
                return entries[0];
            }

            for (AppConfigurationEntry entry : entries) {
                String moduleClass = (String) entry.getOptions().get(ProxyLoginModule.PROPERTY_MODULE);
                if (moduleName != null && (moduleName.equals(entry.getLoginModuleName()) || moduleName.equals(moduleClass))) {
                    return entry;
                }
            }

        }
        return appConfigurationEntry;
    }

    public List<JaasRealm> getRealms() {
        return realms;
    }

    public void setRealms(List<JaasRealm> realms) {
        this.realms = realms;
    }

    public BackingEngineService getBackingEngineService() {
        return backingEngineService;
    }

    public void setBackingEngineService(BackingEngineService backingEngineService) {
        this.backingEngineService = backingEngineService;
    }

}
