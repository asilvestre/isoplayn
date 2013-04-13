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

package com.github.asilvestre.isoplayn.core.tmxparser;

import java.util.Iterator;
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

		// visiting all related objects
		properties.accept(visitor);
		tileOffset.accept(visitor);

		if (image != null) {
			image.accept(visitor);
		}
		
		Iterator<TmxTile> iterTiles = customTiles.values().iterator();
		while (iterTiles.hasNext()) {
			iterTiles.next().accept(visitor);
		}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customTiles == null) ? 0 : customTiles.hashCode());
		result = prime * result + firstgid;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + margin;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + spacing;
		result = prime * result + ((tileOffset == null) ? 0 : tileOffset.hashCode());
		result = prime * result + tileheight;
		result = prime * result + tilewidth;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TmxTileset other = (TmxTileset) obj;
		if (customTiles == null) {
			if (other.customTiles != null)
				return false;
		} else if (!customTiles.equals(other.customTiles))
			return false;
		if (firstgid != other.firstgid)
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (margin != other.margin)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (spacing != other.spacing)
			return false;
		if (tileOffset == null) {
			if (other.tileOffset != null)
				return false;
		} else if (!tileOffset.equals(other.tileOffset))
			return false;
		if (tileheight != other.tileheight)
			return false;
		if (tilewidth != other.tilewidth)
			return false;
		return true;
	}
}
