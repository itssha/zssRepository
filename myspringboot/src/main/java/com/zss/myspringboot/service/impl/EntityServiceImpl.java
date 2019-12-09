package com.zss.myspringboot.service.impl;



import com.zss.myspringboot.entity.BaseEntity;
import com.zss.myspringboot.model.ListParam;
import com.zss.myspringboot.model.Message;
import com.zss.myspringboot.module.condition.ConditionQuery;
import com.zss.myspringboot.module.condition.ui.ConditionModel;
import com.zss.myspringboot.module.condition.ui.OrderBy;
import com.zss.myspringboot.module.condition.util.MapUtil;
import com.zss.myspringboot.repository.EntityRepository;
import com.zss.myspringboot.service.EntityService;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public abstract class EntityServiceImpl<T extends BaseEntity> implements EntityService<T,Integer> {

    private Class <T> entityClass;

    public EntityServiceImpl(){
        //从泛型T，获取到Class<T>，后续需要用到
        initEntityClass();
    }

    private void initEntityClass(){
        entityClass=null;
        Type t=getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p=((ParameterizedType)t).getActualTypeArguments();
            entityClass=(Class <T>)p[0];
        }
    }

    @Override
    public abstract EntityRepository<T, Integer> getRepository();//抽象方法，强迫子类必须重写，提供自己的实现



     //获取所有数据（已过滤active）
     @Override
     public  List<T> getAll()
     {
        // return  getRepository().findAll();
         return  getRepository().findAllByActive(true);
     }



    //单条添加
    @Override
    public Message<T> add(T entityNew) {
        System.out.println("EntityServiceImpl.add");
        System.out.println(entityNew);
        Message<T> message = null;
        try {
            Integer id=entityNew.getId();
            if(id==null){//一般路径
                 entityNew.setCreateTime(new Date());    //创建时间
                T result = getRepository().save(entityNew);
                message = new Message(0, "成功");
                message.setData(result);
            }
            /*else{//用户添加路径
                T entity = getEntity(id);
                if (entity != null) {
                    message = new Message(1, "已存在！");//用户不能重复添加，并且用户名相同时，save就会变成修改原来的用户属性了。
                    message.setData(entity);
                } else {
                    T result = getRepository().save(entityNew);
                    message = new Message(0, "成功");
                    message.setData(result);
                }
            }*/
        } catch (Exception e) {
            System.out.println(e.toString());
            message = new Message(1, e.toString());
        }
        return message;
    }
    //多条添加(待验证)

    @Transactional(rollbackFor = Exception.class)
    public  Message<List<T>> addList(ListParam<T> listParam) {
        Message<List<T>> message = new Message();
        List<T> list = listParam.getList();
        if (list == null) return message;
        try {
            for (int i = 0; i < list.size(); i++) {
                T t = list.get(i);
                T result = getRepository().save(t);
            }
            message.setSuccess(list);
        } catch (Exception e) {
            message.setFailure(e);
        }
        return message;
    }
    //修改
    @Override
    public Message<T> update(T entity) {
        Message<T> message =null;
        try {
            T entityOld=getEntity(entity.getId());
            if (entityOld != null) {
                entity.setModifyTime(new Date());  //修改时间
                getRepository().save(entity);
                message = new Message(0, "成功");
                message.setData(entity);
            } else {
                message = new Message(1, "未找到该条数据! id="+entity.getId());
            }
        } catch (Exception e) {
            message = new Message(1, e.toString());
        }
        return message;
    }

    @Override
    public Message<T> update(Map<String,Object> map){
        Message<T> msg=null;
        try{
            Object id=map.get("id");
            Object updateTime=map.get(new Date());
            T entity=getEntity(Integer.parseInt(id+""));
            if(entity==null){
            }else{
                MapUtil.mapToObject(map,entity);//设置属性后，似乎自动回保存到数据库？
            }
            msg=new Message(0,"成功");
            msg.setData(entity);
        }catch (Exception ex){
            msg=new Message(1,"失败");
        }

        return msg;
    }
    //根据ID删除数据（单表）
    @Override
    public Message<T> deleteById(Integer id)
    {
      return  getMessage(getEntity(id));
    }
    //根据类删除（存在关联关系的多表删除）
    @Override
    public Message<T>  delete(T entity)
    {
        T entityOld=getEntity(entity.getId());
        return  getMessage(entityOld);
    }

    //物理删除
    @Override
    public  Message  deleteTrue(T entity)
    {
        Message<T> message=new Message<>(0,"成功！");
        try{
            getRepository().delete(entity);
        }
        catch (Exception ex)
        {
            return  new Message<>(1,ex.toString());
        }
       return message;
    }



    private Message<T> getMessage(T entity)
    {
        Message<T> message=null;
        try {
            if (entity != null) {
//                getRepository().delete(entity);
                //使用逻辑删除，修改数据状态
                entity.setActive(false);  //删除状态
                entity.setDeleteTime(new Date()); //删除时间
                getRepository().save(entity);
                message = new Message(0, "成功");
                message.setData(entity);
            } else {
                message = new Message(1, "未找到该条数据! id="+entity.getId());
            }
        } catch (Exception e) {
            message = new Message(1, e.toString());
        }
        return  message;
    }


//全部删除（物理删除，慎用）
    @Override
    public List<T> deleteAll() {
        List<T> all=getAll();
        getRepository().deleteAllInBatch();
        return all;
    }



    @Override
    public Message<T> get(Integer id) {
        Message<T> msg=null;
        //T entity=getEntity(id);
        //过滤Active
        T entity=getActiveEntity(id,true);
        if(entity==null){
            msg=new Message<>(1,"未找到该条数据! id="+id);
        }
        else{
            msg=new Message<>(0,"成功");
            msg.setData(entity);
        }
        return msg;
    }


    public T getEntity(Integer id) {
        Optional<T> op = getRepository().findById(id);
        if(op.isPresent()){
            return op.get();
        }
        return null;
    }

     public  T getActiveEntity(Integer id,Boolean active)
     {
         Optional<T> op = getRepository().findByIdAndActive(id,active);
         if(op.isPresent()){
             return op.get();
         }
         return null;
     }

    @Override
    public Page<T> getPage(Pageable pageable)
    {
       // return getRepository().findAll(pageable);
        return  getRepository().findAllByActive(pageable,true);
    }

    @Resource
    private EntityManager entityManager;//必须有

    @Override
    public Message<List<T>> queryAll(ConditionModel<T> condition){
        Message<List<T>> msg=new Message<>();
        try{
            T entity=condition.getEntity();
            if(entity==null){
                ConditionQuery queryUtil=new ConditionQuery(entityManager,entityClass,condition);
                List<T> list= queryUtil.findList();
                msg.setSuccess(list);
            }
            else{ //QueryByExample
                ExampleMatcher matcher = ExampleMatcher.matching();
                Example<T> ex = Example.of(entity, matcher);
                List<T> list = getRepository().findAll(ex);
                msg.setSuccess(list);
            }
        }catch (Exception ex){
            msg.setFailure(ex);
        }
        return msg;
    }

    @Override
    public Message<Page<T>> queryPage(ConditionModel<T> condition) {
        Message<Page<T>> msg = new Message<>();
        try {
            System.out.println("queryPage:"+condition);
            T entity = condition.getEntity();
            T manyToManyEntity=condition.getManyToManyEntity();
            Pageable pageable = getPageable(condition);
            if (entity == null && manyToManyEntity==null) {
                ConditionQuery queryUtil = new ConditionQuery(entityManager, entityClass, condition);
                Page<T> page = getRepository().findAll((itemRoot, query, criteriaBuilder) -> {  //需要JpaSpecificationExecutor接口
                    Predicate result = queryUtil.getPredicate(itemRoot, query, criteriaBuilder);
                    return result;
                }, pageable);
                msg.setSuccess(page);
            } else if(manyToManyEntity!=null){
                ConditionQuery queryUtil = new ConditionQuery(entityManager, entityClass, condition);
                Page<T> page = getRepository().findAll((itemRoot, query, criteriaBuilder) -> {  //需要JpaSpecificationExecutor接口
                    Predicate result = queryUtil.getPredicateTest(itemRoot, query, criteriaBuilder);
                    return result;
                }, pageable);
                msg.setSuccess(page);
            }else{ //QueryByExample
                ExampleMatcher matcher = ExampleMatcher.matching();
                Example<T> ex = Example.of(entity, matcher);
                Page<T> page = getRepository().findAll(ex, pageable);
                msg.setSuccess(page);
            }
        } catch (Exception ex) {
            msg.setFailure(ex);
        }
        return msg;
    }
    public Message<Page<T>> queryPageTest(ConditionModel<T> condition) {
        Message<Page<T>> msg = new Message<>();
        try {
            System.out.println("queryPage:"+condition);
            T entity = condition.getEntity();
            Pageable pageable = getPageable(condition);

                ConditionQuery queryUtil = new ConditionQuery(entityManager, entityClass, condition);
                Page<T> page = getRepository().findAll((itemRoot, query, criteriaBuilder) -> {  //需要JpaSpecificationExecutor接口
                    Predicate result = queryUtil.getPredicateTest(itemRoot, query, criteriaBuilder);
                    return result;
                }, pageable);
                msg.setSuccess(page);

        } catch (Exception ex) {
            msg.setFailure(ex);
        }
        return msg;
    }
    public Pageable getPageable(ConditionModel<T> condition){
        List<Sort.Order> orders=new ArrayList<>();
        for (OrderBy orderBy : condition.getOrderBys()) {
            if (orderBy.isAsc()) {
                orders.add(Sort.Order.asc(orderBy.getColumn()));
            } else {
                orders.add(Sort.Order.desc(orderBy.getColumn()));
            }
        }
        Pageable pageable = PageRequest.of (condition.getPage(),condition.getLimit(), Sort.by(orders));
        return pageable;
    }

    public Message<Long> getCount(){
//        Long count= getRepository().count();
        //过滤Active
        Long count=getRepository().countByActive(true);
        Message<Long> msg=new Message<>();
        msg.setSuccess(count);
        return msg;
    }
}
