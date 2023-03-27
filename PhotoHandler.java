package anpr.localization;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

public class PhotoHandler implements Closeable {
	BufferedImage image = null;
	
	public PhotoHandler(BufferedImage img) {
		this.image = img;
	}
	
	public int getWidth() {
        return this.image.getWidth();
    }
    public int getHeight() {
        return this.image.getHeight();
    }
    public int getSquare() {
        return this.getWidth() * this.getHeight();
    }
	
	public float getHue(int x, int y) {
		return this.getHue(this.image, x, y);
	}
	
	public float getSaturation(int x, int y) {
		return this.getSaturation(this.image, x, y);
	}
	
	public float getBrightness(int x, int y) {
		return this.getBrightness(this.image, x, y);
	}
	
	public float getHue(BufferedImage image, int x, int y) {
        int r = image.getRaster().getSample(x,y,0);
        int g = image.getRaster().getSample(x,y,1);
        int b = image.getRaster().getSample(x,y,2);

        float[] hsb = Color.RGBtoHSB(r,g,b,null);
        return hsb[0];
    }
	
	public float getBrightness(BufferedImage image, int x, int y) {
        int r = image.getRaster().getSample(x,y,0);
        int g = image.getRaster().getSample(x,y,1);
        int b = image.getRaster().getSample(x,y,2);
        float[] hsb = Color.RGBtoHSB(r,g,b,null);
        return hsb[2];
    }
	
	public float getSaturation(BufferedImage image, int x, int y) {
        int r = image.getRaster().getSample(x,y,0);
        int g = image.getRaster().getSample(x,y,1);
        int b = image.getRaster().getSample(x,y,2);

        float[] hsb = Color.RGBtoHSB(r,g,b,null);
        return hsb[1];
    }
	
	public void setBrightness(BufferedImage image, int x, int y, float value) {
        image.setRGB(x,y, new Color(value,value,value).getRGB() );
    }
	
	public void normalizeBrightness(float coef) {
        Statistics stats = new Statistics(this);
        for (int x=0; x<this.getWidth(); x++) {
            for (int y=0; y<this.getHeight(); y++) {
                this.setBrightness(this.image, x, y, stats.thresholdBrightness(this.getBrightness(this.image, x, y), coef));
            }
        }
    }
	
	public BufferedImage duplicateBufferedImage(BufferedImage image) {
        BufferedImage imageCopy = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        imageCopy.setData(image.getData());
        return imageCopy;
    }

	@Override
	public void close() throws IOException {
		System.out.println("Photo Closed Successfully");		
	}

}
