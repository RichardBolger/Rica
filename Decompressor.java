/*
 * 
 * 12 May 2016
 * DeRICA v 0.3
 * 
 * decompresses image correctly, at double size
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


import java.nio.file.Paths;

import org.jsfml.window.*;
import org.jsfml.system.Vector2i;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.*;
import org.jsfml.system.*;


public class Decompressor {
static PrintWriter myPw;
static PrintWriter out;
static BufferedReader in;

static int x,y=512;
static int s=512;
static RenderWindow target;
static Image myImage;
static RectangleShape outrect;

	public static void main(String[] args) throws IOException, InterruptedException 
		{
		
		
		Image myImage = new Image();
		Image compressedImage = new Image();
		Sprite mySprite = new Sprite();
		Texture myTexture=new Texture();
		
		
		Vector2i dimensions=new Vector2i(100,100);
		outrect= new RectangleShape(new Vector2f(100,100));	
		
		int win_width, win_height;
//		myTexture.loadFromImage(myImage);
		win_width=512;
		win_height = 512;
		target = new RenderWindow(new VideoMode(win_width,win_height),"Output");
	//	target.setVerticalSyncEnabled(true);
		mySprite.setTexture(myTexture);					
		target.clear();
//		target.draw(mySprite);
		
	// Display the sprite on the screen
	//	target.display();
		
		
		
		
			
		
		in= new BufferedReader(new FileReader("c:\\temp\\output.txt"));
		out = new PrintWriter("c:\\temp\\decompoutput.txt", "UTF-8");
		
		
		String line;
		int col;
	//	line=in.readLine();
	//	System.out.println(line+"is first");
	//	col=Integer.parseInt(line);
	//	outrect.setSize(new Vector2f(512,512));
	//	outrect.setFillColor(new Color( col,col,col));
	//	outrect.setFillColor(new Color( col,col,col));
	//	target.draw(outrect);
	//	target.display();
	//	Thread.sleep(5000);
		
		decomp(0,0,1024);
		target.display();
		Thread.sleep(2000);
	//	target.display();
		
		
/*		while((line=in.readLine())!=null)
			{
			System.out.println(line);// <-- read whole line
			col=Integer.parseInt(line);
			outrect.setFillColor(new Color( col,col,col));
			target.draw(outrect);
			target.display();
			Thread.sleep(50);
				
			}
*/
		Thread.sleep(50000);
		in.close();
		out.close();
		
		
		
		
		
		
		}
	
	static void decomp(int i, int j, int scale) throws NumberFormatException, IOException, InterruptedException
		{
		if(in.ready()){
		int colour=Integer.parseInt(in.readLine());
		
				{
		System.out.println("Scale="+scale);
		System.out.println("x="+i+" y="+j+"Colour="+colour);
		out.println("x="+i+" y="+j+"Colour="+colour);
		
		RectangleShape rect=new RectangleShape();
		if(colour>0&&scale>1){
			rect.setSize(new Vector2f(scale/2,scale/2));
			rect.setPosition(new Vector2f(i/2,j/2));
			rect.setFillColor(new Color(colour,colour,colour));;
			
			target.draw(rect);
		//	Thread.sleep(100);
		//	target.display();
		//	Thread.sleep(5);
			scale=scale/2;
			System.out.println(scale+" "+colour);
			decomp(i,j,scale);
			decomp(i+scale,j,scale);
			decomp(i,j+scale,scale);
			decomp(i+scale,j+scale,scale);
			//Thread.sleep(1000);
			}
			
			else
			{
				// base case reached
				System.out.println("BC");
					return;
			}
			}
		}
		}
		
	}

