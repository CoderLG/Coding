package lg.controller;

import io.swagger.annotations.Api;
import lg.bean.User;
import lg.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * author: LG
 * date: 2019-09-02 18:40
 * desc:
 *
 * 增    单条增加   多条增加   （自带）
 * 查    查一条     查所有     （自带）        段字段模糊查询_字段倒序_分页        （命名规则查询）
 * 改    改一条     改多条     （自带 先查在改）
 * 删    删一条     删一堆     （自带）        按某个名字模糊删除
 *
 *查询是没事务的
 */
@Api(tags = "JpaRepository中自带的方法")
@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    //JpaRepository 中自带的方法--------------------------

    //单条增加
    @GetMapping("defAddUser")
    @ResponseBody
    public void defAddUser(){
        User user = new User();
        user.setAge(13);
        user.setGridSet("set");
        user.setName("nn");
        userDao.save(user);
    }

    //多条增加
    @GetMapping("defAddUsers")
    @ResponseBody
    public void defAddUsers(){
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setAge(11);
        user.setGridSet("set");
        user.setName("mm");

        User user2 = new User();
        user2.setAge(22);
        user2.setGridSet("set");
        user2.setName("yy");

        users.add(user);
        users.add(user2);
        
        userDao.saveAll(users);
    }

    //查询
    @GetMapping("defFindAll")
    @ResponseBody
    @Transactional
    @Cacheable(value = "userCache")
    public List<User> defFindAll(){
        List<User> all = userDao.findAll(new Sort(Sort.Direction.DESC,"age"));
        System.out.println("update end");
        //int i =1/0;
        return all;
    }

    //修改
    @GetMapping("defUpdate")
    @ResponseBody
   // @Transactional
    public User defUpdate(){
        User one = userDao.getOne(2);
        one.setName("new name");
        User save = userDao.save(one);

        System.out.println("update end");
        int i =1/0;
        return save;
    }

    //删除
    @GetMapping("defDel")
    @ResponseBody
    public void defDel(){
        userDao.deleteById(1);
    }

    //删除全部
    @GetMapping("defDelAll")
    @ResponseBody
    public void defDelAll(){
        userDao.deleteAll();
    }


    //@query 方法------nativeQuery-----------------------------------------------

   //查询
    @GetMapping("querySomeThing")
    @ResponseBody
   // @Transactional
    public List<Object[]> querySomeThing(){
        System.out.println("start");
        List<Object[]> nn = userDao.querySomeThing("n%");
        System.out.println("end");
       // int i = 1/0;
        return  nn;
    }

    //修改
    @GetMapping("updateSomeThing")
    @ResponseBody
    @Transactional
    public void updateSomeThing(){
        System.out.println("start");
        userDao.updateSomeThing("yyy",3);
        System.out.println("end");
        // int i = 1/0;
    }


    //命名规则查询-----------------------------------------------------

    //查询
    @GetMapping("findByNameLikeOrderByIdDesc")
    @ResponseBody
    @Transactional
    @Cacheable(value = "cacheNameOrder", key = "'cache' + #name + #gridSet")
    public Page<User> findByNameLikeOrderByIdDesc(String name,String gridSet){

//        pageable 不只可以分页 还能放分页
//        Pageable pageable = PageRequest.of(currentPage - 1, pageSize,
//                new Sort((orderType != null && orderType.equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC,
//                        orderColumn));

        Pageable pageable = PageRequest.of(0, 2);

        Page<User> byNameLikeOrderByIdDesc = userDao.findByNameLikeAndGridSetLikeOrderByUserIdDesc(name+"%","%"+gridSet,pageable);

      //  System.out.println("end");
       // int i= 1/0;
        return  byNameLikeOrderByIdDesc;
    }



}