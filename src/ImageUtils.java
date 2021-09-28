import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图形工具类 构造方法 图片文件转二维数组
 * <p>
 * 绘制 原图 马赛克 灰度
 */
public class ImageUtils {

  String defaultpath;
  public Graphics gr;
  int[][] defaultArr = {};
  int[][] defaultRarr = {};
  int[][] defaultGarr = {};
  int[][] defaultBarr = {};
  ArrayList<Integer> mosicXArr;
  ArrayList<Integer> mosicYArr;
  DrawPanel drawPanel;

  public ImageUtils(Graphics gr, DrawPanel drawPanel) {
    this.gr = gr;
    this.drawPanel = drawPanel;
  }

  /**
   * 打开文件
   */
  public void fileOpen() {
    JFileChooser fileChooser = new JFileChooser("H:\\");
    fileChooser.showOpenDialog(null);
    defaultpath = fileChooser.getSelectedFile().getPath();
  }

  /**
   * 保存文件
   */
  public File fileSave() {
    JFileChooser fileChooser = new JFileChooser("H:\\");
    fileChooser.showOpenDialog(null);
    File newpath = fileChooser.getSelectedFile();
    return newpath;
  }

  /**
   * 撤销
   *
   * @param buffimglist
   */
  public void imagecancel(ArrayList<BufferedImage> buffimglist) {
    if (buffimglist.size() > 1) {
      buffimglist.remove(buffimglist.size() - 1);
    } else {
      return;
    }
    for (int i = 0; i < buffimglist.size(); i++) {
      gr.drawImage(buffimglist.get(i), 0, 0, buffimglist.get(i).getWidth(),
          buffimglist.get(i).getHeight(), null);
    }
  }

  /**
   * 清空
   *
   * @param defaultbuffimg
   * @param buffimglist
   */
  public void cleanall(BufferedImage defaultbuffimg, ArrayList<BufferedImage> buffimglist) {
    buffimglist.removeAll(buffimglist);
    buffimglist.add(defaultbuffimg);
    gr.drawImage(defaultbuffimg, 0, 0, defaultbuffimg.getWidth(),
        defaultbuffimg.getHeight(), null);
  }

  /**
   * 转换数组
   *
   * @return
   */
  public int[][] imageFileToArr() {
    // 创建文件对象
    System.out.println(defaultpath);
    File file = new File(defaultpath);
    // 拿到图片类对象
    BufferedImage buffimg = null;
    try {
      buffimg = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 获取宽高
    int w = buffimg.getWidth();
    int h = buffimg.getHeight();
    // 声明存储的二维数组
    int[][] imgarr = new int[w][h];
    // 遍历获取像素矩阵
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        imgarr[i][j] = buffimg.getRGB(i, j);
      }
    }
    defaultArr = imgarr;
    return imgarr;
  }

  /**
   * 原图
   *
   * @param imgarr
   */
//    public void drawImage(int[][] imgarr) {
//        for (int i = 0; i < imgarr.length; i++) {
//            for (int j = 0; j < imgarr[i].length; j++) {
//                int rgbvalue = imgarr[i][j];
//                Color color = new Color(rgbvalue);
//                gr.setColor(color);
//                gr.fillRect(i, j, 1, 1);
//
//            }
//        }
//    }
  public BufferedImage drawImage(int[][] imgarr) {
    // 声明一个空的BufferedImage
    BufferedImage buffimg = new BufferedImage
        (drawPanel.getWidth(), drawPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
    // 绘制过程与屏幕刷新无关 纯内存操作
    Graphics bfg = buffimg.getGraphics();
    // 遍历次数 w*h
    for (int i = 0; i < imgarr.length; i++) {
      for (int j = 0; j < imgarr[i].length; j++) {
        int rgbvalue = imgarr[i][j];
        Color color = new Color(rgbvalue);
        bfg.setColor(color);
        bfg.fillRect(i, j, 1, 1);
      }
    }
    gr.drawImage(buffimg, 0, 0, null);
    return buffimg;
  }

  /**
   * 边缘检测
   */
  public BufferedImage edgeDetection(int value) {
    BufferedImage buffimg = new BufferedImage
        (defaultArr.length, defaultArr[0].length, BufferedImage.TYPE_INT_RGB);
    // 绘制过程与屏幕刷新无关 纯内存操作
    Graphics bfg = buffimg.getGraphics();
    for (int i = 0; i < defaultArr.length - 3; i++) {
      for (int j = 0; j < defaultArr[i].length - 3; j++) {
        int rgbvalue = defaultArr[i][j];
        // 获取三个单通道的RGB值
        int red = (rgbvalue >> 16) & 0xFF;
        int green = (rgbvalue >> 8) & 0xFF;
        int blue = (rgbvalue >> 0) & 0xFF;
        int gray = (red + green + blue) / 3;

        int rgbvaluenext = defaultArr[i + 3][j + 3];
        // 获取三个单通道的RGB值
        int rednext = (rgbvaluenext >> 16) & 0xFF;
        int greennext = (rgbvaluenext >> 8) & 0xFF;
        int bluenext = (rgbvaluenext >> 0) & 0xFF;
        int graynext = (rednext + greennext + bluenext) / 3;
        // 边缘检测
        if (Math.abs(gray - graynext) > value) {
          Color color_pr = new Color(rgbvalue);
          bfg.setColor(color_pr);
        } else {
          bfg.setColor(Color.white);
        }
        bfg.fillRect(i, j, 1, 1);
      }
    }
    gr.drawImage(buffimg, 0, 0, null);
    return buffimg;
  }

  /**
   * 灰度图
   *
   * @param imgarr
   */
  public BufferedImage drawGrayImage(int[][] imgarr) {
    BufferedImage buffimg = new BufferedImage
        (defaultArr.length, defaultArr[0].length, BufferedImage.TYPE_INT_RGB);
    // 绘制过程与屏幕刷新无关 纯内存操作
    Graphics bfg = buffimg.getGraphics();
    for (int i = 0; i < imgarr.length; i++) {
      for (int j = 0; j < imgarr[i].length; j++) {
        int rgbvalue = imgarr[i][j];
        // 获取三个单通道的RGB值
        int red = (rgbvalue >> 16) & 0xFF;
        int green = (rgbvalue >> 8) & 0xFF;
        int blue = (rgbvalue) & 0xFF;
        int gray = (red + green + blue) / 3;
        Color color = new Color(gray, gray, gray);
        bfg.setColor(color);
        bfg.fillRect(i, j, 1, 1);
      }
    }
    gr.drawImage(buffimg, 0, 0, null);
    return buffimg;
  }

  /**
   * 马赛克
   *
   * @param x
   * @param y
   */
  public void drawMosaic(int x, int y, BufferedImage buffimg) {
    int w = buffimg.getWidth();
    int h = buffimg.getHeight();
    // 声明存储的二维数组
    int[][] imgarr = new int[w][h];
    // 遍历获取像素矩阵
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        imgarr[i][j] = buffimg.getRGB(i, j);
      }
    }
    int rgbvalue = imgarr[x][y];
    Color color = new Color(rgbvalue);
    gr.setColor(color);
    gr.fillRect(x, y, 10, 10);
  }

  /**
   * 油画
   *
   * @param
   */
  public BufferedImage drawOilPainting(ArrayList<BufferedImage> buffimglist) {
    int w = buffimglist.get(buffimglist.size() - 1).getWidth();
    int h = buffimglist.get(buffimglist.size() - 1).getHeight();
    BufferedImage buffimg = new BufferedImage
        (defaultArr.length, defaultArr[0].length, BufferedImage.TYPE_INT_RGB);
    // 绘制过程与屏幕刷新无关 纯内存操作
    Graphics bfg = buffimg.getGraphics();
    int[][] imgarr = new int[w][h];
    // 遍历获取像素矩阵
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        imgarr[i][j] = buffimglist.get(buffimglist.size() - 1).getRGB(i, j);
      }
    }
    for (int i = 0; i < imgarr.length; i += 2) {
      for (int j = 0; j < imgarr[i].length - 2; j += 2) {
        int rgbvalue = imgarr[i][j];
        Color color = new Color(rgbvalue);
        bfg.setColor(color);
        bfg.fillRoundRect(i, j, 16, 16, 16, 16);
      }
    }
    gr.drawImage(buffimg, 0, 0, null);
    return buffimg;
  }

  /**
   * 融合
   *
   * @param
   * @return
   */
  public int[][] imageFuse(ArrayList<BufferedImage> buffimglist) {
    String nextpath;
    JFileChooser fileChooser = new JFileChooser("H:\\");
    fileChooser.showOpenDialog(null);
    nextpath = fileChooser.getSelectedFile().getPath();
    File file = new File(nextpath);
    // 拿到图片类对象
    BufferedImage buffimg = null;
    try {
      buffimg = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 获取宽高
    int w = buffimg.getWidth();
    int h = buffimg.getHeight();
    int w2 = buffimglist.get(buffimglist.size() - 1).getWidth();
    int h2 = buffimglist.get(buffimglist.size() - 1).getHeight();
    if (w2 <= w) {
      w = w2;
    }
    if (h2 <= h) {
      h = h2;
    }
    // 声明存储的二维数组
    int[][] imgarr = new int[w][h];
    int[][] imgarr2 = new int[w][h];
    // 遍历获取像素矩阵
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        imgarr[i][j] = buffimglist.get(buffimglist.size() - 1).getRGB(i, j);
      }
    }
    // 遍历获取像素矩阵
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        imgarr[i][j] = (imgarr2[i][j] + buffimg.getRGB(i, j)) / 2;
      }
    }
    return imgarr;
  }

  public void rgbgetarr() {
    int[][] Rarr = new int[defaultArr.length][defaultArr[0].length];
    int[][] Garr = new int[defaultArr.length][defaultArr[0].length];
    int[][] Barr = new int[defaultArr.length][defaultArr[0].length];
    for (int i = 0; i < defaultArr.length - 3; i++) {
      for (int j = 0; j < defaultArr[i].length; j++) {
        int rgbvalue = defaultArr[i][j];
        // 获取三个单通道的RGB值
        int red = (rgbvalue >> 16) & 0xFF;
        int green = (rgbvalue >> 8) & 0xFF;
        int blue = (rgbvalue) & 0xFF;
        Rarr[i][j] = red;
        Garr[i][j] = green;
        Barr[i][j] = blue;
      }
    }
    defaultRarr = Rarr;
    defaultGarr = Garr;
    defaultBarr = Barr;
  }

  public int[][] imageprocessing(int[][] imafr, int[][] imagearr) {
    int[][] exarr = new int[imagearr.length - 2][imagearr[0].length - 2];
    for (int i = 0; i < imagearr.length - 2; i++) {
      for (int j = 0; j < imagearr[i].length - 2; j++) {
        int x = imagearr[i][j] * imafr[0][0] + imagearr[i][j + 1] * imafr[0][1]
            + imagearr[i][j + 2] * imafr[0][2] +
            imagearr[i + 1][j] * imafr[1][0] + imagearr[i + 1][j + 1] * imafr[1][1]
            + imagearr[i + 1][j + 2] * imafr[1][2] +
            imagearr[i + 2][j] * imafr[2][0] + imagearr[i + 2][j + 1] * imafr[2][1]
            + imagearr[i + 2][j + 2] * imafr[2][2];
        if (x >= 255) {
          x = 255;
        }
        if (x <= 0) {
          x = 0;
        }
        exarr[i][j] = x;
      }
    }
    return exarr;
  }

  public int[][] rgbfix(int[][] R, int[][] G, int[][] B) {
    int[][] myimage = new int[R.length][R[0].length];
    for (int i = 0; i < R.length; i++) {
      for (int j = 0; j < R[0].length; j++) {
        myimage[i][j] =
            ((R[i][j] << 16) & 0xFF0000) | ((G[i][j] << 8) & 0xFF00) | ((B[i][j]) & 0xFF);
      }
    }
    return myimage;
  }

  public BufferedImage drawsharpening(int[][] arr) {
    BufferedImage buffimg = new BufferedImage
        (arr.length, arr[0].length, BufferedImage.TYPE_INT_RGB);
    Graphics bfg = buffimg.getGraphics();
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < arr[i].length; j++) {
        int rgbvalue = arr[i][j];
        Color color = new Color(rgbvalue);
        bfg.setColor(color);
        bfg.fillRect(i, j, 1, 1);
      }
    }
    gr.drawImage(buffimg, 0, 0, null);
    return buffimg;
  }
}