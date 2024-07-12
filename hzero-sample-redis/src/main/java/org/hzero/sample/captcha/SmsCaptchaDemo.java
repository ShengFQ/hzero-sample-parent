package org.hzero.sample.captcha;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.choerodon.core.exception.CommonException;

import org.hzero.boot.message.MessageClient;
import org.hzero.boot.message.entity.Receiver;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.captcha.CaptchaMessageHelper;
import org.hzero.core.captcha.CaptchaResult;
import org.hzero.core.exception.MessageException;
import org.hzero.core.util.Results;

/**
 * 使用短信验证码示例
 *
 * @author bojiangzhou 2020/02/04
 */
@Controller
public class SmsCaptchaDemo {

    private  CaptchaMessageHelper captchaMessageHelper;
    private  MessageClient messageClient;

    // 消息模板代码，在消息发送配置中维护
    private static final String MSG_TEMPLATE = "DEMO.MSG_TEMPLATE";

    private static final String CACHE_PREFIX = "demo";
    @Autowired
    public SmsCaptchaDemo(CaptchaMessageHelper captchaMessageHelper, MessageClient messageClient) {
        this.captchaMessageHelper = captchaMessageHelper;
        this.messageClient = messageClient;
    }

    /**
     * 生成手机验证码，缓存验证码。返回captcha、captchaKey，需自己调用消息服务发送消息。
     */
    @GetMapping("/captcha/mobile")
    public ResponseEntity<CaptchaResult> createMobileCaptcha(@ApiParam("国际冠码，默认+86") @RequestParam(defaultValue = BaseConstants.DEFAULT_CROWN_CODE) String internationalTelCode,
                                                   @ApiParam("发送验证码的手机号") @RequestParam String phone) {
        // 生成手机验证码
        CaptchaResult captchaResult = captchaMessageHelper.generateMobileCaptcha(internationalTelCode, phone, CACHE_PREFIX);
        if (!captchaResult.isSuccess()) {
            throw new MessageException(captchaResult.getMessage(), captchaResult.getCode());
        }

        // 封装消息模板参数
        Map<String, String> params = new HashMap<>(8);
        params.put(CaptchaResult.FIELD_CAPTCHA, captchaResult.getCaptcha());
        try {
            // 调用消息客户端发送消息
            messageClient.async().sendMessage(
                    0L,
                    MSG_TEMPLATE,
                    null,
                    Collections.singletonList(new Receiver().setPhone(phone).setIdd(internationalTelCode)),
                    params,
                    Collections.singletonList("SMS")
            );
        } catch (Exception e) {
            throw new CommonException("hiam.warn.sendPhoneError");
        }

        captchaResult.clearCaptcha();

        // 返回验证码key到前端
        return Results.success(captchaResult);
    }

    /**
     * 生成邮箱验证码，缓存验证码。返回captcha、captchaKey，需自己调用消息服务发送消息。
     */
    @GetMapping("/captcha/email")
    public ResponseEntity<CaptchaResult> createEmailCaptcha(@ApiParam("发送验证码的邮箱") @RequestParam String email) {
        // 生成邮箱验证码
        CaptchaResult captchaResult = captchaMessageHelper.generateEmailCaptcha(email, CACHE_PREFIX);
        if (!captchaResult.isSuccess()) {
            throw new MessageException(captchaResult.getMessage(), captchaResult.getCode());
        }

        // 与发送短信验证码类似...

        // 返回验证码key到前端
        return Results.success(captchaResult);
    }

    /**
     * 验证验证码的正确性，需传入获取验证码时生成的 captchaKey，以及用户输入的验证码 captcha
     */
    @GetMapping("/captcha/validate")
    @ResponseBody
    public void validate(String captchaKey, String captcha, String phoneOrEmail) {
        // 1.验证验证码，第四个参数：是否缓存验证结果，一般在分步骤验证时，可设置为 true，验证通过 CaptchaResult 会返回验证通过的一个Key，用于二次判断
        CaptchaResult result = captchaMessageHelper.checkCaptcha(captchaKey, captcha, CACHE_PREFIX, false);

        if (result.isSuccess()) {
            // 验证码正确...
        } else {
            // 验证码错误...
        }
    }

    @GetMapping("/captcha/validate/number1")
    @ResponseBody
    public void validateWithNumber(String captchaKey, String captcha, String phoneOrEmail) {
        // 1.验证验证码，第三个参数传入获取验证码的手机号或邮箱号，是否一致
        CaptchaResult result = captchaMessageHelper.checkCaptcha(captchaKey, captcha, phoneOrEmail, CACHE_PREFIX, false);

        if (result.isSuccess()) {
            // 验证码正确...
        } else {
            // 验证码错误...
        }
    }

    /**
     * 分步骤验证
     */
    @GetMapping("/captcha/validate/number2")
    @ResponseBody
    public void validateWithNumber(String captchaKey, String captcha) {
        // 1. 第一步用户获取验证码，比如可调用 createMobileCaptcha 接口

        // 2. 第二步用户输入验证码，先校验验证码是否正确，需缓存验证码结果，第四个参数设置为 true
        CaptchaResult result = captchaMessageHelper.checkCaptcha(captchaKey, captcha, CACHE_PREFIX, true);
        // 将 lastCheckKey 返回给前端
        String lastCheckKey = result.getLastCheckKey();

        // 3. 第三步操作时，验证用户验证码是否验证通过；前端需传入 lastCheckKey
        CaptchaResult result2 = captchaMessageHelper.checkLastResult(lastCheckKey, CACHE_PREFIX);
        // 验证 result2

        // ... 其它业务操作 ...
    }


}
