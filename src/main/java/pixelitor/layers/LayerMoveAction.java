/*
 * Copyright 2025 Laszlo Balazs-Csiki and Contributors
 *
 * This file is part of Pixelitor. Pixelitor is free software: you
 * can redistribute it and/or modify it under the terms of the GNU
 * General Public License, version 3 as published by the Free
 * Software Foundation.
 *
 * Pixelitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pixelitor. If not, see <http://www.gnu.org/licenses/>.
 */

package pixelitor.layers;

import pixelitor.Composition;
import pixelitor.gui.utils.AbstractViewEnabledAction;
import pixelitor.utils.Icons;

import javax.swing.*;

import static pixelitor.utils.Texts.i18n;

/**
 * An {@link Action} that moves the active layer of the active composition
 * up or down in the layer stack
 */
public class LayerMoveAction extends AbstractViewEnabledAction {
    // menu and history names (also for selection movements)
    public static final String RAISE_LAYER = i18n("raise_layer");
    public static final String LOWER_LAYER = i18n("lower_layer");
    public static final String LAYER_TO_TOP = i18n("layer_to_top");
    public static final String LAYER_TO_BOTTOM = i18n("layer_to_bottom");
    public static final String LOWER_LAYER_SELECTION = i18n("lower_layer_selection");
    public static final String RAISE_LAYER_SELECTION = i18n("raise_layer_selection");

    public static final LayerMoveAction MOVE_LAYER_UP = new LayerMoveAction(true);
    public static final LayerMoveAction MOVE_LAYER_DOWN = new LayerMoveAction(false);

    private final boolean up;

    private LayerMoveAction(boolean up) {
        super(getName(up), getIcon(up));
        this.up = up;
        setToolTip(up ? i18n("raise_layer_tt") : i18n("lower_layer_tt"));
    }

    @Override
    protected void onClick(Composition comp) {
        comp.getActiveHolder().reorderActiveLayer(up);
    }

    private static Icon getIcon(boolean up) {
        return up ? Icons.getUpArrowIcon() : Icons.getDownArrowIcon();
    }

    private static String getName(boolean up) {
        return up ? RAISE_LAYER : LOWER_LAYER;
    }
}
