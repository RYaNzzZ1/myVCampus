package seu.list.server.dao;

import seu.list.common.Course;
import seu.list.common.Message;
import seu.list.common.MessageType;
import seu.list.common.User;
import seu.list.server.db.SqlHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class CourseServer {
    private Message mesFromClient;
    private Message mesToClient = new Message();

    public CourseServer(Message mesFromClient) {
        this.mesFromClient = mesFromClient;
    }

    public void execute() throws SQLException, ClassNotFoundException {

        switch (this.mesFromClient.getMessageType()) {
            case MessageType.REQ_STU_ADD_LESSON: {
                System.out.println("serving REQ_STU_ADD_LESSON");
                System.out.println("adding....");
                //检查课程是否存在
                Course course = new Course();
                String CourseId = this.mesFromClient.getContent().get(0);
                System.out.println("CourseID为" + CourseId);
                course = this.searchCourseByID(CourseId);
                if (course == null) {
                    this.mesToClient.setSeccess(false);
                    this.mesToClient.setErrorMessage("Null");
                    break;
                } else {
                    String UserId = this.mesFromClient.getContent().get(1);
                    boolean conflict = this.isConflict(UserId, course.getCourseDate(), course.getCoursePeriod(), CourseId);
                    boolean chose = false;
                    if (!conflict) {
                        List<Course> allCourse;
                        User user = new User();
                        user.setId(UserId);
                        allCourse = user.getCourses();
                        String prefix = CourseId.substring(0, 5);
                        for (Course value : allCourse) {
                            String cid = value.getCourseID();
                            if (cid.contains(prefix)) {
                                conflict = true;
                                if (cid.equals(CourseId))
                                    chose = true;
                                break;
                            }
                        }
                        if (!conflict) {
                            this.mesToClient.setSeccess(this.sigAddCourse(CourseId, UserId));
                            break;
                        }
                    }
                    this.mesToClient.setSeccess(false);
                    this.mesToClient.setErrorMessage("Course conflict.");
                    if (chose)
                        this.mesToClient.setErrorMessage("Chose");
                }
                System.out.println("REQ_STU_ADD_LESSON finished");
                break;
            }
            case MessageType.REQ_STU_REMOVE_LESSON: {
                System.out.println("serving REQ_STU_REMOVE_LESSON");
                System.out.println("removing....");
                String CourseId = this.mesFromClient.getContent().get(0);
                String UserId = this.mesFromClient.getContent().get(1);

                List<Course> allCourse;
                User user = new User();
                user.setId(String.valueOf(this.mesFromClient.getData()));
                allCourse = user.getCourses();
                boolean f = false;
                for (Course course : allCourse) {
                    if (course.getCourseID().equals(CourseId)) {
                        f = true;
                        break;
                    }
                }
                if (!f) {
                    this.mesToClient.setSeccess(false);
                    this.mesToClient.setErrorMessage("Invalid operation");
                } else {
                    this.mesToClient.setSeccess(this.sigRemoveCourse(CourseId, UserId));
                    System.out.println("REQ_STU_REMOVE_LESSON finshed");
                }
                break;
            }
            case MessageType.REQ_ADD_LESSON: {
                System.out.println("serving REQ_ADD_LESSON");
                System.out.println("adding....");
                //
                Course course = new Course();
                course.setContent(this.mesFromClient.getContent());

                this.mesToClient.setSeccess(this.genAddCourse(course));
                System.out.println("REQ_ADD_LESSON finished");
                break;
            }
            case MessageType.REQ_REMOVE_LESSON: {
                System.out.println("serving REQ_REMOVE_LESSON");
                System.out.println("removing.....");
                String courseId = this.mesFromClient.getContent().get(2);

                this.mesToClient.setSeccess(this.genRemoveCourse(courseId));
                System.out.println("REQ_REMOVE_LESSON finished");
                break;
            }
            case MessageType.REQ_SEARCH_LESSON: {
                System.out.println("serving REQ_SEARCH_LESSON");
                System.out.println("searching.....");
                Course course = new Course();
                String courseID = this.mesFromClient.getContent().get(0);
                System.out.println("CourseID为" + courseID);
                course = this.searchCourseByID(courseID);
                if (course == null) this.mesToClient.setSeccess(false);
                else {
                    this.mesToClient.setContent(course.getContent());
                    this.mesToClient.setSeccess(true);
                }

                System.out.println("REQ_SEARCH_LESSON finished");
                break;
            }
            case MessageType.REQ_SHOW_ALL_LESSON: {
                System.out.println("serving REQ_SHOW_ALL_LESSON");
                System.out.println("grabbing.....");
                Vector<String> sigCourseContent = new Vector<>();
                Vector<String> allCourseContent = new Vector<>();
                List<Course> allCourse = this.getAllCourse();
                for (Course course : allCourse) {
                    sigCourseContent = course.getContent();
                    for (int i = 0; i < 8; i++) {
                        allCourseContent.add(sigCourseContent.get(i));
                    }
                }
                System.out.println(allCourseContent);
                this.mesToClient.setContent(allCourseContent);
                System.out.println("REQ_SHOW_ALL_LESSON finished");
                break;
            }
            case MessageType.REQ_STU_ALL_CHOOOSE: {
                System.out.println("serving REQ_STU_ALL_CHOOOSE");
                System.out.println("grabbing......");
                Vector<String> sigCourseContent = new Vector<>();
                Vector<String> allCourseContent = new Vector<>();
                List<Course> allCourse;
                User user = new User();
                user.setId(String.valueOf(this.mesFromClient.getData()));
                allCourse = user.getCourses();
                System.out.println("课程表的allcourse：" + allCourse);
                for (Course course : allCourse) {
                    sigCourseContent = course.getContent();
                    if (sigCourseContent != null)
                        for (int i = 0; i <= 7; i++) {
                            allCourseContent.add(sigCourseContent.get(i));
                        }
                }
                this.mesToClient.setContent(allCourseContent);
                System.out.println("REQ_STU_ALL_CHOOOSE finished");
                break;
            }
            default:
                break;
        }
    }

    public Message getMesToClient() {
        System.out.println("mesToClient的内容是" + this.mesToClient.getContent());
        return this.mesToClient;
    }


    public Course searchCourseByID(String courseID) {
        // TODO Auto-generated method stub
        String sql = "select * from tb_Class where cID =?";
        String[] paras = new String[1];
        paras[0] = courseID;
        List<Course> cList = new SqlHelper().sqlCourseQuery(sql, paras);
        if (cList != null && cList.size() > 0) {
            System.out.println(cList.get(0));
            return cList.get(0);
        } else
            return null;
    }

    public boolean isConflict(String uID, String Date, String period, String cID) {
        String sql = "select * from tb_stc where uID = ? and CourseDate = ? and CoursePeriod = ? and not cID = ?";
        String[] paras = new String[4];
        paras[0] = uID;
        paras[1] = Date;
        paras[2] = period;
        paras[3] = cID;
        return new SqlHelper().sqlConflictCheck(sql, paras);
    }

    public List<Course> getAllCourse() {
        // TODO Auto-generated method stub
        String sql = "select * from tb_Class";
        return new SqlHelper().sqlCourseQuery(sql, new String[]{});
    }


    public boolean sigAddCourse(String courseID, String uID) {
        String sql = "select * from tb_Class where cID= ?";
        String[] paras = new String[1];
        paras[0] = courseID;
        List<Course> thiscourse = new SqlHelper().sqlCourseQuery(sql, paras);

        String[] paras2 = new String[6];
        paras2[0] = uID + courseID;
        paras2[1] = uID;
        paras2[2] = courseID;
        paras2[3] = thiscourse.get(0).getCourseName();
        paras2[4] = thiscourse.get(0).getCoursePeriod();
        paras2[5] = thiscourse.get(0).getCourseType();
        String sql2 = "insert into tb_Stc(scID,uID,cID,courseName,CourseDate,CoursePeriod) values(?,?,?,?,?,?)";

        return new SqlHelper().sqlUpdate(sql2, paras2);
    }


    public boolean sigRemoveCourse(String courseID, String uID) {
        String[] paras2 = new String[2];
        String sql2 = "delete from tb_Stc where uID = ? and cID = ?";
        paras2[0] = uID;
        paras2[1] = courseID;
        return new SqlHelper().sqlUpdate(sql2, paras2);
    }

    public boolean genAddCourse(Course course) {
        // TODO Auto-generated method stub
        String sql1 = "insert into tb_Class(cID,Semester,CourseName,courseMajor,teacherID,CourseType,CourseDate,CoursePeriod) values (?,?,?,?,?,?,?,?)";
        String[] paras = new String[8];
        paras[1] = course.getSemester();
        paras[0] = course.getCourseID();
        paras[3] = course.getCourseMajor();
        paras[2] = course.getCourseName();
        paras[4] = course.getTeacherID();
        paras[5] = course.getCourseType();
        paras[6] = course.getCourseDate();
        paras[7] = course.getCoursePeriod();
        return new SqlHelper().sqlUpdate(sql1, paras);
    }

    public boolean genRemoveCourse(String courseId) {
        // TODO Auto-generated method stub
        String sql = "delete from tb_Class where cID = ?";
        String[] paras = new String[1];
        paras[0] = courseId;
        System.out.println("课程名是：" + paras[0]);
        //学生选课同步删除
        String sql1 = "delete from tb_Stc where cID = ?";
        new SqlHelper().sqlUpdate(sql1, paras);

        return new SqlHelper().sqlUpdate(sql, paras);
    }
}
