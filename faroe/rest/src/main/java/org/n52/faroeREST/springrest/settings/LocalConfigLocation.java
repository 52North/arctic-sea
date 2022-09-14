package org.n52.faroeREST.springrest.settings;

import org.n52.janmayen.ConfigLocationProvider;

public class LocalConfigLocation implements ConfigLocationProvider {

	@Override
	public String get() {
		return "E:\\52 North\\arctic-sea\\faroe\\rest";
	}

}
