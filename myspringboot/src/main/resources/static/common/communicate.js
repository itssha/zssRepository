function Condition(page,limit) {
    if(page==null){
        this.page=0;
    }else{
        this.page=page;
    }
    if(limit==null){
        this.limit=15;
    }else{
        this.limit=limit;
    }
    this.map={};
    this.sort={};
    this.entity=null;
    if (typeof this.setPropertyByJson != 'function') {
        Condition.prototype.setPropertyByJson = function (json) {
            console.log('condition.setPropertyByJson');
            if (typeof (json) == 'undefined') return;
            var obj = JSON.parse(json);
            this.page = obj.page;
            this.limit = obj.limit;
            this.map = obj.map;
            this.sort = obj.sort;
        }
    }
    if (typeof this.setPage != 'function') {
        Condition.prototype.setPage = function (page,limit) {
            console.log('condition.setPage:'+page+','+limit);
            if (typeof (page) == 'undefined') return;
            this.page=page;
            this.limit=limit;
        }
    }
    if (typeof this.addMap != 'function') {
        Condition.prototype.addMap = function (column,op,value) {
            console.log('condition.addMap');
            if (typeof (column) == 'undefined') return;
            var key=column+'-'+op;
            this.map[key]=value;
        }
    }
    if (typeof this.setEntity != 'function') {
        Condition.prototype.setEntity = function (entity) {
            console.log('condition.setEntity');
            if (typeof (entity) == 'undefined') return;
            this.entity=entity;
        }
    }
    if (typeof this.addSort != 'function') {
        Condition.prototype.addSort = function (column,sortType) {
            console.log('condition.addSort:'+column+","+sortType);
            if (column==null || typeof (column) == 'undefined') return;
            this.sort[column]=sortType;
        }
    }
    if (typeof this.asc != 'function') {
        Condition.prototype.asc = function (column) {
            console.log('condition.asc');
            if (column==null || typeof (column) == 'undefined') return;
            this.sort[column]='asc';
        }
    }
    if (typeof this.desc != 'function') {
        Condition.prototype.desc = function (column) {
            console.log('condition.desc');
            if (column==null || typeof (column) == 'undefined') return;
            this.sort[column]='desc';
        }
    }
}


function getResultData(result) {
    var data=result;
    if(result!=null && isDefined(result.state)){
        if(result.state==0){
            //alert('成功!');
            console.log('getResultData:成功!')
        }
        else{
            //alert(result.describe);
            console.log('getResultData:'+result.describe)
        }
        /*console.log('data');
        console.log(result.data);*/
        if(isDefined(result.data)) {//data is undefined User属性中有个state并且等于0
            data=result.data;
        }
        /*console.log(data);*/
    }
    return data;
}

function EntityDao(name) {
    console.log('EntityDao:'+name);
    /*this.ajax=new Ajax();
    this.name=name;*/
    if (typeof this.init != 'function') {
        EntityDao.prototype.init = function (name) {
            this.ajax=new Ajax();
            this.name=name;
        }
    }
    this.init(name);

    //console.log('EntityDao:'+name);
    if (typeof this.template != 'function') {
        EntityDao.prototype.template = function (callBack) {
            console.log('dao.template');
            var url=this.name+'/template';//这里必须是this.name不能用name
            this.ajax.get(url,null,callBack);
        }
    }
    if (typeof this.save != 'function') {
        EntityDao.prototype.save = function (entity,callBack) {
            var obj=this;
            console.log('dao.save');
            if(entity==null)return;
            if(entity.id==null || typeof(entity.id) == 'undefined'||entity.id==""||entity.id=="0"){
                obj.add(entity,callBack);
            }else{
                //更新 update,put,patch都行
                obj.update(entity,callBack);
                //obj.put(entity,callBack);
                //obj.patch(entity,callBack);
            }
        }
    }
    if (typeof this.add != 'function') {
        EntityDao.prototype.add = function (entity,callBack) {
            console.log('dao.add');
            var url=this.name+'/add';
            this.ajax.post(url,entity,callBack);
        }
    }
    if (typeof this.addList != 'function') {
        EntityDao.prototype.addList = function (entityList,callBack) {
            console.log('dao.add');
            var url=this.name+'/addList';
            var listParam={};
            listParam.list=entityList;//这里得封装一层
            this.ajax.post(url,listParam,callBack);
        }
    }
    if (typeof this.update != 'function') {
        EntityDao.prototype.update = function (entity,callBack) {
            console.log('dao.update');
            console.log(entity);
            var url=this.name+'/update';
            this.ajax.post(url,entity,callBack);
        }
    }
    if (typeof this.deleteTrue != 'function') {
        EntityDao.prototype.deleteTrue = function (entity,callBack) {
            console.log('dao.deleteTrue');
            console.log(entity);
            var url=this.name+'/deleteTrue';
            this.ajax.post(url,entity,callBack);
        }
    }
    if (typeof this.put != 'function') {
        EntityDao.prototype.put = function (entity,callBack) {
            console.log('dao.put');
            var url=this.name+'/put';
            this.ajax.put(url,entity,callBack);
        }
    }
    if (typeof this.patch != 'function') {
        EntityDao.prototype.patch = function (entity,callBack) {
            console.log('dao.patch');
            var url=this.name+'/patch';
            this.ajax.patch(url,entity,callBack);
        }
    }
    if (typeof this.get != 'function') {
        EntityDao.prototype.get = function (id,callBack) {
            console.log('dao.get:'+id);
            var url=this.name+'/'+id;
            this.ajax.get(url,null,callBack);
        }
    }
    if (typeof this.delete != 'function') {
        EntityDao.prototype.delete = function (id,callBack) {
            console.log('dao.delete:'+id);
            var url=this.name+'/'+id;
            this.ajax.delete(url,null,callBack);
        }
    }
    if (typeof this.getAllByPath != 'function') {
        EntityDao.prototype.getAllByPath = function (path,callBack) {
            console.log('dao.getAllByPath');
            //console.log(callBack);
            var url=this.name+'/'+path;
            this.ajax.get(url,null,callBack);
        }
    }
    if (typeof this.getAll != 'function') {
        EntityDao.prototype.getAll = function (callBack) {
            console.log('dao.getAll');
            //console.log(callBack);
            var url=this.name+'/list';
            this.ajax.get(url,null,callBack);
        }
    }
    if (typeof this.getPage != 'function') {
        EntityDao.prototype.getPage = function (page,size,callBack) {
            console.log('dao.getPage');
            var url=this.name+'/page';
            var condition={};
            condition.page=page;
            condition.size=size;
            this.ajax.get(url,condition,callBack);
        }
    }
    if (typeof this.queryAll != 'function') {
        EntityDao.prototype.queryAll = function (condition,callBack) {
            console.log('dao.queryAll');
            var url=this.name+'/queryAll';
            this.ajax.post(url,condition,callBack);
        }
    }
    if (typeof this.queryPage != 'function') {
        EntityDao.prototype.queryPage = function (condition,callBack) {
            this._queryPage(condition,callBack);
        }
    }

    //这个方法用于子类调重写queryPage时调用
    if (typeof this._queryPage != 'function') {
        EntityDao.prototype._queryPage = function (condition,callBack) {
            console.log('dao._queryPage');
            var url=this.name+'/queryPage';
            this.ajax.post(url,condition,callBack);
        }
    }

    if (typeof this.getCount != 'function') {
        EntityDao.prototype.getCount = function (callBack) {
            console.log('dao.getCount');
            var url=this.name+'/count';
            this.ajax.get(url,null,callBack);
        }
    }
}

function UserDao() {
    this.init('user');//调用父类的构造函数
    if (typeof this.login != 'function') {
        UserDao.prototype.login = function (user,pass,callBack) {
            console.log('userDao.login');
            var data={loginName: user,password:pass};
            var url='login/DbLogin';
            this.ajax.post0(url,data,callBack);
        }
    }
}
UserDao.prototype = new EntityDao();

function MenuDao() {
    this.init('menu');//调用父类的构造函数
    if (typeof this.getRoot != 'function') {
        MenuDao.prototype.getRoot = function (userId,callBack) {
            console.log('MenuDao.getRoot:'+userId);
            var url=this.name+'/root?userId='+userId;
            this.ajax.get(url,null,callBack);
        }
    }
}
MenuDao.prototype = new EntityDao();
/*//重写方法
MenuDao.prototype.queryPage = function (condition,callBack) {
    console.log('MenuDao.queryPage');
    this._queryPage(condition,function (result) {
        var data=getResultData(result);
        console.log(data);
        var content=data.content;
        //[1, 4, -5, 10].find(function(n){return});
        for(var i in content){
            var item=content[i];
            item.parent=content.find(function(n){
                return n.id==item.pid
            });
            console.log(item.parent);
        }
        if(callBack!=null){
            callBack(result);
        }
    })
}*/

function DevDao(name) {
    this.init(name);//调用父类的构造函数
    if (typeof this.statics != 'function') {
        DevDao.prototype.statics = function (callBack) {
            console.log('DevDao.statics');
            var url=this.name+'/statistic';
            this.ajax.get(url,null,callBack);
        }
    }
}
DevDao.prototype = new EntityDao();

function getDao(name) {
    console.log('getDao:'+name);
    if(name=='menu'){
        return new MenuDao();
    }
    else if(name=='user'){
        return new UserDao();
    }
    else{
        return new EntityDao(name);
    }
}


//document.write(”<script language=javascript src=’/js/import.js’></script>”);

function Ajax() {



    if (typeof this.send != 'function') {
        Ajax.prototype.send = function (type,url,data,sucess,error) {
            $.ajax({
                url:url,
                type:type,
                data:data,
                dataType:'json',
                async:false,
                success:sucess,
                error:error,
            })
        }
    }
    if (typeof this.get != 'function') {
        Ajax.prototype.get = function (url,data,callBack) {
            $.ajax({
                url:url,
                type:'GET',
                data:data,
                dataType:'json',
                success:function (result) {
                    console.log('成功')
                    console.log(result);
                    console.log(callBack);
                    if(callBack!=null)
                        callBack(result);
                },
                error:function () {
                    console.log('失败')
                    if(callBack!=null)
                        callBack(null);
                }
            })
        }
    }
    if (typeof this.post != 'function') {
        Ajax.prototype.post = function (url,data,callBack) {
            //this.post1(url,data,callBack); //后端没有@RequestBody
            this.post2(url,data,callBack); //后端有@RequestBody
        }
    }

    if (typeof this.post0 != 'function') {
        Ajax.prototype.post0 = function (url,data,callBack) {
            console.log('ajax.post0:'+url);
            console.log(data);
            $.ajax({
                type: "post",
                url: url,
                data:data,//这种方式后端简单的参数就行
                success: function (result) {
                    console.log('成功')
                    if(callBack!=null){
                        callBack(result);
                    }
                },
                error:function () {
                    console.log('失败')
                    if(callBack!=null)
                        callBack(null);
                }
            });
        }
    }

    if (typeof this.post1 != 'function') {
        Ajax.prototype.post1 = function (url,data,callBack) {
            console.log('ajax.post1:'+url);
            console.log(data);
            $.ajax({
                url:url,
                type:'POST',
                data:data,
            //    dataType:'json',
                success:function (result) {
                    console.log('成功')
                    console.log(result);
                    if(callBack!=null)
                        callBack(result);
                },
                error:function () {
                    console.log('失败')
                    if(callBack!=null)
                        callBack(null);
                }
            })
        }
    }

    if (typeof this.post2 != 'function') {
        Ajax.prototype.post2 = function (url,data,callBack) {
            console.log('ajax.post2:'+url);
            console.log(data);
            $.ajax({
                url:url,
                type:'POST',
              //  dataType:'json',
                contentType: 'application/json',//这种方式后端要加@ResquestBody
                data:JSON.stringify(data),//必须用JSON.stringify转换一下
                success:function (result) {
                    console.log('成功')
                    console.log(result);
                    if(callBack!=null)
                        callBack(result);
                },
                error:function (err) {
                    console.log(err)
                    console.log('失败')
                    if(callBack!=null)
                        callBack(null);
                }
            })
        }
    }

    if (typeof this.delete != 'function') {
        Ajax.prototype.delete = function (url,data,callBack) {
            $.ajax({
                url:url,
                type:'DELETE',
                data:data,
                dataType:'json',
                success:function (result) {
                    console.log('成功')
                    console.log(result);
                    if(callBack!=null)
                        callBack(result);
                },
                error:function () {
                    console.log('失败')
                    if(callBack!=null)
                        callBack(null);
                }
            })
        }
    }
    if (typeof this.put != 'function') {
        Ajax.prototype.put = function (url,data,callBack) {
            $.ajax({
                url:url,
                type:'PUT',
                data:data,
                dataType:'json',
                success:function (result) {
                    console.log('成功')
                    console.log(result);
                    if(callBack!=null)
                        callBack(result);
                },
                error:function () {
                    console.log('失败')
                    if(callBack!=null)
                        callBack(null);
                }
            })
        }
    }
    if (typeof this.patch != 'function') {
        Ajax.prototype.patch = function (url,data,callBack) {
            $.ajax({
                url:url,
                type:'PATCH',
                data:data,
                dataType:'json',
                success:function (result) {
                    console.log('成功')
                    console.log(result);
                    if(callBack!=null)
                        callBack(result);
                },
                error:function () {
                    console.log('失败')
                    if(callBack!=null)
                        callBack(null);
                }
            })
        }
    }
}


//判断是否是邮箱地址格式
function isMailFormat(mail) {
    var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    var bChk = reg.test(mail);
    return bChk;
}
function isPhoneNo(phone) {
    var pattern = /^1[34578]\d{9}$/;
    return pattern.test(phone);
}
function isDefined(obj) {
    // console.log('isDefined');
    // console.log(typeof (obj));
    //if (obj == null) return true;
    if (typeof (obj) == "undefined") {
        return false;
    } else {
        return true;
    }
}
function isNullOrUndefined(obj){
    if (obj==undefined ||obj==null){
        return true;
    } else{
        return false
    }
}