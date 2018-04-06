/*
 *
 * 30 May 2016
 * DeRICA v 0.3
 *
 * decompresses image, code cleaned up
 * unused variables stripped
 * some validation with try catch
 * made imagesize variable in both x and y
 *
 *
 */

        FILE *fp;
        bool debug = false;
                char *imbuff;
                int xsize=512;
                int ysize=512;



               void rect(int x, int y, int xscale, int yscale, char colour)
                                                {
                        int i,j;
                                                for(i=0;i<xscale;i++)
                                                         for(j=0;j<xscale;j++)
                                *(imbuff+i+x+(y+j)*512)=colour;
                        }


                void decode(int x, int y, int xscale, int yscale)
                        {
                /*
                 *  The recursive method, takes:
                 *      i,j the offset of the block from the origin
                 *      xscale, yscale the size of the block
                 *
                 */
                char colour;
                                int i,j;
                                i=x;
                                j=y;
                                fread(&colour,1,1,fp);






            if(debug)
                {
                printf("Scale=%d,%d",xscale,yscale);
                printf("x=%d,y=%d,colour=%d",x,y,(int)colour);
                }


                if(xscale>1&&yscale>1){
                        rect(i/2,j/2,xscale/2,yscale/2,colour);
                        xscale=xscale/2;
                        yscale=yscale/2;
                        if(debug)
                                printf("x=%d,y=%d,colour=%d",xscale,yscale,(int)colour);
                        fread(&colour,1,1,fp);
                                                if(colour!=0)  // check if next byte is 0, i.e. Base case
                                {
                                fseek(fp,-1,SEEK_CUR);                                   //              Rewind file for next read
                                decode(i,j,xscale,yscale);                              //
                                decode(i+xscale,j,xscale,yscale);               //              Recurse into the four quadrants
                                decode(i,j+yscale,xscale,yscale);                       //
                                decode(i+xscale,j+yscale,xscale,yscale);        //
                                }
                        }
                } // End of method

                void outputimage(int xsize, int ysize)
                {
                        int i,j;
                        for(i=0;i<xsize;i++)
                                for(j=0;j<ysize;j++)
                                {
                                        printf("%c",*(imbuff+i+j*xsize));
                                }
                }








                int main(int argc, char *argv[]){
                int win_width=  512;            //      set window width
                int win_height = 512;           //      set window height
                                int xsize, ysize;
                                xsize=ysize=512;
                if (argc<1)
                        {
                        printf("Usage: decomp input.ric");
                        exit(0);
                        }
             if((fp=fopen(argv[1],"rb"))==NULL)           //  get the filename to open
                        {
                        printf("Cannot open %s\n",argv[1]);
                        exit(0);
                        }
                imbuff=(char *)malloc(xsize*ysize);
                                if(imbuff==NULL)
                        {
                        printf("cannot allocate image memory");
                        exit(0);
                        }
                decode(0,0,win_width*2,win_height*2);
                                outputimage(xsize,ysize);
         //       sleep(10000);    //      wait for a few seconds
                fclose(fp);                      //      close the file
                }





