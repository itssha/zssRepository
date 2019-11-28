function EntityTable(tableId,entityName,dataField,condition,customerSearch) {
    console.log("EntityTable.ctor:"+tableId+","+entityName);
    this.entityName=entityName;
    this.tableId=tableId;
    this.table=$(tableId);
    this.page=1;
    this.limit=15;
  //  this.condition=new Condition();
    this.dao=getDao(entityName);
    this.customerSearch=customerSearch;
    this.condition=condition;
    if(typeof this.init != 'function') {
        EntityTable.prototype.init = function (columns) {
            var obj = this;
            var option = {
                idField: 'id',
                sidePagination: 'server',
                pagination: true,
                dataField: dataField,
                totalField: 'totalElements',
               showColumns: true,
               sortStable: true,
                pageSize:15,
                // checkboxEnabled:true,
                // singleSelect:true,
                // clickToSelect:true,
                onClickRow:function(row,$e){
                    index = $e.data('index');
                },
                onPageChange: function (number, limit) {
                    console.log('onPageChange:' + number + ',' + limit);
                    if(obj.customerSearch!=null){
                        obj.customerSearch(number,limit);
                    }
                    else {
                        console.log("onpagechange");
                        console.log(obj.condition);
                        obj.loadData(number, limit);
                    }
                },
                customSort: function (sortName, sortOrder) {
                    console.log('customSort:' + sortName + ',' + sortOrder);
                },
                onAll: function (name, args) {
                    //console.log('onAll:'+name);
                    //console.log(args);
                },
                onSort: function (name, order) {
                    console.log('onSort:' + name + ',' + order);
                    obj.loadData(obj.page, obj.limit, name, order,obj.map,obj.entity);
                },
                columns: columns
            };
            obj.table.bootstrapTable(option)
            //obj.table.bootstrapTable('refreshOptions', obj.option);//表格发生变化
            obj.option = option;
        }
    }
    if(typeof this.load != 'function'){
        EntityTable.prototype.load=function () {
            var obj=this;
            obj.loadData(obj.page,obj.limit,'id','asc'/*'desc'*/);

        }
    }
    if(typeof this.loadData != 'function'){
        EntityTable.prototype.loadData=function (page, limit,sortName,sortOrder,map,entity) {
            var obj=this;
            obj.table.bootstrapTable('showLoading');
            obj.page=page;
            obj.limit=limit;
            obj.map=map;
            obj.entity=entity;
            if(typeof(sortName) == 'undefined'){
                sortName=obj.sortName;
            }else{
                obj.sortName=sortName;
            }
            if(typeof(sortOrder) == 'undefined'){
                sortOrder=obj.sortOrder;
            }else{
                obj.sortOrder=sortOrder;
            }
          //  var condition=new Condition();
           obj.condition.setPage(page-1,limit);
            obj.condition.addSort(sortName,sortOrder);
            if(entity!=null){
                console.log("set condition entity !!!!!!!!!!!!");
                console.log(entity);
                obj.condition.entity=entity;
            }

            if(map!=null){
                obj.condition.map=map;
            }
            console.log("obj.condition");
            console.log(obj.condition);
            obj.dao.queryPage(obj.condition,function (result) {
                var data=getResultData(result);
                if(data!=null){
                    obj.table.bootstrapTable('load',data);
                }
                obj.table.bootstrapTable('hideLoading');
            });
        }
    }
    if(typeof this.showResult != 'function'){
        EntityTable.prototype.showResult=function(result) {
            var obj=this;
            var data=getResultData(result);
            if(data!=null){
                obj.table.bootstrapTable('load',data);
            }
            obj.table.bootstrapTable('hideLoading');
        }
    }
    if(typeof this.search != 'function'){
        EntityTable.prototype.search=function(map, entity) {
            var obj=this;
            obj.loadData(obj.page, obj.limit, 'id', 'asc', map,entity);
        }
    }
    if(typeof this.searchByMap != 'function'){
        EntityTable.prototype.searchByMap=function(map) {
            this.search(map,null);
        }
    }
    if(typeof this.searchByEntity != 'function'){
        EntityTable.prototype.searchByEntity=function(entity) {
            this.search(null,entity);
        }
    }
    if(typeof this.setOption!='function'){
        EntityTable.prototype.setOption=function (key, value) {
            console.log(' --> EntityTable.setOption:'+key+','+value);
            var obj=this;
            obj.option[key]=value;
            obj.table.bootstrapTable('refreshOptions',obj.option);//刷新一下
        }
    }
    if(typeof this.changeEntity!='function'){
        EntityTable.prototype.changeEntity=function (entityName, columns) {
            console.log(' --> EntityTable.changeEntity:'+entityName);
            var obj=this;
            obj.entityName=entityName;
            //obj.dao=new EntityDao(entityName);
            obj.dao=getDao(entityName);
            obj.setOption('columns',columns);//刷新一下
        }
    }
    if(typeof this.getSelections!='function'){
        EntityTable.prototype.getSelections=function () {
            console.log(' --> EntityTable.getSelections');
            var obj=this;
            var list= obj.table.bootstrapTable('getSelections');//刷新一下
            console.log(list);
            return list;
        }
    }
    if(typeof this.deleteSelections!='function'){
        EntityTable.prototype.deleteSelections=function () {
            console.log(' --> EntityTable.deleteSelections');
            var list=entityTable.getSelections();
            console.log(list);
            if(list.length==0){
                showAlertModal('请先选择一行','info');
            }
            else{
                console.log(list);
                var entity=list[0];
                var msg='是否确定删除？';
                showConfirmModal("确认",msg,function(){
                    var id=entity.id;
                    var dao=entityTable.dao;
                    dao.delete(id,function (result) {
                        if(result.state==0){
                            entityTable.load();
                            showAlertModal('删除成功!', 'success');
                        }
                        else{
                            showAlertModal('删除失败!', 'success');
                        }
                    })
                })
            }
        }
    }
}