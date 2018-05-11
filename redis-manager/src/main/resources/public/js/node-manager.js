$(document).ready(function(){
    window.pluginType = getQueryString("pluginType");
    window.clusterId = getQueryString("clusterId");
    getCluster(clusterId, function(obj){
        var cluster = obj.res;
        console.log(cluster)
        nodeList(cluster.address, function(obj){
            window.nodeList = obj.res;
            console.log(nodeList)
            rebuildNodeListTable( clusterId );
        });
    });
});

function rebuildNodeListTable(){
    smarty.get( "/node/getNodeList?pluginType="+ window.pluginType  +"&clusterId=" + window.clusterId , "plugin/" + window.pluginType + "/" + window.pluginType + "_mode_manager", "node-list", function(){
        $("table").dataTable({});
        console.log("build table");
    }, true );
}

$("[href='#node-list']").click( function () {
    rebuildNodeListTable();
});

$("[href='#add-node']").click( function () {
    $('iframe[name="node-install-manager"]').attr("src","/node/install?pluginType="+ window.pluginType +"&clusterId=" + window.clusterId);
});

$(document).on("click", ".start-node", function(){
    var reqParam = getReqParam( this );
    nodeStart(reqParam, function(obj){
        console.log(obj);
    });
});

$(document).on("click", ".stop-node", function(){
    var reqParam = getReqParam( this );
    nodeStop(reqParam, function(obj){
        console.log(obj);
    });
});

$(document).on("click", ".restart-node", function(){
    var reqParam = getReqParam( this );
    nodeRestart(reqParam, function(obj){
        console.log(obj);
    });
});

$(document).on("click", ".delete-node", function(){
    var reqParam = getReqParam( this );
    nodeRemove(reqParam, function(obj){
        console.log(obj);
    });
});

function getReqParam(obj){
    var nodeRequestPram = {"pluginType": window.pluginType};
    var detail = getNodeDetail(obj);
    nodeRequestPram.req = detail;
    return nodeRequestPram;
}

function getNodeDetail(obj){
    var id = $( obj ).data("id");
    var detail = $("#node-" + id).data("detail");
    return detail;
}