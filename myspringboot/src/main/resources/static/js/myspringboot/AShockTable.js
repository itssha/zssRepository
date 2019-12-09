
var entityName = 'code';
var  entityTable=null;
var index= '';

$(function(){

    codeList(getCondition(),'content');


})
var setLevel= {
    'change #levelSelect': function (e, value, row, index) {
        var newLevel=$('#levelSelect').val()
        var levelUrl="code/level"
        var data={id: row.id,level:newLevel};
        getDao(entityName).ajax.post0(levelUrl,data,function (result){
            if(result==1){
                //修改row值
                row.level=newLevel
                entityTable.table.bootstrapTable('updateRow',{
                    index:index,
                    row:row
                });
            }
        })
    }
}
function selectFormatter(value, row, index) {
    var v="";
    if(value){
        v=   '<button id="select0" type="button" class="btn btn-default">取消自选</button>'

    }else{
        v=    '<button id="select0" type="button" class="btn btn-default">加入自选</button>'

    }

    return [
        v
    ].join("")




    }
//保存模态框
$("#save-RemarkEdit-btn").click(function () {
    $('#myModalRemark').modal('hide');
    var modal_remarks = $('#summernote').summernote('code')
    var remarksUrl="code/remarks";
    var data={id: Number($("#remark_modal_id").val()),remarks:modal_remarks};
    getDao(entityName).ajax.post0(remarksUrl,data,function (result){
        if(result==1){
            alert("保存成功")
            entityTable.table.bootstrapTable('updateRow',{
                index:index,
                row:{
                    remarks:modal_remarks
                }
            });
        }else{
            alert("保存失败")
        }



    })
});

$("#save-LabelEdit-btn").click(function () {
    $('#myModal').modal('hide');
    var modal_labels = $("#modal_labels").val();
    var labelUrl="code/labels";
    var data={id: Number($("#modal_id").val()),labels:modal_labels};
    getDao(entityName).ajax.post0(labelUrl,data,function (result){
       if(result==1){
           alert("保存成功")
           entityTable.table.bootstrapTable('updateRow',{
               index:index,
               row:{
                   label:modal_labels
               }
           });
       }else{
           alert("保存失败")
       }

        })
    $('#summernote').summernote('destroy');
    });

var setRemark={
    'click #remarksEditBut':function (e,value,row,index) {
        //console.log(row)
        $("#remark_modal_id").val(row.id)
        $("#remark_modal_code").val(row.code)
        $("#remark_modal_code_name").val(row.name)
        $("#myModalRemark").modal();
        $('#summernote').summernote({ height: 300});
        var markupStr = $('#summernote').summernote('code',row.remarks);
        //console.log(markupStr)
    }
}



var labelEdit={
    'click #labelEditBut':function (e,value,row,index) {
        console.log(row)
        $("#modal_id").val(row.id)
        $("#modal_code").val(row.code)
        $("#modal_code_name").val(row.name)
        $("#modal_labels").val(row.label)
        $("#myModal").modal();
    }
    }

var changeSelect={
    //修改
    "click #select0":function (e, value, row, index) {

        var changeSelected= !row.selected
        var selectUrl="code/select"
        var data={id: row.id,selected:changeSelected};
        getDao(entityName).ajax.post0(selectUrl,data,function (result){
            if(result==1){
                //修改row值
                row.selected=!row.selected
                entityTable.table.bootstrapTable('updateRow',{
                    index:index,
                    row:row
                });
            }
        })

    },

}

function levelFormatter(value, row, index) {
    var levelName={"1":"一级","2":"二级","3":"三级","4":"四级"}
    var headOption = "<option value ='5' selected>请选择</option>";
    var Option1= "<option value ='1'>"+levelName["1"]+"</option>";
    var Option2= "<option value ='2'>"+levelName["2"]+"</option>";
    var Option3= "<option value ='3'>"+levelName["3"]+"</option>";
    var Option4= "<option value ='4'>"+levelName["4"]+"</option>";

    var optionBody=[Option1,Option2,Option3,Option4]
    if(isNullOrUndefined(value) ){
        headOption = headOption +optionBody
    }else{
        optionBody.splice(value-1,1)
        headOption =  "<option  selected value='"+value+"' >"+levelName[value]+"</option>"+optionBody;
    }
// class="form-control"   select 加入bootstrapTable class 就不能显示selected
    var option = '<select  class="form-control" id =levelSelect style="height:33px">'+
        headOption + '</select>';
    return option;
}

function codeList(condition,dataField){

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
         },
        {field: "selected",align:"center", title: "自选",width: "170",
            events:changeSelect,
            formatter:selectFormatter


        }/*,
        {field: "url",align:"center", title: "url",width: "170",
            formatter:function(value, row, index){
               var v='<a href='+value+'>'+ "链接"+'</a>'
                return [
                    v
                ].join("")

            }
        }*/
    ];

    entityTable=new EntityTable('#AshockTable',entityName,dataField,condition);

    entityTable.init(columns);

    entityTable.load();
};
$("#codeSearch").click(function () {
    entityTable.table.bootstrapTable( 'destroy')
    codeList(getCondition());

});
$("#addStockBtn").click(function () {
    $("#addStockName").val("");
    $("#addStockCode").val("");
    $("#addStockUrl").val("");
    $("#addStockNameCaption").val("");
    $("#addStockModal").modal();

});

$("#addStockCode").bind('input onchange', function(){
    var urlHead="http://stockpage.10jqka.com.cn/";
    var code=$("#addStockCode").val();
    $("#addStockUrl").val(urlHead+code);
});
$("#save-addStock-btn").click(function () {

    var name=$("#addStockName").val();
    var code=$("#addStockCode").val();
    var nameCaption=$("#addStockNameCaption").val();
    var codeUrl=$("#addStockUrl").val();

    var data={code:code,name:name,url:codeUrl,nameCapital:nameCaption};
    getDao(entityName).save(data,function (result){
      /*  if(result==1){
            alert("保存成功")
            entityTable.table.bootstrapTable('updateRow',{
                index:index,
                row:{
                    remarks:modal_remarks
                }
            });
        }else{
            alert("保存失败")
        }*/
    })
})

function  getCondition() {
    var code=$('#code').val();
    var codeName=$('#name').val();
    var level=$('#levelSelectSearch').val()
    var label=$('#label').val()
    var page=1;
    var limit=15;
    var condition=new Condition();
    condition.setPage(page-1,limit);
    condition.addMap('code','lk',code);
    condition.addMap('level','eq',level)
    condition.addMap('label','lk',label)
    var pattern1 = new RegExp("[A-Za-z]+");
    if(pattern1.test(codeName)){
        condition.addMap('nameCapital','lk',codeName)
       // console.log('该字符串是英文');
    }else{
        condition.addMap('name','lk',codeName)
      //  console.log('该字符串是中文');
    }
    return condition;
}

