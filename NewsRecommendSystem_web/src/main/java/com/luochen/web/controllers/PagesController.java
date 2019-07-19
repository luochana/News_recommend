package com.luochen.web.controllers;

import com.luochen.web.Dao.*;
import com.luochen.web.Dao.interf.*;
import com.luochen.web.register.utils.CodeUtils;
import com.luochen.web.register.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.util.*;

import static com.luochen.tools.RandomNums.randomArray;
import static com.luochen.web.register.tools.registerKits.testUserAccount;

@SuppressWarnings("deprecation")
@Controller
@EnableAutoConfiguration
public class PagesController {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ItemSimRepository itemSimRepository;
    @Autowired
    private UserCFRepository userCFRepository;
    @RequestMapping("/test")
    public String test()
    {
        return "pages/readMore";
    }

    @RequestMapping("pages/getNewsContent")
    @ResponseBody
    public ajaxNews getNewsContent(@RequestParam("id")String id)
    {
        ItemSim temp=itemSimRepository.findNews(Long.valueOf(id));
        ajaxNews news=new ajaxNews();
        news.setTitle(temp.getArticleTitle());
        news.setContent(temp.getArticleContent());
        return news;
    }




    @RequestMapping("/pages")
    public String readMore(@RequestParam("id")String id, Model model)
    {
        model.addAttribute("id",id);
        return "pages/readMore";
    }
    @RequestMapping("/index")
    public String hello(HttpSession session)
    {
        session.setAttribute("channel","recommend");
        return "pages/index";
    }

    @ResponseBody
    @RequestMapping("pages/getNews")
    public List<ajaxNews> getNews(@RequestParam("id")String id,HttpSession session) {

        System.out.println(id);
        List<ajaxNews> ajaxNewsList=new ArrayList();
        Long newsNum = itemSimRepository.findCount();
        if(id.isEmpty()||session.getAttribute("visitedFlag")=="visited")
        {
            //Long newsNum = itemSimRepository.findCount();
            int randomNewsId[] = randomArray(0, newsNum.intValue(), 6);
            for (int i = 0; i < 6; ++i) {
                System.out.println(randomNewsId[i]);
                ItemSim temp = itemSimRepository.findNews((long) randomNewsId[i]);
             //   ItemSim temp = targetNews.get();
                ajaxNews temp1 = new ajaxNews();
                temp1.setTitle(temp.getArticleTitle());
                //  temp1.setContent(temp.getContent().substring(0, 80) + "....");
                temp1.setUrl("/pages?id=" + randomNewsId[i]);
                ajaxNewsList.add(temp1);
            }
            return ajaxNewsList;
        }
        else
        {
            session.setAttribute("visitedFlag","visited");
            List<Long> userId=new ArrayList<>();
            Optional<UserCF> userCF=userCFRepository.findById(Long.parseLong(id));
            UserCF temp=userCF.get();
            String recommendStr=temp.getRecommendStr();
            String recommendIds[]= recommendStr.split(",");
            for (int i=0;i<recommendIds.length;++i)
            {
                Optional<ItemSim> targetNews=itemSimRepository.findById(Long.parseLong(recommendIds[i]));
                ItemSim tempItemSim=targetNews.get();
                ajaxNews temp1=new ajaxNews();
                temp1.setTitle(tempItemSim.getArticleTitle());
                temp1.setUrl("/pages?id=" + recommendIds[i]);
                ajaxNewsList.add(temp1);
            }

            if(recommendIds.length<6)
            {
                int randomNewsId[] = randomArray(0, newsNum.intValue(), 6-recommendIds.length);
                for (int i = 0; i < randomNewsId.length; ++i) {
                    System.out.println(randomNewsId[i]);
                    Optional<ItemSim> targetNews = itemSimRepository.findById((long) randomNewsId[i]);
                    ItemSim temp2 = targetNews.get();
                    ajaxNews temp1 = new ajaxNews();
                    temp1.setTitle(temp2.getArticleTitle());
                    //  temp1.setContent(temp.getContent().substring(0, 80) + "....");
                    temp1.setUrl("/pages?id=" + randomNewsId[i]);
                    ajaxNewsList.add(temp1);
                }
            }
           return ajaxNewsList;
        }
    }



    @RequestMapping("Login")
    public String Login()
    {
        return "pages/login";
    }
    @RequestMapping("register")
    public String Register()
    {
        return "pages/register";
    }
    @RequestMapping("about")
    public String About(){return "pages/about";}
    @RequestMapping("dealRegister")
    public String dealRegister(@RequestParam("username")String username, @RequestParam("password")String password, @RequestParam("email")String email,HttpSession session)
    {
        if(testUserAccount(username,password,email))
        {
            if(userAccountRepository.findByUsername(username,email).isEmpty())
            {
                int id;
                try {
                    id = Integer.parseInt(userAccountRepository.findLastAccount())+1;
                }catch (Exception e)
                {
                    id=1;
                }
                try {
                    String code = CodeUtils.generateUniqueCode() + CodeUtils.generateUniqueCode();
                    new Thread(new MailUtils(email,code)).start();
                    userAccount userAccount = new userAccount();
                    userAccount.setId(id);
                    userAccount.setUsername(username);
                    userAccount.setPassword(password);
                    userAccount.setEmail(email);
                    userAccount.setState(0);
                    userAccount.setCode(code);
                    userAccountRepository.save(userAccount);
                    session.setAttribute("msg","激活邮件成功发送，请前往邮箱激活！");
                }catch (Exception e)
                {
                    e.printStackTrace();
                    session.setAttribute("msg","邮件发送失败，请重新操作！");
                }
                return "redirect:/success.html";
            }
            else
            {
                session.setAttribute("msg","该用户名或邮箱已被注册，请重新输入！");
                return "redirect:/register.html";
            }
        }
        else
        {
            return "pages/error";
        }
    }
    @RequestMapping("activateAccount")
    public String activateAccount(@RequestParam("code")String code, HttpServletRequest request)
    {
        List<userAccount> opt=userAccountRepository.findByCode(code);
        if(!opt.isEmpty())
        {
            userAccount userAccount=opt.get(0);
            userAccount.setState(1);
            userAccountRepository.save(userAccount);
            request.setAttribute("msg","激活成功，请前往登录");
            request.setAttribute("url","http://localhost:8080/Login");
        }
        else
        {
            request.setAttribute("msg","激活码有误！");
        }
        return "pages/activateResult";
    }


    @PostMapping(value = "dealLogin")
    public String dealLogin(@RequestParam("name")String name, @RequestParam("password")String password, HttpSession session)
    {
        try {
            List<userAccount> userAccList = userAccountRepository.findByUsername(name, name);
            if (!userAccList.isEmpty()) {
                userAccount temp = userAccList.get(0);
                if (temp.getPassword().equals(password)) {
                    session.setAttribute("msg1", temp.getUsername());
                    session.setAttribute("usrId", temp.getId());
                    session.setAttribute("channel","recommend");
                //    System.out.println(temp.getId());
                    return "redirect:/index.html";
                } else {
                    session.setAttribute("msg", "密码错误，请重新输入！");
                    return "redirect:/login.html";
                }
            } else {
                session.setAttribute("msg", "该用户不存在！");
                return "redirect:/login.html";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       return "pages/error";
    }


    @ResponseBody
    @GetMapping("ReadMore")
    public List<ajaxNews> ReadMore(@RequestParam("id")String id)
    {
        List<ajaxNews> ajaxNewsList=new ArrayList<>();
        ItemSim targetNews=itemSimRepository.findNews(Long.parseLong(id));
        String recommendIds[]=targetNews.getSim().split(",");
        for (int i=0;i<recommendIds.length-1;++i)
        {
            ItemSim temp=itemSimRepository.findNews(Long.parseLong(recommendIds[i]));
            ajaxNews tempNews=new ajaxNews();
            tempNews.setTitle(temp.getArticleTitle());
            tempNews.setUrl("/pages?id="+recommendIds[i]);
            ajaxNewsList.add(tempNews);
        }

        return ajaxNewsList;
    }


    @ResponseBody
    @RequestMapping("getChannelNews")
    public List<ajaxNews> getClassifyNews(@RequestParam("channel") String channel,@RequestParam("index") String index,HttpSession session)
    {
        List<ajaxNews> ajaxNewsList = new ArrayList<ajaxNews>();
        List<ItemSim> classifyNews = new ArrayList<ItemSim>();
        try {
            classifyNews =itemSimRepository.getClassifyNews( channel, Integer.parseInt(index) * 6, 6);
            for (ItemSim nskwd : classifyNews) {
                ajaxNews temp = new ajaxNews();
                temp.setTitle(nskwd.getArticleTitle());
                temp.setUrl("/pages?id=" + nskwd.getNewsId());
                ajaxNewsList.add(temp);
            }
            session.setAttribute("channel",channel);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ajaxNewsList;
    }
}
