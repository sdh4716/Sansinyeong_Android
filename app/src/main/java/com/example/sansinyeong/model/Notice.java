package com.example.sansinyeong.model;

public class Notice {

   private String documentId;
   private String nttitle;
   private String ntcontent;

//
    public Notice() {

    }
    public Notice(String documentId,String title,String content){
        this.documentId=documentId;
        this.nttitle=title;
        this.ntcontent=content;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getNttitle() {
        return nttitle;
    }

    public void setNttitle(String nttitle) {
        this.nttitle = nttitle;
    }

    public String getNtcontent() {
        return ntcontent;
    }

    public void setNtcontent(String ntcontent) {
        this.ntcontent = ntcontent;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "documentId='" + documentId + '\'' +
                ", nttitle='" + nttitle + '\'' +
                ", ntcontent='" + ntcontent + '\'' +
                '}';
    }
}
