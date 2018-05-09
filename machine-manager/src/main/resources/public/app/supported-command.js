$(function(){

    /*加载页面时，获取 supported command list*/
    getSupportedCommandList();

	$(".one-cmd").on("keyup", function(event){
		if (event.keyCode == 13){
		    addCommandToList();
		}
	})
	
	$(".add-btn").on("click", function(){
		addCommandToList();
	})
	
	$("body").delegate(".support-cmd-list ul li","dblclick", function(){
	    var object = $(this);
	    var supportedCommandId = object.attr("data");
		$.ajax({
            type:"POST",
            url:"/rest/supportedCommand/deleteSupportedCommandById",
            dataType: "json",
            contentType: "application/json",
            data:supportedCommandId,
            success: function(result){
                var code = result.code;
                if(code == 1){
                   object.remove();
                }
            },
            error: function(e){
                layer.msg("error");
            }
        })
	})
})

function getSupportedCommandList(){
    $.ajax({
        type:"POST",
        url:"/rest/supportedCommand/getSupportedCommandList",
        dataType: "json",
        success: function(result){
            var code = result.code;
            if(code == 1){
                var supportedCommandList = result.supportedCommandList;
                buildSupportedCommandList(supportedCommandList);
            }
        },
         error: function(e){
            layer.msg("error");
         }
    });
}

function addCommandToList(){
    var command = $(".one-cmd").val().trim();
    if(command != null && command != ""){
        $.ajax({
            type:"POST",
            url:"/rest/supportedCommand/addSupportedCommand",
            dataType: "json",
            contentType: "application/json",
            data:command,
            success: function(result){
                var code = result.code;
                if(code == 1){
                    var supportedCommand = result.supportedCommand;
                    var supportedCommandId = supportedCommand.supportedCommandId;
                    var command = supportedCommand.command;
                    var list = $(".support-cmd-list ul");
                    list.append('<li data="'+supportedCommandId+'">'+command+'</li>')
                    $(".one-cmd").val("");
                } else if(code == 2){
                    layer.msg("Already exists")
                }
            },
            error: function(e){
                layer.msg("error");
            }
        })
    }
}


function buildSupportedCommandList(resultList){
    if(resultList != null && resultList.length > 0){
        var list = $(".support-cmd-list ul");
        var supportedCommand;
        var supportedCommandId;
        var command;
        var data = "";
        for(var index = 0; index < resultList.length; index++){
            supportedCommand = resultList[index];
            supportedCommandId = supportedCommand.supportedCommandId;
            command = supportedCommand.command;
            data += '<li data="'+supportedCommandId+'">'+command+'</li>';
        }
        list.html(data);
    }

}