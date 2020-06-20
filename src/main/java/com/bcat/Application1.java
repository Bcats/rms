//package com.bcat;
//
//import com.bcat.constant.CommonConstant;
//import com.bcat.dao.RmsTableDao;
//import com.bcat.handler.RmsHandler;
//import com.bcat.handler.ZabbixHandler;
//import com.bcat.model.MailModel;
//import com.bcat.model.MyTableModel;
//import com.bcat.renderer.LinkCellRenderer;
//import com.bcat.service.RmsBusinessService;
//import com.bcat.service.RmsServerService;
//import com.bcat.service.ZabbixHostService;
//import com.bcat.utils.*;
//
//import javax.swing.*;
//import javax.swing.Timer;
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.File;
//import java.util.*;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//@Deprecated
//public class Application1 {
//
//    private static JLabel cookieLabel;
//
//    private static JTextField cookieText;
//
//    private static JLabel emailLabel;
//
//    private static JTextField emailText;
//
//    private static JButton runButton;
//
////    private static JTable alarmTable;
//
////    private static MyTableModel myTableModel;
//
//    public static JTextArea logText;
//
//    private static JLabel statusLabel;
//
//    private static final String ACCOUNT_FILE_PATH = "account.log";
//
//    private static LogUtil logUtil;
//
//    private static final String WINDOW_TITLE = "rms110";
//
//    private static final int WINDOW_HEIGHT = 500;
//
//    private static final int WINDOW_WIDTH = 800;
//
//    static {
//        Font font = new Font("微软雅黑", Font.PLAIN,12);
//        String names[] = { "Label", "CheckBox", "PopupMenu","MenuItem", "CheckBoxMenuItem",
//                "JRadioButtonMenuItem","ComboBox", "Button", "Tree", "ScrollPane",
//                "TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
//                "OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
//                "ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
//                "PasswordField","TextField", "Table", "Label", "Viewport",
//                "RadioButtonMenuItem","RadioButton", "DesktopPane", "InternalFrame"
//        };
//        for (String item : names) {
//            UIManager.put(item+ ".font", font);
//        }
//    }
//
//    public static void main(String[] args) {
//            // 创建 JFrame 实例
//            JFrame frame = new JFrame(WINDOW_TITLE);
//            // Setting the width and height of frame
//            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.addWindowListener(new WindowAdapter() {
//                public void windowClosing(WindowEvent e) {
//                    deleteCache();
//                    super.windowClosing(e);
//                }
//            });
//
//
//        /* 创建面板，这个类似于 HTML 的 div 标签
//             * 我们可以创建多个面板并在 JFrame 中指定位置
//             * 面板中我们可以添加文本字段，按钮及其他组件。
//             */
//            JPanel panel = new JPanel();
//            // 添加面板
//            frame.add(panel);
//            /*
//             * 调用用户定义的方法并添加组件到面板
//             */
//            placeComponents(panel);
//
//            // 设置界面可见
//            frame.setVisible(true);
//        }
//
//    /**
//     * 文件时间超过一天，就删除。
//     */
//    private static void deleteCache(){
//            File zb_file = new File(CommonConstant.ZABBIX_SEND_SIGN_FILENAME);
//            File rms_file = new File(CommonConstant.RMS_SEND_SIGN_FILENAME);
//            long now_time = new Date().getTime();
//
//            if (rms_file.exists() && now_time - rms_file.lastModified() > 86400000){
//                rms_file.delete();
//            }
//            if (zb_file.exists() && now_time - zb_file.lastModified() > 86400000){
//                zb_file.delete();
//            }
//        }
//
//    private static void placeComponents(JPanel panel) {
//
//        /* 布局部分我们这边不多做介绍
//         * 这边设置布局为 null
//         */
//        panel.setLayout(null);
//
//        cookieLabel = new JLabel("Cookie:");
//        cookieLabel.setBounds(10,5,60,25);
//        panel.add(cookieLabel);
//
//        cookieText = new JTextField(20);
//        cookieText.setBounds(60,5,285,25);
//        panel.add(cookieText);
//
//        emailLabel = new JLabel("Email:");
//        emailLabel.setBounds(10,40,60,25);
//        panel.add(emailLabel);
//
//        emailText = new JTextField(20);
//        emailText.setBounds(60,40,180,25);
//        panel.add(emailText);
//
//        // 创建登录按钮
//        runButton = new JButton("开始");
//        runButton.setBounds(245, 40, 100, 25);
//        panel.add(runButton);
//
//        //  创建表格
//        MyTableModel myTableModel = new MyTableModel();
//        JTable alarmTable = new JTable(myTableModel);
//        JScrollPane tsPane = new JScrollPane(alarmTable);
//
//        alarmTable.setBounds(5,240, WINDOW_WIDTH - 25, 200);
//        alarmTable.getColumnModel().getColumn(0).setPreferredWidth(70);
//        alarmTable.getColumnModel().getColumn(1).setPreferredWidth(110);
//        alarmTable.getColumnModel().getColumn(2).setPreferredWidth(140);
//        alarmTable.getColumnModel().getColumn(3).setPreferredWidth(180);
//        alarmTable.getColumnModel().getColumn(4).setPreferredWidth(35);
//        alarmTable.getColumnModel().getColumn(5).setPreferredWidth(125);
//        alarmTable.getColumnModel().getColumn(6).setPreferredWidth(45);
//        alarmTable.getColumnModel().getColumn(7).setPreferredWidth(70);
//
//        tsPane.setBounds(5, 240, WINDOW_WIDTH - 25, 200);
//        tsPane.add(alarmTable.getTableHeader());
//        panel.add(tsPane);
//
////        // 1.排序
////        TableRowSorter rowSorter = new TableRowSorter(myTableModel);
////        alarmTable.setRowSorter(rowSorter);
////        List<RowSorter.SortKey> keys = new ArrayList<>();
////        keys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING));
////        rowSorter.setSortKeys(keys);
////        // 2.给 表格 设置 行排序器
////        RowSorter<MyTableModel> rowSorter = new TableRowSorter<>(myTableModel);
////        alarmTable.setRowSorter(rowSorter);
////        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
////        sortKeys.add(new RowSorter.SortKey(5, SortOrder.DESCENDING));
////        rowSorter.setSortKeys(sortKeys);
//
//        //创建单元格渲染器暨鼠标事件监听器
//        LinkCellRenderer renderer = new LinkCellRenderer();
//        //注入渲染器
//        alarmTable.setDefaultRenderer(Object.class, renderer);
//        //注入监听器
//        alarmTable.addMouseListener(renderer);
//        alarmTable.addMouseMotionListener(renderer);
//        // 把 表头 添加到容器顶部（使用普通的中间容器添加表格时，表头 和 内容 需要分开添加）
//
//        logText = new JTextArea();
//        JScrollPane scrollPane = new JScrollPane(logText);
//        logText.setLineWrap(true);
//        logText.setEditable(false);
//        logText.setBounds(355,5, WINDOW_WIDTH - 375, 230);
//        scrollPane.setBounds(355, 5, WINDOW_WIDTH - 375, 230);
//        panel.add(scrollPane);
//
//        statusLabel = new JLabel("状态：无");
//        statusLabel.setBounds(10,WINDOW_HEIGHT - 60,460,25);
//        panel.add(statusLabel);
//
//        logUtil = new LogUtil(logText);
//        final int delay = 30000;
//        AtomicInteger count = new AtomicInteger();
//        AtomicInteger bufferHour = new AtomicInteger();
//        ZabbixHandler zabbixHandler = new ZabbixHandler();
//        RmsHandler rmsHandler = new RmsHandler();
//        // 获取账号密码
//        if(new File(ACCOUNT_FILE_PATH).exists()) getAccount();
//
//        // 监控定时器
//        final Timer timer = new Timer(delay, (e) -> {
//
//            String cookie = cookieText.getText();
//            String emailReceiver = emailText.getText();
//            // 获取数据
//            List<Map<String,String>> rmsBusinessDataArrays = rmsHandler.getBusinessAlarms(cookie);
//            List<Map<String,String>> rmsServerDataArrays = rmsHandler.getServerAlarms(cookie);
//            List<Map<String,String>> zabbixDataArrays = zabbixHandler.getData();
//
//            if (rmsBusinessDataArrays == null || rmsServerDataArrays == null){
//                logUtil.error("  rms网络异常或Cookie失效");
//                return;
//            }
//            if (zabbixDataArrays == null){
//                logUtil.error("  zabbix网络异常或Cookie失效");
//                return;
//            }
//
//            // 当前监控时间段报时
//            if (bufferHour.get() != Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
//                bufferHour.set(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//                logUtil.info("  -------------  "+ bufferHour.get() + "点  -------------");
//            }
//            // 获取需要发送的数据
//            List<Map<String,String>> rmsBusinessSendDataArrays = RmsBusinessService.checkBusinessSend(rmsBusinessDataArrays);
//            List<Map<String,String>> rmsServerSendDataArrays = RmsServerService.checkServerSend(rmsServerDataArrays);
//            List<Map<String,String>> zabbixSendDataArrays = ZabbixHostService.checkSend(zabbixDataArrays);
//
//            /*if (sendDataArrays.size() != 0){
//                logUtil.info("  共获取 " + dataArrays.size() + " 条数据, " +
//                        "需发送 " + sendDataArrays.size() + " 条数据");
//            }*/
//
//            if (rmsBusinessSendDataArrays.size() !=0 ){
//                List<String[]> rbsdStringsList = new ArrayList<>();
//                List<MailModel> rmsBusinessMailModelList = new ArrayList<>();
//                for (Map<String,String> rbsdMap : rmsBusinessSendDataArrays){
//                    // 得到JTable数据
//                    String[] rbsdStrings = RmsTableDao.mapToStrings(rbsdMap);
//                    rbsdStringsList.add(rbsdStrings);
//                    // 从 Map data 中取出邮件发送需要的数据
//                    String id = rbsdMap.get("id");
//                    String projectName = rbsdMap.get("projectName");
//                    String alarmTime = rbsdMap.get("alarmTime");
//                    String level = rbsdMap.get("level");
//                    String errorInfo = rbsdMap.get("errorInfo");
//                    rbsdMap.put("alarmDetailUrl", rmsHandler.getAlarmUrlById(true ,id, cookie));
//                    String title = "[" + TimeUtil.getHourByDate(alarmTime) + "点][rms业务][" + level + "]" + projectName;
//                    String content = MailModel.rmsBusinessMailTemplate(rbsdMap);
//                    rmsBusinessMailModelList.add(new MailModel(emailReceiver, title, content));
//                    logUtil.info("  [rms业务] [" + level + "] " + projectName + " " + errorInfo);
//                }
//                // 更新JTable列表
//                myTableModel.list.addAll(rbsdStringsList);
////                myTableModel.getTotal();
//                alarmTable.updateUI();
//                // 开始发送rms业务告警邮件
//                List<Boolean> rmsBusinessBooleanList = EmailUtil.sendEmails(rmsBusinessMailModelList);
//            }
//
//
//            if (rmsServerSendDataArrays.size() != 0){
//                List<String[]> rssdStringsList = new ArrayList<>();
//                List<MailModel> rmsServerMailModelList = new ArrayList<>();
//                for (Map<String,String> rssdMap : rmsServerSendDataArrays){
//                    // 得到JTable数据
//                    String[] rssdStrings = RmsTableDao.mapToStrings(rssdMap);
//                    rssdStringsList.add(rssdStrings);
//                    // 从 Map data 中取出邮件发送需要的数据
//                    String id = rssdMap.get("id");
//                    String businessName = rssdMap.get("businessName");
//                    String alarmTime = rssdMap.get("alarmTime");
//                    String level = rssdMap.get("level");
//                    String metric = rssdMap.get("metric");
//                    rssdMap.put("alarmDetailUrl", rmsHandler.getAlarmUrlById(false, id, cookie));
//                    String title = "[" + TimeUtil.getHourByDate(alarmTime) + "点][rms服务器][" + level + "]" + businessName;
//                    String content = MailModel.rmsServerMailTemplate(rssdMap);
//                    rmsServerMailModelList.add(new MailModel(emailReceiver, title, content));
//                    logUtil.info("  [rms服务器] [" + level + "] " + businessName + " " + metric);
//                }
//                // 更新JTable列表
//                myTableModel.list.addAll(rssdStringsList);
////                myTableModel.getTotal();
//                alarmTable.updateUI();
//                // 开始发送rms服务器告警邮件
//                List<Boolean> rmsBooleanList = EmailUtil.sendEmails(rmsServerMailModelList);
//            }
//
//            if (zabbixSendDataArrays.size() != 0){
//                List<String[]> zsdStringsList = new ArrayList<>();
//                List<MailModel> zabbixMailModelList = new ArrayList<>();
//                for (Map<String,String> zsdArray : zabbixSendDataArrays){
//                    // 得到JTable数据
//                    String[] zsdStrings = new String[]{"", "", "", "", "", "", "", ""};
//                    zsdStrings[1] = zsdArray.get("host");
//                    zsdStrings[2] = zsdArray.get("question");
//                    zsdStrings[5] = zsdArray.get("time");
//                    zsdStringsList.add(zsdStrings);
//                    // 从 Map data 中取出邮件发送需要的数据
//                    String time = zsdArray.get("time");
//                    String title = "[" + time + "]zabbix有一条红色告警超10分钟未消除";
//                    String content = MailModel.zabbixMailTemplate(zsdArray);
//                    zabbixMailModelList.add(new MailModel(emailReceiver, title, content));
//                    logUtil.info("  发送一条 [ zbbix ] 告警至邮箱 ");
//                }
//                // 更新JTable列表
//                myTableModel.list.addAll(zsdStringsList);
////                myTableModel.getTotal();
//                alarmTable.updateUI();
//                //  开始发送zabbix告警邮件
//                List<Boolean> zabbixBooleanList = EmailUtil.sendEmails(zabbixMailModelList);
//            }
//
//            // 调用状态label更新
//            statusLabel.setText("状态：第 " + count.incrementAndGet() + " 次调用,时间："+ TimeUtil.getDayTime());
//        });
//
//        // 开始按钮事件
//        runButton.addActionListener((e) -> {
//
//            if (runButton.getText().equals("开始")){
//                if (!isTextNotNull()){
//                    logUtil.error("  cookie或email地址不能为空 ");
//                    return;
//                }
//                // 开始首先检测Cookie
//                String cookie = cookieText.getText();
//                List<Map<String,String>> rmsDataArrays = rmsHandler.getBusinessAlarms(cookie);
//                List<Map<String,String>> zabbixDataArrays = zabbixHandler.getData();
//
//                if (rmsDataArrays == null){
//                    logUtil.error("  rms网络异常或Cookie失效");
//                    return;
//                }
//                if (zabbixDataArrays == null){
//                    logUtil.error("  zabbix网络异常或Cookie失效");
//                    return;
//                }
//                logUtil.info("  测试获取数据成功,正在启动监控");
//                saveAccount();
//
//                cookieText.setEnabled(false);
//                emailText.setEnabled(false);
//                runButton.setText("正在运行");
//                timer.start();
//                logUtil.info("  监控运行中, 当前每 " + delay/1000 + "s 监控一次");
//                statusLabel.setText("状态：定时器已启动,等待触发");
//            }else {
//                runButton.setText("开始");
//                cookieText.setEnabled(true);
//                emailText.setEnabled(true);
//                timer.stop();
//                logUtil.info("  监控暂停");
//                statusLabel.setText("状态：无");
//            }
//
//        });
//    }
//
//    public static boolean isTextNotNull(){
//        if (cookieText.getText() == null || cookieText.getText().equals("")) return false;
//        if (emailText.getText() == null || emailText.getText().equals("")) return false;
//        return true;
//    }
//
//    public static void saveAccount(){
//        String content = cookieText.getText()+ "\n" + emailText.getText();
//        CommonUtil.writeStr(ACCOUNT_FILE_PATH, content, false);
//    }
//
//    public static void getAccount(){
//        String content = CommonUtil.readStr(ACCOUNT_FILE_PATH);
//        String[] lines = content.split("\n");
//        if (lines.length >= 2){
//            String cookie = lines[0];
//            String email = lines[1];
//            cookieText.setText(cookie);
//            emailText.setText(email);
//        }else {logUtil.info("[error] -> 读取账号信息失败");}
//
//    }
//
//    public static JTextArea getLogText(){
//        return logText;
//    }
//}
