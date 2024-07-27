// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2024
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.oxi.one;

import java.util.List;
import java.util.UUID;

import de.mossgrabers.framework.controller.DefaultControllerDefinition;
import de.mossgrabers.framework.utils.OperatingSystem;
import de.mossgrabers.framework.utils.Pair;


/**
 * Definition class for the OXI One controller extension.
 *
 * @author Jürgen Moßgraber
 */
public class OxiOneControllerDefinition extends DefaultControllerDefinition
{
    private static final UUID EXTENSION_ID = UUID.fromString ("85593F1C-52FD-423F-BCAC-80D90EEB9ACA");


    /**
     * Constructor.
     */
    public OxiOneControllerDefinition ()
    {
        super (EXTENSION_ID, "One", "OXI", 1, 1);
    }


    /** {@inheritDoc} */
    @Override
    public List<Pair<String [], String []>> getMidiDiscoveryPairs (final OperatingSystem os)
    {
        final List<Pair<String [], String []>> midiDiscoveryPairs = super.getMidiDiscoveryPairs (os);
        switch (os)
        {
            case WINDOWS:
                midiDiscoveryPairs.addAll (this.createDeviceDiscoveryPairs ("OXI ONE"));
                break;

            case LINUX:
                midiDiscoveryPairs.addAll (this.createDeviceDiscoveryPairs ("OXI ONE"));
                midiDiscoveryPairs.addAll (this.createLinuxDeviceDiscoveryPairs ("OXI ONE", "OXI ONE"));
                midiDiscoveryPairs.addAll (this.createLinuxDeviceDiscoveryPairs ("OXI ONE Jack 1", "OXI ONE Jack 1"));
                break;

            case MAC, MAC_ARM:
                for (int i = 1; i < 20; i++)
                {
                    final String name = "OXI ONE Jack " + i;
                    midiDiscoveryPairs.add (this.addDeviceDiscoveryPair (name, name));
                }
                midiDiscoveryPairs.addAll (this.createDeviceDiscoveryPairs ("OXI ONE"));
                break;

            default:
                // Not supported
                break;
        }
        return midiDiscoveryPairs;
    }
}