package com.esempla.test.demo.service.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "comments")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentsDtoXml {
    @XmlElement(name = "comment")
    private List<CommentDtoXml> commentDtoXmlList = null;



    public List<CommentDtoXml> getCommentDtoXmlList() {
        return commentDtoXmlList;
    }

    public void setCommentDtoXmlList(List<CommentDtoXml> commentDtoXmlList) {
        this.commentDtoXmlList = commentDtoXmlList;
    }
}
