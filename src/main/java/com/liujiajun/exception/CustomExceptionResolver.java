package com.liujiajun.exception;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *    全局异常处理器
 */

@ControllerAdvice //@ControllerAdvice 该注解定义全局异常处理类
public class CustomExceptionResolver  {

    @ExceptionHandler(CustomException.class)
    public ModelAndView resolveException(CustomException e) {
        ModelAndView modelAndView = new ModelAndView();
        String message = e.getMessage();
        modelAndView.addObject("message", message);
        modelAndView.setViewName("error");
        //错误信息
        return modelAndView;
    }

    @ExceptionHandler
    public ModelAndView resolveUsernameException(UnknownAccountException e) {
        ModelAndView modelAndView = new ModelAndView();
        //用户名错误异常
        modelAndView.addObject("message", "没有该用户");
        modelAndView.setViewName("../../login");
        return modelAndView;
    }

    @ExceptionHandler
    public ModelAndView resolvePasswordException(IncorrectCredentialsException e) {
        ModelAndView modelAndView = new ModelAndView();
        //用户名错误异常
        modelAndView.addObject("message", "密码错误");
        modelAndView.setViewName("../../login");
        return modelAndView;
    }

    @ExceptionHandler
    public ModelAndView resolveException(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        //未知错误
        modelAndView.addObject("message", "未知错误");
        modelAndView.setViewName("error");
        return modelAndView;
    }

}
