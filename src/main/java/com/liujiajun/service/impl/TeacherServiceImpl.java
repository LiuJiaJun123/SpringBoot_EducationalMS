package com.liujiajun.service.impl;

import com.github.pagehelper.PageHelper;
import com.liujiajun.exception.CustomException;
import com.liujiajun.mapper.CollegeMapper;
import com.liujiajun.mapper.CourseMapper;
import com.liujiajun.mapper.TeacherMapper;
import com.liujiajun.mapper.TeacherMapperCustom;
import com.liujiajun.po.*;
import com.liujiajun.service.TeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherMapperCustom teacherMapperCustom;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private CourseMapper courseMapper;

    //根据id更新老师信息
    public void updateById(Integer id, TeacherCustom teacherCustom) throws Exception {
        teacherMapper.updateByPrimaryKey(teacherCustom);
    }

    //根据id删除老师信息
    public void removeById(Integer id) throws Exception {
        CourseExample courseExample = new CourseExample();

        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andTeacheridEqualTo(id);
        List<Course> list = courseMapper.selectByExample(courseExample);

        if (list.size() != 0) {
            throw new CustomException("请先删除该名老师所教授的课程");
        }

        teacherMapper.deleteByPrimaryKey(id);
    }

    public List<TeacherCustom> findByPaging(Integer page, Integer pageSize) throws Exception {

        PageHelper.startPage(page,pageSize);
        List<TeacherCustom> list = teacherMapperCustom.findByPaging();

        return list;
    }

    //保存教师信息
    public Boolean save(TeacherCustom teacherCustom) throws Exception {

        Teacher tea = teacherMapper.selectByPrimaryKey(teacherCustom.getUserid());
        if (tea == null) {
            teacherMapper.insert(teacherCustom);
            return true;
        }
        return false;
    }

    public int getCountTeacher() throws Exception {
        //自定义查询对象
        TeacherExample teacherExample = new TeacherExample();
        //通过criteria构造查询条件
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        criteria.andUseridIsNotNull();

        return teacherMapper.countByExample(teacherExample);
    }

    public TeacherCustom findById(Integer id) throws Exception {
        Teacher teacher = teacherMapper.selectByPrimaryKey(id);
        TeacherCustom teacherCustom = null;
        if (teacher != null) {
            teacherCustom = new TeacherCustom();
            BeanUtils.copyProperties(teacher, teacherCustom);
        }

        return teacherCustom;
    }

    public List<TeacherCustom> findByName(String name, Integer page, Integer pageSize) throws Exception {
        TeacherCustom teacherCustom=new TeacherCustom();

        teacherCustom.setUsername("%" + name + "%");


        PageHelper.startPage(page,pageSize);
        List<TeacherCustom> list = teacherMapperCustom.selectByName(teacherCustom);

        return list;
    }

    public List<TeacherCustom> findAll() throws Exception {

        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();

        criteria.andUsernameIsNotNull();

        List<Teacher> list = teacherMapper.selectByExample(teacherExample);
        List<TeacherCustom> teacherCustomsList = null;
        if (list != null) {
            teacherCustomsList = new ArrayList<TeacherCustom>();
            for (Teacher t: list) {
                TeacherCustom teacherCustom = new TeacherCustom();
                BeanUtils.copyProperties(t, teacherCustom);
                teacherCustomsList.add(teacherCustom);
            }
        }
        return teacherCustomsList;
    }
}
