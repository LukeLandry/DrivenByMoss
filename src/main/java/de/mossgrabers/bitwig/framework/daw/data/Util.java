// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2025
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.bitwig.framework.daw.data;

import com.bitwig.extension.controller.api.Subscribable;


/**
 * Helper functions for Bitwig API.
 *
 * @author Jürgen Moßgraber
 */
public final class Util
{
    /**
     * Private due to utility class.
     */
    private Util ()
    {
        // Intentionally empty
    }


    /**
     * Hides new subscribe/unsubscribe API.
     *
     * @param subscribable The subscribable
     * @param enable True to subscribe, otherwise unsubscribed
     */
    public static void setIsSubscribed (final Subscribable subscribable, final boolean enable)
    {
        if (enable)
            subscribable.subscribe ();
        else
            subscribable.unsubscribe ();
    }
}
