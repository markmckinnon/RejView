package com.williballenthin.RejistryView;

public class RejTreeNodeSelectionEvent {
    private final RejTreeNode _node;
    public RejTreeNodeSelectionEvent(RejTreeNode n) {
        this._node = n;
    }

    public RejTreeNode getNode() {
        return this._node;
    }
}
