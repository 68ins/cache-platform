package com.newegg.ec.cache.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * supported command
 * @author Jay.H.Zou
 * @date 2018/4/13
 */
public class SupportedCommand implements Serializable {
    private String supportedCommandId;
    private String command;
    private Timestamp updateTime;

    public String getSupportedCommandId() {
        return supportedCommandId;
    }

    public void setSupportedCommandId(String supportedCommandId) {
        this.supportedCommandId = supportedCommandId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SupportedCommand{" +
                "supportedCommandId='" + supportedCommandId + '\'' +
                ", command='" + command + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
