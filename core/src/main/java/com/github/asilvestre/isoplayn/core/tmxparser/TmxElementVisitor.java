/**
 * 
 */
package com.github.asilvestre.isoplayn.core.tmxparser;

/**
 * Superclass for all visitors of TMX elements.
 * Implements the visitor pattern
 */
public interface TmxElementVisitor {
	void visit(TmxMap map);
	void visit(TmxData data);
	void visit(TmxImage image);
	void visit(TmxTileLayer layer);
	void visit(TmxObjectTile objectTile);
	void visit(TmxObjectPolygon objectPolygon);
	void visit(TmxObjectPolyline objectPolyline);
	void visit(TmxObject object);
	void visit(TmxObjectGroup objectGroup);
	void visit(TmxProperty property);
	void visit(TmxTileOffset tileOffset);
	void visit(TmxTileset tileset);
	void visit(TmxTile tile);
	void visit(TmxProperties properties);
	void visit(TmxDataTile tile);
}
