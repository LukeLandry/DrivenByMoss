// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2025
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.novation.launchkey.maxi.controller;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnsupportedCharsetException;

import de.mossgrabers.framework.controller.display.AbstractTextDisplay;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.midi.IMidiOutput;
import de.mossgrabers.framework.utils.StringUtils;


/**
 * The Launchkey Mk3 16x2 character LCD display. It caches different screens for the basic display
 * as well as for each pot and fader.
 *
 * @author Jürgen Moßgraber
 */
public class LaunchkeyMk3Display extends AbstractTextDisplay
{
    private String           sysexDisplayHeaderBase       = "F0 00 20 29 02 0F 04 ";
    private String           sysexDisplayHeaderParamName  = "F0 00 20 29 02 0F 07 ";
    private String           sysexDisplayHeaderParamValue = "F0 00 20 29 02 0F 08 ";

    /** The first row of the base screen. */
    public static final int  SCREEN_ROW_BASE              = 0;
    /** The first row of the pot screens. */
    public static final int  SCREEN_ROW_POTS              = 2;
    /** The first row of the fader screens. */
    public static final int  SCREEN_ROW_FADERS            = 18;

    private static final int SCREEN_ID_POT1               = 56;
    private static final int SCREEN_ID_FADER1             = 80;

    private CharsetEncoder   isoEncoder;


    /**
     * Constructor. 2 rows, 16 characters per row, 18 screens (1 base mode, 8 pots, 9 faders).
     *
     * @param host The host
     * @param output The MIDI output which addresses the display
     */
    public LaunchkeyMk3Display (final IHost host, final IMidiOutput output)
    {
        super (host, output, 2 * 18 /* No of rows */, 1 /* No of cells */, 16);

        try
        {
            this.isoEncoder = Charset.forName ("ISO-8859-2").newEncoder ();
        }
        catch (final UnsupportedCharsetException ex)
        {
            this.isoEncoder = null;
        }

        this.setCenterNotification (false);
    }


    /** {@inheritDoc} */
    @Override
    public void writeLine (final int row, final String text, final String previousText)
    {
        final StringBuilder sb = new StringBuilder ();

        if (row < SCREEN_ROW_POTS)
        {
            // Base screen
            sb.append (this.sysexDisplayHeaderBase).append (StringUtils.toHexStr (row)).append (' ');
        }
        else
        {
            sb.append (row % 2 == 0 ? this.sysexDisplayHeaderParamName : this.sysexDisplayHeaderParamValue);

            if (row < SCREEN_ROW_FADERS)
            {
                // Pot screens
                final int index = (row - SCREEN_ROW_POTS) / 2;
                sb.append (StringUtils.toHexStr (SCREEN_ID_POT1 + index));
            }
            else
            {
                // Fader screens
                final int index = (row - SCREEN_ROW_FADERS) / 2;
                sb.append (StringUtils.toHexStr (SCREEN_ID_FADER1 + index));
            }
            sb.append (' ');
        }

        // Encode text into Launchkey specific ISO-8859-2 format
        if (this.isoEncoder == null)
        {
            sb.append (StringUtils.asciiToHex (StringUtils.pad (StringUtils.fixASCII (text), 16)));
        }
        else
        {
            for (int i = 0; i < text.length (); i++)
            {
                final char character = text.charAt (i);
                if (this.isoEncoder.canEncode (character))
                {
                    if (character > 127)
                        sb.append ("11 ").append (StringUtils.toHexStr (character - 0x80)).append (' ');
                    else
                        sb.append (StringUtils.toHexStr (character)).append (' ');
                }
            }
        }

        this.output.sendSysex (sb.append ("F7").toString ());
    }


    /** {@inheritDoc} */
    @Override
    public void shutdown ()
    {
        // Intentionally empty
    }


    /**
     * Sets the model. The 88 key version uses a different system exclusive string!
     *
     * @param is88KeyVersion
     */
    public void setModel (final boolean is88KeyVersion)
    {
        if (is88KeyVersion)
        {
            this.sysexDisplayHeaderBase = "F0 00 20 29 02 12 04 ";
            this.sysexDisplayHeaderParamName = "F0 00 20 29 02 12 07 ";
            this.sysexDisplayHeaderParamValue = "F0 00 20 29 02 12 08 ";
            this.forceFlush ();
        }
    }
}