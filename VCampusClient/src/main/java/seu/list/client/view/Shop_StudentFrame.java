package seu.list.client.view;

import seu.list.client.driver.Client;
import seu.list.client.driver.ClientMainFrame;
import seu.list.common.Goods;
import seu.list.common.Message;
import seu.list.common.MessageType;
import seu.list.common.ModuleType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Shop_StudentFrame {

    private JFrame frame;
    private JTextField textField;
    private JTable table;
    private JButton btnNewButton;
    private JButton btnNewButton_1;
    private JTextField SearchText;
    private JButton btnNewButton_2;
    private JScrollPane scrollPane;

    //总价
    private double sum = 0.0;
    private String id = null;
    private String PWD = null;

    private MainMenu Mainmenu = null;

    public Shop_StudentFrame(String id, String PWD, MainMenu mainmenu) {
        this.Mainmenu = mainmenu;
        this.id = id;
        this.PWD = PWD;
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        sum = 0.0;
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 532);
        //设置背景图片
        JLabel backgroundImageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/Shop_StudentFrame.png")));
        Toolkit k = Toolkit.getDefaultToolkit();
        Dimension d = k.getScreenSize();
        frame.setBounds(d.width / 2 - 640, d.height / 2 - 360, 1280, 720);
        backgroundImageLabel.setBounds(0, 0, 1280, 720);
        frame.setSize(1280, 760 - 3 - 3);
        frame.setTitle("校园超市");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        //主frame

        //金额显示框
        textField = new JTextField();
        textField.setBounds(723 + 2, 157, 627 - 348, 199 - 157);
        textField.setFont(new Font("华文行楷", Font.BOLD, 36));
        textField.setEnabled(false);
        textField.setText(sum + "");
        frame.add(textField);
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        textField.setOpaque(false);


        //搜索框
        SearchText = new JTextField("请输入商品名称");
        SearchText.setBounds(348, 157, 627 - 348, 199 - 157);
        SearchText.setFont(new Font("华文行楷", Font.BOLD, 36));
        frame.add(SearchText);
        SearchText.setBorder(new EmptyBorder(0, 0, 0, 0));
        SearchText.setOpaque(false);


        //显示商品的表格
        scrollPane = new JScrollPane();
        scrollPane.setEnabled(false);
        scrollPane.setBounds(265, 225, 1100 - 265 + 10, 566 - 260 + 15 + 15 + 10);

        //表格放入带滑动条的容器中
        table = new JTable();
        table.setBackground(Color.WHITE);
        scrollPane.setViewportView(table);
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setRowHeight(25);
        show();
        table.getColumnModel().getColumn(3).setPreferredWidth(79);
        table.setBounds(0, 0, 1100 - 265 + 10, 566 - 260 + 15 + 15 + 10);
        MyCellEditor cellEditor = new MyCellEditor(new JTextField());
        TableColumn tableColumn = table.getColumn("购买数量");
        tableColumn.setCellEditor(cellEditor);//确保输入合法
        table.getColumnModel().getColumn(3).setPreferredWidth(79);
        table.getTableHeader().setReorderingAllowed(false);
        final TableModel tableModel = table.getModel();
        //表格显示商品信息


        tableModel.addTableModelListener(e -> {
                    int firstRow = e.getFirstRow();
                    int lastRow = e.getLastRow();
                    int column = e.getColumn();
                    int type = e.getType();
                    if (type == TableModelEvent.UPDATE) {
                        sum = 0.0;
                        if (column == 4) {
                            for (int row = 0; row < table.getRowCount(); row++) {
                                Object tempnumber = tableModel.getValueAt(row, 4);
                                Object tempprice = tableModel.getValueAt(row, 2);
                                double tem = Double.parseDouble((String) tempprice);
                                int tem1 = Integer.parseInt((String) tempnumber);
                                sum += tem * tem1;

                            }
                            textField.setText(sum + "");
                        } else return;
                    }
                }//表格增加监听，修改信息时需确认
        );
          table.setEnabled(false);
        //透明化处理
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 28));
        table.setRowHeight(40);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {
                "商品编号", "商品名称", "单价", "库存", "购买数量"};
        for (int i = 0; i < 5; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getColumnHeader().setOpaque(false);


        frame.add(scrollPane);
        frame.add(backgroundImageLabel);
        //结结账按钮
        btnNewButton = new JButton("");
        btnNewButton.setBounds(112, 316, 216 - 113 + 8, 356 - 316);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setPayFrame();
            }
        });
        frame.add(btnNewButton);
        btnNewButton.setOpaque(false);
        //购买界面确认需输入密码

        //退出按钮
        btnNewButton_1 = new JButton("");
        //btnNewButton_1.setBounds(10, 308, 60, 25);
        btnNewButton_1.setBounds(112, 462, 216 - 113 + 8, 356 - 316);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        btnNewButton_1.setOpaque(false);
        btnNewButton_1.setFont(new Font("微软雅黑", Font.BOLD, 20));
        //退出
        frame.add(btnNewButton_1);


        //搜索按钮
        btnNewButton_2 = new JButton("");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchGood(e);
            }
        });
        btnNewButton_2.setOpaque(false);

        btnNewButton_2.setBounds(285, 156, 338 - 285, 199 - 157);
        frame.add(btnNewButton_2);

        //购物车按钮
        btnNewButton_2 = new JButton("购物车");
        btnNewButton_2.setOpaque(false);
        btnNewButton_2.setBounds(865,591,1024-865,645-591);
        frame.add(btnNewButton_2);
        btnNewButton_2.addActionListener(event->
        {
            JFrame Car = new JFrame("购物车");
            JLabel back = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/ShaopCar.jpg")));
            Car.setBounds(d.width / 2 - 441, d.height / 2 - 649/2, 882,649+25);
            back.setBounds(0, 0, 882,649);
            Car.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            Car.setResizable(false);
            Car.setLayout(null);

             //购物车清单表格
            DefaultTableModel tablemodel=updateCar();
            JTable table2=new JTable(tablemodel);
            JScrollPane scrollPane=new JScrollPane(table2);
            table2.setBounds(0,0,763-146,470-127);
            scrollPane.setBounds(147,127,763-146,470-127);
            Car.setVisible(true);
            Car.add(scrollPane);
            Trans(table2,scrollPane);
            Car.add(back);
            table2.setEnabled(false);




          //鼠标定位
         /*   back.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    System.out.println("鼠标点击位置：X=" + x + ", Y=" + y);
                }
            });*/

            //购入按钮
            JButton add=new JButton("购入");
            add.setOpaque(false);
            add.setBounds(173,542,288-173,594-542);
            Car.add(add);
            add.setOpaque(false);
            add.addActionListener(event2->
            {
                JFrame GoodsAdd = new JFrame("商品购入");
                JLabel backadd = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/GoodsAdd.jpg")));
                GoodsAdd.setBounds(d.width / 2 - 845 / 2, d.height / 2 - 588 / 2, 845, 588 + 25);
                backadd.setBounds(0, 0, 845, 588);
                GoodsAdd.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                GoodsAdd.setResizable(false);
                GoodsAdd.setLayout(null);
                GoodsAdd.setVisible(true);

                //鼠标定位
          /* backadd.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    System.out.println("鼠标点击位置：X=" + x + ", Y=" + y);
                }
            });*/

                //商品编号输入框
                JTextField number=new JTextField("");
                Font f=new Font("华文行楷",Font.BOLD,30);
                number.setFont(f);
                number.setBounds(335,176,697-335,218-176);
                GoodsAdd.add(number);
                number.setOpaque(false);
                number.setBorder(new EmptyBorder(0,0,0,0));


                //商品数量显示框
                JTextField num=new JTextField("0");
                num.setFont(f);
                num.setBounds(431,285,607-431,330-285);
                num.setHorizontalAlignment(SwingConstants.CENTER);
                num.setOpaque(false);
                num.setBorder(new EmptyBorder(0,0,0,0));
                num.setEnabled(false);
                GoodsAdd.add(num);
                GoodsAdd.add(backadd);

                //增加按钮
                JButton a=new JButton("曾");
                a.setBounds(633,278,406-350,332-280);
                GoodsAdd.add(a);
                a.addActionListener(cc->
                {
                    int n=Integer.parseInt(num.getText());
                    n++;
                    num.setText(n+"");

                });

                a.setOpaque(false);
                //减少按钮
                JButton b=new JButton("键");
                b.setBounds(350,280,406-350,332-280);
                GoodsAdd.add(b);
                b.addActionListener(bb->
                {
                    int n=Integer.parseInt(num.getText());
                    if(n==0)
                    {
                        JOptionPane.showMessageDialog(
                               null,
                                "商品数量不可为负数！",
                                "警告",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                    else
                    {
                        n--;
                        num.setText(n+"");
                    }
                });
               b.setOpaque(false);

                //保存按钮
                JButton save=new JButton("Save");
                save.setBounds(234,435,337-234,495-435);
                GoodsAdd.add(save);
                save.addActionListener(dd->
                {
                    if(number.getText().trim().equals(""))
                    {
                        JOptionPane.showMessageDialog(
                                null,
                                "商品编号不可为空！",
                                "警告",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                    else if(num.getText().equals("0"))
                    {
                        JOptionPane.showMessageDialog(
                                null,
                                "商品数量不可为0！",
                                "警告",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                    else {  //添加成功
                        int zz=0;
                        String str=number.getText();
                        for(;zz<table.getRowCount();zz++)
                        {
                            if(table.getValueAt(zz,0).toString().equals(str))
                                break;
                        }
                        if(zz==table.getRowCount())
                        {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "请输入正确的商品编号",
                                    "警告",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }
                        else
                        {

                            String s=num.getText().toString();
                            table.setValueAt(s,zz,4);
                            DefaultTableModel m=updateCar();
                            table2.setModel(m);
                            Trans(table2,scrollPane);
                            GoodsAdd.dispose();
                        }
                    }
                });
                save.setOpaque(false);
                //取消按钮
                JButton Cancel=new JButton("取消");
                Cancel.setBounds(504,436,337-234,495-435);
                GoodsAdd.add(Cancel);
                Cancel.addActionListener(aa->
                {
                    GoodsAdd.dispose();
                });
                Cancel.setOpaque(false);

            });

            //修改按钮
            JButton motify=new JButton("修改");
            add.setOpaque(false);
            motify.setBounds(381,541,288-173,594-542);
            Car.add(motify);
            motify.setOpaque(false);
            motify.addActionListener(dd->
            {
                JFrame GoodsAdd = new JFrame("商品修改");
                JLabel backadd = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/GoodsMotify.jpg")));
                GoodsAdd.setBounds(d.width / 2 - 845/2, d.height / 2 - 588/2, 845,588+25);
                backadd.setBounds(0, 0, 845,588);
                GoodsAdd.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                GoodsAdd.setResizable(false);
                GoodsAdd.setLayout(null);
                GoodsAdd.setVisible(true);

                //鼠标定位
          /* backadd.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    System.out.println("鼠标点击位置：X=" + x + ", Y=" + y);
                }
            });*/

                //商品编号输入框
                JTextField number=new JTextField("");
                Font f=new Font("华文行楷",Font.BOLD,30);
                number.setFont(f);
                number.setBounds(321,169,582-321,221-169);
                GoodsAdd.add(number);
                number.setOpaque(false);
                number.setBorder(new EmptyBorder(0,0,0,0));


                //商品数量显示框
                JTextField num=new JTextField("0");
                num.setFont(f);
                num.setBounds(430,296,631-430,345-296);
                num.setHorizontalAlignment(SwingConstants.CENTER);
                num.setOpaque(false);
                num.setBorder(new EmptyBorder(0,0,0,0));
                num.setEnabled(false);
                GoodsAdd.add(num);
                GoodsAdd.add(backadd);

                //增加按钮
                JButton a=new JButton("曾");
                a.setBounds(657,285,720-657,349-285);
                GoodsAdd.add(a);
                a.addActionListener(cc->
                {
                    int n=Integer.parseInt(num.getText());
                    n++;
                    num.setText(n+"");

                });
              //刷新按钮
                JButton flash=new JButton("刷新");
                flash.setBounds(606,171,100,222-171);
                GoodsAdd.add(flash);
                flash.setOpaque(false);
                AtomicInteger flag= new AtomicInteger(-1);//代表当前选择商品的行数
                flash.addActionListener(ee->
                {
                    for(int i=0;i<table2.getRowCount();i++)
                    {
                        if(table2.getValueAt(i,0).toString().equals(number.getText()))
                        {
                            flag.set(i);
                            break;
                        }
                    }
                    if(flag.get()==-1)
                    {
                        JOptionPane.showMessageDialog(
                                null,
                                "请输入正确的购物车中的商品编号！",
                                "警告",
                                JOptionPane.WARNING_MESSAGE
                        );
                        number.setText("");
                    }
                    else
                       num.setText(table2.getValueAt(flag.get(),3).toString());
                });
                int t=flag.get();
                a.setOpaque(false);
                //减少按钮
                JButton b=new JButton("键");
                b.setBounds(341,287,402-341,349-287);
                GoodsAdd.add(b);
                b.setOpaque(false);
                b.addActionListener(bb->
                {
                    int n=Integer.parseInt(num.getText());
                    if(n==0)
                    {
                        JOptionPane.showMessageDialog(
                                null,
                                "商品数量不可为负数！",
                                "警告",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                    else
                    {
                        n--;
                        num.setText(n+"");
                    }
                });
                //b.setOpaque(false);

                //保存按钮
                JButton save=new JButton("Save");
                save.setBounds(209,466,327-209,530-466);
                GoodsAdd.add(save);
                save.addActionListener(ff->
                {
                    if(number.getText().trim().equals(""))
                    {
                        JOptionPane.showMessageDialog(
                                null,
                                "商品编号不可为空！",
                                "警告",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                    else {  //添加成功
                        int zz=0;
                        String str=number.getText();
                        for(;zz<table.getRowCount();zz++)
                        {
                            if(table.getValueAt(zz,0).toString().equals(str))
                                break;
                        }
                        if(zz==table.getRowCount())
                        {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "请输入正确的商品编号",
                                    "警告",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }
                        else
                        {

                            String s=num.getText().toString();
                            table.setValueAt(s,zz,4);
                            DefaultTableModel m=updateCar();
                            table2.setModel(m);
                            Trans(table2,scrollPane);
                            GoodsAdd.dispose();
                        }
                    }
                });
                save.setOpaque(false);
                //取消按钮
                JButton Cancel=new JButton("取消");
                Cancel.setBounds(510,464,628-510,529-464);
                GoodsAdd.add(Cancel);
                Cancel.addActionListener(aa->
                {
                    GoodsAdd.dispose();
                });
                Cancel.setOpaque(false);


            });

            //取消按钮
            JButton no=new JButton("退出");
            add.setOpaque(false);
            no.setBounds(597,543,288-173,594-542);
            Car.add(no);
            no.setOpaque(false);
            no.addActionListener(event1->
            {
                //1.捕获当前购物车数量更新到商店表格里面，并且计算总金额多少
                int j=0;
                Double Sum=0.0;
                for (int i=0;i<table2.getRowCount();i++)
                {

                    while(!(table.getValueAt(j,0).toString().equals(table2.getValueAt(i,0).toString())))
                    {

                        j++;
                    }

                    Sum=Sum+ Double.parseDouble(table2.getValueAt(i,3).toString())*Double.parseDouble( table2.getValueAt(i,2).toString());
                    table.setValueAt(table2.getValueAt(i,3),j,4);
                    j++;
                }
                textField.setText(Sum+"");
               //更新总金额显示栏
                //2.购物车窗口销毁
                Car.dispose();
            });
        });
    }

    void Trans(JTable table, JScrollPane scrollPane)  //透明化处理函数
    {
        //透明化处理
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 28));
        table.setRowHeight(40);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {
                "商品编号", "商品名称", "单价", "选购数量"};
        for (int i = 0; i < 4; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getColumnHeader().setOpaque(false);
    }

    /**
     * 将商品信息显示到表格中
     */
    DefaultTableModel updateCar()
    {
        //选购商品清单表格
        DefaultTableModel  tablemodel = new DefaultTableModel(new Object[][]{}, new String[]{
                "商品编号", "商品名称", "单价", "选购数量"});
        for(int i=0;i<table.getRowCount();i++)
        {
            if(!table.getValueAt(i,4).toString().equals("0"))
            {
                String tempgoods[] = new String[4];
                tempgoods[0] = table.getValueAt(i,0) + "";
                tempgoods[1] = table.getValueAt(i,1)+"";
                tempgoods[2] = table.getValueAt(i,2) + "";
                tempgoods[3] = table.getValueAt(i,4) + "";
                tablemodel.addRow(tempgoods);
            }
        }
        return tablemodel;
    }


    public void show() {
        Message mes = new Message();
        mes.setMessageType(MessageType.Goodsgetall);
        mes.setModuleType(ModuleType.Shop);
        Client client = new Client(ClientMainFrame.socket);
        Message serverResponse = client.sendRequestToServer(mes);
        ArrayList<Goods> Goodslist = (ArrayList<Goods>) serverResponse.getData();
        DefaultTableModel tablemodel;

        tablemodel = new DefaultTableModel(new Object[][]{}, new String[]{
                "商品编号", "商品名称", "单价", "库存", "购买数量"}) {


            boolean[] columnEditables = new boolean[]{
                    false, false, false, false,true
            };

            @Override
            public boolean isCellEditable(int row, int column) {

                return columnEditables[column];
            }
        };
        for (int i = 0; i < Goodslist.size(); i++) {
            String tempgoods[] = new String[5];
            tempgoods[0] = Goodslist.get(i).getGoodsid() + "";
            tempgoods[1] = Goodslist.get(i).getGoodsname();
            tempgoods[2] = Goodslist.get(i).getGoodsprice() + "";
            tempgoods[3] = Goodslist.get(i).getGoodsnumber() + "";
            tempgoods[4] = "0";
            tablemodel.addRow(tempgoods);
        }

        MyCellEditor cellEditor = new MyCellEditor(new JTextField());

        table.setModel(tablemodel);
        final TableModel tableModel = table.getModel();
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // 第一个 和 最后一个 被改变的行（只改变了一行，则两者相同）
                int firstRow = e.getFirstRow();
                int lastRow = e.getLastRow();

                // 被改变的列
                int column = e.getColumn();
                int type = e.getType();
                if (type == TableModelEvent.UPDATE) {
//		        	double t=0.0;
                    sum = 0.0;
                    if (column == 4) {
                        for (int row = 0; row < table.getRowCount(); row++) {
                            Object tempnumber = tableModel.getValueAt(row, 4);
                            Object tempprice = tableModel.getValueAt(row, 2);
                            double tem = Double.parseDouble((String) tempprice);
                            int tem1 = Integer.parseInt((String) tempnumber);
//		        			t+=tem*tem1;
                            sum += tem * tem1;

                        }
//		        		 textField.setText(t+"");
                        textField.setText(sum + "");
                    } else return;
                }
            }
        });
        TableColumn tableColumn = table.getColumn("购买数量");
        tableColumn.setCellEditor(cellEditor);//确保输入合法

        //透明化处理
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 28));
        table.setRowHeight(40);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {
                "商品编号", "商品名称", "单价", "库存", "购买数量"};
        for (int i = 0; i < 5; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getColumnHeader().setOpaque(false);
    }

    /**
     * 查询操作
     *
     * @param e 事件
     */
    private void SearchGood(ActionEvent e) {
        // TODO 自动生成的方法存根
        Message mes = new Message();
        mes.setData(SearchText.getText());
        mes.setModuleType(ModuleType.Shop);
        if (((String) SearchText.getText()).equals("")) {
            show();
            return;
        }
        if (SearchText.getText().matches("[0-9]*")) {//商品ID查找
            mes.setMessageType(MessageType.GoodsSearch_ID);

            Client client = new Client(ClientMainFrame.socket);
            Message serverResponse = client.sendRequestToServer(mes);
            ArrayList<Goods> res = (ArrayList<Goods>) serverResponse.getData();

            DefaultTableModel tablemodel;
            tablemodel = new DefaultTableModel(new Object[][]{}, new String[]{
                    "商品编号", "商品名称", "单价", "库存", "购买数量"}) {


                /*
                 * overload the method to change the table's factor
                 */
                boolean[] columnEditables = new boolean[]{
                        false, false, false, false, true
                };

                @Override
                public boolean isCellEditable(int row, int column) {

                    return columnEditables[column];
                }
            };

            for (int i = 0; i < res.size(); i++) {
                String tempgoods[] = new String[5];
                tempgoods[0] = res.get(i).getGoodsid() + "";
                tempgoods[1] = res.get(i).getGoodsname();
                tempgoods[2] = res.get(i).getGoodsprice() + "";
                tempgoods[3] = res.get(i).getGoodsnumber() + "";
                tempgoods[4] = "0";
                tablemodel.addRow(tempgoods);
            }
            MyCellEditor cellEditor = new MyCellEditor(new JTextField());

            table.setModel(tablemodel);

            final TableModel tableModel = table.getModel();
            tableModel.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    // 第一个 和 最后一个 被改变的行（只改变了一行，则两者相同）
                    int firstRow = e.getFirstRow();
                    int lastRow = e.getLastRow();

                    // 被改变的列
                    int column = e.getColumn();
                    int type = e.getType();
                    if (type == TableModelEvent.UPDATE) {
//		        	double t=0.0;
                        sum = 0.0;
                        if (column == 4) {
                            for (int row = 0; row < table.getRowCount(); row++) {
                                Object tempnumber = tableModel.getValueAt(row, 4);
                                Object tempprice = tableModel.getValueAt(row, 2);
                                double tem = Double.parseDouble((String) tempprice);
                                int tem1 = Integer.parseInt((String) tempnumber);
//		        			t+=tem*tem1;
                                sum += tem * tem1;

                            }
//		        		 textField.setText(t+"");
                            textField.setText(sum + "");
                        } else return;
                    }
                }
            });
            TableColumn tableColumn = table.getColumn("购买数量");
            tableColumn.setCellEditor(cellEditor);//确保输入合法


        } else {
            mes.setMessageType(MessageType.GoodsSearch_Name);

            Client client = new Client(ClientMainFrame.socket);
            Message serverResponse = client.sendRequestToServer(mes);
            ArrayList<Goods> res = (ArrayList<Goods>) serverResponse.getData();

            DefaultTableModel tablemodel;
            tablemodel = new DefaultTableModel(new Object[][]{}, new String[]{
                    "商品编号", "商品名称", "单价", "库存", "购买数量"}) {


                /*
                 * overload the method to change the table's factor
                 */
                boolean[] columnEditables = new boolean[]{
                        false, false, false, false, true
                };

                @Override
                public boolean isCellEditable(int row, int column) {

                    return columnEditables[column];
                }
            };
            for (int i = 0; i < res.size(); i++) {
                String tempgoods[] = new String[5];
                tempgoods[0] = res.get(i).getGoodsid() + "";
                tempgoods[1] = res.get(i).getGoodsname();
                tempgoods[2] = res.get(i).getGoodsprice() + "";
                tempgoods[3] = res.get(i).getGoodsnumber() + "";
                tempgoods[4] = "0";
                tablemodel.addRow(tempgoods);
            }
            MyCellEditor cellEditor = new MyCellEditor(new JTextField());

            table.setModel(tablemodel);

            final TableModel tableModel = table.getModel();
            tableModel.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    // 第一个 和 最后一个 被改变的行（只改变了一行，则两者相同）
                    int firstRow = e.getFirstRow();
                    int lastRow = e.getLastRow();

                    // 被改变的列
                    int column = e.getColumn();
                    int type = e.getType();
                    if (type == TableModelEvent.UPDATE) {
//			        	double t=0.0;
                        sum = 0.0;
                        if (column == 4) {
                            for (int row = 0; row < table.getRowCount(); row++) {
                                Object tempnumber = tableModel.getValueAt(row, 4);
                                Object tempprice = tableModel.getValueAt(row, 2);
                                double tem = Double.parseDouble((String) tempprice);
                                int tem1 = Integer.parseInt((String) tempnumber);
//			        			t+=tem*tem1;
                                sum += tem * tem1;

                            }
//			        		 textField.setText(t+"");
                            textField.setText(sum + "");
                        } else return;
                    }
                }
            });
            TableColumn tableColumn = table.getColumn("购买数量");
            tableColumn.setCellEditor(cellEditor);//确保输入合法


        }
        //透明化处理
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 28));
        table.setRowHeight(40);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {
                "商品编号", "商品名称", "单价", "库存", "购买数量"};
        for (int i = 0; i < 5; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getColumnHeader().setOpaque(false);
    }

    private JTable getTable() {
        return null;
    }

    /**
     * 购买操作
     */
    protected void buy() {
        Mainmenu.set(sum);
        Message mes = new Message();
        mes.setModuleType(ModuleType.Shop);
        mes.setMessageType(MessageType.Buy);
        ArrayList<String> bgoods = new ArrayList<String>();
        for (int i = 0; i < table.getRowCount(); i++) {
            String id = (String) table.getValueAt(i, 0);
            String number = (String) table.getValueAt(i, 4);
            int temp = Integer.parseInt(number);
            if (temp != 0) {
                bgoods.add(id);
                bgoods.add(number);
            }
        }
        mes.setData(bgoods);
        mes.setModuleType(ModuleType.Shop);
        Client client = new Client(ClientMainFrame.socket);

        Message serverResponse = client.sendRequestToServer(mes);
        //int res=(int)serverResponse.getData();
        textField.setText("0.0");
    }

    /**
     * 购买按钮的响应
     */
    void setPayFrame() {
        frame.setEnabled(false);
        frame.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
        Shop_StudentForPay newframe = new Shop_StudentForPay(this, frame, sum, id, PWD);
        newframe.setVisible(true);
    }

    /**
     * 保证表格改动时数据合法
     */
    public static class MyCellEditor extends DefaultCellEditor {

        public MyCellEditor(JTextField textField) {
            super(textField);
        }

        @Override
        public boolean stopCellEditing() {
            // 获取当前单元格的编辑器组件
            Component comp = getComponent();

            // 获取当前单元格编辑器输入的值
            Object obj = getCellEditorValue();

            // 如果当前单元格编辑器输入的值不是数字，则返回 false（表示数据非法，不允许设置，无法保存）
            if (obj == null || !obj.toString().matches("[0-9]*")) {
                // 数据非法时，设置编辑器组件内的内容颜色为红色
                comp.setForeground(Color.RED);
                return false;
            }

            // 数据合法时，设置编辑器组件内的内容颜色为黑色
            comp.setForeground(Color.BLACK);

            // 合法数据交给父类处理
            return super.stopCellEditing();
        }
    }
}
