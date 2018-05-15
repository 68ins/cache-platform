window.HOST_URL = "http://" + window.location.host + "/";
window.STATIC_URL = window.HOST_URL;
window.JSTPL_URL = window.STATIC_URL + "jstpl/";

$(document).ready(function(){

});

$("#html-tpl").click(function(){
    var req = {"code":"001", "msg":"msg001","res":"","id":12};
    smarty.html( "demo/parent", req, "node-list-div",function () {
    });
});


$("#get-tpl").click(function(){
    smarty.get( "/demo/getList", "demo/parent", "node-list-div", function(){
    }, true );
});

$("#post-tpl").click(function(){
    var req = {"code":"001", "msg":"msg001","res":""};
    smarty.post( "/demo/postList", req, "demo/parent", "node-list-div", function(res){
    }, true );
});

$("#popen-tpl").click(function(){
    smarty.open( "demo/popen", {"id":12}, { title: "title", width:330, height:270},function(){
    });
});

smarty.register_function( 'test_fun', function( params ){
    var id = params['id'];
    return id + 100;
});

/*smarty.getSlaveNumber('cluster_slave', function(params){
    var nodes = parseInt(params['nodes']);
    var master = parseInt(params["master"]);
    var slave = nodesNum - master;
    return slave;
})*/
