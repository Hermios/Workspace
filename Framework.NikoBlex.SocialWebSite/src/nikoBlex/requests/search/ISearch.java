package nikoBlex.requests.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface ISearch{
	List<? extends ISearch> getSearchResult(String searchValue,HttpServletRequest request);
	
	public String getSearchDescription();
}
