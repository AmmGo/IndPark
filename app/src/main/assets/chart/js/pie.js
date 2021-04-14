
 var ggb = 0 ;
 var gb = 0 ;
 var ddb = 0 ;
 var db = 0 ;
 var sum = 0 ;
 function loadChart(res) {
var obj = JSON.parse(res);
   ggb=obj.ggb;
   gb=obj.gb;
   ddb=obj.ddb;
   db=obj.db;
   sum=obj.sum;
   setChart('pie', makePieChartOption());
 }
 function makePieChartOption() {
   var option = {
   				title:{
   					text: sum,
   					left:"center",
   					top:"43%",
   						textStyle:{
   						color:"#000000",
   						fontSize:10,
   						align:"center"
   						}
   					},
   				color:['#F4B188','#EE8181','#6FBEF6','#68C57B'],
   				graphic:{
   					type:"text",
   					left:"center",
   					top:"50%",
   						style:{
   						text:"危险源报警",
   						textAlign:"center",
   						fill:"#333",
   						fontSize:11
   						}
                    },
   			 	series: [
   					{
   					name: '危险源报警',
   					type: 'pie',
   					 roseType: 'angle',
   					radius: ['25%', '50%'],       //饼图大小
   					labelLine: {    //图形外文字线
   						normal: {
   							length: 35,
   							length2:80
   						}
   					},
   					label: {
   						normal: {
   							formatter:function(params){
                            str = '{a|'+ params.data.flag + '}\n'+'{b|'+params.name+'}';
                            return str
                            },
                            //图形外文字上下显示
   							borderWidth: 20,
   							borderRadius: 4,
   							padding: [0, -70],          //文字和图的边距
   							rich: {
   								a: {
   									color: '#000000',
   									fontSize: 10,
   									lineHeight: 30
   								},
   								b: {                        //name 文字样式
   									fontSize: 11,
   									lineHeight: 30,
   									color: '#000000',
   								},
   								c: {                   //value 文字样式
   									fontSize: 11,
   									lineHeight: 30,
   									color: '#000000',
   									align:"center"
   								}
   							}
   						}
   					},
   					data: [
   						{value: 35, name: '高高报',flag:ggb},
   						{value: 25, name: '高报',flag:gb},
   						{value: 20, name: '低低报',flag:ddb},
   						{value: 25, name: '低报',flag:db}
   					]
               }
           ]
   		};
     return option;
 }


 function setChart(id, option) {
     // 基于准备好的dom，初始化echarts实例
     var myChart = echarts.init(document.getElementById(id));
     // 使用刚指定的配置项和数据显示图表。
     myChart.setOption(option);
 }

