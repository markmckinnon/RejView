package com.williballenthin.RejistryView;

import com.williballenthin.rejistry.RegistryValue;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class RejTreeValueNode implements RejTreeNode {

    private final RegistryValue _value;

    public RejTreeValueNode(RegistryValue value) {
        this._value = value;
    }

    @Override
    public String toString() {
        try {
            String valueName = this._value.getName();
            if (valueName == "") {
                return "(Default)";
            }
            return valueName;
        } catch (UnsupportedEncodingException e) {
            System.err.println("Failed to parse _value name");
            return "PARSE FAILED.";
        }
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public List<RejTreeNode> getChildren() {
        return new LinkedList<RejTreeNode>();
    }

    /**
     * @scope: package-protected
     */
    RegistryValue getValue() {
        return this._value;
    }

    /**
     * TODO(wb): this isn't exactly MVC...
     */
    public RejTreeNodeView getView() {
        return new RejTreeValueView(this);
    }
}
