// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2025
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.generic.flexihandler;

import de.mossgrabers.controller.generic.GenericFlexiConfiguration;
import de.mossgrabers.controller.generic.controller.FlexiCommand;
import de.mossgrabers.controller.generic.controller.GenericFlexiControlSurface;
import de.mossgrabers.controller.generic.flexihandler.utils.FlexiHandlerException;
import de.mossgrabers.controller.generic.flexihandler.utils.KnobMode;
import de.mossgrabers.controller.generic.flexihandler.utils.MidiValue;
import de.mossgrabers.framework.controller.valuechanger.IValueChanger;
import de.mossgrabers.framework.daw.IApplication;
import de.mossgrabers.framework.daw.IModel;


/**
 * The handler for layout commands.
 *
 * @author Jürgen Moßgraber
 */
public class LayoutHandler extends AbstractHandler
{
    /**
     * Constructor.
     *
     * @param model The model
     * @param surface The surface
     * @param configuration The configuration
     * @param absoluteLowResValueChanger The default absolute value changer in low res mode
     * @param signedBitRelativeValueChanger The signed bit relative value changer
     * @param signedBit2RelativeValueChanger The signed bit relative value changer
     * @param offsetBinaryRelativeValueChanger The offset binary relative value changer
     */
    public LayoutHandler (final IModel model, final GenericFlexiControlSurface surface, final GenericFlexiConfiguration configuration, final IValueChanger absoluteLowResValueChanger, final IValueChanger signedBitRelativeValueChanger, final IValueChanger signedBit2RelativeValueChanger, final IValueChanger offsetBinaryRelativeValueChanger)
    {
        super (model, surface, configuration, absoluteLowResValueChanger, signedBitRelativeValueChanger, signedBit2RelativeValueChanger, offsetBinaryRelativeValueChanger);
    }


    /** {@inheritDoc} */
    @Override
    public FlexiCommand [] getSupportedCommands ()
    {
        return new FlexiCommand []
        {
            FlexiCommand.LAYOUT_ARRANGER_ZOOM_IN,
            FlexiCommand.LAYOUT_ARRANGER_ZOOM_OUT,
            FlexiCommand.LAYOUT_SET_ARRANGE_LAYOUT,
            FlexiCommand.LAYOUT_SET_MIX_LAYOUT,
            FlexiCommand.LAYOUT_SET_EDIT_LAYOUT,
            FlexiCommand.LAYOUT_TOGGLE_NOTE_EDITOR,
            FlexiCommand.LAYOUT_TOGGLE_AUTOMATION_EDITOR,
            FlexiCommand.LAYOUT_TOGGLE_DEVICES_PANEL,
            FlexiCommand.LAYOUT_TOGGLE_MIXER_PANEL,
            FlexiCommand.LAYOUT_TOGGLE_FULLSCREEN,
            FlexiCommand.LAYOUT_TOGGLE_ARRANGER_CUE_MARKERS,
            FlexiCommand.LAYOUT_TOGGLE_ARRANGER_PLAYBACK_FOLLOW,
            FlexiCommand.LAYOUT_TOGGLE_ARRANGER_TRACK_ROW_HEIGHT,
            FlexiCommand.LAYOUT_TOGGLE_ARRANGER_CLIP_LAUNCHER_SECTION,
            FlexiCommand.LAYOUT_TOGGLE_ARRANGER_TIME_LINE,
            FlexiCommand.LAYOUT_TOGGLE_ARRANGER_IO_SECTION,
            FlexiCommand.LAYOUT_TOGGLE_ARRANGER_EFFECT_TRACKS,
            FlexiCommand.LAYOUT_TOGGLE_MIXER_CLIP_LAUNCHER_SECTION,
            FlexiCommand.LAYOUT_TOGGLE_MIXER_CROSS_FADE_SECTION,
            FlexiCommand.LAYOUT_TOGGLE_MIXER_DEVICE_SECTION,
            FlexiCommand.LAYOUT_TOGGLE_MIXER_SENDSSECTION,
            FlexiCommand.LAYOUT_TOGGLE_MIXER_IO_SECTION,
            FlexiCommand.LAYOUT_TOGGLE_MIXER_METER_SECTION,
        };
    }


    /** {@inheritDoc} */
    @Override
    public int getCommandValue (final FlexiCommand command)
    {
        switch (command)
        {
            case LAYOUT_ARRANGER_ZOOM_IN, LAYOUT_ARRANGER_ZOOM_OUT:
                return 0;

            case LAYOUT_SET_ARRANGE_LAYOUT:
                return toMidiValue (this.model.getApplication ().isArrangeLayout ());

            case LAYOUT_SET_MIX_LAYOUT:
                return toMidiValue (this.model.getApplication ().isMixerLayout ());

            case LAYOUT_SET_EDIT_LAYOUT:
                return toMidiValue (this.model.getApplication ().isEditLayout ());

            case LAYOUT_TOGGLE_ARRANGER_CUE_MARKERS:
                return toMidiValue (this.model.getArranger ().areCueMarkersVisible ());

            case LAYOUT_TOGGLE_ARRANGER_PLAYBACK_FOLLOW:
                return toMidiValue (this.model.getArranger ().isPlaybackFollowEnabled ());

            case LAYOUT_TOGGLE_ARRANGER_CLIP_LAUNCHER_SECTION:
                return toMidiValue (this.model.getArranger ().isClipLauncherVisible ());

            case LAYOUT_TOGGLE_ARRANGER_TIME_LINE:
                return toMidiValue (this.model.getArranger ().isTimelineVisible ());

            case LAYOUT_TOGGLE_ARRANGER_IO_SECTION:
                return toMidiValue (this.model.getArranger ().isIoSectionVisible ());

            case LAYOUT_TOGGLE_ARRANGER_EFFECT_TRACKS:
                return toMidiValue (this.model.getArranger ().areEffectTracksVisible ());

            case LAYOUT_TOGGLE_MIXER_CLIP_LAUNCHER_SECTION:
                return toMidiValue (this.model.getMixer ().isClipLauncherSectionVisible ());

            case LAYOUT_TOGGLE_MIXER_CROSS_FADE_SECTION:
                return toMidiValue (this.model.getMixer ().isCrossFadeSectionVisible ());

            case LAYOUT_TOGGLE_MIXER_DEVICE_SECTION:
                return toMidiValue (this.model.getMixer ().isDeviceSectionVisible ());

            case LAYOUT_TOGGLE_MIXER_SENDSSECTION:
                return toMidiValue (this.model.getMixer ().isSendSectionVisible ());

            case LAYOUT_TOGGLE_MIXER_IO_SECTION:
                return toMidiValue (this.model.getMixer ().isIoSectionVisible ());

            case LAYOUT_TOGGLE_MIXER_METER_SECTION:
                return toMidiValue (this.model.getMixer ().isMeterSectionVisible ());

            default:
                return -1;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void handle (final FlexiCommand command, final KnobMode knobMode, final MidiValue value)
    {
        final boolean isButtonPressed = this.isButtonPressed (knobMode, value);

        switch (command)
        {
            case LAYOUT_ARRANGER_ZOOM_IN:
                if (isButtonPressed)
                    this.model.getApplication ().zoomIn ();
                break;

            case LAYOUT_ARRANGER_ZOOM_OUT:
                this.model.getApplication ().zoomOut ();
                break;

            // Layout: Set Arrange Layout
            case LAYOUT_SET_ARRANGE_LAYOUT:
                if (isButtonPressed)
                    this.model.getApplication ().setPanelLayout (IApplication.PANEL_LAYOUT_ARRANGE);
                break;

            // Layout: Set Mix Layout
            case LAYOUT_SET_MIX_LAYOUT:
                if (isButtonPressed)
                    this.model.getApplication ().setPanelLayout (IApplication.PANEL_LAYOUT_MIX);
                break;

            // Layout: Set Edit Layout
            case LAYOUT_SET_EDIT_LAYOUT:
                if (isButtonPressed)
                    this.model.getApplication ().setPanelLayout (IApplication.PANEL_LAYOUT_EDIT);
                break;

            // Layout: Toggle Note Editor
            case LAYOUT_TOGGLE_NOTE_EDITOR:
                if (isButtonPressed)
                    this.model.getApplication ().toggleNoteEditor ();
                break;

            // Layout: Toggle Automation Editor
            case LAYOUT_TOGGLE_AUTOMATION_EDITOR:
                if (isButtonPressed)
                    this.model.getApplication ().toggleAutomationEditor ();
                break;

            // Layout: Toggle Devices Panel
            case LAYOUT_TOGGLE_DEVICES_PANEL:
                if (isButtonPressed)
                    this.model.getApplication ().toggleDevices ();
                break;

            // Layout: Toggle Mixer Panel
            case LAYOUT_TOGGLE_MIXER_PANEL:
                if (isButtonPressed)
                    this.model.getApplication ().toggleMixer ();
                break;

            // Layout: Toggle Fullscreen
            case LAYOUT_TOGGLE_FULLSCREEN:
                if (isButtonPressed)
                    this.model.getApplication ().toggleFullScreen ();
                break;

            // Layout: Toggle Arranger Cue Markers
            case LAYOUT_TOGGLE_ARRANGER_CUE_MARKERS:
                if (isButtonPressed)
                    this.model.getArranger ().toggleCueMarkerVisibility ();
                break;

            // Layout: Toggle Arranger Playback Follow
            case LAYOUT_TOGGLE_ARRANGER_PLAYBACK_FOLLOW:
                if (isButtonPressed)
                    this.model.getArranger ().togglePlaybackFollow ();
                break;

            // Layout: Toggle Arranger Track Row Height
            case LAYOUT_TOGGLE_ARRANGER_TRACK_ROW_HEIGHT:
                if (isButtonPressed)
                    this.model.getArranger ().toggleTrackRowHeight ();
                break;

            // Layout: Toggle Arranger Clip Launcher Section
            case LAYOUT_TOGGLE_ARRANGER_CLIP_LAUNCHER_SECTION:
                if (isButtonPressed)
                    this.model.getArranger ().toggleClipLauncher ();
                break;

            // Layout: Toggle Arranger Time Line
            case LAYOUT_TOGGLE_ARRANGER_TIME_LINE:
                if (isButtonPressed)
                    this.model.getArranger ().toggleTimeLine ();
                break;

            // Layout: Toggle Arranger IO Section
            case LAYOUT_TOGGLE_ARRANGER_IO_SECTION:
                if (isButtonPressed)
                    this.model.getArranger ().toggleIoSection ();
                break;

            // Layout: Toggle Arranger Effect Tracks
            case LAYOUT_TOGGLE_ARRANGER_EFFECT_TRACKS:
                if (isButtonPressed)
                    this.model.getArranger ().toggleEffectTracks ();
                break;

            // Layout: Toggle Mixer Clip Launcher Section
            case LAYOUT_TOGGLE_MIXER_CLIP_LAUNCHER_SECTION:
                if (isButtonPressed)
                    this.model.getMixer ().toggleClipLauncherSectionVisibility ();
                break;

            // Layout: Toggle Mixer Cross Fade Section
            case LAYOUT_TOGGLE_MIXER_CROSS_FADE_SECTION:
                if (isButtonPressed)
                    this.model.getMixer ().toggleCrossFadeSectionVisibility ();
                break;

            // Layout: Toggle Mixer Device Section
            case LAYOUT_TOGGLE_MIXER_DEVICE_SECTION:
                if (isButtonPressed)
                    this.model.getMixer ().toggleDeviceSectionVisibility ();
                break;

            // Layout: Toggle Mixer sendsSection
            case LAYOUT_TOGGLE_MIXER_SENDSSECTION:
                if (isButtonPressed)
                    this.model.getMixer ().toggleSendsSectionVisibility ();
                break;

            // Layout: Toggle Mixer IO Section
            case LAYOUT_TOGGLE_MIXER_IO_SECTION:
                if (isButtonPressed)
                    this.model.getMixer ().toggleIoSectionVisibility ();
                break;

            // Layout: Toggle Mixer Meter Section
            case LAYOUT_TOGGLE_MIXER_METER_SECTION:
                if (isButtonPressed)
                    this.model.getMixer ().toggleMeterSectionVisibility ();
                break;

            default:
                throw new FlexiHandlerException (command);
        }
    }
}
