//提交的方法名称
var method = "";
var height=230;
var listParam = "";
var saveParam = "";
$(function(){

    if (Request['type']==1) {
        listParam = "?t1.type=1";
        saveParam = "?t.type=1";
    }
    if (Request['type']==2) {
        listParam = "?t1.type=2";
        saveParam = "?t.type=2";
    }

    //加载表格数据
    $('#grid').datagrid({
        url:name + '_listByPage'+listParam,
        columns:columns,
        singleSelect: true,
        pagination: true,
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function(){
                //设置保存按钮提交的方法为add
                method = "add";
                //关闭编辑窗口
                $('#editDlg').dialog('open');
                $('#editForm').form('clear');
            }
        },"-",{
            iconCls: 'icon-excel',
            text:'导出',
            handler: function(){
                var subData = $('#searchForm').serializeJSON();
                $.download(name + "_export" + listParam, subData);
            }
        },"-",{
            iconCls: 'icon-save',
            text:'导入',
            handler: function(){
                $('#importDlg').dialog('open');
            }
        }
        ]
    });

    $('#importDlg').dialog({
        title: '选择文件',//窗口标题
        width: 300,//窗口宽度
        height: 100,//窗口高度
        closed: true,//窗口是是否为关闭状态, true：表示关闭
        modal: true,//模式窗口
        buttons:[{
            text:'上传',
            iconCls: 'icon-save',
            handler:function(){
                $.ajax({
                    url:name+"_upload",
                    type:'post',
                    data: new FormData($('#importForm')[0]),
                    processData: false,
                    contentType: false,
                    dataType:'json',
                    success:function (result) {
                        $.messager.alert("提示", result.message, 'info');
                        if (result.success){
                            $('#importDlg').dialog('close');
                            $('#grid').datagrid('reload');
                        }
                    }
                })
            }
        }]
    });

    //点击查询按钮
    $('#btnSearch').bind('click',function(){
        //把表单数据转换成json对象
        var formData = $('#searchForm').serializeJSON();
        $('#grid').datagrid('load',formData);
    });

    //初始化编辑窗口
    $('#editDlg').dialog({
        title: '编辑',//窗口标题
        width: 300,//窗口宽度
        height: height,//窗口高度
        closed: true,//窗口是是否为关闭状态, true：表示关闭
        modal: true,//模式窗口
        buttons:[{
            text:'保存',
            iconCls: 'icon-save',
            handler:function(){
                //表单字段验证，当所有字段都有效的时候返回true。该方法使用validatebox(验证框)插件。
                if(!$('#editForm').form('validate')){
                    return;
                }
                //用记输入的部门信息
                var submitData= $('#editForm').serializeJSON();
                $.ajax({
                    url: name + '_' + method + saveParam,
                    data: submitData,
                    dataType: 'json',
                    type: 'post',
                    success:function(rtn){
                        //{success:true, message: 操作失败}
                        $.messager.alert('提示',rtn.message, 'info',function(){
                            if(rtn.success){
                                //关闭弹出的窗口
                                $('#editDlg').dialog('close');
                                //刷新表格
                                $('#grid').datagrid('reload');
                            }
                        });
                    }
                });
            }
        },{
            text:'关闭',
            iconCls:'icon-cancel',
            handler:function(){
                //关闭弹出的窗口
                $('#editDlg').dialog('close');
            }
        }]
    });

});


/**
 * 删除
 */
function del(uuid){
    $.messager.confirm("确认","确认要删除吗？",function(yes){
        if(yes){
            $.ajax({
                url: name + '_delete?id=' + uuid,
                dataType: 'json',
                type: 'post',
                success:function(rtn){
                    $.messager.alert("提示",rtn.message,'info',function(){
                        //刷新表格数据
                        $('#grid').datagrid('reload');
                    });
                }
            });
        }
    });
}

/**
 * 修改
 */
function edit(uuid){
    //弹出窗口
    $('#editDlg').dialog('open');

    //清空表单内容
    $('#editForm').form('clear');

    //设置保存按钮提交的方法为update
    method = "update";

    //加载数据
    $('#editForm').form('load',name + '_get?id=' + uuid);

    //var data = {"t.address":"花果山水帘洞11-2","t.birthday":"1949-10-01","t.dep.name":"总裁办","t.dep.tele":"111111","t.dep.uuid":2,"t.email":"swk@itcast.cn","t.gender":1,"t.name":"孙悟空","t.tele":"12345678","t.username":"sunwukong","t.uuid":2};
    //$('#editForm').form('load',data);
}