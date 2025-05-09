/*
 * Copyright 2024 Laszlo Balazs-Csiki and Contributors
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
package pixelitor.filters.animation;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Writes out the frames of a rendered tweening animation.
 */
public interface AnimationWriter {
    /**
     * Adds a frame to the animation.
     */
    void addFrame(BufferedImage image) throws IOException;

    /**
     * Called if the animation rendering was canceled.
     */
    void cancel();

    /**
     * Called after adding all the frames (if it was not canceled).
     */
    void finish();
}
