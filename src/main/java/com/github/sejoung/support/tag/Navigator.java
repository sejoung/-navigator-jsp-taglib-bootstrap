package com.github.sejoung.support.tag;



import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;



public class Navigator extends BodyTagSupport{

	private static final long serialVersionUID = 1L;
	/**
	 * 전체 리스트의 카운터
	 */
	private Long totalRecordCount;
	/**
	 * 
	 */
	private Long countPerPage;
	/**
	 * 
	 */
	private Long pagePerBlock;
	/**
	 * 선택된 페이지 
	 */
	private Long currentPage;
	/**
	 * 호출할 스크립트 function name
	 */
	private Long currentBlock;
	private Long startPageOfBlock;
	private Long pageCount;
	private Long blockCount;
	private Long firstPage;
	private Long lastPage;
	private Long prevPage;
	private Long nextPage;

	public Navigator(){
	}
	
	public int doStartTag() throws JspException {
	    try {
	        JspWriter out = pageContext.getOut();
	        out.println(makeNavigator().toString());
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return SKIP_BODY;
	}    
	
	/**
	 * 처음 페이징 변수를 셋팅한다.
	 */
	private void init(){
	    if(totalRecordCount.longValue() % countPerPage.longValue() == 0)
	        pageCount = Long.valueOf(totalRecordCount.longValue() / countPerPage.longValue());
	    else
	        pageCount = Long.valueOf(totalRecordCount.longValue() / countPerPage.longValue() + 1);
	    if(pageCount.longValue() % pagePerBlock.longValue() == 0)
	        blockCount = Long.valueOf(pageCount.longValue() / pagePerBlock.longValue());
	    else
	        blockCount = Long.valueOf(pageCount.longValue() / pagePerBlock.longValue() + 1);
	    currentBlock = Long.valueOf((currentPage.longValue() - 1) / pagePerBlock.longValue() + 1);
	    startPageOfBlock = Long.valueOf((currentBlock.longValue() - 1) * pagePerBlock.longValue() + 1);
	    firstPage =  Long.valueOf("1");
	    lastPage = pageCount;
	    prevPage = Long.valueOf(startPageOfBlock.longValue() != 1 ? startPageOfBlock.longValue() - 1 : 1);
	    nextPage = Long.valueOf(currentBlock != blockCount ? startPageOfBlock.longValue() + pagePerBlock.longValue() : lastPage.longValue());
	    if(totalRecordCount.longValue() == 0)
	        nextPage = Long.valueOf("0");
	}
	
	/**
	 *  페이지를 받아서 링크를 생성 시킨다
	 * @param page
	 * @return
	 */
	private String makeLink(Long page){
	    StringBuffer rtn = new StringBuffer();
	    if(currentPage.longValue() == page.longValue()) {
	    	rtn.append(rtn).append("<li class=\"active\"><a href=\"#\">").append(page).append("<span class=\"sr-only\">(current)</span></a></li>").toString();
        } else {
        	rtn.append(rtn).append("<li onclick=\"javascript:goPage('"+page+"');\"><a href=\"#\">").append(page).append("</a></li>");
        }
	    return rtn.toString();
	}
	
	
	/**
	 * 
	 * 해당 디자인의 페이지 네이게이션을 리턴한다.
	 * 
	 * @return pageNavigation String
	 */
	private StringBuffer makeNavigator(){
	    StringBuffer buffer = new StringBuffer();
	    init();
	    buffer.append("<nav>");
	    buffer.append("<ul class=\"pagination\">");
	    if(prevPage.longValue() == startPageOfBlock.longValue()){
            buffer.append("<li class=\"disabled\"><a href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li>").append("\r\n");
	    }else{
            buffer.append("<li onclick=\"javascript:goPage('"+firstPage+"');\"><a href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li>").append("\r\n");
	    }
	    Long showPage = startPageOfBlock;
	    for(int i = 0; (long)i < pagePerBlock.longValue(); i++) {        	
	        buffer.append((new StringBuilder()).append(makeLink(showPage)).append("\r\n").toString());
	        if(totalRecordCount.longValue() == 0)
	            break;
	        if(showPage.longValue() == pageCount.longValue()) {
	            showPage = Long.valueOf(showPage.longValue() + 1);
	            break;
	        }
	        showPage = Long.valueOf(showPage.longValue() + 1);
	    }
	
	    showPage = Long.valueOf(showPage.longValue() - 1);
	    if(showPage.longValue() == nextPage.longValue()){
            buffer.append("<li class=\"disabled\"><a href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">&raquo;</span></a></li>").append("\r\n");
	    }else{
            buffer.append("<li onclick=\"javascript:goPage('"+nextPage+"');\"><a href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">&raquo;</span></a></li>").append("\r\n");
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
	
	public Long getPagePerBlock(){
	    return pagePerBlock;
	}
	
	public void setCurrentPage(Long value){
	    currentPage = value;
	}
	
	public Long getCurrentPage(){
	    return currentPage;
	}
    
}
