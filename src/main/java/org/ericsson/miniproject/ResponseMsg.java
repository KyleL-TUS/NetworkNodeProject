package org.ericsson.miniproject;

public enum ResponseMsg {
    NODE_ADDED("Network node successfully added."),
    NODE_ADDED_FAILURE("Network node addition failure."),
    NODE_DELETED("Network node successfully deleted."),
    NODE_DELETED_FAILURE("Network node deletion failed."),
    NODE_UPDATED("Network node successfully updated."),
    NODE_FOUND("Network node found."),
    NODE_INVALID("Network node invalid."),
    NODE_NOT_FOUND("Network node not found.");

    final String msg;
    ResponseMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
