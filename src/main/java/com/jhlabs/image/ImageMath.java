/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.jhlabs.image;

/**
 * A class containing static math methods useful for image processing.
 */
public class ImageMath {
    /**
     * The value of pi as a float.
     */
    public static final float PI = (float) Math.PI;

    /**
     * The value of half pi as a float.
     */
    public static final float HALF_PI = (float) Math.PI / 2.0f;

    /**
     * The value of quarter pi as a float.
     */
    public static final float QUARTER_PI = (float) Math.PI / 4.0f;

    /**
     * The value of two pi as a float.
     */
    public static final float TWO_PI = (float) Math.PI * 2.0f;
    public static final double SQRT_3 = 1.7320508075688772;
    public static final double HALF_SQRT_3 = SQRT_3 / 2.0;
    public static final double SQRT_2 = 1.4142135623730951;

    private ImageMath() {
    }

    /**
     * Apply a bias to a number in the unit interval, moving numbers towards 0 or 1
     * according to the bias parameter.
     *
     * @param a the number to bias
     * @param b the bias parameter. 0.5 means no change, smaller values bias towards 0, larger towards 1.
     * @return the output value
     */
    public static float bias(float a, float b) {
//		return (float)Math.pow(a, Math.log(b) / Math.log(0.5));
        return a / ((1.0f / b - 2) * (1.0f - a) + 1);
    }

    /**
     * A variant of the gamma function.
     *
     * @param a the number to apply gain to
     * @param b the gain parameter. 0.5 means no change, smaller values reduce gain, larger values increase gain.
     * @return the output value
     */
    public static float gain(float a, float b) {
/*
        float p = (float)Math.log(1.0 - b) / (float)Math.log(0.5);

		if (a < .001)
			return 0.0f;
		else if (a > .999)
			return 1.0f;
		if (a < 0.5)
			return (float)Math.pow(2 * a, p) / 2;
		else
			return 1.0f - (float)Math.pow(2 * (1. - a), p) / 2;
*/
        float c = (1.0f / b - 2.0f) * (1.0f - 2.0f * a);
        if (a < 0.5) {
            return a / (c + 1.0f);
        } else {
            return (c - a) / (c - 1.0f);
        }
    }

    /**
     * The step function. Returns 0 below a threshold, 1 above.
     *
     * @param a the threshold position
     * @param x the input parameter
     * @return the output value - 0 or 1
     */
    public static float step(float a, float x) {
        return (x < a) ? 0.0f : 1.0f;
    }

    /**
     * The pulse function. Returns 1 between two thresholds, 0 outside.
     *
     * @param a the lower threshold position
     * @param b the upper threshold position
     * @param x the input parameter
     * @return the output value - 0 or 1
     */
    public static float pulse(float a, float b, float x) {
        return (x < a || x >= b) ? 0.0f : 1.0f;
    }

    /**
     * A smoothed pulse function. A cubic function is used to smooth the step between two thresholds.
     *
     * @param a1 the lower threshold position for the start of the pulse
     * @param a2 the upper threshold position for the start of the pulse
     * @param b1 the lower threshold position for the end of the pulse
     * @param b2 the upper threshold position for the end of the pulse
     * @param x  the input parameter
     * @return the output value
     */
    public static float smoothPulse(float a1, float a2, float b1, float b2, float x) {
        if (x < a1 || x >= b2) {
            return 0;
        }
        if (x >= a2) {
            if (x < b1) {
                return 1.0f;
            }
            x = (x - b1) / (b2 - b1);
            return 1.0f - smoothStep01(x);
        }
        x = (x - a1) / (a2 - a1);
        return smoothStep01(x);
    }

    /**
     * A smoothed step function. A cubic function is used to smooth the
     * step between two thresholds.
     * It smoothly interpolates between 0 and 1 as x moves from a to b.
     *
     * @param a the lower threshold position
     * @param b the upper threshold position
     * @param x the input parameter
     * @return the output value
     */
    public static float smoothStep(float a, float b, float x) {
        if (x < a) {
            return 0;
        }
        if (x >= b) {
            return 1;
        }
        x = (x - a) / (b - a);
        return smoothStep01(x);
    }

    // cubic smooth step for floats assuming that
    // the input is already in the 0..1 range
    public static float smoothStep01(float x) {
        return x * x * (3.0f - 2.0f * x);
    }

    // quintic smooth step for floats assuming that
    // the input is already in the 0..1 range
    public static float smootherStep01(float x) {
        return x * x * x * (x * (x * 6 - 15) + 10);
    }

    // smooth step with doubles
    public static double smoothStep(double a, double b, double x) {
        if (x < a) {
            return 0;
        }
        if (x >= b) {
            return 1;
        }
        x = (x - a) / (b - a);
        return smoothStep01(x);
    }

    // cubic smooth step for doubles assuming that
    // the input is already in the 0..1 range
    public static double smoothStep01(double x) {
        return x * x * (3.0 - 2.0 * x);
    }

    // quintic smooth step for doubles assuming that
    // the input is already in the 0..1 range
    public static double smootherStep01(double x) {
        return x * x * x * (x * (x * 6 - 15) + 10);
    }

    /**
     * A "circle up" function. Returns y on a unit circle given 1-x. Useful for forming bevels.
     *
     * @param x the input parameter in the range 0..1
     * @return the output value
     */
    public static float circleUp(float x) {
        x = 1 - x;
        return (float) Math.sqrt(1 - x * x);
    }

    /**
     * A "circle down" function. Returns 1-y on a unit circle given x. Useful for forming bevels.
     *
     * @param x the input parameter in the range 0..1
     * @return the output value
     */
    public static float circleDown(float x) {
        return 1.0f - (float) Math.sqrt(1 - x * x);
    }

    /**
     * Clamp a value to an interval.
     *
     * @param min     the lower clamp threshold
     * @param max     the upper clamp threshold
     * @param input the input parameter
     * @return the clamped value
     */
    public static float clamp(float input, float min, float max) {
        return (input < min) ? min : (input > max) ? max : input;
    }

    public static double clamp(double input, double min, double max) {
        return (input < min) ? min : (input > max) ? max : input;
    }

    public static float clamp01(float input) {
        return (input < 0.0f) ? 0.0f : (input > 1.0f) ? 1.0f : input;
    }

    public static double clamp01(double input) {
        return (input < 0.0) ? 0.0 : (input > 1.0) ? 1.0 : input;
    }

    /**
     * Clamp a value to an interval.
     *
     * @param min     the lower clamp threshold
     * @param max     the upper clamp threshold
     * @param input the input parameter
     * @return the clamped input
     */
    public static int clamp(int input, int min, int max) {
        return Math.min(max, Math.max(input, min));
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     *
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    public static double mod(double a, double b) {
        int n = (int) (a / b);

        a -= n * b;
        if (a < 0) {
            return a + b;
        }
        return a;
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     *
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    public static float mod(float a, float b) {
        int n = (int) (a / b);

        a -= n * b;
        if (a < 0) {
            return a + b;
        }
        return a;
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     *
     * @param a the dividend
     * @param b the divisor
     * @return a mod b
     */
    public static int mod(int a, int b) {
        assert (b > 0);
        if (a >= 0) {
            return a % b;
        }

        int n = a / b;

        a -= n * b;
        if (a < 0) {
            return a + b;
        }
        return a;
    }

    /**
     * The triangle function. Returns a repeating triangle shape in the range 0..1 with wavelength 1.0
     *
     * @param x the input parameter
     * @return the output value
     */
    public static float triangle(float x) {
        float r = mod(x, 1.0f);
        return 2.0f * (r < 0.5 ? r : 1 - r);
    }

    // triangle for doubles
    public static double triangle(double x) {
        double r = mod(x, 1.0);
        return 2.0 * (r < 0.5 ? r : 1 - r);
    }

    /**
     * Returns a repeating triangle with range 0..width and with wavelength 2*width
     * Intended to be used as the "reflect" edge action in transform filters
     */
    public static int reflectTriangle(int x, int width) {
        int doubleWidth = 2 * width;
        int mod = mod(x, doubleWidth);

        return mod >= width ? doubleWidth - mod - 1 : mod;
    }

    /**
     * A triangle function with a period of 2 PI and values between -1 and 1
     */
    public static float sinLikeTriangle(float x) {
        return 2 * triangle(x / (2 * ImageMath.PI)) - 1;
    }

    public static double sinLikeTriangle(double x) {
        return 2 * triangle(x / (2 * Math.PI)) - 1;
    }

    /**
     * A sawtooth function with a period of 2 PI and values between -1 and 1
     */
    public static float sinLikeSawtooth(float x) {
        return 2 * mod(x / (2 * PI), 1) - 1;
    }

    public static double sinLikeSawtooth(double x) {
        return 2 * mod(x / (2 * Math.PI), 1) - 1;
    }

    /**
     * Linear interpolation.
     *
     * @param t the interpolation parameter
     * @param a the lower interpolation range
     * @param b the upper interpolation range
     * @return the interpolated value
     */
    public static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    /**
     * Linear interpolation with doubles.
     *
     * @param t the interpolation parameter
     * @param a the lower interpolation range
     * @param b the upper interpolation range
     * @return the interpolated value
     */
    public static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    /**
     * Linear interpolation.
     *
     * @param t the interpolation parameter
     * @param a the lower interpolation range
     * @param b the upper interpolation range
     * @return the interpolated value
     */
    public static int lerp(float t, int a, int b) {
        return (int) (a + t * (b - a));
    }

    public static int lerp(double t, int a, int b) {
        return (int) (a + t * (b - a));
    }

    /**
     * Linear interpolation of ARGB values.
     *
     * @param t    the interpolation parameter
     * @param rgb1 the lower interpolation range
     * @param rgb2 the upper interpolation range
     * @return the interpolated value
     */
    public static int mixColors(float t, int rgb1, int rgb2) {
        int a1 = (rgb1 >> 24) & 0xff;
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = rgb1 & 0xff;
        int a2 = (rgb2 >> 24) & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = rgb2 & 0xff;
        a1 = lerp(t, a1, a2);
        r1 = lerp(t, r1, r2);
        g1 = lerp(t, g1, g2);
        b1 = lerp(t, b1, b2);
        return (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
    }

    /**
     * Bilinear interpolation of ARGB values.
     *
     * @param x   the X interpolation parameter 0..1
     * @param y   the y interpolation parameter 0..1
     * @param rgb array of four ARGB values in the order NW, NE, SW, SE
     * @return the interpolated value
     */
    public static int bilinearInterpolate(float x, float y, int nw, int ne, int sw, int se) {
        float m0, m1;
        int a0 = (nw >> 24) & 0xff;
        int r0 = (nw >> 16) & 0xff;
        int g0 = (nw >> 8) & 0xff;
        int b0 = nw & 0xff;
        int a1 = (ne >> 24) & 0xff;
        int r1 = (ne >> 16) & 0xff;
        int g1 = (ne >> 8) & 0xff;
        int b1 = ne & 0xff;
        int a2 = (sw >> 24) & 0xff;
        int r2 = (sw >> 16) & 0xff;
        int g2 = (sw >> 8) & 0xff;
        int b2 = sw & 0xff;
        int a3 = (se >> 24) & 0xff;
        int r3 = (se >> 16) & 0xff;
        int g3 = (se >> 8) & 0xff;
        int b3 = se & 0xff;

        float cx = 1.0f - x;
        float cy = 1.0f - y;

        m0 = cx * a0 + x * a1;
        m1 = cx * a2 + x * a3;
        int a = (int) (cy * m0 + y * m1);

        m0 = cx * r0 + x * r1;
        m1 = cx * r2 + x * r3;
        int r = (int) (cy * m0 + y * m1);

        m0 = cx * g0 + x * g1;
        m1 = cx * g2 + x * g3;
        int g = (int) (cy * m0 + y * m1);

        m0 = cx * b0 + x * b1;
        m1 = cx * b2 + x * b3;
        int b = (int) (cy * m0 + y * m1);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int bilinearInterpolateD(double x, double y, int nw, int ne, int sw, int se) {
        double m0, m1;
        int a0 = (nw >> 24) & 0xff;
        int r0 = (nw >> 16) & 0xff;
        int g0 = (nw >> 8) & 0xff;
        int b0 = nw & 0xff;
        int a1 = (ne >> 24) & 0xff;
        int r1 = (ne >> 16) & 0xff;
        int g1 = (ne >> 8) & 0xff;
        int b1 = ne & 0xff;
        int a2 = (sw >> 24) & 0xff;
        int r2 = (sw >> 16) & 0xff;
        int g2 = (sw >> 8) & 0xff;
        int b2 = sw & 0xff;
        int a3 = (se >> 24) & 0xff;
        int r3 = (se >> 16) & 0xff;
        int g3 = (se >> 8) & 0xff;
        int b3 = se & 0xff;

        double cx = 1.0f - x;
        double cy = 1.0f - y;

        m0 = cx * a0 + x * a1;
        m1 = cx * a2 + x * a3;
        int a = (int) (cy * m0 + y * m1);

        m0 = cx * r0 + x * r1;
        m1 = cx * r2 + x * r3;
        int r = (int) (cy * m0 + y * m1);

        m0 = cx * g0 + x * g1;
        m1 = cx * g2 + x * g3;
        int g = (int) (cy * m0 + y * m1);

        m0 = cx * b0 + x * b1;
        m1 = cx * b2 + x * b3;
        int b = (int) (cy * m0 + y * m1);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Return the NTSC gray level of an RGB value.
     *
     * @param rgb1 the input pixel
     * @return the gray level (0-255)
     */
    public static int brightnessNTSC(int rgb) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        return (int) (r * 0.299f + g * 0.587f + b * 0.114f);
    }

    // Catmull-Rom splines
    private static final float m00 = -0.5f;
    private static final float m01 = 1.5f;
    private static final float m02 = -1.5f;
    private static final float m03 = 0.5f;
    private static final float m10 = 1.0f;
    private static final float m11 = -2.5f;
    private static final float m12 = 2.0f;
    private static final float m13 = -0.5f;
    private static final float m20 = -0.5f;
    private static final float m21 = 0.0f;
    private static final float m22 = 0.5f;
    private static final float m23 = 0.0f;
    private static final float m30 = 0.0f;
    private static final float m31 = 1.0f;
    private static final float m32 = 0.0f;
    private static final float m33 = 0.0f;

    /**
     * Compute a Catmull-Rom spline.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param knots    the array of knots
     * @return the spline value
     */
    public static float spline(float x, int numKnots, float[] knots) {
        int span;
        int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }

        x = clamp01(x) * numSpans;
        span = (int) x;
        if (span > numKnots - 4) {
            span = numKnots - 4;
        }
        x -= span;

        k0 = knots[span];
        k1 = knots[span + 1];
        k2 = knots[span + 2];
        k3 = knots[span + 3];

        c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
        c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
        c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
        c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;

        return ((c3 * x + c2) * x + c1) * x + c0;
    }

    /**
     * Compute a Catmull-Rom spline.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param knots    the array of knots
     * @return the spline value
     */
    public static float splineClamped(float x, int numKnots, float[] knots) {
        int span;
        int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }

        x = clamp01(x) * numSpans;
        span = (int) x;
        if (span > numKnots - 4) {
            span = numKnots - 4;
        }
        x -= span;

        k0 = knots[span];
        k1 = knots[span + 1];
        k2 = knots[span + 2];
        k3 = knots[span + 3];

        c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
        c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
        c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
        c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;

        float result = ((c3 * x + c2) * x + c1) * x + c0;
        return result < k1 ? k1 : (result > k2 ? k2 : result);
    }

    /**
     * Compute a Catmull-Rom spline, but with variable knot spacing.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param xknots   the array of knot x values
     * @param yknots   the array of knot y values
     * @return the spline value
     */
    public static float spline(float x, int numKnots, int[] xknots, int[] yknots) {
        int span;
        int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }

        for (span = 0; span < numSpans; span++) {
            if (xknots[span + 1] > x) {
                break;
            }
        }
        if (span > numKnots - 3) {
            span = numKnots - 3;
        }
        float t = (x - xknots[span]) / (xknots[span + 1] - xknots[span]);
        span--;
        if (span < 0) {
            span = 0;
            t = 0;
        }

        k0 = yknots[span];
        k1 = yknots[span + 1];
        k2 = yknots[span + 2];
        k3 = yknots[span + 3];

        c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
        c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
        c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
        c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;

        return ((c3 * t + c2) * t + c1) * t + c0;
    }

    /**
     * Compute a Catmull-Rom spline for RGB values.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param knots    the array of knots
     * @return the spline value
     */
    public static int colorSpline(float x, int numKnots, int[] knots) {
        int span;
        int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }

        x = clamp01(x) * numSpans;
        span = (int) x;
        if (span > numKnots - 4) {
            span = numKnots - 4;
        }
        x -= span;

        int v = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;

            k0 = (knots[span] >> shift) & 0xff;
            k1 = (knots[span + 1] >> shift) & 0xff;
            k2 = (knots[span + 2] >> shift) & 0xff;
            k3 = (knots[span + 3] >> shift) & 0xff;

            c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
            c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
            c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
            c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;
            int n = (int) (((c3 * x + c2) * x + c1) * x + c0);
            if (n < 0) {
                n = 0;
            } else if (n > 255) {
                n = 255;
            }
            v |= n << shift;
        }

        return v;
    }

    /**
     * Compute a Catmull-Rom spline for RGB values, but with variable knot spacing.
     *
     * @param x        the input parameter
     * @param numKnots the number of knots in the spline
     * @param xknots   the array of knot x values
     * @param yknots   the array of knot y values
     * @return the spline value
     */
    public static int colorSpline(int x, int numKnots, int[] xknots, int[] yknots) {
        int span;
        int numSpans = numKnots - 3;
        float k0, k1, k2, k3;
        float c0, c1, c2, c3;

        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }

        for (span = 0; span < numSpans; span++) {
            if (xknots[span + 1] > x) {
                break;
            }
        }
        if (span > numKnots - 3) {
            span = numKnots - 3;
        }
        float t = (float) (x - xknots[span]) / (xknots[span + 1] - xknots[span]);
        span--;
        if (span < 0) {
            span = 0;
            t = 0;
        }

        int v = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;

            k0 = (yknots[span] >> shift) & 0xff;
            k1 = (yknots[span + 1] >> shift) & 0xff;
            k2 = (yknots[span + 2] >> shift) & 0xff;
            k3 = (yknots[span + 3] >> shift) & 0xff;

            c3 = m00 * k0 + m01 * k1 + m02 * k2 + m03 * k3;
            c2 = m10 * k0 + m11 * k1 + m12 * k2 + m13 * k3;
            c1 = m20 * k0 + m21 * k1 + m22 * k2 + m23 * k3;
            c0 = m30 * k0 + m31 * k1 + m32 * k2 + m33 * k3;
            int n = (int) (((c3 * t + c2) * t + c1) * t + c0);
            if (n < 0) {
                n = 0;
            } else if (n > 255) {
                n = 255;
            }
            v |= n << shift;
        }

        return v;
    }

    /**
     * An implementation of Fant's resampling algorithm.
     *
     * @param source the source pixels
     * @param dest   the destination pixels
     * @param length the length of the scanline to resample
     * @param offset the start offset into the arrays
     * @param stride the offset between pixels in consecutive rows
     * @param out    an array of output positions for each pixel
     */
    public static void resample(int[] source, int[] dest, int length, int offset, int stride, float[] out) {
        int i, j;
        float sizfac;
        float inSegment;
        float outSegment;
        int a, r, g, b, nextA, nextR, nextG, nextB;
        float aSum, rSum, gSum, bSum;
        float[] in;
        int srcIndex = offset;
        int destIndex = offset;
        int lastIndex = source.length;
        int rgb;

        in = new float[length + 2];
        i = 0;
        for (j = 0; j < length; j++) {
            while (out[i + 1] < j) {
                i++;
            }
            in[j] = i + (j - out[i]) / (out[i + 1] - out[i]);
//			in[j] = ImageMath.clamp( in[j], 0, length-1 );
        }
        in[length] = length;
        in[length + 1] = length;

        inSegment = 1.0f;
        outSegment = in[1];
        sizfac = outSegment;
        aSum = rSum = gSum = bSum = 0.0f;
        rgb = source[srcIndex];
        a = (rgb >> 24) & 0xff;
        r = (rgb >> 16) & 0xff;
        g = (rgb >> 8) & 0xff;
        b = rgb & 0xff;
        srcIndex += stride;
        rgb = source[srcIndex];
        nextA = (rgb >> 24) & 0xff;
        nextR = (rgb >> 16) & 0xff;
        nextG = (rgb >> 8) & 0xff;
        nextB = rgb & 0xff;
        srcIndex += stride;
        i = 1;

        while (i <= length) {
            float aIntensity = inSegment * a + (1.0f - inSegment) * nextA;
            float rIntensity = inSegment * r + (1.0f - inSegment) * nextR;
            float gIntensity = inSegment * g + (1.0f - inSegment) * nextG;
            float bIntensity = inSegment * b + (1.0f - inSegment) * nextB;
            if (inSegment < outSegment) {
                aSum += (aIntensity * inSegment);
                rSum += (rIntensity * inSegment);
                gSum += (gIntensity * inSegment);
                bSum += (bIntensity * inSegment);
                outSegment -= inSegment;
                inSegment = 1.0f;
                a = nextA;
                r = nextR;
                g = nextG;
                b = nextB;
                if (srcIndex < lastIndex) {
                    rgb = source[srcIndex];
                }
                nextA = (rgb >> 24) & 0xff;
                nextR = (rgb >> 16) & 0xff;
                nextG = (rgb >> 8) & 0xff;
                nextB = rgb & 0xff;
                srcIndex += stride;
            } else {
                aSum += (aIntensity * outSegment);
                rSum += (rIntensity * outSegment);
                gSum += (gIntensity * outSegment);
                bSum += (bIntensity * outSegment);
                dest[destIndex] =
                        ((int) Math.min(aSum / sizfac, 255) << 24) |
                                ((int) Math.min(rSum / sizfac, 255) << 16) |
                                ((int) Math.min(gSum / sizfac, 255) << 8) |
                                (int) Math.min(bSum / sizfac, 255);
                destIndex += stride;
                aSum = rSum = gSum = bSum = 0.0f;
                inSegment -= outSegment;
                outSegment = in[i + 1] - in[i];
                sizfac = outSegment;
                i++;
            }
        }
    }

    public static void premultiply(int[] p) {
        premultiply(p, 0, p.length);
    }

    /**
     * Premultiply a block of pixels
     */
    public static void premultiply(int[] p, int offset, int length) {
        length += offset;
        for (int i = offset; i < length; i++) {
            int rgb = p[i];
            int a = (rgb >> 24) & 0xff;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;
            float f = a * (1.0f / 255.0f);
            r = (int) (r * f);
            g = (int) (g * f);
            b = (int) (b * f);
            p[i] = (a << 24) | (r << 16) | (g << 8) | b;
        }
    }

    public static void unpremultiply(int[] p) {
        unpremultiply(p, 0, p.length);
    }

    /**
     * Premultiply a block of pixels
     */
    public static void unpremultiply(int[] p, int offset, int length) {
        length += offset;
        for (int i = offset; i < length; i++) {
            int rgb = p[i];
            int a = (rgb >> 24) & 0xff;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;
            if (a != 0 && a != 255) {
                float f = 255.0f / a;
                r = (int) (r * f);
                g = (int) (g * f);
                b = (int) (b * f);
                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                if (b > 255) {
                    b = 255;
                }
                p[i] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
    }
}

