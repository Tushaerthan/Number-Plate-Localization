package anpr.localization;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class PlotGraph {
	
//	public static void plotGraph(int[] array) {
//		XYSeries series = new XYSeries("Array");
//        for (int i = 0; i < array.length; i++) {
//            series.add(i, array[i]);
//        }
//        XYSeriesCollection dataset = new XYSeriesCollection(series);
//        JFreeChart chart = ChartFactory.createXYLineChart("Array Plot", "X", "Y", dataset);
//        ChartPanel panel = new ChartPanel(chart);
//
//        JFrame frame = new JFrame("Array Plot");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(panel);
//        frame.pack();
//        frame.setVisible(true);
//	}
	
	public static int findPeakHorz(int[] array) {
		int peak = 0;
		int peakLoc = 0;
		
		for (int i = 1; i < array.length; i++) {
			//if (array[i] > array[i-1] && array[i] > array[i+1] && array[i] > peak) {
			if (array[i] > peak) {
			    peak = array[i];
			    peakLoc = i;
			}
		}
		return peakLoc;
	}
	
	public static int[] findPeakVert(int[] array) {
		int width = 0;
		int peakLoc = 0;
		int tempPeakLoc = 0;
		int tempPeakWidth = 0;
		
		int data[] = new int[2];
		
		for (int i = 1; i < array.length; i++) {
			if (array[i] > 0 && width == 0) {
			    peakLoc = i;
			    width += 1;
			}
			else if (array[i] > 0 && width > 0 ) {
				width += 1;
			}
			else if (array[i] == 0) {
				if (width > tempPeakWidth) {
					tempPeakWidth = width;
					tempPeakLoc = peakLoc;
				}
				width = 0;
			}
		}
		
		data[0]=tempPeakLoc;
		data[1]=tempPeakWidth;
		
		return data;
	}
	
	public static int horizontalBand(BufferedImage target) {
		
		
		//int halfOffset = target.getHeight()/2;
		//int[] ydata = new int[halfOffset];
		int[] ydata = new int[target.getHeight()];
		
		for (int y = 120; y < ydata.length - 30; y++) {
			for (int x = 0; x < target.getWidth(); x++) {
				//int pixel = target.getRGB(x, y+halfOffset-1);
				int pixel = target.getRGB(x, y);
				int gray = pixel & 0xff;
		        int binary = (gray > 128) ? 1 : 0;
		        
		        System.out.print(binary + ",");
		        ydata[y] += binary;
			}
			System.out.println();
		}
		
		int yCenter = findPeakHorz(ydata);
		//System.out.println(halfOffset);
		//System.out.println("Highest Edges in Y-axis at: " + (yCenter+halfOffset));
		System.out.println("Highest Edges in Y-axis at: " + yCenter);
		System.out.println(Arrays.toString(ydata));
		
		//return yCenter +halfOffset;
		return yCenter;
		
	}
	
	public static int[] verticalBand(BufferedImage target) {

		int[] xdata = new int[target.getWidth()];
		
		for (int x = 0; x < xdata.length; x++) {
			if (x == target.getWidth()-1) {
				break;
			}
			for (int y = 0; y < target.getHeight(); y++) {
				int pixel = target.getRGB(x, y);
				int gray = pixel & 0xff;
		        int binary = (gray > 128) ? 1 : 0;

		        //int newPixel = (binary << 16) | (binary << 8) | binary;
		        System.out.print(binary + ",");
		        xdata[x] += binary;
			}
			System.out.println();
		}
		
		int[] PlateData = findPeakVert(xdata);
		System.out.println("Highest Width in x-axis at: " + (PlateData[1]));
		System.out.println(Arrays.toString(xdata));
		
		return PlateData;
		
	}
	
	public static int[] verticalProjection(BufferedImage target) {
		int[] verticalProjection = new int[target.getWidth()];
        for (int x = 0; x < target.getWidth(); x++) {
            for (int y = 0; y < target.getHeight(); y++) {
                int pixel = target.getRGB(x, y);
                verticalProjection[x] += Math.abs((pixel >> 16) & 0xff); // add red channel value
                verticalProjection[x] += Math.abs((pixel >> 8) & 0xff); // add green channel value
                verticalProjection[x] += Math.abs(pixel & 0xff); // add blue channel value
            }
        }
        return verticalProjection;
	}
	
	
	
	public static int[] horizontalProjection(BufferedImage target) {
		int[] horizontalProjection = new int[target.getHeight()];
        for (int y = 0; y < target.getHeight(); y++) {
            for (int x = 0; x < target.getWidth(); x++) {
                int pixel = target.getRGB(x, y);
                horizontalProjection[y] += Math.abs((pixel >> 16) & 0xff); // add red channel value
                horizontalProjection[y] += Math.abs((pixel >> 8) & 0xff); // add green channel value
                horizontalProjection[y] += Math.abs(pixel & 0xff); // add blue channel value
            }
        }
        return horizontalProjection;
	}
}
