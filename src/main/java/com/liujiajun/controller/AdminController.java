package com.liujiajun.controller;

import com.github.pagehelper.PageInfo;
import com.liujiajun.exception.CustomException;
import com.liujiajun.po.*;
import com.liujiajun.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource(name = "studentServiceImpl")
    private StudentService studentService;

    @Resource(name = "teacherServiceImpl")
    private TeacherService teacherService;

    @Resource(name = "courseServiceImpl")
    private CourseService courseService;

    @Resource(name = "collegeServiceImpl")
    private CollegeService collegeService;

    @Resource(name = "userloginServiceImpl")
    private UserloginService userloginService;

    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<学生操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

    //  学生信息显示
    @RequestMapping("/showStudent")
    public ModelAndView showStudent(@RequestParam(value = "page",required = true,defaultValue = "1")Integer page,
                                    @RequestParam(value = "pageSize",required = true,defaultValue = "4")Integer pageSize) throws Exception {

        List<StudentCustom> list = studentService.findByPaging(page,pageSize);

        PageInfo<StudentCustom> pageInfo=new PageInfo<StudentCustom>(list);

        ModelAndView mv=new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("admin/showStudent");

        return mv;

    }


    //添加学生时，查看学号是否存在
    @RequestMapping(value = "/checkStudentId")
    @ResponseBody
    public CheckUserId checkStudentId(@RequestBody CheckUserId checkUserId) throws Exception {

        Integer userid=checkUserId.getUserid();
        StudentCustom studentCustom = studentService.findById(userid);
        if(studentCustom!=null){
            //学号已经存在
            checkUserId.setFlag(false);
            checkUserId.setErrorMsg("该学号已经存在，请重新输入！");
        } else {
            //学号不存在，可以添加
            checkUserId.setFlag(true);
            checkUserId.setErrorMsg("");
        }
        return checkUserId;
    }

    //  添加学生信息页面显示
    @RequestMapping(value = "/addStudent", method = {RequestMethod.GET})
    public String addStudentUI(Model model) throws Exception {

        List<College> list = collegeService.finAll();

        model.addAttribute("collegeList", list);

        return "admin/addStudent";
    }

     // 添加学生信息操作
    @RequestMapping(value = "/addStudent", method = {RequestMethod.POST})
    public String addStudent(StudentCustom studentCustom, Model model) throws Exception {

        Boolean result = studentService.save(studentCustom);

        if (!result) {
            model.addAttribute("message", "学号重复");
            return "error";
        }
        //添加成功后，也添加到登录表
        Userlogin userlogin = new Userlogin();
        userlogin.setUsername(studentCustom.getUserid().toString());
        userlogin.setPassword("123");
        userlogin.setRole(2);
        userloginService.save(userlogin);

        //重定向
        return "redirect:/admin/showStudent";
    }

    // 修改学生信息页面显示
    @RequestMapping(value = "/editStudent", method = {RequestMethod.GET})
    public String editStudentUI(Integer id, Model model) throws Exception {
        if (id == null) {
            //加入没有带学生id就进来的话就返回学生显示页面
            return "redirect:/admin/showStudent";
        }
        StudentCustom studentCustom = studentService.findById(id);
        if (studentCustom == null) {
            throw new CustomException("未找到该名学生");
        }
        List<College> list = collegeService.finAll();

        model.addAttribute("collegeList", list);
        model.addAttribute("student", studentCustom);

        return "admin/editStudent";
    }

    // 修改学生信息处理
    @RequestMapping(value = "/editStudent", method = {RequestMethod.POST})
    public String editStudent(StudentCustom studentCustom) throws Exception {

        studentService.updataById(studentCustom.getUserid(), studentCustom);

        //重定向
        return "redirect:/admin/showStudent";
    }

    // 删除学生
    @RequestMapping(value = "/removeStudent", method = {RequestMethod.GET} )
    private String removeStudent(Integer id) throws Exception {
        if (id == null) {
            //加入没有带学生id就进来的话就返回学生显示页面
            return "admin/showStudent";
        }
        studentService.removeById(id);
        userloginService.removeByName(id.toString());

        return "redirect:/admin/showStudent";
    }


    //将查询的学生名字存入session中
    @RequestMapping("/searchStudentName")
    public void searchStudentName(@RequestBody Student student, HttpServletRequest request){
        String username=student.getUsername();
        //将要查找的姓名存入session中，在selectStudent中取出session
        request.getSession().setAttribute("findStudentByName",username);
    }

    // 搜索学生
    @RequestMapping(value = "selectStudent")  //, method = {RequestMethod.POST}
    private ModelAndView selectStudent(HttpServletRequest request,
                                       @RequestParam(value = "page",required = true,defaultValue = "1")Integer page,
                                       @RequestParam(value = "pageSize",required = true,defaultValue = "4")Integer pageSize) throws Exception {


        String  findStudentByName = (String) request.getSession().getAttribute("findStudentByName");

        List<StudentCustom> list = studentService.findByName(findStudentByName,page,pageSize);
        PageInfo selectStudentInfo=new PageInfo(list);



        ModelAndView mv=new ModelAndView();
        mv.addObject("selectStudentInfo",selectStudentInfo);
        mv.setViewName("admin/selectStudent");
        return mv;
    }

    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<教师操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

    // 教师页面显示
    @RequestMapping("/showTeacher")
    public ModelAndView showTeacher(@RequestParam(value = "page",required = true,defaultValue = "1")Integer page,
                                    @RequestParam(value = "pageSize",required = true,defaultValue = "4")Integer pageSize) throws Exception {

        List<TeacherCustom> list = null;

        list = teacherService.findByPaging(page,pageSize);

        PageInfo teacherPageInfo=new PageInfo(list);

        ModelAndView mv=new ModelAndView();
        mv.addObject("teacherPageInfo",teacherPageInfo);
        mv.setViewName("admin/showTeacher");

        return mv;

    }


    //添加教师时，查看工号是否存在
    @RequestMapping(value = "/checkTeacherId")
    @ResponseBody
    public CheckUserId checkTeacherId(@RequestBody CheckUserId checkUserId, HttpServletRequest request) throws Exception {

        Integer userid=checkUserId.getUserid();
        TeacherCustom teacherCustom = teacherService.findById(userid);
        if(teacherCustom!=null){
            //工号已经存在
            checkUserId.setFlag(false);
            checkUserId.setErrorMsg("该工号已经存在，请重新输入！");
        } else {
            //工号不存在，可以添加
            checkUserId.setFlag(true);
            checkUserId.setErrorMsg("");
        }
        return checkUserId;
    }

    // 添加教师信息
    @RequestMapping(value = "/addTeacher", method = {RequestMethod.GET})
    public String addTeacherUI(Model model) throws Exception {

        List<College> list = collegeService.finAll();

        model.addAttribute("collegeList", list);

        return "admin/addTeacher";
    }

    // 添加教师信息处理
    @RequestMapping(value = "/addTeacher", method = {RequestMethod.POST})
    public String addTeacher(TeacherCustom teacherCustom, Model model) throws Exception {

        Boolean result = teacherService.save(teacherCustom);

        if (!result) {
            model.addAttribute("message", "工号重复");
            return "error";
        }
        //添加成功后，也添加到登录表
        Userlogin userlogin = new Userlogin();
        userlogin.setUsername(teacherCustom.getUserid().toString());
        userlogin.setPassword("123");
        userlogin.setRole(1);
        userloginService.save(userlogin);

        //重定向
        return "redirect:/admin/showTeacher";
    }

    // 修改教师信息页面显示
    @RequestMapping(value = "/editTeacher", method = {RequestMethod.GET})
    public String editTeacherUI(Integer id, Model model) throws Exception {
        if (id == null) {
            return "redirect:/admin/showTeacher";
        }
        TeacherCustom teacherCustom = teacherService.findById(id);
        if (teacherCustom == null) {
            throw new CustomException("未找到该教师");
        }
        List<College> list = collegeService.finAll();

        model.addAttribute("collegeList", list);
        model.addAttribute("teacher", teacherCustom);

        return "admin/editTeacher";
    }

    // 修改教师信息页面处理
    @RequestMapping(value = "/editTeacher", method = {RequestMethod.POST})
    public String editTeacher(TeacherCustom teacherCustom) throws Exception {

        teacherService.updateById(teacherCustom.getUserid(), teacherCustom);

        //重定向
        return "redirect:/admin/showTeacher";
    }

    //删除教师
    @RequestMapping("/removeTeacher")
    public String removeTeacher(Integer id) throws Exception {
        if (id == null) {
            //加入没有带教师id就进来的话就返回教师显示页面
            return "admin/showTeacher";
        }
        teacherService.removeById(id);
        userloginService.removeByName(id.toString());

        return "redirect:/admin/showTeacher";
    }


    //将查询的 教师名称存入session中
    @RequestMapping("/searchTeacherName")
    public void searchTeacherName(@RequestBody Teacher teacher, HttpServletRequest request){
        String username=teacher.getUsername();
        //将查询的 课程名称存入session中
        request.getSession().setAttribute("findTeacherByName",username);

    }

    //搜索教师
    @RequestMapping(value = "selectTeacher")
    private ModelAndView selectTeacher(HttpServletRequest request,
                                       @RequestParam(value = "page",required = true,defaultValue = "1")Integer page,
                                       @RequestParam(value = "pageSize",required = true,defaultValue = "4")Integer pageSize) throws Exception {


        String findTeacherByName = (String) request.getSession().getAttribute("findTeacherByName");

        List<TeacherCustom> list = teacherService.findByName(findTeacherByName,page,pageSize);
        PageInfo selectTeacherInfo = new PageInfo(list);


        ModelAndView mv=new ModelAndView();
        mv.addObject("selectTeacherInfo",selectTeacherInfo);
        mv.setViewName("admin/selectTeacher");

        return mv;

    }

    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<课程操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

    // 课程信息显示
    @RequestMapping("/showCourse")
    public ModelAndView showCourse(@RequestParam(value = "page",required = true,defaultValue = "1")Integer page,
                                   @RequestParam(value = "pageSize",required = true,defaultValue = "4")Integer pageSize) throws Exception {


        List<CourseCustom> list = courseService.findByPaging(page,pageSize);

        PageInfo CoursePageInfo=new PageInfo(list);

        ModelAndView mv=new ModelAndView();
        mv.addObject("CoursePageInfo",CoursePageInfo);
        mv.setViewName("admin/showCourse");

        return mv;

    }

    //添加课程时，查看课程号是否存在
    @RequestMapping(value = "/checkCourseId")
    @ResponseBody
    public CheckUserId checkCourseId(@RequestBody CheckUserId checkUserId) throws Exception {

        //获取课程号
        Integer userid=checkUserId.getUserid();
        CourseCustom courseCustom = courseService.findById(userid);
        if(courseCustom!=null){
            //课程号已经存在
            checkUserId.setFlag(false);
            checkUserId.setErrorMsg("该课程号已经存在，请重新输入！");
        } else {
            //课程号不存在，可以添加
            checkUserId.setFlag(true);
            checkUserId.setErrorMsg("");
        }
        return checkUserId;
    }

    //添加课程
    @RequestMapping(value = "/addCourse", method = {RequestMethod.GET})
    public String addCourseUI(Model model) throws Exception {

        List<TeacherCustom> list = teacherService.findAll();
        List<College> collegeList = collegeService.finAll();

        model.addAttribute("collegeList", collegeList);
        model.addAttribute("teacherList", list);

        return "admin/addCourse";
    }

    // 添加课程信息处理
    @RequestMapping(value = "/addCourse", method = {RequestMethod.POST})
    public String addCourse(CourseCustom courseCustom, Model model) throws Exception {

        Boolean result = courseService.save(courseCustom);

        if (!result) {
            model.addAttribute("message", "课程号重复");
            return "error";
        }

        //重定向
        return "redirect:/admin/showCourse";
    }

    // 修改课程信息页面显示
    @RequestMapping(value = "/editCourse", method = {RequestMethod.GET})
    public String editCourseUI(Integer id, Model model) throws Exception {
        if (id == null) {
            return "redirect:/admin/showCourse";
        }
        CourseCustom courseCustom = courseService.findById(id);
        if (courseCustom == null) {
            throw new CustomException("未找到该课程");
        }
        List<TeacherCustom> list = teacherService.findAll();
        List<College> collegeList = collegeService.finAll();

        model.addAttribute("teacherList", list);
        model.addAttribute("collegeList", collegeList);
        model.addAttribute("course", courseCustom);

        return "admin/editCourse";
    }

    // 修改课程信息页面处理
    @RequestMapping(value = "/editCourse", method = {RequestMethod.POST})
    public String editCourse(CourseCustom courseCustom) throws Exception {

        courseService.upadteById(courseCustom.getCourseid(), courseCustom);
        //重定向
        return "redirect:/admin/showCourse";
    }

    // 删除课程信息
    @RequestMapping("/removeCourse")
    public String removeCourse(Integer id) throws Exception {
        if (id == null) {
            //加入没有带教师id就进来的话就返回教师显示页面
            return "admin/showCourse";
        }
        courseService.removeById(id);

        return "redirect:/admin/showCourse";
    }


    //将查询的 课程名称存入session中
    @RequestMapping("/searchCourseName")
    public void searchCourseName(@RequestBody Student student, HttpServletRequest request){
        String username=student.getUsername();
        //将查询的 课程名称存入session中
        request.getSession().setAttribute("findCourseByName",username);
    }

    //搜索课程
    @RequestMapping(value = "selectCourse")
    private ModelAndView selectCourse(HttpServletRequest request,
                                      @RequestParam(value = "page",required = true,defaultValue = "1")Integer page,
                                      @RequestParam(value = "pageSize",required = true,defaultValue = "4")Integer pageSize) throws Exception {


        String  findCourseByName = (String) request.getSession().getAttribute("findCourseByName");

        List<CourseCustom> list = courseService.findByName(findCourseByName,page,pageSize);
        PageInfo selectCourseInfo=new PageInfo(list);


        ModelAndView mv=new ModelAndView();
        mv.addObject("selectCourseInfo",selectCourseInfo);
        mv.setViewName("admin/selectCourse");

        return mv;

    }

    /*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<其他操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

    // 普通用户账号密码重置
    @RequestMapping("/userPasswordRest")
    public String userPasswordRestUI() throws Exception {
        return "admin/userPasswordRest";
    }

    // 普通用户账号密码重置处理
    @RequestMapping(value = "/userPasswordRest", method = {RequestMethod.POST})
    public String userPasswordRest(Userlogin userlogin) throws Exception {

        Userlogin u = userloginService.findByName(userlogin.getUsername());

        if (u != null) {
            if (u.getRole() == 0) {
                throw new CustomException("该账户为管理员账户，没法修改");
            }
            u.setPassword(userlogin.getPassword());
            userloginService.updateByName(userlogin.getUsername(), u);
        } else {
            throw new CustomException("没找到该用户");
        }

        return "admin/userPasswordRest";
    }

    // 本账户密码重置
    @RequestMapping("/passwordRest")
    public String passwordRestUI() throws Exception {
        return "admin/passwordRest";
    }


}
