$(document).ready(function(){
    init_install_ui();
});

function init_install_ui(){
    getImageList("humpback", function(obj){
        console.log( obj );
        var userGroup = window.user.userGroup || "";
        var groupList = [];
        if( userGroup != "" ){
            groupList = userGroup.split(",");
        }
        obj.groups = groupList;
        createClusterStep( obj );
    });
}

$(document).on("click", "#start-install-cluster", function(obj){
    var installParam = sparrow_form.encode( "create-cluster-form", 1 );
    if ( !sparrow.empty( installParam )  ){
        installParam.pluginType = "humpback";
        var param = {
            "pluginType":"humpback",
            "req": installParam
        }
        nodePullImage( param, function(obj){
            if( obj.code ==  0 ){
                nodeInstall( param, function(obj){
                    console.log( obj.res );
                });
            }
        });
    }
});

function  createClusterStep( data ){
    smarty.html( "plugin/humpback/create_cluster_step", data, "create-cluster-container",function () {
        autosize(document.querySelectorAll('textarea'));
        var data = {};
        data.id = window.user.id;
        connect( JSON.stringify(data), "/webSocket/createClusterLog");
    });
}
