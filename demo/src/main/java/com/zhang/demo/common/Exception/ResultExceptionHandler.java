package com.zhang.demo.common.Exception;

import com.zhang.demo.common.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 * @author zhang
 * @date 2020-04-22 10:50:10
 */
@RestControllerAdvice
public class ResultExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(ResultExceptionHandler.class);

	/**
     * 全局处理返回传参校验错误
     * @Validated校验失败时会抛出MethodArgumentNotValidException异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult methodArgumentValidExceptionHandler(MethodArgumentNotValidException e) throws Exception {
        String message = null;
        //如果抛出了异常，这个getErrorCount一定会>0
        if (e.getBindingResult().getErrorCount() > 0) {
            //校验会把所有不通过的选项的错误信息记录下来，现在先默认给前端提供第一个错误信息
            message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }
        return CommonResult.failed(message);
    }

}
