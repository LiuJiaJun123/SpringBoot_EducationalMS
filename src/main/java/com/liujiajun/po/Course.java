package com.liujiajun.po;

public class Course {

    //课程号
    private Integer courseid;

    //课程名称
    private String coursename;

//    授课老师编号
    private Integer teacherid;

//    上课时间
    private String coursetime;

//    上课地点
    private String classroom;

//    上课周数
    private Integer courseweek;

//    课程类型
    private String coursetype;

//    学分
    private Integer collegeid;

//    成绩
    private Integer score;

    public Integer getCourseid() {
        return courseid;
    }

    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename == null ? null : coursename.trim();
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public String getCoursetime() {
        return coursetime;
    }

    public void setCoursetime(String coursetime) {
        this.coursetime = coursetime == null ? null : coursetime.trim();
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom == null ? null : classroom.trim();
    }

    public Integer getCourseweek() {
        return courseweek;
    }

    public void setCourseweek(Integer courseweek) {
        this.courseweek = courseweek;
    }

    public String getCoursetype() {
        return coursetype;
    }

    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype == null ? null : coursetype.trim();
    }

    public Integer getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(Integer collegeid) {
        this.collegeid = collegeid;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseid=" + courseid +
                ", coursename='" + coursename + '\'' +
                ", teacherid=" + teacherid +
                ", coursetime='" + coursetime + '\'' +
                ", classroom='" + classroom + '\'' +
                ", courseweek=" + courseweek +
                ", coursetype='" + coursetype + '\'' +
                ", collegeid=" + collegeid +
                ", score=" + score +
                '}';
    }
}