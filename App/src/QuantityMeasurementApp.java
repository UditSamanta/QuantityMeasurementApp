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

    // Immutable Quantity Class
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid value");
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

        // Convert to base (feet)
        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        // ============================
        // ✅ ADDITION (Instance Method)
        // ============================
        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException("Other quantity cannot be null");
            }

            double sumInFeet = this.toBaseUnit() + other.toBaseUnit();

            // Convert back to this object's unit
            double resultValue = unit.fromFeet(sumInFeet);

            return new QuantityLength(resultValue, this.unit);
        }

        // ============================
        // ✅ STATIC ADD (with target unit)
        // ============================
        public static QuantityLength add(QuantityLength q1,
                                         QuantityLength q2,
                                         LengthUnit targetUnit) {

            if (q1 == null || q2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double sumInFeet = q1.toBaseUnit() + q2.toBaseUnit();
            double resultValue = targetUnit.fromFeet(sumInFeet);

            return new QuantityLength(resultValue, targetUnit);
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
    // DEMO METHODS
    // ============================
    public static void demonstrateAddition(QuantityLength q1, QuantityLength q2) {
        System.out.println(q1 + " + " + q2 + " = " + q1.add(q2));
    }

    public static void demonstrateAddition(QuantityLength q1,
                                           QuantityLength q2,
                                           LengthUnit targetUnit) {
        System.out.println(q1 + " + " + q2 + " = "
                + QuantityLength.add(q1, q2, targetUnit));
    }

    // ============================
    // MAIN METHOD
    // ============================
    public static void main(String[] args) {

        QuantityLength f1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength i1 = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength y1 = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength cm1 = new QuantityLength(2.54, LengthUnit.CENTIMETER);

        // Same unit
        demonstrateAddition(f1, new QuantityLength(2.0, LengthUnit.FEET));

        // Cross unit (result in first operand unit)
        demonstrateAddition(f1, i1); // 1 ft + 12 in = 2 ft

        // Reverse (result in inches)
        demonstrateAddition(i1, f1); // 24 inches

        // Yard + feet
        demonstrateAddition(y1, f1); // 1 yard + 1 ft

        // Centimeter + inch
        demonstrateAddition(cm1, new QuantityLength(1.0, LengthUnit.INCH));

        // Using target unit explicitly
        demonstrateAddition(f1, i1, LengthUnit.INCH);

        // Edge cases
        demonstrateAddition(f1, new QuantityLength(0.0, LengthUnit.INCH));
        demonstrateAddition(f1, new QuantityLength(-2.0, LengthUnit.FEET));
    }
}