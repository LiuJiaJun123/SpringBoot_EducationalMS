package com.liujiajun.service.impl;

import com.github.pagehelper.PageHelper;
import com.liujiajun.mapper.CollegeMapper;
import com.liujiajun.mapper.CourseMapper;
import com.liujiajun.mapper.CourseMapperCustom;
import com.liujiajun.mapper.SelectedcourseMapper;
import com.liujiajun.po.*;
import com.liujiajun.service.CourseService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseMapperCustom courseMapperCustom;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private SelectedcourseMapper selectedcourseMapper;

    public void upadteById(Integer id, CourseCustom courseCustom) throws Exception {
        courseMapper.updateByPrimaryKey(courseCustom);
    }

    public Boolean removeById(Integer id) throws Exception {
        //自定义查询条件
        SelectedcourseExample example = new SelectedcourseExample();
        SelectedcourseExample.Criteria criteria = example.createCriteria();
        criteria.andCourseidEqualTo(id);
        List<Selectedcourse> list = selectedcourseMapper.selectByExample(example);

        if (list.size() == 0) {
            courseMapper.deleteByPrimaryKey(id);
            return true;
        }

        return false;
    }

    public List<CourseCustom> findByPaging(Integer page, Integer pageSize) throws Exception {

        PageHelper.startPage(page,pageSize);
        List<CourseCustom> list = courseMapperCustom.findByPaging();
        return list;
    }

    public Boolean save(CourseCustom couseCustom) throws Exception {
        Course course = courseMapper.selectByPrimaryKey(couseCustom.getCourseid());
        if (course == null) {
            courseMapper.insert(couseCustom);
            return true;
        }
        return false;
    }

    public int getCountCouse() throws Exception {
        //自定义查询对象
        CourseExample courseExample = new CourseExample();
        //通过criteria构造查询条件
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andCoursenameIsNotNull();

        return courseMapper.countByExample(courseExample);
    }

    public CourseCustom findById(Integer id) throws Exception {
        Course course = courseMapper.selectByPrimaryKey(id);
        CourseCustom courseCustom = null;
        if (course != null) {
            courseCustom = new CourseCustom();
            BeanUtils.copyProperties(courseCustom, course);
        }

        return courseCustom;
    }

    public List<CourseCustom> findByName(String name, Integer page, Integer pageSize) throws Exception {

        CourseCustom courseCustom=new CourseCustom();

        courseCustom.setCoursename("%" + name + "%");


        PageHelper.startPage(page,pageSize);
        List<CourseCustom> list = courseMapperCustom.selectByName(courseCustom);

        return list;
    }

    //根据教师id查找课程
    public List<CourseCustom> findByTeacherID(Integer id) throws Exception {
        CourseExample courseExample = new CourseExample();
        //自定义查询条件
        CourseExample.Criteria criteria = courseExample.createCriteria();
        //根据教师id查课程
        criteria.andTeacheridEqualTo(id);

        List<Course> list = courseMapper.selectByExample(courseExample);
        List<CourseCustom> courseCustomList = null;

        if (list.size() > 0) {
            courseCustomList = new ArrayList<CourseCustom>();
            for (Course c : list) {
                CourseCustom courseCustom = new CourseCustom();
                //类拷贝
                BeanUtils.copyProperties(courseCustom, c);
                //获取院系名
                College college = collegeMapper.selectByPrimaryKey(c.getCollegeid());
                courseCustom.setcollegeName(college.getCollegename());

                courseCustomList.add(courseCustom);
            }
        }

        return courseCustomList;
    }
}
