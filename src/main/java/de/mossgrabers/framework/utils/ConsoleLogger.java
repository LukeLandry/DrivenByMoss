// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.utils;

import de.mossgrabers.framework.daw.IHost;


/**
 * Static global logger. Do not use for production.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class ConsoleLogger
{
    private static IHost globalHost;


    /**
     * Init the host for logging.
     *
     * @param host The host
     */
    public static void init (final IHost host)
    {
        globalHost = host;
    }


    /**
     * Log a message.
     *
     * @param message The message
     */
    public static void log (final String message)
    {
        if (globalHost != null)
            globalHost.println (message);
    }
}
