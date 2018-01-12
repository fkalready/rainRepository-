
$(function () {
    $('#grid').datagrid({
        url:'emp_listByPage.action',
        columns:[[
            {field:'uuid',title:'编号',width:100},
            {field:'username',title:'登陆名',width:100},
            {field:'name',title:'真实姓名',width:100},
            {field:'gender',title:'性别',width:100,formatter:function(value){
                    //转成数值类型
                    if(value * 1 == 1){
                        return '男';
                    }
                    if(value * 1 == 0){
                        return '女';
                    }
                }},
            {field:'email',title:'邮件地址',width:100},
            {field:'tele',title:'联系电话',width:100},
            {field:'address',title:'联系地址',width:100},
            {field:'birthday',title:'出生年月日',width:100,formatter:function(value){
                    if(value){//value有值
                        return new Date(value).Format('yyyy-MM-dd');
                    }
                }},
            {field:'dep',title:'部门',width:100,formatter:function(value){
                    return value.name;
                }},

            {field:'-',title:'操作',width:200,formatter: function(value,row,index){
                    var oper = "<a href=\"javascript:void(0)\" onclick=\"resetPwd(" + row.uuid + ')">重置密码</a>';
                    return oper;
                }}
        ]]
    });

    $('#dd').dialog({
        title: '重置密码',
        width: 240,
        height: 150,
        closed: true,
        modal: true,
        buttons:[{
            text:'保存',
            iconCls: 'icon-save',
            handler:function(){
                submitAjax();
            }
        }]
    });
})

function resetPwd(uuid) {
    $('#resetForm').form('clear')
    $('#dd').window('open');
    $('#uuid').val(uuid);
}

function submitAjax() {

    var $newpass = $('#newPwd');
    var $rePass = $('#rePwd');

    if ($newpass.val() == '') {
        msgShow('系统提示', '请输入密码！', 'warning');
        return false;
    }
    if ($rePass.val() == '') {
        msgShow('系统提示', '请在一次输入密码！', 'warning');
        return false;
    }

    if ($newpass.val() != $rePass.val()) {
        msgShow('系统提示', '两次密码不一至！请重新输入', 'warning');
        return false;
    }

    var data = $('#resetForm').serializeJSON();
    $.post('emp_resetPwd.action',data,function (result) {
        if (result.success) {
            msgShow('提示',result.message,'info');
            $('#dd').window('close');
        }else{
            msgShow('提示',result.message,'info');
        }
    },'json')
}

function msgShow(title, msgString, msgType) {
    $.messager.alert(title, msgString, msgType);
}