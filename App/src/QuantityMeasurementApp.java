public class QuantityMeasurementApp {

    // Updated Enum with all units (base unit = feet)
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CENTIMETER(0.0328084); // 1 cm = 0.0328084 feet

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    // Generic Quantity Class (unchanged from UC3)
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

        private double toBaseUnit() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || this.getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            // Using tolerance for floating-point safety
            double EPSILON = 0.0001;
            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBaseUnit());
        }
    }

    // Demo
    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength q3 = new QuantityLength(36.0, LengthUnit.INCH);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        QuantityLength q5 = new QuantityLength(0.393701, LengthUnit.INCH);

        System.out.println("1 yard vs 3 feet: " + q1.equals(q2));   // true
        System.out.println("1 yard vs 36 inch: " + q1.equals(q3)); // true
        System.out.println("1 cm vs 0.393701 inch: " + q4.equals(q5)); // true
        System.out.println("Same reference: " + q1.equals(q1)); // true
        System.out.println("Null comparison: " + q1.equals(null)); // false
    }
}