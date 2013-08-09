package com.williballenthin.RejistryView;

import com.williballenthin.rejistry.RegistryKey;
import com.williballenthin.rejistry.RegistryParseException;
import com.williballenthin.rejistry.RegistryValue;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RejTreeKeyNode implements RejTreeNode {

    private final RegistryKey _key;

    public RejTreeKeyNode(RegistryKey key) {
        this._key = key;
    }

    @Override
    public String toString() {
        try {
            return this._key.getName();
        } catch (UnsupportedEncodingException e) {
            System.err.println("Failed to parse key name");
            return "PARSE FAILED.";
        }
    }

    @Override
    public boolean hasChildren() {
        try {
            return this._key.getValueList().size() > 0 || this._key.getSubkeyList().size() > 0;
        } catch (RegistryParseException e) {
            System.err.println("Failed to parse key children.");
            return false;
        }
    }

    @Override
    public List<RejTreeNode> getChildren() {
        LinkedList<RejTreeNode> children = new LinkedList<RejTreeNode>();

        try {
            Iterator<RegistryKey> keyit = this._key.getSubkeyList().iterator();
            while (keyit.hasNext()) {
                children.add(new RejTreeKeyNode(keyit.next()));
            }

            Iterator<RegistryValue> valueit = this._key.getValueList().iterator();
            while (valueit.hasNext()) {
                children.add(new RejTreeValueNode(valueit.next()));
            }
        } catch (RegistryParseException e) {
            System.err.println("Failed to parse key children.");
        }
        return children;
    }

    /**
     * @scope: package-protected
     */
    RegistryKey getKey() {
        return this._key;
    }


    /**
     * TODO(wb): this isn't exactly MVC...
     */
    public RejTreeNodeView getView() {
        return new RejTreeKeyView(this);
    }
}
