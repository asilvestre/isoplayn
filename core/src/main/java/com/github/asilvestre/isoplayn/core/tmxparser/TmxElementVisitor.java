/**
 * 
 */
package com.github.asilvestre.isoplayn.core.tmxparser;

/**
 * Superclass for all visitors of TMX elements. Implements the visitor pattern
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
	
	void visit(TmxTerrain terrain);
	
	void visit(TmxTerrainTypes tmxTerrainTypes);

	public static class Default implements TmxElementVisitor {

		@Override
		public void visit(TmxMap map) {
		}

		@Override
		public void visit(TmxData data) {
		}

		@Override
		public void visit(TmxImage image) {
		}

		@Override
		public void visit(TmxTileLayer layer) {
		}

		@Override
		public void visit(TmxObjectTile objectTile) {
		}

		@Override
		public void visit(TmxObjectPolygon objectPolygon) {
		}

		@Override
		public void visit(TmxObjectPolyline objectPolyline) {
		}

		@Override
		public void visit(TmxObject object) {
		}

		@Override
		public void visit(TmxObjectGroup objectGroup) {
		}

		@Override
		public void visit(TmxProperty property) {
		}

		@Override
		public void visit(TmxTileOffset tileOffset) {
		}

		@Override
		public void visit(TmxTileset tileset) {
		}

		@Override
		public void visit(TmxTile tile) {
		}

		@Override
		public void visit(TmxProperties properties) {
		}

		@Override
		public void visit(TmxDataTile tile) {
		}
		
		@Override
		public void visit(TmxTerrain terrain) {
		}
		
		@Override
		public void visit(TmxTerrainTypes terrainTypes) {
		}
	}
}
