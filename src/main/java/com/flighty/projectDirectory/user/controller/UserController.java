package com.flighty.projectDirectory.user.controller;

import net.sf.json.JSONObject;
import com.flighty.projectDirectory.user.service.UserService;
import com.flighty.projectDirectory.util.HttpRequestUtil;
import com.flighty.projectDirectory.util.WeiXinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：用户登录注册Controller
 *
 * @author zhoushan
 * @create 2019/04/29 11:18
 */
@Controller
@RequestMapping(value = "flighty/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "wxLogin", produces = "application/json")
    @ResponseBody
    public Map wxLogin(@RequestBody Map paramt) {
        Map<String, Object> result = new HashMap<>();
        System.out.println("接收的数据-->"+paramt);

        String method = "GET";
        System.out.println("小程序获取openid");
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String str = "appid="+ WeiXinUtil.getMpAPP_ID()+"&secret="+ WeiXinUtil.getMpSECRET()+"&js_code="+paramt.get("wxCode")+"&grant_type=authorization_code";
        JSONObject openidStr = HttpRequestUtil.httpRequest(url,method,str);
        String wxOpenId = openidStr.get("openid") == null ? "" : openidStr.get("openid").toString();
        System.out.println("微信openid-->"+wxOpenId);
        paramt.put("wxOpenId",wxOpenId);
        userService.selectWx(paramt);

        return result;
    }

}
