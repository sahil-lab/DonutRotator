package com.chess;

public class Donut {
    public static void main(String[] args) throws InterruptedException {
        // Dimensions of the ASCII output
        final int width = 80;
        final int height = 22;

        // Donut parameters
        final double R1 = 1.0;
        final double R2 = 2.0;
        final double K2 = 5.0;
        final double K1 = 15.0;

        double A = 0.0; // Rotation around x-axis
        double B = 0.0; // Rotation around z-axis

        char[] ramp = ".,-~:;=!*#$@".toCharArray();

        while (true) {
            char[][] output = new char[height][width];
            double[][] zBuffer = new double[height][width];

            // Initialize output and z-buffer
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    output[i][j] = ' ';
                    zBuffer[i][j] = 0;
                }
            }

            double sinA = Math.sin(A), cosA = Math.cos(A);
            double sinB = Math.sin(B), cosB = Math.cos(B);

            for (double theta = 0; theta < 2 * Math.PI; theta += 0.07) {
                double cosTheta = Math.cos(theta);
                double sinTheta = Math.sin(theta);

                for (double phi = 0; phi < 2 * Math.PI; phi += 0.02) {
                    double cosPhi = Math.cos(phi);
                    double sinPhi = Math.sin(phi);

                    double circleX = R2 + R1 * cosTheta;
                    double circleY = R1 * sinTheta;

                    double x = circleX * (cosB * cosPhi + sinA * sinB * sinPhi) - circleY * cosA * sinB;
                    double y = circleX * (sinB * cosPhi - sinA * cosB * sinPhi) + circleY * cosA * cosB;
                    double z = K2 + cosA * circleY * (-1) + circleX * sinPhi * cosA;

                    double ooz = 1 / z;
                    int xp = (int) (width / 2 + K1 * ooz * x);
                    int yp = (int) (height / 2 - K1 * ooz * y);

                    double nx = cosPhi * cosTheta;
                    double ny = sinPhi * cosTheta;
                    double nz = sinTheta;

                    double luminance = nx * sinB + ny * cosB + nz * cosA;

                    // Clamp luminance between 0 and 1
                    luminance = Math.max(0, Math.min(luminance, 1));

                    // Calculate the luminance index
                    int luminanceIndex = Math.max(0, Math.min((int) (luminance * (ramp.length - 1)), ramp.length - 1));

                    if (xp >= 0 && xp < width && yp >= 0 && yp < height) {
                        if (ooz > zBuffer[yp][xp]) {
                            zBuffer[yp][xp] = ooz;
                            output[yp][xp] = ramp[luminanceIndex];
                        }
                    }
                }
            }

            // Clear screen and update output
            System.out.print("\u001b[H"); // Move the cursor to the top-left corner
            for (int i = 0; i < height; i++) {
                System.out.print("\u001b[2K"); // Clear the current line
                System.out.print("\r");       // Return the cursor to the start of the line
                System.out.println(output[i]); // Print the new line
            }

            A += 0.04;
            B += 0.02;

            Thread.sleep(30);
        }
    }
}
