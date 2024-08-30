package de.mossgrabers.controller.novation.slmkiii.view;

import com.bitwig.extension.controller.api.InsertionPoint;
import de.mossgrabers.controller.novation.slmkiii.SLMkIIIConfiguration;
import de.mossgrabers.controller.novation.slmkiii.controller.SLMkIIIControlSurface;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.ICursorDevice;
import de.mossgrabers.framework.daw.data.ICursorTrack;
import de.mossgrabers.framework.featuregroup.AbstractView;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ShortcutsView<S, C> extends AbstractView<SLMkIIIControlSurface, SLMkIIIConfiguration> {

    private final List<Shortcut> shortcutList = Arrays.asList(new Shortcut[]{
            new Shortcut(ShortcutCategory.BITWIG, "e4815188-ba6f-4d14-bcfc-2dcb8f778ccb"),
            new Shortcut(ShortcutCategory.VST3, "56535437364353636C612D3736207374"),
            new Shortcut(ShortcutCategory.PRESET, "/Users/luke/Documents/Bitwig Studio/Library/Presets/Kontakt 7/Noire.bwpreset"),
            new Shortcut(ShortcutCategory.PRESET, "/Users/luke/Documents/Bitwig Studio/Library/Presets/Kontakt 7/NI Mark I.bwpreset"),
            new Shortcut(ShortcutCategory.PRESET, "/Users/luke/Documents/Bitwig Studio/Library/Presets/Kontakt 7/Scarbee Rick.bwpreset"),
            new Shortcut(ShortcutCategory.PRESET, "/Users/luke/Documents/Bitwig Studio/Library/Presets/Guitar Rig 6/SW Clean Tweed.bwpreset"),
            new Shortcut(ShortcutCategory.PRESET, "/Users/luke/Documents/Bitwig Studio/Library/Presets/Guitar Rig 6/SW Clean Fender.bwpreset"),
            new Shortcut(ShortcutCategory.PRESET, "/Users/luke/Documents/Bitwig Studio/Library/Presets/Guitar Rig 6/GR Vox.bwpreset")
    });


    public ShortcutsView(SLMkIIIControlSurface surface, IModel model) {
        super("Shortcuts", surface, model);
    }

    @Override
    public void drawGrid() {
//        int rows = surface.getPadGrid().getRows();
//        int cols = surface.getPadGrid().getCols();

        surface.getPadGrid().turnOff();
    }

    @Override
    public void onGridNote(int note, int velocity) {

        model.getHost().println("onGridNote(" + note + ", " + velocity + ")");
        int shortcutIndex = note - 36;

        if (velocity > 0 && shortcutIndex < shortcutList.size()) {
            shortcutPressed(shortcutList.get(shortcutIndex));
        }

    }

    private void shortcutPressed (Shortcut shortcut) {

        final ICursorDevice cursorDevice = this.model.getCursorDevice ();
        final boolean hasCursorDevice = cursorDevice.doesExist ();

        final ICursorTrack cursorTrack = this.model.getCursorTrack();
        final boolean hasCursorTrack = cursorTrack.doesExist();

        InsertionPoint ip = null;

        if (hasCursorDevice) {
            ip = cursorDevice.afterDeviceInsertionPoint();
        } else if (hasCursorTrack) {
            ip = cursorTrack.endOfDeviceChainInsertionPoint();
        } else {
            // nowhere to put device
            model.getHost().println("Requested to insert " + shortcut.getValue() + " but no track was selected");
            return;
        }

        if (ip != null) {
            switch (shortcut.getCategory()) {
                case BITWIG -> ip.insertBitwigDevice(UUID.fromString(shortcut.getValue()));
                case VST2 -> ip.insertVST2Device(Integer.valueOf(shortcut.getValue()));
                case VST3 -> ip.insertVST3Device(shortcut.getValue());
                case CLAP -> ip.insertCLAPDevice(shortcut.getValue());
                case PRESET -> ip.insertFile(shortcut.getValue());
            }
        }
    }
}

enum ShortcutCategory {
    BITWIG,
    VST2,
    VST3,
    CLAP,
    PRESET
}

class Shortcut {
    private ShortcutCategory category;
    private String value;
    public Shortcut(ShortcutCategory category, String value) {
        this.category = category;
        this.value = value;
    }
    public ShortcutCategory getCategory() {
        return category;
    }

    public String getValue() {
        return value;
    }
}