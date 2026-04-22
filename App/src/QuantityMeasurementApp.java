public class QuantityMeasurementApp {

    // Inner class to represent Feet measurement
    static class Feet {
        private final double value;

        // Constructor
        public Feet(double value) {
            this.value = value;
        }

        // Getter (optional)
        public double getValue() {
            return value;
        }

        // Override equals() method
        @Override
        public boolean equals(Object obj) {

            // Same reference check
            if (this == obj) return true;

            // Null and type check
            if (obj == null || this.getClass() != obj.getClass()) return false;

            // Type casting
            Feet other = (Feet) obj;

            // Compare double values safely
            return Double.compare(this.value, other.value) == 0;
        }

        // Optional: override hashCode when equals is overridden
        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    // Main method to test functionality
    public static void main(String[] args) {

        Feet value1 = new Feet(1.0);
        Feet value2 = new Feet(1.0);
        Feet value3 = new Feet(2.0);

        // Test cases
        System.out.println("1.0 ft vs 1.0 ft: " + value1.equals(value2)); // true
        System.out.println("1.0 ft vs 2.0 ft: " + value1.equals(value3)); // false
        System.out.println("1.0 ft vs null: " + value1.equals(null));     // false
        System.out.println("Same reference: " + value1.equals(value1));   // true
    }
}