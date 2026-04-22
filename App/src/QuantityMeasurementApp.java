public class QuantityMeasurementApp {

    // Step 1: Enum for Units with conversion to base unit (feet)
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    // Step 2: Generic Quantity Class
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        // Convert to base unit (feet)
        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {
            // Same reference
            if (this == obj) return true;

            // Null & type check
            if (obj == null || this.getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            // Compare after conversion
            return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBaseUnit());
        }
    }

    // Main method (demo)
    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength q3 = new QuantityLength(2.0, LengthUnit.FEET);

        System.out.println("1 ft vs 12 inch: " + q1.equals(q2)); // true
        System.out.println("1 ft vs 2 ft: " + q1.equals(q3));    // false
        System.out.println("Same reference: " + q1.equals(q1));  // true
        System.out.println("Null comparison: " + q1.equals(null)); // false
    }
}