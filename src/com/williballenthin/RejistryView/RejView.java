package com.williballenthin.RejistryView;

import com.williballenthin.rejistry.RegistryHive;

import javax.swing.*;
import java.awt.*;

public class RejView extends JPanel implements RejTreeNodeSelectionListener {

    private final RegistryHive _hive;
    private final RejTreeView _tree_view;
    private final JSplitPane _splitPane;

    public RejView(RegistryHive hive) {
        super(new BorderLayout());
        this._hive = hive;
        this._tree_view = new RejTreeView(this._hive);

        this._splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                this._tree_view, new JPanel());
        this._splitPane.setResizeWeight(0.5);
        this._splitPane.setOneTouchExpandable(true);
        this._splitPane.setContinuousLayout(true);

        this.add(this._splitPane, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(800, 600));

        this._tree_view.addRejTreeNodeSelectionListener(this);
    }

    @Override
    public void nodeSelected(RejTreeNodeSelectionEvent e) {
        RejTreeNodeView v = e.getNode().getView();
        int curDividerLocation = this._splitPane.getDividerLocation();
        this._splitPane.setRightComponent(v);
        this._splitPane.setDividerLocation(curDividerLocation);
    }
}
