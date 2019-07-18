package com.luochen.web.controllers;

import com.luochen.recommend.ContentBaseRecommender.ContentBaseRecommender;
import com.luochen.recommend.HotRecommender.hotRecommender;
import com.luochen.recommend.UserBasedCollaborativeRecommender.MahoutUserBasedCollaborativeRecommender;
import com.luochen.tools.RecommKits;
import com.luochen.web.Dao.ajaxNews;
import com.luochen.web.Dao.interf.*;
import com.luochen.web.Dao.newsKeywords;
import com.luochen.web.Dao.userAccount;
import com.luochen.web.register.utils.CodeUtils;
import com.luochen.web.register.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.*;

import static com.luochen.tools.RandomNums.randomArray;
import static com.luochen.web.register.tools.registerKits.testUserAccount;

@SuppressWarnings("deprecation")
@Controller
@EnableAutoConfiguration
public class PagesController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private NewsKeywordsRepository newsKeywordsRepository;
    @Autowired
    private NewsLogsRepository newsLogsRepository;
    @Autowired
    private RecommendationsRepository recommendationsRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @RequestMapping("/test")
    public String test()
    {
        return "pages/readMore";
    }

    @RequestMapping("pages/getNewsContent")
    @ResponseBody
    public ajaxNews getNewsContent(@RequestParam("id")String id)
    {
        Optional<newsKeywords> targetNews=newsKeywordsRepository.findById(Long.valueOf(id));
        newsKeywords temp=targetNews.get();
        ajaxNews news=new ajaxNews();
        news.setTitle(temp.getTitle());
        news.setContent(temp.getContent());
        temp.setViewNewsNum(temp.getViewNewsNum()+1);
        newsKeywordsRepository.save(temp);
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
    public List<ajaxNews> getNews(@RequestParam("id")String id,HttpSession session)
    {
       // session.setAttribute("channel","recommend");
        System.out.println(id);
        List<ajaxNews> ajaxNewsList=new ArrayList();
        if(id.isEmpty())
        {
            Long newsNum = newsKeywordsRepository.findCount();
            int randomNewsId[] = randomArray(0, newsNum.intValue(), 6);
            for (int i = 0; i < 6; ++i) {
                System.out.println(randomNewsId[i]);
                Optional<newsKeywords> targetNews = newsKeywordsRepository.findById((long) randomNewsId[i]);
                newsKeywords temp = targetNews.get();
                ajaxNews temp1 = new ajaxNews();
                temp1.setTitle(temp.getTitle());
              //  temp1.setContent(temp.getContent().substring(0, 80) + "....");
                temp1.setUrl("/pages?id=" + randomNewsId[i]);
                ajaxNewsList.add(temp1);

            }
            return ajaxNewsList;
        }
        else
        {
            List<Long> userId=new ArrayList<>();
            userId.add(Long.parseLong(id));
            MahoutUserBasedCollaborativeRecommender mahoutUserBasedCollaborativeRecommender=new MahoutUserBasedCollaborativeRecommender();
            Set<Long> set= mahoutUserBasedCollaborativeRecommender.recommend(userId,newsLogsRepository,newsKeywordsRepository,recommendationsRepository);
//            for(Long i:set)
//            {
//                System.out.println(i);
//            }
            if(set.size()<6)
            {
                hotRecommender hotrecommender=new hotRecommender();
               List<BigInteger> idList=hotrecommender.recommend(0,6-set.size(),newsKeywordsRepository);
               for(BigInteger hotNewsId:idList)
               {
                   set.add(hotNewsId.longValue());
                   System.out.println(hotNewsId);
               }
            }
            for (Long Id:set) {
                Optional<newsKeywords> targetNews = newsKeywordsRepository.findById(Id);
                newsKeywords temp = targetNews.get();
                ajaxNews temp1 = new ajaxNews();
                temp1.setTitle(temp.getTitle());
                //  temp1.setContent(temp.getContent().substring(0, 80) + "....");
                temp1.setUrl("/pages?id=" + Id);
                ajaxNewsList.add(temp1);
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
        HashMap<String,Double> map = new HashMap<String, Double>();
        Long Id=Long.parseLong(id);
        Optional<newsKeywords> targetNews=newsKeywordsRepository.findById(Id);
        newsKeywords temp=targetNews.get();
        String kwStr[]=temp.getKeyWords().split(";");
        for(String str : kwStr)
        {
            String arr[]=str.split(",");
            map.put(arr[0],Double.parseDouble(arr[1]));
        }
        ContentBaseRecommender recommender=new ContentBaseRecommender();
        HashMap<Long,Double> result=recommender.recommender(map,newsKeywordsRepository);
        List<ajaxNews> ajaxNewsList=new ArrayList<>();
        for(Long tempId:result.keySet())
        {
             targetNews=newsKeywordsRepository.findById(tempId);
             temp=targetNews.get();
             ajaxNews tempNews=new ajaxNews();
             tempNews.setTitle(temp.getTitle());
             tempNews.setUrl("/pages?id="+temp.getId());
             ajaxNewsList.add(tempNews);
            // System.out.println(temp.getTitle());
            // System.out.println(temp.getContent());
        }

        return ajaxNewsList;
    }

    @ResponseBody
    @RequestMapping("getChannelNews")
    public List<ajaxNews> getClassifyNews(@RequestParam("channel") String channel,@RequestParam("index") String index,HttpSession session)
    {
        List<ajaxNews> ajaxNewsList = new ArrayList<ajaxNews>();
        List<newsKeywords> classifyNews = new ArrayList<newsKeywords>();
        try {
            classifyNews = newsKeywordsRepository.getClassifyNews(RecommKits.getCurrentTimeStr(), channel, Integer.parseInt(index) * 6, 6);
            for (newsKeywords nskwd : classifyNews) {
                ajaxNews temp = new ajaxNews();
                temp.setTitle(nskwd.getTitle());
                temp.setUrl("/pages?id=" + nskwd.getId());
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
