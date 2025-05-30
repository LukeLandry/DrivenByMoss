// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2025
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.ableton.push.command.trigger;

import de.mossgrabers.controller.ableton.push.PushConfiguration;
import de.mossgrabers.controller.ableton.push.controller.PushControlSurface;
import de.mossgrabers.framework.command.core.AbstractTriggerCommand;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.constants.Capability;
import de.mossgrabers.framework.featuregroup.ModeManager;
import de.mossgrabers.framework.mode.Modes;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * Command to edit the Volume of tracks.
 *
 * @author Jürgen Moßgraber
 */
public class VolumeCommand extends AbstractTriggerCommand<PushControlSurface, PushConfiguration>
{
    /**
     * Constructor.
     *
     * @param model The model
     * @param surface The surface
     */
    public VolumeCommand (final IModel model, final PushControlSurface surface)
    {
        super (model, surface);
    }


    /** {@inheritDoc} */
    @Override
    public void execute (final ButtonEvent event, final int velocity)
    {
        if (event != ButtonEvent.DOWN)
            return;

        final ModeManager modeManager = this.surface.getModeManager ();
        final Modes currentMode = modeManager.getActiveID ();

        // Layer mode selection for Push 1
        final PushConfiguration config = this.surface.getConfiguration ();
        if (!config.isPushModern () && this.surface.isSelectPressed () && Modes.isLayerMode (currentMode))
        {
            modeManager.setActive (Modes.DEVICE_LAYER_VOLUME);
            return;
        }

        if (Modes.VOLUME.equals (currentMode))
        {
            if (this.model.getHost ().supports (Capability.HAS_CROSSFADER))
                modeManager.setActive (Modes.CROSSFADER);
        }
        else
            modeManager.setActive (Modes.VOLUME);
    }
}
