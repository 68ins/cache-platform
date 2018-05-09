$(document).ready(function(){
  rebuildMachineNodeListTable();
});

function rebuildMachineNodeListTable(){
    //var clusterId = window.clusterId;
    var clusterId = 2;
    smarty.get( "/node/getNodeList?pluginType=machine&clusterId=" + clusterId , "plugin/machine/machine_mode_manager", "node-content", function(){
        $("table").dataTable({});
    }, true );

}
