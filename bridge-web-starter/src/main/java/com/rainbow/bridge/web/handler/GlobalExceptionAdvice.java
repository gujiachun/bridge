package com.rainbow.bridge.web.handler;

import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import com.rainbow.bridge.core.exception.BusinessException;
import com.rainbow.bridge.web.utils.ParameterInvalidItem;
import com.rainbow.bridge.web.utils.ParameterInvalidItemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常处理类，但不能处理filter异常
 * @author gujiachun
 */
@Configuration
@RestControllerAdvice
public class GlobalExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    /**
     * 违反约束异常
     *@author gujiachun
     *@date 2021/9/13 2:03 下午
     *@param e 异常
     *@param request 请求对象
     *@return com.lixiang.common.core.Result
    */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        logger.error("====handleConstraintViolationException-ConstraintViolationException start, uri:{}, caused by: {}", request.getRequestURI(), e);
        List<ParameterInvalidItem> parameterInvalidItemList = ParameterInvalidItemUtil.convertCVSetToParameterInvalidItemList(e.getConstraintViolations());
        if (parameterInvalidItemList != null) {
            logger.error("====handleBindException-违反约束异常:{}", parameterInvalidItemList);
            return Result.fail(ResultEnum.SERVICE_PARAM_INVALID,getParamListMessage(parameterInvalidItemList));
        }
        return Result.fail(ResultEnum.SERVICE_PARAM_INVALID,"违反约束异常");
    }

    /**
     * 处理验证参数封装错误时异常
     *@author gujiachun
     *@date 2021/9/13 2:02 下午
     *@param e 异常
     *@return com.lixiang.common.core.Result
    */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleBindException(MethodArgumentNotValidException e) {
        logger.error("参数校验异常:{}", e.getMessage());
        List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        if(!CollectionUtils.isEmpty(objectErrors)) {
            for (ObjectError objectError : objectErrors) {
                sb.append(objectError.getDefaultMessage());
                sb.append(" ");
            }
        }
        return Result.fail(ResultEnum.SERVICE_PARAM_INVALID,sb.toString());
    }

    /**
     * 处理验证参数封装错误时异常
     *@author gujiachun
     *@date 2021/9/13 2:02 下午
     *@param e 异常
     *@param request 请求对象
     *@return com.lixiang.common.core.Result
    */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleConstraintViolationException(HttpMessageNotReadableException e, HttpServletRequest request) {
        logger.error("handleConstraintViolationException-HttpMessageNotReadableException start, uri:{}, caused by: {}", request.getRequestURI(), e);
        return Result.fail(ResultEnum.PARAM_ERROR);
    }

    /**
     * 处理参数绑定时异常（反400错误码）
     *@author gujiachun
     *@date 2021/9/13 2:02 下午
     *@param e 异常
     *@param request 请求对象
     *@return com.lixiang.common.core.Result
    */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        logger.error("====handleBindException start, uri:{}, caused by: {}", request.getRequestURI(), e);
        List<ParameterInvalidItem> parameterInvalidItemList = ParameterInvalidItemUtil.convertBindingResultToMapParameterInvalidItemList(e.getBindingResult());

        if (parameterInvalidItemList != null) {
            logger.error("====handleBindException-处理参数绑定时异常: {}", parameterInvalidItemList);
            return Result.fail(ResultEnum.SERVICE_PARAM_INVALID,getParamListMessage(parameterInvalidItemList));
        }

        return Result.fail(ResultEnum.SERVICE_PARAM_INVALID);
    }

    private String getParamListMessage(List<ParameterInvalidItem> parameterInvalidItemList) {
        StringBuilder sb = new StringBuilder();
        for (ParameterInvalidItem parameterInvalidItem : parameterInvalidItemList) {
            sb.append(parameterInvalidItem.getMessage());
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 处理404异常
     * 此异常不会进入做处理，只能在此转换
     *@author gujiachun
     *@date 2021/9/13 2:03 下午
     *@param e 异常
     *@param request 请求对象
     *@return com.lixiang.common.core.Result
    */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> handleNotFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        logger.error("NoHandlerFoundException start, uri:{}, caused by: ", request.getRequestURI(), e);

        return Result.fail(ResultEnum.INTERFACE_NOT_FOUND);
    }

    /**
     * 业务系统
     *@author gujiachun
     *@date 2021/9/13 2:04 下午
     *@param e 异常
     *@param request 请求对象
     *@param response 响应对象
     *@return com.lixiang.common.core.Result
    */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e,HttpServletRequest request,
                                                  HttpServletResponse response) {

        logger.error("handleBusinessException start, uri:{}, caused by: ", request.getRequestURI(), e);

        if (e.getCode().equals(ResultEnum.TOKEN_INVALID_ERROR.getCode())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return Result.fail(ResultEnum.TOKEN_INVALID_ERROR);
        }
        return Result.fail(e.getCode(),e.getMessage());
    }

    /**
     * 处理运行时系统异常（返回500错误码）
     *@author gujiachun
     *@date 2021/9/13 2:04 下午
     *@param e 异常
     *@param request 请求对象
     *@return com.lixiang.common.core.Result
    */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        //TODO 可通过邮件、微信公众号等方式发送信息至开发人员、记录存档等操作（这个后面我们文章我们单独说明该怎么处理）
        logger.error("handleRuntimeException start, uri:{}, caused by: ", request.getRequestURI(), e);

        return Result.fail(ResultEnum.SYSTEM_INNER_ERROR);
    }

    /**
     * 捕捉其他所有异常
     *@author gujiachun
     *@date 2021/9/13 2:05 下午
     *@param e 异常
     *@param request 请求对象
     *@return com.lixiang.common.core.Result
    */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Result<Void> handleOtherException(Exception e,HttpServletRequest request){
        logger.error("handleOtherException start, uri:{}, caused by: {}", request.getRequestURI(), e);

        return Result.fail(ResultEnum.SYSTEM_INNER_ERROR);
    }

}
