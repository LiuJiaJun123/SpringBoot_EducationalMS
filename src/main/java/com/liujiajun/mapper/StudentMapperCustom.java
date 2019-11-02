package com.liujiajun.mapper;

import com.liujiajun.po.StudentCustom;

import java.util.List;

public interface StudentMapperCustom {

    //分页查询学生信息
    List<StudentCustom> findByPaging() throws Exception;

    //根据姓名模糊查找
    List<StudentCustom> selectByName(StudentCustom studentCustom) throws Exception;
//    List<StudentCustom> selectByName(String username) throws Exception;

    //查询学生信息，和其选课信息
    StudentCustom findStudentAndSelectCourseListById(Integer id) throws Exception;

}
