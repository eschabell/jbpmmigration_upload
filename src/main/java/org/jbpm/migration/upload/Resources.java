/**
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.jbpm.migration.upload;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Qualifier;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans.
 */
public class Resources {
    private static final String MAIL_CONFIG_FILE = "mail.properties";

    private final Properties mailConfiguration = new Properties();

    @Produces
    public Logger produceLogger(final InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

    @Produces
    @MailConfig
    public Properties produceMailConfiguration() {
        if (mailConfiguration.isEmpty()) {
            try {
                mailConfiguration.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(MAIL_CONFIG_FILE));
            } catch (final IOException ioEx) {
                Logger.getLogger(Resources.class).fatal("Unable to load mail configuration file.", ioEx);
            }

        }
        return mailConfiguration;
    }

    /**
     * Convenience method for date handling between time zones.
     * <p>
     * This is only required because the OpenShift server is not in NL!
     *
     * @param date
     *            The given (UTC) date.
     * @return The actual date in NL.
     */
    public static Date convertToNLDate(final Date date) {
        return DateUtils.addMilliseconds(date, TimeZone.getTimeZone("Europe/Amsterdam").getRawOffset() - TimeZone.getDefault().getRawOffset());
    }

    // Custom qualifiers for different Properties producers.

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
    public @interface MailConfig {
    }
}
