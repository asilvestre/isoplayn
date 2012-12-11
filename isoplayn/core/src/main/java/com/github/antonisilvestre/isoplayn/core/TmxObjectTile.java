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
 * Describes an object in an object layer that has a tile
 */
public class TmxObjectTile extends TmxObject {

	@Override
	public void accept(TmxElementVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String description() {
		return "Tmx Object Tile";
	}

	@Override
	public TmxElementAssembler createAssembler() {
		return new TmxObjectTileAssembler(this);
	}

	@Override
	public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
		assembler.assemble(this);
	}

	/**
	 * @return the gid
	 */
	public int getGid() {
		return gid;
	}

	/**
	 * @param gid the gid to set
	 */
	public void setGid(int gid) {
		this.gid = gid;
	}

	private int gid = 0;
}
