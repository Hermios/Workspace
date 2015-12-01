package nikoBlex.requests.getset;

import javax.servlet.http.HttpServletRequest;

import nikoBlex.requests.get.IGetter;
import nikoBlex.requests.post.ISetter;

public class GetterSetterFactory {
	public static <T extends IGetter> T constructGetter(Class<T> getset,HttpServletRequest request)
	{
		T instance;
		try {
			instance = getset.newInstance();
			instance.initialize(request);
			return instance;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T extends ISetter> T constructSetter(Class<T> getset,HttpServletRequest request)
	{
		T instance;
		try {
			instance = getset.newInstance();
			instance.initialize(request);
			return instance;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
