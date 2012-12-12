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

public class TmxObjectGroupAssembler extends TmxElementAssembler {
	public TmxObjectGroupAssembler(TmxObjectGroup objectGroup) {
		super(objectGroup);
		
		this.objectGroup = objectGroup;
	}
	
	@Override
	void assemble(TmxObject obj) {
		objectGroup.addObject(obj);
	}
	
	@Override
	void assemble(TmxObjectTile obj) {
		objectGroup.addObject(obj);
	}
	
	@Override
	void assemble(TmxObjectPolygon obj) {
		objectGroup.addObject(obj);
	}
	
	@Override
	void assemble(TmxObjectPolyline obj) {
		objectGroup.addObject(obj);
	}
	
	@Override
	public void assemble(TmxProperties properties) {
		objectGroup.setProperties(properties);
	}

	private TmxObjectGroup objectGroup;
}