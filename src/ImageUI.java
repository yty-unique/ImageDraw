import javax.swing.*;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

public class ImageUI {

  public static void main(String[] args) {
    new ImageUI().initUI();
  }

  //创建监听器对象
  ImageListener imagel = new ImageListener();
  //    String path;

  String[] btnstrs = new String[]{
      "原图", "灰度", "轮廓检测", "马赛克", "油画",
      "融合", "高斯模糊", "锐化", "浮雕", "均值模糊"
  };
  String[] menus1 = new String[]{
      "打开", "保存", "退出"
  };
  String[] menus2 = new String[]{
      "开始录制", "录制结束", "回放"
  };
  String[] menus3 = new String[]{
      "撤销", "清空"
  };

  //初始化界面
  public void initUI() {
    JFrame jf = new JFrame("图像处理");
    jf.setSize(1000, 610);
    jf.setLocationRelativeTo(null);//居中显示
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //菜单栏
    Font font = new Font("黑体", Font.PLAIN, 14);
    JMenuBar imgmenu = new JMenuBar();
    imgmenu.setBackground(new Color(120, 120, 120));
    imgmenu.setPreferredSize(new Dimension(0, 30));
    JMenu filemenu = new JMenu("文件");
    filemenu.setFont(font);
    filemenu.setPreferredSize(new Dimension(120, 0));
    JMenu filtermenu = new JMenu("滤镜");
    filtermenu.setFont(font);
    filtermenu.setPreferredSize(new Dimension(120, 0));
    JMenu toolmenu = new JMenu("工具");
    toolmenu.setFont(font);
    toolmenu.setPreferredSize(new Dimension(120, 0));
    JMenu editmenu = new JMenu("编辑");
    editmenu.setFont(font);
    editmenu.setPreferredSize(new Dimension(120, 0));
    JButton exitbut = new JButton();
    imgmenu.add(filemenu);
    imgmenu.add(filtermenu);
    imgmenu.add(toolmenu);
    imgmenu.add(editmenu);
//        imgmenu.add(exitbut);
    //菜单项
    Dimension dim = new Dimension(120, 30);
    for (int i = 0; i < menus1.length; i++) {
      JMenuItem menuItem = new JMenuItem(menus1[i]);
      menuItem.setBackground(Color.WHITE);
      menuItem.setFont(font);
      menuItem.setPreferredSize(dim);
      menuItem.addActionListener(imagel);
      filemenu.add(menuItem);
    }
    for (int i = 0; i < menus2.length; i++) {
      JMenuItem menuItem = new JMenuItem(menus2[i]);
      menuItem.setBackground(Color.WHITE);
      menuItem.setFont(font);
      menuItem.setPreferredSize(dim);
      menuItem.addActionListener(imagel);
      toolmenu.add(menuItem);
    }
    for (int i = 0; i < menus3.length; i++) {
      JMenuItem menuItem = new JMenuItem(menus3[i]);
      menuItem.setBackground(Color.WHITE);
      menuItem.setFont(font);
      menuItem.setPreferredSize(dim);
      menuItem.addActionListener(imagel);
      editmenu.add(menuItem);
    }
    //加载滑块
    JSlider oilslider = new JSlider(0, 255);
    oilslider.addChangeListener(imagel);
    oilslider.setOrientation(SwingConstants.HORIZONTAL);
    oilslider.setVisible(true);
    oilslider.setSize(250, 30);
    oilslider.setMajorTickSpacing(25);
    oilslider.setPaintTrack(true);
    oilslider.setPaintTrack(true);
    oilslider.setPaintLabels(true);
    //分制面板——按钮面板
    DrawPanel drawPanel = new DrawPanel();
    drawPanel.setBackground(new Color(80, 80, 80));
    JPanel brpanel = new JPanel();
    brpanel.setBackground(new Color(100, 100, 100));
    // 绘制添加鼠标监听器
    drawPanel.addMouseListener(imagel);
    drawPanel.addMouseMotionListener(imagel);
    drawPanel.add(oilslider);
    oilslider.setVisible(false);
    //子菜单
    for (int i = 0; i < btnstrs.length; i++) {
      JMenuItem menuItem = new JMenuItem(btnstrs[i]);
      menuItem.setBackground(Color.WHITE);
      menuItem.setFont(font);
      menuItem.setPreferredSize(dim);
      if (i != 2) {
        menuItem.addActionListener(imagel);
        menuItem.addActionListener(e -> {
          oilslider.setVisible(false);
        });
      } else {
        drawPanel.setLayout(null);
        oilslider.setBounds(400, 520, 230, 30);
        menuItem.addActionListener(e -> {
          oilslider.setVisible(true);
        });
      }
      filtermenu.add(menuItem);
    }

//        oilslider.setVisible(false);

    // 将面板加载到窗体上
    ReSizeEvent dg = new ReSizeEvent(jf);
    jf.addMouseListener(dg);
    jf.addMouseMotionListener(dg);
    jf.setJMenuBar(imgmenu);
    jf.setUndecorated(true);
//        drawPanel.add(brpanel,BorderLayout.SOUTH);
    jf.add(drawPanel, BorderLayout.CENTER);
    jf.setVisible(true);// 可视化

    // 获取图形类 对象 -- 画笔
    imagel.setDrawPanel(imagel.drawPanel);
    Graphics g = drawPanel.getGraphics();
    imagel.setGraphics(g);
    drawPanel.setImageList(imagel.buffimglist);
    imagel.initImageUtils(imagel.gr, drawPanel);
  }
}

// 自己写一个类继承 面板 重写面板的绘制方法
class DrawPanel extends JPanel {

  ArrayList<BufferedImage> buffimglist = null;

  public void setImageList(ArrayList<BufferedImage> buffimglist) {
    this.buffimglist = buffimglist;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (buffimglist == null) {
      return;
    }
    if (buffimglist.size() == 0) {
      return;
    }
    for (int i = 0; i < buffimglist.size(); i++) {
      g.drawImage(buffimglist.get(i), 0, 0, this.getWidth(),
          this.getHeight(), null);
    }
  }
}
