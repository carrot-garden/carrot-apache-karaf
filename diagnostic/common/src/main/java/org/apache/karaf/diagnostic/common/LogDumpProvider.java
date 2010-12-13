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
package org.apache.karaf.diagnostic.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.karaf.diagnostic.core.DumpDestination;
import org.apache.karaf.diagnostic.core.DumpProvider;

/**
 * Dump provider which copies log files from data/log directory to
 * destination.
 * 
 * @author ldywicki
 */
public class LogDumpProvider implements DumpProvider {

	/**
	 * Creates log entries in attached zip.
	 */
	public void createDump(DumpDestination destination) throws Exception {
		File logDir = new File("data/log");
		File[] listFiles = logDir.listFiles();

		for (File file : listFiles) {
			FileInputStream inputStream = new FileInputStream(file);

			OutputStream outputStream = destination.add("log/" + file.getName());

			IOUtils.copy(inputStream, outputStream);
		}
	}

}
