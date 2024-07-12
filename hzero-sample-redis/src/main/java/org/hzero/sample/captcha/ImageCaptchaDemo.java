package org.hzero.sample.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.hzero.core.captcha.CaptchaImageHelper;
import org.hzero.core.captcha.CaptchaResult;

/**
 * 使用图形验证码示例
 *
 * @author bojiangzhou 2020/02/04
 */
@Controller
public class ImageCaptchaDemo {

    private final CaptchaImageHelper captchaImageHelper;

    private static final String CACHE_PREFIX = "demo";
    @Autowired
    public ImageCaptchaDemo(CaptchaImageHelper captchaImageHelper) {
        this.captchaImageHelper = captchaImageHelper;
    }

    /**
     * 生成验证码并输出图片到指定输出流，验证码的key为UUID，设置到Cookie中，key和验证码将缓存到Redis中
     */
    @GetMapping("/captcha/image")
    public void createCaptcha(HttpServletResponse response) {
        // 第二个参数一般为服务简码，用于缓存验证码
        captchaImageHelper.generateAndWriteCaptchaImage(response, CACHE_PREFIX);
    }

    /**
     * 验证图形验证码的正确性
     */
    @GetMapping("/captcha/validate/key")
    @ResponseBody
    public void validate(String captchaKey, String captcha) {
        // 校验验证码
        CaptchaResult result = captchaImageHelper.checkCaptcha(captchaKey, captcha, CACHE_PREFIX);
        if (result.isSuccess()) {
            // 验证码正确...
        } else {
            // 验证码错误...
        }
    }

    /**
     * 验证图形验证码的正确性
     */
    @GetMapping("/captcha/validate/req")
    @ResponseBody
    public void validate(HttpServletRequest request, String captcha) {
        // 校验验证码，此方法会自动从 request 的 cookie 中获取 captchaKey
        CaptchaResult result = captchaImageHelper.checkCaptcha(request, captcha, CACHE_PREFIX);
        if (result.isSuccess()) {
            // 验证码正确...
        } else {
            // 验证码错误...
        }
    }

}
