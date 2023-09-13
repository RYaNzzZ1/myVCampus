package seu.list.server.dao;

import seu.list.common.Chat;
import seu.list.common.Course;
import seu.list.common.Student;
import seu.list.common.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOUtil {
    /**
     * @param rs 从数据库获取的课程结果
     * @return 课程信息列表
     */
    public static List<Course> CourseResultSet2List(ResultSet rs) throws SQLException {
        List<Course> courses = new ArrayList<Course>();
        while (rs.next()) {    //String courseID, String semester, String courseName,
            // String courseMajor, String teacherID, String courseState, String courseType
            Course c = new Course();
            c.setSemester(rs.getString(2));
            c.setCourseID(rs.getString(1));
            c.setCourseMajor(rs.getString(4));
            c.setTeacherID(rs.getString(5));
            //c.setCourseState(rs.getString(6));
            c.setCourseName(rs.getString(3));
            c.setCourseType(rs.getString(6));
            c.setCourseDate(rs.getString(7));
            c.setCoursePeriod(rs.getString(8));
            for (int i = 1; i <= 8; i++) System.out.print(rs.getString(i) + "\t");
            //System.out.print(rs.getString(6)+"\t");
            System.out.println(c);
            courses.add(c);
        }
        return courses;
    }

    /**
     * @param rs 从数据库获取的课程结果
     * @return 已选课程名称列表
     */
    public static List<String> relationResulList(ResultSet rs) throws SQLException {
        List<String> objs = new ArrayList<String>();
        while (rs.next()) {
            String obj = rs.getString(3);
            objs.add(obj);
        }
        return objs;
    }
    /**
     * @param rs 从数据库获取的聊天结果
     * @return 聊天记录列表
     */
    public static List<Chat> ChatResultSet2List(ResultSet rs) throws SQLException {
        List<Chat> chats = new ArrayList<Chat>();
        while (rs.next()) {    //String uID, String NickName, String ChatText, String ChatTime

            Chat c = new Chat();
            c.setUID(rs.getString(1));
            c.setNickName(rs.getString(2));
            c.setChatText(rs.getString(3));
            c.setChatTime(rs.getString(4)) ;

            for (int i = 1; i <= 4; i++) System.out.print(rs.getString(i) + "\t");
            //System.out.print(rs.getString(6)+"\t");
            System.out.println(c);
            chats.add(c);
        }
        return chats;
    }
    /**
     * @param rs 从数据库获取的用户信息结果
     * @return 用户信息列表
     */
    public static List<User> UserResultSet2List(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<User>();
        while (rs.next()) {
            User u = new User();
            u.setId(rs.getString(1));
            u.setName(rs.getString(2));
            u.setAge(rs.getString(3));
            u.setSex(rs.getString(4));
            u.setGrade(rs.getString(5));
            u.setMajor(rs.getString(6));
            u.setPwd(rs.getString(7));
            u.setRole(rs.getString(8));
            u.setMoney(rs.getString(9));
            users.add(u);
        }
        return users;
    }

    public static List<Student> StudentCreditResultSetList(ResultSet rs) throws SQLException {
        List<Student> sts = new ArrayList<>();
        while (rs.next()) {
            Student s = new Student();
            s.setStudentcredit(rs.getInt(10));
            sts.add(s);
        }
        return sts;
    }
}
