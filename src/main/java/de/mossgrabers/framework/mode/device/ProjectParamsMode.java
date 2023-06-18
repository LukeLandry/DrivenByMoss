// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2023
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.mode.device;

import de.mossgrabers.framework.configuration.Configuration;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.ContinuousID;
import de.mossgrabers.framework.controller.IControlSurface;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.featuregroup.AbstractParameterMode;
import de.mossgrabers.framework.parameter.IParameter;
import de.mossgrabers.framework.parameterprovider.device.BankParameterProvider;

import java.util.List;
import java.util.function.BooleanSupplier;


/**
 * Mode for editing project remote control parameters.
 *
 * @param <S> The type of the control surface
 * @param <C> The type of the configuration
 *
 * @author Jürgen Moßgraber
 */
public class ProjectParamsMode<S extends IControlSurface<C>, C extends Configuration> extends AbstractParameterMode<S, C, IParameter>
{
    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     * @param isAbsolute If true the value change is happening with a setter otherwise relative
     *            change method is used
     * @param knobs The IDs of the knob to control this mode
     */
    public ProjectParamsMode (final S surface, final IModel model, final boolean isAbsolute, final List<ContinuousID> knobs)
    {
        this (surface, model, isAbsolute, knobs, surface::isShiftPressed);
    }


    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     * @param isAbsolute If true the value change is happening with a setter otherwise relative
     *            change method is used
     * @param knobs The IDs of the knob to control this mode
     * @param isAlternativeFunction Callback function to execute the secondary function, e.g. a
     *            shift button
     */
    public ProjectParamsMode (final S surface, final IModel model, final boolean isAbsolute, final List<ContinuousID> knobs, final BooleanSupplier isAlternativeFunction)
    {
        super ("Project Parameters", surface, model, isAbsolute, model.getProject ().getParameterBank (), knobs, isAlternativeFunction);

        this.setParameterProvider (new BankParameterProvider (model.getProject ().getParameterBank ()));
    }


    /** {@inheritDoc} */
    @Override
    public void onKnobTouch (final int index, final boolean isTouched)
    {
        this.setTouchedKnob (index, isTouched);

        final IParameter param = this.model.getProject ().getParameterBank ().getItem (index);
        if (!param.doesExist ())
            return;

        if (isTouched && this.surface.isDeletePressed ())
        {
            this.surface.setTriggerConsumed (ButtonID.DELETE);
            param.resetValue ();
        }
        param.touchValue (isTouched);
    }


    /** {@inheritDoc} */
    @Override
    public void selectPreviousItem ()
    {
        super.selectPreviousItem ();

        this.mvHelper.notifySelectedProjectParameterPage ();
    }


    /** {@inheritDoc} */
    @Override
    public void selectNextItem ()
    {
        super.selectNextItem ();

        this.mvHelper.notifySelectedProjectParameterPage ();
    }
}