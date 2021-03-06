package com.polydes.paint.app.editors.image.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

import com.polydes.common.res.ResourceLoader;
import com.polydes.common.res.Resources;
import com.polydes.paint.app.editors.image.DrawArea;
import com.polydes.paint.app.editors.image.ImageUtils;
import com.polydes.paint.app.editors.image.ShapeUtils;

public class Rectangle implements Tool, ActionListener
{
	private static Resources res = ResourceLoader.getResources("com.polydes.paint");
	
	private DrawArea area;
	private Point beginPress;
	private java.awt.Rectangle oldDraw;

	private ToolOptions options;
	private JToggleButton outline;
	private JToggleButton filled;
	
	private boolean drawFilled;
	
	public Rectangle()
	{
		beginPress = null;
		oldDraw = new java.awt.Rectangle(0, 0, 1, 1);
		
		ButtonGroup fillGroup = new ButtonGroup();
		outline = new JToggleButton(res.loadIcon("draw/outline.png"));
		filled = new JToggleButton(res.loadIcon("draw/filled.png"));
		outline.setAlignmentX(.5f);
		filled.setAlignmentX(.5f);
		outline.addActionListener(this);
		filled.addActionListener(this);
		outline.setSelected(true);
		drawFilled = false;
		
		fillGroup.add(outline);
		fillGroup.add(filled);
		
		options = new ToolOptions();
		options.add(outline);
		options.add(filled);
		options.glue();
	}
	
	@Override
	public void setArea(DrawArea area)
	{
		this.area = area;
	}

	@Override
	public void press(int x, int y)
	{
		beginPress = new Point(x, y);
	}

	@Override
	public void drag(int x, int y)
	{
		area.repaint(oldDraw.x, oldDraw.y, oldDraw.width, oldDraw.height);
		oldDraw.x = Math.min(beginPress.x, x);
		oldDraw.y = Math.min(beginPress.y, y);
		oldDraw.width = Math.max(beginPress.x, x) - oldDraw.x + 2;
		oldDraw.height = Math.max(beginPress.y, y) - oldDraw.y + 2;
		area.repaint(oldDraw.x, oldDraw.y, oldDraw.width, oldDraw.height);
	}

	@Override
	public void release(int x, int y)
	{
		ShapeUtils.clip(0, 0, area.img.getWidth(), area.img.getHeight());
		
		Point[] points;
		if(drawFilled)
			points = ShapeUtils.getFilledRectangle(beginPress.x, beginPress.y, x, y);
		else
			points = ShapeUtils.getRectangle(beginPress.x, beginPress.y, x, y);
		
		ImageUtils.drawPoints(area.img, points, area.currentRGB);
		area.setDirty();
		
		beginPress = null;
	}

	@Override
	public void enter(int x, int y)
	{
	}

	@Override
	public void exit(int x, int y)
	{
	}

	@Override
	public ToolOptions getOptions()
	{
		return options;
	}

	@Override
	public void move(int x, int y)
	{
	}

	@Override
	public void render(Graphics g, int x, int y, int w, int h)
	{
		if(beginPress == null)
			return;
		
		ShapeUtils.clip(x, y, w, h);
		
		Point[] points;
		if(drawFilled)
			points = ShapeUtils.getFilledRectangle(beginPress.x, beginPress.y, area.lastUpdated.x, area.lastUpdated.y);
		else
			points = ShapeUtils.getRectangle(beginPress.x, beginPress.y, area.lastUpdated.x, area.lastUpdated.y);
		
		int s = (int) area.scale;
		g.setColor(area.currentColor);
		for(Point p : points)
		{
			g.fillRect(p.x * s, p.y * s, s, s);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == outline)
			drawFilled = false;
		else if(e.getSource() == filled)
			drawFilled = true;
	}
}
