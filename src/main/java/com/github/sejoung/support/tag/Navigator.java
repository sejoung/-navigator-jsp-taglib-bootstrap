package com.github.sejoung.support.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author sejoung
 * 
 */
public class Navigator extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	/**
	 * totalRecordCount
	 */
	private Long totalRecordCount;

	/**
	 * countPerPage
	 */
	private Long countPerPage;

	/**
	 * pagePerBlock
	 */
	private Long pagePerBlock;

	/**
	 * currentPage
	 */
	private Long currentPage;

	/**
	 * currentBlock
	 */
	private Long currentBlock;

	/**
	 * startPageOfBlock
	 */
	private Long startPageOfBlock;

	/**
	 * pageCount
	 */
	private Long pageCount;

	/**
	 * blockCount
	 */
	private Long blockCount;

	/**
	 * firstPage
	 */
	private Long firstPage;

	/**
	 * lastPage
	 */
	private Long lastPage;

	/**
	 * prevPage
	 */
	private Long prevPage;

	/**
	 * nextPage
	 */
	private Long nextPage;

	public Navigator() {
	}

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.println(makeNavigator().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	/**
	 * init variable
	 */
	private void init() {
		if (totalRecordCount.longValue() % countPerPage.longValue() == 0)
			pageCount = Long.valueOf(totalRecordCount.longValue() / countPerPage.longValue());
		else
			pageCount = Long.valueOf(totalRecordCount.longValue() / countPerPage.longValue() + 1);
		if (pageCount.longValue() % pagePerBlock.longValue() == 0)
			blockCount = Long.valueOf(pageCount.longValue() / pagePerBlock.longValue());
		else
			blockCount = Long.valueOf(pageCount.longValue() / pagePerBlock.longValue() + 1);
		currentBlock = Long.valueOf(currentPage.longValue() / pagePerBlock.longValue() + 1);
		startPageOfBlock = Long.valueOf((currentBlock.longValue() - 1) * pagePerBlock.longValue() + 1);
		firstPage = Long.valueOf("0");
		lastPage = pageCount;
		prevPage = Long.valueOf(startPageOfBlock.longValue() != 1 ? startPageOfBlock.longValue() - 1 : 1);
		nextPage = Long.valueOf(currentBlock != blockCount ? (startPageOfBlock.longValue() + pagePerBlock.longValue())-1 : lastPage.longValue());
		if (totalRecordCount.longValue() == 0)
			nextPage = Long.valueOf("0");
	}

	/**
	 * page number makelink
	 * 
	 * @param page
	 * @return html
	 */
	private String makeLink(Long page) {
		long page_n = (page.longValue()-1);
		StringBuffer rtn = new StringBuffer();
		if (currentPage.longValue() == page_n) {
			rtn.append(rtn).append("<li class=\"active\"><a href=\"#\">").append(page).append("<span class=\"sr-only\">(current)</span></a></li>");
		} else {
			rtn.append(rtn).append("<li onclick=\"javascript:goPage('" + page_n + "');\"><a href=\"#\">").append(page).append("</a></li>");
		}
		return rtn.toString();
	}

	/**
	 * 
	 * make full navigation html
	 * 
	 * @return pageNavigation html
	 */
	private StringBuffer makeNavigator() {
		StringBuffer buffer = new StringBuffer();
		init();
		buffer.append("<nav>");
		buffer.append("<ul class=\"pagination\">");
		if (prevPage.longValue() == startPageOfBlock.longValue()) {
			buffer.append("<li class=\"disabled\"><a href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li>").append("\r\n");
		} else {
			buffer.append("<li onclick=\"javascript:goPage('" + (prevPage.longValue()-1) + "');\"><a href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li>").append("\r\n");
		}
		Long showPage = startPageOfBlock;
		for (int i = 0; (long) i < pagePerBlock.longValue(); i++) {
			buffer.append((new StringBuilder()).append(makeLink(showPage)).append("\r\n").toString());
			if (totalRecordCount.longValue() == 0)
				break;
			if (showPage.longValue() == pageCount.longValue()) {
				showPage = Long.valueOf(showPage.longValue() + 1);
				break;
			}
			showPage = Long.valueOf(showPage.longValue() + 1);
		}

		showPage = Long.valueOf(showPage.longValue() - 1);
		
		if(showPage.longValue() == lastPage.longValue()){
			buffer.append("<li class=\"disabled\"><a href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">&raquo;</span></a></li>").append("\r\n");
		} else {
			buffer.append("<li onclick=\"javascript:goPage('" + nextPage + "');\"><a href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">&raquo;</span></a></li>").append("\r\n");
		}
		buffer.append("</ul>\r\n");
		buffer.append("</nav>\r\n");
		return buffer;
	}

	public void setTotalRecordCount(Long value) {
		totalRecordCount = value;
	}

	public Long getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setCountPerPage(Long value) {
		countPerPage = value;
	}

	public Long getCountPerPage() {
		return countPerPage;
	}

	public void setPagePerBlock(Long value) {
		pagePerBlock = value;
	}

	public Long getPagePerBlock() {
		return pagePerBlock;
	}

	public void setCurrentPage(Long value) {
		currentPage = value;
	}

	public Long getCurrentPage() {
		return currentPage;
	}

}
