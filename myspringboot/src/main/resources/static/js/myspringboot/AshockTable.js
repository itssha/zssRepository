
var entityName = 'code';
var  entityTable=null;

$(function(){

    codeList(getCondition());

})

function selectFormatter(value, row, index) {
    var v="";
    if(value){
        v=   '<button id="select0" type="button" class="btn btn-default">取消自选</button>'

    }else{
        v=    '<button id="select0" type="button" class="btn btn-default">加入自选</button>'

    }

    return [
        v
    ].join("")            }

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
    var headOption = "<option value ='' selected>请选择</option>";
    var Option1= "<option value ='1'>"+levelName["1"]+"</option>";
    var Option2= "<option value ='2'>"+levelName["2"]+"</option>";
    var Option3= "<option value ='3'>"+levelName["3"]+"</option>";
    var Option4= "<option value ='4'>"+levelName["4"]+"</option>";

    var optionBody=[Option1,Option2,Option3,Option4]
    if(isNullOrUndefined(value) ){
        headOption = headOption +optionBody
        console.log("isNullOrUndefined")
    }else{
        optionBody.splice(value-1,1)
        headOption =  "<option  selected value='"+value+"' >"+levelName[value]+"</option>"+optionBody;
    }
// class="form-control"   select 加入bootstrapTable class 就不能显示selected
    var option = '<select  class="form-control" style="height:33px;width =500px ">'+
        headOption + '</select>';
    return option;
}

function codeList(condition){

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
        {field: "label",align:"center", title: "标签",width: "170"},
        {field: "level",align:"center", title: "等级",width: "500",formatter:levelFormatter},
        {field: "remarks",align:"center", title: "备注",width: "170"},
        {field: "selected",align:"center", title: "自选",width: "170",
            events:changeSelect,
            formatter:selectFormatter

        },
        {field: "url",align:"center", title: "url",width: "170",
            formatter:function(value, row, index){
               var v='<a href='+value+'>'+ value+'</a>'
                return [
                    v
                ].join("")

            }
        }
    ];

    entityTable=new EntityTable('#AshockTable',entityName,condition);

    entityTable.init(columns);

    entityTable.load();
};
$("#codeSearch").click(function () {
    entityTable.table.bootstrapTable( 'destroy')
    codeList(getCondition());

});

function  getCondition() {
    var code=$('#code').val();
    var codeName=$('#name').val();
    var page=1;
    var limit=15;
    var condition=new Condition();
    condition.setPage(page-1,limit);
    condition.addMap('code','lk',code)
    var pattern1 = new RegExp("[A-Za-z]+");
    if(pattern1.test(codeName)){
        condition.addMap('nameCapital','lk',codeName)
        console.log('该字符串是英文');
    }else{
        condition.addMap('name','lk',codeName)
        console.log('该字符串是中文');
    }
    return condition;
}

