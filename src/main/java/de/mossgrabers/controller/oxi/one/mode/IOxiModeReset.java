// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2024
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.oxi.one.mode;

/**
 * Interface for resetting settings to their default value, which are not real parameters.
 *
 * @author Jürgen Moßgraber
 */
public interface IOxiModeReset
{
    /**
     * Resets a setting to its default value.
     * 
     * @param index The index of the value
     */
    void resetValue (final int index);
}
