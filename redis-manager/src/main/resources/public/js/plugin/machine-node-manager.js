$(document).ready(function(){
    var clusterId = getQueryString("clusterId");
    getCluster(clusterId, function(obj){
        var cluster = obj.res;
        nodeList(cluster.address, function(obj){
            window.nodeList = obj.res;
            rebuildMachineNodeListTable( clusterId );
        });
    });
});

function rebuildMachineNodeListTable(){
    //var clusterId = window.clusterId;
    var clusterId = 2;
    smarty.get( "/node/getNodeList?pluginType=machine&clusterId=" + clusterId , "plugin/machine/machine_mode_manager", "node-content", function(){
        $("table").dataTable({});
    }, true );

}
