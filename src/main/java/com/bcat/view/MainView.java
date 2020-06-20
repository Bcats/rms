package com.bcat.view;

import com.bcat.constant.CommonConstant;
import com.bcat.model.MyTableModel;
import com.bcat.renderer.LinkCellRenderer;
import com.bcat.utils.CommonUtil;
import com.bcat.utils.LogUtil;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;


public class MainView {

    public static JLabel cookieLabel         = new JLabel("Cookie:");

    public static JTextField cookieText      = new JTextField(20);

    public static JLabel emailLabel          = new JLabel("Email:");

    public static JTextField emailText       = new JTextField(20);

    public static JButton runButton          = new JButton("开始");

    public static JTextArea logText          = new JTextArea();

    public static MyTableModel myTableModel  = new MyTableModel();

    public static JTable alarmTable          = new JTable(myTableModel);

    public static LogUtil logUtil            = new LogUtil(logText);

    public static JLabel statusLabel         = new JLabel("状态：无");

    private static final String WINDOW_TITLE = "rms110";

    private static final int WINDOW_HEIGHT   = 500;

    private static final int WINDOW_WIDTH    = 800;

    public MainView() {
        // 初始化视图
        initFrame();
        // 获取账号密码
        if(new File(CommonConstant.ACCOUNT_FILE_PATH).exists()) getAccount();
    }

    private static void initGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    private void initFrame() {
        // 创建 JFrame 实例
        JFrame frame = new JFrame(WINDOW_TITLE);
        // Setting the width and height of frame
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                deleteCache();
                super.windowClosing(e);
            }
        });

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        cookieLabel.setBounds(10,5,60,25);
        panel.add(cookieLabel);

        cookieText.setBounds(60,5,285,25);
        panel.add(cookieText);

        emailLabel.setBounds(10,40,60,25);
        panel.add(emailLabel);

        emailText.setBounds(60,40,180,25);
        panel.add(emailText);

        runButton.setBounds(245, 40, 100, 25);
        panel.add(runButton);

        JScrollPane tsPane = new JScrollPane(alarmTable);

        alarmTable.setBounds(5,240, WINDOW_WIDTH - 25, 200);
        alarmTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        alarmTable.getColumnModel().getColumn(1).setPreferredWidth(110);
        alarmTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        alarmTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        alarmTable.getColumnModel().getColumn(4).setPreferredWidth(35);
        alarmTable.getColumnModel().getColumn(5).setPreferredWidth(125);
        alarmTable.getColumnModel().getColumn(6).setPreferredWidth(45);
        alarmTable.getColumnModel().getColumn(7).setPreferredWidth(70);

        tsPane.setBounds(5, 240, WINDOW_WIDTH - 25, 200);
        tsPane.add(alarmTable.getTableHeader());
        panel.add(tsPane);

//        // 1.排序
//        TableRowSorter rowSorter = new TableRowSorter(myTableModel);
//        alarmTable.setRowSorter(rowSorter);
//        List<RowSorter.SortKey> keys = new ArrayList<>();
//        keys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING));
//        rowSorter.setSortKeys(keys);
//        // 2.给 表格 设置 行排序器
//        RowSorter<MyTableModel> rowSorter = new TableRowSorter<>(myTableModel);
//        alarmTable.setRowSorter(rowSorter);
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//        sortKeys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING));
//        rowSorter.setSortKeys(sortKeys);

        //创建单元格渲染器暨鼠标事件监听器
        LinkCellRenderer renderer = new LinkCellRenderer();
        //注入渲染器
        alarmTable.setDefaultRenderer(Object.class, renderer);
        //注入监听器
        alarmTable.addMouseListener(renderer);
        alarmTable.addMouseMotionListener(renderer);
        // 把 表头 添加到容器顶部（使用普通的中间容器添加表格时，表头 和 内容 需要分开添加）

        JScrollPane scrollPane = new JScrollPane(logText);
        logText.setLineWrap(true);
        logText.setEditable(false);
        logText.setBounds(355,5, WINDOW_WIDTH - 375, 230);
        scrollPane.setBounds(355, 5, WINDOW_WIDTH - 375, 230);
        panel.add(scrollPane);

        statusLabel.setBounds(10,WINDOW_HEIGHT - 60,460,25);
        panel.add(statusLabel);
        // 设置界面可见
        frame.setVisible(true);
        // 设置全局字体
        initGlobalFont(new Font("微软雅黑", Font.PLAIN, 12));
    }
    /**
     * 文件时间超过一天，就删除。
     */
    private void deleteCache(){
        File zb_file = new File(CommonConstant.ZABBIX_SEND_SIGN_FILENAME);
        File rms_file = new File(CommonConstant.RMS_SEND_SIGN_FILENAME);
        long now_time = new Date().getTime();

        if (rms_file.exists() && now_time - rms_file.lastModified() > 86400000){
            rms_file.delete();
        }
        if (zb_file.exists() && now_time - zb_file.lastModified() > 86400000){
            zb_file.delete();
        }
    }

    public static void saveAccount(){
        String content = cookieText.getText()+ "\n" + emailText.getText();
        CommonUtil.writeStr(CommonConstant.ACCOUNT_FILE_PATH, content, false);
    }

    public static void getAccount(){
        String content = CommonUtil.readStr(CommonConstant.ACCOUNT_FILE_PATH);
        String[] lines = content.split("\n");
        if (lines.length >= 2){
            String cookie = lines[0];
            String email = lines[1];
            cookieText.setText(cookie);
            emailText.setText(email);
        }else {logUtil.info("[error] -> 读取账号信息失败");}

    }

    public static boolean isTextNotNull(){
        if (cookieText.getText() == null || cookieText.getText().equals("")) return false;
        if (emailText.getText() == null || emailText.getText().equals("")) return false;
        return true;
    }



}
