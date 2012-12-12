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
 * 
 */
public class TmxTilesetAssembler extends TmxElementAssembler {
	
	public TmxTilesetAssembler(TmxTileset tileset)
	{
		super(tileset);
		
		this.tileset = tileset;
	}
	
	@Override
	public void assemble(TmxTileOffset tileOffset) throws TmxInvalidAssembly {
		tileset.setTileOffset(tileOffset);
	}
	
	@Override
	public void assemble(TmxProperties properties) throws TmxInvalidAssembly {
		tileset.setProperties(properties);
	}
	
	@Override
	public void assemble(TmxImage image) throws TmxInvalidAssembly {
		tileset.setImage(image);
	}
	
	@Override
	public void assemble(TmxTile tile) throws TmxInvalidAssembly {
		tileset.addCustomTile(tile);
	}
	
	private TmxTileset tileset;
}
