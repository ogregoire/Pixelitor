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

package pixelitor.utils;

import pixelitor.filters.gui.IntChoiceParam;
import pixelitor.filters.gui.IntChoiceParam.Item;
import pixelitor.tools.shapes.ShapeType;
import pixelitor.tools.shapes.StarSettings;

import java.awt.geom.Point2D;

/**
 * A shape with a blurred boundary that uses floating-point
 * values to define its edges.
 */
public interface BlurredShape {
    /**
     * Returns 1.0 if the given coordinate is completely outside
     * the shape, 0.0 if it's completely inside, and a number between
     * 0.0 and 1.0 if it's in the blurred boundary region.
     */
    double isOutside(int x, int y);

    static BlurredShape createEmptyShape() {
        return (x, y) -> 1.0; // all points are outside
    }

    int TYPE_ELLIPSE = 0;
    int TYPE_RECTANGLE = 1;
    int TYPE_HEART = 2;
    int TYPE_DIAMOND = 3;
    int TYPE_HEXAGON = 4;
    int TYPE_OCTAGON = 5;
    int TYPE_STAR = 6;

    static IntChoiceParam getChoices() {
        return new IntChoiceParam("Shape", new Item[]{
            new Item("Ellipse", TYPE_ELLIPSE),
            new Item("Rectangle", TYPE_RECTANGLE),
            new Item("Heart", TYPE_HEART),
            new Item("Diamond", TYPE_DIAMOND),
            new Item("Hexagon", TYPE_HEXAGON),
            new Item("Octagon", TYPE_OCTAGON),
            new Item("Star", TYPE_STAR),
        });
    }

    static BlurredShape create(int type, Point2D center,
                               double innerRadiusX, double innerRadiusY,
                               double outerRadiusX, double outerRadiusY) {
        if (outerRadiusX < 1 || outerRadiusY < 1) {
            return createEmptyShape();
        }

        return switch (type) {
            case TYPE_ELLIPSE -> new BlurredEllipse(center,
                innerRadiusX, innerRadiusY,
                outerRadiusX, outerRadiusY);
            case TYPE_RECTANGLE -> GenericBlurredShape.of(
                drag -> ShapeType.RECTANGLE.createShape(drag, null), center,
                innerRadiusX, innerRadiusY,
                outerRadiusX, outerRadiusY);
            case TYPE_HEART -> GenericBlurredShape.of(
                drag -> ShapeType.HEART.createShape(drag, null), center,
                innerRadiusX, innerRadiusY,
                outerRadiusX, outerRadiusY);
            case TYPE_DIAMOND -> GenericBlurredShape.of(
                drag -> ShapeType.DIAMOND.createShape(drag, null), center,
                innerRadiusX, innerRadiusY,
                outerRadiusX, outerRadiusY);
            case TYPE_HEXAGON -> GenericBlurredShape.of(
                drag -> ShapeType.STAR.createShape(drag, new StarSettings(3, 100)), center,
                innerRadiusX, innerRadiusY,
                outerRadiusX, outerRadiusY);
            case TYPE_OCTAGON -> GenericBlurredShape.of(
                drag -> ShapeType.STAR.createShape(drag, new StarSettings(4, 100)), center,
                innerRadiusX, innerRadiusY,
                outerRadiusX, outerRadiusY);
            case TYPE_STAR -> GenericBlurredShape.of(
                drag -> ShapeType.STAR.createShape(drag, new StarSettings()), center,
                innerRadiusX, innerRadiusY,
                outerRadiusX, outerRadiusY);
            default -> throw new IllegalStateException();
        };
    }
}
