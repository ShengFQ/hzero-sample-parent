package org.hzero.samples.core.context;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 全局SID返回设置，从request parameter或header获取sid参数，并设置到返回对象中。
 */
public class SIDReturnValueHandler implements HandlerMethodReturnValueHandler {
    private RequestResponseBodyMethodProcessor target;

    public SIDReturnValueHandler(RequestResponseBodyMethodProcessor target) {
        this.target = target;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        Method method = returnType.getMethod();
        if (method != null) {
            Class<?> returnObj = method.getReturnType();
            if (returnObj == null)
                return false;
           /* if (returnObj.equals(ApiResponse.class)) {
                return true;
            }
            if (returnObj.equals(ApiPageResponse.class)) {
                return true;
            }*/
        }
        return false;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        String sid = EssContextHolder.getSID();
        if (StringUtils.hasText(sid)) {
            /*if (returnValue instanceof AbstractResponse) {
                AbstractResponse response = (AbstractResponse) returnValue;
                response.setSid(sid);
                target.handleReturnValue(response, returnType, mavContainer, webRequest);
            }*/
        }
        target.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}
