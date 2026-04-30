import java.util.Objects;

/**
 * UC7: Addition with Target Unit Specification
 * This implementation provides a robust, immutable way to perform unit addition
 * while allowing the caller to explicitly define the output unit.
 */

// 1. Enum to manage units and their conversion factors relative to FEET
enum LengthUnit {
    INCHES(1.0 / 12.0),
    FEET(1.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    public final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }
}

// 2. Main Quantity class implementing immutability and addition logic
class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null.");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number.");
        }
        this.value = value;
        this.unit = unit;
    }

    /**
     * UC6: Implicit addition (defaults to the unit of 'this' object)
     */
    public QuantityLength add(QuantityLength other) {
        return add(this, other, this.unit);
    }

    /**
     * UC7: Explicit addition with target unit specification.
     * This is the primary utility method that avoids code duplication (DRY).
     */
    public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {
        // Validation
        if (l1 == null || l2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Operands and target unit must not be null.");
        }

        // Convert both operands to base unit (FEET) and add
        double val1InBase = l1.value * l1.unit.conversionFactor;
        double val2InBase = l2.value * l2.unit.conversionFactor;
        double sumInBase = val1InBase + val2InBase;

        // Convert sum from base unit to the target unit
        double convertedResult = sumInBase / targetUnit.conversionFactor;

        // Apply rounding to 3 decimal places to handle floating point precision
        double roundedResult = Math.round(convertedResult * 1000.0) / 1000.0;

        return new QuantityLength(roundedResult, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;
        // Use an epsilon for double comparison to account for precision limits
        return Math.abs(that.value - this.value) < 0.001 && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }

    // Getters
    public double getValue() { return value; }
    public LengthUnit getUnit() { return unit; }
}

// 3. Driver App to demonstrate the Use Case requirements
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("--- UC7: Addition with Target Unit Specification ---");

        // Input: add(1.0 FEET, 12.0 INCHES) -> Target: FEET
        printResult(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.FEET);

        // Input: add(1.0 FEET, 12.0 INCHES) -> Target: INCHES
        printResult(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.INCHES);

        // Input: add(1.0 FEET, 12.0 INCHES) -> Target: YARDS
        printResult(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.YARDS);

        // Input: add(36.0 INCHES, 1.0 YARDS) -> Target: FEET
        printResult(new QuantityLength(36.0, LengthUnit.INCHES),
                new QuantityLength(1.0, LengthUnit.YARDS),
                LengthUnit.FEET);

        // Input: add(2.54 CENTIMETERS, 1.0 INCHES) -> Target: CENTIMETERS
        printResult(new QuantityLength(2.54, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.INCHES),
                LengthUnit.CENTIMETERS);

        // Commutativity Check
        QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(-2.0, LengthUnit.FEET);
        QuantityLength resA = QuantityLength.add(q1, q2, LengthUnit.INCHES);
        QuantityLength resB = QuantityLength.add(q2, q1, LengthUnit.INCHES);
        System.out.println("\nCommutativity Check (A+B == B+A): " + resA.equals(resB) + " -> " + resA);
    }

    private static void printResult(QuantityLength l1, QuantityLength l2, LengthUnit target) {
        try {
            QuantityLength result = QuantityLength.add(l1, l2, target);
            System.out.printf("Input: add(%s, %s, %s) \nOutput: %s\n\n",
                    l1, l2, target, result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}