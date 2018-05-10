$(document).ready(function(){
    window.clusterId = getQueryString("clusterId");
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

function rebuildHumpbackNodeListTable(){
    smarty.get( "/node/getNodeList?pluginType=humpback&clusterId=" + window.clusterId , "plugin/humpback/humpback_mode_manager", "node-list", function(){
        $("table").dataTable({});
        console.log("build table");
    }, true );
}

$("[href='#node-list']").click( function () {
    rebuildHumpbackNodeListTable();
});

$("[href='#add-node']").click( function () {
    $('iframe[name="node-install-manager"]').attr("src","/node/install?pluginType=humpback&clusterId=" + window.clusterId);
});

