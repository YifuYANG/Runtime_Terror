package app.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView exceptionHandler(Exception e){
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg", e.getMessage());
        mv.setViewName("error");
        return mv;
    }

}
