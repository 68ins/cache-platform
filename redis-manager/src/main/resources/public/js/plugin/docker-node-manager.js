$(document).ready(function(){
    var clusterId = getQueryString("clusterId");
    getCluster(clusterId, function(obj){
        var cluster = obj.res;
        nodeList(cluster.address, function(obj){
            window.nodeList = obj.res;
            rebuildDockerNodeListTable( clusterId );
        });
    });
});

function rebuildDockerNodeListTable(){
    //var clusterId = window.clusterId;
    var clusterId = 3;
    smarty.get( "/node/getNodeList?pluginType=docker&clusterId=" + clusterId , "plugin/docker/docker_mode_manager", "node-content", function(){
        $("table").dataTable({});
    }, true );

}
