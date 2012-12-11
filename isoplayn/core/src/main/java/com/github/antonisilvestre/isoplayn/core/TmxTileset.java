/**
 * Copyright Antoni Silvestre
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.antonisilvestre.isoplayn.core;

import java.util.TreeMap;

public class TmxTileset implements TmxElement {

	/**
	 * @return the firstgid
	 */
	public int getFirstgid() {
		return firstgid;
	}

	/**
	 * @param firstgid
	 *            the firstgid to set
	 */
	public void setFirstgid(int firstgid) {
		this.firstgid = firstgid;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the tilewidth
	 */
	public int getTilewidth() {
		return tilewidth;
	}

	/**
	 * @param tilewidth
	 *            the tilewidth to set
	 */
	public void setTilewidth(int tilewidth) {
		this.tilewidth = tilewidth;
	}

	/**
	 * @return the tileheight
	 */
	public int getTileheight() {
		return tileheight;
	}

	/**
	 * @param tileheight
	 *            the tileheight to set
	 */
	public void setTileheight(int tileheight) {
		this.tileheight = tileheight;
	}

	/**
	 * @return the spacing
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * @param spacing
	 *            the spacing to set
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	/**
	 * @return the margin
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * @param margin
	 *            the margin to set
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(TmxProperties properties) {
		this.properties = properties;
	}

	/**
	 * @return the properties for this map, if any
	 */
	public TmxProperties getProperties() {
		return properties;
	}

	/**
	 * @return the tileOffset
	 */
	public TmxTileOffset getTileOffset() {
		return tileOffset;
	}

	/**
	 * @param tileOffset
	 *            the tileOffset to set
	 */
	public void setTileOffset(TmxTileOffset tileOffset) {
		this.tileOffset = tileOffset;
	}

	/**
	 * @return the image
	 */
	public TmxImage getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(TmxImage image) {
		this.image = image;
	}

	/**
	 * @param customTiles
	 *            the customTiles to set
	 */
	public void addCustomTile(TmxTile tile) {
		this.customTiles.put(tile.getId(), tile);
	}

	@Override
	public void accept(TmxElementVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String description() {
		return "TMX Tileset";
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxTilesetAssembler(this);
	}

	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * The first global tile ID of this tileset (this global ID maps to the
	 * first tile in this tileset).
	 */
	private int firstgid = 0;

	/**
	 * If the tileset is stored in an external file, source of the TSX file
	 */
	private String source = "";

	/**
	 * Name of this tileset
	 */
	private String name = "";

	/**
	 * The maximum width of the tiles in this tileset
	 */
	private int tilewidth = 0;

	/**
	 * The maximum height of the tiles in this tileset
	 */
	private int tileheight = 0;

	/**
	 * Spacing in pixels between the tiles in this tileset
	 */
	private int spacing = 0;

	/**
	 * The margin around the tiles in this tileset
	 */
	private int margin = 0;

	/**
	 * Properties for this tileset indexed by name
	 */
	private TmxProperties properties = new TmxProperties();

	/**
	 * Tile offset to apply when drawing a tile from this tileset
	 */
	private TmxTileOffset tileOffset = new TmxTileOffset();

	/**
	 * Source image for this tileset
	 */
	private TmxImage image;

	/**
	 * Custom tiles within this tileset indexed by id
	 */
	private TreeMap<Integer, TmxTile> customTiles = new TreeMap<Integer, TmxTile>();
}
