public class QuantityMeasurementApp {

    // Base unit = FEET
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.0328084);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }
    }

    // Quantity Class (immutable)
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid numeric value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        // Convert instance to another unit (returns NEW object)
        public QuantityLength convertTo(LengthUnit targetUnit) {
            double convertedValue = convert(this.value, this.unit, targetUnit);
            return new QuantityLength(convertedValue, targetUnit);
        }

        // Private helper: convert to base (feet)
        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            double EPSILON = 1e-6;
            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBaseUnit());
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ============================
    // ✅ STATIC CONVERSION API
    // ============================
    public static double convert(double value, LengthUnit source, LengthUnit target) {

        // Validation
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }

        // Normalize to base (feet)
        double valueInFeet = source.toFeet(value);

        // Convert to target
        return target.fromFeet(valueInFeet);
    }

    // ============================
    // METHOD OVERLOADING DEMO
    // ============================

    // Method 1: raw values
    public static void demonstrateLengthConversion(double value,
                                                   LengthUnit from,
                                                   LengthUnit to) {
        double result = convert(value, from, to);
        System.out.println("convert(" + value + ", " + from + ", " + to + ") = " + result);
    }

    // Method 2: using object
    public static void demonstrateLengthConversion(QuantityLength q,
                                                   LengthUnit to) {
        QuantityLength converted = q.convertTo(to);
        System.out.println(q + " -> " + converted);
    }

    // Equality demo
    public static void demonstrateLengthEquality(QuantityLength q1,
                                                 QuantityLength q2) {
        System.out.println(q1 + " == " + q2 + " : " + q1.equals(q2));
    }

    // ============================
    // MAIN METHOD (TESTING)
    // ============================
    public static void main(String[] args) {

        // Static conversion API
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH); // 12
        demonstrateLengthConversion(3.0, LengthUnit.YARD, LengthUnit.FEET); // 9
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARD); // 1
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH);

        // Instance conversion
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARD);
        demonstrateLengthConversion(q1, LengthUnit.INCH);

        // Equality
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);
        demonstrateLengthEquality(q1, q2); // true

        // Edge cases
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(-1.0, LengthUnit.FEET, LengthUnit.INCH);
    }
}