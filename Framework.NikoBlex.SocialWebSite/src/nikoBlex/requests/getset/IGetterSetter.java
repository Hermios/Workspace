package nikoBlex.requests.getset;

import javax.servlet.http.HttpServletRequest;

public interface IGetterSetter {
	void initialize(HttpServletRequest request);
	int getAuthorizationLevel();
}