// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2025
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.ni.maschine.mk3.command.trigger;

import de.mossgrabers.controller.ni.maschine.mk3.MaschineConfiguration;
import de.mossgrabers.controller.ni.maschine.mk3.controller.MaschineControlSurface;
import de.mossgrabers.controller.ni.maschine.mk3.view.PlayView;
import de.mossgrabers.framework.command.core.AbstractTriggerCommand;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.featuregroup.ModeManager;
import de.mossgrabers.framework.featuregroup.ViewManager;
import de.mossgrabers.framework.mode.INoteEditorMode;
import de.mossgrabers.framework.mode.Modes;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.Views;


/**
 * Command for the keyboard button which activates the play view or toggles chromatic mode if
 * already active.
 *
 * @author Jürgen Moßgraber
 */
public class KeyboardCommand extends AbstractTriggerCommand<MaschineControlSurface, MaschineConfiguration>
{
    /**
     * Constructor.
     *
     * @param model The model
     * @param surface The surface
     */
    public KeyboardCommand (final IModel model, final MaschineControlSurface surface)
    {
        super (model, surface);
    }


    /** {@inheritDoc} */
    @Override
    public void executeNormal (final ButtonEvent event)
    {
        if (event != ButtonEvent.DOWN)
            return;

        final ViewManager viewManager = this.surface.getViewManager ();
        if (viewManager.isActive (Views.PLAY))
        {
            if (!this.surface.getMaschine ().hasMCUDisplay ())
                ((PlayView) viewManager.get (Views.PLAY)).toggleShifted ();

            final ModeManager modeManager = this.surface.getModeManager ();
            if (modeManager.isActive (Modes.SCALES))
                modeManager.restore ();
            else
                modeManager.setTemporary (Modes.SCALES);
        }
        else
        {
            viewManager.setActive (Views.PLAY);
            ((INoteEditorMode) this.surface.getModeManager ().get (Modes.NOTE)).getNoteEditor ().clearNotes ();

            // Store the newly selected view for the current track
            final ITrack cursorTrack = this.model.getCursorTrack ();
            if (cursorTrack.doesExist ())
                viewManager.setPreferredView (cursorTrack.getPosition (), Views.PLAY);
        }
    }
}
