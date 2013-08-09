package com.williballenthin.RejistryView;

import java.util.List;

/**
 * RejTreeNode is the adaptor between the Registry structure model and the JTree model.
 *   It may describe both the contents of the node, and how it should be displayed.
 */
public interface RejTreeNode {
    public abstract String toString();
    public abstract boolean hasChildren();
    public abstract List<RejTreeNode> getChildren();
    public abstract RejTreeNodeView getView();
}
