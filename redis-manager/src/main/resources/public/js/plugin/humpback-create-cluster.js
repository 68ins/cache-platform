$(document).ready(function(){
    window.clusterId = getQueryString("clusterId");
    init_install_ui(window.clusterId);
});

function init_install_ui(clusterId){
    getImageList("humpback", function(obj){
        console.log( obj );
        var userGroup = window.user.userGroup || "";
        var groupList = [];
        if( userGroup != "" ){
            groupList = userGroup.split(",");
        }
        obj.groups = groupList;
        createClusterStep( obj, clusterId );
    });
}

$(document).on("click", "#start-install-cluster", function(obj){
    var installParam = sparrow_form.encode( "create-cluster-form", 1 );
    if ( !sparrow.empty( installParam )  ){
        installParam.pluginType = "humpback";
        if( clusterId ){
            installParam.clusterId = clusterId;
        }
        var param = {
            "pluginType":"humpback",
            "req": installParam
        }
        console.log( param );
        nodePullImage( param, function(obj){
            if( obj.code ==  0 ){
                nodeInstall( param, function(obj){
                    console.log( obj.res );
                });
            }
        });
    }
});

function  createClusterStep( data, clusterId){
    smarty.html( "plugin/humpback/create_cluster_step", data, "create-cluster-container",function () {
        if( clusterId ){
            // 如果有传入 clusterId 需要重绘界面
            getCluster(clusterId, function (obj) {
                var cluster = obj.res;
                $("[name='clusterName']").val( cluster.clusterName );
                $("[name='clusterName']").attr("disabled","disabled");
                console.log( cluster );
            });
        }
        autosize(document.querySelectorAll('textarea'));
        var data = {};
        data.id = window.user.id;
        connect( JSON.stringify(data), "/webSocket/createClusterLog");
    });
}
