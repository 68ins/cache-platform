package com.newegg.ec.cache.plugin.machine;

import com.newegg.ec.cache.core.mysql.MysqlField;
import com.newegg.ec.cache.core.mysql.MysqlTable;
import com.newegg.ec.cache.plugin.basemodel.Node;

/**
 * Created by gl49 on 2018/4/22.
 */
@MysqlTable( name = "machine_node", autoCreate = true)
public class MachineNode extends Node{
    @MysqlField(isPrimaryKey = true, field = "id", type = "int")
    private int id;
    @MysqlField(field = "image", type = "varchar(250)", notNull = true)
    private String image;
    @MysqlField(field = "cluster_id", type = "int", notNull = true)
    private int clusterId;
    @MysqlField(field = "user_group", type = "varchar(25)", notNull = true)
    private String userGroup;
    @MysqlField(field = "username", type = "varchar(25)", notNull = true)
    private String username;
    @MysqlField(field = "password", type = "varchar(30)", notNull = true)
    private String password;
    @MysqlField(field = "ip", type = "varchar(25)", notNull = true)
    private String ip;
    @MysqlField(field = "port", type = "smallint", notNull = true)
    private int port;
    @MysqlField(field = "start_command", type = "varchar(200)", notNull = true)
    private String startCommand;
    @MysqlField(field = "stop_command", type = "varchar(200)", notNull = true)
    private String stopCommand;
    @MysqlField(field = "install_path", type = "varchar(200)", notNull = true)
    private String installPath;
    @MysqlField(field = "add_time", type = "int",  notNull = true)
    private int addTime;



}
