	
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.nio.file.Paths;
	import org.jsfml.window.*;
	import org.jsfml.system.Vector2i;
	import org.jsfml.system.Vector2f;
	import org.jsfml.graphics.*;
	import org.jsfml.system.*;
	
	
	public class Compressor {
		static PrintWriter myPw;
		public static int total=1;
		public static void main(String[] args) throws IOException, TextureCreationException, InterruptedException 	{
		int win_width, win_height;
		int threshold=1;
		int result,	cutoff=10;  //  Default Threshold for image variance
		if(args.length==3)
		cutoff=Integer.parseInt(args[2]);
		Image myImage = new Image();
		Image compressedImage = new Image();
		Sprite mySprite = new Sprite();
		Texture myTexture=new Texture();
		
	//	for(threshold=1;threshold<100;threshold++)
	//	{
		myPw = new PrintWriter("c:\\temp\\output.txt", "UTF-8");
		myImage.loadFromFile(Paths.get(args[0]));
		
		
		Vector2i dimensions=new Vector2i(100,100);
		
		myTexture.loadFromImage(myImage);
		win_width=myTexture.getSize().x;
		win_height = myTexture.getSize().y;
		RenderWindow target= new RenderWindow(new VideoMode(win_width,win_height),"Target");
		RenderWindow compressed = new RenderWindow(new VideoMode(win_width,win_height),"Output");
		target.setVerticalSyncEnabled(true);
		compressed.setVerticalSyncEnabled(true);
		compressed.setPosition(new Vector2i(50,100));
		mySprite.setTexture(myTexture);					
		target.clear();
		target.draw(mySprite);
		target.display();
	
		myPw.println("100");  // need to have the avg for the whole image here
		split(0,0,win_height,myImage,target,compressed,threshold);
		myPw.close();
		
		compressedImage=compressed.capture();  // move renderwindowcontents to an image
		System.out.print("Threshold,"+threshold+",");
//		System.out.println("#blocks="+total+" Compression ratio equals "+(float)512*512/total);   //  not /3 because using rgb!!!!
		System.out.print("Compression ratio,"+(float)512*512/total);   //  not /3 because using rgb!!!!
		compressed.display();
		System.out.println(",MSQE,"+msqe(myImage,compressedImage));
		total=0;
		Thread.sleep(30000);
		}
		
		
	
		
	//	}
	
	
	
	
	static 	void split(int x,int y, int s, Image myImage, RenderWindow target,RenderWindow compressed, int threshold)throws InterruptedException
		{
		if(s>1)  // test if scale is > 2 pixels
			{
			s=s/2;  
			if(shouldsplit(x,y,s,myImage,target,compressed,threshold)==1) // Quadrant 1
			split(x,y,s,myImage,target,compressed,threshold);
			if(shouldsplit(x+s,y,s,myImage,target,compressed,threshold)==1) //Quadrant 2
			split(x+s,y,s,myImage,target,compressed,threshold);
			if(shouldsplit(x,y+s,s,myImage,target,compressed,threshold)==1) //Quadrant 3
			split(x,y+s,s,myImage,target,compressed,threshold);
			if(shouldsplit(x+s,y+s,s,myImage,target,compressed,threshold)==1) //Quadrant 4
			split(x+s,y+s,s,myImage,target,compressed,threshold);
			}
	/*	else
			{
			myPw.printf("0\n0\n0\n0\n");
			}     Might not need if we can work it out from the scale on decompression, or just use one 0 here ??? */
		}
	
	static int shouldsplit(int x,int y,float s, Image myImage, RenderWindow target,RenderWindow compressed, int threshold) throws InterruptedException
		{
			char cc;
			int a,b;	
			float error=0;
			int cutoff=60;
			long totr,totg,totb;  // total colour values in area
			Color c,c1,c2,c3,c4,inc;  
			c1=Color.BLACK;
			c2=Color.BLACK;
			c3=Color.BLACK;
			// some colours
			int min,max;
	
			max=0;
			min=255;
			totr=totg=totb=0;
			for(a=x;a<x+s;a++)
			for(b=y;b<y+s;b++)
				{
				c1=(myImage.getPixel(a,b));
				totr+=c1.r;    // Gets the avg colour of area
				totg+=c1.g;
				totb+=c1.b;
				}
			float avgred=totr/(s*s);
			float redvalue=0;
			for(a=x;a<x+s;a++)
				for(b=y;b<y+s;b++)
					{
					c1=(myImage.getPixel(a,b) );
					redvalue=c1.r;    // Gets the avg colour of area
					error+=(Math.abs(redvalue-avgred));   //  need to use the average from the superblock here!!!
					}
			RectangleShape outrect= new RectangleShape(new Vector2f(s,s));	
			//System.out.println((int)(totr/(s*s)));
			if(avgred==0)
				avgred=1;  //  need this for the base case
			myPw.println((int)avgred);   // write the color to the file
		//	Thread.sleep(5000);
			outrect.setFillColor(new Color( (int)(totr/(s*s)),(int)(totg/(s*s)),(int)(totb/(s*s)) ));
			outrect.setPosition(x,y);
			compressed.draw(outrect);
		//	compressed.display();
	

		//	myPw.println((int)avgred);   // write the color to the file
			
			
			error=error/(s*s);
			
			cutoff= (int) ((16-s));  //  want cutoff to be low for large regions
		//	cutoff=20;   //  threshold;
			if((error)>cutoff)
			{
		//		myPw.println((int)avgred);   // write the color to the file
		//		outrect.setFillColor(new Color( (int)(totr/(s*s)),(int)(totg/(s*s)),(int)(totb/(s*s)) ));
			//	outrect.setPosition(x,y);
				//compressed.draw(outrect);		
				return(1);  // The variance in pixels > cutoff
			}
			else  // fill in the area with avg colour
				{	
			/*	RectangleShape outrect= new RectangleShape(new Vector2f(s,s));	
				System.out.println((int)(totr/(s*s)));
				myPw.println((int)avgred);   // write the color to the file
			//	Thread.sleep(5000);
				outrect.setFillColor(new Color( (int)(totr/(s*s)),(int)(totg/(s*s)),(int)(totb/(s*s)) ));
				outrect.setPosition(x,y);
				compressed.draw(outrect);
				compressed.display();
		*/
				myPw.println("0");
		//		System.out.println("0");
				myPw.println("0");
		//		System.out.println("0");
				myPw.println("0");
		//		System.out.println("0");
				myPw.println("0");
		//		System.out.println("0");
				total=total+1;
				return (0);
				}
			}
	static 	double msqe(Image original,Image compressed)
		{
		int xsize=original.getSize().x;
		int ysize=original.getSize().y;
		double msqe=0;
		long total=0;
		double diff=0;
	//	System.out.println("xy="+xsize+" "+ysize);
		//System.out.println("Calculating MSQE");
		for (int i=0;i<xsize;i++)
			for (int j=0;j<ysize;j++)
			{
				diff=original.getPixel(i, j).r-compressed.getPixel(i, j).r;
				diff=diff*diff;
				total+=diff;
			}
			msqe=(double)total;
			msqe=msqe/(xsize*ysize);
			msqe=Math.sqrt(msqe);
		return msqe;	
		}
				
		
	}
	
	
	
