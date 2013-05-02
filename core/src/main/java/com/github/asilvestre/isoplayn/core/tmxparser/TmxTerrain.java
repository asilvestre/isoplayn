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
 * Class that represents a TMX Terrain
 */
public class TmxTerrain implements TmxElement {

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the tile
     */
    public int getTile() {
        return tile;
    }

    /**
     * @param tile the tile to set
     */
    public void setTile(int tile) {
        this.tile = tile;
    }

    /**
     * @return the properties
     */
    public TmxProperties getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(TmxProperties properties) {
        this.properties = properties;
    }

    @Override
    public void accept(TmxElementVisitor visitor) {
	visitor.visit(this);

	properties.accept(visitor);
    }

    @Override
    public String description() {
	return "TMX Terrain";
    }

    @Override
    public TmxElementAssembler createAssembler() {
	return new TmxTerrainAssembler(this);
    }

    @Override
    public void getAssembled(TmxElementAssembler assembler) throws TmxInvalidAssembly {
	assembler.assemble(this);
    } 
    
    /**
     * Name of the terrain type
     */
    private String name;
    
    /**
     * The local tile-id of the file that represent that terrain visually
     */
    private int tile;
    
    /**
     * Properties for this map indexed by name
     */
    private TmxProperties properties = new TmxProperties();

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((properties == null) ? 0 : properties.hashCode());
	result = prime * result + tile;
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
	TmxTerrain other = (TmxTerrain) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (properties == null) {
	    if (other.properties != null)
		return false;
	} else if (!properties.equals(other.properties))
	    return false;
	if (tile != other.tile)
	    return false;
	return true;
    }
}