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

/**
 * 
 */
public class TmxTileLayer implements TmxLayer {

	/**
	 * @return the layer
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the opacity
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * @param opacity
	 *            the opacity to set
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the data
	 */
	public TmxData getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(TmxData data) {
		this.data = data;
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
		return "TMX Tile Layer";
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxTileLayerAssembler(this);
	}

	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * Name of the layer
	 */
	private String name;

	/**
	 * Opacity for the layer from 0 to 1
	 */
	private float opacity = 1;

	/**
	 * Whether the layer is visible or not
	 */
	private boolean visible = true;

	/**
	 * Layer tile disposition, which might be compressed and encode or as a
	 * plain XML
	 */
	private TmxData data;

	/**
	 * Properties for this map indexed by name
	 */
	private TmxProperties properties;
}
