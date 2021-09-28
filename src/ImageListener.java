import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MenuEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ImageListener extends ListenerImp {

  String myPath;
  boolean getmyPath = false;
  boolean recording = false;
  int recordinglocate;
  Graphics gr;
  ImageUtils imageUtils;
  int[][] imgarr, imgarr2;
  //锐化
  int[][] sharpening = {{-1, -1, -1},
      {-1, 9, -1},
      {-1, -1, -1}};
  //浮雕
  int[][] relief = {{-1, -1, -1},
      {1, 1, -1},
      {1, 1, 1}};
  //均值模糊处理
  int[][] meanvalue = {{2, 1, 2},
      {1, -9, 1},
      {2, 1, 2}};
  boolean eMosaic = false;
  ArrayList<BufferedImage> buffimglist = new ArrayList<>();
  ArrayList<BufferedImage> recordinglist = new ArrayList<>();
  BufferedImage buffimg, defaultbuffimg;
  DrawPanel drawPanel;

  public ArrayList<BufferedImage> getImageList() {
    return buffimglist;
  }

  public void setDrawPanel(DrawPanel drawPanel) {
    this.drawPanel = drawPanel;
  }

  /**
   * 传入 画笔对象 并初始化画图工具类
   *
   * @param gr
   * @return
   */
  public void setGraphics(Graphics gr) {
    this.gr = gr;
  }

  /**
   * 初始化画图滤镜
   *
   * @param gr
   */
  public void initImageUtils(Graphics gr, DrawPanel drawPanel) {
    imageUtils = new ImageUtils(gr, drawPanel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // 获取按钮对象 或者按钮上字符串
    //JButton btn = (JButton) e.getSource();
    String btnstr = e.getActionCommand();
    System.out.println(btnstr);
    switch (btnstr) {
      case "打开" -> {
        eMosaic = false;
        imageUtils.fileOpen();
        imgarr = imageUtils.imageFileToArr();
        defaultbuffimg = imageUtils.drawImage(imgarr);
        buffimglist.add(defaultbuffimg);
        System.out.println(buffimglist.size());
      }
      case "保存" -> {
        eMosaic = false;
        BufferedImage newimage = buffimglist.get(buffimglist.size() - 1);
        System.out.println(buffimglist.size());

        File getimage = imageUtils.fileSave();
        try {
          ImageIO.write(newimage, "jpg", getimage);
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
      }
      case "撤销" -> {
        eMosaic = false;
        imageUtils.imagecancel(buffimglist);
        System.out.println(buffimglist.size());
      }
      case "清空" -> {
        eMosaic = false;
        imageUtils.cleanall(defaultbuffimg, buffimglist);
        System.out.println(buffimglist.size());
      }
      case "开始录制" -> {
        eMosaic = false;
        recording = true;
        recordinglocate = buffimglist.size() - 1;
      }
      case "录制结束" -> {
        eMosaic = false;
        recording = false;
      }
      case "退出" -> {
        System.exit(0);
      }
      case "回放" -> {
        eMosaic = false;
        for (int i = 0; i < buffimglist.size(); i++) {
          gr.drawImage(buffimglist.get(i), 0, 0, buffimglist.get(i).getWidth(),
              buffimglist.get(i).getHeight(), null);
          Thread thread = null;
          try {
            thread.sleep(1500);
          } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
          }
        }
      }
      case "原图" -> {
        // 绘制原图
        eMosaic = false;
        buffimg = imageUtils.drawImage(imgarr);
        buffimglist.add(buffimg);
        System.out.println(buffimglist.size());
        eMosaic = false;
      }
      case "轮廓检测" -> {
        eMosaic = false;
      }
      case "灰度" -> {
        // 绘制灰阶图片
        eMosaic = false;
        buffimg = imageUtils.drawGrayImage(imgarr);
        buffimglist.add(buffimg);
        System.out.println(buffimglist.size());
      }
      case "马赛克" -> {
        // 绘制马赛克
        eMosaic = true;
      }
      case "油画" -> {
        // 绘制油画
        eMosaic = false;
        buffimg = imageUtils.drawOilPainting(buffimglist);
        buffimglist.add(buffimg);
        System.out.println(buffimglist.size());
      }
      case "融合" -> {
        eMosaic = false;
        imgarr = imageUtils.imageFuse(buffimglist);
        buffimg = imageUtils.drawImage(imgarr);
        buffimglist.add(buffimg);
        System.out.println(buffimglist.size());
      }
      case "锐化" -> {
        imageUtils.rgbgetarr();
        int[][] R = imageUtils.imageprocessing(sharpening, imageUtils.defaultRarr);
        int[][] G = imageUtils.imageprocessing(sharpening, imageUtils.defaultGarr);
        int[][] B = imageUtils.imageprocessing(sharpening, imageUtils.defaultBarr);
        int[][] myarr = imageUtils.rgbfix(R, G, B);
        buffimg = imageUtils.drawsharpening(myarr);
        buffimglist.add(buffimg);
      }
      case "浮雕" -> {
        imageUtils.rgbgetarr();
        int[][] R = imageUtils.imageprocessing(relief, imageUtils.defaultRarr);
        int[][] G = imageUtils.imageprocessing(relief, imageUtils.defaultGarr);
        int[][] B = imageUtils.imageprocessing(relief, imageUtils.defaultBarr);
        int[][] myarr = imageUtils.rgbfix(R, G, B);
        buffimg = imageUtils.drawsharpening(myarr);
        buffimglist.add(buffimg);
      }
      case "均值模糊" -> {
        imageUtils.rgbgetarr();
        int[][] R = imageUtils.imageprocessing(meanvalue, imageUtils.defaultRarr);
        int[][] G = imageUtils.imageprocessing(meanvalue, imageUtils.defaultGarr);
        int[][] B = imageUtils.imageprocessing(meanvalue, imageUtils.defaultBarr);
        int[][] myarr = imageUtils.rgbfix(R, G, B);
        buffimg = imageUtils.drawsharpening(myarr);
        buffimglist.add(buffimg);
      }
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    // 坐标
    int x = e.getX();
    int y = e.getY();
    // 鼠标的左中右键
    int buttonnum = e.getButton();
    // 鼠标的点击次数
    int clickCount = e.getClickCount();
    if (eMosaic) {
      imageUtils.drawMosaic(x, y, buffimglist.get(buffimglist.size() - 1));
      System.out.println(x + " " + y);
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyChar = e.getKeyChar();
    if (keyChar == KeyEvent.VK_UP) {
      ;
    }
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    JSlider oilslider = (JSlider) e.getSource();
    buffimg = imageUtils.edgeDetection(oilslider.getValue());
    buffimglist.add(buffimg);
    System.out.println(buffimglist.size());
  }
}
