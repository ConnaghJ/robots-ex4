package rp.robotics.visualisation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import lejos.geom.Point;
import lejos.robotics.mapping.LineMap;
import rp.robotics.mapping.IGridMap;

/***
 * Visualise an IGridMap on top of a LineMap.
 * 
 * @author Nick Hawes
 *
 */
public class GridMapVisualisation extends LineMapVisualisation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected IGridMap m_gridMap;
	protected float m_scaleFactor;

	private double xScale;
	private double yScale;
	private float xInset;
	private float yInset;

	
	public GridMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor, boolean _flip) {
		super(_lineMap, _scaleFactor, _flip);
		m_scaleFactor = _scaleFactor;
		m_gridMap = _gridMap;
		xScale = _lineMap.getBoundingRect().getWidth() / (_gridMap.getXSize() -_scaleFactor) ;
		yScale = _lineMap.getBoundingRect().getHeight()/ (_gridMap.getYSize());
		xInset = _gridMap.getXOffset();
		yInset = _gridMap.getYOffset();
	}

	public GridMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor) {
		this(_gridMap, _lineMap, _scaleFactor, false);
	}

	private void connectToNeighbour(Graphics2D _g2, int _x, int _y, int _dx,int _dy) 
	{
		if (m_gridMap.isValidTransition(_x, _y, _x + _dx, _y + _dy)) 
		{
			Point p1 = m_gridMap.getCoordinatesOfGridPosition((int)(_x*xScale),(int)( _y*yScale));
			Point p2 = m_gridMap.getCoordinatesOfGridPosition((int)((_x + _dx)*xScale),(int)(( _y + _dy)*yScale));
			renderLine(p1, p2, _g2);
		}

	}

	@Override
	protected void renderMap(Graphics2D _g2) {
		// render lines first
		super.renderMap(_g2);

		_g2.setStroke(new BasicStroke(1));
		_g2.setPaint(Color.BLUE);

		// add grid
		for (int x = 0; x < m_gridMap.getXSize(); x++) {
			
			for (int y = 0; y < m_gridMap.getYSize(); y++) {
				
				if (!m_gridMap.isObstructed(x, y)) {
					Point gridPoint = m_gridMap.getCoordinatesOfGridPosition( (int)(xInset + x * xScale), (int)(yInset + y *yScale));
					renderPoint(gridPoint, _g2);
				}
			}
		}

		// and visualise valid connections
		for (int x = 0; x < m_gridMap.getXSize(); x++) {
			for (int y = 0; y < m_gridMap.getYSize(); y++) {

				if (m_gridMap.isValidGridPosition(x, y)) {
					connectToNeighbour(_g2, x, y, 1, 0);
					connectToNeighbour(_g2, x, y, 0, 1);
					connectToNeighbour(_g2, x, y, -1, 0);
					connectToNeighbour(_g2, x, y, 0, -1);
				}
			}
		}

	}

}