// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2025
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.ableton.push.parameterprovider;

import de.mossgrabers.framework.daw.data.IChannel;
import de.mossgrabers.framework.daw.data.ISpecificDevice;
import de.mossgrabers.framework.daw.data.empty.EmptyParameter;
import de.mossgrabers.framework.observer.IItemSelectionObserver;
import de.mossgrabers.framework.observer.IParametersAdjustObserver;
import de.mossgrabers.framework.parameter.IParameter;
import de.mossgrabers.framework.parameterprovider.device.VolumeLayerOrDrumPadParameterProvider;

import java.util.Optional;


/**
 * Extends volume layer or drum pad parameter provider with the specific layout of Push 2.
 *
 * @author Jürgen Moßgraber
 */
public class PushVolumeLayerOrDrumPadParameterProvider extends VolumeLayerOrDrumPadParameterProvider implements IItemSelectionObserver
{
    /**
     * Constructor.
     *
     * @param device Uses the layer bank from the given device to get the parameters
     */
    public PushVolumeLayerOrDrumPadParameterProvider (final ISpecificDevice device)
    {
        super (device);
    }


    /** {@inheritDoc} */
    @Override
    public void addParametersObserver (final IParametersAdjustObserver observer)
    {
        super.addParametersObserver (observer);

        this.bank.addSelectionObserver (this);
    }


    /** {@inheritDoc} */
    @Override
    public void removeParametersObserver (final IParametersAdjustObserver observer)
    {
        super.removeParametersObserver (observer);

        if (!this.hasObservers ())
            this.bank.removeSelectionObserver (this);
    }


    /** {@inheritDoc} */
    @Override
    public IParameter get (final int index)
    {
        if (!this.device.hasLayers ())
            return EmptyParameter.INSTANCE;

        // Drum Pad Bank has size of 16, layers only 8
        int offset = 0;
        if (this.device.hasDrumPads ())
        {
            final Optional<? extends IChannel> selectedDrumPad = this.bank.getSelectedItem ();
            if (selectedDrumPad.isPresent () && selectedDrumPad.get ().getIndex () > 7)
                offset = 8;
        }

        return super.get (offset + index);
    }


    /** {@inheritDoc} */
    @Override
    public void call (final int index, final boolean isSelected)
    {
        this.notifyParametersObservers ();
    }
}