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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

class TmxMap implements TmxElement {
	/**
	 * Different orientations currently orthogonal or isometric
	 */
	public enum Orientations {
		ORTHOGONAL, ISOMETRIC,
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the orientation
	 */
	public Orientations getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation
	 *            the orientation to set
	 */
	public void setOrientation(Orientations orientation) {
		this.orientation = orientation;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
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
	 * @param firstGid
	 * @return tileset identified by firstGid, null if it's not found
	 */
	public TmxTileset getTileset(int firstGid) {
		return tilesets.get(firstGid);
	}

	/**
	 * @return the tilesets
	 */
	public Iterator<TmxTileset> getTilesets() {
		return tilesets.values().iterator();
	}

	/**
	 * @param tilesets
	 *            the tilesets to set
	 */
	public void addTileset(TmxTileset tileset) {
		this.tilesets.put(tileset.getFirstgid(), tileset);
	}

	/**
	 * @return the layers ordered by Z, the first one being at the top
	 */
	public Iterator<TmxLayer> getLayers() {
		return layers.iterator();
	}

	/**
	 * It will add a layer to the map below the current ones
	 * 
	 * @param layer
	 *            to add
	 */
	public void addLayer(TmxLayer layer) {
		layers.add(layer);
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

	@Override
	public void accept(TmxElementVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String description() {
		return "TMX Map";
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxMapAssembler(this);
	}
	
	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * TMX version
	 */
	private String version = "1.0";

	/**
	 * Map orientation
	 */
	private Orientations orientation = Orientations.ORTHOGONAL;

	/**
	 * The map width in tiles
	 */
	private int width = 0;

	/**
	 * The map height in tiles
	 */
	private int height = 0;

	/**
	 * The width of a tile
	 */
	private int tilewidth = 0;

	/**
	 * The height of a tile
	 */
	private int tileheight = 0;

	/**
	 * Tilesets for this map indexed by firstgid
	 */
	private TreeMap<Integer, TmxTileset> tilesets = new TreeMap<Integer, TmxTileset>();

	/**
	 * Layers for this map ordered by Z, being the first the one at the top
	 */
	private LinkedList<TmxLayer> layers = new LinkedList<TmxLayer>();

	/**
	 * Properties for this map indexed by name
	 */
	private TmxProperties properties = new TmxProperties();
}