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
 * Interface that defines an assembler object, it is used to assemble one TMX
 * element to another
 */
public abstract class TmxElementAssembler {
	protected TmxElementAssembler(TmxElement elem) {
		this.elem = elem;
	}

	public void assemble(TmxMap map) throws TmxInvalidAssembly {
		throwAssemblyError(map);
	}

	void assemble(TmxImage image) throws TmxInvalidAssembly {
		throwAssemblyError(image);
	}

	void assemble(TmxData data) throws TmxInvalidAssembly {
		throwAssemblyError(data);
	}

	void assemble(TmxTileLayer layer) throws TmxInvalidAssembly {
		throwAssemblyError(layer);
	}

	void assemble(TmxObjectTile obj) throws TmxInvalidAssembly {
		throwAssemblyError(obj);
	}
	
	void assemble(TmxObjectPolygon obj) throws TmxInvalidAssembly {
		throwAssemblyError(obj);
	}
	
	void assemble(TmxObjectPolyline obj) throws TmxInvalidAssembly {
		throwAssemblyError(obj);
	}

	void assemble(TmxObject obj) throws TmxInvalidAssembly {
		throwAssemblyError(obj);
	}

	void assemble(TmxObjectGroup objectGroup) throws TmxInvalidAssembly {
		throwAssemblyError(objectGroup);
	}

	void assemble(TmxProperty property) throws TmxInvalidAssembly {
		throwAssemblyError(property);
	}

	void assemble(TmxProperties properties) throws TmxInvalidAssembly {
		throwAssemblyError(properties);
	}

	void assemble(TmxTileOffset tileOffset) throws TmxInvalidAssembly {
		throwAssemblyError(tileOffset);
	}

	void assemble(TmxTileset tileset) throws TmxInvalidAssembly {
		throwAssemblyError(tileset);
	}

	void assemble(TmxTile tile) throws TmxInvalidAssembly {
		throwAssemblyError(tile);
	}

	void assemble(TmxDataTile tile) throws TmxInvalidAssembly {
		throwAssemblyError(tile);
	}

	protected void throwAssemblyError(TmxElement assemblee) throws TmxInvalidAssembly {
		throw new TmxInvalidAssembly(String.format("Cannot assemble a %s into a %s", assemblee.description(),
				elem.description()));
	}

	private TmxElement elem;
}
