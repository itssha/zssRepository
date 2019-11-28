var entityName = 'group';
var  entityTable=null;

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
        console.log(row)
        $("#modal_group_id").val(row.id)
        $("#modal_group_name").val(row.name)
        $("#groupModel").modal();
    }
}
$("#save-groupEdit-btn").click(function () {

    $('#groupModel').modal('hide');
    var modal_group_id = $("#modal_group_id").val();
    var modal_group_name = $("#modal_group_name").val();

    var labelUrl="group/update";
    var data={id: modal_group_id,name:modal_group_name};
    //后台接不到group对象post0可以接收到
    getDao(entityName).save(data)
   /* getDao(entityName).ajax.post0(labelUrl,data,function (result){
        if(result=="success"){
            alert("保存成功")
        }else{
            alert("保存失败")
        }


    })*/


})


$("#groupAdd").click(function() {
    //模态框
    $("#modal_group_id").val("")
    $("#modal_group_name").val("");
    $("#groupModel").modal();
   /* entityTable.table.bootstrapTable('insertRow', {
        index: 0,
        row: {
            name: ''
        }
    });*/

});


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
            {field: "code",align:"center", title: "代码",width: "170"},
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
            },
            {field: "selected",align:"center", title: "自选",width: "170",
                events:changeSelect,
                formatter:selectFormatter


            },
            {field: "url",align:"center", title: "url",width: "170",
                formatter:function(value, row, index){
                    var v='<a href='+value+'>'+ "链接"+'</a>'
                    return [
                        v
                    ].join("")

                }
            }
        ];
        console.log(row.id)
        var page=1;
        var limit=15;
        var condition=new Condition();
        condition.setPage(page-1,limit);
      //  condition.addMap("id",row.id);
      //  var entity={'id':row.id};
        var   entity={"id":row.id};
      //  condition.setEntity(entity);
        entityTable=new EntityTable('#aShockTableByGroup','code','content',condition);

        entityTable.init(columns);

        entityTable.searchByEntity(entity)
        $("#groupByModel").modal();
    }
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