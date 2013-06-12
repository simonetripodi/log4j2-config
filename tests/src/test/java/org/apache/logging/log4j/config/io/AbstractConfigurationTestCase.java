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

import static org.apache.commons.io.FileUtils.contentEquals;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.config.Appender;
import org.apache.logging.log4j.config.AppenderRef;
import org.apache.logging.log4j.config.Configuration;
import org.apache.logging.log4j.config.Filter;
import org.apache.logging.log4j.config.Layout;
import org.apache.logging.log4j.config.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractConfigurationTestCase
{

    private Configuration expected;

    @Before
    public final void setUp()
    {
        expected = new Configuration.Builder()
                                    .setName( "XMLConfigTest" )
                                    .setStatus( "warn" )
                                    .setStrict( true )
                                    .setPackages( "org.apache.logging.log4j.test" )
                                    .addProperty( "filename", "target/test.log" )
                                    .setFilter( new Filter.Builder()
                                                          .setType( "ThresholdFilter" )
                                                          .setLevel( "debug" )
                                                          .build() )
                                    .addAppender( new Appender.Builder()
                                                              .setName( "STDOUT" )
                                                              .setType( "Console" )
                                                              .setLayout( new Layout.Builder()
                                                                                    .setType( "PatternLayout" )
                                                                                    .setPattern( "%m MDC%X%n" )
                                                                                    .build() )
                                                              .addFilter( new Filter.Builder()
                                                                                    .setType( "MarkerFilter" )
                                                                                    .setMarker( "FLOW" )
                                                                                    .setOnMatch( "DENY" )
                                                                                    .setOnMismatch( "NEUTRAL" )
                                                                                    .build() )
                                                              .addFilter( new Filter.Builder()
                                                                                    .setType( "MarkerFilter" )
                                                                                    .setMarker( "EXCEPTION" )
                                                                                    .setOnMatch( "DENY" )
                                                                                    .setOnMismatch( "ACCEPT" )
                                                                                    .build() )
                                                              .build() )
                                    .addLogger( new Logger.Builder()
                                                          .setName( "org.apache.logging.log4j.test1" )
                                                          .setLevel( "debug" )
                                                          .setAdditivity( false )
                                                          .addFilter( new Filter.Builder()
                                                                                .setType( "ThreadContextMapFilter" )
                                                                                .addProperty( "test", "123" )
                                                                                .build() )
                                                          .setAppender( new AppenderRef.Builder()
                                                                                       .setRef( "STDOUT" )
                                                                                       .build() )
                                                          .build() )
                                    .build();
    }

    @After
    public final void tearDown()
    {
        expected = null;
    }

    @Test
    public final void read()
        throws Exception
    {
        Configuration actual = readConfiguration();
        assertReflectionEquals( expected, actual );
    }

    protected abstract Configuration readConfiguration()
        throws Exception;

    @Test
    public final void write()
        throws Exception
    {
        String format = getFormat();
        File expected = new File( "", "log4j." + format );
        File actual = File.createTempFile( "log4j", format );

        FileOutputStream output = new FileOutputStream( actual );
        try
        {
            writeConfiguration( this.expected, output );

            contentEquals( expected, actual );
        }
        finally
        {
            closeQuietly( output );
        }
    }

    protected abstract void writeConfiguration( Configuration configuration, OutputStream outputStream )
        throws Exception;

    protected abstract String getFormat();

}
