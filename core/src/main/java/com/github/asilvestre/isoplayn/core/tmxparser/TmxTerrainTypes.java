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

import java.util.Vector;

/**
 * Defines an array of terrain types, which can be referenced from the terrain attribute of the tile element.
 */
public class TmxTerrainTypes implements TmxElement {
 
    /**
     * Adds a terrain to the terrain types which will be able to be referenced by the order it's been inserted
     * @param terrain terrain to add
     */
    public void addTerrain(TmxTerrain terrain) {
	terrains.add(terrain);
    }
    
    /**
     * Gets a terrain by index, if it's not found returns null
     * @param index the index of the terrain to get
     * @return The terrain or null if it's a bad index
     */
    public TmxTerrain getTerrain(int index) {
	TmxTerrain res = null;
    
	if (index < terrains.size() && index >= 0) {
	    res = terrains.get(index);
	}
	
	return res;
    }
    
    /**
     * @return number of terrains
     */
    public int getTerrainCount() {
	return terrains.size();
    }

    @Override
    public void accept(TmxElementVisitor visitor) {
	visitor.visit(this);
	
	for (TmxTerrain terrain : terrains) {
	    visitor.visit(terrain);
	}
    }
   
    @Override
    public String description() {
	return "TMX Terrain Types";
    }

    @Override
    public TmxElementAssembler createAssembler() {
	return new TmxTerrainTypesAssembler(this);
    }

    @Override
    public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
	assembler.assemble(this);
    }

    /**
     *  Array of terrain types, which can be referenced from the terrain attribute of the tile element.
     */
    private Vector<TmxTerrain> terrains = new Vector<TmxTerrain>();

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((terrains == null) ? 0 : terrains.hashCode());
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
	TmxTerrainTypes other = (TmxTerrainTypes) obj;
	if (terrains == null) {
	    if (other.terrains != null)
		return false;
	} else if (!terrains.equals(other.terrains))
	    return false;
	return true;
    }
}
