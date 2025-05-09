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
package pixelitor.filters;

import com.jhlabs.image.PixelUtils;
import pixelitor.filters.util.FilterPalette;
import pixelitor.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.Serial;

import static pixelitor.utils.Texts.i18n;

/**
 * Invert filter
 */
public class Invert extends Filter {
    @Serial
    private static final long serialVersionUID = -6279018636064203421L;

    public static final String NAME = i18n("invert");

    @Override
    public BufferedImage transform(BufferedImage src, BufferedImage dest) {
        dest = invertImage(src);

        return dest;
    }

    @Override
    protected boolean createDefaultDestImg() {
        return false;
    }

    public static BufferedImage invertImage(BufferedImage src) {
        BufferedImage dest;
        if (src.getColorModel() instanceof IndexColorModel) {
            return new FilterPalette(src) {
                @Override
                protected int changeRed(int r) {
                    return 255 - r;
                }

                @Override
                protected int changeGreen(int g) {
                    return 255 - g;
                }

                @Override
                protected int changeBlue(int b) {
                    return 255 - b;
                }
            }.filter();
        }

        // normal case
        dest = ImageUtils.createImageWithSameCM(src);

        int[] srcPixels = ImageUtils.getPixels(src);
        int[] destPixels = ImageUtils.getPixels(dest);

        boolean simple = !src.isAlphaPremultiplied();

        for (int i = 0; i < destPixels.length; i++) {
            int rgb = srcPixels[i];
            int a = (rgb >>> 24) & 0xFF;

            if (a == 255 || simple) {
                destPixels[i] = rgb ^ 0x00_FF_FF_FF;  // invert the r, g, b values
            } else if (a == 0) {
                destPixels[i] = 0;
            } else {
                int r = (rgb >>> 16) & 0xFF;
                int g = (rgb >>> 8) & 0xFF;
                int b = rgb & 0xFF;

                // unpremultiply
                float f = 255.0f / a;
                int ur = (int) (r * f);
                int ug = (int) (g * f);
                int ub = (int) (b * f);

                if (ur > 255) {
                    ur = 255;
                }
                if (ug > 255) {
                    ug = 255;
                }
                if (ub > 255) {
                    ub = 255;
                }

                // invert
                ur = 255 - ur;
                ug = 255 - ug;
                ub = 255 - ub;

                // premultiply
                float f2 = a * (1.0f / 255.0f);
                r = (int) (ur * f2);
                g = (int) (ug * f2);
                b = (int) (ub * f2);

                r = PixelUtils.clamp(r);
                g = PixelUtils.clamp(g);
                b = PixelUtils.clamp(b);

                destPixels[i] = a << 24 | r << 16 | g << 8 | b;
            }
        }
        return dest;
    }

    public static void quickInvert(BufferedImage dest) {
        int[] pixels = ImageUtils.getPixels(dest);
        for (int i = 0, pixelsLength = pixels.length; i < pixelsLength; i++) {
            pixels[i] ^= 0x00_FF_FF_FF;
        }
    }

    @Override
    public boolean supportsGray() {
        return false;
    }
}