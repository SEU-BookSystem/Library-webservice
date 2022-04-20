package booksystem.utils;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@ControllerAdvice
public class MyControllerAdvice {


    @ResponseBody
    @ExceptionHandler(value = SQLException.class)
    public Result sqlHandler(Exception ex) {
        return Result.error(501,"数据连接失败");
    }


    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        return Result.error(510,ex.getMessage());
    }

}
