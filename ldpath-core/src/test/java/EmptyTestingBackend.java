import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ThreadPoolExecutor;

import at.newmedialab.ldpath.model.backend.AbstractBackend;


public class EmptyTestingBackend extends
		AbstractBackend<String> {
	@Override
	public boolean supportsThreading() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ThreadPoolExecutor getThreadPool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLiteral(String n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isURI(String n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBlank(String n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Locale getLiteralLanguage(String n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getLiteralType(String n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createLiteral(String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createLiteral(String content, Locale language,
			URI type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createURI(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> listObjects(String subject,
			String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> listSubjects(String property,
			String object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stringValue(String node) {
		// TODO Auto-generated method stub
		return null;
	}
}