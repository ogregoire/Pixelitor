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

package pixelitor.utils.input;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import static java.awt.event.KeyEvent.VK_SHIFT;

/**
 * Represents the state of the Shift key during a {@link KeyEvent}.
 */
public enum Shift implements EventMaskModifier {
    PRESSED {
        @Override
        public int modify(int currentMask) {
            return currentMask | KeyEvent.SHIFT_DOWN_MASK;
        }
    }, RELEASED {
        @Override
        public int modify(int currentMask) {
            return currentMask;
        }
    };

    public boolean isDown() {
        return this == PRESSED;
    }

    public static Shift from(MouseEvent e) {
        return e.isShiftDown() ? PRESSED : RELEASED;
    }

    public static Shift randomly(Random rand) {
        return rand.nextBoolean() ? PRESSED : RELEASED;
    }

    public Shift press(Robot robot) {
        if (this == PRESSED) {
            robot.keyPress(VK_SHIFT);
            robot.delay(50);
        }
        return this;
    }

    public void release(Robot robot) {
        if (this == PRESSED) {
            robot.keyRelease(VK_SHIFT);
            robot.delay(50);
        }
    }
}
