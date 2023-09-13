package seu.list.client.view;

import seu.list.client.driver.Client;
import seu.list.common.Course;
import seu.list.common.Message;
import seu.list.common.ModuleType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class CourseInfor extends JDialog implements ActionListener {
    private JTextField Semester;
    private JTextField CourseID;
    private JTextField CourseMajor;
    private JTextField CourseName;
    private JTextField teacherID;
    private JTextField CourseState;
    private JTextField CourseType;
    private Socket socket;
    private String userID;

    public CourseInfor(String ID, Socket socket, ClientCourseFrame tem) {
        userID = ID;
        this.socket = socket;
        tem.dispose();

        Client client = new Client(this.socket);
        //绘制背景图片
        JLabel backgroundImageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/CourseInfor.png")));
        Toolkit k = Toolkit.getDefaultToolkit();
        Dimension d = k.getScreenSize();
        setBounds(d.width / 2 - 844 / 2, d.height / 2 - 650 / 2, 844, 650);
        backgroundImageLabel.setBounds(0, 0, 844, 650);
        setResizable(false);
        setLayout(null);

        setTitle("添加课程");
        Font f = new Font("华文楷体", Font.BOLD, 36);

        JComboBox modifyt1 = new JComboBox<>();
        modifyt1.setModel(new DefaultComboBoxModel(new String[]{"周一", "周二", "周三", "周四", "周五"}));
        modifyt1.setFont(new Font("华文行楷", Font.PLAIN, 24));
        modifyt1.setBounds(502, 305, 110, 43);
        add(modifyt1);
        modifyt1.setOpaque(false);
        modifyt1.setBorder(new EmptyBorder(0, 0, 0, 0));
        modifyt1.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JComponent result = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                result.setOpaque(false);
                return result;
            }
        });

        JComboBox modifyt2 = new JComboBox<>();
        modifyt2.setModel(new DefaultComboBoxModel(new String[]{"1-2节", "3-4节", "5-6节", "7-8节"}));
        modifyt2.setFont(new Font("华文行楷", Font.PLAIN, 24));
        modifyt2.setBounds(633, 305, 110, 43);
        add(modifyt2);
        modifyt2.setOpaque(false);
        modifyt2.setBorder(new EmptyBorder(0, 0, 0, 0));
        modifyt2.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JComponent result = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                result.setOpaque(false);
                return result;
            }
        });

        CourseID = new JTextField();
        CourseID.setFont(f);
        CourseID.setBounds(166, 136, 241, 43);
        add(CourseID);
        CourseID.setOpaque(false);
        CourseID.setBorder(new EmptyBorder(0, 0, 0, 0));

        CourseName = new JTextField();
        CourseName.setFont(f);
        CourseName.setBounds(502, 136, 241, 43);
        add(CourseName);
        CourseName.setOpaque(false);
        CourseName.setBorder(new EmptyBorder(0, 0, 0, 0));


        CourseMajor = new JTextField();
        CourseMajor.setFont(f);
        CourseMajor.setBounds(166, 219, 241, 43);
        add(CourseMajor);
        CourseMajor.setOpaque(false);
        CourseMajor.setBorder(new EmptyBorder(0, 0, 0, 0));


        teacherID = new JTextField();
        teacherID.setFont(f);
        teacherID.setBounds(502, 219, 241, 43);
        add(teacherID);
        teacherID.setOpaque(false);
        teacherID.setBorder(new EmptyBorder(0, 0, 0, 0));


        Semester = new JTextField();
        Semester.setFont(f);
        Semester.setBounds(166, 405, 241, 43);
        add(Semester);
        Semester.setOpaque(false);
        Semester.setBorder(new EmptyBorder(0, 0, 0, 0));


        CourseState = new JTextField();
        CourseState.setFont(f);
        CourseState.setBounds(166, 305, 241, 43);
        add(CourseState);
        CourseState.setOpaque(false);
        CourseState.setBorder(new EmptyBorder(0, 0, 0, 0));


        CourseType = new JTextField();
        CourseType.setFont(f);
        CourseType.setBounds(502, 405, 241, 43);
        add(CourseType);
        CourseType.setOpaque(false);
        CourseType.setBorder(new EmptyBorder(0, 0, 0, 0));

        add(backgroundImageLabel);

        JButton confirmButtom = new JButton("确定");
        confirmButtom.setFont(new Font("微软雅黑", Font.BOLD, 20));
        confirmButtom.setBounds(251, 500, 100, 53);
        confirmButtom.addActionListener(this);
        confirmButtom.setActionCommand("confirm");
        add(confirmButtom);
        confirmButtom.setOpaque(false);


        JButton exit = new JButton("退出");
        exit.setBounds(517, 500, 100, 53);
        add(exit);
        exit.setOpaque(false);
        exit.addActionListener(event ->
        {
            try {
                this.dispose();
                ClientCourseFrame ccf = new ClientCourseFrame(userID, this.socket);
            } catch (ClassNotFoundException | SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("confirm")) {
            Client client = new Client(this.socket);
            Course course = new Course();
            course.setCourseID(CourseID.getText());
            course.setCourseName(CourseName.getText());
            course.setCourseMajor(CourseMajor.getText());
            course.setTeacherID(teacherID.getText());
            course.setSemester(Semester.getText());
            course.setCourseType(CourseType.getText());
            Message clientReq = new Message();
            clientReq.setModuleType(ModuleType.Course);
            clientReq.setMessageType("REQ_ADD_LESSON");
            clientReq.setContent(course.getContent());
            Message rec = client.sendRequestToServer(clientReq);
            if (rec.isSeccess()) {
                try {
                    ClientCourseFrame ccf = new ClientCourseFrame(userID, this.socket);
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "课程id不可重复，请重新填写", "错误", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
}
