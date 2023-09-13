package seu.list.client.view;

import seu.list.client.driver.Client;
import seu.list.client.driver.ClientMainFrame;
import seu.list.common.Course;
import seu.list.common.Message;
import seu.list.common.MessageType;
import seu.list.common.ModuleType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class Schedule extends JFrame {
    private static final long serialVersionUID = 1L;
    JFrame jframe = new JFrame();
    JButton exitbutton;
    JTextField jtf1, jtf2;
    JTable table;
    JScrollPane scrollPane2;
    Socket socket;//传送数据
    private ClientStuCourseFrame cli;

    public Schedule(ClientStuCourseFrame cli) {
        setTitle("课程表");
        setDefaultCloseOperation(2);


        //设置背景图片
        //把图片添加到标签里（把标签的大小设为和图片大小相同），把标签放在分层面板的最底层；
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null); // 使用绝对定位
        // 创建带有背景图片的JLabel
        ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("imgs/Schedule.png"));
        JLabel backlabel = new JLabel(image);
        //获取当前屏幕的尺寸（长、宽的值）
        Toolkit k = Toolkit.getDefaultToolkit();
        Dimension d = k.getScreenSize();
        //将当前窗口设置到屏幕正中央进行显示
        setBounds(d.width / 2 - 1280 / 2, d.height / 2 - 720 / 2, 1280, 720 + 25);
        backlabel.setSize(1280, 720);
        this.getLayeredPane().add(backlabel, new Integer(Integer.MIN_VALUE));
        backlabel.setOpaque(false); // 设置背景透明
        setResizable(false); //阻止用户拖拽改变窗口的大小
        setVisible(true);

        //设置表格

        ArrayList<Course> timetable = new ArrayList<Course>();

        Message mes = new Message();
        mes.setUserType(0);
        Client client = new Client(ClientMainFrame.socket);

        mes.setModuleType(ModuleType.Course);
        mes.setMessageType(MessageType.REQ_STU_ALL_CHOOOSE);
        mes.setData(cli.userID);
        Message rec = client.sendRequestToServer(mes);

        Vector<String> allCourseContents = rec.getContent();
        Object[] sigRow = new String[7];
        Object[][] ak = new String[4][7];
        System.out.println(allCourseContents.size());
        int max = allCourseContents.size() / 8;
        /*for(int i=0;i<allCourseContents.size();i++)
            System.out.println(allCourseContents.get(i));*/
        for (int i = 0; i < max; i++) {

            int period = 0;
            String p = allCourseContents.get(8 * i + 5);
            int date = 6;
            if (Objects.equals(p, "1-2节"))
                period = 0;
            else if (Objects.equals(p, "3-4节"))
                period = 1;
            else if (Objects.equals(p, "5-6节"))
                period = 2;
            else if (Objects.equals(p, "7-8节"))
                period = 3;

            String s = allCourseContents.get(8 * i + 7);
            String j;
            if (Objects.equals(s, "周一"))
                date = 0;
            else if (Objects.equals(s, "周二"))
                date = 1;
            else if (Objects.equals(s, "周三"))
                date = 2;
            else if (Objects.equals(s, "周四"))
                date = 3;
            else
                date = 4;

            System.out.println(s);
            System.out.println(period);
            System.out.println(date);
            System.out.println("--------------------------------------");
            if ((period < 5) && (date < 7))
                ak[period][date] = "<html>" + allCourseContents.get(8 * i + 3) + "<br>" + "1-16周" + p + "<br>" + allCourseContents.get(8 * i + 4) + "</html>";
        }

        //生成表格
        Object[][] courseinformation = {};
        Object[] courselist = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        DefaultTableModel model2;
        model2 = new DefaultTableModel(ak, courselist);
        table = new JTable();
        table.setModel(model2);

        //滚动框框位置调整
        scrollPane2 = new JScrollPane();
        scrollPane2.setOpaque(false);
        scrollPane2.getViewport().setOpaque(false);
        scrollPane2.setViewportView(table);
        table.setBounds(0, 0, 1157 - 165, 661 - 104);
        add(scrollPane2);
        scrollPane2.setBounds(287, 104, 1157 - 287, 661 - 104);
        scrollPane2.setVisible(true);
        setVisible(true);


        //透明化
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 20));
        table.getTableHeader().setFont(new Font("华文楷体", Font.BOLD, 20));
        table.setRowHeight(132);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {
                "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (int i = 0; i < 7; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane2.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane2.setOpaque(false);
        scrollPane2.getViewport().setOpaque(false);
        scrollPane2.setColumnHeaderView(table.getTableHeader());
        scrollPane2.getColumnHeader().setOpaque(false);

        add(scrollPane2);

        add(backlabel);


        exitbutton = new JButton("取消");
        exitbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddqxAvt(e);
            }
        });
        exitbutton.setFont(new Font("华文行楷", Font.BOLD, 29));
        exitbutton.setBounds(1024, 29, 1155 - 1024, 85 - 29);
        exitbutton.setVisible(true);
        add(exitbutton);
        exitbutton.setOpaque(false);
    }

    protected void AddqxAvt(ActionEvent e) {
        dispose();
    }


}
