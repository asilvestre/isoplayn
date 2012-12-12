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

import java.util.Collection;
import java.util.LinkedList;

public class TmxObjectPolygon extends TmxObject {

	/**
	 * @return the coords
	 */
	public Collection<Coord> getCoords() {
		return coords;
	}

	/**
	 * @param coords
	 *            the coords to set
	 */
	public void setCoords(Collection<Coord> coords) {
		this.coords.addAll(coords);
	}

	@Override
	public void accept(TmxElementVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String description() {
		return "TMX Object Polygon";
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxObjectPolygonAssembler(this);
	}

	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * List of coords that make up the polygon
	 */
	private LinkedList<Coord> coords = new LinkedList<Coord>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((coords == null) ? 0 : coords.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TmxObjectPolygon other = (TmxObjectPolygon) obj;
		if (coords == null) {
			if (other.coords != null)
				return false;
		} else if (!coords.equals(other.coords))
			return false;
		return true;
	}
}
