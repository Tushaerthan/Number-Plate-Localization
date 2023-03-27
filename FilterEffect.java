package anpr.filter;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.io.IOException;

import anpr.localization.PhotoHandler;

public class FilterEffect {
	
	public BufferedImage gaussianBlur(BufferedImage source, int radius) throws IOException {
		PhotoHandler info = new PhotoHandler(source);
		BufferedImage output = info.duplicateBufferedImage(source);
		
		int size = radius * 2 + 1;
        float[] data = new float[size * size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                int distance = i * i + j * j;
                int index = (i + radius) * size + (j + radius);
                data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
                total += data[index];
            }
        }
        
        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }
        
        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel);
        
        info.close();
        
        BufferedImage tempOutput = op.filter(output, null);
        
        // resize image
        int newWidth = output.getWidth() - radius - radius;
        int newHeight = output.getHeight() - radius - radius;

        BufferedImage resizedBufferedImage = new BufferedImage(newWidth, newHeight, output.getType());
        
        for (int x = 0 + radius; x < tempOutput.getWidth() - radius; x++) {
        	for (int y = 0 + radius; y < tempOutput.getHeight() - radius; y++) {
        		int pixel = tempOutput.getRGB(x, y);
        		resizedBufferedImage.setRGB(x - radius, y - radius, pixel);
        	}
        }
        
        return resizedBufferedImage;
        /*
        return tempOutput;
         */
	}
	
	public BufferedImage enhance(BufferedImage source) {

        // Create a copy of the original image
        BufferedImage contrastImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                // Get the pixel value of the original image
                int pixel = source.getRGB(x, y);

                // Get the red, green, and blue components of the pixel
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                // Increase the contrast of the red, green, and blue components
                red = (int)((red - 128) * 1.5 + 128);
                green = (int)((green - 128) * 1.5 + 128);
                blue = (int)((blue - 128) * 1.5 + 128);

                // Make sure the values are still within the valid range (0 - 255)
                red = Math.max(0, Math.min(255, red));
                green = Math.max(0, Math.min(255, green));
                blue = Math.max(0, Math.min(255, blue));

                // Set the new pixel value in the contrast image
                int newPixel = (red << 16) | (green << 8) | blue;
                contrastImage.setRGB(x, y, newPixel);
            }
        }

        return contrastImage;
    }
	
	public BufferedImage setBrightness(BufferedImage source, float coef) {
		BufferedImage output = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		
		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				int pixel = source.getRGB(x, y);
		        int alpha = (pixel >> 24) & 0xff;
		        int red = (pixel >> 16) & 0xff;
		        int green = (pixel >> 8) & 0xff;
		        int blue = pixel & 0xff;

		        // Decrease the brightness by a factor of 0.8
		        red = (int)(red * coef);
		        green = (int)(green * coef);
		        blue = (int)(blue * coef);

		        // Make sure the values stay within the range 0-255
		        red = Math.min(Math.max(0, red), 255);
		        green = Math.min(Math.max(0, green), 255);
		        blue = Math.min(Math.max(0, blue), 255);

		        // Set the new pixel value
		        int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
		        output.setRGB(x, y, newPixel);
			}
		}
		
		return output;
	}
	
	public BufferedImage setBright(BufferedImage source, float coef) {
		
		BufferedImageOp contrast = new RescaleOp(1f, coef, null);
		BufferedImage result= new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		contrast.filter(source, result);
		
		return result;
	}
	
}
