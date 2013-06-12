/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.config.io;

import java.io.OutputStream;

import org.apache.logging.log4j.config.Configuration;
import org.apache.logging.log4j.config.io.xpp3.Log4j2Xpp3Reader;
import org.apache.logging.log4j.config.io.xpp3.Log4j2Xpp3Writer;

public final class Log4j2XmlTestCase
    extends AbstractConfigurationTestCase
{

    @Override
    protected Configuration readConfiguration()
        throws Exception
    {
        return new Log4j2Xpp3Reader().read( getClass().getResourceAsStream( "log4j.xml" ) );
    }

    @Override
    protected void writeConfiguration( Configuration configuration, OutputStream outputStream )
        throws Exception
    {
        new Log4j2Xpp3Writer().write( outputStream, configuration );
    }

    @Override
    protected String getFormat()
    {
        return "xml";
    }

}
