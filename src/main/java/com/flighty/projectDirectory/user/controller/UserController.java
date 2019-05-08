package com.flighty.projectDirectory.user.controller;

import com.flighty.projectDirectory.util.UuidUtil;
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
        if(wxOpenId==null){
            result.put("resultType",500);
            return result;
        }
        System.out.println("微信openid-->"+wxOpenId);
        paramt.put("wxOpenId",wxOpenId);
        int userCount = userService.selectWx(paramt);
        if(userCount==0){
            System.out.println("新用户注册");
            paramt.remove("wxCode");
            paramt.put("id", UuidUtil.get32UUID());
            paramt.put("grade", 1);
            paramt.put("experience", 0);
            paramt.put("star_class", 1);
            paramt.put("diamond", 0);
            Map businessDataMap = new HashMap<>();
            businessDataMap.put("params", paramt);
            int registerUser = userService.wxRegister(businessDataMap);
            System.out.println("用户注册结果-->"+registerUser);
            result.put("newUser",true);
        }else{
            System.out.println("老用户");
            result.put("newUser",false);
        }
        Map userMap = userService.selectUser(paramt);
        result.put("userMap",userMap);

        return result;
    }

}
