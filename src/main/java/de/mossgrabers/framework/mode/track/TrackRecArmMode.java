// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2025
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.mode.track;

import de.mossgrabers.framework.configuration.Configuration;
import de.mossgrabers.framework.controller.IControlSurface;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.ITrack;


/**
 * The track record arm mode.
 *
 * @param <S> The type of the control surface
 * @param <C> The type of the configuration
 *
 * @author Jürgen Moßgraber
 */
public class TrackRecArmMode<S extends IControlSurface<C>, C extends Configuration> extends DefaultTrackMode<S, C>
{
    /**
     * Constructor.
     *
     * @param surface The surface
     * @param model The model
     */
    public TrackRecArmMode (final S surface, final IModel model)
    {
        super ("Track Rec Arm", surface, model, true);
    }


    /** {@inheritDoc} */
    @Override
    protected void executeMethod (final ITrack track)
    {
        track.toggleRecArm ();
    }
}
