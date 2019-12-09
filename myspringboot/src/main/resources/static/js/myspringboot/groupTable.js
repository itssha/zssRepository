var entityName = 'group';
var  entityTable=null;
var  codeSearchEntityTable=null;
var  groupIdCurrent=null;
$(function(){

    groupList(getCondition(),'content');
    //如何額外增加bootstrapTable的option事件
    entityTable.table.on('click-cell.bs.table', function (e,field, value,  row, $element) {
        //alert(JSON.stringify(field));
       if(field=='name'){
           $element.attr('contenteditable', true);
           $element.blur(function() {
               var index = $element.parent().data('index');
               var tdValue = $element.html();
           //  saveData(index, field, tdValue);
           })
       }
    })


})
$("#modal_group_code_name").bind('input onchange', function(){
  /*  alert("modal_group_code_name.onchange")*/
    var codeNameOrNameCaption=$('#modal_group_code_name').val();
    /*console.log(codeNameOrNameCaption)*/
    var condition=new Condition();
    condition.setPage(0,15);
    //如果全是数字 ^\d{m,n}$  1-6位数字
    var patternNum=new RegExp("^\\d{1,6}$")
    if(patternNum.test(codeNameOrNameCaption)){
        condition.addMap('code','lk',codeNameOrNameCaption);
        console.log('该字符串是1-6位数字');
    }else {
        var pattern1 = new RegExp("[A-Za-z]+");
        if(pattern1.test(codeNameOrNameCaption)){
            condition.addMap('nameCapital','lk',codeNameOrNameCaption)
            console.log('该字符串是英文');
        }else{
            condition.addMap('name','lk',codeNameOrNameCaption)
            console.log('该字符串是中文');
        }
    }

   addCodeToGroup(condition,'content')
})


function saveData(index, field, value) {
    /*entityTable.table.bootstrapTable('updateCell', {
        index: index,       //行索引
        field: field,       //列名
        value: value        //cell值
    })*/
}

//通过bootstrap table自带的 onClickCell 方法，
// 点击 td 添加 contenteditable 属性(ps: 使元素可编辑),
// 于是 td 元素具有了类似于文本框的 focus 和 blur 事件，
// 用户点击 td 获取焦点，编辑完内容失去焦点后，调用 updateCell方法更新单元格数据。

var groupEdit={
    'click #groupEditBut':function (e,value,row,index) {
        console.log("groupEditBut")
        $("#modal_group_id").val(row.id)
        $("#modal_group_name").val(row.name)
        $("#modal_group_code_name").val("");
        if(codeSearchEntityTable!=null)codeSearchEntityTable.table.bootstrapTable("destroy")

        groupIdCurrent=row.id;
        $("#groupModel").modal();
    }
}
var saveGroup= function() {
    $('#groupModel').modal('hide');
    var modal_group_id = $("#modal_group_id").val();
    var modal_group_name = $("#modal_group_name").val();
    var data={id: modal_group_id,name:modal_group_name};
    //后台接不到group对象post0可以接收到
    getDao(entityName).save(data/*,function (result) {
        console.log(result)

    }*/)
    location.reload()


}
$("#save-groupEdit-btn").click(function () {
    saveGroup();
})


$("#groupAdd").click(function() {
    //模态框
    $("#modal_group_id").val("")
    $("#modal_group_name").val("");
    $("#codeNameDiv").hide();
    $("#groupModel").modal();
   /* entityTable.table.bootstrapTable('insertRow', {
        index: 0,
        row: {
            name: ''
        }
    });*/

});

var addCodeToGroupAjax={
    'click #addCodeToGroupBut':function (e,value,row,index) {

      /*  console.log(groupIdCurrent);*/
        var addCodeToGroupUrl="group/addOrRemoveCode"
        var data={"groupId":groupIdCurrent,"codeId":row.id}
        console.log("row.groups")
        console.log(row.groups)
       getDao(entityName).ajax.post0(addCodeToGroupUrl,data,function (result) {
           if(result!=null){
               codeSearchEntityTable.load()
           }

       })

    }
}
var groupViewBut={
    'click #groupViewBut':function (e,value,row,index) {
        var columns=[
            {
                checkbox: true,
                width : 35
            },
            {field: "edit",align:"center", title: "编辑",
                formatter:function (value, row, index) {
                    return [
                        '<button id="editCode" type="button" class="btn btn-default">编辑</button>'
                    ].join("")
                }
            },
            {field: "id",align:"center", title: "id"},
            {field: "code",align:"center", title: "代码",width: "170",
                formatter:function(value, row, index) {
                    var v = '<a href=' + row.url + '>' +value+ '</a>'
                    return [
                        v
                    ].join("")
                }
            },
            {field: "name",align:"center", title: "名称",width: "170"},
            {field: "nameCapital",align:"center", title: "名称",width: "170"},

            {field: "level",align:"center", title: "等级",width: "250",
                events:setLevel,
                formatter:levelFormatter},
            {field: "label",align:"center", title: "标签",width: "170",
                events:labelEdit,

                formatter:function(value, row, index){
                    var v='<button id="labelEditBut" type="button" class="btn btn-default">查看添加</button>'


                    return [
                        v
                    ].join("")

                }
            },
            {field: "remarks",align:"center", title: "备注",width: "170",
                events:setRemark,
                formatter:function(value, row, index){
                    var v='<button id="remarksEditBut" type="button" class="btn btn-default">查看</button>'
                    return [
                        v
                    ].join("")

                }
            }/*,
            {field: "selected",align:"center", title: "自选",width: "170",
                events:changeSelect,
                formatter:selectFormatter


            },*/
           /* {field: "url",align:"center", title: "url",width: "170",
                formatter:function(value, row, index){
                    var v='<a href='+value+'>'+ "链接"+'</a>'
                    return [
                        v
                    ].join("")

                }
            }*/
        ];
        console.log(row.id)
        var page=1;
        var limit=15;
        var condition=new Condition();
        condition.setPage(page-1,limit);
        condition.setManyToManyAttributeName("groups");
        condition.setManyToManyJoinById(row.id)
        condition.setManyToManyEntity({"id":row.id,"codes":[]})
      //  condition.addMap("id",row.id);
      //  var entity={'id':row.id};
      //  var   entity={"groups":[{"id":2}]};
      //  condition.setEntity(entity);
        entityTable=new EntityTable('#aShockTableByGroup','code','content',condition);

        entityTable.init(columns);
        entityTable.load();

        $("#groupByModel").modal();
    }
}
var groupViewButTest={
    'click #groupViewBut':function (e,value,row,index) {
        //直接加载AstockTable.html
        $("#groupByModel").modal();

    }
}

function addCodeToGroup(condition,dataField){
 //   modal_group_code_name
    var columns=[
        {field: "id",align:"center", title: "id"},
        {field: "code",align:"center", title: "代码",width: "170"},
        {field: "name",align:"center", title: "名称",width: "170"},
        {field: "edit",align:"center", title: "添加或移除",events:addCodeToGroupAjax,
            formatter:function (value, row, index) {
                console.log("添加或移除")
            console.log(row.groups)
                var title='添加';
              /*  for(var group in row.groups){
                    console.log("field")
                    console.log(group);
                    console.log(groupIdCurrent);
                    if(group.id==groupIdCurrent) title='移除';
                }*/
                for(var i=0;i<row.groups.length;i++)
                {
                    row.groups[i];
                    console.log(groupIdCurrent);
                    if(row.groups[i].id==groupIdCurrent)title='移除';
                }
                return [
                    '<button id="addCodeToGroupBut" type="button" class="btn btn-default">'+title+'</button>'
                ].join("")
            }
        }
    ]

    codeSearchEntityTable=new EntityTable('#addAShockTableToGroup','code',dataField,condition);

    codeSearchEntityTable.init(columns);

    codeSearchEntityTable.load();
}
function groupList(condition,dataField){

    var columns=[
        {
            checkbox: true,
            width : 35
        },
        {field: "edit",align:"center", title: "编辑",
            events:groupEdit,
            formatter:function (value, row, index) {
                return [
                    '<button id="groupEditBut" type="button" class="btn btn-default">编辑</button>'
                ].join("")
            }
        },
        {field: "id",align:"center", title: "id"},
        {field: "name",align:"center", title: "分组",width: "170"},
        {field: "view",align:"center", title: "查看",width: "170",
            events:groupViewBut,
            formatter:function (value, row, index) {
                return [
                    '<button id="groupViewBut" type="button" class="btn btn-default">查看</button>'
                ].join("")
            }}

    ];

    entityTable=new EntityTable('#groupTable',entityName,dataField,condition);

    entityTable.init(columns);

    entityTable.load();
};
function  getCondition() {

    var page=1;
    var limit=15;
    var condition=new Condition();
    condition.setPage(page-1,limit);

    return condition;
}