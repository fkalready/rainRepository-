
$(function () {
    $('#grid').datagrid({
        url:'report_orderReport.action',
        columns:[[
            {field:'name',title:'商品类型',width:100},
            {field:'y',title:'销售额',width:100},
        ]],
        singleSelect: true,
        pagination: true,
        onLoadSuccess:function (data) {
            showChar(data.rows)
        }
    });

    //点击查询按钮
    $('#btnSearch').bind('click',function(){
        //把表单数据转换成json对象
        var formData = $('#searchForm').serializeJSON();
        if(formData.endDate != ''){
            formData.endDate += " 23:59:59";
        }
        $('#grid').datagrid('load',formData);
    });
})

/*function showChar(charData) {
    Highcharts.chart('chart', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: '销售统计'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '{point.name}: <b>{point.percentage:.1f}%</b>'
                },
                showInLegend: true
            }
        },
        series: [{
            name: 'Brands',
            colorByPoint: true,
            data: charData
        }]
    });
}*/

function showChar(charData) {
    $('#chart').highcharts({
        chart: {
            type: 'pie',
            options3d: {
                enabled: true,
                alpha: 45,
                beta: 0
            }
        },
        title: {
            text: '销售统计'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                depth: 35,
                dataLabels: {
                    enabled: true,
                    format: '{point.name}: <b>{point.percentage:.1f}%</b>'
                }
            }
        },
        series: [{
            type: 'pie',
            name: '销售额占比',
            data: charData
        }]
    });
};