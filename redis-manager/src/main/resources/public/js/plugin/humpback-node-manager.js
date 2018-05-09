$(document).ready(function(){
    var clusterId = getQueryString("clusterId");
    getCluster(clusterId, function(obj){
        var cluster = obj.res;
        console.log(cluster)
        nodeList(cluster.address, function(obj){
            window.nodeList = obj.res;
            console.log(nodeList)
            rebuildHumpbackNodeListTable( clusterId );
        });
    });
});

function rebuildHumpbackNodeListTable(clusterId){
    smarty.get( "/node/getNodeList?pluginType=humpback&clusterId=" + clusterId , "plugin/humpback/humpback_mode_manager", "node-content", function(){
        $("table").dataTable({});
        console.log("build table");
    }, true );
}

$("#add-node").click(function(){
    init_install_ui();
});
