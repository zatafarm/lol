package com.mooland.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  

    // 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneralException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", "서버에서 오류가 발생했습니다.");
        return modelAndView;
    }

    // 404 예외 처리
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(NoHandlerFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", "요청하신 페이지를 찾을 수 없습니다.");
        return modelAndView;
    }
}