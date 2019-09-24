package core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * Class Graph
 * <p>
 *     A windowed graph which has the capabilities to render a line
 *     Uses awt library
 * </p>
 *
 * @author Nick
 *
 */
public class Graph extends JFrame implements Runnable{

    /**Fields:
     *
     */
    private static final long serialVersionUID = -1398735295691281522L;
    private BufferedImage plane;
    private int[] pixels;
    private Thread thread;
    private boolean running = false;


    private Canvas canvas;
    //o = origin
    private double xMin, xMax, yMin, yMax;
    private int originX, originY, scale, width, height;

    private Node currentEqu;
    private int currentEquPixel = 0;
    private boolean derivative = false;

    


    /**
     *
     *
     * @param xMin x Minimum
     * @param xMax x Maximum
     * @param yMin y Minimum
     * @param yMax y Maximum
     * @param scale number of pixels per interval
     */


    public Graph(double xMin, double xMax, double yMin, double yMax, int scale){
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.scale = scale;

        originX = (int) ((((xMax - xMin) * scale) / 2) * (Math.abs(xMax / xMin)));
        originY = (int) ((((yMax - yMin) * scale) / 2) * (Math.abs(yMax / yMin)));

        plane = new BufferedImage( (int) (xMax - xMin) * scale,(int) (yMax - yMin) * scale, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) plane.getRaster().getDataBuffer()).getData();
        for(int i = 0; i < pixels.length; i++){
            pixels[i] = Color.WHITE.getRGB();
        }

        width = plane.getWidth();
        height = plane.getHeight();
        startUI();

        start();
    }


    /** Initiates the UI, called in constructor
     *
     *
     */
    private void startUI() {

        setName("Calculator");
        setResizable(false);
        setBounds(0,0, width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        canvas = new Canvas();
        add(canvas);

//		addWindowListener(new WindowAdapter() {
//
//			@Override
//			public void windowClosing(WindowEvent arg0) {
//				 int i = JOptionPane.showConfirmDialog(null, "Would you like to save the image?");
//			    if (i == JOptionPane.YES_OPTION) {
//			    	String path = JOptionPane.showInputDialog("Please enter a path to save the image:");
//			    	saveImage(path);
//			        System.exit(0);
//			    } else{
//			       System.exit(0);
//
//			    }
//			}
//		});




    }
    /**Saves the image to the path
     *
     * @param path Path at which the image is saved.
     */

    private void saveImage(String path) {
        try {
            ImageIO.write(plane, "png", new File(path + "/graph.png"));

        } catch (IOException e) {

        }

    }


    /**Sets the color of a pixel at point x,y to color.
     *
     * @param x pixel x
     * @param y pixel y
     * @param color rgb color
     */
    public void setPixel(int x, int y, int color){
        int index = y * width + x;
        if(index >= pixels.length || index < 0)
            return;

        pixels[index] = color;
    }


    /**Sets the color of a pixel at index to color.
     *
     * @param index pixels index in the array
     * @param color rgb color
     */
    public void setPixel(int index, int color){
        if(index >= pixels.length)
            return;

        pixels[index] = color;
    }



    /**Draws the x and y axes based off of originX and originY defined in constructor
     *
     */
    public void drawAxes()
    {
        Color color = new Color(5, 57, 122);
        int c = color.getRGB();
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++){
                if(x == originX) setPixel(x, y, c);
                if(y == originY) setPixel(x, y, c);
            }

    }

    public void drawEquation(Node equ){
        currentEqu = equ;
        derivative = false;

    }
    public void drawDerivative(Equation equ){
        currentEqu = equ;
        derivative = true;
    }


    public synchronized void start(){
        if(running) return;
        running = true;


        thread = new Thread(this);

        thread.start();
    }

    public synchronized void stop(){
        if(!running) return;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        double timePerTick = 1000000000 / 600;
        double delta = 0;
        long lastTime = System.nanoTime();
        long now;

        while(running){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;

            if(delta >=1){
                tick();
                delta = 0;
            }
        }
    }


    public void tick(){
        BufferStrategy bs = canvas.getBufferStrategy();
        if(bs == null){
            canvas.createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        renderEquation();

        g.drawImage(plane, 0, 0, null);
        g.dispose();
        bs.show();
    }

    private void renderEquation(){
        if(currentEqu != null && currentEquPixel < width){
            double x = getXValue(currentEquPixel);
            double y = currentEqu.calculate(x);
            int yIndex = getYPixelIndex(y);
            int yDervIndex = getYPixelIndex(currentEqu.slopeAt(x));
            setPixel(currentEquPixel,yIndex, Color.BLACK.getRGB());
            if(derivative)
                setPixel(currentEquPixel, yDervIndex, Color.BLACK.getRGB());
            currentEquPixel++;

        }
        if(currentEquPixel >= width) currentEqu = null;
    }

    private double getXValue(double xPixel){
        double val = (xPixel + originX) / scale;
        return val - (xMax - xMin);
    }

    private int getYPixelIndex(double y) {
        return (int) (-1* (y * scale - originY));
    }
}