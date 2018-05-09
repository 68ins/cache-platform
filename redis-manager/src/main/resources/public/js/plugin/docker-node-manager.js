$(document).ready(function(){
  rebuildDockerNodeListTable();
});

function rebuildDockerNodeListTable(){
    //var clusterId = window.clusterId;
    var clusterId = 3;
    smarty.get( "/node/getNodeList?pluginType=docker&clusterId=" + clusterId , "plugin/docker/docker_mode_manager", "node-content", function(){
        $("table").dataTable({});
    }, true );

}
