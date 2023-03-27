package anpr.localization;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.awt.image.*;

public class Localization {
	
	public MainIndex main;
	
	public Localization() {
		
	}
	
	public BufferedImage threshold(BufferedImage image, int threshold) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                int average = (r + g + b) / 3;

                if (average > threshold) {
                    result.setRGB(x, y, 0xffffffff);
                } else {
                    result.setRGB(x, y, 0xff000000);
                }
            }
        }

        return result;
    }
	
	public BufferedImage fullEdgeDetector(BufferedImage source) throws IOException {
        float verticalMatrix[] = {
            -1,0,1,
            -2,0,2,
            -1,0,1,
        };
        float horizontalMatrix[] = {
            -1,-2,-1,
            0, 0, 0,
            1, 2, 1
        };
        
        PhotoHandler info = new PhotoHandler(source);
        BufferedImage imageDetect = info.duplicateBufferedImage(source);
        
        BufferedImage i1 = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        BufferedImage i2 = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        
        new ConvolveOp(new Kernel(3, 3, verticalMatrix), ConvolveOp.EDGE_NO_OP, null).filter(imageDetect, i1);
        new ConvolveOp(new Kernel(3, 3, horizontalMatrix), ConvolveOp.EDGE_NO_OP, null).filter(imageDetect, i2);
        
        int w = source.getWidth();
        int h = source.getHeight();
        
        for (int x=0; x < w; x++) {
            for (int y=0; y < h; y++) {
            	float sum = 0.0f;
            	sum += info.getBrightness(i1,x,y);
            	sum += info.getBrightness(i2,x,y);
            	info.setBrightness(imageDetect, x, y, Math.min(1.0f, sum));
            }
        }
        
        BufferedImage output = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        
        Graphics2D g = output.createGraphics();
		g.drawImage(imageDetect, 0, 0, null);
		g.dispose();
        
        info.close();
        
        return output;
    }
	
	public BufferedImage verticalEdgeDetector(BufferedImage source) throws IOException {
		PhotoHandler info = new PhotoHandler(source);
		BufferedImage imageDetect = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        BufferedImage destination = info.duplicateBufferedImage(source);

        float data1[] = {
            -1,0,1,
            -2,0,2,
            -1,0,1,
        };

        /*
        float data2[] = {
            1,0,-1,
            2,0,-2,
            1,0,-1,
        };
        */

        new ConvolveOp(new Kernel(3, 3, data1), ConvolveOp.EDGE_NO_OP, null).filter(destination, imageDetect);
        
        info.close();
        
        BufferedImage output = new BufferedImage(destination.getWidth(), destination.getHeight(), destination.getType());
        
        Graphics2D g = output.createGraphics();
		g.drawImage(imageDetect, 0, 0, null);
		g.dispose();
		
        
        return output;
    }
	
	public BufferedImage horizontalEdgeDetector(BufferedImage source) throws IOException {
		PhotoHandler info = new PhotoHandler(source);
		BufferedImage imageDetect = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        BufferedImage destination = info.duplicateBufferedImage(source);

        float data1[] = {
            -1,-2,-1,
            0,0,0,
            1,2,1,
        };

        new ConvolveOp(new Kernel(3, 3, data1), ConvolveOp.EDGE_NO_OP, null).filter(destination, imageDetect);
        
        info.close();
        
        BufferedImage output = new BufferedImage(destination.getWidth(), destination.getHeight(), destination.getType());
        
        Graphics2D g = output.createGraphics();
		g.drawImage(imageDetect, 0, 0, null);
		g.dispose();
		
        
        return output;
    }
	
	public BufferedImage convertToGrayScale(BufferedImage image) {
		BufferedImage gray_img = new BufferedImage(
			image.getWidth(),
			image.getHeight(),
			BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g = gray_img.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		return gray_img;
	}
	
	public double meanValue(int p[]) {

		double sum=0;
		double len = p.length;
			
		for(int i=0; i<p.length; i++)
			sum+=p[i];
		    
		return sum/len;
	}
	
	
}
