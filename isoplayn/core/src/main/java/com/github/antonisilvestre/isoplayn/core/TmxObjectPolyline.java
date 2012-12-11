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

import java.util.LinkedList;

public class TmxObjectPolyline extends TmxObject {

	/**
	 * @return the coords
	 */
	public LinkedList<Coord> getCoords() {
		return coords;
	}

	/**
	 * @param coords
	 *            the coords to set
	 */
	public void setCoords(LinkedList<Coord> coords) {
		this.coords = coords;
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
		return new TmxObjectPolylineAssembler(this);
	}

	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * List of coords that make up the polygon
	 */
	private LinkedList<Coord> coords = new LinkedList<Coord>();
}
