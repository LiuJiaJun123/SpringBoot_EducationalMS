package com.liujiajun.controller;

import com.github.pagehelper.PageInfo;
import com.liujiajun.exception.CustomException;
import com.liujiajun.po.CourseCustom;
import com.liujiajun.po.SelectedCourseCustom;
import com.liujiajun.po.Student;
import com.liujiajun.po.StudentCustom;
import com.liujiajun.service.CourseService;
import com.liujiajun.service.SelectedCourseService;
import com.liujiajun.service.StudentService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/student")
public class StudentController {

    @Resource(name = "courseServiceImpl")
    private CourseService courseService;

    @Resource(name = "studentServiceImpl")
    private StudentService studentService;

    @Resource(name = "selectedCourseServiceImpl")
    private SelectedCourseService selectedCourseService;

    //展示所有课程
    @RequestMapping(value = "/showCourse")
    public ModelAndView showCourse(@RequestParam(value = "page",required = true,defaultValue = "1")Integer page,
                                   @RequestParam(value = "pageSize",required = true,defaultValue = "4")Integer pageSize) throws Exception {


        List<CourseCustom> list = courseService.findByPaging(page,pageSize);

        PageInfo CoursePageInfo=new PageInfo(list);

        ModelAndView mv=new ModelAndView();
        mv.addObject("CoursePageInfo",CoursePageInfo);
        mv.setViewName("student/showCourse");

        return mv;

    }

    //将查询的 课程名称存入session中
    @RequestMapping("/searchCourseName")
    public void searchCourseName(@RequestBody Student student, HttpServletRequest request){
        String username=student.getUsername();
        //将查询的 课程名称存入session中
        request.getSession().setAttribute("findCourseByName",username);
    }

    //搜索课程
    @RequestMapping(value = "/searchCourse")
    private ModelAndView searchCourse(HttpServletRequest request,
                                      @RequestParam(value = "page",required = true,defaultValue = "1")Integer page,
                                      @RequestParam(value = "pageSize",required = true,defaultValue = "4")Integer pageSize) throws Exception {


        String  findCourseByName = (String) request.getSession().getAttribute("findCourseByName");

        List<CourseCustom> list = courseService.findByName(findCourseByName,page,pageSize);
        PageInfo searchCourseInfo=new PageInfo(list);


        ModelAndView mv=new ModelAndView();
        mv.addObject("searchCourseInfo",searchCourseInfo);
        mv.setViewName("student/searchCourse");

        return mv;

    }

    // 选课操作
    @RequestMapping(value = "/stuSelectedCourse")
    public String stuSelectedCourse(int id) throws Exception {
        //获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();

        SelectedCourseCustom selectedCourseCustom = new SelectedCourseCustom();
        selectedCourseCustom.setCourseid(id);
        selectedCourseCustom.setStudentid(Integer.parseInt(username));

        SelectedCourseCustom s = selectedCourseService.findOne(selectedCourseCustom);

        if (s == null) {
            selectedCourseService.save(selectedCourseCustom);
        } else {
            throw new CustomException("该门课程你已经选了，不能再选");
        }

        return "redirect:/student/selectedCourse";
    }

    // 退课操作
    @RequestMapping(value = "/outCourse")
    public String outCourse(int id) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();

        SelectedCourseCustom selectedCourseCustom = new SelectedCourseCustom();
        selectedCourseCustom.setCourseid(id);
        selectedCourseCustom.setStudentid(Integer.parseInt(username));

        selectedCourseService.remove(selectedCourseCustom);

        return "redirect:/student/selectedCourse";
    }

    // 已选课程
    @RequestMapping(value = "/selectedCourse")
    public String selectedCourse(Model model) throws Exception {
        //获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        StudentCustom studentCustom = studentService.findStudentAndSelectCourseListByName((String) subject.getPrincipal());

        List<SelectedCourseCustom> list = studentCustom.getSelectedCourseList();

        model.addAttribute("selectedCourseList", list);

        return "student/selectCourse";
    }

    // 已修课程
    @RequestMapping(value = "/overCourse")
    public String overCourse(Model model) throws Exception {

        //获取当前用户名
        Subject subject = SecurityUtils.getSubject();
        StudentCustom studentCustom = studentService.findStudentAndSelectCourseListByName((String) subject.getPrincipal());

        List<SelectedCourseCustom> list = studentCustom.getSelectedCourseList();

        model.addAttribute("selectedCourseList", list);

        return "student/overCourse";
    }



    //修改密码
    @RequestMapping(value = "/passwordRest")
    public String passwordRest() throws Exception {
        return "student/passwordRest";
    }



}
