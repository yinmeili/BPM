package com.h3bpm.web.vo;

public class ReqListAnnouncementPageVo extends ReqPageVo {
    private int type = 0;
    private String title = null;
    private String createTimeStart = null;
    private String createTimeEnd = null;

	/*@JsonFormat(pattern="YYYY-MM-dd HH:mm:ss")
	private Date createTimeStart = null;

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private Date createTimeEnd = null;*/

    @Deprecated
    public ReqListAnnouncementPageVo() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }
}
