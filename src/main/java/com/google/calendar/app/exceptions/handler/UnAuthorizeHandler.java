package com.google.calendar.app.exceptions.handler;

import com.google.calendar.app.utils.Constants;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author mir00r on 24/9/20
 * @project IntelliJ IDEA
 */
@ControllerAdvice
public class UnAuthorizeHandler {

    /**
     * Return HTTP unauthorized status and redirect to error page and there is a button to go to home screen if not logged in
     *
     * @return
     */
    @ExceptionHandler(Throwable.class)
    ModelAndView handleGeneralException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(Constants.MESSAGE, Constants.UN_AUTHORIZED_EXCEPTION_MESSAGE);
        modelAndView.setViewName(Constants.UN_AUTHORIZED_URL);
        return modelAndView;
    }
}
