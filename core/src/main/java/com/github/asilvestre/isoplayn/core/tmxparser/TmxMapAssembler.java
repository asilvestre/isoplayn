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

/**
 * Assembler for the TMX Map element
 */
public class TmxMapAssembler extends TmxElementAssembler {

	public TmxMapAssembler(TmxMap map) {
		super(map);
		
		this.map = map;
	}

	@Override
	public void assemble(TmxTileLayer layer) {
		map.addLayer(layer);
	}

	@Override
	public void assemble(TmxObjectGroup objectGroup) {
		map.addLayer(objectGroup);
	}

	@Override
	public void assemble(TmxProperties properties) {
		map.setProperties(properties);
	}

	@Override
	public void assemble(TmxTileset tileset) {
		map.addTileset(tileset);
	}

	private TmxMap map;
}
