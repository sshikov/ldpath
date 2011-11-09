/*
 * Copyright (c) 2011 Salzburg Research.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.newmedialab.ldpath.model.selectors;

import at.newmedialab.ldpath.api.selectors.NodeSelector;

/**
 * A property selector that will match with any property
 * <p/>
 * User: sschaffe
 */
public class WildcardSelector extends PropertySelector implements NodeSelector {

	public WildcardSelector() {
		super(null);
	}

	@Override
	public String asRdfPathExpression() {
		return "*";
	}
}
