import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecret {
    public static void main(String[] args) throws Exception {
        // Load JSON input
    	JSONObject input = new JSONObject(new JSONTokener(new FileReader("C:\\Users\\gsaim\\Downloads\\input.json")));
        JSONArray testCases = input.getJSONArray("testCases");

        // Process each test case
        for (int t = 0; t < testCases.length(); t++) {
            JSONObject testCase = testCases.getJSONObject(t);

            // Extract keys n and k
            int n = testCase.getJSONObject("keys").getInt("n");
            int k = testCase.getJSONObject("keys").getInt("k");

            // Parse points from JSON
            List<Point> points = new ArrayList<>();
            for (String key : testCase.keySet()) {
                if (key.equals("keys")) continue;

                int x = Integer.parseInt(key);
                JSONObject point = testCase.getJSONObject(key);
                int base = point.getInt("base");
                String value = point.getString("value");

                BigInteger y = new BigInteger(value, base); // Convert to decimal
                points.add(new Point(x, y));
            }

            // Calculate the constant term using Lagrange interpolation
            BigInteger constantTerm = calculateConstantTerm(points, k);
            System.out.println("Secret for test case " + (t + 1) + " (constant term c): " + constantTerm);
        }
    }

    // Method to calculate the constant term using Lagrange interpolation
    public static BigInteger calculateConstantTerm(List<Point> points, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(points.get(i).x);
            BigInteger yi = points.get(i).y;
            BigInteger term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(points.get(j).x);
                    term = term.multiply(xj.negate()).divide(xi.subtract(xj));
                }
            }
            result = result.add(term);
        }
        return result;
    }

    // Helper class for points
    static class Point {
        int x;
        BigInteger y;

        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
}