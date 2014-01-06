/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.*;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.javaxyq.config.MapConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.resources.DefaultTileMapProvider;
import com.javaxyq.widget.Player;



/**
 * @author ����ΰ
 * @history 2008-5-22 ����ΰ �½�
 *          2013-12-25 wpaul modify
 */
public class TileMap extends AbstractWidget {

    /** ��ͼ�����ؿ�� */
    public static final int MAP_BLOCK_WIDTH = 320;

    /** ��ͼ�����ظ߶� */
    public static final int MAP_BLOCK_HEIGHT = 240;
    
    public static final int STEP_DISTANCE = 20;

    private DefaultTileMapProvider provider;

    private SoftReference<Image>[][] blockTable;

    /** ��ͼX������� */
    private int xBlockCount;

    /** ��ͼY������� */
    private int yBlockCount;
    
    public int offsetX;
    public int offsetY;
    public int firstTileX;
    public int lastTileX;
    public int firstTileY;
    public int lastTileY;

    private int width;

    private int height;

    private MapConfig config;

    private int lastCount;
    
    //��ɫ��Ϣ������    
    private Player player;
    private int pOffsetX;
    private int pOffsetY;
    private int pwidth;
    private int pheight;
    private int pcoordx;
    private int pcoordy;
    private boolean isdm;
    
 //�ӵ�ͼCell����
    public byte [][] totalcell;
    
 // �ӵ�ͼ��maskͼ ����
    private ArrayList<?> tiles;
    public ArrayList<int[]> maskkey;
    public ArrayList<int[]> mask;
    public ArrayList<int[]> mask_data;
    private int [] masklist;
    private int[][] m_anteroposterior;
    //private BufferedImage[] tileImage;
    //private Raster[] rasterSrc;
    //private BufferedImage totalImage;
    //private WritableRaster totalRaster;
    private BufferedImage[] tileImageDes;
    private WritableRaster [] rasterDes;
    private ColorModel cm;
    private Object data;
    private int[] des;

    /**
     * ����TileMap
     * �����ӵ�ͼtileImage
     * ����MASK����ͼtileImageDes
     */
    public TileMap(DefaultTileMapProvider provider, MapConfig cfg) {
    	
        
    	
        //ˮƽ����w�飬��ֱ����h��
        this.config = cfg;
        this.xBlockCount = provider.getXBlockCount();
        this.yBlockCount = provider.getYBlockCount();
        this.width = provider.getWidth();
        this.height = provider.getHeight();
        blockTable = new SoftReference[this.xBlockCount][this.yBlockCount];
        this.provider = provider;
        
        //totalcell��ʼ��
        int wbol,hbol;
        if(width%320==0){
			wbol=0;
		}else{
			wbol=1;
		}
        if(height%240==0){
			hbol=0;
		}else{
			hbol=1;
		}
        int swidth  = (width / 320 + wbol) * 320/STEP_DISTANCE;
        int sheight = (height / 240 + hbol) * 240/STEP_DISTANCE;
        totalcell = new byte[sheight][swidth];
        
        //���ӵ�ͼ������ͼ���ݶ�ȡ�����
        //tileImage = new BufferedImage[xBlockCount*yBlockCount];
        //rasterSrc = new Raster[xBlockCount*yBlockCount];

        //mask���أ�
        tileImageDes = new BufferedImage[provider.offsetlsize];
        rasterDes = new WritableRaster[provider.offsetlsize];
        tiles = new ArrayList<Object>();
        //cell= new boolean[(int) rMap.m_SubMapRowNum][(int) rMap.m_SubMapColNum][12][16];
        maskkey= new ArrayList<int[]>();
        mask = new ArrayList<int[]>();
        mask_data = new ArrayList<int[]>();
        m_anteroposterior = new int[provider.getWidth()][provider.getHeight()];
       // mapwidth=width;
        //mapheight=height;
        
        //ȷ����������
        int b=yBlockCount;
        int a=xBlockCount;
        //System.out.println("a,b is:"+a+","+b);
        
        
        //��ȡ�ӵ�ͼ
        for(int y=0;y<yBlockCount;y++){
          for(int x=0;x<xBlockCount;x++){
        	provider.ReadUnit(x,y);
        	
        	//���õ�ͼ����
        	//setCellFlag(i/b,(i+a)%a);
   
        	//��ȡmaskƫ��������
        	masklist = new int[provider.m_MaskNum];
        	for(int m=0;m<provider.m_MaskNum;m++){
        		masklist[m]=provider.masklist[m];
        	}
        	
        	//System.out.println("mask" +(y*xBlockCount+x)+" is:"+masklist.length);
        	mask.add(y*xBlockCount+x, masklist);
        	

        	
        	//��ȡIMAGE����
        	/*InputStream buffin = new ByteArrayInputStream(provider.u_jpeg);
        	try {
        		tileImage[y*xBlockCount+x]= ImageIO.read(buffin);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
        	rasterSrc[y*xBlockCount+x] = tileImage[y*xBlockCount+x].getRaster();
        	*/
        	//setMaskFlag(i);
        	
        	//�ӵ�ͼ�ϳ�cell
        	for(int ch=0;ch<12;ch++){
               for(int cw=0;cw<16;cw++){
            	//System.out.println(provider.m_cell[c]);
            	//totalcell[((12*y+ch)*16*xBlockCount)+(16*x+cw)] = provider.m_cell[ch*16+cw];
            	  totalcell[y*12+ch][x*16+cw]=provider.m_cell[ch*16+cw];
                }
        	}
            
        	//tiles.add(tileImage);
          }
        }
        
        /*for(int ch=0;ch<sheight;ch++){
        	for(int cw=0;cw<swidth;cw++){
        		System.out.print(totalcell[ch][cw]);
        	}
        	System.out.println();
        }*/
        
        //cfg.getPath().replace(".map", ".msk")
        /*File fo = new File("scene/1234.msk");   
        try {
			FileOutputStream fos =  new FileOutputStream(fo);
			fos.write(totalcell);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        //System.out.println("scenemask is:"+cfg.getPath().replace(".map", ".msk"));
        /*for(int i=0;i<rMap.m_SubMaTotal;i++){
        	int [] maskdata=(int[]) mask.get(i);
        	for(int j=0;j<maskdata.length;j++){
        		System.out.println("mask j"+j+" is:"+maskdata[j]);
        	}
        }*/
       
      //��ȡmask�ؼ���X,Y
        for(int j=0;j<provider.offsetlsize;j++){
    	    provider.ReadMask(j);
       	    int [] key =new int[4];
       	    key[0] = provider.maskkeyx;
       	    key[1] = provider.maskkeyy;
       	    key[2] = provider.maskwidth;
       	    key[3] = provider.maskheight;
       	    //System.out.println("maskdata length is:"+provider.maskData.length);
       	    maskkey.add(j, key);
       	    mask_data.add(j,provider.maskData);
       	    //maskimage��ʼ��
       	    tileImageDes[j] = new BufferedImage(provider.maskwidth,
       			provider.maskheight,BufferedImage.TYPE_4BYTE_ABGR);
     	    rasterDes[j] = tileImageDes[j].getRaster();
       	    
         }
	
         //setArgtoMask(offsetX,offsetY);
       
    
        
        

       // sprites = new ArrayList();
        //tMap = loadTileMap();
        
    }
    
    public void setPlayerArg(Player player,int pcoordx,int pcoordy){
    	//��ȡplayer��Ϣ��������
    	this.pcoordx = pcoordx;
    	this.pcoordy = pcoordy;
        this.player  = player;
        int pkeyx   = player.person.getRefPixelX();
        pOffsetX = pcoordx- pkeyx;
        int pkeyy   = player.person.getRefPixelY();
        pOffsetY = pcoordy- pkeyy;
        this.pwidth  = player.person.getWidth();
        this.pheight = player.person.getHeight();
        this.isdm = true;
    }
    
    
    /**
     * ����totalRaster��KEYX,KEYY������
     * ��rasterDes�� ���ж��Ƿ�drawmask
     */
  //maskͼ�����ǩ
    public void setArgtoMask(int offsetX,int offsetY){
    	//��ʼ��MASK����ͼ�����
       int firstTilex = pixelsToTilesw(offsetX);
       int lastTilex = pixelsToTilesw(offsetX + MAP_BLOCK_WIDTH*2);
       int firstTiley = pixelsToTilesh(offsetY);
       int lastTiley = pixelsToTilesh(offsetY + MAP_BLOCK_HEIGHT*2);
        
       lastTilex=Math.min(lastTilex, (int)(provider.getXBlockCount()-1));
       lastTiley=Math.min(lastTiley, (int)provider.getYBlockCount()-1);
        //System.out.println("fistX,Y is:"+firstTilex+","+firstTiley);
        //System.out.println("lastX,Y is:"+lastTilex+","+lastTiley);
       int unitnum1 = firstTiley*getXBlockCount()+firstTilex-1;
	   int unitnum2 = lastTiley*getXBlockCount()+lastTilex+1;
       //System.out.println("setargmask unitnum 1,2 is:"+unitnum1+","+unitnum2);
        //��ȡ��ͼmask��Ϣ;
        
        for (int y = firstTiley; y <=lastTiley; y++) {
         	
            for (int x = firstTilex; x <= lastTilex; x++) {
            	
            	
            		int unitnum = (y*xBlockCount+x);

            	//System.out.println("unitnum"+ "is:"+unitnum);
            	int []masknum=(int[]) mask.get(unitnum);
            	
            	for(int i=0;i<masknum.length;i++){
            		//if(tileImageDes[masknum[i]] == null){
            			
            					
            			//provider.ReadMask(masknum[i]);
                		int [] maskinfo =(int []) maskkey.get(masknum[i]);
                    	
                    	//��ȡMASK����
                    	/*tileImageDes[masknum[i]] = new BufferedImage(maskinfo[2],
                    			maskinfo[3],BufferedImage.TYPE_4BYTE_ABGR);
                    	rasterDes[masknum[i]] = tileImageDes[masknum[i]].getRaster();*/
                    	//writerRaster(masknum[i],rMap.maskkeyx,rMap.maskkeyx,rMap.maskwidth,rMap.maskheight,rMap.maskData);
                    	setTileMaskImg(masknum[i],maskinfo[0],maskinfo[1],
                    			maskinfo[2],maskinfo[3],(int [])mask_data.get(masknum[i]));
                    	//System.out.println("MASK keyx,keyy is:"+rMap.maskkeyx+","+
                    		//	rMap.maskkeyy);
                    	
                    	//���ͼ��
//                    	String name ="mask/"+masknum[i]+".png";
//                    	try {
//                    		File jpegdata=new File(name);
//                    		jpegdata.getParentFile().mkdirs();
//                    		FileOutputStream fout =new FileOutputStream(jpegdata);
//                			ImageIO.write(tileImageDes[masknum[i]], "png", fout);
//                			fout.close();
//                		} catch (IOException e) {
//                			e.printStackTrace();
//                		}
            		//}

            		//
            		
            		
            	}
            }	 
           	 
        }
        
    }
    
    public void setTileMaskImg(int unitnum,int keyx,int keyy,int width,int height,int []mask){
    	int firstTilex = pixelsToTilesw(keyx);
    	int lastTilex = pixelsToTilesw(keyx + width - 1);
        int firstTiley = pixelsToTilesh(keyy);
        int lastTiley = pixelsToTilesh(keyy + height - 1);
       // lastTileX=Math.min(lastTileX, (int)(rMap.m_SubMapRowNum-1));
       // lastTileY=Math.min(lastTileY, (int)rMap.m_SubMapColNum);
        //System.out.println("fistX,Y is:"+firstTilex+","+firstTiley);
        //System.out.println("lastX,Y is:"+lastTilex+","+lastTiley);
        
      //�ϲ��ӵ�ͼ���������ֵ��
        BufferedImage tempMaskImage = new BufferedImage((lastTilex+1-firstTilex)*320,
        	    (lastTiley+1-firstTiley)*240,BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster tempMaskRaster = tempMaskImage.getRaster();
        for (int y = firstTiley; y < lastTiley+1; y++) {
         	
            for (int x = firstTilex; x < lastTilex+1; x++) {
            	
            	//��ȡIMAGE����
            	InputStream buffin = new ByteArrayInputStream(provider.getJpegData(x, y));
            	try {
            		BufferedImage Image= ImageIO.read(buffin);
            		Raster raster = Image.getRaster();
            		
            		//�ϲ�raster���ؾ���
                	//System.out.println("x,y is:"+320*(x-firstTilex)+","+240*(y-firstTiley));
                	//System.out.println("num is:"+(y*xBlockCount+x));
                	tempMaskRaster.setRect(320*(x-firstTilex), 240*(y-firstTiley), 
                			raster);
                	
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            
            }
        }
        writerRaster(tempMaskImage,tempMaskRaster,unitnum,keyx,keyy,width,height,mask);
      //���ͼ��
//    	String name ="mask/"+unitnum+".png";
//    	try {
//    		File jpegdata=new File(name);
//    		jpegdata.getParentFile().mkdirs();
//    		FileOutputStream fout =new FileOutputStream(jpegdata);
//			ImageIO.write(tileImageDes[unitnum], "png", fout);
//			fout.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
    }
    //maskͼ�����ǩ
    public void writerRaster(BufferedImage tempMaskImage,WritableRaster tempMaskRaster,
    		int num,int keyx,int keyy,int width,int height,int []mask){
    	cm = tempMaskImage.getColorModel();
    	
       // data = null;
        //int m=0;
        des = new int [4];
       //System.out.println("keyx,keyy is:"+keyx+","+keyy);
       //System.out.println("width,height is:"+width+","+height);
    	for(int h=0;h<height;h++){
    		for(int w=0;w<width;w++){
    			//System.out.println("rgb is:");
    			//data = totalRaster.getDataElements(keyx+w, keyy+h, null);
    			  //int rgb=cm.getRGB(data);
    			//System.out.println("x,y is:"+(keyx+w)+","+(keyy+h));
    			  data = tempMaskRaster.getDataElements(keyx%320+w,keyy%240+h, null);
    			  int rgb = cm.getRGB(data);
    			  int sr,sg,sb;
    			  sr = (rgb & 0xFF0000)>>16;
    	        sg = (rgb & 0xFF00)>>8;
    	        sb = rgb & 0xFF;
    	        des[0]=sr;
    	        des[1]=sg;
    	        des[2]=sb;
    	        if(mask[h*width+w]==3){
    	        	des[3]=110;
    	        }else if(mask[h*width+w] == 1){
    	        	des[0]=0;
    	        	des[1]=0;
    	        	des[2]=0;
    	        	des[3]=0;
    	        }
    	        else{
    	        	des[3]=0;
    	        }
    	        
    	        rasterDes[num].setPixel(w, h, des);
    		}
    	}
   	 
   }
   

    @Override
    protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
        // 1.����Rect���ڵ�ͼ�� 
        Point pFirstBlock = viewToBlock(x, y);
        // 2.�����һ���ͼ���ViewRect��ƫ����,����Graphicsƫ��
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        //System.out.println("x,y is:"+dx+","+dy);
        //System.out.println("x,y is:"+pFirstBlock.x+","+pFirstBlock.y);
        g2.translate(dx, dy);
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.����X��,Y�᷽����Ҫ�ĵ�ͼ������
         offsetX = x;
         offsetY = y;
         firstTileX = pixelsToTilesw(offsetX);
         lastTileX = pixelsToTilesw(offsetX + MAP_BLOCK_WIDTH*2);
         firstTileY = pixelsToTilesh(offsetY);
         lastTileY = pixelsToTilesh(offsetY + MAP_BLOCK_HEIGHT*3);
         
         lastTileX=Math.min(lastTileX, (int)(provider.getXBlockCount()-1));
         lastTileY=Math.min(lastTileY, (int)provider.getYBlockCount());
         
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.�ӻ����ȡ��ͼ��,����Graphics��
        for (int i = firstTileX; i <= lastTileX; i++) {
            for (int j = firstTileY; j < lastTileY; j++) {
            	//System.out.println("i , j is:"+i+","+j);
                Image b = getBlock(i , j );
            	  //Image b = tileImage[j*xBlockCount+i];
                //System.out.println("b width is:"+b.getWidth(null));
                g2.drawImage(b, (i-firstTileX)*MAP_BLOCK_WIDTH, 
                		(j-firstTileY)*MAP_BLOCK_HEIGHT, null);
            }
        }
    }
    
    /**
     * ����MASKǰ�����ݹ�ϵ
     * �ж��Ƿ�drawmask
     */
    public void drawMask(Graphics g,int viewx, int viewy, int offsetX, int offsetY) {
        // 1.����Rect���ڵ�ͼ�� 
       /* Point pFirstBlock = viewToBlock(x, y);
        // 2.�����һ���ͼ���ViewRect��ƫ����,����Graphicsƫ��
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;*/
        //System.out.println("x,y is:"+pFirstBlock.x+","+pFirstBlock.y);
        //g2.translate(dx, dy);
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.����X��,Y�᷽����Ҫ�ĵ�ͼ������
        // doDraw�Ѽ���
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.�ӻ����ȡ��ͼ��,����Graphics��
      
                //System.out.println("unitnum is:"+unitnum);
                

    	 int offsetx = viewx;
         int offsety = viewy;
         int firstTileX = pixelsToTilesw(offsetx);
         int lastTileX = pixelsToTilesw(offsetx + MAP_BLOCK_WIDTH*2);
         int firstTileY = pixelsToTilesh(offsety);
         int lastTileY = pixelsToTilesh(offsety + MAP_BLOCK_HEIGHT*3);
         
         lastTileX=Math.min(lastTileX, (int)(provider.getXBlockCount()-1));
         lastTileY=Math.min(lastTileY, (int)provider.getYBlockCount());
         //System.out.println("firstx,y is:"+firstTileX+","+firstTileY);
         //System.out.println("lastx,y is:"+lastTileX+","+lastTileY);
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.�ӻ����ȡ��ͼ��,����Graphics��
        for (int y = firstTileY; y < lastTileY; y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
               int unitnum = (y*xBlockCount+x);

        	   //System.out.println("unitnum"+ "is:"+unitnum);
        	   int []masknum=(int[]) mask.get(unitnum);
        	
        	   for(int i=0;i<masknum.length;i++){
        		   int [] maskinfo =(int []) maskkey.get(masknum[i]);
        		   
        		   if(pcoordx<=maskinfo[0]+maskinfo[2]-1 && pcoordx>=maskinfo[0]){
        			   int []maskdata = this.mask_data.get(masknum[i]);
        			   int length = maskinfo[2]*maskinfo[3];
        			   if(maskinfo[1]+maskinfo[3] < pcoordy ){
        				   for(int l=length-maskinfo[2]-1;l<length;l++){
            				   if(maskdata[l]==1){
            					   isdm=false;
            				   }
            			   }
        			   }else if(maskinfo[1]+maskinfo[3] > pcoordy){
        				   int row = pcoordy-maskinfo[1];
        				   //System.out.println("maskinfo is:"+maskinfo[0]+","+maskinfo[1]+","
        						  // +maskinfo[2]+","+maskinfo[3]);
        				   //System.out.println("maskinfo y is:"+(maskinfo[1]+maskinfo[3]));
        				   //System.out.println("pcoordy is:"+pcoordy);
        				   for(int l=row*(maskinfo[2]-1);l<row*maskinfo[2];l++){
        					   if(maskdata[l]==1){
            					   isdm=false;
            				   }
        				   }
        			   }
        			   
        		   }else if(pcoordx<maskinfo[0] && pOffsetX+pwidth>maskinfo[0]){
        			   int []maskdata = this.mask_data.get(masknum[i]);
        			   int length = maskinfo[2]*maskinfo[3];
        			   if(maskinfo[1]+maskinfo[3] < pcoordy ){
        				   int col = maskinfo[0]+maskinfo[2]-(pOffsetX+this.pwidth);
        				   for(int l=length-maskinfo[2]-1;l<Math.min(length-col, length);l++){
            				   if(maskdata[l]==1){
            					   isdm=false;
            				   }
            			   }
        			   }else if(maskinfo[1]+maskinfo[3] > pcoordy && pcoordy>maskinfo[1]){
        				   int col = maskinfo[0]+maskinfo[2]-(pOffsetX+this.pwidth);
        				   int row = pcoordy-maskinfo[1];
        				   for(int l=row*(maskinfo[2]-1);
        						   l<Math.min(row*maskinfo[2]-col,row*maskinfo[2]);l++){
        	
        					   if(maskdata[l]==1){
            					   isdm=false;
            				   }
        				   }
        			   }
        			   
        		   }else if(pcoordx>maskinfo[0]+maskinfo[2]-1 &&
        				   pOffsetX<maskinfo[0]+maskinfo[2]-1){
        			   int []maskdata = this.mask_data.get(masknum[i]);
        			   int length = maskinfo[2]*maskinfo[3];
        			   if(maskinfo[1]+maskinfo[3] < pcoordy ){
        				   int col = maskinfo[0]+maskinfo[2]-pOffsetX;
        				   for(int l=length-Math.min(col,maskinfo[2]);l<length;l++){
        					   /*System.out.println("pcoordy is:"+pcoordy);
        					   System.out.println("maskinfo is:"+maskinfo[1]+","+
        				   maskinfo[3]);
        					   System.out.println("length is:"+maskdata.length);
        					   //System.out.println("row is:"+row);*/
        					   //System.out.println("l is:"+l);
            				   if(maskdata[l]==1){
            					   isdm=false;
            				   }
            			   }
        			   }else if(maskinfo[1]+maskinfo[3] > pcoordy && pcoordy>maskinfo[1]){
        				   int col = maskinfo[0]+maskinfo[2]-pOffsetX;
        				   int row = pcoordy-maskinfo[1];
        				   for(int l=Math.max(row*maskinfo[2]-col, maskinfo[0]);
        						   l<row*maskinfo[2];l++){
        					   
        					   /*System.out.println("pcoordy is:"+pcoordy);
        					   System.out.println("maskinfo is:"+maskinfo[1]+","+
        				   maskinfo[3]);
        					   System.out.println("length is:"+maskdata.length);
        					   System.out.println("row is:"+row);
        					   System.out.println("l is:"+l);*/
        					   if(maskdata[l]==1){
            					   isdm=false;
            				   }
        				   }
        			   }
        			   
        		   }
        		   if(isdm){
        			   g.drawImage(this.tileImageDes[masknum[i]], maskinfo[0]-viewx, 
            				   maskinfo[1]-viewy,null);
        		   }
        		   isdm=true;
               }
            }
        }    
        
    }

    /**
     * Ԥ���ش�����ĵ�ͼ��
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void prepare(int x, int y, int width, int height) {
        // 1.����Rect���ڵ�ͼ�� 
        Point pFirstBlock = viewToBlock(x, y);
        // 2.�����һ���ͼ���ViewRect��ƫ����,����Graphicsƫ��
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.����X��,Y�᷽����Ҫ�ĵ�ͼ������
        int xCount = 1 + (width - dx - 1) / MAP_BLOCK_WIDTH;
        int yCount = 1 + (height - dy - 1) / MAP_BLOCK_HEIGHT;
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.���������ĵ�ͼ��
        Image[][] images = new Image[xCount][yCount];
        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                Image img = getBlock(i + pFirstBlock.x, j + pFirstBlock.y);
                images[i][j] = img;
            }
        }
    }

    private int checkTable() {
        int count = 0;
        int width = this.blockTable.length;
        int height = this.blockTable[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                SoftReference<Image> reference = this.blockTable[i][j];
                if (reference != null && reference.get() != null) {
                    count++;
                }
            }
        }
//        if (count != lastCount) {
//            System.out.printf("map loaded block count: %s \n", count);
//        }
        lastCount = count;
        return count;
    }

    private Image getBlock(int x, int y) {
        SoftReference<Image> reference = this.blockTable[x][y];
        //����˵�ͼ�黹û����,��ȡ��ͼ�����ݲ�����ͼ��
        //���GC���ڵ��ڴ�,���ͷ�image,��Ҫ����װ��
        if (reference == null || reference.get() == null) {
            reference = new SoftReference<Image>(provider.getBlock(x, y));
            this.blockTable[x][y] = reference;
        }
        this.checkTable();
        return reference.get();
    }

    public int getXBlockCount() {
        return xBlockCount;
    }

    public void setXBlockCount(int blockCount) {
        xBlockCount = blockCount;
    }

    public int getYBlockCount() {
        return yBlockCount;
    }

    public void setYBlockCount(int blockCount) {
        yBlockCount = blockCount;
    }
    
    public int tileswToPixels(int numTiles) {
        int pixelSize = numTiles * MAP_BLOCK_WIDTH ;
        return pixelSize;
    }

    public int pixelsToTilesw(int pixelCoord) {    	
        int numTiles = pixelCoord / MAP_BLOCK_WIDTH ;
        	return numTiles;
        
    }
    
    public int tileshToPixels(int numTiles){
    	int pixelSize = numTiles * MAP_BLOCK_HEIGHT ;
        return pixelSize;
    }
    
    public int pixelsToTilesh(int pixelCoord) {
    	int numTiles = pixelCoord / MAP_BLOCK_HEIGHT ;
    	
    		return numTiles;
    	
        
    }

    /**
     * ����view����vp���Ӧ�ĵ�ͼ���ݿ�λ�� ����vp�������ĸ���ͼ���ϣ�
     * 
     * @param vp view's top left position
     * @return the map block index of the vp
     */
    private Point viewToBlock(int x, int y) {
        Point p = new Point();
        p.x = x / MAP_BLOCK_WIDTH;
        p.y = y / MAP_BLOCK_HEIGHT;
        if (p.x < 0)
            p.x = 0;
        if (p.y < 0)
            p.y = 0;
        return p;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void dispose() {
        this.provider.dispose();
        this.provider = null;
        for (SoftReference<Image>[] refs : this.blockTable) {
            for (SoftReference<Image> ref : refs) {
                if (ref != null) {
                    ref.clear();
                }
            }
        }
        this.blockTable = null;
    }

    public MapConfig getConfig() {
        return config;
    }

    public void setConfig(MapConfig config) {
        this.config = config;
    }

    public boolean contains(int x, int y) {
        return true;
    }





}
