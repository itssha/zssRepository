package com.zss.myspringboot.module.condition.ui;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zss.myspringboot.module.condition.util.JsonUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ConditionModel<T> {
    private static final String separator = "-";
   /* @JsonProperty("page")*/
    private Integer page ;
  /*  @JsonProperty("limit")*/
    private Integer limit = 15;
  /*  @JsonProperty("map")*/
    private Map<String, Object> map = new HashMap();

    private T entity;

  private T manyToManyEntity;

    private String manyToManyAttributeName ;
    private Integer manyToManyJoinById ;



    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    private Map<String, String> sort= new HashMap();

    @JsonIgnore
    private Map<String, String> hashAlias = new HashMap();
    @JsonIgnore
    private Map<String, Collection<ConditionExpression>> hashCondition = new HashMap();
    @JsonIgnore
    private Map<String, OrderBy> hashOrderBy = new LinkedHashMap();

    public ConditionModel() {
        System.out.println("构造函数0");
    }

    public ConditionModel(Map<String, Object> jsonMap) {
        System.out.println("构造函数1");
        this.setMap(jsonMap);
        this.generate();
    }

    public ConditionModel(Map<String, Object> jsonMap, Integer page, Integer limit) {
        System.out.println("构造函数2");
        this.setMap(jsonMap);
        this.page = page == null ? 0 : page;
        this.limit = limit == null ? 15 : limit;
        this.generate();
    }

    public ConditionModel(String json) {
        System.out.println("构造函数3");
        if (!JsonUtil.isBlank(json)) {
            this.setMap((Map)JsonUtil.decode(json, Map.class, String.class, String.class));
            this.generate();
        }
    }

    public ConditionModel(String json, Integer page, Integer limit) {
        System.out.println("构造函数4");

        this.limit = limit == null ? 15 : limit;
        this.page = page == null ? 0 : page;
        this.setMap((Map)JsonUtil.decode(json, Map.class, String.class, String.class));
        this.generate();
    }

    public ConditionModel(Integer page, Integer limit) {
        System.out.println("构造函数5");
        this.setPage(page);
        this.setLimit(limit);
    }

    public void generate() {
        //解析并生成查询条件
        Map<String, Object> mp = this.getMap();

        if (!CollectionUtils.isEmpty(mp)) {
            String column = null;
            String op = null;
            Object v = null;
            String matchMode = "anyMatch";
            hashCondition.clear();
            for(Iterator i$ = mp.keySet().iterator(); i$.hasNext(); this.addCondition(column, op, v, matchMode)) {
                String key = (String)i$.next();
                key=key.trim();//删除前后空格
                v = mp.get(key);
                //v=v.trim();//删除前后空格
                if (key.endsWith("eq")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "eq";
                } else if (key.endsWith("nq")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "nq";
                } else if (key.endsWith("gt")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "gt";
                } else if (key.endsWith("ge")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "ge";
                } else if (key.endsWith("lt")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "lt";
                } else if (key.endsWith("le")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "le";
                } else if (key.endsWith("lk")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "lk";
                } else if (key.endsWith("lks")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "lk";
                    matchMode = "startMatch";
                } else if (key.endsWith("lke")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "lk";
                    matchMode = "endMatch";
                } else if (key.endsWith("in")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "in";
                } else if (key.endsWith("nul")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "nul";
                } else if (key.endsWith("nnl")) {
                    column = key.substring(0, key.lastIndexOf("-"));
                    op = "nnl";
                } else {
                    column = key;
                    op = "lk";
                    matchMode = "anyMatch";
                }
            }
        }
     //   generateFromEntity();
     //zss   generateSort();//解析并生成排序规则
    }

    private void generateFromEntity(){
        if(entity!=null){//根据实体类创建查询条件 应用：SpaceRes根据City来过滤
            try{
                Class entityClass=entity.getClass();
                Field[] fields=entityClass.getDeclaredFields();
                for (int i=0;i<fields.length;i++){
                    Field field=fields[i];
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), entityClass);
                    Method rM = pd.getReadMethod();//获得读方法
                    Transient transientAnnotation = rM.getAnnotation(Transient.class);//获得方法注解
                    //有Transient注解的方法，不用添加到条件中
                    if(transientAnnotation==null){
                        Object value = rM.invoke(entity);
                        if(value!=null){
                            this.addCondition(field.getName(),"eq",value);
                        /*SpaceRes spaceRes=(SpaceRes)value;
                        City city=spaceRes.getCity();
                        this.addCondition("spaceRes.city","eq",city);*/
                        }
                    }
                }
                //todo:这里可以继续扩展成根据一个实体类对象创建复杂的查询条件
            }catch (Exception ex){
                System.out.println(ex);
            }
        }
    }

    //解析并生成排序规则
    private void generateSort() {
        Map<String, String> sortMap = this.getSort();
        if(hashOrderBy.isEmpty())
            if (!CollectionUtils.isEmpty(sortMap)) {
                for(Iterator i$ = sortMap.keySet().iterator(); i$.hasNext();) {
                    String key = (String)i$.next();
                    String v = sortMap.get(key).toLowerCase();
                    //考虑前端asc,desc没写或者写错的情况。
                    if(v.equals("desc")){//倒叙
                        addOrder(key,v);
                    }
                    else{
                        addOrder(key,"asc");//正序
                    }
                }
            }
    }

    public void addAlias(String column) {
        if (StringUtils.hasText(column) && this.hashAlias.get(column) == null) {
            if (column.indexOf(".") != -1) {
                String alias = column.substring(0, column.lastIndexOf("."));
                int pos = alias.indexOf(".");
                if (pos > -1) {
                    String[] aliases = alias.split("\\.");
                    String lastAlias = null;

                    for(int i = 0; i < aliases.length; ++i) {
                        String a = aliases[i];
                        if (lastAlias != null) {
                            a = lastAlias + "." + a;
                        }

                        if (!this.hashAlias.containsKey(a)) {
                            this.hashAlias.put(a, a.replaceAll("\\.", "_"));
                        }

                        lastAlias = a;
                    }
                } else if (!this.hashAlias.containsKey(alias)) {
                    this.hashAlias.put(alias, alias.replaceAll("\\.", "_"));
                }

            }
        }
    }

    public void removeAlias(String column) {
        if (StringUtils.hasText(column)) {
            if (column.indexOf(".") != -1) {
                String alias = column.substring(0, column.lastIndexOf("."));
                int pos = alias.indexOf(".");
                if (pos > -1) {
                    String[] aliases = alias.split("\\.");
                    String lastAlias = null;

                    for(int i = 0; i < aliases.length; ++i) {
                        String a = aliases[i];
                        if (lastAlias != null) {
                            a = lastAlias + "." + a;
                        }

                        if (this.hashAlias.containsKey(a)) {
                            this.hashAlias.remove(a);
                        }

                        lastAlias = a;
                    }
                } else if (this.hashAlias.containsKey(alias)) {
                    this.hashAlias.remove(alias);
                }

            }
        }
    }

    public int getAliasSize() {
        return this.hashAlias.size();
    }
    public Map<String, String> getSort() {
        return sort;
    }

    public boolean containsAlias(String alias) {
        return this.hashAlias.containsKey(alias);
    }

    public String getAliasValue(String alias) {
        String value = (String)this.hashAlias.get(alias);
        if (!StringUtils.hasText(value)) {
            int pos = alias.lastIndexOf(".");
            if (pos == -1) {
                value = alias;
            } else {
                value = alias.substring(0, pos).replaceAll("\\.", "_") + alias.substring(pos);
            }
        }

        return value;
    }

    public Set<String> getAliasKey() {
        return this.hashAlias.keySet();
    }

    public Collection<String> getAliasValues() {
        return this.hashAlias.values();
    }

    public ConditionExpression addCondition(String column, String op, Object value) {
        return this.addCondition(column, op, value, "anyMatch");
    }

    public ConditionExpression addCondition(String column, String op, Object value, String matchMode) {
        this.addAlias(column);
        ConditionExpression conditionEx = new ConditionExpression(column, op, value, matchMode);
        Collection<ConditionExpression> conditions = (Collection)this.hashCondition.get(column);
        if (conditions == null) {
            conditions = new ArrayList();
        }

        ((Collection)conditions).add(conditionEx);
        this.hashCondition.put(column, conditions);
        return conditionEx;
    }






    public void removeCondition(String column) {
        this.removeAlias(column);
        this.hashCondition.remove(column);
    }

    public int getConditionSize() {
        return this.hashCondition.size();
    }

    public ConditionExpression getCondition(String column) {
        Collection<ConditionExpression> conditions = this.getConditions(column);
        return conditions != null && conditions.size() > 0 ? (ConditionExpression)conditions.iterator().next() : null;
    }

    public Collection<ConditionExpression> getConditions(String column) {
        return StringUtils.hasText(column) && this.hashCondition != null && this.hashCondition.size() != 0 ? (Collection)this.hashCondition.get(column) : null;
    }

    public Set<String> getColumns() {
        Map mp=getMap();
        if(mp==null)return new HashSet<>();
        init();
        return this.hashCondition.keySet();
    }

    public void init(){
        Map mp=getMap();
        if(mp==null)return;
        if(mp.size()>0&&hashCondition.size()==0){
            generate();
        }
    }

    public void addOrder(String column, String direction) {
        if (StringUtils.hasText(column)) {
            this.addAlias(column);
            this.getHashOrderBy().put(column, new OrderBy(column, direction));
        }

    }

    public boolean hasOrderByColumn(String column) {
        return this.getHashOrderBy().containsKey(column);
    }

    public OrderBy getOrderBy(String column) {
        return (OrderBy) this.getHashOrderBy().get(column);
    }

    public Collection<OrderBy> getOrderBys() {
        generateSort();
        return this.getHashOrderBy().values();
    }

    public void clearOrderBy() {
        this.getHashOrderBy().clear();
    }

    public int getOrderByCount() {
        return this.getHashOrderBy().size();
    }

    public String toOrderByString() {
        String result = "";

        OrderBy orderBy;
        for(Iterator i$ = this.getHashOrderBy().values().iterator(); i$.hasNext(); result = "," + orderBy.toString()) {
            orderBy = (OrderBy)i$.next();
        }

        if (StringUtils.hasText(result)) {
            result = result.substring(1);
        }

        return result;
    }

    public String toOrderBySqlString() {
        String result = "";

        OrderBy orderBy;
        for(Iterator i$ = this.getHashOrderBy().values().iterator(); i$.hasNext(); result = "," + orderBy.toSqlString()) {
            orderBy = (OrderBy)i$.next();
        }

        if (StringUtils.hasText(result)) {
            result = result.substring(1);
        }

        return result;
    }

    public <T> Object getValue(String column, Class<T> valueClazz) {
        ConditionExpression condition = this.getCondition(column);
        return condition == null ? null : condition.getValue(valueClazz);
    }

    public String toString() {
        Map mp=getMap();
        if(mp==null){
            return toJsonString();
        }
        init();
        StringBuffer sb = new StringBuffer();
        Iterator i$ = this.getColumns().iterator();

        while(i$.hasNext()) {
            String column = (String)i$.next();
            ConditionExpression ce = this.getCondition(column);
            if (ce != null) {
                sb.append(", ").append(column).append(" ").append(ce.getOp()).append(" ").append(ce.getValue());
            }
        }
        if(sb.length()>0){
            return sb.substring(1);
        }
        else{
            return toJsonString();
        }
    }

    public String toJsonString() {
        return "Condition{" +
                "map=" + map+
                ", sort=" + sort+
                ", hashAlias=" + hashAlias +
                ", hashCondition=" + hashCondition +
                ", hashOrderBy=" + hashOrderBy +
                ", page=" + page +
                ", limit=" + limit +
                '}';
    }





    public Map<String, Object> getMap() {
        if(map.containsKey("active")){

        } else{
            map.put("active-eq","true");//所有的条件加上active=true
        }
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, OrderBy> getHashOrderBy() {
        return hashOrderBy;
    }

    public void setHashOrderBy(Map<String, OrderBy> hashOrderBy) {
        this.hashOrderBy = hashOrderBy;
    }


    public T getEntity() {
        return entity;
    }
    public T getManyToManyEntity() {
        return manyToManyEntity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
    public void setManyToManyEntity(T manyToManyEntity) {
        this.manyToManyEntity = manyToManyEntity;
    }
  // @JsonProperty
    public int getPage() {
        return this.page;
    }
   // @JsonProperty
    public int getLimit() {
        return this.limit;
    }
  //  @JsonProperty
    public void setPage(Integer page) {
        this.page = page == null ? 0 : page;
    }
   // @JsonProperty
    public void setLimit(Integer limit) {

        this.limit = limit == null ? 15 : limit;
    }

    public String getManyToManyAttributeName() {
        return manyToManyAttributeName;
    }

    public void setManyToManyAttributeName(String manyToManyAttributeName) {
        this.manyToManyAttributeName = manyToManyAttributeName;
    }

    public Integer getManyToManyJoinById() {
        return manyToManyJoinById;
    }

    public void setManyToManyJoinById(Integer manyToManyJoinById) {
        this.manyToManyJoinById = manyToManyJoinById;
    }
}
