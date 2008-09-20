/*
 * SIP Communicator, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.protocol.rss;

import java.util.*;

import org.osgi.framework.*;

import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.util.*;

/**
 * The Rss protocol provider factory creates instances of the Rss
 * protocol provider service. One Service instance corresponds to one account.
 *
 * @author Emil Ivov
 */
public class ProtocolProviderFactoryRssImpl
    extends ProtocolProviderFactory
{
    private static final Logger logger
        = Logger.getLogger(ProtocolProviderFactoryRssImpl.class);

    /**
     * Creates an instance of the ProtocolProviderFactoryRssImpl.
     */
    public ProtocolProviderFactoryRssImpl()
    {
        super(RssActivator.getBundleContext(),
            ProtocolProviderServiceRssImpl.RSS_PROTOCOL_NAME);
    }

    /**
     * Initialized and creates an account corresponding to the specified
     * accountProperties and registers the resulting ProtocolProvider in the
     * <tt>context</tt> BundleContext parameter.
     *
     * @param userIDStr tha/a user identifier uniquely representing the newly
     *   created account within the protocol namespace.
     * @param accountProperties a set of protocol (or implementation)
     *   specific properties defining the new account.
     * @return the AccountID of the newly created account.
     */
    public AccountID installAccount( String userIDStr,
                                     Map accountProperties)
    {
        BundleContext context
            = RssActivator.getBundleContext();
        if (context == null)
            throw new NullPointerException("The specified BundleContext was null");

        if (userIDStr == null)
            throw new NullPointerException("The specified AccountID was null");

        if (accountProperties == null)
            throw new NullPointerException("The specified property map was null");

        accountProperties.put(USER_ID, userIDStr);

        AccountID accountID = new RssAccountID(userIDStr, accountProperties);

        //make sure we haven't seen this account id before.
        if (registeredAccounts.containsKey(accountID))
            throw new IllegalStateException(
                "An account for id " + userIDStr + " was already installed!");

        //first store the account and only then load it as the load generates
        //an osgi event, the osgi event triggers (through the UI) a call to the
        //ProtocolProviderService.register() method and it needs to acces
        //the configuration service and check for a stored password.
        this.storeAccount(accountID);

        accountID = loadAccount(accountProperties);

        return accountID;
    }

    protected AccountID createAccountID(String userID, Map accountProperties)
    {
        return new RssAccountID(userID, accountProperties);
    }

    protected ProtocolProviderService createService(String userID,
        AccountID accountID)
    {
        ProtocolProviderServiceRssImpl service =
            new ProtocolProviderServiceRssImpl();

        service.initialize(userID, accountID);
        return service;
    }

    @Override
    public void modifyAccount(  ProtocolProviderService protocolProvider,
                                Map accountProperties)
        throws NullPointerException
    {
        // TODO Auto-generated method stub
        
    }
}
