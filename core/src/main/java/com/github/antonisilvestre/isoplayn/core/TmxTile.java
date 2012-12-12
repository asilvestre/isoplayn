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

public class TmxTile implements TmxElement {

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the horitzontallyFlipped
	 */
	public boolean isHoritzontallyFlipped() {
		return horitzontallyFlipped;
	}

	/**
	 * @param horitzontallyFlipped
	 *            the horitzontallyFlipped to set
	 */
	public void setHoritzontallyFlipped(boolean horitzontallyFlipped) {
		this.horitzontallyFlipped = horitzontallyFlipped;
	}

	/**
	 * @return the verticallyFlipped
	 */
	public boolean isVerticallyFlipped() {
		return verticallyFlipped;
	}

	/**
	 * @param verticallyFlipped
	 *            the verticallyFlipped to set
	 */
	public void setVerticallyFlipped(boolean verticallyFlipped) {
		this.verticallyFlipped = verticallyFlipped;
	}

	/**
	 * @return the diagonallyFlipped
	 */
	public boolean isDiagonallyFlipped() {
		return diagonallyFlipped;
	}

	/**
	 * @param diagonallyFlipped
	 *            the diagonallyFlipped to set
	 */
	public void setDiagonallyFlipped(boolean diagonallyFlipped) {
		this.diagonallyFlipped = diagonallyFlipped;
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
		return "TMX Tile";
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxTileAssembler(this);
	}

	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * Id for this tile
	 */
	private int id = 0;

	/**
	 * States whether the tile is horitzontally flipped
	 */
	private boolean horitzontallyFlipped = false;

	/**
	 * States whether the tile is vertically flipped
	 */
	private boolean verticallyFlipped = false;

	/**
	 * States whether the tile is flipped diagonally
	 */
	private boolean diagonallyFlipped = false;

	/**
	 * Properties for this tileset indexed by name
	 */
	private TmxProperties properties = new TmxProperties();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (diagonallyFlipped ? 1231 : 1237);
		result = prime * result + (horitzontallyFlipped ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + (verticallyFlipped ? 1231 : 1237);
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
		TmxTile other = (TmxTile) obj;
		if (diagonallyFlipped != other.diagonallyFlipped)
			return false;
		if (horitzontallyFlipped != other.horitzontallyFlipped)
			return false;
		if (id != other.id)
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (verticallyFlipped != other.verticallyFlipped)
			return false;
		return true;
	}
}
