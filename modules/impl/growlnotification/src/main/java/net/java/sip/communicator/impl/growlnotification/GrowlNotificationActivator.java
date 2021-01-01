/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Copyright @ 2015 Atlassian Pty Ltd
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
package net.java.sip.communicator.impl.growlnotification;

import net.java.sip.communicator.service.systray.*;

import net.java.sip.communicator.util.osgi.*;
import org.jitsi.service.resources.*;
import org.jitsi.util.OSUtils; // disambiguation
import org.osgi.framework.*;

/**
 * Activates the GrowlNotificationService
 *
 * @author Romain Kuntz
 * @author Egidijus Jankauskas
 * @author Lyubomir Marinov
 */
public class GrowlNotificationActivator
    extends DependentActivator
{
    /**
     * The <tt>Logger</tt> used by the <tt>GrowlNotificationActivator</tt> class
     * and its instances for logging output.
     */
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GrowlNotificationActivator.class);

    /**
     * A reference to the resource management service.
     */
    private static ResourceManagementService resourcesService;

    /**
     * A reference to the Growl notification service
     */
    private static GrowlNotificationServiceImpl handler;

    public GrowlNotificationActivator()
    {
        super(
            ResourceManagementService.class
        );
    }
    /**
     * Initializes and starts a new <tt>GrowlNotificationService</tt>
     * implementation on Mac OS X.
     *
     * @param bundleContext the <tt>BundleContext</tt> to register the new
     * <tt>GrowlNotificationService</tt> implementation into
     */
    @Override
    public void startWithServices(BundleContext bundleContext)
    {
        // This bundle is available for Mac OS X only.
        if (!OSUtils.IS_MAC)
            return;

        if (logger.isInfoEnabled())
            logger.info("Growl Notification... [Starting]");

        resourcesService = getService(ResourceManagementService.class);

        handler = new GrowlNotificationServiceImpl();
        handler.start(bundleContext);
        bundleContext.registerService(
                PopupMessageHandler.class.getName(),
                handler,
                null);

        if (logger.isInfoEnabled())
            logger.info("Growl Notification... [Started]");
    }

    /**
     * Stops this bundle.
     *
     * @param bundleContext the <tt>BundleContext</tt> to stop this bundle into
     */
    public void stop(BundleContext bundleContext) throws Exception
    {
        super.stop(bundleContext);

        // This bundle is available for Mac OS X only.
        if (!OSUtils.IS_MAC)
            return;

        handler.stop(bundleContext);
        if (logger.isInfoEnabled())
            logger.info("Growl Notification Service... [Stopped]");
    }

    /**
     * Returns the <tt>ResourceManagementService</tt> obtained from the bundle
     * context.
     * @return the <tt>ResourceManagementService</tt> obtained from the bundle
     * context
     */
    public static ResourceManagementService getResources()
    {
        return resourcesService;
    }
}
