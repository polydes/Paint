package com.polydes.paint.app.pages;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.polydes.common.comp.MiniSplitPane;
import com.polydes.common.nodes.DefaultBranch;
import com.polydes.common.nodes.DefaultLeaf;
import com.polydes.common.nodes.HierarchyModel;
import com.polydes.common.nodes.NodeSelection;
import com.polydes.common.nodes.NodeSelectionEvent;
import com.polydes.common.nodes.NodeSelectionListener;
import com.polydes.common.ui.darktree.DarkTree;

public class BasicPage extends JPanel implements NodeSelectionListener<DefaultLeaf,DefaultBranch>
{
	protected Boolean listEditEnabled;
	
	protected MiniSplitPane splitPane;
	protected HierarchyModel<DefaultLeaf,DefaultBranch> folderModel;
	protected DarkTree<DefaultLeaf,DefaultBranch> tree;
	
	protected NodeSelection<DefaultLeaf,DefaultBranch> selection;
	
	protected BasicPage()
	{
		super(new BorderLayout());
	}
	
	protected BasicPage(final DefaultBranch rootFolder)
	{
		super(new BorderLayout());
		
		folderModel = new HierarchyModel<DefaultLeaf,DefaultBranch>(rootFolder, DefaultLeaf.class, DefaultBranch.class);
		tree = new DarkTree<DefaultLeaf,DefaultBranch>(folderModel);
		
		splitPane = new MiniSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(tree);
		
		add(splitPane);
		
		splitPane.setDividerLocation(DarkTree.DEF_WIDTH);
		
		folderModel.getSelection().addSelectionListener(this);
		
		tree.forceRerender();
	}
	
	public void setListEditEnabled(boolean value)
	{
		if(listEditEnabled == null || listEditEnabled != value)
		{
			listEditEnabled = value;
			if(listEditEnabled)
			{
				tree.setListEditEnabled(true);
			}
			else
			{
				tree.setListEditEnabled(false);
			}
		}
	}
	
	@Override
	public void selectionChanged(NodeSelectionEvent<DefaultLeaf, DefaultBranch> e)
	{
		
	}
}