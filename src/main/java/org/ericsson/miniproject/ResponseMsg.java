package org.ericsson.miniproject;

public enum ResponseMsg {
    NODE_CREATED("Network node successfully created."),
    NODE_DELETED("Network node successfully deleted."),
    NODE_UPDATED("Network node successfully updated."),
    NODE_CREATED_FAILURE("Network node creation failed."),
    NODE_NOT_FOUND("Network node not found."),
    NODE_DELETED_FAILURE("Network node deletion failed.");

    final String msg;
    ResponseMsg(String msg) {
        this.msg = msg;
    }
}
