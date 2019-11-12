package com.zss.myspringboot.module.condition;


import com.zss.myspringboot.module.condition.ui.ConditionExpression;
import com.zss.myspringboot.module.condition.ui.ConditionModel;
import com.zss.myspringboot.module.condition.ui.OrderBy;
import com.zss.myspringboot.module.condition.util.BeanUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConditionQuery<T> {

    private ConditionModel<T> cm;
    private EntityManager entityManager;

    private Class<T> entityClass;

    private Root<T> itemRoot;

    private CriteriaQuery<T> query;

    private CriteriaBuilder criteriaBuilder;

    //private EntityType<T> itemEntity;

    public ConditionQuery(EntityManager entityManager, Class<T> entityClass, ConditionModel cm){
        this.entityManager=entityManager;
        this.entityClass=entityClass;
        this.cm=cm;

        //创建CriteriaBuilder安全查询工厂
        //CriteriaBuilder是一个工厂对象,安全查询的开始.用于构建JPA安全查询.
        criteriaBuilder = entityManager.getCriteriaBuilder();
        //创建CriteriaQuery安全查询主语句
        //CriteriaQuery对象必须在实体类型或嵌入式类型上的Criteria 查询上起作用。
        query = criteriaBuilder.createQuery(entityClass);

        //Root 定义查询的From子句中能出现的类型
        itemRoot = query.from(entityClass);


    }

    public Predicate getPredicate(Root<T> itemRoot,CriteriaQuery<T> query,CriteriaBuilder criteriaBuilder){
        this.itemRoot=itemRoot;
        this.query=query;
        this.criteriaBuilder=criteriaBuilder;
        List<Predicate> predicatesList=getPredicateList();
        Predicate result= combinePredicateList(predicatesList);
        return result;
    }

    private Predicate combinePredicateList(List<Predicate> predicates){
        Predicate result=null;
        for (Predicate predicate : predicates) {
            if(result==null){
                result=predicate;
            }
            else{
                result=criteriaBuilder.and(result,predicate);
            }
        }
        return result;
    }

    private Predicate getPredicate(ConditionExpression condition, String column){
        String alias = cm.getAliasValue(column);
        EntityType<T> itemEntity=itemRoot.getModel();
        SingularAttribute attribute=itemEntity.getSingularAttribute(alias);
        Expression expression=itemRoot.get(attribute);
        String op = condition.getOp();
        Object value = condition.getValue();
        if (value == null || (value instanceof String && !StringUtils.hasText((String) value))) {
            return null;
        }
        Predicate predicate = null;
        Class<?> type = BeanUtil.findFieldType(entityClass, column);

        if (type == null) {
            return null;
        }
        Object v = ConvertUtils.convert(value, type);
        if (ConditionExpression.LK.equals(op)) {
            String val="";
            if (type.isAssignableFrom(String.class)) {
                /*MatchMode mode = MatchMode.ANYWHERE;
                if (ConditionExpression.START_MATCH.equals(condition.getMatchMode()))
                    mode = MatchMode.START;
                else if (ConditionExpression.END_MATCH.equals(condition.getMatchMode()))
                    mode = MatchMode.END;
                predicate = criteriaBuilder.like(expression,(String)v);//Restrictions.like(alias, (String) v, mode);*/
                val=(String)v;
            } else {
                // 对非字符串字段进行Like查询
                val = String.valueOf(v);
                /*if (ConditionExpression.START_MATCH.equals(condition.getMatchMode())) {
                    val += "%";
                } else if (ConditionExpression.END_MATCH.equals(condition.getMatchMode())) {
                    val = "%" + val;
                } else {
                    val = "%" + val + "%";
                }
                //predicate = Restrictions.sqlRestriction("{alias}." + alias.replaceAll("\\.", "_") + " like ?", val, Hibernate.STRING);
                predicate = criteriaBuilder.like(expression,val);*/
            }

            if (ConditionExpression.START_MATCH.equals(condition.getMatchMode())) {
                val += "%";
            } else if (ConditionExpression.END_MATCH.equals(condition.getMatchMode())) {
                val = "%" + val;
            } else {
                val = "%" + val + "%";
            }
            //val="'"+val+"'";//加上单引号
            //predicate = Restrictions.sqlRestriction("{alias}." + alias.replaceAll("\\.", "_") + " like ?", val, Hibernate.STRING);

            predicate = criteriaBuilder.like(expression,val);
            //现在有个问题 数字类型 用 like查询的话会出现 Parameter value [%1%] did not match expected type [java.lang.Integer (n/a)]
            //发现JPA就是不支持这么操作的 JPA是通用标准 因为有些数据库不支持这种转换的、有些支持 参考:https://stackoverflow.com/questions/5535084/jpa-like-operator-with-integer
        } else if (ConditionExpression.EQ.equals(op)) {
            predicate = criteriaBuilder.equal(expression, v);

        } else if (ConditionExpression.NEQ.equals(op)) {
            predicate = criteriaBuilder.notEqual(expression, v);

        } else if (ConditionExpression.GE.equals(op)) {
            predicate = criteriaBuilder.ge(expression, (Number)v);

        } else if (ConditionExpression.GT.equals(op)) {
            predicate = criteriaBuilder.gt(expression, (Number)v);

        } else if (ConditionExpression.LE.equals(op)) {
            predicate = criteriaBuilder.le(expression, (Number)v);

        } else if (ConditionExpression.LT.equals(op)) {

        } else if (ConditionExpression.NULL.equals(op)) {
            predicate = criteriaBuilder.isNull(expression);

        } else if (ConditionExpression.NOT_NULL.equals(op)) {
            predicate = criteriaBuilder.and(criteriaBuilder.isNotNull(expression), criteriaBuilder.not(criteriaBuilder.equal(expression, "")));

        } else if (ConditionExpression.IN.equals(op)) {
            if (value instanceof String) {
                String[] splits = ((String) value).split(",");
                Object[] values = new Object[splits.length];
                for (int i = 0; i < splits.length; i++) {
                    String val = splits[i];
                    if (StringUtils.hasText(val)) {
                        values[i] = ConvertUtils.convert(splits[i], type);
                    }
                }
                //predicate = criteriaBuilder.in(expression.in(values));
                CriteriaBuilder.In<Object> in=criteriaBuilder.in(expression);
                for (Object item:values ) {
                    in.value(item);
                }
                predicate=in;
            } else if (value instanceof Collection) {
                //predicate = criteriaBuilder.in(expression.in((Collection) value));//2019_03_17_cww:发现这里这样写不行。
                CriteriaBuilder.In<Object> in=criteriaBuilder.in(expression);
                for (Object item:(Collection)value ) {
                    in.value(item);
                }
                predicate=in;
            } else if (value instanceof Object[]) {
                //predicate = criteriaBuilder.in(expression.in((Object[]) value));
                CriteriaBuilder.In<Object> in=criteriaBuilder.in(expression);
                for (Object item:(Object[])value ) {
                    in.value(item);
                }
                predicate=in;
            }
        }
        return predicate;
    }

    private List<Predicate> getPredicateList(){
        List<Predicate> predicatesList = new ArrayList<>();
        //where()拼接查询条件*/
        for (String column : cm.getColumns()) {
            Collection<ConditionExpression> conditions = cm.getConditions(column);
            if (conditions == null || conditions.size() == 0)
                continue;
            Collection<Predicate> predicates = new ArrayList<Predicate>(conditions.size());
            for (ConditionExpression condition : conditions) {
                Predicate predicate=getPredicate(condition,column);
                //predicates.add(criteriaBuilder.and(predicate));
                predicates.add(predicate);
            }
            if (predicates != null && predicates.size() > 0) {
                for (Predicate predicate : predicates) {
                    if (predicate != null) {
                        predicatesList.add(predicate);
                    }
                }
            }
        }
        return predicatesList;
    }

    private List<Predicate> addPredicateAnd(List<Predicate> predicates){
        List<Predicate> predicatesList = new ArrayList<>();
        for (Predicate predicate : predicates) {
            predicatesList.add(criteriaBuilder.and(predicate));
        }
        return predicatesList;
    }

    public List<T> findList() {
        //Predicate 过滤条件 构建where字句可能的各种条件
        //这里用List存放多种查询条件,实现动态查询
        List<Predicate> predicatesList1=getPredicateList();
        List<Predicate> predicatesList2=addPredicateAnd(predicatesList1);
        query.where(predicatesList2.toArray(new Predicate[predicatesList2.size()]));
        // 创建排序
        for (OrderBy orderBy : cm.getOrderBys()) {
            if (orderBy.isAsc()) {
                query.orderBy(criteriaBuilder.asc(itemRoot.get(orderBy.getColumn())));
            } else {
                query.orderBy(criteriaBuilder.desc(itemRoot.get(orderBy.getColumn())));
            }
        }

        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        /*typedQuery.setFirstResult(cm.getPage());//第几行开始
        typedQuery.setMaxResults(cm.getSize());//一页多少行*/
        List<T> resultList = typedQuery.getResultList();
        return resultList;
    }
/*
    public Page<T> findPage(JpaSpecificationExecutor<T> repository){
        //QueryUtil queryUtil=new QueryUtil(entityManager,entityClass,cm);
        Pageable pageable = PageRequest.of (cm.getPage(),cm.getLimit(),null);
        //getRepository().findAll(request);
        Page<T> page=repository.findAll((itemRoot,q,criteriaBuilder)->{
            Predicate result=getPredicate(itemRoot,q,criteriaBuilder);
            return result;
        },pageable);
        return page;
    }*/
}
