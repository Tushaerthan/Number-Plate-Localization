package anpr.localization;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import anpr.filter.FilterEffect;

public class MainIndex {
	

	public static void main(String[] args) throws IOException {
		/*
		// TODO Auto-generated method stub
		for (int i = 1; i <= 13; i++) {
			String file = "";
			if (i<10) {
				file = "sample\\test_00" + i + ".jpg";
			}
			else {
				file = "sample\\test_0" + i + ".jpg";
			}
			runDetection(file);
		}
		 */
		//Testing Purposes
		runDetection("sample\\test_039.jpg");
		
	}
	
	public static void runDetection(String file) throws IOException {
		MainIndex main = new MainIndex();
		Localization local = new Localization();
		FilterEffect filter = new FilterEffect();
		
		int threshold = 90;
		int radius = 5;
		float coef = 0.8f;
		
		BufferedImage imageTest = main.readImage(file);
		//main.showImage(imageTest, "Source Image");
		
		BufferedImage imageBinary = local.threshold(imageTest, threshold);
		//main.showImage(imageBinary, "Binary Image");
		
		BufferedImage imageBright = filter.setBrightness(imageTest, coef);
		//main.showImage(imageBright, "Decrease Brigthness with coef " + coef);
		
		BufferedImage imageEnhance = filter.enhance(imageBright);
		//main.showImage(imageEnhance, "Apply contrast Enhancement");
		
		BufferedImage cleanImage = filter.gaussianBlur(imageEnhance, radius);
		//main.showImage(cleanImage, "Apply Gaussian Blur");
		
		//BufferedImage imageThresRGB = local.threshold(cleanImage, threshold);
		//main.showImage(imageThresRGB, "Applying Thresholding");
		
		BufferedImage imageGray = local.convertToGrayScale(cleanImage);
		//main.showImage(imageGray, "Gray Image");
		
		//BufferedImage imageEdge = local.fullEdgeDetector(cleanImage);
		//main.showImage(imageEdge, "Edge Detected");
		
		//BufferedImage imageEdgeThres = local.threshold(imageEdge, threshold);
		//main.showImage(imageEdgeThres, "Edge Detected with Threshold");
		
		BufferedImage imageVert = local.verticalEdgeDetector(imageGray);
		//main.showImage(imageVert, "Vertical Edge");
		
		//BufferedImage imageHorz = local.horizontalEdgeDetector(imageGray);
		//main.showImage(imageHorz, "Horizontal Edge");
		
		//BufferedImage imageVertThres = local.threshold(imageVert, threshold);
		//main.showImage(imageVertThres, "Vertical Edge with Threshold");
		
		//BufferedImage imageHorzThres = local.threshold(imageHorz, threshold);
		//main.showImage(imageHorzThres, "Horizontal Edge with Threshold");
		
		//To get vertical magnitud band, we must do verticalprojection
		//int[] plotVert = PlotGraph.verticalProjection(iamgeVertThres);
		
		
		int plateCenterYaxis = PlotGraph.horizontalBand(imageVert);
		System.out.println("This is the Center of the Plate in Y-axis : " + plateCenterYaxis);
		System.out.println("This is the height of the image : " + imageVert.getHeight());
		BufferedImage cropped = imageTest.getSubimage(0, plateCenterYaxis - 15 + radius, imageTest.getWidth(), 40 + radius);
		//main.showImage(cropped, "Cropped Plate");
		
		//offsetting image
		int xLocationCrop = imageTest.getWidth()/4;
		int widthCrop = imageTest.getWidth()-(xLocationCrop * 2);
		
		BufferedImage croppedHor = imageBinary.getSubimage(xLocationCrop, plateCenterYaxis-15 + radius, widthCrop, 40 + radius);
		//main.showImage(croppedHor, "Cropped Hor Thres");
		
		int plateLocation[] = PlotGraph.verticalBand(croppedHor);
		System.out.println(croppedHor.getWidth());
		
		System.out.println("Plate Location : " + plateLocation[0]);
		System.out.println(plateLocation[1]);
		BufferedImage plate = cropped.getSubimage(plateLocation[0] + xLocationCrop, 0, plateLocation[1], cropped.getHeight());
		main.showImage(plate, "Detected Plate for " + file);
	}
	
	public BufferedImage readImage(String url) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(url));
		} catch (IOException e) {
			
		}
		return img;
	}

	public void showImage(Image img, String label) throws IOException {
		ImageIcon icon=new ImageIcon(img);
		JFrame frame=new JFrame(label);
		frame.setLayout(new FlowLayout());
		frame.setSize(450,390);
		JLabel lbl=new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
