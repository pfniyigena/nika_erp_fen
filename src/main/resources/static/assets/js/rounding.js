// rounding.js

export const RoundingMode = Object.freeze({
  UP: "UP",
  DOWN: "DOWN",
  CEILING: "CEILING",
  FLOOR: "FLOOR",
  HALF_UP: "HALF_UP",
  HALF_DOWN: "HALF_DOWN",
  HALF_EVEN: "HALF_EVEN",
});

/**
 * setScale(value, scale, mode)
 * ----------------------------------------
 * Mimics Java's BigDecimal.setScale(scale, RoundingMode)
 * Example: setScale(2.345, 2, RoundingMode.HALF_UP) → 2.35
 *
 * @param {number} value - The number to round
 * @param {number} scale - Number of decimal places
 * @param {string} mode  - One of RoundingMode.*
 * @returns {number} Rounded number
 */
export function setScale(value, scale = 2, mode = RoundingMode.HALF_UP) {
  const factor = Math.pow(10, scale);
  let scaled = value * factor;

  switch (mode) {
    case RoundingMode.UP:
      scaled = scaled > 0 ? Math.ceil(scaled) : Math.floor(scaled);
      break;

    case RoundingMode.DOWN:
      scaled = scaled > 0 ? Math.floor(scaled) : Math.ceil(scaled);
      break;

    case RoundingMode.CEILING:
      scaled = Math.ceil(scaled);
      break;

    case RoundingMode.FLOOR:
      scaled = Math.floor(scaled);
      break;

    case RoundingMode.HALF_UP:
      scaled = Math.sign(scaled) * Math.floor(Math.abs(scaled) + 0.5);
      break;

    case RoundingMode.HALF_DOWN:
      scaled = Math.sign(scaled) * Math.floor(Math.abs(scaled) + 0.4999999999);
      break;

    case RoundingMode.HALF_EVEN:
      const floorVal = Math.floor(scaled);
      const diff = Math.abs(scaled - floorVal);
      if (diff > 0.5) {
        scaled = Math.sign(scaled) * Math.ceil(Math.abs(scaled));
      } else if (diff < 0.5) {
        scaled = Math.sign(scaled) * floorVal;
      } else {
        // exactly .5 → round to even
        scaled = (floorVal % 2 === 0) ? floorVal : floorVal + 1;
      }
      break;

    default:
      throw new Error(`Unsupported RoundingMode: ${mode}`);
  }

  return scaled / factor;
}
